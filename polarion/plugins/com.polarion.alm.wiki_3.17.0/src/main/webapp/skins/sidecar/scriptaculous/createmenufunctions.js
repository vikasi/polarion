Object.extend(Element, {
	getWidth: function(element) {
	   	element = $(element);
	   	return element.offsetWidth; 
	},
	setWidth: function(element,w) {
	   	element = $(element);
    	element.style.width = w +"px";
	},
	setHeight: function(element,h) {
   		element = $(element);
    	element.style.height = h +"px";
	},
	setTop: function(element,t) {
	   	element = $(element);
    	element.style.top = t +"px";
	},
	setLeft: function(element,t) {
	   	element = $(element);
    	element.style.left = t +"px";
	},
	setSrc: function(element,src) {
    	element = $(element);
    	element.src = src; 
	},
	setHref: function(element,href) {
    	element = $(element);
    	element.href = href; 
	},
	setInnerHTML: function(element,content) {
		element = $(element);
		element.innerHTML = content;
	}
});

		var initialized;
		function init(){
			var objBody = document.getElementsByTagName("body").item(0);
//			var objOverlay = document.createElement("div");
//			objOverlay.setAttribute('id','overlay');
//			objOverlay.style.display = 'none';
//			objOverlay.onclick = function() { end(); return false; }
//			objBody.appendChild(objOverlay);

			var objTable = document.createElement("div");
			objTable.setAttribute('id','createnewpagewindow');
			objBody.appendChild(objTable);

			var objWindowContainer = document.createElement("div");
			objWindowContainer.setAttribute('id','windowContainer');
			objTable.appendChild(objWindowContainer);
			objTable.innerHTML=popupwindowcontent;
			loadSpaces();
			
		}

	function createNew(){
	//	hideSelectBoxes();
		//if(initialized !=1) init();
		//initialized = 1;
		init();
//		var arrayPageSize = getPageSize();
//		Element.setHeight('overlay', arrayPageSize[1]);
//		new Effect.Appear('overlay', { duration: 0.2, from: 0.0, to: 0.8 });
		
//		var arrayPageSize = getPageSize();
//		var arrayPageScroll = getPageScroll();
//		var objTableTop = arrayPageScroll[1] + (arrayPageSize[3] / 15);
		for (var i=0; i<props.length;i++)
		{
			var w = document.documentElement[props[1]];
			var h = document.documentElement[props[0]];
		}
		Element.show('createnewpagewindow');

		Element.setTop('createnewpagewindow', (h - 300)/2);
		Element.setLeft('createnewpagewindow', (w - 400)/2);
	}
	
	
	function end(){
		document.getElementById('createnewpagewindow').innerHTML = '';
		Element.hide('createnewpagewindow');
	//	showSelectBoxes();
	}
	
	function showSelectBoxes(){
		selects = document.getElementsByTagName("select");
		for (i = 0; i != selects.length; i++) {
			selects[i].style.visibility = "visible";
		}
	}

	function hideSelectBoxes(){
		selects = document.getElementsByTagName("select");
		for (i = 0; i != selects.length; i++) {
			selects[i].style.visibility = "hidden";
		}
	}
	
	function getPageScroll(){

		var yScroll;
	
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){	 // Explorer 6 Strict
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {// all other Explorers
			yScroll = document.body.scrollTop;
		}
	
		arrayPageScroll = new Array('',yScroll) 
		return arrayPageScroll;
	}
	
	
	function getPageSize(){
	
	var xScroll, yScroll;
	
	if (window.innerHeight && window.scrollMaxY) {	
		xScroll = document.body.scrollWidth;
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
		xScroll = document.body.scrollWidth;
		yScroll = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
		xScroll = document.body.offsetWidth;
		yScroll = document.body.offsetHeight;
	}
	
	var windowWidth, windowHeight;
	if (self.innerHeight) {	// all except Explorer
		windowWidth = self.innerWidth;
		windowHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
		windowWidth = document.documentElement.clientWidth;
		windowHeight = document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowWidth = document.body.clientWidth;
		windowHeight = document.body.clientHeight;
	}	
	
	// for small pages with total height less then height of the viewport
	if(yScroll < windowHeight){
		pageHeight = windowHeight;
	} else { 
		pageHeight = yScroll;
	}

	// for small pages with total width less then width of the viewport
	if(xScroll < windowWidth){	
		pageWidth = windowWidth;
	} else {
		pageWidth = xScroll;
	}


	arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight) 
	return arrayPageSize;
}