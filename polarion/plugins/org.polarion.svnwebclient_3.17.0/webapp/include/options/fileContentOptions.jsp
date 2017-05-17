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

<%@ page import="java.util.Set,java.util.Iterator" %>
                 
<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.file.FileContentBean"/>

<table cellpadding="0" cellspacing="0" border="0" height="100%"> 
<form name="form.options">
    <tr>
        <td style="padding-right:20px;">
            <select name="views" class="combo" onchange="javascript:changeSelect()">
<%
    for (Iterator i = bean.getViews().keySet().iterator(); i.hasNext(); ) {
        String view = (String) i.next();
        String viewUrl = (String) bean.getViews().get(view);
        
        
        if (bean.getSelectedView().equals(view)) {
%>                 
                <option value="<%=viewUrl%>" selected>
                    <%=view%>
                </option>                    
<%
        } else {
%>            
                <option value="<%=viewUrl%>">
                    <%=view%>
                </option>
<%
        }                        
    }                
%>    

            </select>
        </td>
        
       

        <td style="padding-right:20px;">
            <select name="encodings" class="combo" onchange="javascript:changeSelect()">
<%
    for (Iterator i = bean.getCharacterEncodings().iterator(); i.hasNext(); ) {
        String encoding = (String) i.next();        
        if (bean.isSelectedCharacterEncoding(encoding)) {
%>                 
                <option value="<%=encoding%>" selected>
<%
        } else {
%>            
                <option value="<%=encoding%>">
<%
        }                        
%>        
                    <%=encoding%>
                </option>
<%
    }                
%>    
            </select>
        </td>             
        
    </tr>
    
</form>    
</table>





<script language="JavaScript">

    function changeSelect() {                       
        var views = document.forms["form.options"].elements["views"];
        var encodings = document.forms["form.options"].elements["encodings"];
        var encoding = encodings.options[encodings.selectedIndex].value;
        
        var url = views.options[views.selectedIndex].value + "&encoding" + "=" + encoding;        
        frames['content_frame'].location.href=url;        
    }
</script>