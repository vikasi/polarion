/**
 * $Id: mxAndroid.js,v 1.3 2014/01/07 16:23:23 mate Exp $
 * Copyright (c) 2006-2013, JGraph Ltd
 */

//**********************************************************************************************************************************************************
//Horizontal Tab Bar
//**********************************************************************************************************************************************************
/**
 * Extends mxShape.
 */
function mxShapeAndroidTabBar(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
 * Extends mxShape.
 */
mxUtils.extend(mxShapeAndroidTabBar, mxShape);

mxShapeAndroidTabBar.prototype.cst = {
		MAIN_TEXT : 'mainText',
		SHAPE_TAB_BAR : 'mxgraph.android.tabBar',
		TEXT_COLOR : 'textColor',
		TEXT_COLOR2 : 'textColor2',
		STROKE_COLOR2 : 'strokeColor2',
		FILL_COLOR2 : 'fillColor2',
		SELECTED : '+',			//must be 1 char
		TEXT_SIZE : 'textSize'
};

/**
 * Function: paintVertexShape
 * 
 * Paints the vertex shape.
 */
mxShapeAndroidTabBar.prototype.paintVertexShape = function(c, x, y, w, h)
{
	var textStrings = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.MAIN_TEXT, '+Tab 1, Tab 2, Tab 3').toString().split(',');
	var fontColor = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.TEXT_COLOR, 'none');
	var selectedFontColor = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.TEXT_COLOR2, 'none');
	var fontSize = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.TEXT_SIZE, '17').toString();
	var frameColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, 'none');
	var separatorColor = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.STROKE_COLOR2, 'none');
	var bgColor = mxUtils.getValue(this.style, mxConstants.STYLE_FILLCOLOR, 'none');
	var selectedFillColor = mxUtils.getValue(this.style, mxShapeAndroidTabBar.prototype.cst.FILL_COLOR2, 'none');
	var buttonNum = textStrings.length;
	var buttonWidths = new Array(buttonNum);
	var buttonTotalWidth = 0;
	var selectedButton = -1;
	var labelOffset = 5;

	for (var i = 0; i < buttonNum; i++)
	{
		var buttonText = textStrings[i];

		if(buttonText.charAt(0) === mxShapeAndroidTabBar.prototype.cst.SELECTED)
		{
			buttonText = textStrings[i].substring(1);
			selectedButton = i;
		}

		buttonWidths[i] = mxUtils.getSizeForString(buttonText, fontSize, mxConstants.DEFAULT_FONTFAMILY).width;

		buttonTotalWidth += buttonWidths[i];
	}

	var trueH = Math.max(h, fontSize * 1.5, 7);
	var minW = 2 * labelOffset * buttonNum + buttonTotalWidth;
	var trueW = Math.max(w, minW);

	c.translate(x, y);
	this.background(c, trueW, trueH, buttonNum, buttonWidths, labelOffset, minW, frameColor, separatorColor, bgColor, selectedFillColor, selectedButton);
	c.setShadow(false);

	c.setFontStyle(mxConstants.FONT_BOLD);
	var currWidth = 0;

	for (var i = 0; i < buttonNum; i++)
	{
		if (i === selectedButton)
		{
			c.setFontColor(selectedFontColor);
		}
		else
		{
			c.setFontColor(fontColor);
		}

		currWidth = currWidth + labelOffset;
		this.buttonText(c, currWidth, trueH, textStrings[i], buttonWidths[i], fontSize, minW, trueW);
		currWidth = currWidth + buttonWidths[i] + labelOffset;
	}
};

mxShapeAndroidTabBar.prototype.background = function(c, w, h, buttonNum, buttonWidths, labelOffset, minW, frameColor, separatorColor, bgColor, selectedFillColor, selectedButton)
{
	c.begin();

	//draw the frame
	c.setStrokeColor(frameColor);
	c.setFillColor(bgColor);
	c.rect(0, 0, w, h);
	c.fillAndStroke();

	//draw the button separators
	c.setStrokeColor(separatorColor);
	c.begin();
	for (var i = 1; i < buttonNum; i++)
	{
		if (i !== selectedButton && i !== (selectedButton + 1))
		{
			var currWidth = 0;

			for (var j = 0; j < i; j++)
			{
				currWidth += buttonWidths[j] + 2 * labelOffset;
			}

			currWidth = currWidth * w / minW;
			c.moveTo(currWidth, h * 0.2);
			c.lineTo(currWidth, h * 0.8);
		}
	}

	c.stroke();

	//draw the selected button
	var buttonLeft = 0;
	c.setFillColor(selectedFillColor);

	for (var i = 0; i < selectedButton; i++)
	{
		buttonLeft += buttonWidths[i] + 2 * labelOffset;
	}

	buttonLeft = buttonLeft * w / minW;
	var buttonRight = (buttonWidths[selectedButton] + 2 * labelOffset) * w / minW;
	buttonRight += buttonLeft;

	c.rect(buttonLeft, 0, buttonRight - buttonLeft, h);
	c.fill();

	c.setAlpha(1);
	c.setFillColor('#33b5e5');
	c.rect(buttonLeft, h * 0.9, buttonRight - buttonLeft, h * 0.1);
	c.fill();
};

mxShapeAndroidTabBar.prototype.buttonText = function(c, w, h, textString, buttonWidth, fontSize, minW, trueW)
{
	if(textString.charAt(0) === mxShapeAndroidTabBar.prototype.cst.SELECTED)
	{
		textString = textString.substring(1);
	}

	c.begin();
	c.setFontSize(fontSize);
	c.text((w + buttonWidth * 0.5) * trueW / minW, h * 0.5, 0, 0, textString, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
};

mxCellRenderer.prototype.defaultShapes[mxShapeAndroidTabBar.prototype.cst.SHAPE_TAB_BAR] = mxShapeAndroidTabBar;

//**********************************************************************************************************************************************************
//Android Phone Vertical
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidPhone(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidPhone, mxShape);

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidPhone.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);
	var rSize = 25;

	c.roundrect(0, 0, w, h, rSize, rSize);
	c.fillAndStroke();
	
	c.setShadow(false);
	
	this.foreground(c, x, y, w, h, rSize);
};

mxShapeAndroidPhone.prototype.foreground = function(c, x, y, w, h, rSize)
{
	c.rect(w * 0.0625, h * 0.15, w * 0.875, h * 0.7);
	c.stroke();

	c.ellipse(w * 0.4875, h * 0.04125, w * 0.025, h * 0.0125);
	c.stroke();

	c.roundrect(w * 0.375, h * 0.075, w * 0.25, h * 0.01875, w * 0.02, h * 0.01);
	c.stroke();
	c.ellipse(w * 0.4, h * 0.875, w * 0.2, h * 0.1);
	c.stroke();
	c.roundrect(w * 0.4575, h * 0.905, w * 0.085, h * 0.04375, h * 0.00625, h * 0.00625);
	c.stroke();
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.phone'] = mxShapeAndroidPhone;

//**********************************************************************************************************************************************************
//Android Status Bar
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidStatusBar(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidStatusBar, mxShape);

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidStatusBar.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);
	this.background(c, x, y, w, h);
	c.setShadow(false);
	this.foreground(c, x, y, w, h);
};

mxShapeAndroidStatusBar.prototype.background = function(c, x, y, w, h)
{
	c.rect(0, 0, w, h);
	c.fill();
};

mxShapeAndroidStatusBar.prototype.foreground = function(c, x, y, w, h)
{
	c.rect(0, 0, w, h);
	c.fill();

	c.setFontColor(mxUtils.getValue(this.style, mxConstants.STYLE_FONTCOLOR, '#222222'));
	c.setFontSize(mxUtils.getValue(this.style, mxConstants.STYLE_FONTSIZE, '5'));
	c.text(w - 30, h * 0.5 + 1, 0, 0, '12:00', mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);

	c.setFillColor('#444444');
	c.begin();
	c.moveTo(w - 37, h * 0.5 + 6);
	c.lineTo(w - 37, h * 0.5 - 5);
	c.lineTo(w - 36, h * 0.5 - 5);
	c.lineTo(w - 36, h * 0.5 - 6);
	c.lineTo(w - 32, h * 0.5 - 6);
	c.lineTo(w - 32, h * 0.5 - 5);
	c.lineTo(w - 31, h * 0.5 - 5);
	c.lineTo(w - 31, h * 0.5 + 6);
	c.close();
	c.fill();
	
	c.setFillColor(mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, 'none'));
	c.ellipse(w - 56, h * 0.5 + 2, 2, 2);
	c.fillAndStroke();
	
	c.setStrokeWidth(2);
	c.begin();
	c.moveTo(w - 52, h * 0.5 + 1);
	c.arcTo(3.5, 3.5, 0, 0, 0, w - 58, h * 0.5 + 1);
	c.stroke();
	
	c.begin();
	c.moveTo(w - 50, h * 0.5 - 1);
	c.arcTo(6, 6, 0, 0, 0, w - 60, h * 0.5 - 1);
	c.stroke();
	
	c.setStrokeWidth(1);
	
	c.rect(w - 51, h * 0.5 + 5, 2, 1);
	c.fill();
	
	c.rect(w - 48, h * 0.5 + 2, 2, 4);
	c.fill();
	
	c.rect(w - 45, h * 0.5 - 1, 2, 7);
	c.fill();
	
	c.rect(w - 42, h * 0.5 - 4, 2, 10);
	c.fill();

//	c.setFillColor('#444444');
	c.rect(w - 37, h * 0.5  - 2, 6, 8);
	c.fill();
	
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.statusBar'] = mxShapeAndroidStatusBar;

//**********************************************************************************************************************************************************
//Checkbox Button Group
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidCheckboxGroup(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidCheckboxGroup, mxShape);

mxShapeAndroidCheckboxGroup.prototype.cst = {
		STYLE_TEXTCOLOR2 : 'textColor2',
		STYLE_STROKECOLOR2 : 'strokeColor2',
		BUTTON_TEXT : 'buttonText',
		SELECTED : '+' 		//has to be one character long
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidCheckboxGroup.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var fontColor = mxUtils.getValue(this.style, mxConstants.STYLE_TEXTCOLOR, '#666666');
	var fontSize = mxUtils.getValue(this.style, mxConstants.STYLE_FONTSIZE, '8').toString();
	var optionText = mxUtils.getValue(this.style, mxShapeAndroidCheckboxGroup.prototype.cst.BUTTON_TEXT, 'Option 1').toString().split(',');
	var strokeColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, 'none');
	var strokeColor2 = mxUtils.getValue(this.style, mxShapeAndroidCheckboxGroup.prototype.cst.STYLE_STROKECOLOR2, 'none');
	
	c.setStrokeColor(strokeColor2);
	var optionNum = optionText.length;
	var buttonSize = 5;
	var lineH = Math.max(fontSize * 1.5, buttonSize);
	var maxTextWidth = 0;
	var selected = -1;
	var labelOffset = 2.5;
	var minH = optionNum * lineH;
	var trueH = Math.max(h, minH);

	//get min width and selected option 
	for (var i = 0; i < optionNum; i++)
	{
		var currText = optionText[i];

		if(currText.charAt(0) === mxShapeAndroidCheckboxGroup.prototype.cst.SELECTED)
		{
			currText = optionText[i].substring(1);
			selected = i;
		}

		var currWidth = mxUtils.getSizeForString(currText, fontSize, mxConstants.DEFAULT_FONTFAMILY).width;

		if (currWidth > maxTextWidth)
		{
			maxTextWidth = currWidth;
		}
	}

	var minW = 2 * labelOffset + maxTextWidth + 2 * buttonSize;
	var trueW = Math.max(w, minW);

	//draw the background
	c.roundrect(0, 0, trueW, trueH, 2.5, 2.5);
	c.fillAndStroke();
	c.setShadow(false);

	c.setFontSize(fontSize);
	c.setFontColor(fontColor);
	c.setStrokeColor(strokeColor);

	for (var i = 0; i < optionNum; i++)
	{
		var currHeight = (i * lineH + lineH * 0.5) * trueH / minH;

		var currText = optionText[i];

		if(currText.charAt(0) === mxShapeAndroidCheckboxGroup.prototype.cst.SELECTED)
		{
			currText = optionText[i].substring(1);
			selected = i;
		}

		c.text(buttonSize * 2 + labelOffset, currHeight, 0, 0, currText, mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);

		var iconX = buttonSize * 0.5;
		var iconY = currHeight - buttonSize * 0.5;

		if (selected === i)
		{
			c.rect(iconX, iconY, buttonSize, buttonSize);
			c.fillAndStroke();
			c.begin();
			c.moveTo(iconX + buttonSize * 0.25, iconY + buttonSize * 0.5);
			c.lineTo(iconX + buttonSize * 0.5, iconY + buttonSize * 0.75);
			c.lineTo(iconX + buttonSize * 0.75, iconY + buttonSize * 0.25);
			c.stroke();
		}
		else
		{
			c.rect(iconX, iconY, buttonSize, buttonSize);
			c.fillAndStroke();
		}

		selected = -1;
	}
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.checkboxGroup'] = mxShapeAndroidCheckboxGroup;

//**********************************************************************************************************************************************************
//Radio Button Group
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidRadioGroup(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidRadioGroup, mxShape);

mxShapeAndroidRadioGroup.prototype.cst = {
		STYLE_TEXTCOLOR2 : 'textColor2',
		STYLE_STROKECOLOR2 : 'strokeColor2',
		BUTTON_TEXT : 'buttonText',
		SELECTED : '+' 		//has to be one character long
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidRadioGroup.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var fontColor = mxUtils.getValue(this.style, mxConstants.STYLE_TEXTCOLOR, '#666666');
	var fontSize = mxUtils.getValue(this.style, mxConstants.STYLE_FONTSIZE, '8').toString();
	var optionText = mxUtils.getValue(this.style, mxShapeAndroidRadioGroup.prototype.cst.BUTTON_TEXT, 'Option 1').toString().split(',');
	var strokeColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, 'none');
	var strokeColor2 = mxUtils.getValue(this.style, mxShapeAndroidRadioGroup.prototype.cst.STYLE_STROKECOLOR2, 'none');

	c.setStrokeColor(strokeColor2);

	var optionNum = optionText.length;
	var buttonSize = 5;
	var lineH = Math.max(fontSize * 1.5, buttonSize);
	var maxTextWidth = 0;
	var selected = -1;
	var labelOffset = 2.5;
	var minH = optionNum * lineH;
	var trueH = Math.max(h, minH);

	//get min width and selected option 
	for (var i = 0; i < optionNum; i++)
	{
		var currText = optionText[i];

		if(currText.charAt(0) === mxShapeAndroidRadioGroup.prototype.cst.SELECTED)
		{
			currText = optionText[i].substring(1);
			selected = i;
		}

		var currWidth = mxUtils.getSizeForString(currText, fontSize, mxConstants.DEFAULT_FONTFAMILY).width;

		if (currWidth > maxTextWidth)
		{
			maxTextWidth = currWidth;
		}
	}

	var minW = 2 * labelOffset + maxTextWidth + 2 * buttonSize;
	var trueW = Math.max(w, minW);

	//draw the background
	c.roundrect(0, 0, trueW, trueH, 2.5, 2.5);
	c.fillAndStroke();
	c.setShadow(false);

	c.setFontSize(fontSize);
	c.setFontColor(fontColor);
	c.setStrokeColor(strokeColor);
	c.setFillColor(strokeColor);

	for (var i = 0; i < optionNum; i++)
	{
		var currHeight = (i * lineH + lineH * 0.5) * trueH / minH;

		var currText = optionText[i];

		if(currText.charAt(0) === mxShapeAndroidRadioGroup.prototype.cst.SELECTED)
		{
			currText = optionText[i].substring(1);
			selected = i;
		}

		c.text(buttonSize * 2 + labelOffset, currHeight, 0, 0, currText, mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);

		var iconX = buttonSize * 0.5;
		var iconY = currHeight - buttonSize * 0.5;

		if (selected === i)
		{
			c.ellipse(iconX, iconY, buttonSize, buttonSize);
			c.stroke();
			c.ellipse(iconX + buttonSize * 0.25, iconY + buttonSize * 0.25, buttonSize * 0.5, buttonSize * 0.5);
			c.fillAndStroke();
		}
		else
		{
			c.ellipse(iconX, iconY, buttonSize, buttonSize);
			c.stroke();
		}
	}
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.radioGroup'] = mxShapeAndroidRadioGroup;

//**********************************************************************************************************************************************************
//Menu Bar
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidMenuBar(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidMenuBar, mxShape);

mxShapeAndroidMenuBar.prototype.cst = {
		MENU_TEXT : 'menuText'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidMenuBar.prototype.paintVertexShape = function(c, x, y, w, h)
{
	var textStrings = mxUtils.getValue(this.style, mxShapeAndroidMenuBar.prototype.cst.MENU_TEXT, 'Item 1, Item 2, Item 3').toString().split(',');
	var fontSize = mxUtils.getValue(this.style, mxConstants.STYLE_FONTSIZE, '12');
	var buttonNum = textStrings.length;
	var maxButtonWidth = 0;
	var labelOffset = 2.5;

	for (var i = 0; i < buttonNum; i++)
	{
		var buttonText = textStrings[i];

		var currWidth = mxUtils.getSizeForString(buttonText, fontSize, mxConstants.DEFAULT_FONTFAMILY).width;

		if (currWidth > maxButtonWidth)
		{
			maxButtonWidth = currWidth;
		}
	}

	var minButtonHeight =  fontSize * 1.5;
	var minH = buttonNum * minButtonHeight;
	var trueH = Math.max(h, minH);
	var minW = 2 * labelOffset + maxButtonWidth;
	var trueW = Math.max(w, minW);

	c.translate(x, y);

	c.rect(0, 0, w, trueH);
	c.fillAndStroke();

	c.setShadow(false);

	//draw the button separators
	c.begin();

	for (var i = 1; i < buttonNum; i++)
	{
			var currHeight = i * minButtonHeight * trueH / minH;
			c.moveTo(0, currHeight);
			c.lineTo(w, currHeight);
	}

	c.stroke();

	for (var i = 0; i < buttonNum; i++)
	{
		currWidth = currWidth + labelOffset;
		var currHeight = (i * minButtonHeight + minButtonHeight * 0.5) * trueH / minH;
		c.text(10, currHeight, 0, 0, textStrings[i], mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
	}
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.menuBar'] = mxShapeAndroidMenuBar;

//**********************************************************************************************************************************************************
//Text Selection Handles
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeAndroidTextSelHandles(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeAndroidTextSelHandles, mxShape);

mxShapeAndroidRadioGroup.prototype.cst = {
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeAndroidTextSelHandles.prototype.paintVertexShape = function(c, x, y, w, h)
{
	var strokeColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, 'none');
	c.translate(x, y);

	var handleSizeX = 15;
	var barH = Math.max(0, h - handleSizeX * 1.5);
	c.setAlpha(0.5);
	
	c.rect(handleSizeX, 0, w - 2 * handleSizeX, barH);
	c.fill();
	
	c.begin();
	c.moveTo(0, h - handleSizeX);
	c.lineTo(handleSizeX * 0.5, h - handleSizeX * 1.5);
	c.lineTo(handleSizeX, h - handleSizeX);
	c.close();
	c.moveTo(w - handleSizeX, h - handleSizeX);
	c.lineTo(w - handleSizeX * 0.5, h - handleSizeX * 1.5);
	c.lineTo(w, h - handleSizeX);
	c.close();
	c.fill();
	
	c.setFillColor(strokeColor);
	c.rect(0, h - handleSizeX, handleSizeX, handleSizeX);
	c.fill();
	c.rect(w - handleSizeX, h - handleSizeX, handleSizeX, handleSizeX);
	c.fill();
};

mxCellRenderer.prototype.defaultShapes['mxgraph.android.textSelHandles'] = mxShapeAndroidTextSelHandles;

