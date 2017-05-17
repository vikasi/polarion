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
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="org.polarion.svnwebclient.web.model.data.directory.*,
                 org.polarion.svnwebclient.decorations.IIconDecoration,
                 org.polarion.svnwebclient.web.model.sort.DirectoryContentSortManager,
                 java.util.List" %>                   
                   
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.directory.DirectoryContentBean"/>
<%
    if (bean.execute(request, response)) {
        DirectoryContent content = bean.getDirectoryContent();
        DirectoryContentSortManager sortManager = bean.getSortManager();
        
        List childs = content.getChilds();
%>

<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.directoryContent.title") %>"/>
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
                        <jsp:param name="jsp.infoPanel.info" value="directoryContentInfo.jsp"/>
                    </jsp:include>
                </td>
            </tr>

            <tr>
                <td height="100%" valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:5px;padding-bottom:5px;">            
                    <form name="dir_list" method="POST" action="<%=bean.getDeleteUrl()%>">
                        <table id="table_list_of_files" name="table_list_of_files" class="list" width="100%" cellpadding="0" cellspacing="0" rules="all">
                            <tr>
                                <th></th>
                                <th nowrap="true" width="30%">
                                    <table class="internalheader" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="background:#75A8E2;padding-right:5px;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_NAME))%>">
                                                    <%= Localization.getString("svnwebclient.revprops.name") %>
                                                </a>
                                            </td>
<%
        if (sortManager.hasSortIcon(DirectoryContentSortManager.FIELD_NAME)) {
%>
                                            <td style="background:#75A8E2;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_NAME))%>">
                                                    <img src="<%=sortManager.getSortIcon(DirectoryContentSortManager.FIELD_NAME)%>" />
                                                </a>
                                            </td>
<%
        }                                                        
%>        
                                         </tr>
                                    </table>
                                </th>
                                <th nowrap="true" width="5%">
                                    <table class="internalheader" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="background:#75A8E2;padding-right:5px;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_REVISION))%>">
                                                    <%= Localization.getString("svnwebclient.revprops.revision") %>
                                                </a>
                                            </td>
<%
        if (sortManager.hasSortIcon(DirectoryContentSortManager.FIELD_REVISION)) {
%>
                                            <td style="background:#75A8E2;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_REVISION))%>">
                                                    <img src="<%=sortManager.getSortIcon(DirectoryContentSortManager.FIELD_REVISION)%>" />
                                                </a>
                                            </td>
<%
        }                                                        
%>        
                                         </tr>
                                    </table>                                
                                </th>
                                <th nowrap="true" width="5%">
                                    <table class="internalheader" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="background:#75A8E2;padding-right:5px;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_SIZE))%>">
                                                    <%= Localization.getString("svnwebclient.directoryContent.size") %>
                                                </a>
                                            </td>
<%
        if (sortManager.hasSortIcon(DirectoryContentSortManager.FIELD_SIZE)) {
%>
                                            <td style="background:#75A8E2;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_SIZE))%>">
                                                    <img src="<%=sortManager.getSortIcon(DirectoryContentSortManager.FIELD_SIZE)%>" />
                                                </a>
                                            </td>
<%
        }                                                        
%>        
                                         </tr>
                                    </table>                                                                
                                </th>
                                <th nowrap="true" width="5%">
                                    <table class="internalheader" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="background:#75A8E2;padding-right:5px;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_DATE))%>">
                                                    <%= Localization.getString("svnwebclient.revprops.date") %>
                                                </a>
                                            </td>
<%
        if (sortManager.hasSortIcon(DirectoryContentSortManager.FIELD_DATE)) {
%>
                                            <td style="background:#75A8E2;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_DATE))%>">
                                                    <img src="<%=sortManager.getSortIcon(DirectoryContentSortManager.FIELD_DATE)%>" />
                                                </a>
                                            </td>
<%
        }                                                        
%>        
                                         </tr>
                                    </table>                                  
                                </th>
                                <th nowrap="true" width="10%">
                                    <table class="internalheader" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td style="background:#75A8E2;padding-right:5px;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_AUTHOR))%>">
                                                    <%= Localization.getString("svnwebclient.revprops.author") %>
                                                </a>
                                            </td>
<%
        if (sortManager.hasSortIcon(DirectoryContentSortManager.FIELD_AUTHOR)) {
%>
                                            <td style="background:#75A8E2;">  
                                                <a href="<%=EscapeChars.forHTMLTag(sortManager.getSortUrl(DirectoryContentSortManager.FIELD_AUTHOR))%>">
                                                    <img src="<%=sortManager.getSortIcon(DirectoryContentSortManager.FIELD_AUTHOR)%>" />
                                                </a>
                                            </td>
<%
        }                                                        
%>        
                                         </tr>
                                    </table>                                  
                                </th>
                                <th nowrap="true" width="45%">
                                    <%= Localization.getString("svnwebclient.revprops.comment") %>
                                </th>
                            </tr>
<%
		int counter = 0;
        for (int i = 0; i < childs.size(); i++) {
            DirectoryContent.Element element = (DirectoryContent.Element) childs.get(i);
%>            
                            <input type="hidden" name="flags" multiple="yes" value="0" />    
                            <input type="hidden" name="types" multiple="yes" value="<%=element.getImage()%>" />
                            <input type="hidden" name="names" multiple="yes" value="<%=element.getName()%>" />
                            <input type="hidden" name="revisions" multiple="yes" value="<%=element.getRevision()%>" />
                            <input type="hidden" name="sizes" multiple="yes" value="<%=element.getSize()%>" />
                            <input type="hidden" name="dates" multiple="yes" value="<%=element.getDate()%>" />
                            <input type="hidden" name="ages" multiple="yes" value="<%=element.getAge()%>" />
                            <input type="hidden" name="authors" multiple="yes" value="<%=element.getAuthor()%>" />
                            <input type="hidden" name="comments" multiple="yes" value="<%=element.getComment()%>" />
    
                            <tr onmouseover="selectRow(this)" onmouseout="deselectRow(this)">
                                <td style="padding:0; padding-left:2px; padding-right:2px; text-align: center;">
                                    <input name="items" multiple="yes" type="checkbox" />
                                </td>
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
<%
		if (element.isRestricted()) {	
%>       
												<%= Localization.getString("svnwebclient.directoryContent.restrictedAccess") %>
<%
		}
%>             
                                            </td>                                        
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td class="internal" width="16px" style="padding-right:5px;">
<%
            if (element.isHeadRevision()) {
%>
                                                <img src="images/head.gif" alt="<%= Localization.getString("svnwebclient.directoryContent.headRevision") %>" title="<%= Localization.getString("svnwebclient.directoryContent.headRevision") %>" border="0"/>
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
                                            <td class="internal" width="16px" style="padding-right:5px;">
                                                <a href="<%=element.getRevisionListUrl()%>"><img src="images/revlist.gif" alt="<%= Localization.getString("svnwebclient.directoryContent.revisionList") %>" title="<%= Localization.getString("svnwebclient.directoryContent.revisionList") %>" border="0"></img></a>
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
                                <td align="right" nowrap="true">
                                    <%=element.getSize()%>
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
        if (content.getChilds().size() == 0) {
%>
                            <tr>
                                <td colspan="7" align="center">
                                    <b><%= Localization.getString("svnwebclient.directoryContent.empty") %></b>
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
        firstsecond('table_list_of_files');
    </script>
</html>
<%
    }
%>