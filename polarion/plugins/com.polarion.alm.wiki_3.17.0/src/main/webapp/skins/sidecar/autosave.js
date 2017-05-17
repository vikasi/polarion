var c = 0
var b = 0
var t
var ed
var url

function timedCount()
{
	
	c=c+1
	t=setTimeout("timedCount()",1000)
	
	if ( c == 300 )
	{
		if (  ed == "wysiwyg" )
		{
			tinyMCE.triggerSave();
		}

		document.getElementById('area1').value = document.getElementById('content').value;
		get(document.getElementById('myform'))
		

		clearTimeout(t)
		c = 0
		
		timedCount()

	}	

}

   var http_request = false;
   function makePOSTRequest(url, parameters) {
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
      http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      http_request.setRequestHeader("Content-length", parameters.length);
      http_request.setRequestHeader("Connection", "close");
      http_request.send(parameters);
   }

   function alertContents() {
      if (http_request.readyState == 4) {
         if (http_request.status == 200) {
           
         } else {
            alert('There was a problem with the request.');
         }
      }
   }
   
   function setVars( ed1, url1 )
	{
		ed = ed1;
		url = url1;
	}

   function get(obj) {

      var poststr = "content=" + encodeURI( document.getElementById("content").value );
      makePOSTRequest(url, poststr);
   }