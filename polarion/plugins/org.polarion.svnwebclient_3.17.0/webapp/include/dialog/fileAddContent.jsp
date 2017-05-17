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

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.file.FileAddBean"/>
<form name="fileAdd" method="POST" enctype="multipart/form-data" action="<%=bean.getOkUrl()%>" onSubmit="if (document.getElementById('filepath').value <=0 ) {alert('<%= Localization.getString("svnwebclient.fileAddContent.alertEnterPath") %>'); return false;} else {return true;}" style="padding:0;margin:0;">
    <table class="dialogcontent" cellspacing="0" cellpadding="0" width="100%" height="100%">
        <tr>
            <td>
                <b><%= Localization.getString("svnwebclient.fileAddContent.filePath") %></b>
            </td>
        </tr>
        <tr>
            <td>
                <input type="file" id="filepath" name="filepath" style="width:100%;margin:0;font-size:11px;"/>
            </td>
        </tr>    
        <tr>
            <td style="padding-top:10px;">
                <b><%= Localization.getString("svnwebclient.revpropscol.comment") %></b>
            </td>
        </tr>
        <tr>
            <td>
                <textarea id="comment" name="comment" rows="5" style="width:100%;margin:0;font-size:11px;"><%= Localization.getString("svnwebclient.fileAddContent.defaultComment") %></textarea>
            </td>
        </tr>    
        <tr>
            <td width="100%" style="padding-top:20px;padding-bottom:0px;">
                <table cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td align="left">
                            <input type="submit" class="button" value="<%= Localization.getString("svnwebclient.generic.ok") %>"/>                
                        </td>
                        <td align="left" style="padding-left:10px;">
                            <input type="button" class="button" value="<%= Localization.getString("svnwebclient.generic.cancel") %>" onclick="javascript:window.location='<%=bean.getCancelUrl()%>'"/>                
                        </td>      
                        <td width="100%"/>
                    </tr>
                </table>
            </td>    
        </tr>    
    </table>
</form>    