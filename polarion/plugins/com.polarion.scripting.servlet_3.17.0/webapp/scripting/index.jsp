<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
 <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >      
<%@ page contentType="text/html; charset=UTF-8" %>              
<%@ page import="com.polarion.scripting.servlet.ScriptingBean,javax.script.ScriptEngineFactory" %>
<%@ page import="com.polarion.platform.i18n.Localization"%>
<jsp:useBean id="scripting" scope="request" class="com.polarion.scripting.servlet.ScriptingBean"/>
<% if(!scripting.isCurrentUserAuthorized()) {response.sendRedirect("not_authorized.jsp");} else if (!scripting.isEnabled()){response.sendRedirect("disabled.jsp");} %>

<head>
   <title><%= Localization.getString("scripting.title") %></title>
   <link rel="shortcut icon" href="/polarion/ria/images/favicon.ico" />
   <style>
       body,html {
           margin: 10px;
           padding: 0px;
           font-family: Arial, Arial, Helvetica, sans-serif;
           font-size: 13px;
       }
   </style>
</head>
<body>
    <h1><%= Localization.getString("scripting.title") %></h1>
	<form action="run" method="post">
		<table border="0">
			<tr><td><%= Localization.getString("scripting.engine") %></td>
				<td><select name="engine" size="1">
				    <% for (ScriptEngineFactory factory : scripting.getAvailableEngineFactories()) { %>
				        <option value="<%=factory.getNames().get(0)%>"><%=factory.getEngineName()%> (<%=factory.getLanguageName()%>)</option>
				    <% } %>
    				</select>
				</td>
			</tr>
			
			<tr><td><%= Localization.getString("scripting.waitForJob") %></td><td><input type="checkbox" name="wait" value="true" checked="checked"/></td></tr>
		</table>
		<p><%= Localization.getString("scripting.enterScriptToRun") %><br/>
		<textarea cols="100" rows="30" name="script"></textarea></p>
		<input type="submit" value="<%= Localization.getString("scripting.run") %>"/>
	</form>
</body>
</html>
