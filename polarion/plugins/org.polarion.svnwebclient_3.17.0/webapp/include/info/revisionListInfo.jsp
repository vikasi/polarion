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

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.RevisionListBean"/>  
<table class="tabcontent" cellpadding="0" cellspacing="0" width="100%" border="0">
    <tr nowrap="true">
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.revisionListInfo.headRevision") %></b>&nbsp;
            <a href="<%=bean.getRevisionList().getHeadRevisionUrl()%>">
                <%=bean.getRevisionList().getHeadRevision()%>
            </a>
        </td>    
        <td class="value" style="padding-left:20px;" nowrap="true">
            <b><%= Localization.getString("svnwebclient.revisionListInfo.displayedRevisions") %></b>&nbsp;
            <%=bean.getRevisionList().getRevisionsCount()%>
        </td>   
        
<%
    if (bean.isAllowedHidePolarionCommit()) {
%>                
<%
        if (bean.isHidePolarionCommit()) {
%>        
        <td class="value" nowrap="true" style="padding-left:20px;padding-top:5px;">
            <input type="checkbox" name="hidepolarioncommit" id="hidepolarioncommit" checked onchange='javascript:window.location="<%=bean.getButtonUrl().getHidePolarionCommitUrl()%>"'/>
        </td>
<%
        } else {
%>
        <td class="value" nowrap="true" style="padding-left:20px;padding-top:5px;">
            <input type="checkbox" name="hidepolarioncommit" id="hidepolarioncommit" onchange='javascript:window.location="<%=bean.getButtonUrl().getHidePolarionCommitUrl()%>"'/>
        </td>
<%
        }
%>
        <td class="value" nowrap="true" style="padding-top:5px;">
            <%= Localization.getString("svnwebclient.revisionListInfo.hideRevisions") %>
        </td>
<%        
    }        
%>
        <td width="100%"/>
    </tr>
</table>
