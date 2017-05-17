<%@page import="com.polarion.platform.i18n.Localization"%>
<!DOCTYPE html>
<% request.getSession().invalidate(); %>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Connect to ${applicationName}</title>
<link type="text/css" href="<%=request.getContextPath()%>/oauth/stylesheets/polarion.css" rel="stylesheet" />
</head>

<body>
	<div id="background">
		<img src="/polarion/ria/images/login/background.jpg" class="stretch" alt="">
	</div>
	
	<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
	
	<div id="container">
		<div id="box">
			<div id="header">
				<div>Siemens PLM Software</div>
				<div id="productName">Polarion<sup style=\"line-height: 0;\">&#174;</sup> ALM<sup style=\"line-height: 0;\">&#8482;</sup></div>
			</div>
			<div id="login">
				<form id="loginForm">
					<div><%=Localization.getString("oslc.adminLogin")%></div>
					<div><a href="${callback}&logout=true"><%=Localization.getString("login.logIn")%></a></div>
				</form>
			</div>
		</div>
	</div>


</body>

</html>
