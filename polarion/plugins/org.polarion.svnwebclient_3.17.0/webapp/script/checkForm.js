function checkGotoForm(submitButton, revisionCountAlert) {  
    var form = submitButton.form;
    if (!form.setRevision[0].checked) {
        if (!isNumber("inputRevision")) {
            alert(revisionCountAlert); 
            return false;
        }                
    }
    return true;
}

function disableField(field) {
    var form = field.form;
    form.elements["inputRevision"].disabled = true;        
}

function enableField(field) {
    var form = field.form;
    form.elements["inputRevision"].disabled = false;        
}

function isNumber(fieldName) {
    var re = new RegExp('[0-9.]'); 
    return !(!re.test(document.getElementById(fieldName).value) || document.getElementById(fieldName).value <= 0);
}

function checkRevisionNumber(revisionAlert) {
    if (isNumber("revcount")) {
        return true;    
    } else {
        alert(revisionAlert); 
        return false;
    }
}

function checkRevisionsRange(revisionRangeAlert) {
   if (isNumber("startrange") && isNumber("endrange")) {
       return true;     
   } else {
       alert(revisionRangeAlert); 
       return false;
   } 
}

function navigate(url, revisionAlert) {        
    if (checkRevisionNumber(revisionAlert) == true) {
        window.location = url + "&revcount=" + document.getElementById('revcount').value;
    } else   {
        return false;
    }  
}

function rangeNavigate(url, revisionRangeAlert) {        
    if (checkRevisionsRange(revisionRangeAlert) == true) {    
        window.location = url + "&startrevision=" + document.getElementById('startrange').value + "&endrevision=" + document.getElementById('endrange').value;
    } else   {
        return false;
    }          
}
