function toggle(objId, collapsedImg, expandedImg) {
	div = document.getElementById(objId + ".pane");
	
	if(div.style.display != "none") {
		collapse(objId, collapsedImg);
	} else {
		expand(objId, expandedImg);
	}
}

/**
 * 
 */
function expand(objId, expandedImg) {
	obj = document.getElementById(objId);
	if(obj != null) {
		obj.className = "expanded";
	}
	
	div = document.getElementById(objId + ".pane");
	if(div != null) {
		div.style.display = "block";
	}
	
	img = document.getElementById(objId + ".img");
	if(img != null) {
		if(expandedImg.src != null) {
			img.src = expandedImg.src;
			img.alt = expandedImg.alt;
			img.title = expandedImg.title;
		} else {
			img.src = expandedImg;
		}
	}
		
	if(objId.lastIndexOf("/") > 0) {
		objId = objId.substring(0, objId.lastIndexOf("/"));
		expand(objId, expandedImg);
	}
}

/**
 * Function collapse collapses simple node (tree, preference, ..)
 */
function collapse(objId, collapsedImg) {
	obj = document.getElementById(objId);
	if(obj != null) {
		obj.className = "collapsed";
	}
	
	div = document.getElementById(objId + ".pane");
	if(div != null) {
		div.style.display = "none";
	}
	
	img = document.getElementById(objId + ".img");
	if(img != null) {
		if(collapsedImg.src != null) {
			img.src = collapsedImg.src;
			img.alt = collapsedImg.alt;
			img.title = collapsedImg.title;
		} else {
			img.src = collapsedImg;
		}
	}
}

function toggleMinMax(objId, imgPath) {
	var collapsedImg = new Image();
	imgPath = "/polarion/ria/images/";
	collapsedImg.src = imgPath + "control/maximize_portlet.gif";
	collapsedImg.alt = "Maximize";
	collapsedImg.title = "Maximize";
	
	var expandedImg = new Image();
	expandedImg.src = imgPath + "control/minimize_portlet.gif";
	expandedImg.alt = "Minimize";
	expandedImg.title = "Minimize";
	
	toggle(objId, collapsedImg, expandedImg)
}

function setRefresh(el, handler){
		if(el==null){
			return false;
		}
		var portlet;
		var parent = el.parentNode;
  	for (var i=0; i<5 && portlet!=null; i++) {
   		if(parent.className!=null && parent.className.indexOf("portlet")!=-1){
   			portlet = parent;
   		}
   		parent = parent.parentNode;
		}
		alert("Portlet:"+portlet);
		var refreshAction = portlet.getElementByName("refreshAction");
		if(refreshAction==null){
			return false;
		}
		alert("refreshAction:"+refreshAction);
		refreshAction.href=handler;
		refreshAction.style.display="inline";
	  return true;
}