/*
 * $Id: Menus.js,v 1.19 2014/01/09 19:05:09 gaudenz Exp $
 * Copyright (c) 2006-2014, JGraph Ltd
 */
(function()
{
	// Adds scrollbars for menus that exceed the page height
	var mxPopupMenuShowMenu = mxPopupMenu.prototype.showMenu;
	mxPopupMenu.prototype.showMenu = function()
	{
		mxPopupMenuShowMenu.apply(this, arguments);
		
		this.div.style.overflowY = 'auto';
		this.div.style.overflowX = 'hidden';
		var h0 = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
		this.div.style.maxHeight = (h0 - 10) + 'px';
	};
	
	var menusInit = Menus.prototype.init;
	Menus.prototype.init = function()
	{
		menusInit.apply(this, arguments);
		var editorUi = this.editorUi;
		var graph = editorUi.editor.graph;
		var isGraphEnabled = mxUtils.bind(graph, graph.isEnabled);

		this.editorUi.actions.addAction('new...', mxUtils.bind(this, function()
		{
			var dlg = new NewDialog(this.editorUi);
			this.editorUi.showDialog(dlg.container, 620, 410, true, true, mxUtils.bind(this, function(cancel)
			{
				if (cancel && this.editorUi.getCurrentFile() == null)
				{
					this.editorUi.showSplash();
				}
			}));
			dlg.init();
		}));

		this.editorUi.actions.addAction('export...', mxUtils.bind(this, function()
		{
			this.editorUi.showDialog(new ExportDialog(this.editorUi).container, 300, 220, true, true);
		}), null, null, 'Ctrl+E');
		
		this.editorUi.actions.put('about', new Action(mxResources.get('aboutDrawio') + '...', mxUtils.bind(this, function()
		{
			this.editorUi.showDialog(new AboutDialog(this.editorUi).container, 220, 310, true, true);
		}), null, null, 'F1'));
		this.editorUi.actions.put('help', new Action('Support', mxUtils.bind(this, function()
		{
			window.open('https://plus.google.com/+DrawIo1/posts/jHBTuTynzYY');
		})));
		this.editorUi.actions.put('video', new Action('Video Tutorial', mxUtils.bind(this, function()
		{
			window.open('http://youtu.be/d-Nf0uNsR8w');
		})));
		this.editorUi.actions.put('status', new Action('Status', mxUtils.bind(this, function()
		{
			window.open('http://status.draw.io/');
		})));
		this.editorUi.actions.put('donate', new Action('Donate', mxUtils.bind(this, function()
		{
			window.open('https://coinbase.com/checkouts/3cb99f426ab2b75e8a24a8d6bf60a1b9');
		})));
		// NOTE: To disable the action in the toolbar we need an event for when the graph is disabled
		this.editorUi.actions.addAction('image...', mxUtils.bind(this, function()
		{
			if (graph.isEnabled())
			{
				this.editorUi.showDialog(new ImageDialog(this.editorUi).container, 320, 200, true, true);
			}
		}));
		
		// Adds language menu to options only if localStorage is available for
		// storing the choice. We do not want to use cookies for older browsers.
		// Note that the URL param lang=XX is available for setting the language
		// in older browsers. URL param has precedence over the saved setting.
//		if (isLocalStorage)
//		{
//			this.put('language', new Menu(mxUtils.bind(this, function(menu, parent)
//			{
//				var addLangItem = mxUtils.bind(this, function (id)
//				{
//					var lang = (id == '') ? mxResources.get('automatic') : mxLanguageMap[id];
//					
//					var item = menu.addItem(lang, null, mxUtils.bind(this, function()
//					{
//						mxSettings.setLanguage(id);
//						mxSettings.save();
//						
//						mxClient.language = id;
//						mxResources.loadDefaultBundle = false;
//						mxResources.add(RESOURCE_BASE);
//						
//						mxUtils.alert(mxResources.get('restartForChangeRequired'));
//					}), parent);
//					
//					if (id == mxLanguage || (id == '' && mxLanguage == null))
//					{
//						this.addCheckmark(item);
//					}
//					
//					return item;
//				});
//				
//				var item = addLangItem('');
//				menu.addSeparator(parent);
//
//				// LATER: Sort menu by language name
//				for(var langId in mxLanguageMap) 
//				{
//					addLangItem(langId);
//				}
//			})));
//
//			// Extends the menubar with the language menu
//			var menusCreateMenuBar = Menus.prototype.createMenubar;
//			Menus.prototype.createMenubar = function(container)
//			{
//				var menubar = menusCreateMenuBar.apply(this, arguments);
//				
//				var langMenu = this.get('language');
//				
//				if (langMenu != null)
//				{
//					var elt = menubar.addMenu('', langMenu.funct);
//					elt.setAttribute('title', mxResources.get('language'));
//					elt.style.width = '16px';
//					elt.style.paddingTop = '0px';
//					elt.style.paddingLeft = '4px';
//					elt.innerHTML = '<div class="geIcon geSprite geSprite-globe"/>';
//				}
//
//				return menubar;
//			};
//		}
		
//		this.put('help', new Menu(mxUtils.bind(this, function(menu, parent)
//		{
//			this.addMenuItems(menu, ['help', 'video', 'status', '-', 'donate', '-', 'about']);
//			
//			if (urlParams['test'] == '1')
//			{
//				// For testing local PNG export
//				mxResources.parse('testDebugImageExport=Debug Image Export');
//				
//				this.editorUi.actions.addAction('testDebugImageExport', mxUtils.bind(this, function()
//				{
//					var root = this.editorUi.getSvg(null);
//					mxLog.show();
//					mxLog.debug(mxUtils.getXml(root));
//				}));
//					
//				this.addMenuItems(menu, ['-', 'testDebugImageExport'], parent);
//
//				mxResources.parse('testShowRtModel=Show RT model');
//				mxResources.parse('testToggleLogging=Toggle Logging');
//				
//				this.editorUi.actions.addAction('testShowRtModel', mxUtils.bind(this, function()
//				{
//					if (this.editorUi.getCurrentFile() != null)
//					{
//						console.log('root', this.editorUi.getCurrentFile().realtime.dumpRoot());
//						this.editorUi.getCurrentFile().realtime.check();
//					}
//				}));
//				
//				this.editorUi.actions.addAction('testToggleLogging', mxUtils.bind(this, function()
//				{
//					/*mxLog.show();
//					
//					if (this.editorUi.sharing != null)
//					{
//						this.editorUi.sharing.logging = !this.editorUi.sharing.logging;
//						
//						if (this.editorUi.sharing.logging)
//						{
//							mxLog.show();
//						}
//						
//						mxLog.debug('Logging ' + ((this.editorUi.sharing.logging) ? 'enabled' : 'disabled'));
//					}
//					else
//					{
//						mxLog.debug('Document not shared');
//					}*/
//				}));
//				
//				this.addMenuItems(menu, ['-', 'testShowRtModel', 'testToggleLogging'], parent);
//				
//				mxResources.parse('testShowConsole=Show Console');
//				this.editorUi.actions.addAction('testShowConsole', function() { mxLog.show(); });
//				this.addMenuItems(menu, ['-', 'testShowConsole']);
//			}
//		}
//		)));

		/*this.put('new', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			this.addMenuItems(menu, ['new', 'newCopy', '-', 'fromTemplate', 'fromText'], parent);
		})));*/

		this.editorUi.actions.addAction('embed...', mxUtils.bind(this, function()
		{
			this.editorUi.showDialog(new EmbedDialog(this.editorUi).container, 620, 420, true, true);
		}));
		
		this.editorUi.actions.addAction('moreShapes...', mxUtils.bind(this, function()
		{
			this.editorUi.showDialog(new MoreShapesDialog(this.editorUi).container, 360, 170, true, true);
		}));

		// Adds plugins menu item in file menu only if localStorage is available for
		// storing the plugins.
		if (isLocalStorage)
		{
			this.editorUi.actions.addAction('plugins...', mxUtils.bind(this, function()
			{
				this.editorUi.showDialog(new PluginsDialog(this.editorUi).container, 360, 156, true, false);
			}));
		}
		
		this.editorUi.actions.addAction('apply', mxUtils.bind(this, function()
		{
			window.parent.postMessage(mxUtils.getXml(this.editorUi.editor.getGraphXml()), '*');
		}));
		
		this.editorUi.actions.addAction('cancel', mxUtils.bind(this, function()
		{
			window.parent.postMessage('', '*');
		}));

		this.put('downloadAs', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			menu.addItem('Portable Network Graphics (.png)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('png');
			}), parent);
		
			menu.addItem('Graphics Interchange Format (.gif)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('gif');
			}), parent);
			
			menu.addItem('JPEG File Interchange Format (.jpg)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('jpg');
			}), parent);
		
			menu.addItem('Portable Document Format (.pdf)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('pdf');
			}), parent);

			menu.addItem('Scalable Vector Graphics (.svg)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('svg');
			}), parent);

			menu.addItem('HTML Embedded (.html)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('html');
			}), parent);
			
			menu.addItem('mxGraph Model (.xml)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('xml', true);
			}), parent);
			
			menu.addItem('Draw.io File (.xml)', null, mxUtils.bind(this, function()
			{
				this.editorUi.downloadFile('xml');
			}), parent);
		})));

		this.editorUi.actions.addAction('chatWindowTitle...', mxUtils.bind(this.editorUi, this.editorUi.toggleChat));
		
		this.editorUi.actions.addAction('open...', mxUtils.bind(this, function()
		{
			this.editorUi.pickFile();
		}));
		this.editorUi.actions.addAction('import...', mxUtils.bind(this, function()
		{
			window.openNew = false;
			window.openKey = 'import';
			
			// Closes dialog after open
			window.openFile = new OpenFile(mxUtils.bind(this, function(cancel)
			{
				this.editorUi.hideDialog(cancel);
			}));
			
			window.openFile.setConsumer(mxUtils.bind(this, function(xml, filename)
			{
				try
				{
					var doc = mxUtils.parseXml(xml);
					
					if (doc.documentElement.nodeName == 'mxfile')
					{
						var diagrams = doc.getElementsByTagName('diagram');
						
						if (diagrams.length > 0)
						{
							// Workaround for missing textContent in IE8 and earlier
							var data = decodeURIComponent(RawDeflate.inflate(Base64.decode(diagrams[0].textContent, true)));
							doc = mxUtils.parseXml(data);
						}
					}
					
					var model = new mxGraphModel();
					var codec = new mxCodec(doc);
					codec.decode(doc.documentElement, model);
					
					var children = model.getChildren(model.getChildAt(model.getRoot(), 0));
					this.editorUi.editor.graph.setSelectionCells(this.editorUi.editor.graph.importCells(children));
				}
				catch (e)
				{
					this.editorUi.handleError(e, mxResources.get('invalidOrMissingFile'));
				}
			}));

			// Removes openFile if dialog is closed
			this.editorUi.showDialog(new OpenDialog(this).container, 300, 180, true, true, function()
			{
				window.openFile = null;
			});
		})).isEnabled = isGraphEnabled;
		this.editorUi.actions.addAction('rename...', mxUtils.bind(this, function()
		{
			var file = this.editorUi.getCurrentFile();
			
			if (file != null)
			{
				var filename = (file.getTitle() != null) ? file.getTitle() : this.editorUi.defaultFilename;
				
				var dlg = new FilenameDialog(this.editorUi, filename, mxResources.get('rename'), mxUtils.bind(this, function(title)
				{
					if (title != null && title.length > 0 && file != null)
					{
						this.editorUi.spinner.spin(document.body, mxResources.get('renaming'));
						
						// Delete old file, save new file in dropbox if autosize is enabled
						file.rename(title, mxUtils.bind(this, function(resp)
						{
							this.editorUi.spinner.stop();
						}),
						mxUtils.bind(this, function(resp)
						{
							this.editorUi.spinner.stop();
							this.editorUi.handleError(resp, (resp != null) ? mxResources.get('errorRenamingFile') : null);
						}));
					}
				}));
				this.editorUi.showDialog(dlg.container, 300, 80, true, true);
				dlg.init();
			}
		})).isEnabled = isGraphEnabled;
		
		this.editorUi.actions.addAction('makeCopy...', mxUtils.bind(this, function()
		{
			var file = this.editorUi.getCurrentFile();
			
			if (file != null && file.constructor == DriveFile)
			{
				var copyOf = mxResources.get('copyOf') + ' ';
				var title = file.getTitle();
				
				if (title.substring(0, copyOf.length) != copyOf)
				{
					title = copyOf + title;
				}
				var dlg = new FilenameDialog(this.editorUi, title, mxResources.get('create'), mxUtils.bind(this, function(newTitle)
				{
					if (newTitle != null && newTitle.length > 0 && this.editorUi.spinner.spin(document.body, mxResources.get('loading')))
					{
						// Makes sure the latest XML is in the file
						file.save(false, mxUtils.bind(this, function()
						{
							// Saveas does not update the file descriptor in Google Drive
							file.saveAs(newTitle, mxUtils.bind(this, function(resp)
							{
								this.editorUi.spinner.stop();
								var url = this.editorUi.getUrl();
								window.openWindow(url + '#G' + resp.id, null, mxUtils.bind(this, function()
								{
									window.location.hash = 'G' + resp.id;
								}));
							}), mxUtils.bind(this, function(resp)
							{
								this.editorUi.spinner.stop();
								this.editorUi.handleError(resp);
							}));
						}), mxUtils.bind(this, function(resp)
						{
							this.editorUi.spinner.stop();
							this.editorUi.handleError(resp);
						}));
					}
				}));
				this.editorUi.showDialog(dlg.container, 300, 80, true, true);
				dlg.init();
			}
		})).isEnabled = isGraphEnabled;
		
		this.editorUi.actions.addAction('moveToFolder...', mxUtils.bind(this, function()
		{
			var file = this.editorUi.getCurrentFile();
			
			if (file != null && this.editorUi.spinner.spin(document.body, mxResources.get('authorizing')))
			{	
				this.editorUi.drive.checkToken(mxUtils.bind(this, function()
				{
					this.editorUi.spinner.stop();
					
					var token = gapi.auth.getToken().access_token;
					
					// Reuses picker as long as token doesn't change.
					var token = gapi.auth.getToken().access_token;
					
					if (this.editorUi.folderPicker == null || this.editorUi.folderPickerToken != token)
					{
						this.editorUi.folderPickerToken = token;
						var docsView = new google.picker.DocsView()
				            .setIncludeFolders(true)
				            .setMimeTypes('application/vnd.google-apps.folder')
				            .setSelectFolderEnabled(true);
						this.editorUi.folderPicker = new google.picker.PickerBuilder()
				            .enableFeature(google.picker.Feature.NAV_HIDDEN)
				            .setOAuthToken(this.folderPickerToken)
				            .setLocale(mxLanguage)
				            .setAppId(this.appId)
				            .addView(docsView)
				            .setCallback(mxUtils.bind(this, function(data)
				            {
				            	if (data.action == google.picker.Action.PICKED)
				            	{
				            		this.editorUi.spinner.spin(document.body, mxResources.get('moving'));
			
				            	    var doc = data.docs[0];
				            	    file.move(doc.id, mxUtils.bind(this, function(resp)
				            		{
				            	    	this.editorUi.spinner.stop();
				        			}), mxUtils.bind(this, function(resp)
				        			{
				        				this.editorUi.spinner.stop();
				        				this.editorUi.handleError(resp);
				        			}));
				            	}
				        	})).build();
					}
					
					this.editorUi.folderPicker.setVisible(true);
				}));
			}
		})).isEnabled = isGraphEnabled;

		this.editorUi.actions.addAction('share...', mxUtils.bind(this, function()
		{
			var file = this.editorUi.getCurrentFile();
			
			if (file != null)
			{
				this.editorUi.drive.showPermissions(file.getId());
			}
		})).isEnabled = isGraphEnabled;

		this.editorUi.actions.addAction('specialLink', mxUtils.bind(this, function()
		{
			if (this.editorUi.spinner.spin(document.body, mxResources.get('authorizing')))
			{	
				this.editorUi.drive.checkToken(mxUtils.bind(this, function()
				{
					this.editorUi.spinner.stop();
					
			        var insert = mxUtils.bind(this, function(data)
	                {
	                	if (data.action == google.picker.Action.PICKED)
	        			{
	                		var icon = data.docs[0].iconUrl;
	                		var href = null;
	                		
	                		if (data.docs[0].mimeType == 'application/mxe' || data.docs[0].mimeType == 'application/vnd.jgraph.mxfile')
	                		{
	        					var domain = 'www.draw.io';
	        					href = 'https://' + domain + '/#G' + data.docs[0].id;
	                		}
	                		else if (data.docs[0].mimeType == 'application/mxr' || data.docs[0].mimeType == 'application/vnd.jgraph.mxfile.realtime')
	                		{
	        					var domain = 'drive.draw.io';
	        					href = 'https://' + domain + '/#G' + data.docs[0].id;
	                		}
	                		else if (data.docs[0].mimeType == 'application/vnd.google-apps.folder')
	                		{
	                			// Do not use folderview in data.docs[0].url link to Google Drive instead
	                			href = 'https://drive.google.com/#folders/' + data.docs[0].id;
	                		}
	                		else
	                		{
	                			href = data.docs[0].url;
	                		}
	                		
	                		var title = data.docs[0].name || data.docs[0].type;
	                		title = title.charAt(0).toUpperCase() + title.substring(1);
	                	    var graph = this.editorUi.editor.graph;
	                		
	                	    var linkCell = new mxCell(title, new mxGeometry(graph.gridSize, graph.gridSize, 100, 40),
	                	    	'fontColor=#0000EE;fontStyle=4;rounded=1;' + ((icon != null) ?
	                	    		'shape=label;imageWidth=16;imageHeight=16;spacingLeft=26;align=left;image=' + icon :
	                	    		'spacing=10;'));
	                	    linkCell.vertex = true;

	                	    graph.setLinkForCell(linkCell, href);
	                	    graph.addCell(linkCell);
	                	    graph.cellSizeUpdated(linkCell, true);
	                	    graph.setSelectionCell(linkCell);
	        			}
	                });
	                
			        if (this.mediaPicker == null)
			        {
				    	var token = gapi.auth.getToken().access_token;
						var view = new google.picker.DocsView()
							.setIncludeFolders(true)
				            .setSelectFolderEnabled(true);
						this.mediaPicker = new google.picker.PickerBuilder()
				            .setAppId(this.editorUi.drive.appId)
				            .setOAuthToken(token)
				            .addView(view)
				            .addView(google.picker.ViewId.VIDEO_SEARCH)
				            .addView(google.picker.ViewId.WEBCAM)
				            .addView(google.picker.ViewId.MAPS)
				            .addView(new google.picker.DocsUploadView())
				            .setCallback(insert)
				            .build();
			        }
			        
					this.mediaPicker.setVisible(true);
				}));
			}
		})).isEnabled = isGraphEnabled;

		// Overrides arrange menu to add insert submenu
		this.put('arrange', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			this.addMenuItems(menu, ['toFront', 'toBack', '-'], parent);
			this.addSubmenu('direction', menu, parent);
			this.addSubmenu('layout', menu, parent);
			this.addSubmenu('align', menu, parent);
			menu.addSeparator(parent);
			this.addSubmenu('layers', menu, parent);
			this.addSubmenu('navigation', menu, parent);
			this.addSubmenu('insert', menu, parent);
			this.addMenuItems(menu, ['-', 'group', 'ungroup', 'removeFromGroup', '-', 'lockUnlock', '-', 'autosize'], parent);
		})));
		
		var methods = ['horizontalFlow', 'verticalFlow', '-', 'horizontalTree', 'verticalTree', '-', 'organic', 'circle', '-', 'fromText'];

		var addInsertItem = function(menu, parent, title, method)
		{
			menu.addItem(title, null, mxUtils.bind(this, function()
			{
				if (method == 'fromText')
				{
					var dlg = new ParseDialog(editorUi, title);
					editorUi.showDialog(dlg.container, 620, 420, true, true);
					editorUi.dialog.container.style.overflow = 'auto';
					dlg.init();
				}
				else
				{
					var dlg = new CreateGraphDialog(editorUi, title, method);
					editorUi.showDialog(dlg.container, 620, 420, true, true);
					// Executed after dialog is added to dom
					dlg.init();
				}
			}), parent);
		};
		
		this.put('insert', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			this.addMenuItems(menu, ['specialLink', '-'], parent);
			
			for (var i = 0; i < methods.length; i++)
			{
				if (methods[i] == '-')
				{
					menu.addSeparator(parent);
				}
				else
				{
					addInsertItem(menu, parent, mxResources.get(methods[i]), methods[i]);
				}
			}
		}))).isEnabled = isGraphEnabled;

		// Overrides format menu to add style submenu
		this.put('format', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			this.addMenuItems(menu, ['fillColor'], parent);
			this.addSubmenu('gradient', menu, parent);
			this.addMenuItems(menu, ['-', 'shadow'], parent);
			this.promptChange(menu, mxResources.get('opacity'), '(%)', '100', mxConstants.STYLE_OPACITY, parent, this.get('format').enabled);
			this.addMenuItems(menu, ['-', 'curved', 'rounded', 'dashed', '-', 'strokeColor'], parent);
			this.addSubmenu('linewidth', menu, parent);
			this.addMenuItems(menu, ['-'], parent);
			this.addSubmenu('line', menu, parent);
			this.addMenuItems(menu, ['-'], parent);
			this.addSubmenu('linestart', menu, parent);
			this.addSubmenu('lineend', menu, parent);
			menu.addSeparator(parent);
			this.addSubmenu('style', menu, parent);
		})));
		
		var stylenames = ['gray', 'blue', 'green', 'turquoise', 'yellow', 'red', 'purple', 'pink'];

		var addStyleItem = function(menu, parent, title, stylename)
		{
			menu.addItem(title, null, mxUtils.bind(this, function()
			{
				graph.getModel().beginUpdate();
				var cells = graph.getSelectionCells();
				
				for (var i = 0; i < cells.length; i++)
				{
					var style = graph.getModel().getStyle(cells[i]);

					for (var j = 0; j < stylenames.length; j++)
					{
						style = mxUtils.removeStylename(style, stylenames[j]);
					}
					
					if (stylename != null)
					{
						style = mxUtils.addStylename(style, stylename);
					}
					
					graph.getModel().setStyle(cells[i], style);
				}
				
				graph.getModel().endUpdate();
			}), parent);
		};
		
		this.put('style', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			for (var i = 0; i < stylenames.length; i++)
			{
				addStyleItem(menu, parent, mxResources.get(stylenames[i]), stylenames[i]);
			}
			
			this.addMenuItems(menu, ['-'], parent);
			addStyleItem(menu, parent, mxResources.get('plain'), null);
			this.addMenuItem(menu, 'style', parent);
		})));

		this.put('file', new Menu(mxUtils.bind(this, function(menu, parent)
		{
			if (urlParams['embed'] == '1')
			{
				if (urlParams['ea'] == '1') { // Polarion early.adoption
					this.addMenuItems(menu, ['-', 'import'], parent);
			    }
//				this.addSubmenu('downloadAs', menu, parent);
//				this.addMenuItems(menu, ['-', 'editFile', '-', 'moreShapes', 'plugins', '-', 'pageSetup', 'print', '-', 'cancel', 'apply'], parent);
				this.addMenuItems(menu, ['editFile', '-', 'moreShapes', '-', 'pageSetup', 'print'], parent);
			}
			else
			{
				if (this.editorUi.mode == App.MODE_GOOGLE)
				{
					var file = this.editorUi.getCurrentFile();
					
					if (file != null && file.constructor == DriveFile && file.realtime == null)
					{
						this.addMenuItems(menu, ['save', 'share', '-'], parent);
					}
					else
					{
						this.addMenuItems(menu, ['share', 'chatWindowTitle', '-'], parent);
					}
				}
				else
				{
					this.addMenuItems(menu, ['new'], parent);
				}

				if (this.editorUi.mode == App.MODE_GOOGLE)
				{
					this.addMenuItems(menu, ['open', 'new', 'rename', 'makeCopy', 'moveToFolder'], parent);
				}
				else
				{
					this.addMenuItems(menu, ['open', '-', 'save', 'saveAs'], parent);
				}
				
				this.addMenuItems(menu, ['-', 'import'], parent);
				this.addSubmenu('downloadAs', menu, parent);
				this.addMenuItems(menu, ['-', 'embed', 'editFile', '-', 'moreShapes', 'plugins', '-', 'pageSetup', 'print'], parent);
			}
		})));
	};
})();
