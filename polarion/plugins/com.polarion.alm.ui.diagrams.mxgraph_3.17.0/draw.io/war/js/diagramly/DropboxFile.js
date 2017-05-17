// $Id = DropboxFile.js,v 1.12 2010-01-02 09 =45 =14 gaudenz Exp $
// Copyright (c) 2006-2014, JGraph Ltd
/**
 * Constructs a new point for the optional x and y coordinates. If no
 * coordinates are given, then the default values for <x> and <y> are used.
 * @constructor
 * @class Implements a basic 2D point. Known subclassers = {@link mxRectangle}.
 * @param {number} x X-coordinate of the point.
 * @param {number} y Y-coordinate of the point.
 */
DropboxFile = function(ui, data, stat)
{
	File.call(this, ui, data);
	
	this.stat = stat;
};

//Extends mxEventSource
mxUtils.extend(DropboxFile, File);

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.getHash = function()
{
	return 'D' + encodeURIComponent(this.stat.path.substring(1));
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.getMode = function()
{
	return App.MODE_DROPBOX;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.getTitle = function()
{
	return this.stat.name;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.isRenamable = function()
{
	return true;
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.save = function(revision, success, error)
{
	this.doSave(this.getTitle(), success, error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.saveAs = function(title, success, error)
{
	this.doSave(title, success, error);
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.doSave = function(title, success, error)
{
	File.prototype.save.apply(this, arguments);
	
	var fn = mxUtils.bind(this, function(checked)
	{
		if (checked)
		{
			this.ui.dropbox.saveFile(title, this.getData(), mxUtils.bind(this, function(stat)
			{
				this.stat = stat;
				this.setModified(false);
				this.contentChanged();
				
				if (success != null)
				{
					success();
				}
			}), error);
		}
		else if (error != null)
		{
			error();
		}
	});
	
	if (this.getTitle() == title)
	{
		fn(true);
	}
	else
	{
		this.ui.dropbox.checkExists(title, fn);
	}
};

/**
 * Translates this point by the given vector.
 * 
 * @param {number} dx X-coordinate of the translation.
 * @param {number} dy Y-coordinate of the translation.
 */
DropboxFile.prototype.rename = function(title, success, error)
{
	this.ui.dropbox.renameFile(this, title, mxUtils.bind(this, function(stat)
	{
		this.stat = stat;
		this.descriptorChanged();
		
		if (success != null)
		{
			success();
		}
	}), error);
};
