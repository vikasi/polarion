<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.portal.internal.server.register.RegisterServlet"%>
<%@page import="com.polarion.portal.login.LoginData"%>
<%@page import="com.polarion.core.boot.BootPlugin"%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  	<head>
		<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>  
	  	<link rel="shortcut icon" href="/polarion/ria/images/favicon.ico?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	  	<link rel="stylesheet" type="text/css" href="/polarion/ria/css/loginPages.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
	  	<link rel="stylesheet" type="text/css" href="createUser.css?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" />
		<script src="/polarion/ria/javascript/loginPages.js?buildId=<%= BootPlugin.getPolarionBuildNumber() %>" type="text/javascript"></script>
	  	<title>Register</title>
  	</head>
  	<body onresize="hideCompanyLogo()" onload="hideCompanyLogo()" style="background-image: url(<%= System.getProperty("login.background", "/polarion/ria/images/login/background.jpg?buildId="+BootPlugin.getPolarionBuildNumber()) %>" id="background">
  		<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
  		
  		<div id="companyAndContainer">
			<% 	String companylogo = System.getProperty("login.companylogo");
				if (companylogo  != null && !LoginData.getInstance().isMultiInstance()) {
			%>	
				<img src="<%= companylogo %>" alt="YourCompanyLogo" id="companyLogo"/>
	  		<% } %>
	
			<div class="container" id="container">
			<table class="fixed main" cellpadding="0" cellspacing="0" border="0"  >
			<tbody>
			<tr>
				<% if (!LoginData.getInstance().isActivated()) { %>
					<td valign="top" align="center" style="font-weight:bold;"><%= Localization.getString("register.cannotUseBeforeActivation") %></td>
				<% } else if(RegisterServlet.isRegistrationEnabled()){	%>
				<% 		if (RegisterServlet.areThereFreeSlots()) { %> 
					<td align="center" height="100%" valign="middle" width="100%">
				<form name="registrationForm" id="registrationForm" method="POST" action="/polarion/register/execute" style="margin: 0; padding: 0;">
					<input type="hidden" name="destination"  id="destination"  value="" />
	    			<table cellpadding="8" cellspacing="0" border="0">
	    				<% 		if (request.getAttribute("registerErrorMessage") != null) { %>
						<tr>
	    					<td colspan="2" align="center" style='color:red;font-weight:bold;'>
								<%= request.getAttribute("registerErrorMessage") %>
	    					</td>
	    				</tr>
						<%		} %>
	    				<tr>
	    					<td colspan="2">
	    						<div id="heading">Create new user account</div>
	    					</td>
	    				</tr>
						<tr>
	    					<td colspan="2" align="left">
	    						<div class="heading2">Account information</div>
								<div class="headingSub">Please use only letters and numbers in these fields:</div>
	    					</td>
	    				</tr>
	    				<tr>
	    					<td class="name" align="right" width="50%">
	    						User name:
	    					</td>
	    					<td align="left"><input id="username" name="username" size="12" value="" maxlength="25" type="text"  class="input"/></td>
	    				</tr>
						<tr>
	    					<td class="name" align="right">
								Password:
							</td>
							<td align="left"><input size="12" value="" id="password" name="password" maxlength="25" class="input" type="password"></td>
							
	    				</tr>
						<tr>
	    					<td class="name" align="right">
								Repeat password:
							</td>
							<td align="left"><input size="12" value="" id="password2" name="password2" maxlength="25" class="input" type="password"></td>
							
	    				</tr>
						<tr>
	    					<td  colspan="2" align="left">
	    						<div class="heading2">Personal information</div>
	    					</td>
	    				</tr>
	    				<tr>
	    					<td class="name" align="right">
	    						Full name:
	    					</td>
	    					<td align="left"><input id="fullname" name="fullname" size="30" value="" maxlength="500" type="text"  class="input"/></td>
	    				</tr>
	    				<tr>
	    					<td  class="name" align="right">
	    						E-mail:
	    					</td>
	    					<td align="left"><input name="email" size="30" value="" maxlength="500" type="text"  class="input"/></td>
	    				</tr>
	    				<tr>
	    					<td colspan="2">&nbsp;</td>
	    				</tr>
						</tr>
							<td></td>	
							<td align="left" style="padding-top:5px">
								<input id="createbutton" name="submit" type="submit" value="Create Account" onclick="return validateForm();">
							</td>
						</tr>
									
	    			</table>
				</form>
			</td>
			
	<% 		} else {%>
				<td valign="top" align="center" style="font-weight:bold;padding-right:25px;padding-bottom:10px;"><%= RegisterServlet.getNoMoreFreeSlotsMessage() %></td>
	<%		} %>
	<% } else { %>
				<td valign="top" align="center" style="font-weight:bold;padding-right:25px;padding-bottom:10px;"><%= Localization.getString("register.createAccountDisabled") %></td>
	<% } %>		
		</tr>
		
	</tbody></table>
		<div id="options">
			<a class="polarionLink" href="/polarion"><%= Localization.getString("login.backToLogin.title") %></a>
		</div>
		<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>
	</div>
</div>

</body>

<% if(RegisterServlet.isRegistrationEnabled()){	%>
<script type="text/javascript">
	function validateForm(){
		try{
    		var minimalPasswordLength = <%= System.getProperty("minimalPasswordLength", "0")%>;
    		var password = document.getElementById("password").value;
    		var password2 = document.getElementById("password2").value;
    		if (document.getElementById("username").value.length == 0) {
    			alert("<%= Localization.getString("register.obligatoryUsernameField") %>");
    			return false;
    		}
    		if (password != password2){
    			alert("<%= Localization.getString("register.passwordsDoNotMatch") %>");
    			return false;
    		}
    		if (password.length < minimalPasswordLength) {
    			alert("<%= Localization.getString("register.passwordTooShort", System.getProperty("minimalPasswordLength", "0")) %>");
    			return false;
    		}
    		if (document.getElementById("fullname").value.length == 0) {
    			alert("<%= Localization.getString("register.obligatoryFullNameField") %>");
    			return false;
    		}
    		document.getElementById("registrationForm").submit();
    		
		}catch(e){
		
		}
	}
	try{
    	if(document.getElementById("username") != null){
    		document.getElementById("destination").value=""+document.location;
    		document.getElementById("username").focus();
    	}
	}catch(e){
	
	}
</script>
<% } %>

  </body>
</html> 
