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

<%@ page import="org.polarion.svnwebclient.configuration.ConfigurationProvider" %>  
<table cellspacing="0" cellpadding="0" width="100%" height="100%">
<%
    boolean skipNavigation = false;
    boolean isPickerMode = false;
    
    if ((request.getParameter("jsp.top.skipnavigation") != null) && "true".equals(request.getParameter("jsp.top.skipnavigation"))) {
        skipNavigation = true;
    }
    
    if ((request.getParameter("jsp.top.isPickerMode") != null) && "true".equals(request.getParameter("jsp.top.isPickerMode"))) {
        isPickerMode = true;
    }    
%>

<%
    if (!isPickerMode) {     
    
    
        if (ConfigurationProvider.getInstance().isEmbedded()) {
            if (!skipNavigation) {
%>        
    <tr>
        <td>
            <jsp:include page="navigation.jsp"/>        
        </td>
    </tr>
<%
            }
%> 
    <tr>
        <td>
            <jsp:include page="header.jsp"/>        
        </td>
    </tr>    

<%
        } else {
%>

    <tr>
        <td>
            <jsp:include page="header.jsp"/>
        </td>
    </tr>
<%
            if (!skipNavigation) { 
%>               
    <tr>
        <td>
            <jsp:include page="navigation.jsp"/>
        </td>
    </tr>    
<%
            }
        }    
    } else {       
        if (!skipNavigation) {
%>
     <tr>
        <td>
            <jsp:include page="navigation.jsp"/>        
        </td>
    </tr>
<%        
        }    
    }
%>

</table>
