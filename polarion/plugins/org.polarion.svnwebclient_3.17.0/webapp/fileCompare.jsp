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

<%@ page import="org.polarion.svnwebclient.web.model.data.file.FileCompareInfo,
				org.polarion.svnwebclient.web.resource.Images,
				java.util.*" %>                
				                
<jsp:useBean id="bean" scope="request" class="org.polarion.svnwebclient.web.controller.file.FileCompareBean"/>
<%
    if (bean.execute(request, response)) {
	    FileCompareInfo info = bean.getChangeSummary();	    
%>
<html>
    <jsp:include page="include/head.jsp">
        <jsp:param name="jsp.head.title" value="<%= Localization.getString("svnwebclient.fileCompare.title") %>"/>
    </jsp:include>
<%	if (info != null) {   
%>	
		<SCRIPT LANGUAGE="JavaScript">	
			var numbers = new Array(<%=info.getStopPoints().size()%>);	
			var names = new Array(<%=info.getStopPoints().size()%>);	
<%	
		List list = info.getStopPoints();
		for(int i = 0;i < list.size();i++){													
%>										
			numbers[<%=i%>] = <%=((FileCompareInfo.StopPoints)list.get(i)).getLineNumber()%>;
			names[<%=i%>] = '<%=((FileCompareInfo.StopPoints)list.get(i)).getFrameName()%>';					
<%		
		}									
%>
			set(numbers,names);
		</SCRIPT>			
<%
	}
%>		
		<script language="JavaScript">
		  function resize_difftable(){
		    var diffHeight = (document.body.clientHeight - 146);
		    
		    document.getElementById("difftable").style.height = (diffHeight > 0 ? diffHeight : 200) + "px";
		  }
		  
        </script>
        
        
    <body style="height:100%" onLoad="resize_difftable();" onResize="resize_difftable();">
        <table cellspacing="0" cellpadding="0" width="100%" height="100%">
            <tr>
                <td>
                    <jsp:include page="include/top.jsp"/>
                </td>
            </tr>
            <tr>
                <td style="padding:0;spacing:0px; padding-left:5px; padding-right:5px; padding-top:2px;">
                    <jsp:include page="include/compareInfoPanel.jsp">
                        <jsp:param name="jsp.compareInfoPanel.info" value="fileCompareInfo.jsp"/>                       
                    </jsp:include>
                </td>
            </tr>			                                    
<%
	if (info != null) {
%>            
            <tr>
            	<td width="100%" valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:5px;">
            		<table width="100%" height="100%"  class="subinfopanel" height="100%">
	            		<tr>
	            			<td width="100%">
		            			<table>
		            				<tr class="value">	            						            						        				
				        				<td>
				        					<table>
				        						<tr valign="bottom">
				        							<td>
				        								<img style="margin:0;padding:0;border:0;" src="<%=Images.ADDED%>" border="0" width="16px" style="vertical-align:bottom;"/>
				        							</td>
													<td class="value" style="padding-right:20px;" nowrap="true" width="100%">
														 <b><%= Localization.getString("svnwebclient.fileCompare.added") %></b>&nbsp;<%=info.getAddedItemsCount()%>	
													</td>						        						
				        						</tr>
				        					</table>
				        				</td>	
				        				<td>
				        					<table>
				        						<tr valign="bottom">
				        							<td>
				        								<img style="margin:0;padding:0;border:0;" src="<%=Images.MODIFIED%>" border="0" width="16px" style="vertical-align:bottom;"/>
				        							</td>
													<td class="value" style="padding-right:20px;" nowrap="true" width="100%">
														 <b><%= Localization.getString("svnwebclient.fileCompare.changed") %></b>&nbsp; <%=info.getModifiedItemsCount()%>            							
													</td>						        						
				        						</tr>
				        					</table>
				        				</td>
				        				<td>
				        					<table>
				        						<tr valign="bottom">
				        							<td>
				        								<img style="margin:0;padding:0;border:0;" src="<%=Images.DELETED%>" border="0" width="16px" style="vertical-align:bottom;"/>
				        							</td>
													<td class="value" style="padding-right:20px;" nowrap="true" width="100%">
														 <b><%= Localization.getString("svnwebclient.fileCompare.deleted") %></b>&nbsp;<%=info.getDeletedItemsCount()%>            							
													</td>						        						
				        						</tr>
				        					</table>
				        				</td>					        				
				        			</tr>
								</table>
							</td>	
														
							<td align="right" style="padding-right: 20px;">
                                 <jsp:include page="include/options/fileCompareEncodingOptions.jsp"/>
                            </td>    
                            
<!-- Next/Previous buttons -->
										        				
	        				<td align="right">
	        					<table align="right">
	        						<form style="padding:0;margin:0;">
	            						<tr>
	            							<td>        						
												<input type="button" onClick="previous()" value="<%= Localization.getString("svnwebclient.fileCompare.previous") %>" class="button">		            							
											</td>
											<td>
												<input type="button" onClick="next()" value="<%= Localization.getString("svnwebclient.fileCompare.next") %>" class="button">	
											</td>										
										</tr>	
									</form>
								</table>	
	        				</td>            					
	            		</tr>            			
            		</table>
            	</td>
            </tr>            
<%
	} else {
%>	        <tr>
                <td width="100%" valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:5px;">
                    <table width="100%" height="100%"  class="subinfopanel" height="100%">
                        <tr>
                            <td align="right">
                                <jsp:include page="include/options/fileCompareEncodingOptions.jsp"/>                                
                            </td>        
                        </tr>           
                     </table>           
                </td>                                
            </tr>    
<%
    }
%>
    
    
            <tr>            
                <td valign="top" style="padding:0;spacing:0px; padding-left:5px; padding-right:7px;padding-bottom:5px;">            
                    <table id="difftable" style="width:100%;height:100%;" cellpadding="0" cellspacing="0" border="0" rules="all"">
                        <tr style="height:100%">
                            <td style="height:100%;width:50%">
                                <iframe class="frame" width="100%" height="100%" frameBorder="0" name="left_file" id="left_file" <% if (bean.getStartRevisionInfo() != null && bean.getEndRevisionInfo() != null) {%>onload="frame_loaded('left_file', 'right_file');"<%}%> src="<%=bean.getStartRevisionViewUrl()%>">
                                    <a href="<%=bean.getStartRevisionViewUrl()%>"><%= Localization.getString("svnwebclient.fileCompare.content") %></a>
                                </iframe>                                
                            </td>
                            <td style="height:100%;width:50%">
                                <iframe class="frame" width="100%" height="100%" frameBorder="0" name="right_file" id="right_file" <% if (bean.getStartRevisionInfo() != null && bean.getEndRevisionInfo() != null) {%>onload="frame_loaded('left_file', 'right_file');"<%}%> src="<%=bean.getEndRevisionViewUrl()%>">                            
                                    <a href="<%=bean.getEndRevisionViewUrl()%>"><%= Localization.getString("svnwebclient.fileCompare.content") %></a>
                                </iframe>                                                                
                            </td>                            
                        </tr>
                    </table>
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
<%
    }
%>