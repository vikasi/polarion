<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.psvn.launcher.CoordinatorApplication"%>
<%@page import="com.polarion.cluster.info.ClusterInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

	<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  	<meta name="apple-itunes-app" content="app-id=600778564" />  
  	<link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  	<link rel="stylesheet" type="text/css" href="/polarion/welcome.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  	<link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	<script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
	<title>Welcome</title>

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
		
		<div id="container">
			<div id="box">
			<div id="title"><%= Localization.getString("login.changeServer") %> </div>
				<div id="nav">
					<%
		    			List<ClusterInfo> clusters = CoordinatorApplication.getInstance().getActiveClusters();
		    			if (clusters.isEmpty()) {
					%>
					<span class="noInstances"><%= Localization.getString("login.noInstanceRunning") %></span>
					<% } else { %>
					<ul>
						<% 
							for(int i = 0; i < clusters.size(); i++) {
							ClusterInfo cluster = clusters.get(i);
						%>
						<li>
							<a href="<%= cluster.getBaseUrl() %>/polarion">
								<img src="<%= cluster.getIcon() %>" class="icon" />
								<span class="item">
									<span class="label"><%= cluster.getLabel() %></span>
									<span class="desc"><% if (cluster.getDescription() != null) { %><%= cluster.getDescription() %><% } %></span>
								</span>
								<img src="/polarion/ria/images/login/arrowRight.png" class="arrow" />
							</a>
						</li>
						<% } %>
					</ul>
					<% } %>
				</div>
				<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>
		    </div>
	    </div>
    </div>
</body>
</html> 
