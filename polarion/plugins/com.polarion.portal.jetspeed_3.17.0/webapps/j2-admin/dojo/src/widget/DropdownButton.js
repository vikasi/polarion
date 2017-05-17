dojo.provide("dojo.widget.DropdownButton");

// Draws a button with a down arrow;
// when you press the down arrow something appears (usually a menu)

dojo.require("dojo.widget.Widget");

dojo.requireIf("html", "dojo.widget.html.DropdownButton");

dojo.widget.tags.addParseTreeHandler("dojo:dropdownbutton");

dojo.widget.DropdownButton = function(){
	dojo.widget.Widget.call(this);

	this.widgetType = "DropdownButton";
}
dojo.inherits(dojo.widget.DropdownButton, dojo.widget.Widget);
