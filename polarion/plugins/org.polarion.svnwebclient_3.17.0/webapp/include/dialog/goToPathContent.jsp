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

<jsp:useBean id="bean" scope="request" type="org.polarion.svnwebclient.web.controller.GotoPathBean"/>

<script type="text/javascript" src="script/checkForm.js" media="screen">
</script>   

<table class="dialogcontent" cellspacing="0" cellpadding="0" width="100%" height="100%">
    <form method="POST" name="goto" action="<%=bean.getGotoUrl()%>">    
        <tr>
            <td>
                <b><%= Localization.getString("svnwebclient.goToPathContent.path") %></b>
            </td>
        </tr>
        <tr>
            <td>
                <input type="text" name="filepath" style="margin:0;font-size:11px;width:100%;" value='<%=bean.getPath()%>'/>
            </td>
        </tr>
        <tr>
            <td style="padding-top:10px;">
                <b><%= Localization.getString("svnwebclient.revpropscol.revision") %></b>
            </td>
        </tr>
        
        <tr>
            <td>
                <table class="dialogcontent" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <td>
                            <input name="setRevision" id="head" style="margin:0;" type="radio" value="HEAD" checked onclick="disableField(this)"/>
                        </td>
                        <td style="padding-left:10px;">
                            <%= Localization.getString("svnwebclient.goToPathContent.headRevision") %>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-top:5px;">
                            <input style="margin:0;" name="setRevision" type="radio" value="revisionNumber" onclick="enableField(this)"/>                        
                        </td>
                        <td style="padding-top:5px;padding-left:10px;">
                            <input name="inputRevision" type="text"  id="inputRevision" maxlength="10" style="font-size:11px;width:75px;margin:0;text-align:right"/>
                        </td>
                    </tr>        
                </table>
            </td>
        </tr>
        
                
         <tr>
            <td width="100%" style="padding-top:20px;padding-bottom:0px;">
                <table cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td align="left">
                            <input type="submit" class="button" value="<%= Localization.getString("svnwebclient.generic.ok") %>" onclick='return checkGotoForm(this, "<%= Localization.getString("svnwebclient.revisionListOptions.revisionCount")%>" )'/>                
                        </td>
                        <td align="left" style="padding-left:10px;">
                            <input type="button" class="button" value="<%= Localization.getString("svnwebclient.generic.cancel") %>" onclick="javascript:window.location='<%=bean.getCancelUrl()%>'"/>                
                        </td>      
                        <td width="100%"/>
                    </tr>
                </table>
            </td>    
        </tr>  
                
     <script language="JavaScript">    
        var inputElem = document.getElementById("inputRevision");
        inputElem.disabled = true;
        var headElem = document.getElementById("head");
        headElem.checked = true;        
    </script>      
    </form>    
</table>