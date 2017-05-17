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
<%@page import="com.polarion.core.util.EscapeChars"%>
<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.file.FileCompareBean"/>  
    
<table>
    <tr>
        <td>
		    <form name="form.encodings" style="padding:0;margin:0;">
		        <input type="hidden" name="currentUrl" value="<%=EscapeChars.forHTMLTag(bean.getCurrentUrlWithParameters())%>">
		        
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
            </form>                
        </td>
    </tr>
</table>


<script language="JavaScript">

    function changeSelect() {      
        var form = document.forms["form.encodings"];
        var urlElem = form.elements["currentUrl"];                         
        
        var encodings = form.elements["encodings"];
        var encoding = encodings.options[encodings.selectedIndex].value;
        
        var url = urlElem.value + "&encoding" + "=" + encoding;        
        document.location.href=url;        
    }
</script>  