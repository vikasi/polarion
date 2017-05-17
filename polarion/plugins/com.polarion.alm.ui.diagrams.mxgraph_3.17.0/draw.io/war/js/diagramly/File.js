// $Id = File.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
File = function(ui, data)
{
	mxEventSource.call(this);
	
	/**
	 * Holds the x-coordinate of the point.
	 * @type number
	 * @default 0
	 */
	this.ui = ui;
	
	/**
	 * Holds the x-coordinate of the point.
	 * @type number
	 * @default 0
	 */
	this.data = data || '';
};

//Extends mxEventSource
mxUtils.extend(File, mxEventSource);

/**
 * Sets the delay for autosave in milliseconds. Default is 1500.
 */
File.prototype.autosaveDelay = 1500;

/**
 * Sets the delay for autosave in milliseconds. Default is 1000.
 */
File.prototype.maxAutosaveDelay = 30000;

/**
 * Sets the delay for autosave in milliseconds. Default is 2000.
 */
File.prototype.autosaveThread = null;

/**
 * Sets the delay for autosave in milliseconds. Default is 500.
 */
File.prototype.lastAutosave = null;

/**
 * Sets the delay for autosave in milliseconds. Default is 1500.
 */
File.prototype.modified = false;

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.descriptorChanged = function()
{
	this.fireEvent(new mxEventObject('descriptorChanged'));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.contentChanged = function()
{
	this.fireEvent(new mxEventObject('contentChanged'));
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */
File.prototype.save = function(revision, success, error)
{
	this.setData(this.ui.getFileData());
	this.clearAutosave();
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.saveAs = function(filename, success, error) { };

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.isModified = function()
{
	return this.modified;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.setModified = function(value)
{
	this.modified = value;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.isAutosave = function()
{
	return this.ui.editor.autosave;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.isRenamable = function()
{
	return false;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.rename = function(title, success, error) { };

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.isMovable = function()
{
	return false;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.move = function(folderId, success, error) { };

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.getHash = function()
{
	return '';
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.getId = function()
{
	return '';
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.isEditable = function()
{
	return true;
};

/**
 * Returns the location as a new object.
 * @type mx.Point
 */
File.prototype.getUi = function()
{
	return this.ui;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
File.prototype.getTitle = function()
{
	return '';
};

/**
 * Sets the location of this point.
 * 
 * @param {number} x New X-coordinate of the point.
 * @param {number} y New Y-coordinate of the point.
 */
File.prototype.setData = function(data)
{
	this.data = data;
};

/**
 * Returns the location as a new object.
 * @type mx.Point
 */
File.prototype.getData = function()
{
	return this.data;
};

/**
 * Returns the location as a new object.
 * @type mx.Point
 */
File.prototype.open = function()
{
	this.ui.setFileData(this.getData());
	
	this.changeListener = mxUtils.bind(this, function(sender, eventObject)
	{
		this.setModified(true);
		
		if (this.isAutosave())
		{
			this.ui.editor.setStatus(mxResources.get('saving') + '...');
			
			this.autosave(this.autosaveDelay, this.maxAutosaveDelay, mxUtils.bind(this, function(resp)
			{
				// Does not update status if another autosave was scheduled
				if (this.autosaveThread == null && this.ui.getCurrentFile() == this)
				{
					this.ui.editor.setStatus(mxResources.get('allChangesSaved'));
				}
			}), mxUtils.bind(this, function(resp)
			{
				if (this.ui.getCurrentFile() == this)
				{
					this.ui.editor.setStatus(mxResources.get('unsavedChanges'));
				}
			}));
		}
		else
		{
			this.ui.editor.setStatus(mxResources.get('unsavedChanges'));
		}
	});
	
	this.ui.editor.graph.model.addListener(mxEvent.CHANGE, this.changeListener);
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */
File.prototype.autosave = function(delay, maxDelay, success, error)
{
	if (this.lastAutosave == null)
	{
		this.lastAutosave = new Date().getTime();
	}
	
	var tmp = (new Date().getTime() - this.lastAutosave < maxDelay) ? delay : 0;
	this.clearAutosave();
	
	// Starts new timer or executes immediately if not unsaved for maxDelay
	this.autosaveThread = window.setTimeout(mxUtils.bind(this, function()
	{
		this.autosaveThread = null;
		this.lastAutosave = null;

		this.save(false, function()
		{
			if (success != null)
			{
				success();
			}
		}, function()
		{
			if (error != null)
			{
				error();
			}
		});
	}), tmp);
};

/**
 * Adds the listener for automatically saving the diagram for local changes.
 */
File.prototype.clearAutosave = function()
{
	if (this.autosaveThread != null)
	{
		window.clearTimeout(this.autosaveThread);
		this.autosaveThread = null;
	}
};

/**
 * Returns the location as a new object.
 * @type mx.Point
 */
File.prototype.close = function()
{
	if (this.isAutosave() && this.isModified())
	{
		this.save(true);
	}
	
	if (this.changeListener != null)
	{
		this.ui.editor.graph.model.removeListener(this.changeListener);
		this.changeListener = null;
	}
};
