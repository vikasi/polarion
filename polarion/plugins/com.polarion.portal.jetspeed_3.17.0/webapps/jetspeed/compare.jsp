<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
	<head>
	  		<style type="text/css">
				html{
					height:100%;
					overflow:hidden;
				}
			</style>
		<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
	</head>
	<body style="overflow:hidden;height:100%">
			<div id="container_div" style="height:100%" >
			<table style="width:100%;height:100%">
				<tr>
					<td align="center" valign="middle">
						<img src="/polarion/ria/images/progress.gif" alt="progress"/>
						<br/>
						<span style="font-family: Arial;font-size: 13px;"><%= com.polarion.platform.i18n.Localization.getString("definition.loadingBig") %></span>
					</td>
				</tr>
			</table>
		</div>
		
	</body>
</html>