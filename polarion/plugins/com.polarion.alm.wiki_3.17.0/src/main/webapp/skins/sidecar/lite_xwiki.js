 // threadsafe asynchronous XMLHTTPRequest code
    function executeCommand(url, callback) {
        // we use a javascript feature here called "inner functions"
        // using these means the local variables retain their values after the outer function
        // has returned. this is useful for thread safety, so
        // reassigning the onreadystatechange function doesn't stomp over earlier requests.
        function ajaxBindCallback() {
            if (ajaxRequest.readyState == 4) {
                if (ajaxRequest.status == 200) {
                    if (ajaxCallback) {
                        ajaxCallback(ajaxRequest.responseText);
                    } else {
                        alert('no callback defined');
                    }
                } else {
                     if ( document.getElementById("progressbar") )
                          document.getElementById("progressbar").style.visibility="hidden";

                    if (ajaxCallback) {
                        ajaxCallback(ajaxRequest.responseText);
		    }
                }
            }
        }

        // addMessage(url);
        // use a local variable to hold our request and callback until the inner function is called...
        var ajaxRequest = null;
        var ajaxCallback = callback;
        // bind our callback then hit the server...
        if (window.XMLHttpRequest) {
            // moz et al
            ajaxRequest = new XMLHttpRequest();
            ajaxRequest.onreadystatechange = ajaxBindCallback;
            ajaxRequest.open("GET", url, true);
            ajaxRequest.send(null);
        } else if (window.ActiveXObject) {
            // ie
            ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            if (ajaxRequest) {
                ajaxRequest.onreadystatechange = ajaxBindCallback;
                ajaxRequest.open("GET", url, true);
                ajaxRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
                ajaxRequest.send();
            }
            else{
                alert("your browser does not support xmlhttprequest" )
            }
        }
        else{
            alert("your browser does not support xmlhttprequest" )
        }
    }


    function executeCommandPost(url, callback, param) {
        // we use a javascript feature here called "inner functions"
        // using these means the local variables retain their values after the outer function
        // has returned. this is useful for thread safety, so
        // reassigning the onreadystatechange function doesn't stomp over earlier requests.
        function ajaxBindCallback() {
            if (ajaxRequest.readyState == 4) {
                if (ajaxRequest.status == 200) {
                    if (ajaxCallback) {
                        ajaxCallback(ajaxRequest.responseText);
                    } else {
                        alert('no callback defined');
                    }
                } else {
                     if ( document.getElementById("progressbar") )
                          document.getElementById("progressbar").style.visibility="hidden";

                    if (ajaxCallback) {
                        ajaxCallback(ajaxRequest.responseText);
		    }
                }
            }
        }

        // addMessage(url);
        // use a local variable to hold our request and callback until the inner function is called...
        var ajaxRequest = null;
        var ajaxCallback = callback;

        // bind our callback then hit the server...
        if (window.XMLHttpRequest) {
            // moz et al
            ajaxRequest = new XMLHttpRequest();
            ajaxRequest.onreadystatechange = ajaxBindCallback;
            //ajaxRequest.open("POST", url, true);
            //ajaxRequest.setRequestHeader ("Content-type", "application/x-www-form-urlencoded");
            //ajaxRequest.setRequestHeader ("Content-length", param.length);
            //ajaxRequest.setRequestHeader ("Connection", "close");
            //ajaxRequest.send(param);
            ajaxRequest.open("GET", url+"&"+param, true);
            ajaxRequest.send(null);
        } else if (window.ActiveXObject) {
            // ie
            ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            if (ajaxRequest) {
                ajaxRequest.onreadystatechange = ajaxBindCallback;
                //ajaxRequest.setRequestHeader ("Content-type", "application/x-www-form-urlencoded");
                //ajaxRequest.setRequestHeader ("Content-length", param.length);
                //ajaxRequest.setRequestHeader ("Connection", "close");
                //ajaxRequest.open("POST", url, true);
                //ajaxRequest.send(param);
                  ajaxRequest.open("GET", url+"&"+param, true);
                  ajaxRequest.send();

            }
            else{
                alert("your browser does not support xmlhttprequest" )
            }
        }
        else{
            alert("your browser does not support xmlhttprequest" )
        }
    }

function XWikiEngine() 
{
	this.xpreview = "xpreview"; 
}

   function execAJAX(url, parameters, callback, id) 
   {
      var http_request = false;
      var storeCallback = callback;

      function alertContents() 
      {
        if (http_request.readyState == 4) {
          if (http_request.status == 200) 
          {
              if (storeCallback) 
              {
//alert("Get back: " + http_request.responseText);
                      storeCallback(http_request.responseText, id);
                    } 
                    else 
                    {
                       // alert('no callback defined');
                    }
           
               } 
               else 
               {
                 alert('There was a problem with the request.');
               }
          }
      }

      http_request = false;
      if (window.XMLHttpRequest) { // Mozilla, Safari,...
         http_request = new XMLHttpRequest();
         if (http_request.overrideMimeType) {
         	// set type accordingly to anticipated content type
            //http_request.overrideMimeType('text/xml');
            http_request.overrideMimeType('text/html');
         }
      } else if (window.ActiveXObject) { // IE
         try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
         } catch (e) {
            try {
               http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
         }
      }
      if (!http_request) {
         alert('Cannot create XMLHTTP instance');
         return false;
      }
      
      http_request.onreadystatechange = alertContents;
      http_request.open('POST', url, true);
      http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
      http_request.setRequestHeader("Content-length", parameters.length);
      http_request.setRequestHeader("Connection", "close");
      http_request.send(parameters);
   }


function errorInFunc(s1, s2)
{
  alert ("Error in the function: " + s1 + ".\n Details: " + s2 + ".\n Click OK to continue.\n");
}

function loadWorkItemListCallback(e, obj) {

  var attachEl = document.getElementById(obj);

//alert(attachEl.id);
  var viewcontentWidth = 0;
  var xpreviewWidth = 0;
  var s1 = "loadWorkItemListCallback";

  try
  {
    if($('viewcontent'))
    {
     // viewcontentWidth = parseInt($('viewcontent').getWidth());
//alert($('panelMain').style.width);
      viewcontentWidth = parseInt($('panelMain').style.width);

    }
  }
  catch(ex)
  {errorInFunc(s1, ex);}

  try
  {
    if($('xpreview'))
    {
      xpreviewWidth = parseInt($('xpreview').getWidth());
    }
  }
  catch(ex)
  {errorInFunc(s1, ex);}

  attachEl.innerHTML = e;

  try
  {
    if (e.indexOf("empty_item") >= 0)
    {
      $(obj).previous().style.display = 'none';
      $(obj).style.visibility = 'visible';
      return;
    }    
    if ($(obj).down('table', 1).id)
    {
     var tableId = $(obj).down('table', 1).id;
     var _id = tableId.replace("itemTable_", "");

     var pageRowsId = tableId.replace("itemTable","itemTableRows");
     var pageColsId = tableId.replace("itemTable","itemTableCols");
     var pageHeightId = tableId.replace("itemTable","itemTableHeight");
     var pageWidthId = tableId.replace("itemTable","itemTableWidth");
     var pageSortById = tableId.replace("itemTable","itemTableSortBy");
     var pageClass = tableId.replace("itemTable","itemClass");

    if($(pageHeightId).value.toLowerCase().match('%'))
    {
      var realHeight = parseInt($(pageRowsId).value)*22;
      var tableHeight = $(pageHeightId).value.replace("%","");
      var vr = Math.round(((parseInt(realHeight)*parseInt(tableHeight)/100))/22);
      if(vr<1) vr = 1;
    }
    else
    {
      var vr = Math.round((parseInt($(pageHeightId).value) - 61)/22);
    }

  var sortby = $(pageSortById).value=="" ? 0 : $(pageSortById).value;
  Rico.loadModule('LiveGrid');

  Rico.include('greenHdg.css');
  Rico.onLoad( function() {
  var opts = {  
    menuEvent     : 'none',
    canSortDefault: true,
    canHideDefault: false,
    allowColResize: true,
    canFilterDefault: false,
    windowResize:true,
    prefetchBuffer: true,
    hdrIconsFirst: false,
    headingSort: 'hover',
    sortCol:sortby, 
    offset:0,
    maxPageRows:parseInt($(pageRowsId).value),
    //frozenColumns : parseInt(vr),
    visibleRows: parseInt(vr),
    highlightElem: 'none'
  };

  if(document.all){
//    $(tableId).up('div',1).setStyle({padding:'0px',border:'0px',margin:'-2px -4px 0px 0px'});
//    $(tableId).up('div',2).setStyle({padding:'0px',border:'0px',margin:'0px -2px 0px 0px'});
  }
  else 
  {
//    $(tableId).up('div',1).setStyle({padding:'0px',border:'0px',margin:'-3px -3px 3px 0px'});
  }
//  $(tableId).up('div').setStyle({marginLeft:'0px',marginRight:'4px'});

  
  if($(pageWidthId).value.toLowerCase().match('%')){
	var tWidth = $(pageWidthId).value.replace("%","");
   	var newWidth = (parseInt(viewcontentWidth?viewcontentWidth:xpreviewWidth)-43)*parseInt(tWidth)/100;
	$(tableId).up('div',2).style.width = newWidth + 'px';
	$(tableId).up('div',1).style.width = newWidth + 'px';
	$(tableId).style.width = newWidth - 20 + 'px';
        $(pageWidthId).value = newWidth;
  } else {
	var tWidth = $(pageWidthId).value.replace("%","");
   	var newWidth = parseInt(tWidth) + 'px'; 
        $(tableId).up('div',1).style.width = newWidth;
        $(tableId).up('div',2).style.width = newWidth;
        $(tableId).style.width = newWidth;
  }

  var ex1=new Rico.LiveGrid ($(obj).down('table', 1).id, new Rico.Buffer.Base($(tableId).tBodies[0]), opts);
  $(tableId+'_outerDiv').style.width =  newWidth + 'px';
  $(tableId+'_scrollDiv').style.width = newWidth + 'px';
  $(tableId+'_innerDiv').style.width = parseInt(newWidth) - 19 + 'px';
  $(obj).previous().style.display = 'none';

  $(obj).style.visibility = 'visible';


  if ($(pageClass).value.indexOf("collapsed") >= 0)
  {
     togglePanelbyID(_id);
  }

//mainTab

});

}else
  {
    $(obj).previous().style.display = 'none';
    $(obj).style.visibility = 'visible';
  }
  }
  catch(ex)
  {errorInFunc(s1, ex);}
}

var ex1,buffer1;
function showSpaceIndexTable(e, obj, perc) {
//alert("SpaceIndex");
	var attachEl = document.getElementById(obj);
	attachEl.innerHTML = e;
        var s1 = "showSpaceIndexTable";
	//alert('$(obj).innerHTML');

	try {
		var tableId = $(obj).down('table', 0).id;
		
		var pageRowsId = tableId.replace("itemTable","itemTableRows");
		var pageColsId = tableId.replace("itemTable","itemTableCols");
		var pageHeightId = tableId.replace("itemTable","itemTableHeight");
		var pageWidthId = tableId.replace("itemTable","itemTableWidth");
		var pageSortById = tableId.replace("itemTable","itemTableSortBy");
		var pageClass = tableId.replace("itemTable","itemClass");

		var sortby = 1;
		Rico.loadModule('LiveGrid');
		Rico.loadModule('LiveGridMenu');
		Rico.include('greenHdg.css');
		Rico.onLoad(function() 
                {
                  try
                  {
                    height = $('viewcontent').style.height;				
  		    $(pageHeightId).value = parseInt(height) - 76;

		    if($(pageHeightId).value.toLowerCase().match('%')){
//			var realHeight = $(pageRowsId).value * 22;
//			var tableHeight = $(pageHeightId).value.replace("%","");

//			var vr = Math.round(((parseInt(realHeight)*parseInt(tableHeight)/100))/22);
//			if(vr<1) vr = 1;
		     }
                     else 
                     {
//		        var rowHeight = 25;
 
//                        if (document.all)
//                          rowHeight = 23;
//			var vr = Math.floor(($(pageHeightId).value - 70)/rowHeight);
		    }
//alert(vr);
//				visibleRows: parseInt(vr),
			var opts = {  
				menuEvent     : 'none',
				canSortDefault: true,
				canHideDefault: false,
				allowColResize: true,
				canFilterDefault: false,
				windowResize:true,
				prefetchBuffer: true,
				hdrIconsFirst: false,
				headingSort: 'hover',
				sortCol: sortby, 
				offset:0,
				maxPageRows:parseInt($(pageRowsId).value),
				visibleRows: -1,
				highlightElem: 'none',
				columnSpecs: [{canSort:false,canFilter:false,canHide:false,noResize:true,width:40},,{type:'number',width:72, thouSep: ''}]
			};


                        $(tableId).up('div',0).style.width = '100%';
//alert($(tableId).id);
//if (!document.all)
//    $(tableId).up('div',0).style.border = '1px solid #74A7E1;';

			if (ex1) {
				Event.unloadCache();
				buffer1.clear();
			}
			buffer1=null;
			ex1=null;
			buffer1 = new Rico.Buffer.Base($(tableId).tBodies[0]);
			ex1=new Rico.LiveGrid ($(obj).down('table', 0).id, buffer1, opts);
		 }	
	        catch(ex) 
                {errorInFunc(s1, ex);}

		});

		if($(tableId))
		{
                         $(tableId).style.visibility = "visible";
			//$(tableId).setStyle("visibility: 'visible'");
                
                }                
	}
	catch(ex) 
        {errorInFunc(s1, ex);}
}


function loadWorkItemsList(baseurl, param, obj)
{
    var url = baseurl + "?xpage=xworkitemview&"+param;
    // pbo SC-562
    url = url.replace(/&#91;/gi, '\[');
    url = url.replace(/&#93;/gi, '\]');
    url = url.replace(/#/gi, escape("&#35;"));
    url = url.replace(/&amp;/gi, '&');
    execAJAX(url, "", loadWorkItemListCallback, obj);
}

function loadUsersList(baseurl, param, obj)
{
	var url = baseurl + "?xpage=xuserview&"+param;
    url = url.replace(/&amp;/gi, '&');
	execAJAX(url, "", loadWorkItemListCallback, obj);
}
