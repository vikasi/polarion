<%--
  - Copyright (c) 2004, 2005 Polarion Software, All rights reserved.
  - Email: community@polarion.org
  -
  - This program and the accompanying materials are made available under the
  - terms of the Apache License, Version 2.0 (the "License"). You may not use
  - this file except in compliance with the License. Copy of the License is
  - located in the file LICENSE.txt in the project distribution. You may also
  - obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
  -
  -
  - POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES
  - ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED,
  - INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
  - SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
  - OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
  --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@page import="com.polarion.platform.i18n.Localization"%>

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.directory.DirectoryCompareBean"/>  
<table cellpadding="0" cellspacing="0" width="100%" border="0">
    <tr> 
		<td width="50%" class="message" style="padding-left:20px;" nowrap="true">    
<%
    if (bean.getStartRevisionInfo() != null) {	    
%>     	
    		 <table class="tabcontent" cellpadding="0" cellspacing="0" width="100%" border="0">
                <tr class="value" nowrap="true">
                    <td class="value" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.revision") %></b>&nbsp;                        
                        <a href="<%=bean.getStartRevisionUrl()%>">
                            <%=bean.getStartRevisionInfo().getDecoratedRevision()%>
                        </a>
<%
        if (bean.getStartRevisionInfo().isHeadRevision()) {
%>
                        &nbsp;<%= Localization.getString("svnwebclient.directoryCompareInfo.head") %>
<%
        }                    
%>            
                    </td>    
                    <td class="value" style="padding-left:20px;" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.date") %></b>&nbsp;
                        <span title="<%= Localization.getString("svnwebclient.revprops.ago", bean.getStartRevisionInfo().getAge()) %>"><%=bean.getStartRevisionInfo().getDate()%></span>
                    </td>        
                    <td class="value" style="padding-left:20px;padding-right:20px;" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.author") %></b>&nbsp;
                        <%=bean.getStartRevisionInfo().getAuthor()%>
                    </td>
                    <td width="100%"/>
                </tr>
            </table>   
<%
	} else {
%>  
            <b><%= Localization.getString("svnwebclient.directoryCompareInfo.nodata") %></b>
<%
	}
%>
		</td>
		<td width="50%" class="message" style="padding-left:20px;" nowrap="true">     
<%
    if (bean.getEndRevisionInfo() != null) {
%>    
    		<table class="tabcontent" cellpadding="0" cellspacing="0" width="100%" border="0">
                <tr class="value" nowrap="true">
                    <td class="value" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.revision") %></b>&nbsp;                        
                        <a href="<%=bean.getEndRevisionUrl()%>">
                            <%=bean.getEndRevisionInfo().getDecoratedRevision()%>
                        </a>    
<%
        if (bean.getEndRevisionInfo().isHeadRevision()) {
%>
                        &nbsp;<%= Localization.getString("svnwebclient.directoryCompareInfo.head") %>
<%
        }                    
%>            
                    </td>    
                    <td class="value" style="padding-left:20px;" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.date") %></b>&nbsp;
                        <span title="<%= Localization.getString("svnwebclient.revprops.ago", bean.getEndRevisionInfo().getAge()) %>"><%=bean.getEndRevisionInfo().getDate()%></span>
                    </td>        
                    <td class="value" style="padding-left:20px;padding-right:20px;" nowrap="true">
                        <b><%= Localization.getString("svnwebclient.revpropscol.author") %></b>&nbsp;
                        <%=bean.getEndRevisionInfo().getAuthor()%>
                    </td>
                    <td width="100%"/>
                </tr>
            </table>        
<%
	} else {
%>	
            <b><%= Localization.getString("svnwebclient.directoryCompareInfo.nodata") %></b>
<%
	}
%>
		</td>
	</tr>
	
	

	<tr>  	
<%
    if (bean.getStartRevisionInfo() != null) {	    
%>          
        
<%
		if (bean.getStartRevisionInfo().isMultiLineComment()) {
%>            
			<td width="50%" class="message" style="padding-left:25px;" id="left_cell"  onmouseover="xstooltip_show('left_tooltip', 'left_cell');" onmouseout="xstooltip_hide('left_tooltip');"> 
				<div id="left_tooltip" class="xstooltip">
					<%=bean.getStartRevisionInfo().getTooltip()%>	
                </div> 
				<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
				<img src="images/multiline_text.gif" style="position:absolute;margin-top:2px" align="middle" width="8" height="9">
				&nbsp;&nbsp;&nbsp;&nbsp;<%=bean.getStartRevisionInfo().getFirstLine()%>
			</td>

<%
		}	else {
%>            
			<td width="50%" class="message" style="padding-left:25px;"> 
				<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
				<%=bean.getStartRevisionInfo().getComment()%>
			</td>	
<%
		}
%>                        
<%
	} else {
%>
		<td width="50%" class="message" style="padding-left:25px;" nowrap="true"> 
        </td>    
<%
	}
%>

<%
    if (bean.getEndRevisionInfo() != null) {	    
%>          
        
<%
		if (bean.getEndRevisionInfo().isMultiLineComment()) {
%>            
			<td width="50%" class="message" style="padding-left:25px;" id="right_cell"  onmouseover="xstooltip_show('right_tooltip', 'right_cell');" onmouseout="xstooltip_hide('right_tooltip');"> 
			    <div id="right_tooltip" class="xstooltip">
					<%=bean.getEndRevisionInfo().getTooltip()%>	
                </div> 
				<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
				<img src="images/multiline_text.gif" style="position:absolute;margin-top:2px" align="middle" width="8" height="9">
				     &nbsp;&nbsp;&nbsp;&nbsp;<%=bean.getEndRevisionInfo().getFirstLine()%>
			</td>

<%
		}	else {
%>            
			<td width="50%" class="message" style="padding-left:25px;"> 
				<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
				<%=bean.getEndRevisionInfo().getComment()%>
			</td>	
<%
		}
%>                        
<%
	} else {
%>
		<td width="50%" class="message" style="padding-left:25px;" nowrap="true"> 
        </td>    
<%
	}
%>
	</tr>	
</table>
