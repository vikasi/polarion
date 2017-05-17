<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page contentType="text/html; charset=UTF-8"%>
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
  
<html>

<body>
    <table style="width:100%; height:100%;">
    	<tr>
    		<td align="center" valign="middle">
    			<img src="images/progress.gif" alt="progress"/>
    			<br/>
    			<span style="font-family:Verdana;font-size:11px"><%= Localization.getString("definition.loadingBig") %></span>
    		</td>
    	</tr>
    </table>	
    
</body>

<%
    String url = "pickerDirectoryContent-real.jsp";
    
    String queryString = (String) request.getAttribute("javax.servlet.forward.query_string");
    if (queryString == null) {
        queryString = request.getQueryString();
    }
    if (queryString != null) {
        url += "?";
        url += queryString;
    }

%> 

<script language="JavaScript">
    document.location.href="<%=url%>";
</script>

</html> 