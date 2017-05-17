
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<head>
  <%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  <meta name="apple-itunes-app" content="app-id=600778564">  
  <title><%= request.getParameter("title")%></title>

  <link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/activate/activate.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <script src="/polarion/ria/javascript/jquery-3.0.0.min.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>"></script>
  <script src="/polarion/ria/javascript/jquery.xdomainrequest.min.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>"></script> 
  <script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
  <script>
  	$(document).ready(function(){
	    $('input[id=submitButton]').on('click', function() {
	        doSubmit();
	    });

		$(window).keydown(function(event){
			if(event.keyCode == 13) {
				doSubmit();
				return false;
			}
		});
		
	    validate();
	    checkConditions();
	});
	
	function checkConditions() {
		var isOnlineActivation = <%= Boolean.valueOf(request.getParameter("onlineActivation")) %>;
		var ua = navigator == null ? '' : navigator.userAgent.toLowerCase();
    	if (isOnlineActivation && ua.indexOf('msie') != -1 && ua.indexOf('trident/5.0') != -1 && window.location.protocol == 'http:') {
    		var message = {content: "<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.activationNotSupportedOnIE9", "/polarion/activate/offline")) %>", html: true};
			setMessageText(message, true);
			$(".panel").hide();
    	}
	}
	
	function doSubmit() {
		if (validate()) {
		    setMessage("", false);
	        showActivating(true);
	        submitForm();
		}
	}

	function handleResult(result) {
		if (result.activated) {
			var message = {content: "<%= request.getParameter("successLabel")%>", html: true};
			setMessageText(message, false);
			$(".panel").hide();
		} else {
			setMessageText(result.message, true);
		}
	}
	
	function setMessageText(messageText, error) {
		if (messageText) {
			if (messageText.html) {
				$("#messageText").html(messageText.content);
			} else {
				$("#messageText").text(messageText.content);
			}
			$("#message").css('display', 'block');
		} else {
			$("#messageText").text("");
			$("#message").css('display', 'none');
		}
		$("#message").attr("class", error ? "messageError" : "messageInfo");
	}
	
	function setMessage(message, error) {
		var messageText = null;
		if (message) {
		    messageText = {content: message};
		}
		setMessageText(messageText, error);
	}

	function showActivating(start) {
		setSubmitEnabled(!start);
		
		var submitLabel;  
		if (start) {
			submitLabel = "<%= request.getParameter("submittingLabel")%>";
		} else {
			submitLabel = "<%= request.getParameter("submitLabel")%>";
		}
		$("#submitButton").attr("value", submitLabel);
	}
	
	function setSubmitEnabled(enabled) {
	    if (enabled) {
	    	$("#submitButton").removeAttr("disabled");
	    } else {
	    	$("#submitButton").attr("disabled", "disabled");
	    }
	}
  </script>
</head>

<body onresize="hideCompanyLogo()" onload="hideCompanyLogo()" style="background-image: url(<%= System.getProperty("login.background", "/polarion/ria/images/login/background.jpg?buildId="+BootPlugin.getPolarionBuildNumber()) %>" id="background">
	<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
	
	<div id="companyAndContainer">
	<% 
		String companylogo = System.getProperty("login.companylogo");
		if (companylogo  != null) {
	%>	
		<img src="<%= companylogo %>" alt="YourCompanyLogo" id="companyLogo"/>
  	<% } %>
	
	<div class="<%= (request.getParameter("containerClass") == null ? "container" : request.getParameter("containerClass")) %>" id="container">
		<div id="message" class="messageError" style="display: none;" >
			<table>
			 <tbody>
			  <tr>
			   <td>
			    <img src="/polarion/ria/images/login/exmark_login.png?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" class="infoIcon">
			   </td>
			   <td>
			    <div id="messageText"></div>
			   </td>
			  </tr>
			 </tbody>
			</table>
		</div>

		<div class="panel">
