/*
 * $Id: EditorUi.js,v 1.74 2014/01/09 19:07:33 gaudenz Exp $
 * Copyright (c) 2006-2010, JGraph Ltd
 */
(function()
{
	/**
	 * Graph Overrides
	 */
	// Sets default style (used in editor.get/setGraphXml below)
	var graphLoadStylesheet = Graph.prototype.loadStylesheet;
	Graph.prototype.loadStylesheet = function()
	{
		graphLoadStylesheet.apply(this, arguments);
		this.currentStyle = 'default-style2';
	};

	/**
	 * Toolbar overrides.
	 */
	Toolbar.prototype.showInsertImage = function(applyFn)
	{
		// TODO: Restore selection after close and after cancel in edit image dialog
		// KNOWN: IE+FF don't return keyboard focus after image dialog (calling focus doesn't help)
		this.editorUi.showDialog(new ImageDialog(this.editorUi, false, mxResources.get('insertImage'), applyFn).container, 320, 200, true, true);
	};
	
	/**
	 * Editor Overrides
	 */
	// Adds support for old stylesheets and compressed files
	var editorSetGraphXml = Editor.prototype.setGraphXml;
	Editor.prototype.setGraphXml = function(node)
	{
		if (node.nodeName == 'mxfile')
		{
			var diagrams = node.getElementsByTagName('diagram');
			
			if (diagrams.length > 0)
			{
				var data = decodeURIComponent(RawDeflate.inflate(Base64.decode(mxUtils.getTextContent(diagrams[0]), true)));
				node = mxUtils.parseXml(data).documentElement;
			}
		}
		
		if (node.nodeName == 'mxGraphModel')
		{
			var style = node.getAttribute('style');

			// Decodes the style if required
			if (urlParams['embed'] != '1' && (style == null || style == ''))
			{
				var node2 = mxUtils.load(STYLE_PATH + '/default-old.xml').getDocumentElement();
				var dec2 = new mxCodec(node2.ownerDocument);
				dec2.decode(node2, this.graph.getStylesheet());
			}
			else if (style != this.graph.currentStyle && style != null)
			{
			    var node2 = mxUtils.load(STYLE_PATH + '/' + style + '.xml').getDocumentElement();
				var dec2 = new mxCodec(node2.ownerDocument);
				dec2.decode(node2, this.graph.getStylesheet());
				this.graph.currentStyle = style;
			}

			this.graph.currentStyle = style;
		}

		// Will call updateGraphComponents
		editorSetGraphXml.apply(this, arguments);
	};

	// Adds persistent style to file
	var editorGetGraphXml = Editor.prototype.getGraphXml;	
	Editor.prototype.getGraphXml = function()
	{
		var node = editorGetGraphXml.apply(this, arguments);
		
		// Encodes the current style
		if (this.graph.currentStyle != null)
		{
			node.setAttribute('style', this.graph.currentStyle);
		}
		
		return node;
	};
	
	/**
	 * EditorUi Overrides
	 */
	// Overrides footer height
	EditorUi.prototype.footerHeight = 0;

	// Fetches footer from page
	EditorUi.prototype.createFooter = function()
	{
		var footer = document.createElement('table');
		footer.className = 'geFooter';
		footer.style.width = '100%';
		footer.style.fontSize = '1em';
		var tbody = document.createElement('tbody');
		var tr = document.createElement('tr');
		var left = document.createElement('td');
		left.style.width = '100%';
		var right = document.createElement('td');
		
		var contents = document.getElementById('geFooter');

		if (contents != null)
		{
			left.appendChild(contents);
			contents.style.visibility = 'visible';
			
			// Adds a small feature to hide the footer
			var img = document.createElement('img');
			img.setAttribute('border', '0');
			img.setAttribute('src', IMAGE_PATH + '/close.png');
			img.setAttribute('title', mxResources.get('hide'));
			img.style.cursor = 'pointer';
			img.style.marginTop = '12px';
			img.style.marginRight = '8px';
			img.style.border = '1px solid transparent';
			img.style.padding = '1px';
			img.style.opacity = 0.5;
			right.appendChild(img)
			
			mxEvent.addListener(img, 'click', mxUtils.bind(this, function()
			{
				this.footerHeight = 0;
				footer.style.display = 'none';
				this.refresh();
			}));
			
			mxEvent.addListener(img, 'mouseover', function()
			{
				img.style.border = '1px solid gray';
				img.style.opacity = 1;
			});
			
			mxEvent.addListener(img, 'mouseout', function()
			{
				img.style.border = '1px solid transparent';
				img.style.opacity = 0.5;
			});
		}
		
		tr.appendChild(left);
		tr.appendChild(right);
		tbody.appendChild(tr);
		footer.appendChild(tbody);
	
		return footer;
	};

	/**
	 * Hook for sidebar footer container.
	 */
	EditorUi.prototype.createSidebarFooterContainer = function()
	{
		var div =  this.createDiv('geSidebarContainer');
		div.style.position = 'absolute';
		div.style.overflow = 'hidden';
		div.style.height = '36px';
		div.style.borderWidth = '3px';

		var elt = document.createElement('a');
		elt.setAttribute('href', 'javascript:void(0);');
		elt.className = 'geTitle';
		elt.style.height = '100%';
		elt.style.paddingTop = '9px';
		mxUtils.write(elt, mxResources.get('moreShapes') + '...');

		mxEvent.addListener(elt, 'click', mxUtils.bind(this, function(evt)
		{
			this.actions.get('moreShapes').funct();
			mxEvent.consume(evt);
		}));
		
		div.appendChild(elt);
		
		return div;
	};

	// Initializes the user interface
	var editorUiInit = EditorUi.prototype.init;
	EditorUi.prototype.init = function()
	{
		editorUiInit.apply(this, arguments);
		
		// Layouts
		var graph = this.editor.graph;
		var layoutMgr = new mxLayoutManager(graph);
		var rackLayout = new mxStackLayout(graph, false);
		
		rackLayout.setChildGeometry = function(child, geo)
		{
			var unitSize = 20;
			geo.height = Math.max(geo.height, unitSize);
			
			if (geo.height / unitSize > 1)
			{
				var mod = geo.height % unitSize;
				geo.height += mod > unitSize / 2 ? (unitSize - mod) : -mod;
			}

			this.graph.getModel().setGeometry(child, geo);
		};

		rackLayout.fill = true;

		layoutMgr.getLayout = function(cell)
		{
			var state = graph.view.getState(cell);
			var style = (state != null) ? state.style : graph.getCellStyle(cell);
			
			// mxRackContainer may be undefined as it is dynamically loaded at render time
			if (typeof(mxRackContainer) != 'undefined' && style['childLayout'] == 'rack')
			{
				rackLayout.unitSize = mxRackContainer.unitSize | 20;
				rackLayout.marginLeft = style['marginLeft'] || 0;
				rackLayout.marginRight = style['marginRight'] || 0;
				rackLayout.marginTop = style['marginTop'] || 0;
				rackLayout.marginBottom = style['marginBottom'] || 0;
				rackLayout.resizeParent = false;
				
				return rackLayout;
			}
			
			return null;
		};
		// end
		
		this.sidebar.showEntries(urlParams['libs']);
		
		var y = Math.max(document.body.clientHeight || 0, document.documentElement.clientHeight || 0) / 2;
		var x = document.body.clientWidth / 2 - 2;
	
		/**
		 * Holds the x-coordinate of the point.
		 * @type number
		 * @default 0
		 */
		this.spinner = this.createSpinner(x, y, 24);
		
		// Adds style input in test mode
		if (urlParams['test'] == '1')
		{
			var footer = document.getElementById('geFooter');

			if (footer != null)
			{
				this.styleInput = document.createElement('input');
				this.styleInput.setAttribute('type', 'text');
				this.styleInput.style.position = 'absolute';
				this.styleInput.style.left = '2px';
				// Workaround for ignore right CSS property in FF
				this.styleInput.style.width = '98%';
				this.styleInput.style.visibility = 'hidden';

				mxEvent.addListener(this.styleInput, 'change', mxUtils.bind(this, function()
				{
					this.editor.graph.getModel().setStyle(this.editor.graph.getSelectionCell(), this.styleInput.value);
				}));

				footer.appendChild(this.styleInput);

				this.editor.graph.getSelectionModel().addListener(mxEvent.CHANGE, mxUtils.bind(this, function(sender, evt)
				{
					if (this.editor.graph.getSelectionCount() > 0)
					{
						var cell = this.editor.graph.getSelectionCell();
						var style = this.editor.graph.getModel().getStyle(cell);

						this.styleInput.value = style || '';
						this.styleInput.style.visibility = 'visible';
					} else
					{
						this.styleInput.style.visibility = 'hidden';
					}
				}));
			}

			var isSelectionAllowed = this.isSelectionAllowed;
			this.isSelectionAllowed = function(evt)
			{
				if (mxEvent.getSource(evt) == this.styleInput)
				{
					return true;
				}

				return isSelectionAllowed.apply(this, arguments);
			};
		}

		// Removes info text in page
		var info = document.getElementById('geInfo');

		if (info != null)
		{
			info.parentNode.removeChild(info);
		}

		// Changes top level error handling
		window.onerror = function(message,url,linenumber)
		{
			try
			{
				if (message != null && url != null &&
					((message.indexOf('Script error') != -1) ||
					(message.indexOf('extension') != -1) ||
					(message.indexOf('olark') != -1)))
				{
					// TODO log external domain script failure "Script error." is
					// reported when the error occurs in a script that is hosted
					// on a domain other than the domain of the current page
				}
				else
				{
					var img = new Image();
		    		img.src = "images/1x1.png?msg=" + encodeURIComponent(message) + "&url=" + encodeURIComponent(url) + "&lnum=" + encodeURIComponent(linenumber) + "&mxvers=" + encodeURIComponent(mxClient.VERSION);
				}
			}
			catch (err)
			{
				// do nothing
			}
		};

		// LATER: Expand the first entry

		// Adds zoom via shift-wheel
		mxEvent.addMouseWheelListener(mxUtils.bind(this, function(evt, up)
		{
			var outlineWheel = false;

			if (this.editor.outline.outline.dialect == mxConstants.DIALECT_SVG)
			{
				var source = mxEvent.getSource(evt);

				while (source != null)
				{
					if (source == this.editor.outline.outline.view.canvas.ownerSVGElement)
					{
						outlineWheel = true;
						break;
					}

					source = source.parentNode;
				}
			}

			if (mxEvent.isShiftDown(evt) || outlineWheel)
			{
				if (up)
				{
					this.editor.graph.zoomIn();
				} else
				{
					this.editor.graph.zoomOut();
				}

				mxEvent.consume(evt);
			}
		}));

		// Initial page layout view, scrollBuffer and timer-based scrolling
		var graph = this.editor.graph;
		var pageBorder = 800;
		graph.timerAutoScroll = true;

		var graphSizeDidChange = graph.sizeDidChange;
		graph.sizeDidChange = function()
		{
			var bounds = this.getGraphBounds();

			if (this.container != null)
			{
				if (mxUtils.hasScrollbars(this.container))
				{
					var border = this.getBorder();

					var t = this.view.translate;
					var s = this.view.scale;
					var width = Math.max(0, bounds.x + bounds.width + border - t.x * s);
					var height = Math.max(0, bounds.y + bounds.height + border - t.y * s);
					var fmt = this.pageFormat;
					var ps = this.pageScale;
					var page = new mxRectangle(0, 0, fmt.width * ps, fmt.height * ps);

					var hCount = (this.pageBreaksVisible) ? Math.max(1, Math.ceil(width / (page.width * s))) : 1;
					var vCount = (this.pageBreaksVisible) ? Math.max(1, Math.ceil(height / (page.height * s))) : 1;

					// Computes unscaled, untranslated graph bounds
					var x = (bounds.width > 0) ? bounds.x / this.view.scale - this.view.translate.x : 0;
					var y = (bounds.height > 0) ? bounds.y / this.view.scale - this.view.translate.y : 0;
					var w = bounds.width / this.view.scale;
					var h = bounds.height / this.view.scale;

					var fmt = this.pageFormat;
					var ps = this.pageScale;

					var pw = fmt.width * ps;
					var ph = fmt.height * ps;

					var x0 = Math.floor(Math.min(0, x) / pw);
					var y0 = Math.floor(Math.min(0, y) / ph);

					hCount -= x0;
					vCount -= y0;

					// Extends the page border based on current scale
					var pb = pageBorder;

					var minWidth = (pb * 2 + pw * hCount);
					var minHeight = (pb * 2 + ph * vCount);
					var m = graph.minimumGraphSize;
					
					if (m == null || m.width != minWidth || m.height != minHeight)
					{
						graph.minimumGraphSize = new mxRectangle(0, 0, minWidth, minHeight);
					}

					var autoDx = pb - x0 * fmt.width;
					var autoDy = pb - y0 * fmt.height;

					if (!this.autoTranslate && (graph.view.translate.x != autoDx || graph.view.translate.y != autoDy))
					{
						this.autoTranslate = true;

						// NOTE: THIS INVOKES THIS METHOD AGAIN. UNFORTUNATELY
						// THERE IS NO WAY AROUND THIS SINCE THE BOUNDS ARE
						// KNOWN AFTER THE VALIDATION AND SETTING THE
						// TRANSLATE TRIGGERS A REVALIDATION. SHOULD
						// MOVE TRANSLATE/SCALE TO VIEW.
						var tx = graph.view.translate.x;
						var ty = graph.view.translate.y;

						graph.view.setTranslate(autoDx, autoDy);
						graph.container.scrollLeft += (autoDx - tx) * graph.view.scale;
						graph.container.scrollTop += (autoDy - ty) * graph.view.scale;

						this.autoTranslate = false;
						return;
					}
				} else
				{
					graph.minimumGraphSize = null;
				}

				graphSizeDidChange.apply(this, arguments);
			}
		};

		// LATER: Cleanup
		graph.getPreferredPageSize = function(bounds, width, height)
		{
			var border = this.getBorder();
			var t = this.view.translate;
			var s = this.view.scale;
			width = Math.max(0, bounds.x + bounds.width + border - t.x * s);
			height = Math.max(0, bounds.y + bounds.height + border - t.y * s);
			var fmt = this.pageFormat;
			var ps = this.pageScale;
			var page = new mxRectangle(0, 0, fmt.width * ps, fmt.height * ps);

			var hCount = (this.pageBreaksVisible) ? Math.max(1, Math.ceil(width / (page.width * s))) : 1;
			var vCount = (this.pageBreaksVisible) ? Math.max(1, Math.ceil(height / (page.height * s))) : 1;
			var gb = this.getGraphBounds();

			// Computes unscaled, untranslated graph bounds
			var x = (gb.width > 0) ? gb.x / this.view.scale - this.view.translate.x : 0;
			var y = (gb.height > 0) ? gb.y / this.view.scale - this.view.translate.y : 0;
			var w = gb.width / this.view.scale;
			var h = gb.height / this.view.scale;

			var fmt = this.pageFormat;
			var ps = this.pageScale;

			var pw = fmt.width * ps;
			var ph = fmt.height * ps;

			var x0 = Math.floor(Math.min(0, x) / pw);
			var y0 = Math.floor(Math.min(0, y) / ph);

			hCount -= x0;
			vCount -= y0;

			return new mxRectangle(0, 0, hCount * page.width + 2, vCount * page.height + 2);
		};

		// LATER: Zoom to multiple pages using minimumGraphSize
		var outlineGetSourceContainerSize = this.editor.outline.getSourceContainerSize;
		this.editor.outline.getSourceContainerSize = function()
		{
			if (mxUtils.hasScrollbars(graph.container))
			{
				var scale = this.source.view.scale;
				
				return new mxRectangle(0, 0, this.source.container.scrollWidth - pageBorder * 2 * scale,
					this.source.container.scrollHeight - pageBorder * 2 * scale);
			}

			return outlineGetSourceContainerSize.apply(this, arguments);
		};

		this.editor.outline.getOutlineOffset = function(scale)
		{
			if (mxUtils.hasScrollbars(graph.container))
			{
				var fmt = this.source.pageFormat;
				var ps = this.source.pageScale;

				var pw = fmt.width * ps;
				var ph = fmt.height * ps;

				var dx = this.outline.container.clientWidth / scale - pw;
				var dy = this.outline.container.clientHeight / scale - ph;

				return new mxPoint(dx / 2 - pageBorder, dy / 2 - pageBorder);
			}

			return null;
		};

		graph.sizeDidChange();

		// Sets the default edge
		var defaultEdge = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=none');
		defaultEdge.geometry.relative = true;
		defaultEdge.edge = true;
		
		graph.setDefaultEdge(defaultEdge);

		// Switch to page view by default
		this.actions.get('pageView').funct();

//		// Embedded mode
//		if (urlParams['embed'] == '1')
//		{
//			var div = document.createElement('div');
//			div.style.display = 'inline-block';
//			div.style.position = 'absolute';
//			div.style.right = '0px';
//			
//			mxEvent.addListener(window, 'message', mxUtils.bind(this, function(evt)
//			{
//				var doc = mxUtils.parseXml(evt.data);
//				this.editor.setGraphXml(doc.documentElement);
//			}));
//			
//			var button = document.createElement('button');
//			mxUtils.write(button, mxResources.get('apply'));
//			button.style.backgroundColor = '#4687CE';
//			button.style.border = '1px solid #486582';
//			button.style.borderRadius = '3px 3px 3px 3px';
//			button.style.fontSize = '14px';
//			button.style.fontWeight = 'bold';
//			button.style.color = '#FFFFFF';
//			button.style.cssFloat = 'right';
//			button.style.styleFloat = 'right';
//			button.style.marginRight = '20px';
//			button.style.padding = '3px';
//			button.style.cursor = 'pointer';
//			
//			mxEvent.addListener(button, 'click', mxUtils.bind(this, function()
//			{
//				this.actions.get('apply').funct();
//			}));
//			
//			div.appendChild(button);
//			
//			button = document.createElement('a');
//			mxUtils.write(button, mxResources.get('cancel'));
//			button.style.cssFloat = 'right';
//			button.style.styleFloat = 'right';
//			button.style.fontSize = '14px';
//			button.style.marginRight = '10px';
//			button.style.padding = '3px';
//			button.style.paddingTop = '6px';
//			button.style.cursor = 'pointer';
//			
//			mxEvent.addListener(button, 'click', mxUtils.bind(this, function()
//			{
//				this.actions.get('cancel').funct();
//			}));
//			
//			div.appendChild(button);
//			this.menubar.container.appendChild(div);
//		}
	};

	/**
	 * Returns the URL for a copy of this editor with no state.
	 */
	EditorUi.prototype.getUrl = function(pathname)
	{
		var href = (pathname != null) ? pathname : window.location.pathname;
		var parms = (href.indexOf('?') > 0) ? 1 : 0;

		// Removes template URL parameter for new blank diagram
		for (var key in urlParams)
		{
			if (key != 'tmp' && key != 'libs' && key != 'state' && key != 'fileId' && key != 'code' && key != 'share' && key != 'url' && key != 'embed')
			{
				if (parms == 0)
				{
					href += '?';
				}
				else
				{
					href += '&';
				}

				href += key + '=' + urlParams[key];
				parms++;
			}
		}

		return href;
	};

	// Replaces save button if alternative I/O is available (Chrome Dev-Channel or Flash)
	EditorUi.prototype.replaceSaveButton = function(elt, dataCallback, filenameCallback, onComplete)
	{
		var result = null;
		var wnd = window;
		wnd.URL = wnd.webkitURL || wnd.URL;
		wnd.BlobBuilder = wnd.BlobBuilder || wnd.WebKitBlobBuilder || wnd.MozBlobBuilder;

		// Prefers BLOB Builder API in Chrome
		/*
		 * if (mxClient.IS_GC && (wnd.URL != null && wnd.BlobBuilder != null)) { //
		 * Experimental Chrome feature result =
		 * mxUtils.button(mxResources.get('save'), mxUtils.bind(this, function() {
		 * var bb = new wnd.BlobBuilder(); bb.append(dataCallback());
		 * 
		 * var a = wnd.document.createElement('a'); a.download =
		 * filenameCallback(); a.href =
		 * wnd.URL.createObjectURL(bb.getBlob('text/plain'));
		 * a.dataset.downloadurl = ['text/plain', a.download, a.href].join(':');
		 * 
		 * var evt = document.createEvent("MouseEvents");
		 * evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false,
		 * false, false, false, 0, null); var allowDefault =
		 * a.dispatchEvent(evt); onComplete(); }));
		 * 
		 * elt.parentNode.replaceChild(result, elt); } else
		 */
		// FIXME:
		// - Possible to hover over button in IE (near right border)
		// - Removes focus from input element while entering filename
		// - No full hover simulation possible, only focus on button
		result = document.createElement('span');
		elt.parentNode.insertBefore(result, elt);

		// Adds a Flash object as a button
		Downloadify.create(result,
		{
			data : dataCallback,
			filename : filenameCallback,
			onComplete : onComplete,
			onCancel : function()
			{
			},
			onError : function()
			{
			},
			swf : 'js/downloadify/downloadify.swf',
			downloadImage : 'js/downloadify/transparent.png',
			width : elt.offsetWidth + 2,
			height : elt.offsetHeight + 2,
			transparent : true,
			append : true
		});

		// Fixes vertical shift of OBJECT node
		var dx = '-6px';

		if (mxClient.IS_IE && document.documentMode == 9)
		{
			dx = '-7px';
		}
		else if (mxClient.IS_IE)
		{
			dx = '-3px';
		}

		result.style.display = 'inline';
		result.style.position = 'absolute';
		result.style.left = (elt.offsetLeft + 20) + 'px';
		result.style.height = (elt.offsetHeight + 2) + 'px';
		result.style.width = (elt.offsetWidth + 2) + 'px';
		result.firstChild.style.marginBottom = dx;

		mxEvent.addListener(result, 'mouseover', function(evt)
		{
			elt.focus();
		});

		mxEvent.addListener(result, 'mouseout', function(evt)
		{
			elt.blur();
		});

		return result;
	};
	
	// Implements cross-tab clipboard
	var clipboard = urlParams['clipboard'] == 'system' && window.clipboardData && clipboardData.setData;
	
	// Shared clipboard via local storage
	if ((urlParams['clipboard'] == 'storage' && isLocalStorage) || clipboard)
	{
		var clipboardKey = '.drawio-clipboard';

		// Adds update of paste action based on local storage changes
		var editorUiInit = EditorUi.prototype.init;
		EditorUi.prototype.init = function()
		{
			editorUiInit.apply(this, arguments);
			
			var paste = this.actions.get('paste');
			
			mxEvent.addListener(window, 'storage', function()
			{
				paste.setEnabled(!mxClipboard.isEmpty());
			});
		};

		mxClipboard.isEmpty = function()
		{
			return (clipboard) ? clipboardData.getData("Text") == null :
				localStorage.getItem(clipboardKey) == null;
		};
		
		mxClipboard.setCells = function(cells)
		{
			var codec = new mxCodec();
			var model = new mxGraphModel();
			var parent = model.getChildAt(model.getRoot(), 0);
			
			for (var i = 0; i < cells.length; i++)
			{
				model.add(parent, cells[i]);
			}

			if (clipboard)
			{
				clipboardData.setData("Text", mxUtils.getXml(codec.encode(model)));
			}
			else
			{
				localStorage.setItem(clipboardKey, mxUtils.getXml(codec.encode(model)));
			}
		};
		
		mxClipboard.getCells = function()
		{
			var xml = (clipboard) ? clipboardData.getData("Text") : localStorage.getItem(clipboardKey);
			var doc = mxUtils.parseXml(xml);
			var codec = new mxCodec(doc);
			var model = codec.decode(doc.documentElement);
			var parent = model.getChildAt(model.getRoot(), 0);
			var childCount = model.getChildCount(parent);
			var cells = [];
			
			for (var i = 0; i < childCount; i++)
			{
				cells.push(model.getChildAt(parent, i));
			}
			
			return cells;
		};
	}

	// Extends Save Dialog to replace Save button
	if (!useLocalStorage)
	{
		EditorUi.prototype.saveFile = function(forceDialog)
		{
			// Required to use new SaveDialog below
			if (!forceDialog && this.editor.filename != null)
			{
				this.save(this.editor.getOrCreateFilename());
			}
			else
			{
				var dlg = new FilenameDialog(this, this.editor.getOrCreateFilename(), mxResources.get('save'), mxUtils.bind(this, function(newFilename)
				{
					this.save(newFilename, true);
				}))
				this.showDialog(dlg.container, 300, 80, true, true);
				dlg.init();
			}

			if (typeof (swfobject) != 'undefined' && swfobject.hasFlashPlayerVersion("10") && typeof (Downloadify) != 'undefined')
			{
				// Extends code for using flash in save button
				if (this.dialog != null)
				{
					// Finds elements inside the current dialog
					var findElt = mxUtils.bind(this, function(tagName)
					{
						var elts = document.getElementsByTagName(tagName);
	
						for ( var i = 0; i < elts.length; i++)
						{
							var parent = elts[i].parentNode;
	
							while (parent != null)
							{
								if (parent == this.dialog.container)
								{
									return elts[i];
								}
	
								parent = parent.parentNode;
							}
						}
	
						return null;
					});
	
					// Replaces the Save button
					var input = findElt('input');
					var saveBtn = findElt('button');
	
					if (input != null && saveBtn != null)
					{
						this.replaceSaveButton(saveBtn, mxUtils.bind(this, function()
						{
							return mxUtils.getXml(this.editor.getGraphXml());
						}),
						mxUtils.bind(this, function()
						{
							return input.value;
						}),
						mxUtils.bind(this, function()
						{
							this.editor.setModified(false);
							this.editor.setFilename(input.value);
							this.updateDocumentTitle();
							this.hideDialog();
						}));
					}
				}
			}
		};
	}
})();