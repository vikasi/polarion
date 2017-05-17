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
<%@page import="org.polarion.svnwebclient.web.model.data.*" %>

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.PathSwitchBean"/>
<%
    PathSwitchContent content = bean.getSwitchContent();
%>
<table class="dialogcontent" cellspacing="0" cellpadding="0" width="100%" height="100%">
    <tr>
        <td>
            <b><%= Localization.getString("svnwebclient.pathSwitchContent.select", bean.getName()) %></b>
        </td>
    </tr>
    <tr>
        <td>            
            <table id="table_list_of_files" name="table_list_of_files" class="list" width="100%" cellpadding="0" cellspacing="0" rules="all">
                <tr>
                    <th nowrap="true" width="30%">
                        <%= Localization.getString("svnwebclient.revprops.name") %>
                    </th>
                    <th nowrap="true" width="5%">
                        <%= Localization.getString("svnwebclient.revprops.revision") %>
                    </th>
                    <th nowrap="true" width="5%">
                        <%= Localization.getString("svnwebclient.revprops.date") %>
                    </th>
                    <th nowrap="true" width="10%">
                        <%= Localization.getString("svnwebclient.revprops.author") %>
                    </th>
                    <th nowrap="true" width="45%">
                        <%= Localization.getString("svnwebclient.revprops.comment") %>
                    </th>
                </tr>
<%
    if ((content != null) && (content.getChilds().size() > 0)) {
	    int counter = 0;
        for (int i = 0; i < content.getChilds().size(); i++) {
            PathSwitchContent.Element element = (PathSwitchContent.Element) content.getChilds().get(i);
%>            
                <tr onmouseover="selectRow(this)" onmouseout="deselectRow(this)">
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="internal" style="padding-right:5px;">
                                    <a href="<%=element.getContentUrl()%>">
                                        <img src="<%=element.getImage()%>" />
                                    </a>
                                </td>
                                <td class="internal" width="100%" nowrap="true">
                                    <a href="<%=element.getContentUrl()%>">
                                        <%=element.getName()%>
                                    </a>
                                </td>                                        
                            </tr>
                        </table>
                    </td>
                    <td align="right">
                        <%=element.getDecoratedRevision()%>                            
                    </td>
                    <td nowrap="true">
                        <span title="<%= Localization.getString("svnwebclient.revprops.ago", element.getAge()) %>"><%=element.getDate()%></span>
                    </td>
                    <td nowrap="true">
                        <%=element.getAuthor()%>
                    </td>
<%                                
			if (element.isMultiLineComment()) {
				counter ++;
				String cell = "cell_" + counter;
				String tool = "tooltip_" + counter;
%>	
					<td id="<%=cell%>" onmouseover="xstooltip_show('<%=tool%>', '<%=cell%>');" onmouseout="xstooltip_hide('<%=tool%>');">									
						<div id="<%=tool%>" class="xstooltip">
							<%=element.getTooltip()%>	
						</div>
						<img src="images/multiline_text.gif" style="position:absolute;margin-top:2px" align="middle" width="8" height="9">
	     				&nbsp;&nbsp;&nbsp;&nbsp;<%=element.getFirstLine()%>				
	     			</td>	
<%
			} else {								 
%>				    
		         	<td>
                        <%=element.getComment()%>
                    </td>
<%
			}
%> 
                </tr>
<%    
        }
    } else {
%>
                <tr>
                    <td colspan="5" align="center">
                        <b><%= Localization.getString("svnwebclient.pathSwitchContent.noAvailable", bean.getPluralName()) %></b>
                    </td>
                </tr>
<%
    }
%>
            </table>
        </td>
    </tr>
        <tr>
            <td width="100%" style="padding-top:20px;padding-bottom:0px;">
                <table cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td align="left">
                            <input type="button" class="button" value="<%= Localization.getString("svnwebclient.generic.cancel") %>" onclick="javascript:window.location='<%=bean.getCancelUrl()%>'"/> 
                        </td>
                        <td width="100%"/>
                    </tr>
                </table>
            </td>    
        </tr>     
</table>

<script language="javascript">
firstsecond('table_list_of_files');
</script>
