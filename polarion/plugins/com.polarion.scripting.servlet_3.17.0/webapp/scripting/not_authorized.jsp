<%@ page contentType="text/html; charset=UTF-8" %>   
<%@page import="com.polarion.platform.i18n.Localization"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
       "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
   <title>Polarion Scripting Interface: Error</title>
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
	<font color="red"><b><%= Localization.getString("scripting.notAuthorized") %></b></font>
</body>
</html>
