/**
 * Browser independent XMLHttpRequestLoader
 */
function XMLHttpRequestCaller(url, caller)
{    
  this.url = url;
  this.caller = caller;

	this.serviceRequest = function()
	{
	  // branch for native XMLHttpRequest object
	  var _caller = this.caller;
	  var _url = this.url;
	  
      xmlRequest = getHTTPObject();
      xmlRequest.onreadystatechange = function()
      {
         if (xmlRequest.readyState == 4)
         {
          // only if "OK"
          if (xmlRequest.status == 200)
          {
             _caller.invoke(xmlRequest);
          }
          else
          {
            alert("There was a problem retrieving the XML data:\n" + xmlRequest.statusText + 
            " for url: \n" + _url);
          }
        }
	  };
	      
      xmlRequest.open("GET", _url, true);
	  xmlRequest.send(null);	
	}
}


function PortletAppLoader(elementId)
{

  
  this.invoke = function(xmlRequest)
  {
   
     response  = xmlRequest.responseXML.documentElement;
     portletApps = response.getElementsByTagName('portletApp');
     list = document.getElementById(elementId);
     for(i=0; i<portletApps.length; i++)
     {         
        name  = new EZDom(portletApps[i]).getChildValue('name');
        list.options[i] = new Option(name, name);
     }
  }
  
  var requestCaller = new XMLHttpRequestCaller(applicationRoot+"/ajax/portlet_apps.ajax?ajax_service=portletRegistry.getPortletApplications" ,this); 
  requestCaller.serviceRequest();
}

function PortletDefinitionLoader(elementId)
{ 

  this.appName;
   
  this.invoke = function(xmlRequest)
  {
   
     response  = xmlRequest.responseXML.documentElement;
     portletDefs = response.getElementsByTagName('portletDefinition');
     list = document.getElementById(elementId);
     for(i=0; i<portletDefs.length; i++)
     {         
        name  = new EZDom(portletDefs[i]).getChildValue('name');
        list.options[i] = new Option(name, this.appName+"::"+name);
     }
  }
  
  this.load = function(appName)
  {
     this.appName = appName;
     var requestCaller = new XMLHttpRequestCaller(applicationRoot+"/ajax/portlet_definitions.ajax?ajax_service=portletRegistry.getPortletApplication&ajax_param_0_str="+appName ,this); 
     requestCaller.serviceRequest();
  }
}

function PortletEntityLoader(elementId, blockId)
{
   
  this.invoke = function(xmlRequest)
  {   
     response  = xmlRequest.responseXML.documentElement;
     portletEntities = response.getElementsByTagName('portletEntity');
     list = document.getElementById(elementId);
     hasEntities=false;
     for(i=0; i<portletEntities.length; i++)
     {         
        id  = new EZDom(portletEntities[i]).getChildValue('id');
        list.options[i] = new Option(id, id);
        hasEntities = true;
     }
     
      if(hasEntities && blockId)
      {
        show(blockId);
      }
      else if( blockId )
      {
        hide(blockId);
      }
  }
  
  this.load = function(portletName)
  {
      var requestCaller = new XMLHttpRequestCaller(applicationRoot+"/ajax/portlet_entities.ajax?ajax_service=entityAccess.getPortletEntities&ajax_param_0_str="+portletName ,this); 
      requestCaller.serviceRequest();
  }
}

function InitEntityEditor(nameId, prefListId, labelId)
{
   this.nameId = nameId;
   this.prefListId = prefListId;
   this.labelId = labelId;
   
  this.invoke = function(xmlRequest)
  {   
     var portletEntity  = new PortletEntity(xmlRequest.responseXML.documentElement);
     var nameElem = getElement(this.nameId);
     nameElem.value = portletEntity.getId();
     var list = getElement(this.prefListId);
     var prefs = portletEntity.getPreferences();
     var indent = String.fromCharCode(160, 160, 160, 160);
     var cIndex = 0;
     clearList(list);
     for(var i=0; i<prefs.length; i++)
     {
        var pref = prefs[i];        
        
        list.options[cIndex] = new Option(pref.getName(), "pref::"+pref.getName());
        
        cIndex++;
        list.options[cIndex] = new Option(indent+" ------------------------- Values for "+pref.getName()+" -------------------------");
        
        var values = pref.getValues();
        for(j=0; j<values.length; j++)
        {
          ++cIndex;
          list.options[cIndex] = new Option(indent+values[j], "prefvalue::"+pref.getName()+"::"+j+"::"+values[j]);
        }
        
        ++cIndex;
        
     }     
     
     setHTML(this.labelId, portletEntity.getId());     
  }
  
  this.load = function(entityName)
  {
      var requestCaller = new XMLHttpRequestCaller(applicationRoot+"/ajax/portlet_entity.ajax?ajax_service=entityAccess.getPortletEntity&ajax_param_0_str="+entityName,this); 
      requestCaller.serviceRequest();
  }


}

function EZDom(anElement)
{
  this.anElement = anElement;
  
  this.getChildValue = function(name)
  {
    return this.anElement.getElementsByTagName(name)[0].firstChild.data;
  }
}

function PortletEntity(element)
{
  this.parentClass = EZDom;
  this.parentClass(element);
  this.prefs = new Array();
  
  var prefElements = element.getElementsByTagName('preference');
  for(i=0; i<prefElements.length; i++)
  {
     this.prefs[i] = new Preference(prefElements[i]);
  }  
  
  this.getId = function()
  {
     return this.getChildValue('id');
  }
  
  this.getPreferences = function()
  {
     return this.prefs;
  }
}

function Preference(element)
{
  this.parentClass = EZDom;
  this.parentClass(element);
  
  this.getName = function()
  {
    return this.getChildValue('name');
  }
  
  this.getValues = function()
  {
     var valueElements = this.anElement.getElementsByTagName('value');
     var values = new Array(valueElements.length);
     for(i=0; i<valueElements.length; i++)
     {
       values[i] = valueElements[i].firstChild.data;
     }
     return values;
  }    
}

  
  
function clearList(selectList)
{
   for(i=0; i<selectList.length; i++)
   {
     selectList.options[i] = null;     
   }
}

function hide(elementIds)
{
   var elementArrayId = elementIds.split(",");
   for(i=0; i<elementArrayId.length; i++)
   {
       getElement(elementArrayId[i]).style.display="none";
   }
}

function show(elementIds)
{
   var elementArrayId = elementIds.split(",");
   for(i=0; i<elementArrayId.length; i++)
   {
       getElement(elementArrayId[i]).style.display="inline";
   }
}

function getElement(eid)
{
   return document.getElementById(eid);
}

function setHTML(eid, htmlValue)
{
  getElement(eid).innerHTML = htmlValue;
}

function enableIfComplete(valuesToCheck, elementsToShow)
{
  var checkArray = valuesToCheck.split(",");
  var ok = false;
  for(i=0; i<checkArray.length; i++)
  {
     var checkValue = getElement(checkArray[i]).value;
     if(checkValue.length > 0  && isNotWhiteSpace(checkValue))
     {
        ok = true;
     }
     else
     {
        ok = false;
        break;
     }
     
  }
  
  var enableArray = elementsToShow.split(",");
  for(i=0; i<enableArray.length; i++)
  {
     if(ok)
     {
     	getElement(enableArray[i]).style.display="inline";
     }
     else
     {
        getElement(enableArray[i]).style.display="none";
     }
  }
}

function isNotWhiteSpace(value)
{
   var regex = /^\s*$/i;
   return !regex.test(value);
}

function trim(value)
{
  return value.replace(/^(\s+)?(.*\S)(\s+)?$/, '$2')
}


/** Cross browser XMLHttpObject creator */
function getHTTPObject() 
{
  var xmlhttp;
  /*@cc_on
  @if (@_jscript_version >= 5)
    try 
    {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) 
    {
      try 
      {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
      } 
      catch (E) 
      {
        xmlhttp = false;
      }
    }
  @else
  xmlhttp = false;
  @end @*/
  if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
    try 
    {
      xmlhttp = new XMLHttpRequest();
    } 
    catch (e) 
    {
      xmlhttp = false;
    }
  }
  return xmlhttp;
}


