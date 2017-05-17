var SHOW_BRANCH = "showBranch";
var HIDE_BRANCH = "hideBranch";

var SHOW_IMAGE = "control/toggle_plus.gif";
var HIDE_IMAGE = "control/toggle_minus.gif";

var SHOW_ARROW = "showArrow";
var HIDE_ARROW = "hideArrow";

var SHOW_ARROW_IMAGE = "down_arrow.gif";
var HIDE_ARROW_IMAGE = "down_arrow.gif";

var maximalAllowedRowCount = 3;

function toggleSection(nsender, tableId, rowsToHandleIdPart, imagesDir, isRestrictedRowCount) {
    var toggleFunction = "javascript:toggleSection('" + nsender + "','" +  tableId + "','" + rowsToHandleIdPart+ "','" + imagesDir+ "','" + isRestrictedRowCount + "');";   
    
	var sender = document.getElementById(nsender);
	var t = document.getElementById(tableId);
    rows = t.rows;    	    	            
    	
   	var isShowRows = false;   
   	   	
   	if (sender.name == undefined) {
        sender.name = SHOW_ARROW;   	
    }   	
    
   	if (sender.name == SHOW_ARROW) {   
    		sender.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + toggleFunction + "\">" + 
    		    "<img border=\"0\" align=\"absmiddle\" src=\"" + imagesDir + HIDE_ARROW_IMAGE +  "\"/>" + 
    		"</a>" +         	
    		"&nbsp;Hide more (Click to toggle)";       	                   	
                    
	   	sender.name = HIDE_ARROW;
   		isShowRows = true;
   	} else if (sender.name = HIDE_ARROW) {       	
    		sender.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + toggleFunction + "\">" + 
    		    "<img border=\"0\" align=\"absmiddle\" src=\"" + imagesDir + SHOW_ARROW_IMAGE +  "\"/>" + 
    		"</a>" +         	
    	    "&nbsp;Show more (Click to toggle)";  
    		       	
       	
	   	sender.name = SHOW_ARROW;
	   	isShowRows = false;
   	}       
	if (isShowRows) {
    	showRows(rows, rowsToHandleIdPart);
    } else {
        hideRows(rows, rowsToHandleIdPart, isRestrictedRowCount);    
    }
}

function toggleList(nsender, tableId, rowsToHandleIdPart, imagesDir) {
	var sender = document.getElementById(nsender);
	var t = document.getElementById(tableId);
    rows = t.rows;    	    	            
    	
   	var isShowRows = false;  
   	
   	if (sender.name == SHOW_BRANCH) { 
   		sender.name = HIDE_BRANCH;
	   	sender.src = imagesDir + HIDE_IMAGE;  
	   	isShowRows = true; 		
   	} else  {  
	   	sender.name = SHOW_BRANCH;
	   	sender.src = imagesDir + SHOW_IMAGE;
	   	isShowRows = false; 			   	
   	}
   	
   	if (isShowRows) {
    	showRows(rows, rowsToHandleIdPart);
    } else {
        hideRows(rows, rowsToHandleIdPart, 'false');    
    }
}


function showRows(rows, rowsToHandleIdPart) {                       
    for (i = 0; i < rows.length; i++) {			
		if (rows[i].id.indexOf(rowsToHandleIdPart)!=-1) {
            if (isMSIE()) {
                rows[i].style.display = 'block';                    
            } else {
                rows[i].style.display = 'table-row';                                        
            }                        
		}    		
	}
}
	
function hideRows(rows, rowsToHandleIdPart, isRestrictedRowCount) {       
    var visitedRows = 0;
    for (i = 0; i < rows.length; i++) {                                    			          	   	    	    
        if (rows[i].id.indexOf(rowsToHandleIdPart)!= -1) {   
            visitedRows ++;                                         
	        if (isRestrictedRowCount == 'false' || isRestrictedRowCount == 'true' && visitedRows  > maximalAllowedRowCount) {    	                                                                       	
                rows[i].style.display = 'none';                            
    		}
        }
    }           
}

var __isMSIE = navigator.userAgent.indexOf("MSIE")!=-1;

function isMSIE() {
    return __isMSIE;
}