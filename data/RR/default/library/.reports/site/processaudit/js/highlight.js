var PARAMETER_TABLE_ID = "tableid";
var PARAMETER_COLUMN_ID = "tdid";

var MAXIMUM_DEPTH_LEVEL = 2;

function highlight() {
     var tableParam = getURLParam(PARAMETER_TABLE_ID);     
     var tdId = getURLParam(PARAMETER_COLUMN_ID);
     
     //alert(tableParam);
     
     if (tableParam != null && tdId != null) {         
        var table = document.getElementById(tableParam);
        //alert(table);       
        if (table != null) {                 
            processElements(table, tdId, 1);
        }
     }
}

function processElements(table, tdId, depthLevel) {
    for (var i = 0; i < table.rows.length; i ++) {
        for (var j = 0; j < table.rows[i].cells.length; j ++) {
            var cell = table.rows[i].cells[j];
            var cellId = cell.id;
            if (cellId != null) {                
                if (cellId.indexOf(tdId) != -1) {
                    cell.className = 'active';                       
                    //alert('gut ' + cellId);
                    var subTable = cell.getElementsByTagName("table")[0];
                    if (depthLevel <= MAXIMUM_DEPTH_LEVEL && subTable != null) {
                        processElements(subTable, tdId, depthLevel + 1);
                    }
                }   
            }            
        }
    }
}

function getURLParam(strParamName){
	var strReturn = "";
	var strHref = window.location.href;
	if ( strHref.indexOf("?") > -1 ){
    	var strQueryString = strHref.substr(strHref.indexOf("?")).toLowerCase();
	    var aQueryString = strQueryString.split("&");
    	for ( var iParam = 0; iParam < aQueryString.length; iParam++ ){
	        if (aQueryString[iParam].indexOf(strParamName.toLowerCase() + "=") > -1 ) {
		        var aParam = aQueryString[iParam].split("=");
        		strReturn = aParam[1];
      			 break;
            }
        }
	}
	return unescape(strReturn);
} 