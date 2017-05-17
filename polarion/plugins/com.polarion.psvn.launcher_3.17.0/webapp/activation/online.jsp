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
	<jsp:param name="title" value="<%=EscapeChars.forHTMLTag(Localization.getString("activation.online.title"))%>" />
	<jsp:param name="submitLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.button.submit.activate")) %>" />
	<jsp:param name="submittingLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.button.submit.activating")) %>" />
	<jsp:param name="successLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.activationSuccessful", "/polarion")) %>" />
	<jsp:param name="onlineActivation" value="true" />
</jsp:include>

<script>

function submitForm() {
	var confirmed = confirm('<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.noOtherInstancesConfirmation")) %>');
	   if(confirmed) {
		   var formData = JSON.stringify($("#activationForm").serializeObject());
		   $.ajax({
			   url: "<%= ActivationData.getInstance().getGeneratorUrl() %>",
			   contentType: "application/json",
			   dataType: "json",
		       type: 'POST',
		       data: formData,
		       success: function(result, textStatus, jqXHR) {
							showActivating(false)
							submitLicense(result);
		               },
		       error: function(jqXHR, textStatus, errorThrown) {
		       			showActivating(false)
		       			switch(jqXHR.status) {
		       				case 0:
		       					var message = {content: "<%= EscapeChars.forJavascriptString(Localization.getString("activation.online.message.activationServerNotReachable", "/polarion/activate/offline"))%>", html: true};
		       					setMessageText(message, true);
		       					break;
		       				case 404:
		       					var message = {content: "<%= EscapeChars.forJavascriptString(Localization.getString("activation.online.message.activationServerNotAvailable", "/polarion/activate/offline"))%>", html: true};
		       					setMessageText(message, true);
		       					break;
		       				default:
		       					var errorText = "<%= EscapeChars.forJavascriptString(Localization.getString("activation.online.message.activationServerCommunicationFailedWithStatus", "/polarion/activate/offline", "%code%")) %>";
		       					setMessage(errorText.replace("%code%", jqXHR.status + " - " + jqXHR.statusText), true);
		       					var message = {content: errorText, html: true};
		       					setMessageText(message, true);
		       					break;
		       			}
		               },
		       cache: false
		   });
	   } else {
	       showActivating(false)
	   }
}

function submitLicense(result) {
		   $.ajax({
		       type: 'POST',
		       data: result,
		       dataType: "json",
		       success: function(result, textStatus, jqXHR) {
							showActivating(false)
			    			handleResult(result);
		               },
		       error: function(jqXHR, textStatus, errorThrown) {
		       			showActivating(false)
		       			setMessage("<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.serverCommunicationFailed")) %>", true);
		               },
		       cache: false
		   });
	}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


$(document).ready(function(){
    $("#productKeyInput").keydown(function() {
    	validate();
    });
    $("#productKeyInput").bind('input propertychange', function() {
    	validate();
	});
	$("#productKeyInput").focus();
});

function validate() {
    var productKey = $("#productKeyInput").val();
    var enableSubmit = false;
    if (productKey) {
    	enableSubmit = true;
    }
    
    setMessage("", true);
    setSubmitEnabled(enableSubmit);
    return enableSubmit;
}
</script> 
<div id="heading"><%=Localization.getString("activation.online.title")%></div>

<form id="activationForm" action="/polarion/activate/online" method="post" enctype="application/x-www-form-urlencoded" >

	<div class="boxInputLabel">
		<label for="productKeyInput"><%= Localization.getString("activation.licenseKey")%></label>
	</div>

	<div class="box">
		<input id="productKeyInput" class="input" type="text" name="licenseKey"/>
	</div>
	<div class="boxInfoLong">
		<%= Localization.getString("activation.licenseKey.info") %>
	</div>
          
	<div class="boxSubmitSeparator">
	</div>

    <div id="submit">
        <input name="hardwareKey" type="hidden" value="<%= ActivationData.getInstance().getHardwareKey() %>"/>
        <input name="productVersion" type="hidden" value="<%= ActivationData.getInstance().getProductVersion() %>"/>
    	<input id="submitButton" class="bigButton" name="submit" type="button" value="<%= Localization.getString("activation.button.submit.activate") %>" />
    </div>
</form>

<div class="bottomNote">
	<%= Localization.getString("activation.online.noInternetConnection", "/polarion/activate/offline") %>
	&nbsp;&nbsp;
	<a class="polarionLink" href="<%=System.getProperty("com.polarion.activation.activationHelpLink")%>"><%= Localization.getString("activation.link.activationHelp")%></a>
</div>
<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>

