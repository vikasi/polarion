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

<%@ page import="org.polarion.svnwebclient.web.model.data.file.*,              
                 java.util.Iterator" %> 
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.file.FileDataBean"/>                 
<%
    if (bean.execute(request, response)) {
        FileData content = bean.getFileData();       
%>
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.fileData.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td>
                    <table class="linenumber" cellspacing="0" cellpadding="0">
<%
        for (Iterator i = content.getLinesIterator(); i.hasNext(); ) {
            FileData.Line line = (FileData.Line) i.next();
%>
                        <tr valign="top" align="right">
                            <td>
                                <a class="number" name="<%=line.getNumber()%>" href="#<%=line.getNumber()%>">
                                    <%=line.getNumber()%>
                                </a>
                            </td>
                        </tr>      
<%
        }                          
%>                    
                    </table>                
                </td>                
                <td width="100%">
                    <table class="codeline" cellspacing="0" cellpadding="0">
<%		
        for (Iterator i = content.getLinesIterator(); i.hasNext(); ) {
            FileData.Line line = (FileData.Line) i.next();            
%>
                        <tr valign="top" align="left">
                            <td nowrap="true">
                                <%=line.getData()%>&nbsp;
                            </td>
                        </tr>      
<%
        }                          
%>                    
                    </table>                
                </td>                                
            </tr>
        </table>
    </body>
</html>
<%
    }
%>    