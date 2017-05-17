function show(url) {
	window.location.href = url;
}

function showUrl(url) {
	window.status = url;
}

function open_context_help(context_help_url)
{
    var vWinUsers = window.open(context_help_url, 'Help', 'status=no,resizable=yes,top=100,left=200,width=500,height=300,titlebar=no');
    vWinUsers.opener = self;
    vWinUsers.focus();
}


var required_input_fields = new Array(0);	

function add_required_input(id){
	required_input_fields.push(id);
}

function check_required_fields(){
    var pass = true;
    var notFilled = new Array(0);
for(var i = 0; i < required_input_fields.length;i++){
	var key = required_input_fields[i];
	var value = document.getElementById(key).value;
		
  if(value!=null && value.length<1){
		pass = false;
			notFilled.push(key);
  }else if(value.search(/\w+/)==-1){
	  	pass = false;
			notFilled.push(key);
  }
}
if(!pass){
	window.status="you must fill required fields: " + notFilled.toString() ;
	document.getElementById("submitButton").disabled=true;
}else{
	window.status="";
	document.getElementById("submitButton").disabled=false;
}
}


var validate_functions = new Array(0);	

function add_validator(validator){
	validate_functions.push(validator);
}

function validate_form(){
    var pass = true;
    var errors = new Array(0);	    
    for(var i = 0; i < validate_functions.length;i++){
	var validator = validate_functions[i];
	var valid_state = validator();
	if(valid_state!=null){
		pass = false;
		errors.push("\n" + valid_state.toString());
	}
    }    
    
    if(!pass){
	var message = "Validation failed:" + errors.toString() ;		
	window.status= message;
	alert(message)
	return false;
    }else{
	window.status="";
	return true;
    }
}

function isValidTime(timeStr) {
    if (timeStr == null || timeStr == "") {
       return true;
    }
    
    var timePat = /^(\d{1,2}):(\d{2})(:(\d{2}))?(\s?(AM|am|PM|pm))?$/;
    
    var matchArray = timeStr.match(timePat);
    if (matchArray == null) {
       return false;
    }
    hour = matchArray[1];
    minute = matchArray[2];
    second = matchArray[4];
    ampm = matchArray[6];
    
    if (second=="") { second = null; }
    if (ampm=="") { ampm = "AM" }
    
    if (hour < 0  || hour > 23) {
       return false;
    }
    if (minute<0 || minute > 59) {
       return false;
    }
    if (second != null && (second < 0 || second > 59)) {
       return false;
    }
    return true;
}


function isValidEmail(id, required) {
  var element = document.getElementById(id);
  var value = element.value;	
  if(value!=null && value.length<1){
		if(required){
			return id + " must be filled.";
		}
  }else if(value.search(/\w+/)==-1){
		if(required){
			return id + " must be filled.";
		}
  }else if(value.search(/[^A-Za-z0-9_\-\.@]+/)!=-1){
    	element.focus();
		return id + " is not valid, it contains some invalid characters.";
  }else if(value.search(/[@]+/)==-1){
    	element.focus();
		return id + " is not valid, it does not contain @.";
  }  
  
}



function show_info(message) {
   var str_buffer = new String (
   		"<html>"+
			"<head>" +
				"<link href='/polarion/css/polarion/pol_default.css' type='text/css' rel='stylesheet'/>"+
				"<title>Info</title>"+
			"</head>"+
			"<body bgcolor='white'>"+
				"<table border='0' width='100%'>"+
					"<tr>" +
						"<td align='center' class='pb pt' style='font-family: Arial, Helvetica, sans-serif;font-size: 8pt;'>"+ message +"</td>" +
					"</tr>"+
				"</table>"+
				"<table border='0' width='100%'>"+
					"<tr>" +
						"<td align='center'>" +
							"<input type='sumbit' onClick='window.close()' value='Close' class='actionButton' style='text-align:center;padding:3;'>" +
						"</td>" +
					"</tr>"+
				"</table>"+
			"</body>"+
		"</html>"
   );

   var vWinCal = window.open("", "Info", 
                             "width=300,height=100,status=no,resizable=no,top=200,left=200");
   var doc = vWinCal.document;
   doc.write (str_buffer);
   doc.close();
}

function show_format_info(formatname, format,examplename, example) {
   var str_buffer = new String (
        "<table>\n" +
        "   <tr>\n" +
        "       <td align='right' style='font-family: Arial, Helvetica, sans-serif;font-size: 8pt;'>\n" +
        "           "+formatname+"\n" +
        "       </td>\n" +
        "       <td style='font-family: Arial, Helvetica, sans-serif;font-size: 8pt;'>\n" +
        "           "+format+"\n" +
        "       </td>\n" +
        "   </tr>\n" +
        "   <tr>\n" +
        "       <td align='right' style='font-family: Arial, Helvetica, sans-serif;font-size: 8pt;'>\n" +
        "           "+examplename+"\n" +
        "       </td>\n" +
        "       <td style='font-family: Arial, Helvetica, sans-serif;font-size: 8pt;'>\n" +
        "           "+example+"\n" +
        "       </td>\n" +
        "   </tr>\n" +
        "</table>\n"
   );
   show_info(str_buffer);
}

/*
 * Function disables all elements (buttons, inputs, textareas, ...) of the form
 */
function disableForm(form) {
	var elems = form.elements;
	for(var i = 0; i < elems.length; i++) {
		if(elems[i].type == "submit") {
			elems[i].disabled = true;
		} else {
			elems[i].readOnly = true;
		}
	}
}

function submitForm(submitButton) {
	var elems = submitButton.form.elements;
	for(var i = 0; i < elems.length; i++) {
		if(elems[i].type == "hidden" && elems[i].id == "eventSubmit") {
			elems[i].name = submitButton.name;
		} else if(elems[i].type == "submit") {
			elems[i].disabled = true;
		} else {
			elems[i].readOnly = true;
		}
	}
	if(submitButton.form.submit) {
		submitButton.form.submit();
	}
}
