// $Id = DriveClient.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
DriveClient = function(editorUi, realtimeEnabled)
{
	mxEventSource.call(this);
	
	/**
	 * Holds a reference to the UI. Needed for the sharing client.
	 */
	this.ui = editorUi;

	/**
	 * Specifies if realtime should be enabled.
	 */
	this.realtimeEnabled = realtimeEnabled;
	
	/**
	 * Contains the Google Drive App ID.
	 */
	this.appId = (this.realtimeEnabled) ? '671128082532' : '420247213240';
	
	/**
	 * Contains the Google Drive Client ID.
	 */
	this.clientId = (this.realtimeEnabled) ? '671128082532.apps.googleusercontent.com' :
		'420247213240-hnbju1pt13seqrc1hhd5htpotk4g9q7u.apps.googleusercontent.com';

	/**
	 * Executes the first step for connecting to Google Drive.
	 */
	this.mimeType = 'application/vnd.jgraph.mxfile' + ((this.realtimeEnabled) ? '.realtime' : '');

	/**
	 * Executes the first step for connecting to Google Drive.
	 */
	this.mimeTypes = 'application/mxe,application/vnd.jgraph.mxfile' + ((this.realtimeEnabled) ?
		',application/mxr,application/vnd.jgraph.mxfile.realtime' : '');
};

// Extends mxEventSource
mxUtils.extend(DriveClient, mxEventSource);

/**
 * OAuth 2.0 scopes for installing Drive Apps.
 */
DriveClient.prototype.scopes = ['https://www.googleapis.com/auth/drive.file',
                                'https://www.googleapis.com/auth/drive.install',
                                'https://www.googleapis.com/auth/userinfo.email',
                                'https://www.googleapis.com/auth/userinfo.profile'];

/**
 * Executes the first step for connecting to Google Drive.
 */
DriveClient.prototype.apiKey = 'AIzaSyCFv2xAOSjqDofA8oTzCsOR4TyeOBahB2A';

/**
 * Interval for updating the access token.
 */
DriveClient.prototype.tokenRefreshInterval = 0;

/**
 * Interval for updating the access token.
 */
DriveClient.prototype.lastTokenRefresh = 0;

/**
 * Executes the first step for connecting to Google Drive.
 */
DriveClient.prototype.maxRetries = 4;

/**
 * Executes the first step for connecting to Google Drive.
 */
DriveClient.prototype.user = null;

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.setUser = function(user)
{
	this.user = user;
	
	if (this.user == null && this.tokenRefreshThread != null)
	{
		window.clearTimeout(this.tokenRefreshThread);
		this.tokenRefreshThread = null;
	}
	
	this.fireEvent(new mxEventObject('userChanged'));
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.getUser = function()
{
	return this.user;
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.setUserId = function(userId, remember)
{
	if (typeof(Storage) != 'undefined')
	{
		sessionStorage.setItem('GUID', userId);
		
		if (remember)
		{
			var expiry = new Date();
			expiry.setYear(expiry.getFullYear() + 1);
			document.cookie = 'GUID=' + userId + '; expires=' + expiry.toUTCString();
		}
	}
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.clearUserId = function()
{
	if (typeof(Storage) != 'undefined')
	{
		sessionStorage.removeItem('GUID');

		var expiry = new Date();
		expiry.setYear(expiry.getFullYear() - 1);
		document.cookie = 'GUID=; expires=' + expiry.toUTCString();
	}
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.getUserId = function()
{
	var uid = null;
	
	if (this.user != null)
	{
		uid = this.user.id;
	}

	if (typeof(Storage) != 'undefined')
	{
		if (uid == null)
		{
			uid = sessionStorage.getItem('GUID');
		}
		
		if (uid == null)
		{
			var cookies = document.cookie.split(";");
			
			for (var i = 0; i < cookies.length; i++)
			{
				// Removes spaces around cookie
				var cookie = mxUtils.trim(cookies[i]);
				
				if (cookie.substring(0, 5) == 'GUID=')
				{
					uid = cookie.substring(5);
					break;
				}
			}
		}
	}
	
	return uid;
};

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.execute = function(fn)
{
	// Handles error in immediate authorize call via callback that shows a
	// UI with a button that executes the second non-immediate authorize
	var fallback = mxUtils.bind(this, function(resp)
	{
		// Remember is an argument for the callback that executes
		// when the user clicks the authorize button in the UI and
		// success executes after successful authorization.
		this.ui.showAuthDialog(this, true, mxUtils.bind(this, function(remember, success)
		{
			this.authorize(false, function()
			{
				if (success != null)
				{
					success();
				}
				
				fn();
			}, mxUtils.bind(this, function()
			{
				this.ui.showError(mxResources.get('error'), mxResources.get('cannotLogin'), mxResources.get('ok'));
			}), remember);
		}));
	});
	
	// First immediate authorize attempt
	this.authorize(true, fn, fallback);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.executeRequest = function(req, success, error)
{
	var acceptResponse = true;
	var timeoutThread = null;
	var retryCount = 0;
	
	// Cancels any pending requests
	if (this.requestThread != null)
	{
		window.clearTimeout(this.requestThread);
	}

	var fn = mxUtils.bind(this, function()
	{
		this.requestThread = null;
		this.currentRequest = req;

		if (timeoutThread != null)
		{
			window.clearTimeout(timeoutThread);
		}
		
		timeoutThread = window.setTimeout(mxUtils.bind(this, function()
		{
			acceptResponse = false;
			
			if (error != null)
			{
				error({code: App.ERROR_TIMEOUT});
			}
		}), this.ui.timeout);
		
		req.execute(mxUtils.bind(this, function(resp)
		{
			window.clearTimeout(timeoutThread);
			
			if (acceptResponse)
			{
				if (resp != null && resp.error == null)
				{
					if (success != null)
					{
						success(resp);
					}
				}
				else
				{
					// Handles authentication error
					if (resp != null && resp.error != null && (resp.error.code == 401 || resp.error.code == 403))
					{
						this.execute(fn)
					}
					// Schedules a retry if no new request was executed
					// TODO: Check for 'rateLimitExceeded', 'userRateLimitExceeded' in errors
					// see https://developers.google.com/drive/handle-errors
					else if (resp != null && resp.error != null && resp.error.code != 404 && this.currentRequest == req && retryCount < this.maxRetries)
					{
						retryCount++;
						var jitter = 1 + 0.1 * (Math.random() - 0.5);
						this.requestThread = window.setTimeout(fn, Math.round(Math.pow(2, retryCount) * jitter * 1000));
					}
					else if (error != null)
					{
						error(resp);
					}
				}
			}
		}));
	});
	
	fn();
},

/**
 * Authorizes the client, gets the userId and calls <open>.
 */
DriveClient.prototype.authorize = function(immediate, success, error, remember)
{
	var userId = this.getUserId();
	
	// Immediate only possible with userId
	if (immediate && userId == null)
	{
		if (error != null)
		{
			error();
		}
	}
	else
	{
		var params =
		{
			scope: this.scopes,
			client_id: this.clientId
		};
		
		if (immediate && userId != null)
		{
			params.immediate = true;
			params.user_id = userId;
		}
		else
		{
			params.immediate = false;
			params.authuser = -1;
		}
		
		gapi.auth.authorize(params, mxUtils.bind(this, function(resp)
		{
			// Updates the current user info
			if (resp != null && resp.error == null)
			{
				if (this.user == null || !immediate || this.user.id != userId)
				{
					this.updateUser(success, error, remember);
				}
				else if (success != null)
				{
					success();
				}
			}
			else if (error != null)
			{
				error();
			}

			this.resetTokenRefresh(resp);
		}));
	}
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.resetTokenRefresh = function(resp)
{
	if (this.tokenRefreshThread != null)
	{
		window.clearTimeout(this.tokenRefreshThread);
		this.tokenRefreshThread = null;
	}

	// Starts timer to refresh token before it expires
	if (resp != null && resp.error == null && resp.expires_in > 0)
	{
		this.tokenRefreshInterval = resp.expires_in * 1000;
		this.lastTokenRefresh = new Date().getTime();
		
		this.tokenRefreshThread = window.setTimeout(mxUtils.bind(this, function()
		{
			this.authorize(true, mxUtils.bind(this, function()
			{
				//console.log('tokenRefresh: refreshed', gapi.auth.getToken());
			}), mxUtils.bind(this, function()
			{
				//console.log('tokenRefresh: error refreshing', gapi.auth.getToken());
			}));
		}), resp.expires_in * 900);
	}
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.checkToken = function(fn)
{
	var connected = this.lastTokenRefresh > 0;
	var delta = new Date().getTime() - this.lastTokenRefresh;

	if (delta > this.tokenRefreshInterval || this.tokenRefreshThread == null)
	{
		// Use execute instead of authorize to allow a fallback authorization if cookie was lost
		this.execute(mxUtils.bind(this, function()
		{
			fn();
			
			if (connected)
			{
				this.fireEvent(new mxEventObject('disconnected'));
			}
		}));
	}
	else
	{
		fn();
	}
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.updateUser = function(success, error, remember)
{
	var token = gapi.auth.getToken().access_token;
	var url = 'https://www.googleapis.com/oauth2/v2/userinfo?alt=json&access_token=' + token;
	
	this.ui.loadUrl(url, mxUtils.bind(this, function(data)
	{
    	var info = JSON.parse(data);
    	
    	// Requests more information about the user
    	this.executeRequest(gapi.client.drive.about.get(), mxUtils.bind(this, function(resp)
    	{
    		this.setUser(new User(info.id, info.email, resp.user.displayName,
    				(resp.user.picture != null) ? resp.user.picture.url : null));
        	this.setUserId(info.id, remember);

    		if (success != null)
			{
				success();
			}
    	}), error);
	}), error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.copyFile = function(id, title, success, error)
{
	if (id != null && title != null)
	{
		this.executeRequest(gapi.client.drive.files.copy({'fileId': id, 'resource': {'title' : title}}), success, error);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.renameFile = function(id, title, success, error)
{
	if (id != null && title != null)
	{
		this.executeRequest(this.createDriveRequest(id, {'title' : title}), success, error);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.moveFile = function(id, folderId, success, error)
{
	if (id != null && folderId != null)
	{
		this.executeRequest(this.createDriveRequest(id, {'parents': [{'kind': 'drive#fileLink', 'id': folderId}]}), success, error);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.createDriveRequest = function(id, body)
{
	return gapi.client.request({
		'path': '/drive/v2/files/' + id,
		'method': 'PUT',
		'params': {'uploadType' : 'multipart'},
		'headers': {'Content-Type': 'application/json; charset=UTF-8'},
		'body': JSON.stringify(body)
	});
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.saveFile = function(file, revision, success, error)
{
	if (file.isEditable())
	{
		// Creates new revision if mime type changes
		revision = revision || file.desc.mimeType != this.mimeType;
		
		this.executeRequest(this.createUploadRequest(file.getId(),
		{
			'mimeType': this.mimeType,
			'title': file.getTitle()
		}, file.getData(), revision), success, error);
	}
	else if (error != null)
	{
		error({message: mxResources.get('readOnly')});
	}
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.getFile = function(id, success, error)
{
	this.executeRequest(gapi.client.drive.files.get({'fileId' : id}), mxUtils.bind(this, function(resp)
	{
		this.loadRealtime(resp, mxUtils.bind(this, function(doc)
    	{
			if (doc != null && (resp.mimeType == 'application/mxe' || resp.mimeType == 'application/vnd.jgraph.mxfile'))
			{
				this.ui.confirm(mxResources.get('convertToPro'), mxUtils.bind(this, function()
				{
					this.getXmlFile(resp, doc, success, error);
				}), error);
			}
			else if (doc == null || doc.getModel().getRoot().isEmpty())
    		{
    			this.getXmlFile(resp, doc, success, error);
    		}
    		else
    		{
        		// XML not required here since the realtime model is not empty
    			success(new DriveFile(this.ui, null, resp, doc));
    		}
    	}), error);
	}), error);
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.getXmlFile = function(resp, doc, success, error)
{
	var token = gapi.auth.getToken().access_token;
	var url = resp.downloadUrl + '&access_token=' + token;
	
	// Loads XML to initialize realtime document if realtime is empty
	this.ui.loadUrl(url, mxUtils.bind(this, function(data)
	{
		var file = new DriveFile(this.ui, data, resp, doc);

		// Checks if mime-type needs to be updated if the file is editable
		if (resp.editable && resp.mimeType != this.mimeType)
		{
			// Overwrites mime-type (only mutable on update when uploading new content)
			this.saveFile(file, true, mxUtils.bind(this, function(resp)
			{
				file.desc = resp;
				success(file);
			}), error);
		}
		else
		{
			success(file);
		}
	}), error);
};

/**
 * Checks if the client is authorized and calls the next step.
 */
DriveClient.prototype.loadRealtime = function(resp, success, error)
{
	if (this.realtimeEnabled)
	{
		// Checks if the file is writeable if it needs to be converted
		if (resp.editable || (resp.mimeType != 'application/mxe' && resp.mimeType != 'application/vnd.jgraph.mxfile'))
		{
			var acceptResponse = true;
			
			var timeoutThread = window.setTimeout(mxUtils.bind(this, function()
			{
				acceptResponse = false;
				error({code: App.ERROR_TIMEOUT});
			}), this.ui.timeout);
	
			gapi.drive.realtime.load(resp.id, mxUtils.bind(this, function(doc)
			{
		    	window.clearTimeout(timeoutThread);
		    	
		    	if (acceptResponse)
		    	{
		    		success(doc);
		    	}
			}));
		}
		else if (error != null)
		{
			error({message: mxResources.get('invalidOrMissingFile')});
		}
	}
	// Redirects to drive.draw.io with appId that can read the realtime document
	else if (this.appId != '671128082532' && (resp.mimeType == 'application/mxr' ||
		resp.mimeType == 'application/vnd.jgraph.mxfile.realtime'))
	{
		this.ui.confirm(mxResources.get('redirectToPro'), mxUtils.bind(this, function()
		{
			window.location.hostname = 'drive.draw.io';	
		}), error);
	}
	else
	{
		success();
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.insertFile = function(title, data, folderId, success, error)
{
	var metadata =
	{
		'mimeType': this.mimeType,
		'title': title
	};
	
	if (folderId != null)
	{
		metadata.parents = [{'kind': 'drive#fileLink', 'id': folderId}];
	}
	
	this.executeRequest(this.createUploadRequest(null, metadata, data, false), mxUtils.bind(this, function(resp)
	{
		this.loadRealtime(resp, mxUtils.bind(this, function(doc)
    	{
    		success(new DriveFile(this.ui, data, resp, doc));
    	}), error);
	}), error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.createUploadRequest = function(id, metadata, data, revision)
{
	var bd = '-------314159265358979323846';
	var delim = '\r\n--' + bd + '\r\n';
	var close = '\r\n--' + bd + '--';
	var ctype = 'application/octect-stream';

	var reqObj = 
	{
		'path': '/upload/drive/v2/files' + (id != null ? '/' + id : ''),
		'method': (id != null) ? 'PUT' : 'POST',
		'params': {'uploadType': 'multipart'},
		'headers': {'Content-Type' : 'multipart/mixed; boundary="' + bd + '"'},
		'body' : delim + 'Content-Type: application/json\r\n\r\n' + JSON.stringify(metadata) + delim +
			'Content-Type: ' + ctype + '\r\n' + 'Content-Transfer-Encoding: base64\r\n' + '\r\n' +
			((data != null) ? Base64.encode(data) : '') + close
	}
	
	if (!revision)
	{
		reqObj.params['newRevision'] = false;
	}
	
	return gapi.client.request(reqObj);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.pickFile = function()
{
	if (this.ui.spinner.spin(document.body, mxResources.get('authorizing')))
	{
		this.checkToken(mxUtils.bind(this, function()
		{
			this.ui.spinner.stop();
	
			// Reuses picker as long as token doesn't change.
			var token = gapi.auth.getToken().access_token;
			
			if (this.picker == null || this.pickerToken != token)
			{
				// TODO: Destroy old picker and remove related DOM nodes
				this.pickerToken = token;
				var view = new google.picker.DocsView();
			    view.setMimeTypes(this.mimeTypes);
				view.setIncludeFolders(true);
			    this.picker = new google.picker.PickerBuilder()
			        .enableFeature(google.picker.Feature.NAV_HIDDEN)
			        .setOAuthToken(this.pickerToken)
			        .setLocale(mxLanguage)
			        .setAppId(this.appId)
			        .addView(view)
			        .setCallback(mxUtils.bind(this, function(data)
			        {
			        	if (data.action == google.picker.Action.PICKED)
			    		{
			        		this.ui.loadFile('G' + data.docs[0].id);
			    		}
			        	else if (data.action == google.picker.Action.CANCEL && this.ui.getCurrentFile() == null)
			    		{
			        		this.ui.showSplash();
			    		}
			        })).build();
			}
			
			this.picker.setVisible(true);
		}));
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DriveClient.prototype.showPermissions = function(id)
{
	var shareClient = new gapi.drive.share.ShareClient(this.appId);
	shareClient.setItemIds([id]);
	shareClient.showSettingsDialog();
};
