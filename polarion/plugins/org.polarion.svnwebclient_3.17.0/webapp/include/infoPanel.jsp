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

<table cellspacing="0" cellpadding="0" width="100%" height="100%">
    <tr>
        <td>
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td class="activetab" bgcolor="green" height="20"  colspan="2">
                        <img src="images/info_ico.gif" alt="<%= Localization.getString("svnwebclient.infoPanel.info") %>" title="<%= Localization.getString("svnwebclient.infoPanel.info") %>" style="vertical-align:middle;margin-right:3px;"/><%= Localization.getString("svnwebclient.infoPanel.info") %>
                    </td>    
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td width="100%">
            <table class="infopanel" cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td width="100%">
<%
    String infoPart = "info/" + request.getParameter("jsp.infoPanel.info");
%>                    
                        <jsp:include page="<%=infoPart%>"/>
                    </td>
<%
    if (request.getParameter("jsp.infoPanel.options") != null) {
        String optionsPart = "options/" + request.getParameter("jsp.infoPanel.options");
%>
                    <td>                    
                        <jsp:include page="<%=optionsPart%>"/>
                    </td>        
<%
    }                    
%>    
                    <td>
                        <jsp:include page="actionPanel.jsp"/>
                    </td>                
                </tr>
            </table>
        </td>
    </tr>
</table>