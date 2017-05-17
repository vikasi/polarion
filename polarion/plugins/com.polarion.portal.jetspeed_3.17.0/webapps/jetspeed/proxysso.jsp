<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
  <link rel="shortcut icon" href="/polarion/ria/images/favicon.ico" />
  <title>Proxy Single Sign On</title>
  </head>
  <body>
<style>
/** LOGIN **/
BODY,HTML{
	background-color: #5a9fd0;
	height:95%;
	padding-top:20px;
	padding-bottom:50px;
}
TD {
	font-family: verdana;
	font-size: 11px;
}
.input {
	width: 263px;
	height: 22px;
	padding: 6px 3px 7px 10px;
	color: #0E4075;
	font-size: 18px;
	font-family: Arial,Helvetica,sans-serif;
	background: #FCFCFC;
	border: 1px solid #7f9db9;

}



.main{
	width:100%;
}

.container{
	background-color: white;
	width:600px;
	padding:20px;
}

#createbutton{
	width: 278px;
	height: 42px;
	padding: 0px;
	border: 1px solid #89adce;
	color: #00346d;
	text-transform: uppercase;
	font-weight: bold;
	background: #D1E4F2 url("/polarion/ria/images/backgrounds/bg_form_btn.gif") repeat-x scroll 0 0;
	cursor: pointer;
}
.name{
	width: 130px;
	padding-right: 15px;
	text-align: right;
	font-weight: bold;
	font-size: 12px;
}

#info{
	background: white;
	color:black;
	padding:10px;
	border:2px solid red;
	margin-top:20px;
	width:300px;
}


#top_img{
	margin-bottom:0px;
}

#heading {
	width: 550px;
	font-size: 23px;
	font-weight:bold;
	font-family:arial;
	text-align: center;
	border-bottom: 2px white solid;
	margin-top: 7px;
}
.heading2{
	width: 500px;
	font-size: 14px;
	font-weight: bold;
	color:#00ADF5;
	padding-bottom:2px;
	padding-top:15px;
	border-bottom:4px solid #F8F8F8;
}
.headingSub{
	padding-top:10px;
	color:#A1A1CA;
}
.headingSub a{
	color:#A1A1CA;
}
#info{
	background: white;
	color:black;
	padding:10px;
	border:2px solid red;
	margin-top:20px;
	width:300px;
}
</style>
<center>
<div class="container">
<table class="fixed main" cellpadding="0" cellspacing="0" border="0">
<%	if (session.getAttribute("loginFailure") != null) { %>
<tr>
	<td align="center" style="padding-top:0px;color:red;font-weight:bold;">
		<%= request.getSession().getAttribute("loginFailure") %>
		<% request.getSession().removeAttribute("loginFailure"); %>
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
<% } else { %>
<tr>
    <td valign="top" align="center" style="font-weight:bold;"><%= Localization.getString("core.message.youHaveBeenLoggedOut")%></td>
</tr>
<% } %>
<tr><td>&nbsp;</td></tr>
<tr>
    <td valign="top" align="center" style="font-weight:bold;"><a href="<%= request.getContextPath()%>"><%= Localization.getString("definition.home")%></a></td>
</tr>
</table>
</div>
</center>

</body>
</html>
