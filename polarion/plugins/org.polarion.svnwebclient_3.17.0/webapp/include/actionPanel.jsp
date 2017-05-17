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
  
<%@ page import="java.util.List,
                 java.util.Iterator,
                 org.polarion.svnwebclient.web.model.Button"%>

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.IBean"/>
<%
    List actions = bean.getActions();
%>
  
<table class="tabcontent" cellpadding="0" cellspacing="0" border="0">
    <tr>
<%
    for (Iterator i = actions.iterator(); i.hasNext(); ) {
        Button button = (Button) i.next();
%>
        <td style="padding-right:7px;" align="left">
<%
    if (button.getUrl().startsWith("javascript:")) {
%>
            <a onclick="<%=button.getUrl()%>">
<%
    } else {
%>
            <a href="<%=button.getUrl()%>">
<%
    }
%>        
                <img src="<%=button.getImage()%>" alt="<%=button.getCaption()%>" title="<%=button.getCaption()%>" style="white-space:pre;padding:0 0 0 0; margin:0 0 0 0; cursor:pointer;" border="0"/>
            </a>
        </td>        
<%
    }
%>
    </tr>
</table>