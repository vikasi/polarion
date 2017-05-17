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

<%@ page import="org.polarion.svnwebclient.web.model.data.directory.*" %>  
  
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.directory.DirectoryCompareBean"/>
<%
    if (bean.execute(request, response)) {
        DirectoryCompare content = bean.getCompareResult();
%>
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.directoryCompare.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" height="100%">
            <tr>
                <td>
                    <jsp:include page="include/top.jsp"/>
                </td>
            </tr>
            <tr>
                <td style="padding:0;spacing:0px; padding-left:5px; padding-right:5px; padding-top:2px;">
                    <jsp:include page="include/compareInfoPanel.jsp">
                        <jsp:param name="jsp.compareInfoPanel.info" value="directoryCompareInfo.jsp"/>
                    </jsp:include>
                </td>
            </tr>
            <tr>            
                <td height="100%" valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:5px;padding-bottom:5px;">            
                    <table id="table_list_of_revisions" name="table_list_of_revisions" class="list" width="100%" cellpadding="0" cellspacing="0" rules="all">
                        <tr>
                            <th nowrap="true">
                                <%= Localization.getString("svnwebclient.directoryCompare.changedResources") %>
                            </th>
                        </tr>
<%
        for (int i = 0; i < content.getElements().size(); i++) {
            DirectoryCompare.Element element = (DirectoryCompare.Element) content.getElements().get(i);
%>            
                        <tr onmouseover="selectRow(this)" onmouseout="deselectRow(this)">
                            <td>
                                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td class="internal" style="padding-right:5px;">
<%
            if (!element.isDirectory()) {
%>                
                                            <a href="<%=element.getUrl()%>">
<%
            }                                            
%>            
                                                <img src="<%=element.getImage()%>" />
<%
            if (!element.isDirectory()) {
%>                                                
                                            </a>
<%
            }                                            
%>                                            
                                        </td>
                                        <td class="internal" width="100%" nowrap="true">
<%
            if (!element.isDirectory()) {
%>                                        
                                            <a href="<%=element.getUrl()%>">
<%
            }                                            
%>            
                                                <%=element.getName()%>
<%
            if (!element.isDirectory()) {
%>                                                
                                            </a>
<%
            }                                            
%>            
                                        </td>                                        
                                    </tr>
                                </table>

                            </td>                            
                        </tr>
<%    
        }
%>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <jsp:include page="include/footer.jsp"/>
                </td>
            </tr>
        </table>
    </body>
    <script language="javascript">
        firstsecond('table_list_of_revisions');
    </script>
</html>
<%
    }
%>