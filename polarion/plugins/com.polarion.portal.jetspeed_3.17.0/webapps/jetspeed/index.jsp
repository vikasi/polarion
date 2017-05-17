<% 
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", System.currentTimeMillis() - 10L * 365L * 24L * 60L * 60L * 1000L);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<%@page import="com.polarion.portal.jetspeed.gwt.GWTServletProxy"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.portal.tomcat.*" %>
	<head>
	    <% 
	    	String dialogUrl = (String)pageContext.getAttribute("dialogUrl"); 
	    	if(dialogUrl != null) {
	    
	    %>
	    <script src="/polarion/oslcDelegatedUI.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script type="text/javascript">
            //<![CDATA[
                var hash = '/project/<%= request.getParameter("project") %>/<%= dialogUrl %>' + 
                    '&ui_callback=OSLC_delegatedUICallback' + 
                    '&oslc_callback_protocol=' + encodeURIComponent(window.location.hash);
                window.location.hash = hash;
            //]]>
        </script>
        <% } %>
		<%=com.polarion.portal.tomcat.SupportedBrowsers.getXUACompatibleTag(request)%>
		<link rel="shortcut icon" href="/polarion/ria/images/favicon.ico" />
		<link rel="stylesheet" type="text/css" href="/polarion/gwt/gwt/polarion/polarion.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" />
  		<style type="text/css">
			html{
				height:100%;
				overflow:hidden;
			}
			body{
			    font-size:13px;
            }
        	TEXTAREA {
        	    font-family:Arial, Helvetica, sans-serif;
        		resize:none !important;
        	    font-size:13px;
        	}
			P{
				margin: 0px;
			}
			
			A,TD,DIV,LI,UL,INPUT,BUTTON{
			    font-family:Arial, Helvetica, sans-serif !important;
			}
			
			INPUT[type="file"]{
			    font-size:11px; 
			}
			
			A:link { text-decoration:none; color:black;}
			A:visited { text-decoration:none; color:black;}
			A:hover { text-decoration:underline; color:black;}
			
			#FIELD_description A:link, #FIELD_comments A:link { 
				cursor: pointer !important;
				color: #0277BB !important;
				text-decoration: none !important;
			}
			#FIELD_description  A:visited, #FIELD_comments A:visited { 
				cursor: pointer !important;
				color: #0277BB !important;
				text-decoration: none !important;
			}
			#FIELD_description A:hover, #FIELD_comments A:hover { 
				cursor: pointer !important;
				color: #0277BB !important;
				text-decoration: underline !important;
			}

			.polarion-dle-comment-resolved-icon { display:none; }
			
			a.actionLink {cursor: pointer; text-decoration:underline; color:blue;}
			
			.wiki_style p{line-height:1.7em;margin-top:8px;margin-bottom:8px;}
			.wiki_style H1, .wiki_style H2, .wiki_style H3, .wiki_style H4, .wiki_style H5, .wiki_style H6{
			color: #0046b0;
			margin: 0px;
			padding: 0px;
			padding-left: 4px;
			padding-bottom: 0px;
			font-weight: bold;
			line-height: normal;
			font-family:Lucida Grande,helvetica,lucida,verdana,sans-serif;}
			
			.wiki_style H1{
			font-size: 18pt;
			border-bottom: 2px solid #c7c7c7;
			margin-top: 14px;}
			
			.wiki_style H2{
			font-size: 16pt;
			background-color:#f2f2f2;
			margin-top: 24px;}
			
			.wiki_style H3 {
				font-size: 15pt;
				margin-top: 20px; 
			} 
			
			.wiki_style H4{
				font-size: 13.5pt;
				margin-top: 20px;}H5{
				font-size: 12pt;
				color: black;
				margin-top: 14px;
			} 
			.wiki_style H6{   font-size: 10pt;   color: black;  margin-top: 14px;}
			
			.wiki_style div.infomessage{text-align: left;width: 95%;margin: 4px 5px;padding: 16px 4px 16px 36px;background: #D8E4F1 url(/polarion/ria/images/msginfo.png) 10px 18px no-repeat;}
			
            .document_preview {
                border: 1px solid rgb(204,204,204); 
                background-color: white;
                font-size: 10pt;
                line-height:1.5;
                padding: 40px 40px 40px 40px;
                
                font-family: Arial, Verdana, sans-serif;
                cursor: text;
                    
                A:link { text-decoration:none; color:black; }
                A:visited { text-decoration:none; color:black; }
                A:hover { text-decoration:underline; color:black; }
                A.descriptionLink:link { text-decoration:none; color:blue; }
                A.descriptionLink:visited { text-decoration:none; color:blue; }
                A.descriptionLink:hover { text-decoration:underline; color:blue; }
            
            }
            
            div.document_preview {
                width:754px;
            }
            
            .document_preview H1, .document_preview H2, .document_preview H3, .document_preview H4, .document_preview H5, .document_preview H6 {
                    color:#003366;
                    font-weight:bold;
                    line-height:1.5;
                    font-family:Arial, Helvetica, sans-serif;
                    margin: 0.83em 0 0.41em;
            }
                            
            .document_preview H1 {
                    border-bottom : 1px solid lightgrey; 
                    font-size : 26pt; 
                    line-height:1.2;
                    padding-bottom : 5px;
            }
                
            .document_preview H2 {
                    font-size : 22pt;
                    line-height:1.2;
            }
                            
            .document_preview H3 {  
                    font-size : 18pt;
                    line-height:1.2;
            }
            
            .document_preview H4 {
                    font-size : 14pt;
                    line-height:1.2;
            }
            
            .document_preview H5 { 
                    font-size : 12pt;
            }
            
            .document_preview H6 {
                    font-size : 10pt;
            }
            
            .document_preview P {
                line-height:1.5;
            	margin: 8px 0px;
            }

            .document_preview TABLE P {
                line-height: 14.4px;
                margin: 0px;
            }
            
            <%= com.polarion.core.config.Configuration.getInstance().ui().listStyle().getStyle() %>
			
  		</style>
  		<link rel="stylesheet" href="/polarion/ria/font-awesome-4.0.3/css/font-awesome.css">
  		
  		<link rel="stylesheet" href="/polarion/ria/codemirror/lib/codemirror.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>">
  		<link rel="stylesheet" href="/polarion/ria/codemirror/addon/hint/show-hint.css?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>">
  		<title></title>
  	</head>
	<body onbeforeunload="try {return handleOnBeforeUnload();}catch(e){}" style="overflow:hidden;height:100%">
		<script type="text/javascript">
			//<![CDATA[
                onerror = function(errMessage, url, line){
               		if(url != null && url != ""){
                    	handleOnError(errMessage + " \nline: " + line + " \nurl: " + url);
                    }
                    return true;
                }
                
                <%
    					boolean isFF4 = com.polarion.portal.tomcat.SupportedBrowsers.isFF4(request);
    					if (isFF4) {
				%>
					oldalert = alert;
                	alert = function(message){
	                	try{
	                		oldalert(message);
	                	
	                	} catch(e){
	                	
	                	}
                	}
				<%
				    }
				%>
                
			//]]>
		</script>
		
		<div id="container_div" style="height:100%" >
			<table style="width:100%;height:100%">
				<tr>
					<td align="center" valign="middle">
						<img src="/polarion/ria/images/progress.gif" alt="progress"/>
						<br/>
						<span><%= Localization.getString("definition.loadingBig") %></span>
					</td>
				</tr>
			</table>
		</div>
		
        <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
		<script src="/polarion/gwt/com.polarion.UI/com.polarion.UI.<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getJsTimestamp()%>.nocache.js" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/flash_detect.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/liveplan.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/rangy/rangy-core.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		<script src="/polarion/ria/javascript/rangy/rangy-selectionsaverestore.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>" type="text/javascript"></script>
		
		<script src="/polarion/ria/codemirror/lib/codemirror.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/xml/xml.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/javascript/javascript.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/css/css.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/htmlmixed/htmlmixed.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/velocity/velocity.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/mode/sql/sql.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/addon/edit/matchbrackets.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/addon/selection/active-line.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

		<script src="/polarion/ria/codemirror/addon/hint/show-hint.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/addon/hint/xml-hint.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
		<script src="/polarion/ria/codemirror/addon/edit/closebrackets.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

		<script src="/polarion/ria/javascript/jquery-1.12.4.min.js" type="text/javascript"></script>

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
		
		<%//add some more code mirror modes 
		for (java.io.File file: ExternalFilesProvider.getAllContributedFiles()){
				String path = "\"codemirror-modes/"+file.getName()+"?buildId="+com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()+"\"";
				%>
		<script src=<%=path%> type="text/javascript"></script>
		<% 
		}
		%> 
		<iframe src="/polarion/ria/prefetch.jsp" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe> 
		
		<script type="text/javascript">
		//<![CDATA[
		//]]>
		</script>
		<script type="text/vbscript"> 
		//<![CDATA[
		sub openWord(url)
		  On Error Resume Next
		
	      Set objApp = CreateObject("Word.Application")
	      
		  If Err.number > 0 Then
         	MsgBox "Please allow ActiveX components in your browser to enable this operation.",vbOKOnly , "Polarion"
        Else
          	objApp.Visible = true
	     	objApp.Documents.Open url
         End If
       	
        End sub
		sub openExcel(url)
		  On Error Resume Next
          Set objApp = CreateObject("Excel.Application")
          If Err.number > 0 Then
       	  	MsgBox "Please allow ActiveX components in your browser to enable this operation.",vbOKOnly , "Polarion"
       	  Else
       	  Set objWorkBook = objApp.Workbooks.Open(url)
    	  objApp.Visible = true
	      End If
        End sub
   		//]]>
   		</script>
   		
   		<% if(System.getProperty("google.analytics.number") != null ) {	%>

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("<%out.print(System.getProperty("google.analytics.number"));%>");
pageTracker._trackPageview();
} catch(err) {}</script>

<% } %>
	</body>
</html>