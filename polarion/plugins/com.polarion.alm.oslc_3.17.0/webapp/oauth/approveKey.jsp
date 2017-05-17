<!DOCTYPE html>
<%--
 Copyright (c) 2011, 2012 IBM Corporation.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 
 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.
 
 Contributors:
 
    Sam Padgett	  	- initial API and implementation
--%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>
<meta charset="utf-8">
<title>Approve <c:out value="${applicationName}">OAuth</c:out>
	Consumer
</title>
<link type="text/css"
	href="<%=request.getContextPath()%>/oauth/stylesheets/polarion.css"
	rel="stylesheet" />
<jsp:include page="/oauth/common.jsp" />
<script data-dojo-config="async: true" type="text/javascript"
	src="<%=request.getContextPath()%>/oauth/scripts/dojo.js">
	
</script>
<script type="text/javascript">
	require([ "dojo/dom", "dojo/dom-style", "dojo/on", "dojo/_base/event",
			"dojo/_base/xhr", "dojo/ready" ], function(dom, domStyle, on,
			event, xhr, ready) {
		ready(function() {
			on(dom.byId('approveConsumer'), 'submit', function(e) {
				event.stop(e);
				xhr.post({
					url : 'consumers/'
							+ encodeURIComponent(dom.byId('key').value),
					headers : {
						'X-CSRF-Prevent' : lyoOAuthConfig.csrfPrevent
					},
					content : {
						provisional : 'false',
						name : dom.byId('name').value,
						trusted : (dom.byId('trusted').checked) ? 'true'
								: 'false'
					},
					load : function(response) {
						domStyle.set('keyApproved', 'display', 'block');
						domStyle.set('approveConsumer', 'display', 'none');
					},
					error : function() {
						alert('An error occurred.');
					}
				});
			});

			on(dom.byId('deny'), 'click', function(e) {
				event.stop(e);
				xhr.del({
					url : 'consumers/'
							+ encodeURIComponent(dom.byId('key').value),
					headers : {
						'X-CSRF-Prevent' : lyoOAuthConfig.csrfPrevent
					},
					load : function(response) {
						domStyle.set('keyRejected', 'display', 'block');
						domStyle.set('approveConsumer', 'display', 'none');
					},
					error : function() {
						alert('An error occurred.');
					}
				});
			});
		});
	});
</script>
</head>

<body>
	<div id="background">
		<img src="/polarion/ria/images/login/background.jpg" class="stretch" alt="">
	</div>
	
	<img src="/polarion/ria/images/login/siemens_logo.png" alt="Siemens" id="siemensLogo"/>
	
	<div id="container">
		<div id="box">
			<div id="header">
				<div>Siemens PLM Software</div>
				<div id="productName">Polarion<sup style=\"line-height: 0;\">&#174;</sup> ALM<sup style=\"line-height: 0;\">&#8482;</sup></div>
			</div>
			<div id="login">

				<p id="keyApproved" style="display: none;">
				<img src="<%=request.getContextPath()%>/oauth/images/yes.gif">
					You have <b>approved</b> consumer '<c:out value="${consumerName}"/>' with key	<c:out value="${consumerKey}" />.
				</p>

				<p id="keyRejected" style="display: none;">
				<img src="<%=request.getContextPath()%>/oauth/images/no.gif">
					You have <b>rejected</b> consumer '<c:out value="${consumerName}"/>' with key	<c:out value="${consumerKey}" />.
				</p>
				<form id="approveConsumer">
					<div>
						Allow this consumer to access <b><c:out
								value="${applicationName}">Your Application</c:out></b>?
					</div>

					<div id="error" class="error" style="display: hidden;"></div>
					<input type="hidden" id="key" name="key"
						value="<c:out value="${consumerKey}"/>">

					<div>
						<table style="width: 100%;">
							<tr>
								<th align="left"><label for="name">Name:</label></th>
								<td align="left"><input type="text" id="name" name="name"
									required autofocus value="<c:out value="${consumerName}"/>"
									style="width: 100%;" placeholder="Type a name for the consumer"
									required autofocus></td>
							</tr>
							<tr>
								<th align="left"><label for="id">Key:</label></th>
								<td align="left"><c:out value="${consumerKey}" /></td>
							</tr>
							<tr>
								<td colspan="2" align="left"><input type="checkbox"
									id="trusted" name="trusted"
									<c:if test="${trusted}">checked</c:if>> <label
									for="trusted">Trusted</label></td>
							</tr>
						</table>

					</div>

					<div id="submitDiv">
						<input type="submit" value="Allow">
						<button type="button" id="deny">Deny</button>
					</div>
				</form>
			</div>
		</div>
	</div>










</body>

</html>
