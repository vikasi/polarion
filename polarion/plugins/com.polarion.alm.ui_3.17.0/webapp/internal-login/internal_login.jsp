<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>
<%@page import="com.polarion.portal.tomcat.session.PolarionSingleSignOn"%>
<%@page import="com.polarion.portal.tomcat.internal.auth.LoginPageDispatcher"%>

<%if (!PolarionSingleSignOn.getInstance().requiresInternalLogin(request)) {%>
<%@ include file="internal_login_success.jsp" %>
<% return; } %>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  <link rel="stylesheet" type="text/css" href="/polarion/login.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/internal-login/internal_login.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <script src="/polarion/ria/javascript/jquery-3.0.0.min.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>"></script>
  <script src="/polarion/login.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" id="loginScript" type="text/javascript"></script>
  <title>Login</title>
</head>
<body id="internalBody">
  	<div id="internalHeader">
  		<img src="/polarion/ria/images/logos/small/repoLogo.png" class="logo" />
	</div>
	<div id="internalLogin">
		<%	if (request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) != null) { %>
		<span class="loginFailure" style="color:#ff0000;"><%= request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) %></span>
		<script type="text/javascript">switchLoginLabel();</script>
		<% request.removeAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE); %>
		<% } %>
		
		<script type="text/javascript">
		
		 <% 
		 boolean isChromeOrEdge = com.polarion.portal.tomcat.SupportedBrowsers.isChromeOrEdge(request);
		 if (isChromeOrEdge) { 
		 %> 
		 	setTimeout(function(){ 
		    	//prevents chrome autofill 
		        document.getElementById('j_username').value='';
		        document.getElementById('j_username').blur(); 
		    	document.getElementById('j_username').focus(); 
		    }, 100);  
		 <% } %> 
        </script>
            
		<form method="post" action="<%= response.encodeURL(LoginPageDispatcher.INTERNAL_LOGIN_CONTEXT + "/j_security_check") %>" enctype="application/x-www-form-urlencoded" onsubmit="return login();">
			<% 
				String target = (String) request.getParameter("target");
				target = target == null ? "" : target; 
			%>
			<input id="target" name="target" type="hidden" value="<%= target %>"/>
	        <div id="username" style="padding: 12px 0px;">
	        	<label for="j_username" class="hidden"><%= Localization.getString("login.userName") %></label>
	            <input id="j_username" name="j_username" size="12" type="text" class="input" <% if(isChromeOrEdge) { %> value="Username" <% } %>/>
	       	</div>
	                
	        <div id="password">
	        	<label for="j_password" class="hidden"><%= Localization.getString("login.password") %></label>
	            <input id="j_password" name="j_password" size="12" value="" type="password" class="input" />
	        </div>
	                
	        <div id="internalSubmit">
	        	<label id="submitMsg" class="hidden" for="submitButton"><%= Localization.getString("login.loggingIn") %></label>
	            <input id="submitButton" name="submit" type="submit" value="<%= Localization.getString("login.logIn") %>" />
	            <label id="rememberme"><input name="rememberme" value="true" type="checkbox" /><span class="remember"><%= Localization.getString("login.stayLoggedIn") %></span></label>
	        </div>
	                
		</form>
	</div>
</body>
</html> 
