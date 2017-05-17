<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
	<head>
		<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
  		<link href="/polarion/ria/css/richtext.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" rel="stylesheet" type="text/css"/>
  	</head>
	<body id="rte_init">
		<div style="height:100%" >
			<table style="width:100%;height:100%">
				<tr>
					<td align="center" valign="middle">
						<img src="/polarion/ria/images/progress.gif" alt="progress"/>
						<br/>
						<span><%= com.polarion.platform.i18n.Localization.getString("definition.loadingBig") %></span>
					</td>
				</tr>
			</table>
		</div>	
	</body>
</html>