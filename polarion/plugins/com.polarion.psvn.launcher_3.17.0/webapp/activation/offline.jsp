<% 
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", 0);
%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.core.util.EscapeChars"%>
<%@page import="com.polarion.core.boot.PolarionProperties"%>
<%@page import="com.polarion.psvn.launcher.internal.activation.ActivationData"%>

<% request.setCharacterEncoding("utf-8"); %>

<jsp:include page="includes/head.jsp">
    <jsp:param name="title" value="<%=EscapeChars.forHTMLTag(Localization.getString("activation.offline.title"))%>" />
	<jsp:param name="submitLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.button.submit.activate")) %>" />
	<jsp:param name="submittingLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.button.submit.activating")) %>" />
	<jsp:param name="successLabel" value="<%= EscapeChars.forJavascriptString(Localization.getString("activation.message.activationSuccessful", "/polarion")) %>" />
	<jsp:param name="containerClass" value="container" />
</jsp:include>
<script>

function getDoc(frame) {
    var doc = null;
    try {
        if (frame.contentWindow) {
            doc = frame.contentWindow.document;
        }
    } catch(err) {
    }

    if (doc) { 
        return doc;
    }

    try { 
        doc = frame.contentDocument ? frame.contentDocument : frame.document;
    } catch(err) {
        doc = frame.document;
    }
    return doc;
}

function submitForm() {
	
	 if(window.FormData !== undefined) 
	    {
			var fd = new FormData(document.getElementById("activationForm"));
			$.ajax({
			    type: 'POST',
			    data: fd,
			    success: function(result, textStatus, jqXHR) {
			    			showActivating(false)
			    			result = $.parseJSON(result);
			    			handleResult(result);
			            },
			    error: function(jqXHR, textStatus, errorThrown) {
			    			showActivating(false)
			    			setMessage("<%=EscapeChars.forJavascriptString(Localization.getString("activation.message.serverCommunicationFailed"))%>", true);
			    		},
	   			cache: false,
			    contentType: false,
			    processData: false
			});
	    } else {
	    	var formObj = document.getElementById("activationForm");
	    	var iframe = $("#hidden-frame");
	        var iframeId = 'hidden-frame';
	        formObj.target=iframeId;
	        iframe.on("load", function(e)
	        {
	            var doc = getDoc(iframe[0]);
	            var docRoot = doc.body ? doc.body : doc.documentElement;
	            var data = docRoot.document.body.children[0].textContent;
	            showActivating(false);    
	            data = $.parseJSON(data);
	            handleResult(data);
	        });
	        $("#activationForm").submit();
	    }
}

$(document).ready(function(){
    $("#chooseFileButton").change(function() {
    	validate();
    });
});

function validate() {
    var error = "";
    var path = $("#chooseFileButton").val();
    var enableSubmit = false;
    if (path) {
	    if (path.match(/lic$/)) {
	    	enableSubmit = true;
	   	} else {
	    	error = "<%=EscapeChars.forJavascriptString(Localization	.getString("activation.offline.message.selectLicenseFile"))%>";
			}
		}

		setMessage(error, true);
		setSubmitEnabled(enableSubmit);
		return enableSubmit;
	}
</script>
<div id="heading"><%=Localization.getString("activation.offline.title")%></div>
<form id="activationForm" action="/polarion/activate/offline" method="post" enctype="multipart/form-data">
    <div id="steps">
    	<div class="step">
	    	<%=Localization.getString("activation.offline.steps.1")%>
	    </div>
    	<div class="instructions">
		    <%	ActivationData activationData = ActivationData.getInstance(); %>
		    <%=Localization.getString("activation.offline.steps.1.msg", PolarionProperties.SUPPORT_LICENSE_EMAIL, activationData.getHardwareKey(), activationData.getPolarionVersion())%>
	    </div>
    	<div class="hwKeyDiv">
	    	<%=Localization.getString("activation.offline.steps.hwKey")%> <span class="hwKey"><%=EscapeChars.forHTMLTag(ActivationData.getInstance().getHardwareKey())%></span>
	    </div>
	    <div class="step">
		    <%=Localization.getString("activation.offline.steps.2")%>
	    </div>
		<div class="instructions">
	    	<%=Localization.getString("activation.offline.steps.2.msg")%>
	    </div>
    </div>

	<div class="boxInputLabel">
		<label for="chooseFileButton"><%= Localization.getString("activation.offline.licenseFile")%></label>
	</div>

	<div>
		<input id="chooseFileButton" class="input" type="file" name="licenseFile" size="50" accept=".lic"/>
	</div>
	
	<div class="boxSubmitSeparator">
	</div>
	
    <div id="submitDiv">
    	<input id="submitButton" class="bigButton" type="button" value="<%= EscapeChars.forHTMLTag(Localization.getString("activation.button.submit.activate")) %>" />
    </div>    
</form>
<div class="bottomNote">
	<%= Localization.getString("activation.online.backToOnlineActivation", "/polarion/activate/online") %>
	    	&nbsp;&nbsp;
	<a class="polarionLink" href="<%=System.getProperty("com.polarion.activation.activationHelpLink")%>"><%= Localization.getString("activation.link.activationHelp")%></a>
</div>
<div id="copyright"><%= Localization.getString("login.copyRightMessage") %></div>

<iframe style="display:none;" src="javascript:false;" name="hidden-frame" id="hidden-frame"></iframe> 

