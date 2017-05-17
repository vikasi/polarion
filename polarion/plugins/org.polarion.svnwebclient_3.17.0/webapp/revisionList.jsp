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

<%@ page import="org.polarion.svnwebclient.web.model.data.*,
		org.polarion.svnwebclient.decorations.IIconDecoration"
%>  
  
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.RevisionListBean"/>
<%
    if (bean.execute(request, response)) {
        RevisionList content = bean.getRevisionList();
%>
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.revisionList.title") %>"/>
    </jsp:include>
    <body>
        <table cellspacing="0" cellpadding="0" width="100%" height="100%">
            <tr>
                <td>
                    <jsp:include page="include/top.jsp"/>
                </td>
            </tr>
            <tr>
                <td style="padding:0;spacing:0px; padding-left:5px; padding-right:5px; padding-top:2px;">
                    <jsp:include page="include/infoPanel.jsp">
                        <jsp:param name="jsp.infoPanel.info" value="revisionListInfo.jsp"/>
                        <jsp:param name="jsp.infoPanel.options" value="revisionListOptions.jsp"/>
                    </jsp:include>
                </td>
            </tr>
            <tr>
                <td height="100%" valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:5px;padding-bottom:5px;"> 
                    <form name="revisionform" style="margin:0;padding:0;">             
                        <table id="table_list_of_revisions" name="table_list_of_revisions" class="list" width="100%" cellpadding="0" cellspacing="0" rules="all">
                            <tr>
                                <th></th>
                                <th nowrap="true" width="5%">
                                    <%= Localization.getString("svnwebclient.revprops.revision") %>
                                </th>
                                <th nowrap="true" width="5%">
                                    <%= Localization.getString("svnwebclient.revprops.date") %>
                                </th>
                                <th nowrap="true" width="10%">
                                    <%= Localization.getString("svnwebclient.revprops.author") %>
                                </th>
                                <th nowrap="true" width="75%">
                                    <%= Localization.getString("svnwebclient.revprops.comment") %>
                                </th>
                                <th>
                                    <%= Localization.getString("svnwebclient.revisionList.actions") %>
                                </th>
                            </tr>
<%
		int counter = 0;
        for (int i = 0; i < content.getRevisions().size(); i++) {
            RevisionList.Element element = (RevisionList.Element) content.getRevisions().get(i);
%>            
                            <tr onmouseover="selectRow(this)" onmouseout="deselectRow(this)">
                                <td style="padding:0; padding-left:2px; padding-right:2px; text-align: center;">
                                    <input name="revision" type="checkbox" value="<%=element.getRevision()%>" id="revision<%=element.getRevision()%>" onclick="checkRevision('revision<%=element.getRevision()%>')"/>
                                </td>
                                                              
                                  <td>         
                                  

                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>                                        
                                       		<td class="internal" width="16px" style="padding-right:5px;">
<%
            if (element.isHeadRevision()) {
%>
                                                <img src="images/head.gif" alt="<%= Localization.getString("svnwebclient.revisionList.headRevision") %>" title="<%= Localization.getString("svnwebclient.revisionList.headRevision") %>" border="0"/>
<%
            } else {
%>                  
                                                <img src="images/pixel.gif" border="0" width="16px"/>
<%
            }                                                                                  
%>            
                                            </td>
                                        
                                            <td class="internal" width="100%" nowrap="true" style="padding-right:5px;" align="right">                               
			                                    <a href="<%=element.getRevisionUrl()%>">
			                                        <%=element.getDecoratedRevision()%>
			                                    </a>
			                                </td>   
											 <td class="internal" width="16px">
<%
            if (element.isRevisionDecorated()) {
                IIconDecoration decoration = element.getRevisionDecoration();
                if (decoration.getIconLink() != null) {
%>
                                                <a target="_top" href="<%=decoration.getIconLink()%>">
<%
                }                                            
%>                
                                                    <img src="<%=decoration.getIconURL()%>" alt="<%=decoration.getIconTooltip()%>" title="<%=decoration.getIconTooltip()%>" border="0" width="16px"/>
<%
                if (decoration.getIconLink() != null) {
%>
                                                </a>                                            
<%
                }
            } else {
%>                
                                                <img src="images/pixel.gif" border="0" width="16px"/>
<%
            }                                            
%>            
                                            </td>                                                                                                     
                                        </tr>
                                    </table>
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
                                
                                
                                <td>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td class="internal" width="16px" style="padding-right:5px;">
                                                <a href="<%=element.getRevisionInfoUrl()%>">
                                                   <img src="images/show_affected_path.gif " alt="<%= Localization.getString("svnwebclient.revisionList.revisionChanges") %>" title="<%= Localization.getString("svnwebclient.revisionList.revisionChanges") %>" border="0"></img>
                                                </a>
                                            </td>                                          
                                            <td class="internal" width="16px">
<%
            if (content.isDirectory()) {                                        
%>
                                                <img src="images/pixel.gif" border="0" width="16px"/>
<%
            } else {
%>                
                                                <a href="<%=element.getDownloadUrl()%>">
                                                   <img src="images/download.gif" alt="<%= Localization.getString("svnwebclient.revisionList.download") %>" title="<%= Localization.getString("svnwebclient.revisionList.download") %>" border="0"></img>
                                                </a>
<%
            }                                            
%>            
                                            </td>                                                                                   
                                        </tr>
                                    </table>
                                </td>
                            </tr>
<%    
        }
%>
                        </table>
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
    <script language="javascript">
        firstsecond('table_list_of_revisions');
    </script>
</html>
<%
    }
%>