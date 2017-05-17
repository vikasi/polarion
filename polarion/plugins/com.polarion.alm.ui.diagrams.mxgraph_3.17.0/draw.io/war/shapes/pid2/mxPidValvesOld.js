/**
 * $Id: mxPidValvesOld.js,v 1.1 2013/09/09 04:54:19 mate Exp $
 * Copyright (c) 2006-2013, JGraph Ltd
 */

//**********************************************************************************************************************************************************
//Valve
//**********************************************************************************************************************************************************
/**
 * Extends mxShape.
 */
function mxShapePidValve(bounds, fill, stroke, strokewidth)
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
mxUtils.extend(mxShapePidValve, mxShape);

mxShapePidValve.prototype.cst = {
		SHAPE_VALVE : 'mxgraph.pid2valves.valve',
		//states
		DEFAULT_STATE : 'defState',
		CLOSED : 'closed',
		OPEN : 'open',
		//actuators
		ACTUATOR : 'actuator',
		MANUAL : 'man',
		DIAPHRAGM : 'diaph',
		BALANCED_DIAPHRAGM : 'balDiaph',
		MOTOR : 'motor',
		SPRING : 'spring',
		PILOT : 'pilot',
		SOLENOID : 'solenoid',
		SOLENOID_MANUAL_RESET : 'solenoidManRes',
		ONE_ACTING : 'singActing',
		TWO_ACTING : 'dbrActing',
		PILOT_CYLINDER : 'pilotCylinder',
		DIGITAL : 'digital',
		WEIGHT : 'weight',
		KEY : 'key',
		ELECTRO_HYDRAULIC : 'elHyd',
		//types
		VALVE_TYPE : 'valveType',
		BUTTERFLY : 'butterfly',
		CHECK : 'check',
		PLUG : 'plug',
		GATE : 'gate',
		GLOBE : 'globe',
		NEEDLE : 'needle',
		PLUG : 'plug',
		SELF_DRAINING : 'selfDrain',
		BALL : 'ball'
};

/**
 * Function: paintVertexShape
 * 
 * Paints the vertex shape.
 */
mxShapePidValve.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);
	this.background(c, x, y, w, h);
	c.setShadow(false);
	this.foreground(c, x, y, w, h);
};

mxShapePidValve.prototype.background = function(c, x, y, w, h)
{
	var defState = mxUtils.getValue(this.style, mxShapePidValve.prototype.cst.DEFAULT_STATE, 'open');
	var fillColor = mxUtils.getValue(this.style, mxConstants.STYLE_FILLCOLOR, '#ffffff');
	var strokeColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, '#000000');
	var valveType = mxUtils.getValue(this.style, mxShapePidValve.prototype.cst.VALVE_TYPE, 'gate');
	var actuator = mxUtils.getValue(this.style, mxShapePidValve.prototype.cst.ACTUATOR, 'none');

	if (valveType === mxShapePidValve.prototype.cst.BALL || valveType === mxShapePidValve.prototype.cst.GLOBE)
	{
		c.ellipse(w * 0.3, h * 0.55, w * 0.4, h * 0.4)
		c.fillAndStroke();
	}
	else if (valveType === mxShapePidValve.prototype.cst.PLUG)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.6);
		c.lineTo(w * 0.6, h * 0.75);
		c.lineTo(w * 0.5, h * 0.9);
		c.lineTo(w * 0.4, h * 0.75);
		c.close();
		c.fillAndStroke();
	}
	else if (valveType === mxShapePidValve.prototype.cst.NEEDLE)
	{
		c.begin();
		c.moveTo(w * 0.45, h * 0.55);
		c.lineTo(w * 0.55, h * 0.55);
		c.lineTo(w * 0.5, h * 0.9);
		c.close();
		c.fillAndStroke();
	}
	else if (valveType === mxShapePidValve.prototype.cst.SELF_DRAINING)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.75);
		c.lineTo(w * 0.5, h * 0.98);
		c.stroke();

		c.begin();
		c.moveTo(w * 0.48, h * 0.95);
		c.lineTo(w * 0.52, h * 0.95);
		c.lineTo(w * 0.5, h * 0.99);
		c.close();
		c.setFillColor(strokeColor);
		c.fillAndStroke();
		c.setFillColor(fillColor);
	}

	if (actuator === mxShapePidValve.prototype.cst.MANUAL)
	{
		c.begin();
		c.moveTo(w * 0.3, h * 0.25);
		c.lineTo(w * 0.7, h * 0.25);
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.DIAPHRAGM)
	{
		c.begin();
		c.moveTo(w * 0.3, h * 0.25);
		c.arcTo(w * 0.3, h * 0.3, 0, 0, 1, w * 0.7, h * 0.25);
		c.close();
		c.fillAndStroke();

		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.BALANCED_DIAPHRAGM)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.stroke();

		c.ellipse(w * 0.3, h * 0.15, w * 0.4, h * 0.2);
		c.fillAndStroke();

		c.begin();
		c.moveTo(w * 0.3, h * 0.25);
		c.lineTo(w * 0.7, h * 0.25);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.MOTOR ||
			actuator === mxShapePidValve.prototype.cst.ELECTRO_HYDRAULIC)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.stroke();

		c.ellipse(w * 0.35, h * 0.1, w * 0.3, h * 0.3);
		c.fillAndStroke();

		//set the proper actuator marking
		var m = '';

		if (actuator === mxShapePidValve.prototype.cst.MOTOR)
		{
			m = 'M';
		}
		else if (actuator === mxShapePidValve.prototype.cst.ELECTRO_HYDRAULIC)
		{
			m = 'E/H';
		}

		c.setFontSize(Math.min(w, h) * 0.15);
		c.setFontStyle(1);
		c.text(w * 0.5, h * 0.25, 0, 0, m, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
	}
	else if (actuator === mxShapePidValve.prototype.cst.PILOT ||
			actuator === mxShapePidValve.prototype.cst.SOLENOID ||
			actuator === mxShapePidValve.prototype.cst.SOLENOID_MANUAL_RESET ||
			actuator === mxShapePidValve.prototype.cst.DIGITAL ||
			actuator === mxShapePidValve.prototype.cst.WEIGHT ||
			actuator === mxShapePidValve.prototype.cst.KEY)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.stroke();

		c.rect(w * 0.35, h * 0.1, w * 0.3, h * 0.3);
		c.fillAndStroke();

		//set the proper actuator marking
		var m = '';

		if (actuator === mxShapePidValve.prototype.cst.PILOT)
		{
			m = 'P';
		}
		else if (actuator === mxShapePidValve.prototype.cst.SOLENOID)
		{
			m = 'S';
		}
		else if (actuator === mxShapePidValve.prototype.cst.SOLENOID_MANUAL_RESET)
		{
			m = 'S';

			c.begin();
			c.moveTo(w * 0.5, h * 0.45);
			c.lineTo(w * 0.65, h * 0.45);
			c.stroke();

			c.begin();
			c.moveTo(w * 0.65, h * 0.45);
			c.lineTo(w * 0.775, h * 0.325);
			c.lineTo(w * 0.9, h * 0.45);
			c.lineTo(w * 0.775, h * 0.575);
			c.close();
			c.fillAndStroke();

			c.setFontStyle(1);
			c.setFontSize(Math.min(w, h) * 0.1);
			c.text(w * 0.775, h * 0.45, 0, 0, 'R', mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
		}
		else if (actuator === mxShapePidValve.prototype.cst.DIGITAL)
		{
			m = 'D';
		}
		else if (actuator === mxShapePidValve.prototype.cst.WEIGHT)
		{
			m = 'W';
		}
		else if (actuator === mxShapePidValve.prototype.cst.KEY)
		{
			m = 'K';
		}

		c.setFontSize(Math.min(w, h) * 0.15);
		c.setFontStyle(1);
		c.text(w * 0.5, h * 0.25, 0, 0, m, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
	}
	else if (actuator === mxShapePidValve.prototype.cst.SPRING)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.1);
		c.lineTo(w * 0.5, h * 0.75);
		c.moveTo(w * 0.45, h * 0.2);
		c.lineTo(w * 0.55, h * 0.15);
		c.moveTo(w * 0.42, h * 0.31);
		c.lineTo(w * 0.58, h * 0.23);
		c.moveTo(w * 0.39, h * 0.44);
		c.lineTo(w * 0.61, h * 0.33);
		c.moveTo(w * 0.36, h * 0.57);
		c.lineTo(w * 0.64, h * 0.43);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.ONE_ACTING)
	{
		c.rect(w * 0.35, h * 0.1, w * 0.3, h * 0.3);
		c.fillAndStroke();

		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.moveTo(w * 0.35, h * 0.25);
		c.lineTo(w, h * 0.25);
		c.moveTo(w * 0.85, h * 0.2);
		c.lineTo(w * 0.8, h * 0.3);
		c.moveTo(w * 0.88, h * 0.2);
		c.lineTo(w * 0.83, h * 0.3);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.TWO_ACTING)
	{
		c.rect(w * 0.35, h * 0.1, w * 0.3, h * 0.3);
		c.fillAndStroke();

		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.moveTo(w * 0.35, h * 0.25);
		c.lineTo(w * 0.65, h * 0.25);
		c.moveTo(w * 0.65, h * 0.175);
		c.lineTo(w, h * 0.175);
		c.moveTo(w * 0.85, h * 0.125);
		c.lineTo(w * 0.8, h * 0.225);
		c.moveTo(w * 0.88, h * 0.125);
		c.lineTo(w * 0.83, h * 0.225);
		c.moveTo(w * 0.65, h * 0.325);
		c.lineTo(w, h * 0.325);
		c.moveTo(w * 0.85, h * 0.275);
		c.lineTo(w * 0.8, h * 0.375);
		c.moveTo(w * 0.88, h * 0.275);
		c.lineTo(w * 0.83, h * 0.375);
		c.stroke();
	}
	else if (actuator === mxShapePidValve.prototype.cst.PILOT_CYLINDER)
	{
		c.rect(w * 0.35, h * 0.1, w * 0.3, h * 0.3);
		c.fillAndStroke();

		c.begin();
		c.moveTo(w * 0.5, h * 0.25);
		c.lineTo(w * 0.5, h * 0.75);
		c.moveTo(w * 0.35, h * 0.25);
		c.lineTo(w * 0.8, h * 0.25);
		c.stroke();

		c.rect(w * 0.8, h * 0.15, w * 0.2, h * 0.2);
		c.fillAndStroke();

		c.setFontSize(Math.min(w, h) * 0.15);
		c.setFontStyle(1);
		c.text(w * 0.9, h * 0.25, 0, 0, 'P', mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, 0, null, 0, 0, 0);
	}

	if (valveType === mxShapePidValve.prototype.cst.GATE || 
			valveType === mxShapePidValve.prototype.cst.BALL ||
			valveType === mxShapePidValve.prototype.cst.PLUG ||
			valveType === mxShapePidValve.prototype.cst.NEEDLE ||
			valveType === mxShapePidValve.prototype.cst.SELF_DRAINING ||
			valveType === mxShapePidValve.prototype.cst.GLOBE)
	{
		c.begin();
		c.moveTo(0, h * 0.5);
		c.lineTo(w * 0.5, h * 0.75);
		c.lineTo(0, h);
		c.close();

		c.moveTo(w, h * 0.5);
		c.lineTo(w, h);
		c.lineTo(w * 0.5, h * 0.75);
		c.close();

		if (defState === mxShapePidValve.prototype.cst.CLOSED)
		{
			c.setFillColor(strokeColor);
			c.fillAndStroke();
			c.setFillColor(fillColor);
		}
		else
		{
			c.fillAndStroke();
		}
	}
	else if (valveType === mxShapePidValve.prototype.cst.BUTTERFLY || 
			valveType === mxShapePidValve.prototype.cst.CHECK)
	{
		c.begin();
		c.moveTo(0, h);
		c.lineTo(0, h * 0.5);
		c.lineTo(w, h);
		c.lineTo(w, h * 0.5);
		c.stroke();

		if (valveType === mxShapePidValve.prototype.cst.BUTTERFLY)
		{
			c.ellipse(w * 0.4, h * 0.65, w * 0.2, h * 0.2);
			c.fillAndStroke();
		}
		else if (valveType === mxShapePidValve.prototype.cst.CHECK)
		{
			c.begin();
			c.moveTo(w * 0.89, h * 0.86);
			c.lineTo(w * 0.99, h * 0.995);
			c.lineTo(w * 0.82, h * 0.995);
			c.close();
			c.setFillColor(strokeColor);
			c.fillAndStroke();
			c.setFillColor(fillColor);
		}
	}


};

mxShapePidValve.prototype.foreground = function(c, x, y, w, h)
{
	var defState = mxUtils.getValue(this.style, mxShapePidValve.prototype.cst.DEFAULT_STATE, 'open');
	var fillColor = mxUtils.getValue(this.style, mxConstants.STYLE_FILLCOLOR, '#ffffff');
	var strokeColor = mxUtils.getValue(this.style, mxConstants.STYLE_STROKECOLOR, '#000000');
	var valveType = mxUtils.getValue(this.style, mxShapePidValve.prototype.cst.VALVE_TYPE, 'gate');

	if (valveType === mxShapePidValve.prototype.cst.BALL)
	{
		c.ellipse(w * 0.3, h * 0.55, w * 0.4, h * 0.4)
		c.fillAndStroke();
	}
	else if (valveType === mxShapePidValve.prototype.cst.GLOBE)
	{
		c.ellipse(w * 0.3, h * 0.55, w * 0.4, h * 0.4)
		c.setFillColor(strokeColor);
		c.fillAndStroke();
		c.setFillColor(fillColor);
	}
	else if (valveType === mxShapePidValve.prototype.cst.PLUG)
	{
		c.begin();
		c.moveTo(w * 0.5, h * 0.6);
		c.lineTo(w * 0.6, h * 0.75);
		c.lineTo(w * 0.5, h * 0.9);
		c.lineTo(w * 0.4, h * 0.75);
		c.close();
		c.fillAndStroke();
	}
	else if (valveType === mxShapePidValve.prototype.cst.NEEDLE)
	{
		c.begin();
		c.moveTo(w * 0.45, h * 0.55);
		c.lineTo(w * 0.55, h * 0.55);
		c.lineTo(w * 0.5, h * 0.9);
		c.close();
		c.fillAndStroke();
	}
};

mxCellRenderer.prototype.defaultShapes[mxShapePidValve.prototype.cst.SHAPE_VALVE] = mxShapePidValve;
