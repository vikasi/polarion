function selectRow(row) {    
    row.name = row.className;
	row.className = "selected";	
}

function deselectRow(row) {
    row.className = row.name;	
}

function selectRowWithoutLink(row) {    
    row.name = row.className;
	row.className = "selectedWithoutLink";	
}

function setHandCursor(row) {
    row.style.cursor = "pointer";
}