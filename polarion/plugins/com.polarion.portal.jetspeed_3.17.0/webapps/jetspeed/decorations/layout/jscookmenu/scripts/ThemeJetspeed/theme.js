
// directory of where all the images are
var cmThemeJetspeedBase = 'decorations/layout/jscookmenu/images/ThemeJetspeed/';

var cmThemeJetspeed =
{
  	// main menu display attributes
  	//
  	// Note.  When the menu bar is horizontal,
  	// mainFolderLeft and mainFolderRight are
  	// put in <span></span>.  When the menu
  	// bar is vertical, they would be put in
  	// a separate TD cell.

  	// HTML code to the left of the folder item
  	mainFolderLeft: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',
  	// HTML code to the right of the folder item
  	mainFolderRight: '<img alt="" src="' + cmThemeJetspeedBase + 'arrowdown.gif">',
	// HTML code to the left of the regular item
	mainItemLeft: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',
	// HTML code to the right of the regular item
	mainItemRight: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',

	// sub menu display attributes

	// HTML code to the left of the folder item
	folderLeft: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',
	// HTML code to the right of the folder item
	folderRight: '<img alt="" src="' + cmThemeJetspeedBase + 'arrow.gif">',
	// HTML code to the left of the regular item
	itemLeft: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',
	// HTML code to the right of the regular item
	itemRight: '<img alt="" src="' + cmThemeJetspeedBase + 'blank.gif">',
	// cell spacing for main menu
	mainSpacing: 0,
	// cell spacing for sub menus
	subSpacing: 0,
	// auto dispear time for submenus in milli-seconds
	delay: 500
};

// for sub menu horizontal split
var cmThemeJetspeedHSplit = [_cmNoAction, '<td colspan="3" style="height: 5px; overflow: hidden"><div class="ThemeJetspeedMenuSplit"></div></td>'];
// for vertical main menu horizontal split
var cmThemeJetspeedMainHSplit = [_cmNoAction, '<td colspan="3" style="height: 5px; overflow: hidden"><div class="ThemeJetspeedMenuSplit"></div></td>'];
// for horizontal main menu vertical split
var cmThemeJetspeedMainVSplit = [_cmNoAction, '|'];
