<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=5,IE=9" ><![endif]-->
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="styles/grapheditor.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>">
	<style type="text/css">
		.geSidebarContainer .geTitle { color:#606060; }
		.geSidebar .geItem:hover { opacity:1; }
		.geSidebar .geItem { opacity:0.7; }
		.geSidebarContainer .geTitle input {
			font-size:8pt;
			color:#606060;
		}
		.geBlock {
			z-index: -3;
			margin:100px;
			margin-top:40px;
			margin-bottom:30px;
			padding:20px;
			border-radius:6px;
			-webkit-box-shadow:0px 0px 4px 4px #e5e5e5;
			-moz-box-shadow:0px 0px 4px 4px #e5e5e5;
			box-shadow:0px 0px 4px 4px #e5e5e5;
			_filter:progid:DXImageTransform.Microsoft.DropShadow(OffX=4, OffY=4, Color='#e5e5e5', Positive='true');
			border:1px solid gray;
			background-color:white;
		}
		.geBlock h1, .geBlock h2 {
			margin-top:0px;
			padding-top:0px;
		}
		.geEditor {
			background-color:whiteSmoke;
		}
		.geEditor ::-webkit-scrollbar {
		    width:12px;
		    height:12px;
		}
		.geEditor ::-webkit-scrollbar-track {
			background:whiteSmoke;
			-webkit-box-shadow:inset 0 0 4px rgba(0,0,0,0.1);
		}
		.geEditor ::-webkit-scrollbar-thumb {
			background:#c5c5c5;
		    border-radius:10px;
			border:whiteSmoke solid 3px;
		}
		.geEditor ::-webkit-scrollbar-thumb:hover {
			background:#b5b5b5;
		}
	</style>
	<script>
		/**
		 * Parses URL parameters. Supported parameters are (precedence in given order):
		 * 
		 * - lang=xy: Specifies the language of the user interface
		 * - touch=1: Enables a touch-style user interface
		 * - libs=key1;key2;...;keyN: Specifies the libraries
		 * - picker=0: Disables the Google image picker
		 * - picker=1: Enables Google image picker without image upload
		 * - picker=2: Enables Google image picker with image upload
		 * - clipboard=storage: Implements clipboard via local storage (experimental)
		 * - clipboard=system: Implements clipboard via system clipboard (experimental)
		 * - https=0: Disables/enables SSL
		 * - flash=1: Enables Flash for saving
		 * - url=url: Opens diagram from URL (URL should be encoded)
		 * - analytics=0: Disables Google Analytics
		 * - plugins=0: Do not load Plugins
		 * - gapi=0: Do not load Google APIs
		 * - db=0: Do not load Dropbox APIs
		 * - nerd=1: Includes features for geeks
		 * - mode=google/dropobox/device/browser: Switch to given mode
		 * - rt=1/0: Enable/disable Google Realtime
		 * - embed=1: Runs in embedded mode
		 * --
		 * - dev=1: For developers only
		 * - test=1: For developers only
		 * - drawdev=1: For developers only
		 * - export=URL for export: For developers only
		 */
		var urlParams = (function(url)
		{
			var result = new Object();
			var params = window.location.search.slice(1).split('&');
			
			for (var i = 0; i < params.length; i++)
			{
				idx = params[i].indexOf('=');
				
				if (idx > 0)
				{
					result[params[i].substring(0, idx)] = params[i].substring(idx + 1);
				}
			}
			
			return result;
		})(window.location.href);

		// Checks for local storage and SVG support
		var isSvgBrowser = navigator.userAgent.indexOf('MSIE') < 0 || document.documentMode >= 9;
		var isLocalStorage = typeof(Storage) != 'undefined';

		var t0 = new Date();
	
		// Public global variables
		var MAX_REQUEST_SIZE = 10485760;
		var MAX_AREA = 10000 * 10000;
	
		// CUSTOM_PARAMETERS - URLs for save and export
		var EXPORT_URL = 'http://exp.draw.io/ImageExport3/export';

		var SAVE_URL = 'save';
		var OPEN_URL = 'open';
		var PROXY_URL = 'proxy';

		// Paths and files
		var STENCIL_PATH = 'stencils';
		var SHAPES_PATH = 'shapes';
		var IMAGE_PATH = 'images';
		// Path for images inside the diagram
		var GRAPH_IMAGE_PATH = 'img';
		var ICONFINDER_PATH = 'iconfinder';
		var STYLE_PATH = 'styles';
		var CSS_PATH = 'styles';
		var OPEN_FORM = 'open.html';
		var TEMPLATE_PATH = '/templates';
		
		// Directory for i18 files and basename for main i18n file
		var RESOURCES_PATH = 'resources';
		var RESOURCE_BASE = RESOURCES_PATH + '/dia';
	
		// Specifies connection mode for touch devices (at least one should be true)
		var tapAndHoldStartsConnection = true;
		var showConnectorImg = true;
		
		/**
		 * Synchronously adds scripts to the page.
		 */
		function mxscript(src)
		{
			document.write('<script src="'+src+'?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></scr' + 'ipt>');
		};

		/**
		 * Asynchronously adds scripts to the page.
		 */
		function mxinclude(src)
		{
			var g = document.createElement('script'); g.type = 'text/javascript'; g.async = true; g.src = src;
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(g, s);
		};

		/**
		 * Global function for loading local files via servlet
		 */
		function setCurrentXml(data, filename)
		{
			if (window.openFile != null)
			{
				window.openFile.setData(data, filename);
			}
		};

		/**
		 * Returns the global language
		 */
		function getLanguage() 
		{
			return urlParams['lang'];
		};
		
		// Sets the base path, the UI language via URL param and configures the
		// supported languages to avoid 404s. The loading of all core language
		// resources is disabled as all required resources are in grapheditor.
		// properties. Note that in this example the loading of two resource
		// files (the special bundle and the default bundle) is disabled to
		// save a GET request. This requires that all resources be present in
		// the special bundle.
		var mxLoadResources = false;
		var mxLanguage = getLanguage();
		
		// Add new languages here. First entry is translated to [Automatic]
		// in the menu defintion in Diagramly.js.
		var mxLanguageMap = {};

		var geBasePath = 'js';
		var mxBasePath = 'mxgraph';
		var mxLanguages = [mxLanguage];

		// polarion start
		var FONTS;
		if (urlParams['fonts']) {
	        FONTS = decodeURIComponent(urlParams['fonts']).split(',');
		} else {
		    FONTS = ['Helvetica', 'Verdana', 'Times New Roman', 'Garamond', 'Comic Sans MS', 'Courier New', 'Georgia', 'Lucida Console', 'Tahoma'];
		}
		
        // define indexOf on arrays (IE8 does not have it) 
        if (!Array.indexOf) {
            Array.prototype.indexOf = function(obj) {
                for (var i = 0; i < this.length; i++) {
                    if (this[i] === obj) {
                        return i;
                    }
                }
                return -1;
            }
        }	
		// polarion end
		
		// Used to request grapheditor/mxgraph sources in dev mode
		var mxDevUrl = document.location.protocol + '//devhost.jgraph.com/mxgraph2';

		// Used to request draw.io sources in dev mode
		var drawDevUrl = '';

		if (urlParams['drawdev'] == '1')
		{
			drawDevUrl = document.location.protocol + '//drawhost.jgraph.com/';
		}

		var driveDomain = (urlParams['rt'] != null) ? urlParams['rt'] == '1' :
						(window.location.hostname == 'rt.draw.io' ||
						window.location.hostname == 'drive.draw.io');

		// Customizes export URL
		var ex = urlParams['export'];
		
		if (ex != null)
		{
			EXPORT_URL = ex;
		}

		// Changes paths for local development environment
		if (urlParams['dev'] == '1')
		{
			geBasePath = mxDevUrl + '/javascript/examples/grapheditor/www/js';
			mxBasePath = mxDevUrl + '/javascript/src';
			mxscript(mxBasePath + '/js/mxClient.js');

			// Adds external dependencies
			mxscript(drawDevUrl + 'js/spin/spin.min.js');
			mxscript(drawDevUrl + 'js/deflate/rawdeflate.js');
			mxscript(drawDevUrl + 'js/deflate/rawinflate.js');
			mxscript(drawDevUrl + 'js/deflate/base64.js');
			
			// Adds all JS code that depends on mxClient. This indirection via Devel.js is
			// required in some browsers to make sure mxClient.js (and the files that it
			// loads asynchronously) are available when the code loaded in Devel.js runs.
			mxscript(drawDevUrl + 'js/diagramly/Devel.js');
		}
		else
		{
			mxscript('js/diagramly.min.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>');
		}

		// Loads JSON for older browsers
		if (typeof(JSON) == 'undefined')
		{
			mxscript('js/json/json2.min.js');
		}

		// Adds basic error handling
		window.onerror = function()
		{
			var status = document.getElementById('geStatus');
			
			if (status != null)
			{
				status.innerHTML = 'Page could not be loaded. Please try refreshing.';
			}
			
			var status2 = document.getElementById('geStatus2');
			
			if (status2 != null)
			{
				status2.innerHTML = '';
			}
		};
	</script>
</head>
<body class="geEditor">
<script type="text/javascript">
/**
 * Main
 */
(function()
{
	var ui = new App(new Editor());
	editorUi = ui;

	// Loads and executes the plugins
	var plugins = mxSettings.getPlugins();
	
	if (plugins != null && plugins.length > 0 && urlParams['plugins'] != '0')
	{
		// Global entry point for plugins is Draw.loadPlugin. This is the only
		// long-term supported solution for access to the EditorUi instance.
		window.Draw = new Object();
		window.Draw.loadPlugin = function(callback)
		{
			callback(ui);
		};
		
		if (plugins.length == 1 && plugins[0].indexOf(window.location.protocol + '//' + window.location.host) == 0)
		{
			mxscript(plugins[0]);
		}
		// Loads plugins asynchronously
		else if (mxUtils.confirm(mxResources.get('pluginWarning', [plugins.join('\n')]).replace(/\\n/g, '\n')))
		{
			for (var i = 0; i < plugins.length; i++)
			{
				mxscript(plugins[i]);
			}
		}
	}

	// Color dialog - Do not add to diagramly.min.js due to path issues!
	mxscript('js/jscolor/jscolor.js');
	
	// For developers only
	if (urlParams['test'] == '1' || urlParams['dev'] == '1')
	{
		mxLog.show();
		mxLog.debug('Started in ' + (new Date().getTime() - t0.getTime()) + 'ms');
		mxLog.debug('Export:', EXPORT_URL);
		mxLog.debug('Development mode:', (urlParams['dev'] == '1') ? 'active' : 'inactive');
		mxLog.debug('Test mode:', (urlParams['test'] == '1') ? 'active' : 'inactive');
	}
})();
</script>


<!-- POLARION START -->
<script type="text/javascript">

    mxscript('js/polarion/Sidebar.js');
    
    var editor = editorUi.editor;

    function getDiagramData() {
        stopEditing();
        return mxUtils.getXml(editor.getGraphXml())
    }

    function setDiagramData(xml) {
		if (xml != null) {
            var doc = mxUtils.parseXml(xml);
            editor.setGraphXml(doc.documentElement);

            editor.modified = false;

            editor.undoManager.clear();
        }
    }

    function getDiagramImageProperties() {                 
        stopEditing();

		var graph = editor.graph;
		var bounds = graph.getGraphBounds();
		var scale = graph.view.scale;
		var width = Math.ceil(bounds.width / scale);
		var height = Math.ceil(bounds.height / scale);
	
		var borderSize = 15; // user preference
		var border = Math.max(0, borderSize) + 1;
		var scale = 1;
			
		// Resuable image export instance
		var imgExport = new mxImageExport();
								
		// New image export
		var xmlDoc = mxUtils.createXmlDocument();
		var root = xmlDoc.createElement('output');
		xmlDoc.appendChild(root);
		var xmlCanvas = new mxXmlCanvas2D(root);
			
		// Render graph
		xmlCanvas.scale(scale);
		xmlCanvas.translate(Math.floor(-bounds.x * scale) + border, Math.floor(-bounds.y * scale) + border);
		imgExport.drawState(graph.getView().getState(graph.model.root), xmlCanvas);
	
		// Puts request data together
		var w = Math.ceil(bounds.width * scale) + 2 * border;
		var h = Math.ceil(bounds.height * scale) + 2 * border;
		var bg = graph.background || '#ffffff';
		var xml = mxUtils.getXml(root);
		
		var properties = new Object();
		properties.width = w + '';	
		properties.height = h + '';	
		properties.backgroundColor = bg + '';
		properties.graphImageXml = xml;
		return properties;
    }
    
	function isModified() {
    	return editor.modified;
	}
   
	// private 
	function stopEditing() {
		editor.graph.stopEditing(false);
	}

	function getStateData() {
		var stateData = new Object();
		stateData.expandedPalettes = editorUi.sidebar.getExpandedPalettes();
		stateData.toggledPalettesPrefixes = editorUi.sidebar.getToggledPalettesPrefixes();
	    return stateData;
	}
	
	function setStateData(stateData) {
	    editorUi.sidebar.setExpandedPalettes(stateData.expandedPalettes);
	    editorUi.sidebar.setToggledPalettesPrefixes(stateData.toggledPalettesPrefixes);
	}
    
</script>
<!-- POLARION END -->

</body>
</html>
