<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="com.polarion.portal.login.LoginData"%>
<%@page import="com.polarion.portal.tomcat.session.PolarionSingleSignOn"%>
<%@page import="com.polarion.portal.tomcat.internal.auth.LoginPageDispatcher"%>
<%@page import="com.polarion.core.boot.BootPlugin"%><%
if (com.polarion.portal.tomcat.auth.PolarionAuthenticator.usesProxySSO()) {
    application.getRequestDispatcher("/proxysso.jsp").forward(request, response);
    return;
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	  	<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
	  	<meta name="apple-itunes-app" content="app-id=600778564" />  
	  	<link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	  	<link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	  	<link rel="stylesheet" type="text/css" href="/polarion/login.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	  	<script src="/polarion/ria/javascript/jquery-3.0.0.min.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>"></script>
	  	<script src="/polarion/ria/javascript/flash_detect.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
		<script>
			setFlashMessages("<%= EscapeChars.forJavascriptString(Localization.getString("login.flashWarning.higherVersionOfFlashRequired", "%requiredMajorVersion%")) %>", "<%= EscapeChars.forJavascriptString(Localization.getString("login.flashWarning.yourVersionIs", "%currentVersion%")) %>")
	  	</script>
	  	<script src="/polarion/login.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" id="loginScript" type="text/javascript"></script>
	  	<script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
	  	<title>Login</title>  
	</head>
	<body onresize="hideCompanyLogo()" onload="hideCompanyLogo()" style="background-image: url(<%= System.getProperty("login.background", "/polarion/ria/images/login/background.jpg?buildId="+BootPlugin.getPolarionBuildNumber()) %>)" id="background">
	
		<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
	
		<div id="companyAndContainer">
			<% String companylogo = System.getProperty("login.companylogo");
				if (companylogo  != null && !LoginData.getInstance().isMultiInstance()) {%>	
					<img src="<%= companylogo %>" alt="YourCompanyLogo" id="companyLogo"/>
	  		<% } %>
	  		
			<div id="container">
				<div id="warning" class="hidden">
					<ul>
						<li id="warningInfo"><%= Localization.getString("login.warningInfo") %><span id="moreinfo"><%= Localization.getString("login.moreInfo") %></span></li>
					
						<%
		    				if (!com.polarion.portal.tomcat.SupportedBrowsers.isSupported(request)) {
						%>
						<li><%= Localization.getString("login.browserNotFullySupported") %></li>
						<% } %>
					
						<%
		    				if (com.polarion.portal.tomcat.SupportedBrowsers.showWarningAboutIECompatibilityView(request)) {
						%>
						<li><%= Localization.getString("login.ieInCompatibilityView") %></li>
						<% } %>
					
					</ul>
					<span id="lessinfo"><%= Localization.getString("login.lessInfo") %></span>
				</div>
			
				<%	if (request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) != null) { %>
					<div id="errorMessage">
						<%= request.getAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE) %>
						<script type="text/javascript">switchLoginLabel();</script>
						<% request.removeAttribute(LoginPageDispatcher.LOGIN_FAILURE_ATTRIBUTE); %>
					</div>
				<% } %>
		
				<div id="box">
					<div id="header">
						<div id="companyName"><%=Localization.getString("login.companyName")%></div>
						<div id="productName"><%=BootPlugin.getPolarionProductNameHtml()%></div>
					</div>
				
					<form method="post" id="logInForm" action="<%= response.encodeURL("j_security_check") %>" enctype="application/x-www-form-urlencoded" onsubmit="return login();">
						<% 
							String target = (String) request.getParameter("target");
							target = target == null ? "" : target; 
						%>
						<input id="target" name="target" type="hidden" value="<%= target %>"/>
						<div id="loginRow">
							<div id="polarionLogo">
								<% if (!LoginData.getInstance().isMultiInstance()) {%>
									<img src="/polarion/ria/images/logos/repo_logo.png" class="logo" />
								<% } else { %>
									<img src="<%= System.getProperty("com.polarion.logoURL", "/polarion/ria/images/logos/repo_logo.png") %>" class="logo" />
								<% } %>	
							</div>
	
							<div id="login">							
								<%	if (getServletContext().getAttribute("registerMessage") != null) { %>
									<span class="registerMessage"><%= getServletContext().getAttribute("registerMessage") %></span>
									<% getServletContext().removeAttribute("registerMessage"); %>
				    			<% } %>
				                <div id="username">
				                	<label for="j_username" class="hidden"><%= Localization.getString("login.userName") %></label>
				                	<input id="j_username" name="j_username" size="12" type="text" class="input" />
				                </div>
				                
				                <div id="password">
				                	<label for="j_password" class="hidden"><%= Localization.getString("login.password") %></label>
				                	<input id="j_password" name="j_password" size="12" value="" type="password" class="input" />
				                </div>
							</div>
						</div>
						<div id="loginRow">
							<div id="serverName">
								<% if (LoginData.getInstance().isMultiInstance()) { %>
									<% String clusterLabel=EscapeChars.forJavascriptString(LoginData.getInstance().getClusterLabel()); %>
									<span title="<%= clusterLabel %>"><%= clusterLabel %></span>
								<% } %>
							</div>
							<div id="submit">
								<label id="submitMsg" class="hidden" for="submitButton"><%= Localization.getString("login.loggingIn") %></label>
	                			<input id="submitButton" name="submit" type="submit" value="<%= Localization.getString("login.logIn") %>" />
	                			<label id="rememberme"><input name="rememberme" value="true" type="checkbox" /><span class="remember"><%= Localization.getString("login.stayLoggedIn") %></span></label>
							</div>
						</div>
					</form>
					<div id="options">
						<% boolean enableCreateAccountForm = Boolean.getBoolean("enableCreateAccountForm");%>
						<% String resetPasswordLinkURL = System.getProperty("com.polarion.ui.login.resetPasswordLinkURL");%>
						<% boolean isMultiInstance = LoginData.getInstance().isMultiInstance();%> 
						<% if (enableCreateAccountForm || isMultiInstance || resetPasswordLinkURL!=null) { %>
					    	<% if (enableCreateAccountForm) {	%>
								<a class="polarionLink" href="<%= System.getProperty("createAccountFormUrl", "/polarion/register/") %>"><%= Localization.getString("login.createAccount") %></a>
							<% } %>
							<% if (isMultiInstance) { %>
								<% if (enableCreateAccountForm) {	%>
							        &nbsp;&nbsp;
							   	<% } %>
							   	<a class="polarionLink" href="<%= LoginData.getInstance().getChangeServerUrl() %>"><%= Localization.getString("login.changeServer") %></a>
							<% } %>
							<% if(resetPasswordLinkURL!=null) { %>
								<% if (enableCreateAccountForm || isMultiInstance) {	%>
									&nbsp;&nbsp;
						    	<% } %>
						    	<a class="polarionLink" href="<%= resetPasswordLinkURL %>"><%= System.getProperty("com.polarion.ui.login.resetPasswordLinkLabel") %></a>
							<% } %>
						<% } %>
					</div>
				
					<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>
		    	</div>
			</div>
		</div>
		<% if (System.getProperty("google.analytics.number") != null ) { %>
			<script type="text/javascript">
				var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
				document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
				try {
					var pageTracker = _gat._getTracker("<%out.print(System.getProperty("google.analytics.number"));%>");
					pageTracker._trackPageview("/polarion/login.jsp");
				} catch(e) {
				// nothing to do
				}
			</script>
		<% } %>

		<iframe src="/polarion/ria/prefetch.jsp" id="prefetch"></iframe> 
	</body>
</html> 
