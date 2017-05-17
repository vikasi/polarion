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
	collapsedImg.src = imgPath + "maximize.gif";
	collapsedImg.alt = "Maximize";
	collapsedImg.title = "Maximize";
	
	var expandedImg = new Image();
	expandedImg.src = imgPath + "minimize.gif";
	expandedImg.alt = "Minimize";
	expandedImg.title = "Minimize";
	
	toggle(objId, collapsedImg, expandedImg)
}