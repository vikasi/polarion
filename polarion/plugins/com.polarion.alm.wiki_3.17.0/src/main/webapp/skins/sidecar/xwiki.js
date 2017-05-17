function hideForm(form){
  form.getElementsByTagName("fieldset").item(0).className = "collapsed";
}

function toggleForm(form){

  //var fieldset = document.getElementsByTagName("fieldset").item(0);
  var fieldset = document.getElementById("commentform");

  if(fieldset.className == "collapsed"){
    fieldset.className = "expanded";
  }
  else{
    fieldset.className = "collapsed";
  }

}

var attCount = 0;
var tableCurrentCount = 0;
var tableAddTr = 0;
var editAttach = false;
var attachDelete = new Array();
var countNewAttach = 0;
var runItem = new Array();
var popIsExpiredSession = false;

function showJumpWorkItem(path)
{
	opt = document.getElementById('workItemsList');
	if (opt)
	{
		var selectedindex = opt.selectedIndex;
        var selectedItem = opt.options[selectedindex].value;
		s = path + "?id=" + selectedItem;
		window.open(s);
	}
}

function deleteAttachRow(id, inpId)
{
  var attTab = document.getElementById('attTable');
  var attRow = attTab.getElementsByTagName('tr');

  for (var i=0; i<attRow.length; i++)
  {
     var attInp = attRow[i].getElementsByTagName('input');
     if (attInp.length>0)
     {

         if (attInp[0].name == inpId && attRow.length != i+1)
         {
            attTab.deleteRow(i);
            tableAddTr = tableAddTr -1;
         } 
      }
  }

  if ( tableAddTr == tableCurrentCount )
  {
    var fieldsetEdit = document.getElementById("attEdit");
    var fieldsetSave = document.getElementById("attSave");
    var fieldsetCancel = document.getElementById("attCancel");

    fieldsetEdit.style.display = "inline";
    fieldsetSave.style.display = "none";
    fieldsetCancel.style.display = "none";

    if (tableCurrentCount==2)
    {
      var divTable = document.getElementById("attTableM");
      divTable.style.display ="none";
      var fieldsetNo = document.getElementById("attNo");
      fieldsetNo.style.display = "inline";
    }

  }
}

function hideAttachForm(form)
{
  var up = document.getElementById('attTable');
  var arrRow = up.getElementsByTagName('tr');
  var nowRow =  arrRow.length;
  var delCount= nowRow - tableCurrentCount;

  if (delCount >0)
  {
     for (var i=nowRow; i>tableCurrentCount; i--)
     {
       up.deleteRow(i-1);
       if ( attchs.length > (tableCurrentCount-2))
       	attchs.pop();
     }   
  }

  var fieldsetEdit = document.getElementById("attEdit");
  var fieldsetSave = document.getElementById("attSave");
  var fieldsetCancel = document.getElementById("attCancel");

    fieldsetEdit.style.display = "inline";
    fieldsetSave.style.display = "none";
    fieldsetCancel.style.display = "none";

    if (tableCurrentCount==2)
    {
      var divTable = document.getElementById("attTableM");
      divTable.style.display ="none";
      var fieldsetNo = document.getElementById("attNo");
      fieldsetNo.style.display = "inline";
    }
}


function toggleAttachForm(form, img, img2, msg, msg2)
{
  
  var fieldsetEdit = document.getElementById("attEdit");
  var fieldsetSave = document.getElementById("attSave");
  var fieldsetCancel = document.getElementById("attCancel");
  var fieldsetNo = document.getElementById("attNo");

  var divTable = document.getElementById("attTableM");


  var up = document.getElementById('attTable');
  var arrRow = up.getElementsByTagName('tr');
  tableCurrentCount= arrRow.length;
  tableAddTr = arrRow.length;


  if(fieldsetEdit.style.display == "none")
  {
    fieldsetEdit.style.display = "inline";
    fieldsetSave.style.display = "none";
    fieldsetCancel.style.display = "none";
  }
  else{
    attCount = 0;
    addAttach(img, img2, msg, msg2);
    fieldsetEdit.style.display = "none";
    fieldsetSave.style.display = "inline";
    fieldsetCancel.style.display = "inline";
    if (fieldsetNo)
	    fieldsetNo.style.display = "none";
    divTable.style.display ="inline";
  }

}

var attchs = new Array();
var attchsSaved = new Array();


function getFileName(path)
{
   ps = path.lastIndexOf('\\');

   if (ps > -1)
     {return path.substring(ps + 1, ps + (path.length - ps));}
   else 
     {return path;}
}




function validateNameAttach(val, text)
{

   var res = (val).indexOf('%') > 0;
//alert("Validate: " + val + " " + res);                   
    if (res)
    {
      alert(text);
      return false;
    }
    else
    {    
      return true;
    } 
}

function addAttachToListSaved(path)
{
  attchsSaved.push(path);
}

function addAttachToList(path)
{
  attchs.push(path);
//  alert("Added: " +  attchs[attchs.length-1]);
}

function isAttachExist(path)
{
  for (i = 0; i < attchs.length; i++)
  { 
    if (attchs[i] == path) 
    {
      return i;
    }
  }
  return -1;
}

function deleteAttachCheck(name)
{
  for (i = 0; i < attchs.length; i++)
  { 
    if (attchs[i] == name) 
    {
      attchs[i]= "";
      return;
    }
  }
  return;
}


function deleteAttachRow2(id, inpId)
{ 
  var elem = document.getElementById("filepathMult" + (inpId-1));
  if (elem)
  {
    var path = getFileName(elem.value);
    deleteAttachCheck(path);
  }

  var attTab = document.getElementById('attTable');
  var attRow = attTab.getElementsByTagName('tr');

  for (var i=0; i<attRow.length; i++)
  {
     var attInp = attRow[i].getElementsByTagName('span');
     if (attInp.length>0)
     {

         if (attInp[0].id == id /*&& attRow.length != i+1*/)
         {
            attTab.deleteRow(i);
            tableAddTr = tableAddTr -1;
         } 
      }
  }
  countNewAttach -=1;
  attachChange();
}


function addAttach(img, img2, msg,msg2)
{

var row_id = "sp_"+(attCount-1);
var row_doc = document.getElementById(row_id);
if (row_doc)
{
  row_doc.innerHTML = "<a href=\"#\" onclick=\"if (checkBeforeDelete('"+tableAddTr+"')) deleteAttachRow2('sp_"+(attCount-1)+"',"+attCount+")\"><img src=\""+img2+"\"/></a>"
}

var newRow = document.getElementById('attTable').insertRow(tableAddTr);
newRow.insertCell(0).innerHTML = "<input class=\"browseinput\"; style=\"width: 100px;\" type=\"text\" value=\"\" size=\"20\" id=\"fileTitle"+attCount+"\"  name=\"fileTitle"+attCount+"\"/>"; 
newRow.insertCell(1).innerHTML = "<input class=\"browseinput\"; style=\"width: 200px;\" id=\"filepathMult"+attCount+"\" type=\"file\" name=\"filepathMult"+attCount+"\" value=\"\" size=\"18\" onChange=\"attachChangeEditAttach();\" />"; 
newRow.insertCell(2).innerHTML = "";
var cell3 = newRow.insertCell(3);
cell3.innerHTML = "<input id=\"exst_"+attCount+"\"  name=\"exst_"+attCount+"\" type=\"checkbox\" value=\"1_1\" ><b class=\"attachUpd\">"+msg2+"</b>";
//newRow.insertCell(4).innerHTML = "";
cell3.colSpan = "2";
newRow.insertCell(4).innerHTML = "<span id=\"sp_"+attCount+"\"><a href=\"#\" onclick=\"if (checkCurrentAtt("+(attCount)+",'"+img+"','"+msg+"')) addAttach('"+img+"','"+img2+"','"+ msg+"','"+msg2+"');\"><img src=\""+img+"\"/></a></span>";

attCount +=1;
tableAddTr +=1;

}

function plusTableRow()
{
 tableAddTr +=1;	
}

function checkBeforeDelete(num)
{
 return true;
}

function checkCurrentAtt(num, img, msg)
{
  if ( !checkAttach(num, msg) )
        return false;

  //return true;
  var id = "filepathMult"+num;
  var sp_id = "sp_"+num;
  var title_id = "fileTitle"+num;

   
  var fieldsetNew1 = document.getElementById(id);
  var fieldsetNewSp = document.getElementById(sp_id);
  var titleElement = document.getElementById(title_id).value;
  var file = fieldsetNew1.value;


  if (fieldsetNew1.value != "" )
  {
        fieldsetNewSp.innerHTML = "<img src=\""+img+"\"/>";
        //textAdded();
        attachChange();
   	return true;
  }
  else
  {
  	alert(msg);
  	return false;
  }

}

function togglePortletVisibility(portletName, img1, img2)
{
//alert(portletName);
  var spliterName = "panelHorResizePanels." + portletName;
//alert(spliterName);
  var contentName = "content" + portletName;
  var imgName = "imgTitle" + portletName;
  var splitter = splitters["Panels." + portletName];
//alert(splitters["Panels." + portletName]);
//alert($(imgName).src);
//alert(img2);
  if ($(imgName).src.indexOf(img2) > 0)
  {
    var portletContent = $(contentName);
    if (splitter)
    {
    	splitter.height = portletContent.style.height;
   	$(spliterName).style.top = - (parseInt(portletContent.style.height) - 1) + "px";
      	splitter.drag.onEnd();
    }
    else
    {   //no splitter for last portlet
        var spltName = "panelHorResizePanels." + beforLastPanelName;
        var name = "Panels."+ beforLastPanelName;
        splitter = splitters[name];
//alert(spltName) 
        var beforeLastSpliter = $(spltName);
//alert(spltName);
        splitter.height = parseInt($(contentName).style.height) - 24 + "px";
//alert(beforeLastSpliter);
        beforeLastSpliter.style.top = splitter.height;      
        splitter.drag.onEnd();
    }
    $(imgName).src = img1;
  }
  else
  {
//alert("Max");
    $(imgName).src = img2;
    var portletContent = $(contentName);
    if (splitter)
    {
       $(spliterName).style.top = splitter.height;
    }
    else
    {
       var spltName = "panelHorResizePanels." + beforLastPanelName;
       var name = "Panels."+ beforLastPanelName;
       splitter = splitters[name];
//alert(splitters[name].height);
       $(spltName).style.top = "-" + splitter.height;
    }
    splitter.drag.onEnd();

  }
}

var splitters = new Array();
var beforLastPanelName = "";

function togglePanelVisibilityByID(cont, btn)
{
  element = $(cont);
  button = $(btn);

  if (element.style.display == "none")
  {
    element.style.display = "block"
    if (button)
    {
      button.className = "btn_min";
      button.title = msgMinimize;
    }
  }
  else
  {
    element.style.display = "none";
    if (button)
    {
      button.className = "btn_max";
      button.title = msgMaximize;
    }
  }                             
}

function togglePanelbyID(id)
{
  var s1 = "wi_panel_" + id;
  var s2 = "wi_but_"  + id;
  element = $(s1);
  button = $(s2);

  if (element.style.display == "none")
  {
    element.style.display = "block"
    if (button)
      button.className = "btn_min";
  }
  else
  {
    element.style.display = "none";
    if (button)
      button.className = "btn_max";
  }                             
}

function togglePanelVisibility(element){
  if(element.className.indexOf("expanded") >= 0){
    element.className = element.className.replace('expanded', 'collapsed');
  }
  else{
    element.className = element.className.replace('collapsed', 'expanded');    
  }
}

function togglePanelVisibilityExpand(element, element2){
  if(element.className.indexOf("expanded") >= 0){
    element.className = element.className.replace('expanded', 'collapsed');
    element2.className = "expand_close";
  }
  else{
    element.className = element.className.replace('collapsed', 'expanded');
     element2.className = "expand_open";
  }
}

function getForthParent(element) {
   return element.parentNode.parentNode.parentNode.parentNode;
} 

function expandPanel(uniqueId){
    var element = document.getElementById(uniqueId);
    var button = document.getElementById(uniqueId+"_button");
    
    element.className = "expandedPanel";
    getForthParent(element).className = "workitemsTablePanel";
    button.style.display="none";
}

function switchQueryContainerDivVisibility(uniqueId){
    var element = document.getElementById(uniqueId);
    element.style.display = (element.style.display == "none") ? "block" :"none";
}


function assignAjaxResult(elementsName, result){

	var elements = document.getElementsByName(elementsName);
	var i = 0; 

	for (i=0; i < elements.length; i++){
		elements.item(i).innerHTML=result;
	};
}
	    

function togglePanelVisibilityStyle2(element, expandedStyle, collapsedStyle, element2, expandedStyle2, collapsedStyle2){
  if(element.className.indexOf(expandedStyle) >= 0){
    element.className = element.className.replace(expandedStyle, collapsedStyle);
    element2.className = element2.className.replace(expandedStyle2, collapsedStyle2);
  }
  else{
    element.className = element.className.replace(collapsedStyle, expandedStyle);
    element2.className = element2.className.replace(collapsedStyle2, expandedStyle2);
  }
}

var isStart = null;

function startmenu(element){
  isStart = element;
  showsubmenu(element);
}

var currentMenu = null;

function hidePreviousMenu(element)
{
 if ( currentMenu != null /*&& element != currentMenu*/)
 {
    currentMenu.lastChild.className = currentMenu.lastChild.className.replace("visible", "hidden");
 }
}

function showsubmenu(element){

if (isStart != null)
{

    if(element.lastChild.tagName.toLowerCase() == "span")
    {
      hidePreviousMenu(element);
      
    if(window.hidetimer)
    {
      if(window.hideelement == element.lastChild)
      {
        clearTimeout(window.hidetimer);
        window.hidetimer = null;
        window.hideelement = null;
      }
      else
      {
        doHide();
      }
    }

    /*if(element.lastChild.style.display == "block") return;*/
    if(window.ActiveXObject)
    {
       element.lastChild.style.left = (computeAbsoluteLeft(element)  - 9) + "px";
    }else
    {
       element.lastChild.style.left = (computeAbsoluteLeft(element)  - 9) + "px";
    }
    element.lastChild.style.top = computeAbsoluteTop(element)   + "px";

    if (element.lastChild.className.indexOf("hidden") != -1 )
        element.lastChild.className = element.lastChild.className.replace("hidden", "visible");
    else if (element.lastChild.className.indexOf("visible") != -1 )
        element.lastChild.className = element.lastChild.className.replace("visible", "hidden");
    else
	element.lastChild.className = element.lastChild.className.replace("hidden", "visible");        	

    currentMenu	= element;
  }

}
}
function computeAbsoluteLeft(element) {
  if(window.ActiveXObject){
     return element.parentNode.offsetLeft;	
  }
  return element.parentNode.offsetLeft;
  
}
function computeAbsoluteTop(element) {
  if(window.ActiveXObject){
    return element.offsetHeight;
  }
  return element.offsetTop + element.offsetHeight;
}
function hidesubmenu(element){
  if(element.lastChild.tagName.toLowerCase() == "span"){
    window.hideelement = element.lastChild;
    window.hidetimer = setTimeout(doHide, 100);
  }
}

function setDirty(dirty){
  if(dirty === undefined){
    dirty = true;
  }
  window.docdirty = dirty;
}

function doHide(){
  window.hideelement.className = window.hideelement.className.replace("visible", "hidden");
  clearTimeout(window.hidetimer);
  window.hidetimer = null;
  window.hideelement = null;
  isStart = null;
}

function updateAttachName(form, msg) {
  var fname = form.filepath.value;

  if (fname == "") {
    return false;
  }

  var i = fname.lastIndexOf('\\');
  if (i == -1){
    i = fname.lastIndexOf('/');
  }

  fname = fname.substring(i + 1);
  if (form.filename.value == fname){
    return true;
  }

  if (form.filename.value == ""){
    form.filename.value = fname;
  }
  else {
    if (confirm(msg + " '" + fname + "' ?")){
      form.filename.value = fname;
    }
  }
  return true;
}

function isAttachExistSaved(path)
{
  for (i = 0; i < attchsSaved.length; i++)
  { 
    if (attchsSaved[i] == path) 
    {
      return i;
    }
  }
  return -1;
}




function updateAttachNameMult(form, msg, error, msg2) {
  var inp = form.getElementsByTagName('input');
  var inpCount = inp.length;

  var arrPath = new Array();
  var arrNames = new Array();
  var resultNames = "";

  if (inpCount < 3)
  {
  	return false;
  }

  for (var i=0; i<inp.length; i++)
  {

    if (inp[i].value != "" && (inp[i].id).indexOf("exst_") < 0 && (  (inp[i].id).indexOf("filepathMult") >= 0 || (inp[i].id).indexOf("fileTitle") >= 0 )  )
    {
       var valid = checkAttachmentsBeforeSave(inp[i].id, msg2);
       if ( valid == 2 ) 
       {
         arrPath.push(inp[i].name);
         resultNames += inp[i].name+";"
       }
       else if ( valid == 1 ) 
       {
       		return false;
       }
    }

  }

  form.filepathMult.value = resultNames;
  //alert(resultNames);

   var namesDel = "";
   for(i=0; i<attachDelete.length; i++ )
   {
     	 if (attachDelete[i] != null && attachDelete[i] != "")
     	     namesDel +=attachDelete[i]+";";
   }

  form.fileNamesDelete.value = namesDel;
  return true;
}

function updateAttachNameMult2(form, msg, error) {
  var inp = form.getElementsByTagName('input');
  //alert(inp.length);
  var inpCount = inp.length;

  var arrPath = new Array();
  var arrNames = new Array();
  var resultNames = "";

  if (inpCount < 3)
  {
  	return false;
  }

  for (var i=0; i<inp.length; i++)
  {

    if (inp[i].value != "" && (inp[i].id).indexOf("exst_") < 0  && (  (inp[i].id).indexOf("filepathMult") >= 0 || (inp[i].id).indexOf("fileTitle") >= 0 ) )
    {
       arrPath.push(inp[i].name);
       resultNames += inp[i].name+";"
    }

  }

  document.getElementById("filepathMult").value = resultNames;
  //alert(resultNames);

   var namesDel = "";
   for(i=0; i<attachDelete.length; i++ )
   {
     	 if (attachDelete[i] != null && attachDelete[i] != "")
     	     namesDel +=attachDelete[i]+";";
   }

  form.fileNamesDelete.value = namesDel;


  return true;
}


function toggleClass(o, className){
  if(!isClassExist(o,className)) {
    o.className += ' ' + className
  }
  else {
    rmClass(o, className);
  }
}
function addClass(o, className){
  if(!isClassExist(o,className))
    o.className += ' ' + className
}

function isClassExist(o,className){
    if(!o.className)
      return false;
    return new RegExp('\\b' + className + '\\b').test(o.className)
}

function rmClass(o, className){
  o.className = o.className.replace(new RegExp('\\s*\\b' + className + '\\b'),'')
}

function openURL(url) {
  win = open( url, "win", "titlebar=0,width=750,height=480,resizable,scrollbars");
  if( win ) {
    win.focus();
  }
}

function openHelp() {
  win = open( "http://www.xwiki.com/xwiki/bin/view/Doc/XWikiSyntax?xpage=plain", "syntax", "titlebar=0,width=750,height=480,resizable,scrollbars");
  if( win ) {
    win.focus();
  }
}

var XWiki = {
  Version: '0.8_pre1',
  require: function(libraryName) {
    // inserting via DOM fails in Safari 2.0, so brute force approach
    document.write('<script type="text/javascript" src="'+libraryName+'"></script>');
  },
  addLibrary: function(scriptLibraryName) {
    JSfileName = 'xwiki.js'; // This should be added in a xwiki.js file 'xwiki.js'
    if(scriptLibraryName=='scriptaculous') {
	libraries = ['util.js', 'effects.js', 'dragdrop.js', 'controls.js'];
    }
    if(scriptLibraryName=='rico') {
	libraries = ['rico.js'];
    }
    var scriptTags = document.getElementsByTagName("script");
    for(var i=0;i<scriptTags.length;i++) {
      if(scriptTags[i].src && scriptTags[i].src.match(JSfileName)) {
        var path = scriptTags[i].src.replace(JSfileName,scriptLibraryName) + '/';
	  for (var j=0;j<libraries.length;j++) {
	    this.require (path + libraries[j]);
	  }
      }
    }
  }
};

var XWikiAjax = {
  requests: new Array(),
  start: function(status) {
    this.status = $(status);
    ajaxEngine.registerRequest ('setValue', 'SetValueResponse?xpage=rdf');
    ajaxEngine.registerAjaxElement (this.status.id);
  },
  addRequest: function(dName, cName, field, value) {
    var request = Object.extend({
      type: 'set'
    });
    request.className = cName;
    request.document = dName;
    request.field = field;
    request.value = value;
    this.requests.push(request);
  },
  end: function() {
    this.requests.reverse();
    var req = this.requests.pop();
    while (req) {
      if (req.type=='set') {
        ajaxEngine.sendRequest ('setValue', 'status='+this.status.id, 'doc='+req.document, 
	    'typedoc='+req.className, 'field='+req.field, 'value='+req.value);
      };
	req = this.requests.pop();
    }
    this.status.innerHtml = 'updated';
  }
};

function updateName(field1, field2, removeclass) {
  var name = field1.value;
  name = noaccent(name);
  if (removeclass!=false) {
    name = name.replace(/class$/gi,"");
  }
  if (field2 == null) {
    field1.value = name;
  } else {
    field2.value = name;
  }
  if (name=="")
  return false;
  return true;
}

function prepareName(form) {
  var fname = form.register_first_name.value;
  var lname = form.register_last_name.value;
  var cxwikiname = form.xwikiname;
  if (fname != "") {
    fname = fname.substring(0,1).toUpperCase() + fname.substring(1);
    fname.replace(/ /g,"");
  }
  if (lname != "") {
    lname = lname.substring(0,1).toUpperCase() + lname.substring(1)
    lname.replace(/ /g,"");
  }
  if (cxwikiname.value == "") {
    cxwikiname.value = noaccent(fname + lname);
  }
}

var rightCanScroll = true;
var leftCanScroll = true;

function scrollL()
{
 var leftDiv = document.getElementById('viewcontentL');
 var rightDiv = document.getElementById('viewcontentR');

     if(rightCanScroll){      
 
       leftCanScroll = false;
 
       rightDiv.scrollTop  = leftDiv.scrollTop;
       rightDiv.scrollLeft  = leftDiv.scrollLeft;
 
       leftCanScroll = true;
      }
      return false;
}

function scrollR()
{

 var leftDiv = document.getElementById('viewcontentL');
 var rightDiv = document.getElementById('viewcontentR');


     if(leftCanScroll){
 
      rightCanScroll = false;
 
      leftDiv.scrollTop =    rightDiv.scrollTop;
 
      leftDiv.scrollLeft =    rightDiv.scrollLeft;
 
      rightCanScroll = true;
 
     }
     return false;

}


function scrollCL()
{
 var leftDiv = document.getElementById('viewcommL');
 var rightDiv = document.getElementById('viewcommR');

     if(rightCanScroll){      
 
       leftCanScroll = false;
 
       rightDiv.scrollTop  = leftDiv.scrollTop;
       rightDiv.scrollLeft  = leftDiv.scrollLeft;
 
       leftCanScroll = true;
      }
      return false;

}

function scrollCR()
{
 var leftDiv = document.getElementById('viewcommL');
 var rightDiv = document.getElementById('viewcommR');


     if(leftCanScroll){
 
      rightCanScroll = false;
 
      leftDiv.scrollTop =    rightDiv.scrollTop;
 
      leftDiv.scrollLeft =    rightDiv.scrollLeft;
 
      rightCanScroll = true;
 
     }
     return false;

}

function scrollAL()
{
 var leftDiv = document.getElementById('viewattachL');
 var rightDiv = document.getElementById('viewattachR');

     if(rightCanScroll){      
 
       leftCanScroll = false;
 
       rightDiv.scrollTop  = leftDiv.scrollTop;
       rightDiv.scrollLeft  = leftDiv.scrollLeft;
 
       leftCanScroll = true;
      }
      return false;



}

function scrollAR()
{
 var leftDiv = document.getElementById('viewattachL');
 var rightDiv = document.getElementById('viewattachR');


     if(leftCanScroll){
 
      rightCanScroll = false;
 
      leftDiv.scrollTop =    rightDiv.scrollTop;
 
      leftDiv.scrollLeft =    rightDiv.scrollLeft;
 
      rightCanScroll = true;
 
     }
     return false;

}


function checkSubmitText(val, text)
{

    var res = (/[\<\>\!\~\^\']/.test(val));
    if (res)
    {
      alert(text);
      return false;
    }
    else
    {
     return true;
    } 
}

//Checks the space/page name to be valid
function checkTextTitle(e)
{
  var ev = e||window.event;
  var char = String.fromCharCode(ev.charCode||ev.keyCode);
  if (ev.charCode==0) {
     return true;
   }
   return validateName(char);
}

//Returns true if space.page name is valid
function validateName(name)
{
 return  !(/[^0-9a-zA-Z\_\-\s]+/.test(name));
/*          
  for (i = 0; i < name.length; i++)
  {
    if (invalidChars.indexOf(name.charAt(i)) > -1) 
     return false;
  }
  return true;
*/

}

//Returns true if space.page name is valid
function validateNameSubmit(name)
{

    return  !(/([^0-9a-zA-Z\_\-\s]+)|(^\_+$)|(^\-+$)|(^\s*\-*\_*\-*\_*\_*\-*\s*$)|(\_\_)+|(\-\-)+|(\s*\_\s+\_\s*)|(\s*\-\s+\-\s*)/.test(name));

}

function showWizardError(text)
{
   var elem  = $('new_prj_err');
   elem.style.display = "block";
   elem.innerHTML = text;
   return false;
}

function checkSubmitTextTitle(val, text)
{
    res = validateNameSubmit(val);
//alert(val + " res=" + res);
                   
    if (!res || val == "")
    {
      showWizardError(text);
      return false;
    } 
    return res;
}

function checkSubmitTextTitleDefine(val, text)
{
      var elem  = $('new_prj_err');
      elem.style.display = "block";
      elem.innerHTML = text;
      return false;
}

function replaceMCE(val)
{

 var val1 =  val.replace(/&nbsp;/g, ""); 
 val1 =  val1.replace(/<br>/g, ""); 
 val1 =  val1.replace(/<\/p>/g, ""); 
 val1 =  val1.replace(/<p>/g, ""); 
 val1 =  val1.replace(/<p class="">/g, "");
 val1 =  val1.replace(/\s+/g, "");
 val1 =  val1.replace(/<\w*>/g, "");   
 val1 =  val1.replace(/<\/\w*>/g, "");

 return val1;

}

var canEditVal = false;
var canEditByAttach = false;


function canEdit()
{
  if (canEditByAttach || canEditVal)
      return true;
  else
      return false;
}

function setCantEdit()
{
  canEditVal = false;
}

function setCanEdit()
{
  canEditVal = true;
}

function commentChanged()
{
  var textArea = document.getElementById('XWiki.XWikiComments_comment');
  if (textArea != null) 
  {
    if (textArea.value.length == 0) 
      document.getElementById('saveComments').className = "disab";
    else
      document.getElementById('saveComments').className = "enab";
  }
}

var contentModified = false;

function forceCheck(e){
	//alert(contentModified);
	if (contentModified == false){
		var content = document.getElementById("content");
		content.blur();
		content.focus();
	}
}

function textAdded()
{  
  if (!canEditVal && ( document.getElementById('ViewMode').value != "view" ) )
  { 
  	  contentModified = true;
	  document.getElementById('formaction_save').className = "enab";
	  document.getElementById('formaction_save_only').className = "enab";
	  canEditVal = true;
	  document.getElementById('isEdit').value = "y";	  
  }
}

function myCustomOnInit() {
	document.getElementById('contentcontainer').style.visibility = "visible";
        document.getElementById('xwikidoccontent').style.visibility = "visible";
}

function hiddeMenuAll()
{
  hidePreviousMenu(document.getElementById('editButt'));
  hidePreviousMenu(document.getElementById('actionButt'));
  hidePreviousMenu(document.getElementById('createButt'));
}


function changeDiscard(msg)
{
 if (canEditVal)
  	return confirm(msg);
 else
        return true;
}

function isAttachEdit()
{
  return editAttach;
}

function attachEdit()
{
  editAttach = true;
}

function attachCancel(img1)
{
   var attTab = document.getElementById('attTable');

  var attRow = attTab.getElementsByTagName('tr');

  for (var i=0; i<attRow.length; i++)
  {
  
     var attInp = attRow[i].getElementsByTagName('a');
     if (attInp.length>0)
     {
         try{
            if (attInp[1].name.indexOf("buttonD") >=0 )
            {
              attInp[1].innerHTML = "<img src=\""+ img1 +"\"/>"; 
            } 
	 }catch(e){}
      }
  }

     for(i=0; i<attachDelete.length; i++ )
     {
       	 attachDelete[i] = null;
     }

   editAttach = false;
}

function AttachCheck(element, msg, img1,img2, name)
{

 if (editAttach)
 {
   var text = element.innerHTML;
   if ( text.toLowerCase().indexOf('minus') >=0 )
   {
     element.innerHTML = "<img src=\""+ img2 +"\"/>";
     var tr = getParentByTagName(element, "tr");
     if (tr != null) {
         tr.style.backgroundColor="#FFB9B9";
     }
     attachDelete.push(name);
     attachChange();
   }
   else
   {
   
     element.innerHTML = "<img src=\""+ img1 +"\"/>";
     var tr = getParentByTagName(element, "tr");
     if (tr != null) {
         tr.style.backgroundColor="";
     }
     for(i=0; i<attachDelete.length; i++ )
     {
       if ( attachDelete[i] == name )
       	 attachDelete[i] = "";

     }
     attachChange();
   }

   return false;
 }

 return confirm(msg);
}

function getParentByTagName(obj, tagName) {
    tagName = tagName.toLowerCase();
    while (obj != null && obj.tagName != null && obj.tagName.toLowerCase() != tagName) {
        obj = obj.parentNode;
    }
    return obj;
}


function attachChangeEditAttach()
{
     if ( document.getElementById('ViewMode').value != "view"  )
     { 
       document.getElementById('formaction_save').className = "enab";
       document.getElementById('formaction_save_only').className = "enab";
       canEditByAttach = true;
     }
}

var storeSpliterPos = -1;
var helpPanelWidth = 250;
var paddingHeight = 4;

function getClientWidth()
{
  return document.body.clientWidth;
}

function getClientHeight()
{
  var H = document.body.clientHeight; 
  return  H;
}

function delta()
{
  if (document.all)
    return 0;
  else
    return 1;
}

function getPanelsHeight()
{
    return 150;
}


function showMenubar() {
  $('menuView').style.display = 'block';
  document.getElementById("panelSwitcher").style.display = 'none';
  
  showTrackerSwitch();
  
  calcPanelsHeighView();
}

function showTrackerSwitch() {
    var trackerSwitch = top.document.getElementById("module_tracker_switch");
    if (trackerSwitch != null) {
        trackerSwitch.style.display = "block";
    }
}

function hideTrackerSwitch() {
    var trackerSwitch = top.document.getElementById("module_tracker_switch");
    if (trackerSwitch != null) {
        trackerSwitch.style.display = "none";
    }
}

function calcPanelsHeighView()
{
//alert("calcPanelsHeighView");
  var panelView = $('panelView');
  var panelMain = $('panelMain');
  var menuView = $('menuView');
  var xpreview = $('xpreview');

  var clientHeight = getClientHeight();
  
  if (!panelView && document.all) {
	  return;
  }
      

 
  if (panelView) {
     panelView.style.height = clientHeight + "px"; 
     panelMain.style.height = parseInt(panelView.style.height)  + "px";
  }
  
  var menuBarHeight = 46;
  if (menuView != null && menuView.style.display == 'none') {
	  menuBarHeight = 0;
  }
  
  var contentHeight = 0;
  if(panelMain)
  {
     contentHeight =  parseInt(panelMain.style.height) - menuBarHeight ;
     $('viewcontent').style.height = contentHeight + "px";
  }

  if ($('xwikisyntaxhelppanel'))
    $('xwikisyntaxhelppanel').style.height = contentHeight + "px";
  if (xpreview)
  {
    xpreview.style.height = $('viewcontent').style.height;
  }


  calcCompareHeight(contentHeight);
}

function calcPanelsHeighEdit()
{
  var panelView = $('panelView');
  var xpreview = $('xpreview');
  var clientHeight = getClientHeight();

  if (!panelView && document.all) {
	  return;
  }
  panelView.style.height = clientHeight + "px";
  
  if ($('panelHelp')) {
      $('panelHelp').style.height = panelView.style.height;
  }
  
  var menuBarHeight = 46;
  var contentHeight = parseInt(panelView.style.height) - menuBarHeight ;
  $('viewcontent').style.height = contentHeight + "px";
  if ($('xwikisyntaxhelppanel')) {
    $('xwikisyntaxhelppanel').style.height = contentHeight + "px";
  }
  
  var editArea = document.getElementById("content");
  if (editArea != null) {
      editArea.style.height = (contentHeight - (document.all ? 50 : 60)) + "px";
  }
  
  if (xpreview) {
      xpreview.style.height = $('viewcontent').style.height;
  }
  if ($('resizer')) {
    $('resizer').style.top = Math.round(getClientHeight() / 2) - 2 + "px";
  }
  calcCompareHeight(contentHeight);
}

function calcCompareHeight(contentHeight)
{
  var offset = 70; //78 + 2*delta();
  if ($('compare_content_view'))
  {
     var panelResize = null;
     var viewL = null;
     var viewR = null;
     var resizer =  null;
    if ($('compare_content_view').style.display == "none")
    {
      panelResize = $('panelResizeAtt');
      viewL = $('viewattachL');
      viewR = $('viewattachR');
      resizer = $('resizerAtt');
    }
    else
    {
      panelResize = $('panelResize');
      viewL = $('viewcontentL');
      viewR = $('viewcontentR');
      resizer = $('resizerCont');
    }
    var compareHeight = contentHeight - offset + "px";

    if (panelResize)
    {
        panelResize.style.top =  offset + "px";
        panelResize.style.height = compareHeight;
    }
    if (viewL)
        viewL.style.height = compareHeight;
    if (viewR)
        viewR.style.height = compareHeight;
    if (resizer)
        resizer.style.top = Math.round((contentHeight - offset) / 2)  + "px";
  }
}

function calcPanelsWidth() {
    var panelHelp = $('panelHelp');
    var isMin = false;
    var panView = $('panelView');
    
    if (panView ) {
        panView.style.width = parseInt(getClientWidth()) + "px";
    }

    if (panelHelp && panelHelp.style.display != "none") {
        var rsWidth = parseInt($('panelResize').style.width);
        var rsMain = parseInt($('panelResize').style.left) ;
        var helpWidth = getClientWidth() - rsWidth - rsMain;
        var mnWidth = (rsMain - 2*delta());
        if (mnWidth < 0) mnWidth = 1;
        
        $('panelMain').style.width = mnWidth + "px";
        
        panelHelp.style.left = parseInt($('panelResize').style.left) + rsWidth + "px";
        if (helpWidth > -1) {
            panelHelp.style.width = helpWidth + "px";
        }
    } else {
        var pMain = $('panelMain');
        if (pMain || !document.all) {
            if (pMain) {
                pMain.style.width = $('panelView').style.width;
            }
        }
    }
    
    //Compare
    if ($('viewcontentL')) {
        calcCompareWidth();
    }
    
    calcSpaceIndexWidth();
}

function calcCompareWidth()
{
  if ($('compare_content_view'))
  {
    if ($('compare_content_view').style.display == "none")
    {
      //attachment     
      resizePanel = $('panelResizeAtt');
      viewL = $('viewattachL');
      viewR = $('viewattachR');
    }
    else
    {
      //content
      resizePanel = $('panelResize');
      viewL = $('viewcontentL');
      viewR = $('viewcontentR');
    }
    var resizeWidth = 0;
    var resizeLeft =  0;
    if (resizePanel)
    {
       resizeWidth = parseInt(resizePanel.style.width);
       resizeLeft = parseInt(resizePanel.style.left);
    }
    if (viewL)
        viewL.style.width = resizeLeft + "px";
    var viewRLeft = resizeLeft + resizeWidth;
    if (viewR)
    {
        viewR.style.left = viewRLeft + "px";
        if ( (parseInt(getClientWidth()) - viewRLeft) > 0 )
        viewR.style.width = parseInt(getClientWidth()) - viewRLeft + "px";
    }
  }
}

function calcSpaceIndexWidth()
{
//alert("calcSpaceIndexWidth");
  //resize space index width
//  var spaceIndexWidth = parseInt($('tableindex_info').style.width);
//alert(spaceIndexWidth);
  if($('SpaceIndex_tab'))
  {                         
     if ($('SpaceIndex_tabitemTable_scrollDiv'))     
     {
//       $('SpaceIndex_tabitemTable_scrollDiv').style.width = spaceIndexWidth - 4 + "px";
//       $('SpaceIndex_tabitemTable_outerDiv').style.width = spaceIndexWidth - 4 + "px";
//       $('SpaceIndex_tabitemTable_innerDiv').style.width = spaceIndexWidth - 22 + "px";
     }
   }
   if ($('SpaceIndex_tab'))
     $('SpaceIndex_tab').style.width = "99.5%";

   if (!document.all && $('tableindex_info'))
     $('tableindex_info').style.width = "99.9%";
}



function attachChange()
{
  if (!canEditVal  && !canEditByAttach  && ( document.getElementById('ViewMode').value != "view" ) )
  { 

	  document.getElementById('formaction_save').className = "enab";
	  if(document.getElementById('formaction_save_only'))
		  document.getElementById('formaction_save_only').className = "enab";
	  canEditByAttach = true;
  }
  else if ( !canEditVal  && canEditByAttach  && ( document.getElementById('ViewMode').value != "view" ) )
  {
     var res = true;
     for(i=0; i<attachDelete.length; i++ )
     {
       if ( attachDelete[i] != "" && attachDelete[i] != null)
       {
           res = false;
           break;
       }
       else
       {
           res = true;
           continue;
       }
     }

     if (res && countNewAttach <= 0)
     {
	  document.getElementById('formaction_save').className = "disab";
	  if(document.getElementById('formaction_save_only'))
		  document.getElementById('formaction_save_only').className = "disab";
	  canEditByAttach = false;

     }	
  }
}
function xwikiAjaxCallStarted(url){
    try {
        if (top.publishFromFrame){
            top.publishFromFrame("xwikiAjaxCall", "Started: " + url);
        }
    } catch (e){
        //
    }
}
function xwikiAjaxCallFinished(url){
    try {
        if (top.publishFromFrame){
            top.publishFromFrame("xwikiAjaxCall", "Finished: " + url);
        }
    } catch (e){
        //
    }
}
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
                xwikiAjaxCallFinished(url);
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
            xwikiAjaxCallStarted(url);
        } else if (window.ActiveXObject) {
            // ie
            ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            if (ajaxRequest) {
                ajaxRequest.onreadystatechange = ajaxBindCallback;
                ajaxRequest.open("GET", url, true);
                ajaxRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
                ajaxRequest.send();
                xwikiAjaxCallStarted(url);
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

function scrollToTargetInViewContent(target)
{
 // window.location = "#" + target;
  document.getElementById("viewcontent").scrollTop = document.getElementById("viewcontent").scrollHeight;
}

function LoadWorkItems()
{
 for (var i=0; i<runItem.length; i++)
 {
   var fn = runItem[i];
   setTimeout(""+fn+"()",100);
 }
}
try{
    Event.observe(window, 'load', LoadWorkItems);
} catch(e){
    //TODO this throws error in DLE in both FF and IE, so where it is supposed to work? 
}
//Added to set title of document in frame as page title
/*Event.observe(window, 'load', function(){
	window.parent.document.title = window.document.title;
});*/

function selectAllBoxs()
{   
     var currentState = document.getElementById("main_checkbox").checked;
     var allBoxs = document.getElementsByTagName('input');
     
     for (var i=0; i<allBoxs.length; i++ )
     {
         var elm = allBoxs[i].id;
         if (elm.indexOf('box_') > -1)
         {
             allBoxs[i].checked = currentState;
         }
     }    
}

function deletePagesInSpace(msg)
{
     if ( !confirm(msg))
          return;

     var allBoxs = document.getElementsByTagName('input');
     var array = "";
     var selCount = 0;
     for (var i=0; i < allBoxs.length; i++ )
     {
         var elm = allBoxs[i];
         if (elm.id.indexOf('box_') > -1 && elm.checked)
         {
            array = array + allBoxs[i].value +";";             
            selCount++;
         }
     }
     if (selCount == 0) 
     {
       alert("None of the page has been selected.");
       return;	
     }
     document.getElementById('pagesForDelete').value = array;
     document.forms['spaceIndexDeletePages'].submit();
}

function closeLoginWiki()
{
		 try {
		 	loginSucessfull();
		 } catch(e){
		 
		 }
         document.getElementById('checkFrame').style.display = "inline";
         document.getElementById('divcheckFrame').style.visibility =  "hidden";

}

function openLoginWiki()
{
         document.getElementById('checkFrame').style.display = "inline";
         document.getElementById('divcheckFrame').style.visibility =  "visible";

}

function closeLoginWiki2()
{
         document.getElementById('checkFrame2').style.display = "inline";
         document.getElementById('divcheckFrame2').style.visibility =  "hidden";
}

function openLoginWiki2()
{
         document.getElementById('checkFrame2').style.display = "inline";
         document.getElementById('divcheckFrame2').style.visibility =  "visible";
}

//Use prototype ajax, in IE there is a problem with native ajax
function sessionWisiwyg(URL)
{
var url = document.forms['checkLoginW'].action;
popIsExpiredSession = false;
new Ajax.Request(url, {
  method: 'get',
  onSuccess: function(transport) 
  {
    var notice = transport.responseText;

      if(notice.indexOf("j_username") != -1 )
      {  
          showMainlogin = true;
          openLoginWiki2();
          document.getElementById('checkFrame2').src = "/polarion/internal_login.jsp";
          popIsExpiredSession = true;
      }
      else
      {
          document.getElementById('popupWindow').style.display = 'block';
          document.getElementById('popupFrame').src = URL;
      }
  },
  onFailure: function() 
  {
          showMainlogin = true;
          openLoginWiki2();
          document.getElementById('checkFrame2').src = "/polarion/internal_login.jsp";
  }

});

}

function sessionWisiwyg2(URL, pop)
{
var url = document.forms['checkLoginW'].action;
popIsExpiredSession = false;

new Ajax.Request(url, {
  method: 'get',
  onSuccess: function(transport) 
  {
    var notice = transport.responseText;

      if(notice.indexOf("j_username") != -1 )
      {  
          showMainlogin = true;
          openLoginWiki2();
          document.getElementById('checkFrame2').src = "/polarion/internal_login.jsp";
          popIsExpiredSession = true;
      }
      else
      {
	  pop.insertFromAjax();
      }
  },
  onFailure: function() 
  {
          showMainlogin = true;
          openLoginWiki2();
          document.getElementById('checkFrame2').src = "/polarion/internal_login.jsp";
  }

});

}


function CallBackDescription(e, id)
{
   if (e.indexOf("xdesc_wi") == -1)
   {
      return;
   }
   document.getElementById('descrWI').innerHTML = e;
   document.getElementById('descrWI').style.left = tempX-3;
   document.getElementById('descrWI').style.top  = tempY-3;
   document.getElementById('descrWI').style.display = 'inline';
   document.getElementById('descrWI').style.visibility = 'visible';
}

function getDescription(id, url)
{
    url = encodeURI(url+"?xpage=xdescrpwi&makroPicker=true&idwi="+id);
    execAJAX(url, "", CallBackDescription, "" ) ;
}

function getComments(id, url)
{
    url = encodeURI(url+"?xpage=xcommentswi&makroPicker=true&idwi="+id);
    execAJAX(url, "", CallBackDescription, "" ) ;
}

document.onmousemove = getMouseXY;
var tempX = 0
var tempY = 0


function getMouseXY(e) 
{
  try {
    if (document.all) {
      tempX = event.clientX + document.body.scrollLeft
      tempY = event.clientY + document.body.scrollTop
    } else {
      tempX = e.pageX
      tempY = e.pageY
    }
  } catch(e){
    // nothing to do, throws exception in IE
  }  
  if (tempX < 0){tempX = 0}
  if (tempY < 0){tempY = 0}  

  return true
}

function closeDesc()
{
  document.getElementById('descrWI').style.visibility = 'hidden';
  document.getElementById('descrWI').style.display    = 'none';
}

 function doSearch(opt, text)
 {
   var s = opt[opt.selectedIndex].value;
   if (opt.selectedIndex == 1) 
   { 
     if (s == "/") 
       //in the repo root
       s = "wiki/repo_root";
     else
       //in the project rooot
       s = "project/" + s + "wiki";
   }
	top.search(text,s,"wiki");
 }

function doSearchSpace(s, text)
 {
 	top.search(text,s,"wiki");
 }
 
function checkAndSearch(searchForm, message){
	if (checkSubmitText(document.search.text.value , message)){
		document.getElementById("searchForm").value = searchForm;
                var param = "searchForm="+ searchForm;
                executeCommandPost(document.getElementById('checkLoginS').action, checkCallBack, param);
	}
}

var contentLen = -1;

function checkContentModified(){
	var content = document.getElementById("content");
	if (contentLen<0){
		contentLen = content.value.length;
	}
	if (contentLen != content.value.length){
		textAdded();
		contentLen = content.value.length;
		window.clearInterval(window.checkerId);
	}
}

function checkContentModifiedScheduler(){
	if (document.getElementById("content")){
		var content = document.getElementById("content");
		contentLen = content.value.length;
	 	window.checkerId = window.setInterval('checkContentModified()', 100);
	 }
}
try{
    Event.observe(window, 'load', checkContentModifiedScheduler);
} catch(e){
    //TODO this throws error in DLE in both FF and IE, so where it is supposed to work? 
}

function refreshWikiWindow(){
	refreshWikiWindow(true);
}


function refreshWikiWindow(newStyle)
{
 try
 {
    if (!window.parent.working_area.document)
    {
       //window.location.reload();
       window.location.href = window.location;
    }

 }catch(e)
 {
   //window.location.reload();
   window.location.href = window.location;
 }

 var formsave;
 try
 {
   formsave = window.parent.working_area.document.getElementById('formaction_save');
 }
 catch(e)
 {
   formsave = null;
 }

 if(formsave == null)
 {
    var a = true;
    if (isAttachEdit())
      a = confirm(msgDiscard);
    if (a == true){
    	//window.parent.working_area.location.reload();
    	var location = window.parent.working_area.location;

    	var k = location.toString().lastIndexOf("#");
    	var d = location.toString().lastIndexOf("/#");

    	if ( (k != -1 && d == -1) || (k != -1 && d != -1 && ( (k-1) != d ))  )
		     location = location.toString().substring(0,k);
    	
    	if (newStyle)
    	{
    		window.parent.reloadPageWidget(escape(location));
    	}
    	else
    	{
    		window.parent.working_area.location.href = location;
    	}
    } 
 }
 else
 {
    var loc = window.parent.working_area.location.toString();
    if (formsave.className == 'enab')
    {
        var r = confirm(msgDiscard);
        if (r == true)
        {
            window.parent.reloadPageWidget(escape(loc));
        }
    }
    else
    {
        window.parent.reloadPageWidget(escape(loc));
    }
  }
  editAttach = false;
}

function getActionText(text)
{
  return text;
}

function showPanel(btnId)
{
//alert("hidePanel");
   btn =  $(btnId);
   s = "hide" + btnId;
   menuHide =  $(s);
   s = "show" + btnId;
   menuShow = $(s);
   if (btn.style.display == "none")
   {
     btn.style.display = "block";
     if ( menuHide ) {
        menuHide.style.display = "block";
     }
     if ( menuShow ){
        menuShow.style.display = "none";
     }
     scrollToTargetInViewContent(btnId);
   } 
   else if (btnId == "recentlyVisitedPanel")
   { 
     btn.style.display = "none";
     if ( menuHide )
        menuHide.style.display = "none";
     if ( menuShow )
        menuShow.style.display = "block";
   }
   else if (btn.style.display = "block")
   {
      scrollToTargetInViewContent(btnId);
   }
  hiddeMenuAll();
}

function togglePanelVisibilityWi(obj, element, url)
{
 if (obj.style.display == "none")
 {
    obj.style.display = "inline";
    element.src = url + "expand_min.gif";
 }
 else
 {
   obj.style.display = "none";
   element.src = url + "expand.gif";
 }
}

function isDocumentPage(page)
{
 for (var i=0; i<wikiDefineNames.length; i++ )
 {
   if (wikiDefineNames[i] == page.toLowerCase())
   {
       return false;
   }
 }
 return true;
}

var chLn = -1;
var lastChLn = 0;
var origBgColor = "";

function prevChangeLine(){
	if ( chLn > 0 ) 
	   chLn = chLn - 1;
	if (chLn >= 0)
	{
		highlightLine(chLn, chLn+1);
		if ( document.getElementById("ch_lnL_" + chLn) )
		   location.hash = "ch_lnL_" + (chLn);
		else
		   if ( document.getElementById("ch_lnR_" + chLn) )
		     location.hash = "ch_lnR_" + (chLn);
	}
	return false;
}

function nextChangeLine(){
	if (chLn < lastChLn){
	        chLn = chLn + 1;
		highlightLine(chLn, chLn - 1);
		if ( document.getElementById("ch_lnL_" + chLn) )
		   location.hash = "ch_lnL_" + (chLn);
		else
		   if ( document.getElementById("ch_lnR_" + chLn) )
		     location.hash = "ch_lnR_" + (chLn);
	}
	return false;
}

function highlightLine(currNum, prevNum){
	if  (currNum >= 0 && currNum <= lastChLn){
		if (document.getElementById("ch_lnL_" + currNum))
			document.getElementById("ch_lnL_" + currNum).parentNode.style.backgroundColor = "yellow";
		if (document.getElementById("ch_lnR_" + currNum))	
			document.getElementById("ch_lnR_" + currNum).parentNode.style.backgroundColor = "yellow";
	}
	if (prevNum >= 0 && prevNum <= lastChLn){
		if (document.getElementById("ch_lnL_" + prevNum))
			document.getElementById("ch_lnL_" + prevNum).parentNode.style.backgroundColor = "rgb(233, 243, 250)";
		if (document.getElementById("ch_lnR_" + prevNum))	
			document.getElementById("ch_lnR_" + prevNum).parentNode.style.backgroundColor = "rgb(233, 243, 250)";
	}
}

function setLastChLnNum(num){
	lastChLn = num;
}

function mainProgressBarShow(text)
{
  if ( document.getElementById("progressbar") )
  {
     document.getElementById("progress_operation").innerHTML=text;
     document.getElementById("progressbar").style.visibility="visible";
  }
}

function mainProgressBarHide()
{
  if ( document.getElementById("progressbar") )
  {
     document.getElementById("progressbar").style.visibility="hidden";
  }
}

function searchEnterPress(e, text, isAdv)
{
   var keynum;
   var keychar;


   if(window.event) // IE
   {
     keynum = e.keyCode;
   }
   else if(e.which) // Netscape/Firefox/Opera
   {
      keynum = e.which;
   }
   else return;


  if(keynum == 13)
  {
     if (isAdv)
     {
        if (checkSubmitText(searchW.text.value , text ))
        {
          doSearch(searchW.search_in, document.searchW.text.value);
          document.searchW.submit();
        }
     }
     else
     {

        if (checkSubmitText(document.search1.text1.value , text) )
        {
          doSearch(document.search1.search_in1, document.search1.text1.value);
          document.search1.submit();
        }
     }
  }	
}


function decodeWikiURL(utftext)
{
                //decodeWikiURL(unescape(string)

		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
 
		while ( i < utftext.length ) {
 
			c = utftext.charCodeAt(i);
 
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
 
		}
 
		return string;

}

function encodeWikiURL(string)
{
        //escape(encodeWikiURL(string)

	string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}

		return utftext;


}

function checkCallBack(text)
{

	if( text.indexOf("j_username") != -1  )
        {
           document.getElementById('checkFrame').src = "/polarion/internal_login.jsp";
           //openLoginWiki();
        }
        else
        {
           
           if(text.indexOf("<script type=\"text/javascript\">") == -1 )
           {
            closeLoginWiki();
           }
           else
           {
             text = text.replace("<script type=\"text/javascript\">", "");
             text = text.replace("</script>", "");
             eval(text);
           }
        }
}