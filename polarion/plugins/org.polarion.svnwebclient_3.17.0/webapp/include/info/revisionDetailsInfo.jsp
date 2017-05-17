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

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.RevisionDetailsBean"/>  
<table class="tabcontent" cellpadding="0" cellspacing="0" width="100%" border="0">
    <tr class="value" nowrap="true">
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.revpropscol.revision") %></b>&nbsp;
            <%=bean.getRevision().getDecoratedRevision()%>
<%
    if (bean.getRevision().isHeadRevision()) {
%>
            &nbsp;<%= Localization.getString("svnwebclient.directoryCompareInfo.head") %>
<%
    }                    
%>            
        </td>    
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.revpropscol.date") %></b>&nbsp;
            <span title="<%= Localization.getString("svnwebclient.revprops.ago", bean.getRevision().getAge()) %>"><%=bean.getRevision().getDate()%></span>
        </td>        
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.revpropscol.author") %></b>&nbsp;
            <%=bean.getRevision().getAuthor()%>
        </td>
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.directoryContentInfo.totalItems") %></b>&nbsp;
            <%=bean.getRevision().getChangedElementsCount()%>
        </td>
        <td width="100%"/>
    </tr>
    <tr>            
<%                                
	if (bean.getRevision().isMultiLineComment()) {
		String tool = "tool";
		String cell = "cell";
%>	

		<td class="value" style="padding-left:20px;" width="100%" colspan="5" id="<%=cell%>" onmouseover="xstooltip_show('<%=tool%>', '<%=cell%>');" onmouseout="xstooltip_hide('<%=tool%>');">									
			<div id="<%=tool%>" class="xstooltip">
				<%=bean.getRevision().getTooltip()%>	
			</div>		
		
			<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
			<img src="images/multiline_text.gif" style="position:absolute;margin-top:2px" align="middle" width="8" height="9">
			&nbsp;&nbsp;&nbsp;&nbsp;<%=bean.getRevision().getFirstLine()%>				
		</td>	
<%
	} else {								 
%>				    
     	<td class="value" style="padding-left:20px;" width="100%" colspan="5">
     		<b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>&nbsp;
            <%=bean.getRevision().getComment()%>
        </td>
<%
	}
%>                       
    </tr>    
</table>