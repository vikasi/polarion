<% 
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", 0);
%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="com.polarion.psvn.launcher.internal.activation.ActivationData"%>

<% request.setCharacterEncoding("utf-8"); %>

<jsp:include page="includes/head.jsp">
	<jsp:param name="title" value="<%=EscapeChars.forHTMLTag(Localization.getString("activation.entry.title"))%>" />
	<jsp:param name="submitLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.entry.startTrial")) %>" />
	<jsp:param name="submittingLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.entry.startingTrial")) %>" />
	<jsp:param name="successLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.trialStarted", "/polarion")) %>" />
</jsp:include>

<script>

function submitForm() {
   $.ajax({
       type: 'POST',
       url: "/polarion/activate/entry",
       data: $("#activationForm").serialize(),
       success: function(result, textStatus, jqXHR) {
					showActivating(false);
					result = $.parseJSON(result);
	    			handleResult(result);
               },
       error: function(jqXHR, textStatus, errorThrown) {
       			showActivating(false)
       			setMessage("<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.serverCommunicationFailed")) %>", true);
               },
       cache: false,
       contentType: false,
       processData: false
   });
}

$(document).ready(function(){
    $('input[id=activateButton]').on('click', function() {
    	window.location.href = "/polarion/activate/online";
    });
    $("#productKeyInput").keydown(function() {
    	validate();
    });
    $("#productKeyInput").bind('input propertychange', function() {
    	validate();
	});
});

function validate() {
    setMessage("", true);
    return true;
}
</script> 

<div id="heading"><%=Localization.getString("activation.title")%></div>

<form id="activationForm" action="/polarion/activate/entry" method="post" enctype="application/x-www-form-urlencoded">

    <div id="submit" class="noSpaceBefore">
    	<input id="submitButton" class="bigButton" name="submit" type="button" <%=!ActivationData.getInstance().canActivateTrial() ? "disabled" : ""%> value="<%=Localization.getString("activation.entry.startTrial")%>" />
    </div>
	<div class="boxInfoShort">
        <%
            if (ActivationData.getInstance().canActivateTrial()) {
        %>	
			<%= EscapeChars.forHTMLTag(Localization.getString("activation.entry.startTrial.info")) %>
        <%} else {%>	
			<%= ActivationData.getInstance().getTrialNotAvailableMessage() %>
        <%}%>	
    </div>

	<div class="boxOr">
		<table>
			<tr>
				<td style="width: 50%;"><div class="hr"></div></td>
				<td><%= EscapeChars.forHTMLTag(Localization.getString("activation.entry.or")) %></td>
				<td style="width: 50%;"><div class="hr"></div></td>
			</tr>
	    </table>
    </div>

	<div class="box">
    	<input id="activateButton" class="bigButton" type="button" value="<%= Localization.getString("activation.entry.activate") %>" />
    </div>
	<div class="boxInfoShort">
    	<%= EscapeChars.forHTMLTag(Localization.getString("activation.entry.activate.info")) %>
    </div>
    
    
          
</form>
<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>

<jsp:include page="includes/tail.jsp"/> 
