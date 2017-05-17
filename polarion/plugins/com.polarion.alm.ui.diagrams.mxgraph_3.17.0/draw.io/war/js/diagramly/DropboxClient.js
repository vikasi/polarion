// $Id = DropboxClient.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
DropboxClient = function(editorUi)
{
	mxEventSource.call(this);
	
	/**
	 * Holds the x-coordinate of the point.
	 * @type number
	 * @default 0
	 */
	this.ui = editorUi;

	/**
	 * Holds the x-coordinate of the point.
	 * @type number
	 * @default 0
	 */
	this.client = new Dropbox.Client(
	{
		key: this.appKey,
		sandbox: true
	});
	
	/**
	 * Holds the x-coordinate of the point.
	 * @type number
	 * @default 0
	 */
	this.client.authDriver(new Dropbox.AuthDriver.Popup(
	{
		rememberUser: true,
		receiverUrl: 'https://' + window.location.host + '/dropbox.html'
	}));
};

// Extends mxEventSource
mxUtils.extend(DropboxClient, mxEventSource);

/**
 * Variable: appId
 * 
 * Executes the first step for connecting to Google Drive.
 */
DropboxClient.prototype.appKey = 'libwls2fa9szdji';

if (urlParams['db'] != '0' && isSvgBrowser)
{
	// Dynamically loads client code which depends on the appKey above
	document.write('<script type="text/javascript" src="https://www.dropbox.com/static/api/1/dropins.js" id="dropboxjs" data-app-key="' + DropboxClient.prototype.appKey + '"></script>');
}

/**
 * Executes the first step for connecting to Google Drive.
 */
DropboxClient.prototype.appFolder = '/Apps/drawio';

/**
 * Executes the first step for connecting to Google Drive.
 */
DropboxClient.prototype.extension = '.xml';

/**
 * Executes the first step for connecting to Google Drive.
 */
DriveClient.prototype.maxRetries = 4;

/**
 * Executes the first step for connecting to Google Drive.
 */
DropboxClient.prototype.user = null;

/**
 * Executes the first step for connecting to Google Drive.
 */
DropboxClient.prototype.writingFile = false;

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DropboxClient.prototype.signOut = function()
{
	this.client.signOut(mxUtils.bind(this, function()
	{
		this.setUser(null);
	}));
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DropboxClient.prototype.setUser = function(user)
{
	this.user = user;
	this.fireEvent(new mxEventObject('userChanged'));
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DropboxClient.prototype.getUser = function()
{
	return this.user;
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DropboxClient.prototype.updateUser = function(success, error, remember)
{
	this.client.getUserInfo(null, mxUtils.bind(this, function(error, info)
	{
		if (error == null)
		{
			this.setUser(new User(info.uid, info.email, info.name));
		}
		else
		{
			this.setUser(null);
		}
	}));
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DropboxClient.prototype.execute = function(fn)
{
	if (this.client.isAuthenticated())
	{
		fn();
	}
	else
	{
		this.authorize(false, mxUtils.bind(this, function(error, client)
		{
			if (error != null)
			{
				this.ui.handleError(error);
			}
			else
			{
				if (this.client.isAuthenticated())
				{
					this.updateUser();
					fn();
				}
				else
				{
					this.ui.showAuthDialog(this, false, mxUtils.bind(this, function(remember, success)
					{
						this.authorize(true, mxUtils.bind(this, function(error2, client)
						{
							if (error2 != null)
							{
								this.ui.handleError(error2);
							}
							else if (this.client.isAuthenticated())
							{
								this.updateUser();
								
								if (success != null)
								{
									success();
								}

								fn();
							}
						}));
					}));
				}
			}
		}));
	}
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DropboxClient.prototype.authorize = function(interactive, fn)
{
	this.client.authenticate({interactive: interactive}, mxUtils.bind(this, function(error, client)
	{
		if (error != null)
		{
			console.log(error);
		}
		else
		{
			fn();
		}
	}));
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DropboxClient.prototype.inAppFolder = function(link)
{
	var index = link.indexOf(this.appFolder + '/');
	
	if (index == link.indexOf('/', link.indexOf('/view/') + 6))
	{
		return decodeURIComponent(link.substring(index + this.appFolder.length + 1));
	}
	
	return null;
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DropboxClient.prototype.getFile = function(path, success, error)
{
	this.execute(mxUtils.bind(this, function()
	{
		var acceptResponse = true;
		
		var timeoutThread = window.setTimeout(mxUtils.bind(this, function()
		{
			acceptResponse = false;
			error({code: App.ERROR_TIMEOUT});
		}), this.ui.timeout);
		
		this.client.readFile('/' + path, mxUtils.bind(this, function(err, data, stat)
		{
	    	window.clearTimeout(timeoutThread);
	    	
	    	if (acceptResponse)
	    	{
				if (err != null)
				{
					error(err)
				}
				else
				{
					success(new DropboxFile(this.ui, data, stat));
				}
	    	}
		}));
	}));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.checkExists = function(filename, fn)
{
	this.client.stat(filename, mxUtils.bind(this, function(err, stat)
	{
		if ((err != null && err.status == 404) || (stat != null && stat.isRemoved))
		{
			fn(true);
		}
		else
		{
			this.ui.confirm(mxResources.get('replace', [filename]), function()
			{
				fn(true);
			}, function()
			{
				fn(false);
			});
		}
	}));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.renameFile = function(file, filename, success, error)
{
	if (file != null && filename != null)
	{
		// Checks if file exists
		this.execute(mxUtils.bind(this, function()
		{
			this.checkExists(filename, mxUtils.bind(this, function(checked)
			{
				if (checked)
				{
					// Uses write and remove because move does not allow overwriting an existing target
					this.writeFile(filename, file.getData(), mxUtils.bind(this, function(stat)
					{
						this.client.remove(file.getTitle(), function(err2, stat2)
						{
							if (err2 != null)
							{
								error(err2)
							}
							else
							{
								success(stat);
							}
						});
					}), error);
				}
				else
				{
					error();
				}
			}));
		}));
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.insertFile = function(filename, data, success, error)
{
	this.execute(mxUtils.bind(this, function()
	{
		this.checkExists(filename, mxUtils.bind(this, function(checked)
		{
			if (checked)
			{
				this.writeFile(filename, data, mxUtils.bind(this, function(stat)
				{
					success(new DropboxFile(this.ui, data, stat));
				}), error);
			}
			else
			{
				error();
			}
		}));
	}));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.saveFile = function(filename, data, success, error)
{
	this.execute(mxUtils.bind(this, function()
	{
		this.writeFile(filename, data, success, error);
	}));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.writeFile = function(filename, data, success, error)
{
	if (!this.writingFile)
	{
		var acceptResponse = true;
		var timeoutThread = null;
		this.writingFile = true;
		var retryCount = 0;
		
		// Cancels any pending requests
		if (this.requestThread != null)
		{
			window.clearTimeout(this.requestThread);
		}
	
		var fn = mxUtils.bind(this, function()
		{
			if (timeoutThread != null)
			{
				window.clearTimeout(timeoutThread);
			}
			
			timeoutThread = window.setTimeout(mxUtils.bind(this, function()
			{
				this.writingFile = false;
				acceptResponse = false;
				error({code: App.ERROR_TIMEOUT});
			}), this.ui.timeout);
			
			this.client.writeFile(filename, data, mxUtils.bind(this, function(err, stat)
			{
		    	window.clearTimeout(timeoutThread);
		    
		    	if (acceptResponse)
		    	{
					if (err != null)
					{
						if (retryCount < this.maxRetries)
						{
							retryCount++;
							var jitter = 1 + 0.1 * (Math.random() - 0.5);
							this.requestThread = window.setTimeout(fn, Math.round(Math.pow(2, retryCount) * jitter * 1000));
						}
						else if (error != null)
						{
							this.writingFile = false;
							error(err);
						}
					}
					else
					{
						this.writingFile = false;
						
						if (success != null)
						{
							success(stat);
						}
					}
		    	}
			}));
		});
		
		fn();
	}
	else if (error != null)
	{
		error({code: App.ERROR_BUSY});
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxClient.prototype.pickFile = function()
{
	// Authentication will be carried out on open to make sure the
	// autosave does not show an auth dialog. Showing it here will
	// block the second dialog (the file picker) so it's too early.
	Dropbox.choose(
	{
		linkType : 'direct',
		cancel: mxUtils.bind(this, function()
		{
			if (this.ui.getCurrentFile() == null)
			{
				this.ui.showSplash();
			}
        }),
		success : mxUtils.bind(this, function(files)
		{
			this.ui.spinner.spin(document.body, mxResources.get('loading'));
			
			var path = this.inAppFolder(files[0].link);
			
			if (path != null)
			{
				this.ui.spinner.stop();
				this.ui.loadFile('D' + encodeURIComponent(path));
			}
			else
			{
				var error = mxUtils.bind(this, function(e)
				{
					this.ui.spinner.stop();
					this.ui.handleError(e);
				});
				
				this.ui.confirm(mxResources.get('note') + ': ' + mxResources.get('fileWillBeSavedInAppFolder', [files[0].name]), mxUtils.bind(this, function()
				{
					this.ui.loadUrl(files[0].link, mxUtils.bind(this, function(data)
				    {
				    	this.insertFile(files[0].name, data, mxUtils.bind(this, function(file)
				    	{
				    		this.ui.spinner.stop();
				    		this.ui.loadFile(file.getHash());
				    	}), error);
				    }), error);
				}), error);
			}
		})
	});
};
