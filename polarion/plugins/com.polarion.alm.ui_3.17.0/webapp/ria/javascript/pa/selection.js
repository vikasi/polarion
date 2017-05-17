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

function open_context_help(url) {
    window.open("/polarion"+url, "_blank", document.all ? "toolbar=yes,location=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes" : "");
}