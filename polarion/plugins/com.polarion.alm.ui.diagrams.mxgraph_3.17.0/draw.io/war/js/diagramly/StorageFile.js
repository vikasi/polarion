// $Id = StorageFile.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
StorageFile = function(ui, data, title)
{
	File.call(this, ui, data);
	
	this.title = title;
};

//Extends mxEventSource
mxUtils.extend(StorageFile, File);

/**
 * Sets the delay for autosave in milliseconds. Default is 500.
 */
StorageFile.prototype.autosaveDelay = 500;

/**
 * Sets the delay for autosave in milliseconds. Default is 500.
 */
StorageFile.prototype.maxAutosaveDelay = 10000;

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.getMode = function()
{
	return App.MODE_BROWSER;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.getHash = function()
{
	return 'L' + encodeURIComponent(this.getTitle());
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.getTitle = function()
{
	return this.title;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.isRenamable = function()
{
	return true;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.save = function(revision, success, error)
{
	this.saveAs(this.getTitle(), success, error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.saveAs = function(title, success, error)
{
	File.prototype.save.apply(this, arguments);
	this.doSave(title, success, error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.doSave = function(title, success, error)
{
	var data = this.getData();
	
	var fn = mxUtils.bind(this, function()
	{
		this.title = title;
		localStorage.setItem(this.title, data);
		this.setModified(false);
		this.contentChanged();
		
		if (success != null)
		{
			success();
		}
	});
	
	if (this.getTitle() == title || localStorage.getItem(title) == null)
	{
		fn();
	}
	else
	{
		this.ui.confirm(mxResources.get('replace', [title]), fn, error);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
StorageFile.prototype.rename = function(title, success, error)
{
	this.doSave(title, success, error);
};

/**
 * Returns the location as a new object.
 * @type mx.Point
 */
StorageFile.prototype.open = function()
{
	File.prototype.open.apply(this, arguments);

	// Immediately creates the storage entry
	this.doSave(this.getTitle());
};
