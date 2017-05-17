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

<%@page import="org.polarion.svnwebclient.web.controller.ErrorBean"%>
 
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.ReviewBean"/> 
<%
    if (bean.execute(request, response)) {     
        session.setAttribute("letter", bean.getLetterContent());  
        
        ErrorBean errorBean = new ErrorBean();
        errorBean.execute(request, response);
        String stacktrace = errorBean.getStacktrace();        
        String message = errorBean.getMessage();
        String description = errorBean.getDescription();
%>

<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.review.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
            <form method="POST" action="sendEmail.jsp">

                <input type="hidden" name="email" value="<c:out value=${request.getParameter('email')} />" />
                <input type="hidden" name="description" value="<c:out value=${description} />" />
                <input type="hidden" name="stacktrace" value="<c:out value=${stacktrace} />" />
                <input type="hidden" name="name" value="<c:out value=${request.getParameter('name')} />" />
                <input type="hidden" name="message" value="<c:out value=${message} />" />        
                <input type="hidden" name="letter" value="<c:out value=${bean.getEncodedLetterContent()} />" />                      
                <input type="hidden" name="falseSend" value="<c:out value=${falseSend} />" />                      
                <input type="hidden" name="reportId" value="<c:out value=${request.getParameter('reportId')} />" />
                
                <tr>
                    <td>
                        <jsp:include page="include/top.jsp">
                            <jsp:param name="jsp.top.skipnavigation" value="true"/>                    
                        </jsp:include>
                    </td>
                </tr>                
                
                <tr>
                    <td style="padding:0;spacing:0px; padding-left:5px; padding-right:5px; padding-top:5px;padding-bottom:5px;" height="100%">
                        <jsp:include page="include/dialog.jsp">
                            <jsp:param name="jsp.dialog.title" value="<%= Localization.getString("svnwebclient.review.dialogTitle") %>"/>
                            <jsp:param name="jsp.dialog.fullwidth" value="true"/>
                            <jsp:param name="jsp.dialog.fullheight" value="true"/>
                            <jsp:param name="jsp.dialog.content" value="reportReviewContent.jsp"/>                        
                        </jsp:include>
                    </td>
                </tr>
            
                <tr>
                    <td>
                        <jsp:include page="include/footer.jsp"/>
                    </td>
                </tr>
            </form>    
        </table>
    </body>
</html>            
<%
    }
%>
