<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.psvn.launcher.CoordinatorApplication"%>
<%@page import="com.polarion.cluster.coordinator.internal.MonitoringData"%>
<%@page import="com.polarion.cluster.coordinator.internal.MonitoringData.Instance"%>
<%@page import="com.polarion.cluster.coordinator.internal.MonitoringData.Node"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<head>
  <%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  <meta name="apple-itunes-app" content="app-id=600778564">  
  <link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="monitoring.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
  <script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
  <title><%=Localization.getString("coordinator.monitoring.title")%></title>
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
	
		<div id="container">
			<div id="box">
			
			<div id="heading"><%=Localization.getString("coordinator.monitoring.title")%></div>
			
			<%
				MonitoringData data = CoordinatorApplication.getInstance().getMonitoringData();
				List<Instance> instances = data.getInstances();
			%>
			
				<% if (data.hasError()) { %>
					<div class="error"><%=Localization.getString("coordinator.monitoring.error")%></div>
				<% } %>		
			
				<% if (!instances.isEmpty()) { %>
				<table>
				
					<tr>
						<th><%=Localization.getString("coordinator.monitoring.column.name")%></th>  
						<th><%=Localization.getString("coordinator.monitoring.column.url")%></th>  
						<th></th>
					</tr>
				
					<% for(int i = 0; i < instances.size(); i+=1) { %>
						<%
							Instance instance = instances.get(i);
							List<Node> nodes;
							if (instance.isCluster()) {
								nodes = instance.getNodes();
							} else {
								nodes = Collections.emptyList();
							}
						%>
						<tr>
							<td>
							    <img src="/polarion/ria/images/monitoring/<%=instance.isCluster() ? "cluster" : "node"%>-<%=instance.isActive() ? "active" : "inactive"%>.png">
							    <%=instance.getLabel()%>
							</td>  
							<td>
								<a class="polarionLink" href="<%=instance.getUrl()%>" target="_blank"><%=instance.getUrl()%></a> 
							</td>
							<td>
							    <% if (instance.getLoadBalancerManagerUrl() != null) { %>
								<a class="polarionLink" href="<%=instance.getLoadBalancerManagerUrl()%>" target="_blank"><%=Localization.getString("coordinator.monitoring.balancerManager")%></a>
								<% } %> 
							</td>
						</tr>
						
				   		<% for(int j = 0; j < nodes.size(); j+=1) { %>
							<%
								Node node = nodes.get(j);
							%>
							<tr>
								<td class="nodeName">
								    <img src="/polarion/ria/images/monitoring/node-<%=node.isActive() ? "active" : "inactive"%>.png"> 
								    <%=node.getLabel()%>
								</td>  
								<td>
								    <% if (node.getUrl() != null) { %>
									<a class="polarionLink" href="<%=node.getUrl()%>" target="_blank"><%=node.getUrl()%></a>
									<% } %> 
								</td>  
								<td>
								</td>
							</tr>
						<% } %>
						
					<% } %>
				</table>
			<% } else if (!data.hasError()) { %>
				<%=Localization.getString("coordinator.monitoring.noInstance")%>
			<% } %>
			<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>
			</div>
		</div>
	</div>

</body>
</html> 
