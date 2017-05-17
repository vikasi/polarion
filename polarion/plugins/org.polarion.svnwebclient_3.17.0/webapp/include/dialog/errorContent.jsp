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
                              
<jsp:useBean id="errorBean" scope="request" type="org.polarion.svnwebclient.web.controller.ErrorBean"/>
                 
<%      
    String description = errorBean.getDescription();
    String name = errorBean.getName();
    String email = errorBean.getEmail();
    
    String stacktrace = errorBean.getStacktrace();
    String message = errorBean.getMessage();
    String reportId = errorBean.getReportId();           
%>             
    
<table  cellspacing="0" cellpadding="0" width="100%" height="100%" border="0" class="dialogcontent">
    <form name="error" method="POST" action="review.jsp" height="100%">
        <input type="hidden" name="message" value='<%=message%>'/>  
        <input type="hidden" name="reportId" value='<%=reportId%>'/>  
          
        <tr>
            <td>
                <b>Send Error Report</b>
            </td>
        </tr>                

        </tr>
        <tr>
            <td style="font-size:11px;padding-top:10px;">
                Errors occured during the operation execution. You can send the error report and your comments to the SVNWebClient developers. 
                This is suggested in order to improve product quality and reliability.
                The fields below are optional. You can provide us with your e-mail
				and name to let us keep you informed about the status of the occured problem solving.
				No confidential information is picked from your computer. The report contents are: the
				project ID, JVM properties and the occured exception stack trace.
				You can preview what exactly will the generated report contain.
            </td>
        </tr>
        
        <tr>       
            <td style="padding-top:10px;">
                <b>Name:</b>
            </td>    
        </tr>                    
        
        <tr>                
            <td>
                <input type="text" name="name" size="25" value="<%=name%>" style="margin:0;font-size:11px;"/>
            </td>    
        </tr>    
         
        <tr>    
            <td style="padding-top:10px;">
                <b>E-mail:</b>
            </td>    
        </tr>
       
        <tr>
            <td>
                <input type="text" name="email" size="25" value="<%=email%>" style="width:100%;margin:0;font-size:11px;"/>
            </td>    
        </tr>    

    
        <tr>
            <td style="padding-top:10px;">
                <b>Description:</b>
            </td>
        </tr>

        <tr>
            <td width="100%">    
                <textarea name="description" rows="5" style="width:100%;margin:0;font-size:11px;"><%=description%></textarea>
            </td>    
        </tr>

        <tr>
            <td style="padding-top:10px;">
                <b>Message:</b>
            </td>
        </tr>


        <tr>
            <td>                            
                <%=message%>
            </td>
        </tr>    
       
       <tr>
            <td style="padding-top:10px;">                            
                <b>Stack trace:</b>
            </td>
        </tr> 

        <tr>
            <td height="100%">
                <textarea readonly "id="stacktrace" name="stacktrace" style="width:100%;height:100%;margin:0;font-size:11px;"><%=stacktrace%></textarea>
            </td>
        </tr> 

        <tr>        
            <td width="100%" style="padding-top:20px;padding-bottom:0px;" >
                <table cellspacing="0" cellpadding="0" width="100%" border="0">
                    <tr>
                        <td align="left">
                            <input type="submit" class="button" value="Preview and Send"/>            
                        </td>    
                        <td align="left" style="padding-left:10px;" width="100%">
                            <input type="button" class="button" value="Don't Send" onclick="javascript:window.location='directoryContent.jsp'"/>            
                        </td>    
                    </tr>    
                </table>    
            </td>    
        </tr>           
    </form>
</table>              
