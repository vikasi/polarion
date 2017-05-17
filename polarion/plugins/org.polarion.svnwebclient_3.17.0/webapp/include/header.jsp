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
<%@page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@page import="com.polarion.platform.i18n.Localization"%>

<%@ page import="org.polarion.svnwebclient.configuration.ConfigurationProvider"%>
<%
    if (!ConfigurationProvider.getInstance().isEmbedded()) {
%>
<table class="actionbar_head" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td style="padding-left:8px;padding-right:7px;">
            <a href="http://www.polarion.org/">
                <img src="images/logo_small.gif" alt="Polarion.org" title="Polarion.org" style="padding:0 0 0 0; margin:0 0 0 0;vertical-align:middle;" border="0"></img>
            </a>
        </td>
        <td nowrap="nowrap" class="title">
            <%= Localization.getString("svnwebclient.header.webClient") %>
        </td>
        <td width="100%">
        </td>
<%
	if (ConfigurationProvider.getInstance().isLogout()) {
%>        

<%
		if (request.getSession().getAttribute(org.polarion.svnwebclient.authorization.ICredentialsManager.IS_LOGGED_IN) == null) {
%>
		<td style="padding-left:8px;padding-right:7px;" class="title">
            <a>
                <%= Localization.getString("svnwebclient.header.logout") %>
            </a>
        </td>
<%
		} else {
%>
		<td style="padding-left:8px;padding-right:7px;" class="title">
            <a href="logout.jsp">
                <%= Localization.getString("svnwebclient.header.logout") %>
            </a>
        </td>
<%
		}
%>
        
<%
	}
%>        
    </tr>
</table>
<%
    }
%>    