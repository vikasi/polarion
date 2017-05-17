<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
		
		<script src="/polarion/ria/javascript/jquery-3.0.0.min.js" type="text/javascript"></script>
  		
		<script src="/polarion/ria/javascript/highcharts.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/highcharts-more.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/highcharts-3d.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/data.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/exporting.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/drilldown.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/funnel.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/heatmap.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/no-data-to-display.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/solid-gauge.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
  		
  		<script src="/polarion/ria/javascript/diff/dojo/dojo.js" type="text/javascript"></script>
  		<script src="/polarion/ria/javascript/diff/diff.js" type="text/javascript"></script>
  		<script type="text/javascript">try{htmlDiffInit();}catch(e){}</script>
  		
  		<style>
  			<%= com.polarion.core.config.Configuration.getInstance().ui().listStyle().getStyle() %>
  		</style>  
  		
  		<link href="/polarion/gwt/gwt/polarion/rpe_print.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" rel="stylesheet" type="text/css"/>
  		<link href="/polarion/ria/font-awesome-4.0.3/css/font-awesome.css" rel="stylesheet">  		
  	</head>
	<body id="polarion-rpe-print-id" class="polarion-rpe-view polarion-rpe-content">
	</body>
</html>
