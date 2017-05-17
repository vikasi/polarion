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

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.AbstractBean"/>

<%
    if ("true".equals(request.getParameter("jsp.dialog.fullwidth"))) {
%>        
<table width="100%" height="100%" class="dialog" cellspacing="0" cellpadding="0">
<%
    } else {
%>
<table align="center" class="dialog" cellspacing="0" cellpadding="0">
<%
    }
%>        
    <tr>
        <td width="100%">
            <table width="100%" class="valueListTitle" cellpadding="0" cellspacing="0" >
			    <tr>
                    <td style="padding-left:10px;padding-right:10px;">
                        <%=request.getParameter("jsp.dialog.title")%>
                    </td>            
                </tr>
            </table>
        </td>
    </tr>            

<%
    if ("true".equals(request.getParameter("jsp.dialog.errormessage"))) { 
%>    
    <tr>
        <td align="left" width="100%" style="padding-left:10px;padding-right:10px; padding-top:10px;">
            <font color="red"><b><%=bean.getErrorMessage()%></b></font>
        </td>    
    </tr>
<%
    }
%>     

    <tr>
<%
    if ("true".equals(request.getParameter("jsp.dialog.fullheight"))) {
%>     
        <td width="100%" height="100%" style="padding-left:10px;padding-right:10px;padding-top:20px;padding-bottom:20px;">
<%
    } else {
%>
        <td width="100%" style="padding-left:10px;padding-right:10px;padding-top:20px;padding-bottom:20px;">
<%
    }        

    String contentPart = "dialog/" + request.getParameter("jsp.dialog.content");
%>        
            <jsp:include page="<%=contentPart%>"/>
        </td>    
    </tr>
<%
    String buttonsPart = request.getParameter("jsp.dialog.buttons");
    if (buttonsPart != null) {    
        buttonsPart = "dialog/" + buttonsPart;
%>    
    <tr>
        <td width="100%" style="padding-left:10px;padding-right:10px;padding-top:0px;padding-bottom:20px;">
            <jsp:include page="<%=buttonsPart%>"/>
        </td>    
    </tr>
<%
    }
%>   
 
</table>