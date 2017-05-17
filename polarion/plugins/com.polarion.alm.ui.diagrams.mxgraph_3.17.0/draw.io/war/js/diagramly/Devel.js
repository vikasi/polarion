/*
 * $Id: Devel.js,v 1.27 2014/01/08 16:38:06 gaudenz Exp $
 * Copyright (c) 2006-2013, JGraph Ltd
 */
// This provides an indirection to make sure the mxClient.js
// loads before the dependent classes below are loaded. This
// is used for development mode where the JS is in separate
// files and the mxClient.js loads other files.

// Uses grapheditor from devhost
mxscript(geBasePath +'/Editor.js');
mxscript(geBasePath +'/Graph.js');
mxscript(geBasePath +'/Shapes.js');
mxscript(geBasePath +'/EditorUi.js');
mxscript(geBasePath +'/Actions.js');
mxscript(geBasePath +'/Menus.js');
mxscript(geBasePath +'/Sidebar.js');
mxscript(geBasePath +'/Toolbar.js');
mxscript(geBasePath +'/Dialogs.js');

// Loads main class
mxscript(drawDevUrl + 'js/diagramly/User.js');
mxscript(drawDevUrl + 'js/diagramly/File.js');
mxscript(drawDevUrl + 'js/diagramly/LocalFile.js');
mxscript(drawDevUrl + 'js/diagramly/StorageFile.js');
mxscript(drawDevUrl + 'js/diagramly/DriveRealtime.js');
mxscript(drawDevUrl + 'js/diagramly/DriveFile.js');
mxscript(drawDevUrl + 'js/diagramly/DriveClient.js');
mxscript(drawDevUrl + 'js/diagramly/DropboxFile.js');
mxscript(drawDevUrl + 'js/diagramly/DropboxClient.js');
mxscript(drawDevUrl + 'js/diagramly/App.js');
mxscript(drawDevUrl + 'js/diagramly/Dialogs.js');
mxscript(drawDevUrl + 'js/diagramly/Sidebar.js');
mxscript(drawDevUrl + 'js/diagramly/EditorUi.js');
mxscript(drawDevUrl + 'js/diagramly/ChatWindow.js');
mxscript(drawDevUrl + 'js/diagramly/Menus.js');
mxscript(drawDevUrl + 'js/diagramly/Settings.js');
