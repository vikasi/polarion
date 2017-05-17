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

<%
    String originalUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");
    if (originalUrl == null) {
        StringBuffer requestURL = request.getRequestURL();
        if (requestURL == null) {
            originalUrl = "";
        } else {
            originalUrl = requestURL.toString();
        }
    }
    String queryString = (String) request.getAttribute("javax.servlet.forward.query_string");
    if (queryString == null) {
        queryString = request.getQueryString();
    }
    if (queryString != null) {
        originalUrl += "?";
        originalUrl += queryString;
    }
%>    
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.login.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" height="100%">
            <tr>
                <td>
                    <jsp:include page="include/header.jsp"/>
                </td>
            </tr>
            <tr>
                <td height="100%">
                    <form name="loginForm" method="POST" enctype="application/x-www-form-urlencoded" action="<%=originalUrl%>">
                        <table align="center" valign="middle" class="login" width="470px">
                            <tr>
                                <td class="logintitle" align="center">
                                    <%= Localization.getString("svnwebclient.login.login") %>
                                </td>
                            </tr>
                            <tr>
                                <td height="20">
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" align="center">
                                    <br/>
                                    <table cellpadding="0" cellspacing="2">  
<%
	if (request.getParameter("wrongCredentials") != null) {
%>                                    
                                    	<tr>
                                    		<td colspan="2">
                                    			<font color="red"><%= Localization.getString("svnwebclient.login.wrongCredentials") %></font>
                                    		</td>
                                    	</tr>                 
<%
	} else {
%>                                    	
            							<tr>
            								<td colspan="2">&nbsp;</td>
            							</tr>         
<%
	}
%>
                                    </table>
                                    <table cellpadding="0" cellspacing="2">              							               	                                                                                             
<%
	boolean isMiltirepository = org.polarion.svnwebclient.configuration.ConfigurationProvider.getInstance().isMultiRepositoryMode();
	if (isMiltirepository) {
%>		

										<tr>
                                    		<td class="pr0" align="right">
                                    			<%= Localization.getString("svnwebclient.login.parentRepositoryPath") %>
                                    		</td>	
                                    		<td>
                                    			<%=org.polarion.svnwebclient.configuration.ConfigurationProvider.getInstance().getParentRepositoryDirectory()%>
                                    		</td>
                                    	</tr>	
                                    	 <tr>
                                            <td class="pr0" align="right">
                                                <%= Localization.getString("svnwebclient.login.repositoryName") %>
                                            </td>
     
<%
		String location = request.getParameter("location");
		String res = "";
		if (location != null) {
			res = location;
		}
%>   
											 <td>	                                         
                                                <input name="location" size="12" maxlength="25" type="text" style="width: 25ex" value="<%=res%>"/>
                                            </td>
                                        </tr>
                                    	
<%
		} else {					
%>          
										<tr>
											<td class="pr0" align="right">
                                    			<%= Localization.getString("svnwebclient.login.repositoryUrl") %>
                                    		</td>	
                                    		<td>
                                    			<%=org.polarion.svnwebclient.configuration.ConfigurationProvider.getInstance().getRepositoryUrl()%>
                                    		</td>
										</tr>	


<%	
		}
%>      
                                    	<tr>
                                        	<td class="pr0" align="right">
                                                <%= Localization.getString("svnwebclient.login.userName") %>
                                            </td>
                                            <td>
                                                <input name="username" size="12" value="" maxlength="25" type="text" style="width: 25ex"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="pr0" align="right">
                                                <%= Localization.getString("svnwebclient.login.password") %>
                                            </td>
                                            <td>
                                                <input size="12" value="" name="password" maxlength="25" style="width: 25ex;" type="password"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" height="15"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" align="center">
                                                <input class="button" name="submit" type="submit" value="<%= Localization.getString("svnwebclient.login.loginButton") %>"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td height="30"></td>
                            </tr>
                        </table>
                        <input type="hidden" name="wrongCredentials" value="wrongCredentials"/>
                    </form>
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