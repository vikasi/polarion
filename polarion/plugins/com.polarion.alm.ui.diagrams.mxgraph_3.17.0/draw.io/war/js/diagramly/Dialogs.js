/*
 * $Id: Dialogs.js,v 1.86 2014/01/10 10:08:32 gaudenz Exp $
 * Copyright (c) 2006-2010, JGraph Ltd
 */
function StorageDialog(editorUi, fn)
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';
	
	editorUi.addLanguageMenu(div);

	var cb = document.createElement('input');
	cb.setAttribute('type', 'checkbox');
	
	function addLogo(img, title, mode)
	{
		var button = document.createElement('div');
		
		var logo = document.createElement('img');
		logo.src = img;
		logo.setAttribute('border', '0');
		logo.setAttribute('align', 'absmiddle');
		logo.style.width = '60px';
		logo.style.height = '60px';
		logo.style.paddingBottom = '4px';
		button.style.display = 'inline-block';
		button.className = 'geBaseButton';
		button.style.margin = '4px';
		button.style.whiteSpace = 'nowrap';
		
		button.appendChild(logo);
		mxUtils.br(button);
		mxUtils.write(button, title);
		
		div.appendChild(button);
		
		mxEvent.addListener(button, 'click', function()
		{
			editorUi.hideDialog();
			
			if (mode == App.MODE_GOOGLE && editorUi.spinner.spin(document.body, mxResources.get('authorizing')))
			{
				// Tries immediate authentication
				editorUi.drive.checkToken(mxUtils.bind(this, function()
				{
					editorUi.spinner.stop();
					editorUi.setMode(mode, cb.checked);
					fn();
				}));
			}
			else
			{
				editorUi.setMode(mode, cb.checked);
				fn();
			}
		});
	};

	var hd = document.createElement('p');
	hd.style.fontSize = '18pt';
	hd.style.padding = '0px';
	hd.style.paddingBottom = '16px';
	hd.style.margin = '0px';
	hd.style.color = 'gray';
	mxUtils.write(hd, mxResources.get('saveDiagramsTo') + ':');
	div.appendChild(hd);

	if (editorUi.drive != null)
	{
		addLogo(IMAGE_PATH + '/google-drive-logo.svg', mxResources.get('googleDrive'), App.MODE_GOOGLE);
	}
	
	if (editorUi.dropbox != null)
	{
		addLogo(IMAGE_PATH + '/dropbox-logo.svg', mxResources.get('dropbox'), App.MODE_DROPBOX);
	}
	
	if (!useLocalStorage || urlParams['nerd'] == '1')
	{
		addLogo(IMAGE_PATH + '/osa_drive-harddisk.png', mxResources.get('device'), App.MODE_DEVICE);
	}
	
	if (useLocalStorage || (urlParams['nerd'] == '1' && typeof(localStorage) != 'undefined'))
	{
		addLogo(IMAGE_PATH + '/osa_database.png', mxResources.get('browser'), App.MODE_BROWSER);
	}
	
	var p2 = document.createElement('p');
	p2.style.paddingTop = '8px';
	p2.appendChild(cb);
	mxUtils.write(p2, ' ' + mxResources.get('rememberThisSetting'));
	div.appendChild(p2);

	this.container = div;
};

/**
 * Constructs a dialog for creating new files from templates.
 */
function SplashDialog(editorUi)
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';
	
	editorUi.addLanguageMenu(div);

	var hd = document.createElement('p');
	hd.style.fontSize = '18pt';
	hd.style.padding = '0px';
	hd.style.paddingBottom = '12px';
	hd.style.margin = '0px';
	hd.style.color = 'gray';
	mxUtils.write(hd, mxResources.get('chooseAnOption') + ':');
	
	div.appendChild(hd);

	var btn = document.createElement('button');
	btn.className = 'geBigButton';
	btn.style.marginBottom = '8px';
	
	if (document.documentMode == 5)
	{
		btn.style.width = '340px';
	}
	
	mxUtils.write(btn, mxResources.get('createNewDiagram') + '...');
	
	mxEvent.addListener(btn, 'click', function()
	{
		editorUi.hideDialog();
		editorUi.actions.get('new').funct();
	});
	
	div.appendChild(btn);
	mxUtils.br(div);
	
	var btn = document.createElement('button');
	btn.className = 'geBigButton';
	
	if (document.documentMode == 5)
	{
		btn.style.width = '340px';
	}
	
	mxUtils.write(btn, mxResources.get('openExistingDiagram') + '...');
	
	mxEvent.addListener(btn, 'click', function()
	{
		editorUi.hideDialog();
		editorUi.actions.get('open').funct();
	});
	
	div.appendChild(btn);

	var storage = 'undefined';
	
	if (editorUi.mode == App.MODE_GOOGLE)
	{
		storage = mxResources.get('googleDrive');
	}
	else if (editorUi.mode == App.MODE_DROPBOX)
	{
		storage = mxResources.get('dropbox');
	}
	else if (editorUi.mode == App.MODE_DEVICE)
	{
		storage = mxResources.get('device');
	}
	else if (editorUi.mode == App.MODE_BROWSER)
	{
		storage = mxResources.get('browser');
	}
	
	if (editorUi.dropbox != null || editorUi.drive != null || (isLocalStorage && urlParams['nerd'] == '1'))
	{
		mxUtils.br(div);
		mxUtils.br(div);
		
		var link = document.createElement('a');
		link.setAttribute('href', 'javascript:void(0)');
		mxUtils.write(link, mxResources.get('notUsingService', [storage]));
		
		mxEvent.addListener(link, 'click', function()
		{
			editorUi.hideDialog();
			editorUi.clearMode();
			editorUi.showSplash(true);
		});
		
		div.appendChild(link);
		mxUtils.br(div);

		var driveUser = (editorUi.drive != null) ? editorUi.drive.getUser() : null;
		
		if (editorUi.mode == App.MODE_GOOGLE && driveUser != null)
		{
			var link = document.createElement('a');
			link.setAttribute('href', 'javascript:void(0)');
			mxUtils.write(link, mxResources.get('changeUser') + ' (' + driveUser.email + ')');
			
			mxEvent.addListener(link, 'click', function()
			{
				editorUi.drive.setUser(null);
				gapi.auth.setToken(null);
				editorUi.hideDialog();
				
				// FIXME: Does not force showing the auth dialog if only one user is logged in
				editorUi.drive.authorize(false, mxUtils.bind(this, mxUtils.bind(this, function()
				{
					editorUi.showSplash();
				})), mxUtils.bind(this, function(resp)
				{
					editorUi.handleError(resp, null, function()
					{
						editorUi.showSplash();
					});
				}));
			});
			
			div.appendChild(link);
		}
	}
	
	this.container = div;
};

/**
 * 
 */
function ConfirmDialog(editorUi, message, okFn, cancelFn)
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';

	var p2 = document.createElement('div');
	p2.style.padding = '6px';
	mxUtils.write(p2, message);
	div.appendChild(p2);
	
	var btns = document.createElement('div');
	btns.style.marginTop = '16px';
	btns.style.textAlign = 'right';

	btns.appendChild(mxUtils.button(mxResources.get('ok'), function()
	{
		editorUi.hideDialog();
		
		if (okFn != null)
		{
			okFn();
		}
	}));
	
	btns.appendChild(mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog();
		
		if (cancelFn != null)
		{
			cancelFn();
		}
	}));
	
	div.appendChild(btns);

	this.container = div;
};

/**
 * 
 */
function ErrorDialog(editorUi, title, message, buttonText, fn)
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';

	if (title != null)
	{
		var hd = document.createElement('div');
		hd.style.padding = '0px';
		hd.style.margin = '0px';
		hd.style.fontSize = '18px';
		hd.style.paddingBottom = '16px';
		hd.style.marginBottom = '16px';
		hd.style.borderBottom = '1px solid #c0c0c0';
		hd.style.color = 'gray';
		mxUtils.write(hd, title);
		div.appendChild(hd);
	}

	var p2 = document.createElement('div');
	p2.style.padding = '6px';
	mxUtils.write(p2, message);
	div.appendChild(p2);
	
	var btns = document.createElement('div');
	btns.style.marginTop = '16px';
	btns.style.textAlign = 'right';
	
	var btn = mxUtils.button(buttonText, function()
	{
		editorUi.hideDialog();
		
		if (fn != null)
		{
			fn();
		}
	});
	
	btns.appendChild(btn);
	div.appendChild(btns);

	this.container = div;
};

/**
 * Constructs a new embed dialog.
 * TODO: Add links, highlight (color) options.
 */
function EmbedDialog(editorUi)
{
	var div = document.createElement('div');

	var graph = editorUi.editor.graph;
	var bounds = graph.getGraphBounds();
	var scale = graph.view.scale;
	var x0 = Math.floor(bounds.x / scale - graph.view.translate.x);
	var y0 = Math.floor(bounds.y / scale - graph.view.translate.y);

	var textarea = document.createElement('textarea');
	textarea.style.width = '610px';
	textarea.style.height = '270px';
	textarea.style.resize = 'none';

	div.appendChild(textarea);

	mxUtils.write(div, mxResources.get('options') + ': ');
	
	var panCheckBox = document.createElement('input');
	panCheckBox.setAttribute('type', 'checkbox');
	panCheckBox.setAttribute('checked', 'checked');
	div.appendChild(panCheckBox);
	mxUtils.write(div, mxResources.get('pan') + ' ');

	var zoomCheckBox = document.createElement('input');
	zoomCheckBox.setAttribute('type', 'checkbox');
	zoomCheckBox.setAttribute('checked', 'checked');
	div.appendChild(zoomCheckBox);
	mxUtils.write(div, mxResources.get('zoom') + ' ');
	
	var resizeCheckBox = document.createElement('input');
	resizeCheckBox.setAttribute('type', 'checkbox');
	resizeCheckBox.setAttribute('checked', 'checked');
	div.appendChild(resizeCheckBox);
	mxUtils.write(div, mxResources.get('resize') + ' ');
	
	var fitCheckBox = document.createElement('input');
	fitCheckBox.setAttribute('type', 'checkbox');
	div.appendChild(fitCheckBox);
	mxUtils.write(div, mxResources.get('fit') + ' ');
	
	var scrollbarsCheckBox = document.createElement('input');
	scrollbarsCheckBox.setAttribute('type', 'checkbox');
	div.appendChild(scrollbarsCheckBox);
	mxUtils.write(div, mxResources.get('scrollbars') + ' ');
	
	var inlineCheckBox = document.createElement('input');
	inlineCheckBox.setAttribute('type', 'checkbox');
	div.appendChild(inlineCheckBox);
	mxUtils.write(div, mxResources.get('inline') + ' ');
	
	mxUtils.br(div);
	
	mxUtils.write(div, mxResources.get('width') + ': ');
	var widthInput = document.createElement('input');
	widthInput.setAttribute('type', 'text');
	widthInput.setAttribute('size', '4');
	widthInput.value = Math.ceil(bounds.width / scale);
	div.appendChild(widthInput);
	
	mxUtils.write(div, ' ' + mxResources.get('height') + ': ');
	var heightInput = document.createElement('input');
	heightInput.setAttribute('type', 'text');
	heightInput.setAttribute('size', '4');
	heightInput.value = Math.ceil(bounds.height / scale);
	div.appendChild(heightInput);
	
	mxUtils.write(div, ' ' + mxResources.get('borderWidth') + ': ');
	var borderInput = document.createElement('input');
	borderInput.setAttribute('type', 'text');
	borderInput.setAttribute('size', '3');
	borderInput.value = '0';
	div.appendChild(borderInput);
	
	mxUtils.write(div, ' ' + mxResources.get('backgroundColor') + ': ');
	var backgroundInput = document.createElement('input');
	backgroundInput.setAttribute('type', 'text');
	backgroundInput.setAttribute('size', '7');
	backgroundInput.value = graph.background ||'';
	div.appendChild(backgroundInput);
	
	mxUtils.write(div, ' ' + mxResources.get('url') + ': ');
	var urlInput = document.createElement('input');
	urlInput.setAttribute('type', 'text');
	urlInput.setAttribute('size', '9');
	div.appendChild(urlInput);

	var textarea2 = document.createElement('textarea');
	textarea2.style.width = '610px';
	textarea2.style.height = '34px';
	textarea2.style.resize = 'none';

	mxUtils.br(div);
	
	mxUtils.write(div, mxResources.get('embedNotice') + ': ');
	
	div.appendChild(textarea2);
	
	var node = null;
	var s = '';
	
	// Scans shapes for stencils
	var stencilNames = new Object();
	var states = graph.view.states.getValues();
	
	for (var i = 0; i < states.length; i++)
	{
		var state = states[i];
		var shape = state.style[mxConstants.STYLE_SHAPE];
		var basename = mxStencilRegistry.getBasenameForStencil(shape);
		
		if (basename != null)
		{
			if (stencilNames[basename] == null)
			{
				stencilNames[basename] = true;
				s += basename + ';';
			}
		}
	}
	
	if (s.length > 0)
	{
		s = '?s=' + s.substring(0, s.length - 1);
	}

	textarea2.value = '<script type="text/javascript" src="//www.draw.io/embed.js' + s + '"></script>';

	function update()
	{
		var node = editorUi.editor.getGraphXml();
		
		// Adds embed attributes
		node.setAttribute('x0', x0);
		node.setAttribute('y0', y0);
		node.setAttribute('pan', (panCheckBox.checked) ? '1': '0');
		node.setAttribute('zoom', (zoomCheckBox.checked) ? '1': '0');
		node.setAttribute('resize', (resizeCheckBox.checked) ? '1': '0');
		node.setAttribute('fit', (fitCheckBox.checked) ? '1': '0');
		node.setAttribute('border', borderInput.value);
		
		// Hidden attributes
		node.setAttribute('links', '1');
		// Highlight can contain a color code for links
		//node.setAttribute('highlight', '1');

		if (urlInput.value != '')
		{
			node.removeChild(node.firstChild);
			node.setAttribute('url', urlInput.value);
		}
				
		var xml = Base64.encode(RawDeflate.deflate(encodeURIComponent(mxUtils.getXml(node))), true);
		var style = 'position:relative;overflow:' + ((scrollbarsCheckBox.checked) ? 'auto' : 'hidden') + ';';
		
		if (widthInput.value != '')
		{
			style += 'width:' + widthInput.value + 'px;';
		}
		
		if (heightInput.value != '')
		{
			style += 'height:' + heightInput.value + 'px;';
		}
		
		if (backgroundInput.value != '')
		{
			style += 'background-color:' + backgroundInput.value + ';';
		}
		
		if (inlineCheckBox.checked)
		{
			style += 'display:inline-block;_display:inline;';
		}
		
		// Appspot URL allows https and avoids cookies
		textarea.value = '<div class="mxgraph" style="' + style + '">' +
			'<div style="width:1px;height:1px;overflow:hidden;">' + xml + '</div></div>\n';
	};
	
	update();
	
	mxEvent.addListener(panCheckBox, 'change', update);
	mxEvent.addListener(zoomCheckBox, 'change', update);
	mxEvent.addListener(resizeCheckBox, 'change', update);
	mxEvent.addListener(fitCheckBox, 'change', update);
	mxEvent.addListener(scrollbarsCheckBox, 'change', update);
	mxEvent.addListener(inlineCheckBox, 'change', update);
	mxEvent.addListener(widthInput, 'change', update);
	mxEvent.addListener(heightInput, 'change', update);
	mxEvent.addListener(borderInput, 'change', update);
	mxEvent.addListener(backgroundInput, 'change', update);
	mxEvent.addListener(urlInput, 'change', update);
	
	var buttons = document.createElement('div');
	buttons.style.paddingTop = '6px';
	buttons.style.textAlign = 'right';

	buttons.appendChild(mxUtils.button(mxResources.get('close'), function()
	{
		editorUi.hideDialog();
	}));
	
	div.appendChild(buttons);
	
	this.container = div;
};

/**
 * Constructs a new parse dialog.
 */
function CreateGraphDialog(editorUi, title, type)
{
	var div = document.createElement('div');
	div.style.textAlign = 'right';
	
	this.init = function()
	{
		var container = document.createElement('div');
		container.style.position = 'relative';
		container.style.border = '1px solid gray';
		container.style.width = '610px';
		container.style.height = '360px';
		container.style.overflow = 'hidden';
		container.style.marginBottom = '10px';
		mxEvent.disableContextMenu(container);
		div.appendChild(container);
	
		var graph = new Graph(container);
		
		graph.setCellsCloneable(true);
		graph.setPanning(true);
		graph.setAllowDanglingEdges(false);
		graph.connectionHandler.select = false;
		graph.view.setTranslate(20, 20);
		graph.border = 20;
		graph.panningHandler.useLeftButtonForPanning = true;
		
		var vertexStyle = 'rounded=1;';
		var edgeStyle = 'curved=1;';
		var startStyle = 'ellipse';
		
		// FIXME: Does not work in iPad
		var mxCellRendererInstallCellOverlayListeners = mxCellRenderer.prototype.installCellOverlayListeners;
		graph.cellRenderer.installCellOverlayListeners = function(state, overlay, shape)
		{
			mxCellRenderer.prototype.installCellOverlayListeners.apply(this, arguments);
			
			mxEvent.addListener(shape.node, (mxClient.IS_POINTER) ? 'MSPointerDown' : 'mousedown', function (evt)
			{
				overlay.fireEvent(new mxEventObject('pointerDown', 'event', evt, 'state', state));
			});
			
			if (!mxClient.IS_POINTER && mxClient.IS_TOUCH)
			{
				mxEvent.addListener(shape.node, 'touchstart', function (evt)
				{
					overlay.fireEvent(new mxEventObject('pointerDown', 'event', evt, 'state', state));
				});
			}
		};

		graph.getAllConnectionConstraints = function()
		{
			return null;
		};
	
		graph.extend = true;
	
		graph.connectionHandler.createEdgeState = function(me)
		{
			var edge = graph.createEdge(null, null, null, null, null, edgeStyle);
			
			return new mxCellState(this.graph.view, edge, this.graph.getCellStyle(edge));
		};
	
		// Gets the default parent for inserting new cells. This
		// is normally the first child of the root (ie. layer 0).
		var parent = graph.getDefaultParent();
		
		var addOverlay = function(cell)
		{
			// Creates a new overlay with an image and a tooltip
			var overlay = new mxCellOverlay(new mxImage('images/add.png', 24, 24), 'Add outgoing');
			overlay.cursor = 'hand';
	
			// Installs a handler for clicks on the overlay							
			overlay.addListener(mxEvent.CLICK, function(sender, evt2)
			{
				// TODO: Add menu for picking next shape
				graph.connectionHandler.reset();
				graph.clearSelection();
				var geo = graph.getCellGeometry(cell);
				
				var v2;
				
				executeLayout(function()
				{
					v2 = graph.insertVertex(parent, null, 'Entry', geo.x, geo.y, 80, 30, vertexStyle);
					addOverlay(v2);
					graph.view.refresh(v2);
					var e1 = graph.insertEdge(parent, null, '', cell, v2, edgeStyle);
				}, function()
				{
					graph.scrollCellToVisible(v2);
				});
			});
			
			// Special CMS event
			// FIXME: Does not work in iPad (inserts loop)
			overlay.addListener('pointerDown', function(sender, eo)
			{
				var evt2 = eo.getProperty('event');
				var state = eo.getProperty('state');
				
				graph.popupMenuHandler.hideMenu();
				graph.stopEditing(false);
				
				var pt = mxUtils.convertPoint(graph.container,
						mxEvent.getClientX(evt2), mxEvent.getClientY(evt2));
				graph.connectionHandler.start(state, pt.x, pt.y);
				graph.isMouseDown = true;
				graph.isMouseTrigger = mxEvent.isMouseEvent(evt2);
				mxEvent.consume(evt2);
			});
			
			// Sets the overlay for the cell in the graph
			graph.addCellOverlay(cell, overlay);
		};
						
		// Adds cells to the model in a single step
		graph.getModel().beginUpdate();
		var v1;
		try
		{
			v1 = graph.insertVertex(parent, null, 'Start', 0, 0, 80, 30, startStyle);
			addOverlay(v1);
		}
		finally
		{
			// Updates the display
			graph.getModel().endUpdate();
		}
	
		var layout;
		
		if (type == 'horizontalTree')
		{
			layout = new mxCompactTreeLayout(graph);
			layout.edgeRouting = false;
			layout.levelDistance = 30;
			edgeStyle = 'edgeStyle=elbowEdgeStyle;elbow=horizontal;';
		}
		else if (type == 'verticalTree')
		{
			layout = new mxCompactTreeLayout(graph, false);
			layout.edgeRouting = false;
			layout.levelDistance = 30;
			edgeStyle = 'edgeStyle=elbowEdgeStyle;elbow=vertical;';
		}
		else if (type == 'verticalFlow')
		{
			layout = new mxHierarchicalLayout(graph, mxConstants.DIRECTION_NORTH);
		}
		else if (type == 'horizontalFlow')
		{
			layout = new mxHierarchicalLayout(graph, mxConstants.DIRECTION_WEST);
		}
		else if (type == 'organic')
		{
			layout = new mxFastOrganicLayout(graph, false);
			layout.forceConstant = 80;
		}
		else if (type == 'circle')
		{
			layout = new mxCircleLayout(graph);
		}
		
		if (layout != null)
		{
			var executeLayout = function(change, post)
			{
				graph.getModel().beginUpdate();
				try
				{
					if (change != null)
					{
						change();
					}
					
					layout.execute(graph.getDefaultParent(), v1);
				}
				catch (e)
				{
					throw e;
				}
				finally
				{
					// New API for animating graph layout results asynchronously
					var morph = new mxMorphing(graph);
					morph.addListener(mxEvent.DONE, mxUtils.bind(this, function()
					{
						graph.getModel().endUpdate();
						
						if (post != null)
						{
							post();
						}
					}));
					
					morph.startAnimation();
				}
			};
			
			var edgeHandleConnect = mxEdgeHandler.prototype.connect;
			mxEdgeHandler.prototype.connect = function(edge, terminal, isSource, isClone, me)
			{
				edgeHandleConnect.apply(this, arguments);
				executeLayout();
			};
			
			graph.resizeCell = function()
			{
				mxGraph.prototype.resizeCell.apply(this, arguments);
		
				executeLayout();
			};
		
			graph.connectionHandler.addListener(mxEvent.CONNECT, function()
			{
				executeLayout();
			});
		}

		div.appendChild(mxUtils.button(mxResources.get('insert'), function()
		{
			graph.clearCellOverlays();
			var view = editorUi.editor.graph.view;
			var bds = editorUi.editor.graph.getGraphBounds();
			
			// Computes unscaled, untranslated graph bounds
			var x = Math.max(0, bds.x / view.scale - view.translate.x) + graph.gridSize;
			var y = Math.max(0, (bds.y + bds.height) / view.scale - view.translate.y) + 4 * graph.gridSize;
			editorUi.editor.graph.setSelectionCells(editorUi.editor.graph.importCells(
					graph.getModel().getChildren(graph.getDefaultParent()), x, y));
			
			graph.destroy();
			container.parentNode.removeChild(container);

			editorUi.hideDialog();
		}));
		
		div.appendChild(mxUtils.button(mxResources.get('cancel'), function()
		{
			if (mxUtils.confirm(mxResources.get('areYouSure')))
			{
				graph.destroy();
				container.parentNode.removeChild(container);
		
				editorUi.hideDialog();
			}
		}));
	};

	this.container = div;
};

/**
 * Constructs a new parse dialog.
 */
function ParseDialog(editorUi, title)
{
	function parse(text)
	{
		var lines = text.split('\n');
		var vertices = new Object();
		var cells = [];
		
		function getOrCreateVertex(id)
		{
			var vertex = vertices[id];

			if (vertex == null)
			{
				vertex = new mxCell(id, new mxGeometry(0, 0, 80, 30));
				vertex.vertex = true;
				vertices[id] = vertex;
				cells.push(vertex);
			}
			
			return vertex;
		};
		
		for (var i = 0; i < lines.length; i++)
		{
			if (lines[i].charAt(0) != ';')
			{
				var values = lines[i].split('->');
				
				if (values.length == 2)
				{
					var source = getOrCreateVertex(values[0]);
					var target = getOrCreateVertex(values[1]);
					
					var edge = new mxCell('', new mxGeometry());
					edge.edge = true;
					source.insertEdge(edge, true);
					target.insertEdge(edge, false);
					cells.push(edge);
				}
			}
		}
		
		if (cells.length > 0)
		{
			var container = document.createElement('div');
			container.style.visibility = 'hidden';
			document.body.appendChild(container);
			var graph = new Graph(container);
			
			graph.getModel().beginUpdate();
			try
			{
				cells = graph.importCells(cells);
				
				for (var i = 0; i < cells.length; i++)
				{
					if (graph.getModel().isVertex(cells[i]))
					{
						var size = graph.getPreferredSizeForCell(cells[i]);
						cells[i].geometry.width = Math.max(cells[i].geometry.width, size.width);
						cells[i].geometry.height = Math.max(cells[i].geometry.height, size.height);
					}
				}

				var layout = new mxFastOrganicLayout(graph);
				layout.disableEdgeStyle = false;
				layout.forceConstant = 120;
				layout.execute(graph.getDefaultParent());
				
				graph.moveCells(cells, 20, 20);
			}
			finally
			{
				graph.getModel().endUpdate();
			}
			
			graph.clearCellOverlays();
			var view = editorUi.editor.graph.view;
			var bds = editorUi.editor.graph.getGraphBounds();
			
			// Computes unscaled, untranslated graph bounds
			var x = Math.max(0, bds.x / view.scale - view.translate.x) + graph.gridSize;
			var y = Math.max(0, (bds.y + bds.height) / view.scale - view.translate.y) + 4 * graph.gridSize;
			editorUi.editor.graph.setSelectionCells(editorUi.editor.graph.importCells(
					graph.getModel().getChildren(graph.getDefaultParent()), x, y));
			
			graph.destroy();
			container.parentNode.removeChild(container);
		}
	};
	
	var div = document.createElement('div');
	div.style.textAlign = 'right';
	
	var textarea = document.createElement('textarea');
	textarea.style.width = '600px';
	textarea.style.height = '374px';
	
	textarea.value = ';example\na->b\nb->c\nc->a\n';
	div.appendChild(textarea);
	
	this.init = function()
	{
		textarea.focus();
	};
	
	// Enables dropping files
	if (fileSupport)
	{
		function handleDrop(evt)
		{
		    evt.stopPropagation();
		    evt.preventDefault();
		    
		    if (evt.dataTransfer.files.length > 0)
		    {
		    	var file = evt.dataTransfer.files[0];
    			
				var reader = new FileReader();
				reader.onload = function(e) { textarea.value = e.target.result; };
				reader.readAsText(file);
    		}
		};
		
		function handleDragOver(evt)
		{
			evt.stopPropagation();
			evt.preventDefault();
		};

		// Setup the dnd listeners.
		textarea.addEventListener('dragover', handleDragOver, false);
		textarea.addEventListener('drop', handleDrop, false);
	}

	div.appendChild(mxUtils.button(mxResources.get('insert'), function()
	{
		editorUi.hideDialog();
		parse(textarea.value);
	}));
	
	div.appendChild(mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog();
	}));
	
	this.container = div;
};

// Constructs a dialog for creating new files from templates.
function NewDialog(editorUi)
{
	var div = document.createElement('div');
	div.style.height = '100%';
	div.style.overflow = 'auto';
	
	var logo = document.createElement('img');
	logo.setAttribute('border', '0');
	logo.setAttribute('align', 'absmiddle');
	logo.style.width = '40px';
	logo.style.height = '40px';
	logo.style.paddingRight = '10px';
	logo.style.paddingBottom = '4px';
	
	if (editorUi.mode == App.MODE_GOOGLE)
	{
		logo.src = IMAGE_PATH + '/google-drive-logo.svg';
	}
	else if (editorUi.mode == App.MODE_DROPBOX)
	{
		logo.src = IMAGE_PATH + '/dropbox-logo.svg';
	}
	else if (editorUi.mode == App.MODE_BROWSER)
	{
		logo.src = IMAGE_PATH + '/osa_database.png';
	}
	else
	{
		logo.src = IMAGE_PATH + '/osa_drive-harddisk.png';
	}

	div.appendChild(logo);
	mxUtils.write(div, mxResources.get('filename') + ':');
	
	var nameInput = document.createElement('input');
	nameInput.setAttribute('value', editorUi.defaultFilename);
	nameInput.style.width = '240px';
	nameInput.style.marginLeft = '10px';
	
	this.init = function()
	{
		nameInput.focus();
	};

	div.appendChild(nameInput);

	mxUtils.br(div);
	
	var hd = document.createElement('p');
	hd.style.display = 'block';
	hd.style.padding = '0px';
	mxUtils.write(hd, mxResources.get('chooseAnOption') + ':');
	div.appendChild(hd);
	
	// Defines the templates for NewDialog (see below)
	var templates = ['', 'aws1&libs=aws2', 'bpmn1&libs=general;bpmn', 'compare1', 'flowchart1&libs=general;flowchart', 'flowchart2&libs=general;flowchart', 'gantt1', 'lean2', 'mindmap1&libs=general', 'orgchart1&libs=general;flowchart', 'orgchart2&libs=general;flowchart', 'package1', 'pid1', 'plcladder1&libs=general;plc_ladder',
	                 'seqdiag1&libs=general', 'sitemap1&libs=general', 'socnet1', 'stardelta', 'statemachine1&libs=general', 'uml1&libs=uml', 'usecase1&libs=general', 'venn1&libs=general', 'wf1&libs=general;flowchart'];
	
	for (var i = 0; i < templates.length; i++)
	{
		var base = templates[i];
		var index = base.indexOf('&');
		var libs = '';
		
		if (index > 0)
		{
			libs = base.substring(index + 6);
			base = base.substring(0, index);
		}
		
		var elt = document.createElement('button');
		elt.className = 'geBigButton';
		elt.style.verticalAlign = 'top';
		elt.style.margin = '6px';

		if (base.length > 0)
		{
			elt.style.backgroundImage = 'url(' + TEMPLATE_PATH + '/images/' + base + '.png)';
			elt.style.backgroundPosition = 'center center';
			elt.style.backgroundRepeat = 'no-repeat';
			elt.style.backgroundSize = 'contain';
		}
		else
		{
			mxUtils.write(elt, mxResources.get('blankDiagram'));
		}
		
		(function(template, libs)
		{
			mxEvent.addListener(elt, 'click', function(evt)
			{
				editorUi.hideDialog();
				var title = nameInput.value;
					
				if (title != null && title.length > 0)
				{
					function create(title, data)
					{
				    	editorUi.createFile(title, data, (libs.length > 0) ? libs : null);
					}
					
					if (template != null && template.length > 0)
					{
						var url = TEMPLATE_PATH + '/xml/' + template + '.xml';
						
						mxUtils.get(url, mxUtils.bind(this, function(req)
						{
							if (req.getStatus() == 200)
							{
								create(title, req.getText());
							}
							else
							{
								this.handleError(null, mxResources.get('errorLoadingFile'));
							}
						}), mxUtils.bind(this, function()
						{
							this.handleError(null, mxResources.get('errorLoadingFile'));
						}));
					}
					else
					{
						create(title);
					}
				}
			});
		})(base, libs);
		
		elt.style.height = '120px';
		elt.style.width = '180px';

		div.appendChild(elt);
	}
	
	mxUtils.br(div);
	mxUtils.br(div);
	div.appendChild(mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog(true);
	}));
	
	this.container = div;
};

//Constructs a new about dialog.
function PopupDialog(editorUi, url, pre, fallback, hideDialog) 
{
	hideDialog = (hideDialog != null) ? hideDialog : true;
	
	var div = document.createElement('div');
	div.style.textAlign = 'left';
	
	mxUtils.write(div, mxResources.get('fileOpenLocation'));
	mxUtils.br(div);
	mxUtils.br(div);
	
	div.appendChild(mxUtils.button(mxResources.get('openInNewWindow'), function()
	{
		if (hideDialog)
		{
			editorUi.hideDialog();
		}

		if (pre != null)
		{
			pre();
		}
		
		window.open(url);
	}));
	
	mxUtils.br(div);
	
	div.appendChild(mxUtils.button(mxResources.get('replaceExistingDrawing'), function()
	{
		if (hideDialog)
		{
			editorUi.hideDialog();
		}
		
		if (fallback != null)
		{
			fallback();
		}
	}));
	
	mxUtils.br(div);
	mxUtils.br(div);
	mxUtils.write(div, mxResources.get('allowPopups'));
	
	this.container = div;
};

//Constructs a new about dialog.
function ImageDialog(editorUi, showEditTextOption, title, updateImage, cancelFn) 
{
	showEditTextOption = (showEditTextOption != null) ? showEditTextOption : true;
	var graph = editorUi.editor.graph;
	
	var div = document.createElement('div');
	div.style.textAlign = 'center';
	
	var p = document.createElement('p');
	p.style.marginTop = '0px';
	p.style.fontSize = '120%';
	mxUtils.write(p, ((title != null) ? title : mxResources.get(((graph.isSelectionEmpty()) ? 'insertImage' : 'editImage'))) + ':');
	div.appendChild(p);

	updateImage = (updateImage != null) ? updateImage : function(value, w, h)
	{
		var select = null;
		var cells = graph.getSelectionCells();
		
		graph.getModel().beginUpdate();
    	try
    	{
    		// Inserts new cell if no cell is selected
			if (cells.length == 0)
			{
				var gs = graph.getGridSize();
				cells = [graph.insertVertex(graph.getDefaultParent(), null, '', gs, gs, w, h, 'verticalLabelPosition=bottom;verticalAlign=top;imageAspect=0')];
				select = cells;
			}
			
    		graph.setCellStyles(mxConstants.STYLE_IMAGE, value, cells);
        	graph.setCellStyles(mxConstants.STYLE_SHAPE, 'image', cells);
        	
        	if (graph.getSelectionCount() == 1)
        	{
	        	if (w != null && h != null)
	        	{
	        		var cell = cells[0];
	        		var geo = graph.getModel().getGeometry(cell);
	        		
	        		if (geo != null)
	        		{
	        			geo = geo.clone();
		        		geo.width = w;
		        		geo.height = h;
		        		graph.getModel().setGeometry(cell, geo);
	        		}
	        	}
        	}
    	}
    	finally
    	{
    		graph.getModel().endUpdate();
    	}
    	
    	if (select != null)
    	{
    		graph.setSelectionCells(select);
    		graph.scrollCellToVisible(select[0]);
    	}
	};
	
	// Workaround for callback using old updateImage
	editorUi.imageDialogCallback = updateImage;

	if (typeof(google) != 'undefined' && typeof(google.picker) != 'undefined')
	{
		div.appendChild(mxUtils.button(mxResources.get('imageSearch'), function()
		{
			editorUi.hideDialog();
			
			// Creates one picker and reuses it to avoid polluting the DOM
			if (editorUi.imagePicker == null)
			{
				var picker = new google.picker.PickerBuilder()
					.setLocale(mxLanguage)
		            .addView(google.picker.ViewId.IMAGE_SEARCH)
		            .enableFeature(google.picker.Feature.NAV_HIDDEN);
				
				editorUi.imagePicker = picker.setCallback(function(data)
				{
			        if (data.action == google.picker.Action.PICKED)
			        {
			        	// Larger images are at this index: data.docs[0].thumbnails.length - 1
			        	var thumb = data.docs[0].thumbnails[0];

			        	if (thumb != null)
			        	{
				        	var i = 0;
				        	
				        	while (thumb.width < 100 && thumb.height < 100 &&
				        			data.docs[0].thumbnails.length > i + 1)
				        	{
				        		thumb = data.docs[0].thumbnails[++i];	
				        	}

			        		editorUi.imageDialogCallback(thumb.url, Number(thumb.width), Number(thumb.height));
			        	}
			        }
			    }).build();
			}
			
			editorUi.imagePicker.setVisible(true);
		}));
		
		mxUtils.br(div);
		
		div.appendChild(mxUtils.button(mxResources.get('googleImages'), function()
		{
			editorUi.hideDialog();
			
			// Creates one picker and reuses it to avoid polluting the DOM
			if (editorUi.photoPicker == null)
			{
				var picker = new google.picker.PickerBuilder()
					.setLocale(mxLanguage)
		            .addView(google.picker.ViewId.PHOTO_UPLOAD)
		            .addView(google.picker.ViewId.PHOTOS);
				
				editorUi.photoPicker = picker.setCallback(function(data)
				{
			        if (data.action == google.picker.Action.PICKED)
			        {
			        	// Larger images are at this index: data.docs[0].thumbnails.length - 1
			        	var thumb = data.docs[0].thumbnails[0];
			        	var i = 0;
			        	
			        	while (thumb.width < 100 && thumb.height < 100 &&
			        			data.docs[0].thumbnails.length > i + 1)
			        	{
			        		thumb = data.docs[0].thumbnails[++i];	
			        	}

			        	if (thumb != null)
			        	{
			        		editorUi.imageDialogCallback(thumb.url, Number(thumb.width), Number(thumb.height));
			        	}
			        }
			    }).build();
			}
			
			editorUi.photoPicker.setVisible(true);
		}));
	
		mxUtils.br(div);
	}
	
	div.appendChild(mxUtils.button(mxResources.get('editImageUrl'), function()
	{
    	var value = '';
    	var state = graph.getView().getState(graph.getSelectionCell());
    	
    	if (state != null)
    	{
    		value = state.style[mxConstants.STYLE_IMAGE] || value;
    	}
    	
    	editorUi.hideDialog();
    	var dlg = new TextareaDialog(editorUi, mxResources.get('image') + ' ' + mxResources.get('url') + ':', value, function(newValue)
    			{
    		    	if (newValue != null && newValue.length > 0)
    	    		{
    		    		var img = new Image();
    		    		
    		    		img.onload = function()
    		    		{
    		    			editorUi.imageDialogCallback(newValue, Number(img.width), Number(img.height));
    		    		};
    		    		img.onerror = function()
    		    		{
    		    			editorUi.alert(mxResources.get('fileNotFound'));
    		    		};
    		    		
    		    		img.src = newValue;
    	    		}
    			});
    			editorUi.showDialog(dlg.container, 320, 200, true, true);
    			dlg.init();
   		}));

	mxUtils.br(div);
	
	if (showEditTextOption && !graph.isSelectionEmpty())
	{
		div.appendChild(mxUtils.button(mxResources.get('editText'), function()
		{
			editorUi.hideDialog();
			graph.setSelectionCell(graph.getSelectionCell());
			graph.startEditingAtCell(graph.getSelectionCell());
		}));
		
		mxUtils.br(div);
	}
	
	mxUtils.br(div);
	
	div.appendChild(mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog();
		
		if (cancelFn != null)
		{
			cancelFn();
		}
	}));
	
	this.container = div;
};

//Constructs a new about dialog.
//function FilePickerDialog(editorUi, docs) 
//{
//	var div = document.createElement('div');
//	div.style.textAlign = 'left';
//	
//	var message = document.createElement('div');
//	mxUtils.write(message, mxResources.get('fileOpenLocation'));
//	
//	div.appendChild(message);
//	div.appendChild(document.createElement('br'))
//	
//	div.appendChild(mxUtils.button(mxResources.get('openInNewWindow'), function()
//	{
//		editorUi.hideDialog();
//		
//		for (var i = 0; i < docs.length; i++) 
//		{
//			window.open(editorUi.getUrl(window.location.pathname) + '#G' + docs[i].id);
//		}
//	}));
//	
//	div.appendChild(mxUtils.button(mxResources.get('replaceExistingDrawing'), function()
//	{
//		editorUi.hideDialog();
//		this.editorUi.loadFile('G' + data.docs[0].id);
//	}));
//	
//	this.container = div;
//};

// Constructs a new about dialog.
function AboutDialog(editorUi)
{
	var div = document.createElement('div');
	div.setAttribute('align', 'center');
	
	var img = document.createElement('img');
	img.style.border = '0px';
	img.setAttribute('width', '176');
	img.setAttribute('width', '151');
	img.style.width = '170px';
	img.style.height = '219px';
	img.setAttribute('src', IMAGE_PATH + '/logo-flat.png');
	img.setAttribute('title', 'mxGraph v ' + mxClient.VERSION);
	div.appendChild(img);
	
	mxUtils.br(div);
	mxUtils.br(div);
	mxUtils.write(div, 'Powered by ');
	
	var link = document.createElement('a');
	link.setAttribute('href', 'http://www.jgraph.com/mxgraph.html');
	link.setAttribute('target', '_blank');
	mxUtils.write(link, 'mxGraph');
	div.appendChild(link);
	
	mxUtils.br(div);
	mxUtils.br(div);
	
	var small = document.createElement('small');
	small.innerHTML = '&copy; 2005-2014 JGraph Ltd.<br>All Rights Reserved.';
	div.appendChild(small);
	
	mxEvent.addListener(div, 'click', function(e)
	{
		if (!mxEvent.isConsumed(e) && mxEvent.getSource(e) != link)
		{
			editorUi.hideDialog();
		}
	});
	
	this.container = div;
};

//Constructs a new about dialog.
function AuthDialog(editorUi, peer, showRememberOption, fn)
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';
	
	var hd = document.createElement('p');
	hd.style.fontSize = '18pt';
	hd.style.padding = '0px';
	hd.style.margin = '0px';
	hd.style.color = 'gray';
	
	mxUtils.write(hd, mxResources.get('authorizationRequired'));
	
	var service = 'Unknown';
	
	var img = document.createElement('img');
	img.setAttribute('border', '0');
	img.setAttribute('align', 'absmiddle');
	img.style.marginRight = '10px';

	if (peer == editorUi.drive)
	{
		service = mxResources.get('googleDrive');
		img.src = IMAGE_PATH + '/google-drive-logo-white.svg';
	}
	else if (peer == editorUi.dropbox)
	{
		service = mxResources.get('dropbox');
		img.src = IMAGE_PATH + '/dropbox-logo-white.svg';
	}
	
	var p = document.createElement('p');
	mxUtils.write(p, mxResources.get('authorizeThisAppIn', [service]));

	var cb = document.createElement('input');
	cb.setAttribute('type', 'checkbox');
	
	var button = mxUtils.button(mxResources.get('authorize'), function()
	{
		editorUi.hideDialog(false);
		fn(cb.checked);
	});

	button.insertBefore(img, button.firstChild);
	button.style.marginTop = '6px';
	button.className = 'geBigButton';

	div.appendChild(hd);
	div.appendChild(p);
	div.appendChild(button);
	
	if (showRememberOption)
	{
		var p2 = document.createElement('p');
		p2.style.marginTop = '20px';
		p2.appendChild(cb);
		mxUtils.write(p2, ' ' + mxResources.get('rememberMe'));
		div.appendChild(p2);
	}
	
	this.container = div;
};

function MoreShapesDialog(editorUi) 
{
	var div = document.createElement('div');
	
	var libFS = document.createElement('table');
	var tbody = document.createElement('tbody');
	var row = document.createElement('tr');
	libFS.style.width = '100%';
	
	var leftDiv =  document.createElement('td');
	var midDiv =  document.createElement('td');
	var rightDiv =  document.createElement('td');
			
	var addLibCB = mxUtils.bind(this, function(wrapperDiv, title, key) 
	{
		var libCB = document.createElement('input');
		libCB.type = "checkbox";
		libFS.appendChild(libCB);
		
		libCB.checked = editorUi.sidebar.isEntryVisible(key);
		
		var libSpan = document.createElement('span');
		mxUtils.write(libSpan, title);
		
		var label = document.createElement('div');
		label.style.display = 'block';
		label.appendChild(libCB);
		label.appendChild(libSpan);
		
		wrapperDiv.appendChild(label);
		
		return function()
		{
			return (libCB.checked) ? key : null;
		};
	});
	
	row.appendChild(leftDiv);
	row.appendChild(midDiv);
	row.appendChild(rightDiv);

	tbody.appendChild(row);
	libFS.appendChild(tbody);
	
	var applyFunctions = [];
	
	applyFunctions.push(addLibCB(leftDiv, mxResources.get('general'), 'general'));
//	applyFunctions.push(addLibCB(leftDiv, mxResources.get('images'), 'images'));
	applyFunctions.push(addLibCB(leftDiv, 'UML', 'uml'));
	applyFunctions.push(addLibCB(leftDiv, 'Entity Relation', 'er'));
	applyFunctions.push(addLibCB(leftDiv, 'iOS', 'ios'));
	applyFunctions.push(addLibCB(leftDiv, 'Flowchart', 'flowchart'));
	applyFunctions.push(addLibCB(midDiv, 'Mockups', 'mockups'));
	applyFunctions.push(addLibCB(midDiv, 'BPMN', 'bpmn'));
	applyFunctions.push(addLibCB(midDiv, mxResources.get('basic'), 'basic'));
	applyFunctions.push(addLibCB(midDiv, mxResources.get('arrows'), 'arrows'));
	applyFunctions.push(addLibCB(midDiv, 'Clipart', 'clipart'));
	applyFunctions.push(addLibCB(midDiv, 'Signs', 'signs'));
	applyFunctions.push(addLibCB(rightDiv, 'Rack', 'rack'));
	applyFunctions.push(addLibCB(rightDiv, 'Electrical', 'electrical'));
	applyFunctions.push(addLibCB(rightDiv, 'AWS', 'aws2'));
	applyFunctions.push(addLibCB(rightDiv, 'Proc. Eng.', 'pid'));
	applyFunctions.push(addLibCB(rightDiv, 'Lean Mapping', 'lean_mapping'));
	applyFunctions.push(addLibCB(rightDiv, 'Cisco', 'cisco'));

	div.appendChild(libFS);
	
	var applyBtn = mxUtils.button(mxResources.get('apply'), function()
	{
		var libs = [];
		
		for (var i = 0; i < applyFunctions.length; i++)
		{
			var lib = applyFunctions[i].apply(this, arguments);
			
			if (lib != null)
			{
				libs.push(lib);
			}
		}
		
		editorUi.sidebar.showEntries((libs.length > 0) ? libs.join(';') : '');
    	editorUi.hideDialog();
	});
	
	var cancelBtn = mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog();
	});
	
	var buttons = document.createElement('div');
	buttons.style.marginTop = '10px';
	buttons.style.textAlign = 'right';
	
	buttons.appendChild(applyBtn);
	buttons.appendChild(cancelBtn);

	div.appendChild(buttons);

	this.container = div;
};

function PluginsDialog(editorUi) 
{
	var div = document.createElement('div');
	var inner = document.createElement('div');
	
	inner.style.height = '126px';
	inner.style.overflow = 'auto';

	var plugins = mxSettings.getPlugins().slice();
	
	function refresh()
	{
		if (plugins.length == 0)
		{
			inner.innerHTML = mxResources.get('noPlugins');
		}
		else
		{
			inner.innerHTML = '';
			
			for (var i = 0; i < plugins.length; i++)
			{
				var span = document.createElement('span');
				span.style.whiteSpace = 'nowrap';

				var img = document.createElement('span');
				img.className = 'geSprite geSprite-delete';
				img.style.position = 'relative';
				img.style.cursor = 'pointer';
				img.style.top = '5px';
				img.style.marginRight = '4px';
				img.style.display = 'inline-block';
				span.appendChild(img);
				
				mxUtils.write(span, plugins[i]);
				inner.appendChild(span);
				
				mxUtils.br(inner);
				
				mxEvent.addListener(img, 'click', (function(index)
				{
					return function()
					{
						if (mxUtils.confirm(window.parent.mxResources.get('delete') + ' "' + plugins[index] + '"?'))
						{
							plugins.splice(index, 1);
							refresh();
						}
					};
				})(i));
			}
		}
	}
	
	div.appendChild(inner);
	refresh();

	var addBtn = mxUtils.button(mxResources.get('add'), function()
	{
		var plugin = mxUtils.prompt(mxResources.get('pluginUrl'));
		
		if (plugin != null && plugin.length > 0)
		{
			plugins.push(plugin);
			refresh();
		}
	});
	
	var applyBtn = mxUtils.button(mxResources.get('apply'), function()
	{
		mxSettings.setPlugins(plugins);
		mxSettings.save();
		editorUi.hideDialog();
		editorUi.alert(mxResources.get('restartForChangeRequired'));
	});
	
	var cancelBtn = mxUtils.button(mxResources.get('cancel'), function()
	{
		editorUi.hideDialog();
	});
	
	var buttons = document.createElement('div');
	buttons.style.marginTop = '10px';
	buttons.style.textAlign = 'right';

	buttons.appendChild(addBtn);
	buttons.appendChild(applyBtn);
	buttons.appendChild(cancelBtn);

	div.appendChild(buttons);

	this.container = div;
};

function LoginDialog(editorUi) 
{
	var div = document.createElement('div');
	div.style.textAlign = 'center';
	var button = document.createElement('div');

	var logo = document.createElement('img');
	logo.src = IMAGE_PATH + '/google-drive-logo.svg';
	logo.setAttribute('border', '0');
	logo.setAttribute('align', 'absmiddle');
	logo.style.width = '60px';
	logo.style.height = '60px';
	logo.style.paddingBottom = '4px';

	button.style.display = 'inline-block';
	button.className = 'geBaseButton';
	button.style.margin = '4px';
	button.style.whiteSpace = 'nowrap';
	
	button.appendChild(logo);
	mxUtils.br(button);
	mxUtils.write(button, 'Click to Connect to Google Drive');
	
	div.appendChild(button);

	mxEvent.addListener(button, 'click', function()
	{
		editorUi.hideDialog();
		var authConfig = mxGoogleDrive.createAuthConfig(null, false);
		mxGoogleDrive.startIntegration(authConfig);
	});

	this.container = div;
};