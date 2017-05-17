// $Id = App.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
App = function(editor)
{
	EditorUi.call(this, editor);
	
	// Global helper method to deal with popup blockers
	window.openWindow = mxUtils.bind(this, function(url, pre, fallback)
	{
		if (window.open(url) == null)
		{
			this.showDialog(new PopupDialog(this, url, pre, fallback).container, 320, 120, true, true);
		}
		else if (pre != null)
		{
			pre();
		}
	});
	
	// Global helper method to display error messages
	window.showOpenAlert = mxUtils.bind(this, function(message)
	{
		this.handleError(message);
	});
	
	this.load();
};

//Extends EditorUi
mxUtils.extend(App, EditorUi);

/**
 * Overrides the menubar height.
 */
if (urlParams['embed'] != '1')
{
	App.prototype.menubarHeight = 60;
}

/**
 * Executes the first step for connecting to Google Drive.
 */
App.prototype.defaultUserPicture = 'https://lh3.googleusercontent.com/-HIzvXUy6QUY/AAAAAAAAAAI/AAAAAAAAAAA/giuR7PQyjEk/photo.jpg?sz=30';

/**
 * Executes the first step for connecting to Google Drive.
 */
App.prototype.currentFile = null;

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
App.prototype.mode = null;

/**
 * Executes the first step for connecting to Google Drive.
 */
App.prototype.timeout = 25000;

/**
 * Executes the first step for connecting to Google Drive.
 */
App.ERROR_TIMEOUT = 'timeout';

/**
 * Executes the first step for connecting to Google Drive.
 */
App.ERROR_BUSY = 'busy';

/**
 * Executes the first step for connecting to Google Drive.
 */
App.ERROR_UNKNOWN = 'unknown';

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
App.MODE_GOOGLE = 'google';

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
App.MODE_DROPBOX = 'dropbox';

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
App.MODE_DEVICE = 'device';

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
App.MODE_BROWSER = 'browser';

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.init = function()
{
	EditorUi.prototype.init.apply(this, arguments);
	
	/**
	 * Specifies the default filename.
	 */
	this.defaultFilename = mxResources.get('untitledDiagram');

	/**
	 * Holds the listener for description changes.
	 */	
	this.descriptorChangedListener = mxUtils.bind(this, this.descriptorChanged);
	
	if (urlParams['embed'] != '1')
	{
		/**
		 * Holds the background element.
		 */
		this.bg = this.createBackground();
		document.body.appendChild(this.bg);
		
		this.diagramContainer.style.visibility = 'hidden';
		this.outlineContainer.style.visibility = 'hidden';

		if (typeof(gapi) != 'undefined')
		{
			/**
			 * Holds the x-coordinate of the point.
			 * @type number
			 * @default 0
			 */
			this.drive = new DriveClient(this, this.isRealtimeApp());
			this.drive.addListener('userChanged', mxUtils.bind(this, function()
			{
				var file = this.getCurrentFile();
				
				if (this.drive.getUser() == null && (file == null || file.constructor == DriveFile))
				{
					this.fileLoaded(null);
				}
				
				this.updateUserElement();
			}))
		}

		if (typeof(Dropbox) != 'undefined' && Dropbox.Client != null)
		{
			/**
			 * Holds the x-coordinate of the point.
			 * @type number
			 * @default 0
			 */
			this.dropbox = new DropboxClient(this);
			this.dropbox.addListener('userChanged', mxUtils.bind(this, function()
			{
				var file = this.getCurrentFile();
				
				if (this.dropbox.getUser() == null && (file == null || file.constructor == DropboxFile))
				{
					this.fileLoaded(null);
				}
				
				this.updateUserElement();
			}));
		}

		this.updateHeader();
	
		// Sets the initial mode
		if (this.drive == null && this.dropbox == null && (!isLocalStorage || urlParams['nerd'] != 1))
		{
			this.setMode(App.MODE_DEVICE);
		}
		else
		{
			this.mode = urlParams['mode'];
					
			if (this.mode == null)
			{
				// Reads mode from cookie
				this.setMode(this.getStoredMode());
			}
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.isRealtimeApp = function()
{
	return driveDomain;
};

///**
// * Sets the onbeforeunload for the application
// */
//EditorUi.prototype.onBeforeUnload = function()
//{
//	var file = this.getCurrentFile();
//	
//	if (file != null)
//	{
//		// Modified flag is set to true after first change and never set to false for local files
//		if (file.constructor == LocalFile && file.isModified())
//		{
//			return mxResources.get('ensureDataSaved');
//		}
//		// Cannot save dropbox file in this handler
//		else if ((file.constructor == DropboxFile || !file.isAutosave()) && file.isModified())
//		{
//			return mxResources.get('allChangesLost');
//		}
//		else
//		{
//			file.close();
//		}
//	}
//};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.updateDocumentTitle = function()
{
	var title = this.editor.appName;
	var file = this.getCurrentFile();
	
	if (file != null)
	{
		var filename = (file.getTitle() != null) ? file.getTitle() : this.defaultFilename;
		title = filename + ' - ' + title;
	}
	
	document.title = title;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.getFileData = function()
{
	var node = this.editor.getGraphXml();
	var diagramNode = node.ownerDocument.createElement('diagram');
	var fileNode = node.ownerDocument.createElement('mxfile');
	// Workaround for missing textContent in IE8 and earlier
	diagramNode[(diagramNode.textContent === undefined) ? 'text' : 'textContent'] =
		Base64.encode(RawDeflate.deflate(encodeURIComponent(mxUtils.getXml(node))), true);
	fileNode.appendChild(diagramNode);
	
	return mxUtils.getXml(fileNode);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.setFileData = function(data)
{
	if (data != null && data.length > 0)
	{
		var node = mxUtils.parseXml(data).documentElement;
		this.editor.setGraphXml(node);
	}
	else
	{
		this.editor.resetGraph();
		this.editor.graph.model.clear();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.createBackground = function()
{
	var bg = this.createDiv('background');
	bg.style.position = 'absolute';
	bg.style.background = 'white';
	bg.style.left = '0px';
	bg.style.top = '0px';
	bg.style.bottom = '0px';
	bg.style.right = '0px';
	
	mxUtils.setOpacity(bg, 100);
	
	if (mxClient.IS_QUIRKS)
	{
		new mxDivResizer(bg);
	}

	return bg;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.setMode = function(mode, remember)
{
	this.mode = mode;
	
	// Note: UseLocalStorage affects the file dialogs
	// and should not be modified if mode is undefined
	if (this.mode != null)
	{
		useLocalStorage = this.mode == App.MODE_BROWSER;
	}
	
	if (typeof(Storage) != 'undefined' && remember)
	{
		var expiry = new Date();
		expiry.setYear(expiry.getFullYear() + 1);
		document.cookie = 'MODE=' + mode + '; expires=' + expiry.toUTCString();
	}
	
	if (this.appIcon != null)
	{
		if (this.mode == App.MODE_GOOGLE)
		{
			this.appIcon.setAttribute('href', 'https://drive.google.com/?authuser=0');
			this.appIcon.setAttribute('title', mxResources.get('openIt', [mxResources.get('googleDrive')]));
			this.appIcon.setAttribute('target', '_blank');
			this.appIcon.style.cursor = 'pointer';
		}
		else if (this.mode == App.MODE_DROPBOX)
		{
			this.appIcon.setAttribute('href', 'https://www.dropbox.com/');
			this.appIcon.setAttribute('title', mxResources.get('openIt', [mxResources.get('dropbox')]));
			this.appIcon.setAttribute('target', '_blank');
			this.appIcon.style.cursor = 'pointer';
		}
		else
		{
			this.appIcon.setAttribute('href', 'javascript:void(0);');
			this.appIcon.removeAttribute('title');
			this.appIcon.removeAttribute('target');
			this.appIcon.style.cursor = 'default';
		}
	}
};

/**
 * Function: authorize
 * 
 * Authorizes the client, gets the userId and calls <open>.
 */
App.prototype.clearMode = function()
{
	if (typeof(Storage) != 'undefined')
	{
		var expiry = new Date();
		expiry.setYear(expiry.getFullYear() - 1);
		document.cookie = 'MODE=; expires=' + expiry.toUTCString();
	}
};

/**
 * Function: authorize
 * 
 * Authorizes the client, gets the userId and calls <open>.
 */
App.prototype.getStoredMode = function()
{
	var mode = null;
	
	if (typeof(Storage) != 'undefined')
	{
		var cookies = document.cookie.split(";");
		
		for (var i = 0; i < cookies.length; i++)
		{
			// Removes spaces around cookie
			var cookie = mxUtils.trim(cookies[i]);
			
			if (cookie.substring(0, 5) == 'MODE=')
			{
				mode = cookie.substring(5);
				break;
			}
		}
	}
	
	return mode;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.setCurrentFile = function(file)
{
	this.currentFile = file;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.getCurrentFile = function()
{
	return this.currentFile;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.createSpinner = function(x, y, size)
{
	size = (size != null) ? size : 24;

	var spinner = new Spinner({
		lines : 12, // The number of lines to draw
		length : size, // The length of each line
		width : Math.round(size / 3), // The line thickness
		radius : Math.round(size / 2), // The radius of the inner circle
		rotate : 0, // The rotation offset
		color : '#000', // #rgb or #rrggbb
		speed : 1.5, // Rounds per second
		trail : 60, // Afterglow percentage
		shadow : false, // Whether to render a shadow
		hwaccel : true, // Whether to use hardware acceleration
		//className : 'spinner', // The CSS class to assign to the spinner
		zIndex : 2e9, // The z-index (defaults to 2000000000)
		left : Math.max(0, x - 3 * size / 2),
		top : Math.max(0, y - 50)
	});

	// Extends spin method to include an optional label
	var oldSpin = spinner.spin;
	
	spinner.spin = function(container, label)
	{
		var result = false;
		
		if (!this.active)
		{
			oldSpin.call(this, container);
			this.active = true;
			
			// TODO: Update spinner position
			if (label != null)
			{
				var status = document.createElement('div');
				status.style.position = 'absolute';
				status.style.whiteSpace = 'nowrap';
				status.style.background = '#4B4243';
				status.style.color = 'white';
				status.style.fontFamily = 'Helvetica, Arial';
				status.style.fontSize = '9pt';
				status.style.padding = '6px';
				status.style.paddingLeft = '10px';
				status.style.paddingRight = '10px';
				status.style.opacity = '0';
				status.style.zIndex = '9999';
				status.style.left = Math.max(0, x + 8) + 'px';
				status.style.top = Math.max(0, y + 64) + 'px';
				status.style.opacity = '1';
				
				mxUtils.setPrefixedStyle(status.style, 'borderRadius', '6px');
				mxUtils.setPrefixedStyle(status.style, 'transition', 'opacity 0.5s ease-in-out');
				mxUtils.setPrefixedStyle(status.style, 'boxShadow', '2px 2px 3px 0px #ddd');
				mxUtils.setPrefixedStyle(status.style, 'transform', 'translate(-50%,-50%)');
				
				status.innerHTML = label + '...';
				container.appendChild(status);
				spinner.status = status;
				
				// Centers the label in older IE versions
				if (mxClient.IS_VML && (document.documentMode == null || document.documentMode < 8))
				{
					status.style.left = Math.round(Math.max(0, x + 8 - status.offsetWidth / 2)) + 'px';
					status.style.top = Math.round(Math.max(0, y + 64 - status.offsetHeight / 2)) + 'px';
				}
			}
			
			// Pause returns a function to resume the spinner
			this.pause = mxUtils.bind(this, function()
			{
				var fn = function() { };
				
				if (this.active)
				{
					fn = mxUtils.bind(this, function()
					{
						this.spin(container, label);
					});
				}
				
				this.stop();
				
				return fn;
			});
			
			result = true;
		}
			
		return result;
	};
	
	// Extends stop method to remove the optional label
	var oldStop = spinner.stop;
	
	spinner.stop = function()
	{
		oldStop.call(this);
		this.active = false;
		
		if (spinner.status != null)
		{
			spinner.status.parentNode.removeChild(spinner.status);
			spinner.status = null;
		}
	};
	
	return spinner;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.getDiagramId = function()
{
	var id = window.location.hash;
	
	// Strips the hash sign
	if (id != null && id.length > 0)
	{
		id = id.substring(1);
	}
	
	return id;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.getSearch = function(exclude)
{
	var result = '';
	
	if (exclude != null && window.location.search.length > 0)
	{
		var amp = '?';
		
		for (var key in urlParams)
		{
			if (mxUtils.indexOf(exclude, key) < 0)
			{
				result += amp + key + '=' + urlParams[key];
				amp = '&';
			}
		}
	}
	else
	{
		result = window.location.search;
	}
	
	return result;
};

/**
 * Main function. Program starts here.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.open = function()
{
	// Cross-domain window access is not allowed in FF, so if we
	// were opened from another domain then this will fail.
	try
	{
		if (window.opener != null && window.opener.openFile != null)
		{
			window.opener.openFile.setConsumer(mxUtils.bind(this, function(xml, filename)
			{
				this.fileLoaded((this.mode == App.MODE_BROWSER) ?
						new StorageFile(this, xml, filename) :
						new LocalFile(this, xml, filename));
			}));
		}
	}
	catch(e)
	{
		// ignore
	}
};

/**
 * Main function. Program starts here.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.load = function()
{
	// Checks if we're running in embedded mode
	if (urlParams['embed'] != '1')
	{
		if (this.spinner.spin(document.body, mxResources.get('starting')))
		{
			this.stateArg = (urlParams['state'] != null && this.drive != null) ? JSON.parse(decodeURIComponent(urlParams['state'])) : null;
			this.editor.graph.setEnabled(this.getCurrentFile() != null);
			
			// Passes the userId from the state parameter to the client
			if (typeof(Storage) != 'undefined' && this.stateArg != null && this.stateArg.userId != null)
			{
				this.drive.setUserId(this.stateArg.userId);
			}
			
			// Passes the fileId from the state parameter to the hash tag and reloads
			// the page without the state parameter
			if (this.stateArg != null && this.stateArg.action == 'open')
			{
				window.location.hash = 'G' + this.stateArg.ids[0];
				window.location.search = this.getSearch(['state']);
			}
			// Legacy support for fileId parameter which is moved to the hash tag
			else if (urlParams['fileId'] != null)
			{
				window.location.hash = 'G' + urlParams['fileId'];
				window.location.search = this.getSearch(['fileId']);
			}
			else
			{
				if (this.drive == null)
				{
					this.start();
				}
				else
				{
					gapi.load('auth:client,drive-realtime,drive-share', mxUtils.bind(this, function()
					{
						gapi.client.load('drive', 'v2', mxUtils.bind(this, function()
						{
							// Needed to avoid popup blocking for non-immediate authentication
							gapi.auth.init(mxUtils.bind(this, function()
							{
								this.start();
							}));
						}));
					}));
				}
			}
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.start = function()
{
	this.bg.parentNode.removeChild(this.bg);
	this.spinner.stop();
	
	if (urlParams['url'] != null && this.spinner.spin(document.body, mxResources.get('loading')))
	{
		var reconnect = mxUtils.bind(this, function()
		{
			// Removes URL parameter and reloads the page
			if (this.spinner.spin(document.body, mxResources.get('reconnecting')))
			{
				window.location.search = this.getSearch(['url']);
			};
		});
		
		// Uses proxy to avoid CORS issues in IE9
		mxUtils.get(PROXY_URL + '?url=' + urlParams['url'], mxUtils.bind(this, function(req)
		{
			this.spinner.stop();
			
			if (req.getStatus() == 200)
			{
				this.fileLoaded(new LocalFile(this, req.getText(), null));
			}
			else
			{
				this.handleError(null, mxResources.get('errorLoadingFile'), reconnect);
			}
		}), mxUtils.bind(this, function()
		{
			this.spinner.stop();
			this.handleError(null, mxResources.get('errorLoadingFile'), reconnect);
		}));
	}
	else if (this.getCurrentFile() == null)
	{
		if (this.drive != null)
		{
			if (this.stateArg != null && this.stateArg.action == 'create')
			{
				this.setMode(App.MODE_GOOGLE);
				this.actions.get('new').funct();
			}
			else
			{
				// Listens to changes of the hash
				window.addEventListener('hashchange', mxUtils.bind(this, function(evt)
				{
					var id = this.getDiagramId();
					var file = this.getCurrentFile();
					
					if (file == null || file.getHash() != id)
					{
						this.loadFile(id, true);
					}
				}), false);
				
				this.defineCustomObjects();
				
				// Checks if no earlier loading errors are showing
				if (this.dialog == null)
				{
					this.loadFile(this.getDiagramId());
				}
			}
		}
		else
		{
			this.fileLoaded(null);
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.showSplash = function(force)
{
	if (this.mode == null || force)
	{
		this.showDialog(new StorageDialog(this, mxUtils.bind(this, function()
		{
			this.showDialog(new SplashDialog(this).container, 340, 230, true, true);
		})).container, (urlParams['nerd'] == '1') ? 380 : 320, 230, true, false);
	}
	else
	{
		this.showDialog(new SplashDialog(this).container, 340, 230, true, true);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.addLanguageMenu = function(elt)
{
	var langMenu = this.menus.get('language');
	
	if (langMenu != null)
	{
		var img = document.createElement('div');
		img.setAttribute('title', mxResources.get('language'));
		img.className = 'geIcon geSprite geSprite-globe';
		img.style.position = 'absolute';
		img.style.cursor = 'pointer';
		img.style.bottom = '20px';
		img.style.right = '20px';
		
		mxEvent.addListener(img, 'click', mxUtils.bind(this, function(evt)
		{
			this.editor.graph.popupMenuHandler.hideMenu();
			var menu = new mxPopupMenu(this.menus.get('language').funct);
			menu.div.className += ' geMenubarMenu';
			menu.smartSeparators = true;
			menu.showDisabled = true;
			menu.autoExpand = true;
			
			// Disables autoexpand and destroys menu when hidden
			menu.hideMenu = mxUtils.bind(this, function()
			{
				mxPopupMenu.prototype.hideMenu.apply(menu, arguments);
				menu.destroy();
			});
	
			var offset = mxUtils.getOffset(img);
			menu.popup(offset.x, offset.y + img.offsetHeight, null, evt);
			
			// Allows hiding by clicking on document
			this.menubar.currentMenu = menu;
		}));
	
		elt.appendChild(img);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.defineCustomObjects = function()
{
	if (gapi.drive.realtime != null)
	{
		gapi.drive.realtime.custom.registerType(mxRtCell, 'Cell');
		
		mxRtCell.prototype.cellId = gapi.drive.realtime.custom.collaborativeField('cellId');
		mxRtCell.prototype.type = gapi.drive.realtime.custom.collaborativeField('type');
		mxRtCell.prototype.value = gapi.drive.realtime.custom.collaborativeField('value');
		mxRtCell.prototype.xmlValue = gapi.drive.realtime.custom.collaborativeField('xmlValue');
		mxRtCell.prototype.style = gapi.drive.realtime.custom.collaborativeField('style');
		mxRtCell.prototype.geometry = gapi.drive.realtime.custom.collaborativeField('geometry');
		mxRtCell.prototype.visible = gapi.drive.realtime.custom.collaborativeField('visible');
		mxRtCell.prototype.collapsed = gapi.drive.realtime.custom.collaborativeField('collapsed');
		mxRtCell.prototype.connectable = gapi.drive.realtime.custom.collaborativeField('connectable');
		mxRtCell.prototype.parent = gapi.drive.realtime.custom.collaborativeField('parent');
		mxRtCell.prototype.children = gapi.drive.realtime.custom.collaborativeField('children');
		mxRtCell.prototype.source = gapi.drive.realtime.custom.collaborativeField('source');
		mxRtCell.prototype.target = gapi.drive.realtime.custom.collaborativeField('target');
	}
};

mxRtCell = function() {};

// Ignores rtCell property in codec and cloning
mxCodecRegistry.getCodec(mxCell).exclude.push('rtCell');
mxCell.prototype.mxTransient.push('rtCell');

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.pickFile = function()
{
	if (this.mode == App.MODE_DROPBOX)
	{
		this.dropbox.pickFile();
	}
	else if (this.mode == App.MODE_GOOGLE)
	{
		if (typeof(google) != 'undefined' && typeof(google.picker) != 'undefined')
		{
			this.drive.pickFile();
		}
		else
		{
			window.open('https://drive.google.com');
		}
	}
	else
	{
		window.openNew = this.getCurrentFile() != null;
		window.baseUrl = this.getUrl();
		window.openKey = 'open';
		this.openFile();
		
		// Installs local handler for opened files in same window
		window.openFile.setConsumer(mxUtils.bind(this, function(xml, filename)
		{
			this.fileLoaded((this.mode == App.MODE_BROWSER) ?
					new StorageFile(this, xml, filename) :
					new LocalFile(this, xml, filename));
		}));
		
		// Extends dialog close to show splash screen
		var dlg = this.dialog;
		var dlgClose = dlg.close;
		
		this.dialog.close = mxUtils.bind(this, function(cancel)
		{
			dlgClose.apply(dlg, arguments);
			
			if (cancel && this.getCurrentFile() == null)
			{
				this.showSplash();
			}
		});
	}
};

/**
 * Adds the label menu items to the given menu and parent.
 */
App.prototype.saveFile = function(forceDialog)
{
	var file = this.getCurrentFile();
	
	if (file != null)
	{
		if (!forceDialog && file.getTitle() != null)
		{
			this.save(file.getTitle());
		}
		else
		{
			var filename = (file.getTitle() != null) ? file.getTitle() : this.defaultFilename;
			var dlg = new FilenameDialog(this, filename, mxResources.get('save'), mxUtils.bind(this, function(name)
			{
				this.save(name, true);
			}));
			
			this.showDialog(dlg.container, 300, 100, true, true);
			dlg.init();
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.createFile = function(title, data, libs)
{
	if (title != null && this.spinner.spin(document.body, mxResources.get('inserting')))
	{
		data = (data != null) ? data : '';
		
		var error = mxUtils.bind(this, function(resp)
		{
			this.spinner.stop();
			
			if (resp == null && this.getCurrentFile() == null)
			{
				this.showSplash();
			}
			else
			{
				this.handleError(resp);
			}
		});
		
		if (this.mode == App.MODE_GOOGLE)
		{
			var folderId = (this.stateArg != null) ? this.stateArg.folderId : null;
			
			this.drive.insertFile(title, data, folderId, mxUtils.bind(this, function(file)
			{
				this.spinner.stop();
				
				if (this.stateArg != null)
				{
					// Makes sure the file is not loaded when the hash changes
					this.setCurrentFile(file);
					window.location.hash = file.getHash();
					
					// Removes state URL parameter and reloads page
					window.location.search = this.getSearch(['state']);
				}
				else
				{
					this.fileCreated(file, libs);
				}
			}), error);
		}
		else if (this.mode == App.MODE_DROPBOX)
		{
			if (title.toLowerCase().substring(title.length - this.dropbox.extension.length) != this.dropbox.extension.toLowerCase())
			{
				title += this.dropbox.extension;
			}
			
			this.dropbox.insertFile(title, data,  mxUtils.bind(this, function(file)
			{
				this.spinner.stop();
				this.fileCreated(file, libs);
			}), error);
		}
		else if (this.mode == App.MODE_BROWSER)
		{
			this.spinner.stop();
			
			var fn = mxUtils.bind(this, function()
			{
				var file = new StorageFile(this, data, title);
				
				// Inserts data into local storage
				file.doSave(title, mxUtils.bind(this, function()
				{
					this.fileCreated(file, libs);
				}), error);
			});
			
			if (localStorage.getItem(title) == null)
			{
				fn();
			}
			else
			{
				this.confirm(mxResources.get('replace', [title]), fn, mxUtils.bind(this, function()
				{
					if (this.getCurrentFile() == null)
					{
						this.showSplash();
					}
				}));
			}
		}
		else if (this.mode == App.MODE_DEVICE)
		{
			this.spinner.stop();
			var ext = '.xml';
			
			if (title.toLowerCase().substring(title.length - ext.length) != ext)
			{
				title += ext;
			}
			
			this.fileCreated(new LocalFile(this, data, title), libs);
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.fileCreated = function(file, libs)
{
	var fn = mxUtils.bind(this, function()
	{
		window.openFile = null;
		this.fileLoaded(file);
		
		if (libs != null)
		{
			this.sidebar.showEntries(libs);
		}
	});
	
	if (this.getCurrentFile() != null)
	{
		var url = window.location.pathname;
		
		if (libs != null && libs.length > 0)
		{
			url += '?libs=' + libs;
		}
		
		url = this.getUrl(url);

		// Always opens a new tab for local files to avoid losing changes
		if (file.constructor == LocalFile)
		{
			window.openFile = new OpenFile();
			window.openFile.setData(file.getData(), file.getTitle());
			window.open(url);
		}
		else
		{
			url += '#' + file.getHash();
			window.openWindow(url, null, fn);
		}
	}
	else
	{
		fn();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.loadFile = function(id, sameWindow)
{
	this.hideDialog();
	
	var fn = mxUtils.bind(this, function()
	{
		if (this.spinner.spin(document.body, mxResources.get('loading')))
		{
			// Handles files from localStorage
			if (id.charAt(0) == 'L')
			{
				this.spinner.stop();
				id = decodeURIComponent(id.substring(1));
				this.fileLoaded(new StorageFile(this, localStorage.getItem(id), id));
			}
			else
			{
				// Google Drive files are handled as default file types
				var peer = this.drive;
				
				if (id.charAt(0) == 'D')
				{
					id = decodeURIComponent(id.substring(1));
					peer = this.dropbox;
				}
				else if (id.charAt(0) == 'G')
				{
					id = id.substring(1);
				}
				
				peer.getFile(id, mxUtils.bind(this, function(file)
				{
					this.spinner.stop();
					this.fileLoaded(file);
				}), mxUtils.bind(this, function(resp)
				{
					this.spinner.stop();
					this.handleError(resp, (resp != null) ? mxResources.get('errorLoadingFile') : null, mxUtils.bind(this, function()
					{
						var file = this.getCurrentFile();
						window.location.hash = (file != null) ? file.getHash() : '';
					}));
				}));
			}
		}
	});
	
	if (id == null || id.length == 0)
	{
		this.editor.setStatus('');
		this.fileLoaded(null);
	}
	else if (this.getCurrentFile() != null && !sameWindow)
	{
		window.openWindow(this.getUrl() + '#' + id, null, fn);
	}
	else
	{
		fn();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.fileLoaded = function(file)
{
	var oldFile = this.getCurrentFile();
	
	if (oldFile != null)
	{
		oldFile.removeListener(this.descriptorChangedListener);
		oldFile.close();
	}
	
	this.editor.graph.model.clear();
	this.editor.undoManager.clear();
	
	var noFile = mxUtils.bind(this, function()
	{
		this.setCurrentFile(null);
		this.diagramContainer.style.visibility = 'hidden';
		this.outlineContainer.style.visibility = 'hidden';
		this.editor.graph.setEnabled(false);
		this.updateDocumentTitle();
		window.location.hash = '';
		
		if (this.fname != null)
		{
			this.fname.style.display = 'none';
			this.fname.innerHTML = '';
		}

		this.updateUi()
		this.showSplash();
	});

	if (file != null)
	{
		try
		{
			file.open();
			
			this.setCurrentFile(file);
			this.editor.setStatus('');
			
			this.actions.get('autosave').visible = file.constructor == DropboxFile || file.constructor == StorageFile ||
				(file.constructor == DriveFile && file.realtime == null);
			this.editor.graph.setEnabled(file.isEditable());
			
			this.diagramContainer.style.visibility = '';
			this.outlineContainer.style.visibility = '';
			
			file.addListener('descriptorChanged', this.descriptorChangedListener);
			file.addListener('contentChanged', this.descriptorChangedListener);
			this.descriptorChanged();
			
			this.editor.undoManager.clear();
			window.location.hash = file.getHash();
			
			this.setMode(file.getMode());
			this.updateUi()
		}
		catch (e)
		{
			// Asynchronous handling of errors
			this.handleError(e, mxResources.get('errorLoadingFile'), mxUtils.bind(this, function()
			{
				// Removes URL parameter and reloads the page
				if (urlParams['url'] != null && this.spinner.spin(document.body, mxResources.get('reconnecting')))
				{
					window.location.search = this.getSearch(['url']);
				}
				else
				{
					noFile();
				}
			}));
		}
	}
	else
	{
		noFile();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.updateUi = function()
{
	var file = this.getCurrentFile();
	
	this.actions.get('save').setEnabled(file != null);
	this.actions.get('saveAs').setEnabled(file != null);
	this.actions.get('rename').setEnabled(file != null);
	this.actions.get('import').setEnabled(file != null);
	this.actions.get('embed').setEnabled(file != null);
	this.actions.get('pageSetup').setEnabled(file != null);
	this.actions.get('print').setEnabled(file != null);
	this.actions.get('editFile').setEnabled(file != null);
	this.actions.get('share').setEnabled(file != null);
	this.actions.get('chatWindowTitle').setEnabled(file != null);
	this.actions.get('makeCopy').setEnabled(file != null);
	this.actions.get('moveToFolder').setEnabled(file != null);
	
	// Disables actions in the toolbar
	this.actions.get('image').setEnabled(file != null);
	this.actions.get('zoomIn').setEnabled(file != null);
	this.actions.get('zoomOut').setEnabled(file != null);
	this.actions.get('actualSize').setEnabled(file != null);
	
	this.menus.get('edit').setEnabled(file != null);
	this.menus.get('view').setEnabled(file != null);
	this.menus.get('options').setEnabled(file != null);
	this.menus.get('downloadAs').setEnabled(file != null);
	this.menus.get('arrange').setEnabled(file != null);
	
	this.updateUserElement();
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.save = function(name)
{
	var file = this.getCurrentFile();
	
	if (file != null && this.spinner.spin(document.body, mxResources.get('saving')))
	{
		// TODO: Unify API for save and saveAs
		if (name == file.getTitle())
		{
			file.save(true, mxUtils.bind(this, function(resp)
			{
				this.spinner.stop();
				this.editor.setStatus(mxResources.get('allChangesSaved'));
			}), mxUtils.bind(this, function(resp)
			{
				this.spinner.stop();
				this.editor.setStatus('');
				this.handleError(resp, (resp != null) ? mxResources.get('errorSavingFile') : null);
			}));
		}
		else
		{
			file.saveAs(name, mxUtils.bind(this, function(resp)
			{
				this.spinner.stop();
				this.editor.setStatus(mxResources.get('allChangesSaved'));
			}), mxUtils.bind(this, function(resp)
			{
				this.spinner.stop();
				this.editor.setStatus('');
				this.handleError(resp, (resp != null) ? mxResources.get('errorSavingFile') : null);
			}));
		}
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.downloadFile = function(format, nonCompressed)
{
	var file = this.getCurrentFile();
	
	if (file != null)
	{
		var basename = file.getTitle();
		
		if (basename.substring(basename.length - 4).toLowerCase() == '.xml')
		{
			basename = basename.substring(0, basename.length - 4);
		}
		
		var filename = encodeURIComponent(basename + '.' + format);
		
        if (format == 'xml')
    	{
        	var data = encodeURIComponent((nonCompressed) ?
        			mxUtils.getXml(this.editor.getGraphXml()) :
        			this.getFileData());
			new mxXmlRequest(SAVE_URL, 'filename=' + filename + '&format=' + format + '&xml=' + data).simulate(document, '_blank');
    	}
        else if (format == 'html')
        {
        	var graph = this.editor.graph;
        	var bounds = graph.getGraphBounds();
        	var scale = graph.view.scale;
        	var x0 = Math.floor(bounds.x / scale - graph.view.translate.x);
        	var y0 = Math.floor(bounds.y / scale - graph.view.translate.y);
        	
        	// Scans shapes for stencils
        	var node = this.editor.getGraphXml();
        	var s = '';
        	var stencilNames = new Object();
        	var states = this.editor.graph.view.states.getValues();
        	
        	for (var i = 0; i < states.length; i++)
        	{
        		var state = states[i];
        		var shape = state.style[mxConstants.STYLE_SHAPE];
        		var base = mxStencilRegistry.getBasenameForStencil(shape);
        		
        		if (base != null)
        		{
        			if (stencilNames[base] == null)
        			{
        				stencilNames[base] = true;
        				s += base + ';';
        			}
        		}
        	}
        	
        	if (s.length > 0)
        	{
        		s = '?s=' + s.substring(0, s.length - 1);
        	}
        	
    		// Adds embed attributes
    		node.setAttribute('x0', x0);
    		node.setAttribute('y0', y0);
    		node.setAttribute('pan', '1');
    		node.setAttribute('zoom', '1');
    		node.setAttribute('resize', '1');
    		node.setAttribute('fit', '0');
    		node.setAttribute('border', '');
    		
    		// Hidden attributes
    		node.setAttribute('links', '1');
    		
    		var xml = Base64.encode(RawDeflate.deflate(encodeURIComponent(mxUtils.getXml(node))), true);
    		var style = 'position:relative;overflow:hidden;' +
    			'width:' + Math.ceil(bounds.width / scale) + 'px;' +
    			'height:' + Math.ceil(bounds.height / scale) + 'px;';
    		var bg = this.editor.graph.background;

        	var html = encodeURIComponent('<html>\n<head>\n<title>' + basename + '</title>\n</head>\n<body' +
        		(((bg != null && bg != 'none') ? ' style="background-color:' + bg + ';">' : '>')) +
        		'\n<div class="mxgraph" style="' + style + '">\n' +
        		'<div style="width:1px;height:1px;overflow:hidden;">' + xml + '</div>\n</div>' +
        		'\n<script type="text/javascript" src="https://www.draw.io/embed.js' + s + '"></script>\n</body>\n</html>');
			new mxXmlRequest(SAVE_URL, 'filename=' + filename + '&format=' +
					format + '&xml=' + html).simulate(document, '_blank');
        }
        else
        {
        	var bg = this.editor.graph.background;
        	
        	if (bg == 'none')
        	{
        		bg = null;
        	}
        	
        	// JPG does not support transparent backgrounds
        	if (bg == null && format == 'jpg')
        	{
        		bg = '#ffffff';
        	}
			
			var svgRoot = this.getSvg(bg);
			var svg = encodeURIComponent(mxUtils.getXml(svgRoot));
			
			if (svg.length <= MAX_REQUEST_SIZE)
			{
				if (format == 'svg')
				{
					new mxXmlRequest(SAVE_URL, 'filename=' + filename + '&format=' +
							format + '&xml=' + svg).simulate(document, '_blank');
				}
				else
				{
					var w = parseInt(svgRoot.getAttribute('width'));
					var h = parseInt(svgRoot.getAttribute('height'));
	
					if (w > 0 && h > 0 && w * h < MAX_AREA)
					{	
						var bgParam = (bg != null) ? '&bg=' + bg : '';
						
						new mxXmlRequest(EXPORT_URL, 'filename=' + filename + '&format=' + format +
							bgParam + '&w=' + w + '&h=' + h + '&svg=' + svg).simulate(document, '_blank');
					}
				}
			}
			else
			{
				mxUtils.alert(mxResources.get('drawingTooLarge'));
				mxUtils.popup(svg);
			}
        }
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.formatFileSize = function(size)
{
    var units = [' kB', ' MB', ' GB', ' TB', 'PB', 'EB', 'ZB', 'YB'];
	var i = -1;
    do
    {
    	size = size / 1024;
        i++;
    } while (size > 1024);

    return Math.max(size, 0.1).toFixed(1) + units[i];
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.descriptorChanged = function()
{
	var file = this.getCurrentFile();
	
	if (file != null)
	{
		if (this.fname != null)
		{
			this.fname.style.display = 'inline-block';
			this.fname.innerHTML = '';
			var filename = (file.getTitle() != null) ? file.getTitle() : this.defaultFilename;
			mxUtils.write(this.fname, filename);
		}
		
		this.editor.graph.setEnabled(file.isEditable());
		window.location.hash = file.getHash();
		this.updateDocumentTitle();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.handleError = function(resp, title, fn)
{
	var e = (resp != null && resp.error != null) ? resp.error : resp;

	if (e != null || title != null)
	{
		var msg = mxResources.get('unknownError');
		title = (title != null) ? title : mxResources.get('error');
		
		if (e != null)
		{
			if (typeof(gapi) != 'undefined' && typeof(gapi.drive) != 'undefined' && typeof(gapi.drive.realtime) != 'undefined' &&
				e.type == gapi.drive.realtime.ErrorType.FORBIDDEN)
			{
				msg = mxResources.get('forbidden');
			}
			else if (e.code == 404 || (typeof(gapi) != 'undefined' && typeof(gapi.drive) != 'undefined' && typeof(gapi.drive.realtime) != 'undefined' &&
					e.type == gapi.drive.realtime.ErrorType.NOT_FOUND))
			{
				msg = mxResources.get('fileNotFoundOrDenied');
			}
			else if (e.code == App.ERROR_TIMEOUT)
			{
				msg = mxResources.get('timeout');
			}
			else if (e.message != null)
			{
				msg = e.message;
			}
		}

		this.showError(title, msg, mxResources.get('ok'), fn);
	}
	else if (fn != null)
	{
		fn();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.showError = function(title, msg, btn, fn)
{
	this.showDialog(new ErrorDialog(this, title, msg, btn, fn).container, 340, 140, true, false);	
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.alert = function(msg, fn)
{
	this.showDialog(new ErrorDialog(this, null, msg, mxResources.get('ok'), fn).container, 340, 90, true, false);	
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.confirm = function(msg, okFn, cancelFn)
{
	var resume = this.spinner.pause();
	
	this.showDialog(new ConfirmDialog(this, msg, function()
	{
		resume();
		
		if (okFn != null)
		{
			okFn();
		}
	}, function()
	{
		resume();
		
		if (cancelFn != null)
		{
			cancelFn();
		}
	}).container, 340, 80, true, false);	
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.toggleChat = function()
{
	var file = this.getCurrentFile();
	
	if (file != null)
	{
		if (file.chatWindow == null)
		{
			var cwLeft = document.body.offsetWidth - 300;
			file.chatWindow = new ChatWindow(mxResources.get('chatWindowTitle'), document.getElementById('geChat'), cwLeft , 80, 250, 350, file.realtime);
			file.chatWindow.window.setVisible(false);
		}
		
		file.chatWindow.window.setVisible(!file.chatWindow.window.isVisible());
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
App.prototype.status = function(html)
{
	this.editor.setStatus(html);
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */
App.prototype.showAuthDialog = function(peer, showRememberOption, fn)
{
	var resume = this.spinner.pause();
	
	this.showDialog(new AuthDialog(this, peer, showRememberOption, mxUtils.bind(this, function(remember)
	{
		try
		{
			if (fn != null)
			{
				fn(remember, function()
				{
					resume();
				});
			}
		}
		catch (e)
		{
			this.editor.setStatus(e.message);
		}
	})).container, 300, (showRememberOption) ? 180 : 140, true, true, mxUtils.bind(this, function(cancel)
	{
		if (cancel && this.getCurrentFile() == null)
		{
			this.showSplash();
		}
	}));
};

/**
 * Checks if the client is authorized and calls the next step.
 */
App.prototype.loadUrl = function(url, success, error)
{
	try
	{
		// Workaround for unsupported CORS in IE9 XHR
		var xhr = (navigator.userAgent.indexOf('MSIE 9') > 0) ? new XDomainRequest() : new XMLHttpRequest();
		xhr.open('GET', url);
		xhr.timeout = this.timeout;
		
	    xhr.onload = mxUtils.bind(this, function()
	    {	
	    	var data = (xhr.getText != null) ? xhr.getText() : xhr.responseText;
	    	
	    	if (data != null)
	    	{
		    	if (success != null)
		    	{
		    		success(data);
		    	}
	    	}
	    	else if (error != null)
	    	{
	    		error({code: App.ERROR_UNKNOWN});
	    	}
	    });
	    
	    xhr.onerror = function()
	    {
	    	if (error != null)
	    	{
	    		error({code: App.ERROR_UNKNOWN});
	    	}
	    };
	    
	    xhr.ontimeout = function()
	    {
	    	if (error != null)
			{
				error({code: App.ERROR_TIMEOUT});
			}
	    }
	
	    xhr.send();
	}
	catch (e)
	{
		error(e);
	}
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */ 
App.prototype.updateHeader = function()
{
	this.appIcon = document.createElement('a');
	this.appIcon.style.display = 'block';
	this.appIcon.style.position = 'absolute';
	this.appIcon.style.width = '40px';
	this.appIcon.style.height = this.menubarHeight + 'px';
	this.appIcon.style.backgroundColor = '#ffa500';
	
	mxEvent.disableContextMenu(this.appIcon);
	
	mxEvent.addListener(this.appIcon, 'click', mxUtils.bind(this, function(evt)
	{
		if (mxEvent.isAltDown(evt))
		{
			this.showSplash(true);
			mxEvent.consume(evt);
		}
	}));
	
	// LATER: Use Alpha image loader in IE6
	//this.appIcon.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src=' + IMAGE_PATH + '/logo-white.png,sizingMethod=\'scale\')';
	var logo = (isSvgBrowser) ? 'url(' + IMAGE_PATH + '/logo-white.svg)' : 'url(' + IMAGE_PATH + '/logo-white.png)';
	this.appIcon.style.backgroundImage = logo;		
	this.appIcon.style.backgroundPosition = 'center center';
	this.appIcon.style.backgroundRepeat = 'no-repeat';
	
	mxUtils.setPrefixedStyle(this.appIcon.style, 'transition', 'all 125ms linear');

	mxEvent.addListener(this.appIcon, 'mouseover', mxUtils.bind(this, function()
	{
		if (this.mode == App.MODE_GOOGLE)
		{
			this.appIcon.style.backgroundImage = 'url(' + IMAGE_PATH + '/google-drive-logo-white.svg)';
		}
		else if (this.mode == App.MODE_DROPBOX)
		{
			this.appIcon.style.backgroundImage = 'url(' + IMAGE_PATH + '/dropbox-logo-white.svg)';
		}
	}));
	
	mxEvent.addListener(this.appIcon, 'mouseout', mxUtils.bind(this, function()
	{
		this.appIcon.style.backgroundImage = logo;
	}));
	
	this.menubarContainer.appendChild(this.appIcon);

	this.fname = document.createElement('a');
	this.fname.setAttribute('href', 'javascript:void(0);');
	this.fname.setAttribute('title', mxResources.get('rename'));
	this.fname.className = 'geItem';
	this.fname.style.fontSize = '18px';
	this.fname.style.display = 'none';
	this.fname.style.position = 'relative';
	this.fname.style.padding = '2px 8px 2px 8px';
	this.fname.style.left = '50px';
	this.fname.style.top = '4px';
	this.fname.style.maxWidth = '400px';
	this.fname.style.overflow = 'hidden';
	this.fname.style.textOverflow = 'ellipsis';
	
	mxEvent.addListener(this.fname, 'click', mxUtils.bind(this, function()
	{
		var file = this.getCurrentFile();
		
		if (file != null && file.isRenamable())
		{
			this.actions.get('rename').funct();
		}
	}));
	
	this.menubarContainer.appendChild(this.fname);

	this.menubar.container.style.position = 'absolute';
	this.menubar.container.style.paddingLeft = '52px';
	this.menubar.container.style.top = '29px';
	
	this.toolbar.container.style.paddingLeft = '56px';
	
	if (urlParams['embed'] != '1' && urlParams['url'] != '')
	{
		this.toggleElement = document.createElement('a');
		this.toggleElement.setAttribute('href', 'javascript:void(0);');
		this.toggleElement.className = 'geButton';
		this.toggleElement.style.position = 'absolute';
		this.toggleElement.style.display = 'inline-block';
		this.toggleElement.style.verticalAlign = 'bottom';
		this.toggleElement.style.width = '16px';
		this.toggleElement.style.height = '16px';
		this.toggleElement.style.color = '#666';
		this.toggleElement.style.top = '4px';
		this.toggleElement.style.right = '10px';
		this.toggleElement.style.padding = '2px';
		this.toggleElement.style.fontSize = '14px';
		this.toggleElement.style.textDecoration = 'none';
		this.toggleElement.style.verticalAlign = 'middle';
		this.toggleElement.style.backgroundImage = 'url("' + IMAGE_PATH + '/chevron-up.png")';
		this.toggleElement.style.backgroundPosition = '50% 50%';
		this.toggleElement.style.backgroundRepeat = 'no-repeat';
		
		// Toggles compact mode
		mxEvent.addListener(this.toggleElement, 'click', mxUtils.bind(this, function(evt)
		{
			if (this.appIcon.style.display == 'none')
			{
				this.menubar.container.style.position = 'absolute';
				this.menubar.container.style.paddingLeft = '52px';
				this.menubar.container.style.top = '29px';
				this.toolbar.container.style.paddingLeft = '56px';
				this.appIcon.style.display = 'block';
				this.fname.style.display = 'inline-block';
				this.menubarHeight = App.prototype.menubarHeight;
				this.refresh();
				this.toggleElement.style.backgroundImage = 'url("' + IMAGE_PATH + '/chevron-up.png")';
			}
			else
			{
				this.menubar.container.style.position = 'relative';
				this.menubar.container.style.paddingLeft = '4px';
				this.menubar.container.style.top = '0px';
				this.toolbar.container.style.paddingLeft = '4px';
				this.appIcon.style.display = 'none';
				this.fname.style.display = 'none';
				this.menubarHeight = EditorUi.prototype.menubarHeight;
				this.refresh();
				this.toggleElement.style.backgroundImage = 'url("' + IMAGE_PATH + '/chevron-down.png")';
			}
			
			mxEvent.consume(evt);
		}));
	
		this.toolbarContainer.appendChild(this.toggleElement);
	}
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */
App.prototype.updateUserElement = function()
{
	if ((this.mode != App.MODE_GOOGLE && this.mode != App.MODE_DROPBOX) ||
		((this.drive == null || this.drive.getUser() == null) &&
		(this.dropbox == null || this.dropbox.getUser() == null)))
	{
		if (this.userElement != null)
		{
			this.userElement.parentNode.removeChild(this.userElement);
			this.userElement = null;
		}
	}
	else
	{
		if (this.userElement == null)
		{
			this.userElement = document.createElement('a');
			this.userElement.setAttribute('href', 'javascript:void(0);');
			this.userElement.className = 'geItem';
			this.userElement.style.position = 'absolute';
			this.userElement.style.fontSize = '8pt';
			this.userElement.style.top = '2px';
			this.userElement.style.right = '28px';
			this.userElement.style.color = '#666';
			this.userElement.style.margin = '4px';
			this.userElement.style.padding = '2px';
			this.userElement.style.paddingRight = '16px';
			this.userElement.style.verticalAlign = 'middle';
			this.userElement.style.backgroundImage =  'url(' + IMAGE_PATH + '/expanded.gif)';
			this.userElement.style.backgroundPosition = '100% 60%';
			this.userElement.style.backgroundRepeat = 'no-repeat';
			
			this.menubarContainer.appendChild(this.userElement);
			
			mxEvent.addListener(this.userElement, 'click', mxUtils.bind(this, function(evt)
			{
				if (this.userPanel == null)
				{
					var div = document.createElement('div');
					div.className = 'geDialog';
					div.style.position = 'absolute';
					div.style.top = (this.userElement.clientTop + this.userElement.clientHeight + 6) + 'px';
					div.style.right = '30px';
					div.style.padding = '0px';

					this.userPanel = div;
				}
				
				if (this.userPanel.parentNode != null)
				{
					this.userPanel.parentNode.removeChild(this.userPanel);
				}
				else
				{
					this.userPanel.innerHTML = '';
					
					var img = document.createElement('img');

					img.setAttribute('src', IMAGE_PATH + '/close.png');
					img.setAttribute('title', mxResources.get('close'));
					img.className = 'geDialogClose';
					img.style.top = '8px';
					img.style.right = '8px';
					
					mxEvent.addListener(img, 'click', mxUtils.bind(this, function()
					{
						if (this.userPanel.parentNode != null)
						{
							this.userPanel.parentNode.removeChild(this.userPanel);
						}
					}));
					
					this.userPanel.appendChild(img);
										
					if (this.mode == App.MODE_GOOGLE && this.drive != null)
					{
						var driveUser = this.drive.getUser();
						
						if (driveUser != null)
						{
							this.userPanel.innerHTML += '<table style="font-size:10pt;padding:20px 20px 10px 10px;"><tr><td valign="top"><img style="margin-right:10px;" src="' + ((driveUser.pictureUrl != null) ? driveUser.pictureUrl : this.defaultUserPicture) + '"/></td>' +
								'<td valign="top" style="white-space:nowrap;"><b>' + driveUser.displayName + '</b><br><font color="#808080">' + driveUser.email + '</font></tr></table>';
							var div = document.createElement('div');
							div.style.textAlign = 'center';
							div.style.padding = '10px';
							div.style.background = 'whiteSmoke';
							div.style.borderTop = '1px solid #e0e0e0';
							div.style.whiteSpace = 'nowrap';

							// LATER: Cannot change user while file is open since close will not work with new
							// credentials and closing the file usinf fileLoaded(null) will show splash dialog.
							div.appendChild(mxUtils.button(mxResources.get('signOut'), mxUtils.bind(this, function()
							{
								this.drive.clearUserId();
								this.drive.setUser(null);
								gapi.auth.setToken(null);
							})));
							
							this.userPanel.appendChild(div);
						}
					}
					else if (this.mode == App.MODE_DROPBOX && this.dropbox != null)
					{
						var dropboxUser = this.dropbox.getUser();
						
						if (dropboxUser != null)
						{
							this.userPanel.innerHTML += '<table style="font-size:10pt;padding:20px 20px 10px 10px;"><tr><td valign="top"><img style="margin-right:10px;" src="images/dropbox-logo.svg" width="40" height="40"/></td>' +
								'<td valign="top" style="white-space:nowrap;"><b>' + dropboxUser.displayName + '</b><br><font color="gray">' + dropboxUser.email + '</font></tr></table>';
							var div = document.createElement('div');
							div.style.textAlign = 'center';
							div.style.padding = '10px';
							div.style.background = 'whiteSmoke';
							div.style.borderTop = '1px solid #e0e0e0';
							div.style.whiteSpace = 'nowrap';
							
							div.appendChild(mxUtils.button(mxResources.get('signOut'), mxUtils.bind(this, function()
							{
								this.dropbox.signOut();
							})));
							
							this.userPanel.appendChild(div);
						}
					}
					else
					{
						var div = document.createElement('div');
						div.style.textAlign = 'center';
						div.style.padding = '20px 20px 10px 10px';
						div.innerHTML = mxResources.get('notConnected');
						
						this.userPanel.appendChild(div);
					}

					document.body.appendChild(this.userPanel);
				}
				
				mxEvent.consume(evt);
			}));
			
			mxEvent.addListener(document.body, 'click', mxUtils.bind(this, function(evt)
			{
				if (!mxEvent.isConsumed(evt) && this.userPanel != null && this.userPanel.parentNode != null)
				{
					this.userPanel.parentNode.removeChild(this.userPanel);
				}
			}));
		}
		
		var user = (this.mode == App.MODE_DROPBOX) ? ((this.dropbox != null) ? this.dropbox.getUser() : null) :
				((this.drive != null) ? this.drive.getUser() : null);
		
		if (user != null)
		{
			this.userElement.innerHTML = user.displayName;
			this.userElement.style.display = 'block';
		}
		else
		{
			this.userElement.style.display = 'none';
		}
	}
};
