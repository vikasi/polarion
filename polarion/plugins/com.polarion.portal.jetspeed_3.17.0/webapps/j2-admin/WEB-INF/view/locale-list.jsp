<%--
Copyright 2004 The Apache Software Foundation
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page language="java" import="javax.portlet.*, java.util.List, java.util.Iterator, org.apache.jetspeed.om.common.portlet.MutablePortletApplication" session="true" %>
<%@ taglib uri='/WEB-INF/portlet.tld' prefix='portlet'%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="currentLocale"><%= request.getAttribute("currentLocale") %></c:set>
<fmt:setBundle basename="org.apache.jetspeed.portlets.localeselector.resources.LocaleSelectorResources" />

<portlet:actionURL var="changeLocaleAction">
</portlet:actionURL>

<form action="<%=changeLocaleAction%>" method="POST">
<table border="0">
	<tr>
		<td align="right"><fmt:message key="localeselector.label.language"/></td>
		<td align="left">
			<select name="org.apache.jetspeed.prefered.locale">
				<c:set var="locales"><fmt:message key="localeselector.locales"/></c:set>
				<c:forTokens var="l" items="${locales}" delims=",">
				<option value="<c:out value="${l}"/>"
					<c:if test="${currentLocale == l}">selected</c:if>
				><fmt:message>localeselector.locale.<c:out value="${l}"/></fmt:message></option>
				</c:forTokens>
			</select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2"><input type="submit" value="<fmt:message key="localeselector.label.change"/>"/></td>
	</tr>
</table>
</form>
