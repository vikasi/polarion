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
<%@page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@page import="com.polarion.platform.i18n.Localization"%>

<%@ page import="org.polarion.svnwebclient.web.model.data.file.*" %>  
  
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.file.FileContentBean"/>
<%
    if (bean.execute(request, response)) {
        FileContent content = bean.getFileContent();
%>
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.fileContent.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" height="99%">
            <tr>
                <td>
                    <jsp:include page="include/top.jsp"/>
                </td>
            </tr>
            <tr>
                <td style="padding:0px;spacing:0px; padding-left:5px; padding-right:5px; padding-top:2px;">
                    <jsp:include page="include/infoPanel.jsp">
                        <jsp:param name="jsp.infoPanel.info" value="fileContentInfo.jsp"/>
                        <jsp:param name="jsp.infoPanel.options" value="fileContentOptions.jsp"/>
                    </jsp:include>
                </td>
            </tr>
            <tr>
                <td width="100%" height="100%" style="padding:0px;spacing:0px; padding-left:5px; padding-right:7px;">
                    <iframe class="frame" id="content_frame" name="content_frame" width="100%" height="100%" frameborder="0" src="<%=bean.getSelectedViewUrl()%>">
                        <a href="<%=bean.getSelectedViewUrl()%>"><%= Localization.getString("svnwebclient.fileContent.content") %></a>
                    </iframe>
                </td>
            </tr>
            <tr>
                <td>
                    <jsp:include page="include/footer.jsp"/>
                </td>
            </tr>
        </table>
    </body>
</html>
<%
    }
%>