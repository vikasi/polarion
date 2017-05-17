dojo.provide("dojo.jetspeed.JetspeedAjax");

dojo.require("dojo.io.*");
dojo.require("dojo.xml.Parse");

dojo.jetspeed.getFragments = function(list)
{
	var kw = 
	{
		url: "http://localhost:8080/jetspeed/ajaxapi",
		transport: "XMLHTTPTransport",
		mimetype: "text/xml",
		load: function(type, js, http) 
		{
//			dojo.debug("http:", http);
//			dojo.debug("response:", http.responseText);
//			dojo.debug("state:", http.readyState);

            dom = http.responseXML.documentElement;
            fragments = dom.getElementsByTagName('fragment');
            for(i=0; i<fragments.length; i++)
	        {          	        
			  name = fragments[i].attributes[0].value;
	          list.options[i] = new Option(name, name);
 	        }
		}
	};
	dojo.io.bind(kw);

}
