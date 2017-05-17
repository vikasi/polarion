<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>
<%@page import="com.polarion.portal.tomcat.internal.auth.SSOPageDispatcher" %>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<head>
  <%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  <meta name="apple-itunes-app" content="app-id=600778564">  
  <title></title>

  <link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/ria/css/ssoLandingPage.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
</head>

<body onresize="hideCompanyLogo()" onload="hideCompanyLogo()" style="background-image: url(<%= System.getProperty("login.background", "/polarion/ria/images/login/background.jpg?buildId="+BootPlugin.getPolarionBuildNumber()) %>)" id="background">
	<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
	
	<div id="companyAndContainer">
	<% 
		String companylogo = System.getProperty("login.companylogo");
		if (companylogo  != null) {
	%>	
		<img src="<%= companylogo %>" alt="YourCompanyLogo" id="companyLogo"/>
  	<% } %>
  	
  	<%
  		String messageType = (String) session.getAttribute(SSOPageDispatcher.PARAM_MESSAGE_TYPE);
  		boolean messageEscape = session.getAttribute(SSOPageDispatcher.PARAM_MESSAGE_ESCAPE) != null;
  		String message = (String) session.getAttribute(SSOPageDispatcher.PARAM_MESSAGE);
  		if (message == null) {
  			response.sendRedirect("/polarion/");
  		} 
  	%>
	
		<div class="container" id="container">
			<div id="message" class="<%= messageType %>">
				<div id="messageText"><%= messageEscape ? EscapeChars.forHTMLTag(message) : message %></div>
			</div>
		</div>
	</div>
	
	<% 
		session.removeAttribute(SSOPageDispatcher.PARAM_MESSAGE_TYPE);
		session.removeAttribute(SSOPageDispatcher.PARAM_MESSAGE_ESCAPE);
		session.removeAttribute(SSOPageDispatcher.PARAM_MESSAGE);
	%>
</body>
</html>
	
