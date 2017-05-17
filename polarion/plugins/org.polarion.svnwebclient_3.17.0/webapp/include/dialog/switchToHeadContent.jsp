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
<%@page import="org.polarion.svnwebclient.web.model.data.*" %>

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.SwitchToHeadBean"/>
<%
    SwitchToHead content = bean.getSwitchToHead();
%>
<table class="dialogcontent" cellspacing="0" cellpadding="0" width="100%" height="100%">
    <tr>
        <td>
            <%= Localization.getString("svnwebclient.switchToHeadContent.unableToFind", content.getHeadRevision(), content.getOriginalUrl(), content.getRevision()) %>
        </td>
    </tr>
<%
    if (content.getUrlInHead() != null) {
%>        
    <tr>
        <td>
            <%= Localization.getString("svnwebclient.switchToHeadContent.found", content.getUrlInRevision(), content.getRevision(), content.getUrlInHead(), content.getHeadRevision()) %>
        </td>
    </tr>
<%
    }    
%>    
    <tr>
        <td>
            <%= Localization.getString("svnwebclient.switchToHeadContent.pressOK") %>
        </td>
    </tr>
</table>