<% 
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setIntHeader("Expires", -1);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
	<head>
		<link rel="shortcut icon" href="/polarion/ria/images/favicon.ico"></link>
  		<title></title>
  	</head>
	<body onload="prefetchResources();" style="background:lightgray">
		<script type="text/javascript">
		    function prefetchJS(url){
		    	var element = document.createElement("SCRIPT");
		    	element.type = "text/javascript";
		    	element.src = url + "?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>";
		    	document.body.appendChild(element);
		    }
		    
		    function prefetchImg(url){
		    	var element = document.createElement("IMG");
		    	element.src = url + "?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"; 
		    	document.body.appendChild(element);
		    }
		    
		    function prefetchImgNoBuildId(url){
                var element = document.createElement("IMG");
                element.src = url; 
                document.body.appendChild(element);
            }
		    
		    function prefetchResources(){
		        
		        prefetchImgNoBuildId("/polarion/ria/images/backgrounds/navigator_bg.png");
		        prefetchImgNoBuildId("/polarion/ria/images/backgrounds/navigator_dark_bg.png");
		        
				prefetchImg("/polarion/ria/images/progress.gif");
				
				prefetchImg("/polarion/ria/images/error_occured.gif");
				prefetchImg("/polarion/ria/images/button_arrow.gif");
				prefetchImg("/polarion/ria/images/search_combo_erase.gif");
				
				prefetchImg("/polarion/ria/images/details/tracker.gif");
				prefetchImg("/polarion/ria/images/details/wiki_svn.gif");
				
				prefetchImg("/polarion/ria/images/topicIcons/workItems.png");
				prefetchImg("/polarion/ria/images/topicIcons/tests.png");
		        prefetchImg("/polarion/ria/images/topicIcons/testruns.png");
                prefetchImg("/polarion/ria/images/topicIcons/home.png");
                prefetchImg("/polarion/ria/images/topicIcons/builds.png");
                prefetchImg("/polarion/ria/images/topicIcons/baselines.png");
                prefetchImg("/polarion/ria/images/topicIcons/wiki2.png");
                prefetchImg("/polarion/ria/images/topicIcons/wiki1.png");
                prefetchImg("/polarion/ria/images/topicIcons/shortcuts.png");
                prefetchImg("/polarion/ria/images/topicIcons/repository.png");
                prefetchImg("/polarion/ria/images/topicIcons/project.png");
                prefetchImg("/polarion/ria/images/topicIcons/monitor.png");
                prefetchImg("/polarion/ria/images/topicIcons/index.png");
                prefetchImg("/polarion/ria/images/topicIcons/documentsAndWiki.png");
                prefetchImg("/polarion/ria/images/topicIcons/space.png");
                prefetchImg("/polarion/ria/images/topicIcons/document.png");
                prefetchImg("/polarion/ria/images/topicIcons/quality.png");
                prefetchImg("/polarion/ria/images/topicIcons/modules.png");
                prefetchImg("/polarion/ria/images/topicIcons/dashboard.png");
                prefetchImg("/polarion/ria/images/topicIcons/reports.png");
                prefetchImg("/polarion/ria/images/topicIcons/radar.png");
                prefetchImg("/polarion/ria/images/topicIcons/search.png");
                prefetchImg("/polarion/ria/images/topicIcons/administrationGlobal.png");
                prefetchImg("/polarion/ria/images/topicIcons/announcements.png");
                prefetchImg("/polarion/ria/images/topicIcons/backArrow.png");
                prefetchImg("/polarion/ria/images/topicIcons/help.png");
                prefetchImg("/polarion/ria/images/topicIcons/indexAndCache.png");
                prefetchImg("/polarion/ria/images/topicIcons/license.png");
                prefetchImg("/polarion/ria/images/topicIcons/notification.png");
                prefetchImg("/polarion/ria/images/topicIcons/portal.png");
                prefetchImg("/polarion/ria/images/topicIcons/project.png");
                prefetchImg("/polarion/ria/images/topicIcons/projectTemplate.png");
                prefetchImg("/polarion/ria/images/topicIcons/repository.png");
                prefetchImg("/polarion/ria/images/topicIcons/scheduler.png");
                prefetchImg("/polarion/ria/images/topicIcons/users.png");
                
                prefetchImg("/polarion/ria/images/topicIconsSmall/workItems.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/tests.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/testruns.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/home.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/builds.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/baselines.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/wiki2.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/wiki1.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/shortcuts.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/repository.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/project.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/monitor.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/index.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/documentsAndWiki.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/space.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/document.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/quality.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/modules.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/dashboard.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/reports.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/radar.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/search.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/administrationGlobal.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/announcements.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/backArrow.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/help.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/indexAndCache.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/license.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/notification.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/portal.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/project.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/projectTemplate.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/repository.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/scheduler.png");
                prefetchImg("/polarion/ria/images/topicIconsSmall/users.png");
                
                prefetchImg("/polarion/ria/images/details/quality_sub.gif");
                prefetchImg("/polarion/ria/images/details/reports_sub.gif");
                
                prefetchImg("/polarion/ria/images/documents/type_excel.gif");
                prefetchImg("/polarion/ria/images/documents/type_excel_invalid.gif");
                prefetchImg("/polarion/ria/images/documents/type_generic.gif");
                prefetchImg("/polarion/ria/images/documents/type_msproject.gif");
                prefetchImg("/polarion/ria/images/documents/type_msproject_invalid.gif");
                prefetchImg("/polarion/ria/images/documents/type_unknown.gif");
                prefetchImg("/polarion/ria/images/documents/type_word.gif");
                prefetchImg("/polarion/ria/images/documents/type_word_invalid.gif");
				
				prefetchImg("/polarion/ria/images/tree/T.gif");
				prefetchImg("/polarion/ria/images/tree/T+.gif");
				prefetchImg("/polarion/ria/images/tree/T-.gif");
				prefetchImg("/polarion/ria/images/tree/L.gif");
				prefetchImg("/polarion/ria/images/tree/L+.gif");
				prefetchImg("/polarion/ria/images/tree/L-.gif");
				
				prefetchImg("/polarion/ria/images/portal/lite_settings_off.png");
				prefetchImg("/polarion/ria/images/portal/lite_add_shortcut.png");
				prefetchImg("/polarion/ria/images/portal/settings_off.png");
                prefetchImg("/polarion/ria/images/portal/add_shortcut.png");
				prefetchImg("/polarion/ria/images/control/search_box.png");
				
				prefetchImg("/polarion/ria/images/control/pinned.png");
				prefetchImg("/polarion/ria/images/control/unpinned.png");
				prefetchImg("/polarion/ria/images/control/expand_morebutton.png");
                prefetchImg("/polarion/ria/images/control/collapse_morebutton.png");
				
				prefetchImg("/polarion/ria/images/logo.gif");
				prefetchImg("/polarion/ria/images/logo2.gif");
				prefetchImg("/polarion/ria/images/logos/small/logo_white.png");
				prefetchImg("/polarion/ria/images/logos/repo_logo.png");
		    	
				prefetchJS("/polarion/ria/javascript/pa/toggle.js");
				prefetchJS("/polarion/ria/javascript/pa/selection.js");
				
				prefetchJS("/polarion/ria/javascript/rangy/rangy-core.js");
				
				prefetchImg("/polarion/ria/images/portal/user_account.png");
				prefetchImg("/polarion/ria/images/portal/manageShortcutsIcon.png");
				prefetchImg("/polarion/ria/images/portal/administration.png");
				prefetchImg("/polarion/ria/images/portal/help.png");
				prefetchImg("/polarion/ria/images/portal/view.png");
				prefetchImg("/polarion/ria/images/portal/logout.png");
				prefetchImg("/polarion/ria/images/portal/settings_on.png");
		    }
		</script>
	</body>
</html>