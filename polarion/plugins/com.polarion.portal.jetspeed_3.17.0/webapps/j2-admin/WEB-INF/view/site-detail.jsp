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

<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri='/WEB-INF/portlet.tld' prefix='portlet'%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pam.tld" prefix="pam" %>

<%@ page import="org.apache.portals.bridges.TabBean" %>
<%@ page import="org.apache.jetspeed.page.document.*" %>

<fmt:setBundle basename="org.apache.jetspeed.portlets.site.resources.SiteResources" />

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<c:set var="selectedTab" value="${requestScope.selected_tab}"/>

<c:if test="${folder == null && page == null}">
	<fmt:message key="site.details.choose_folder_or_page"/>
</c:if>

<div id="tabs">
	<c:set var="tab_items" value="${requestScope.tabs}"/>
	<c:set var="currentTab" value="${selectedTab}"/>
	<c:set var="url_param_name" value="selected_site_tab"/>
	<%@ include file="tabs.jsp"%>
</div>

<c:set var="node" value="${folder}"/>
<c:if test="${page != null}">
	<c:set var="node" value="${page}"/>
</c:if>

<c:if test="${currentTab.id == 'site_details' && node != null}">
	Name (Node): <c:out value="${node.name}"/> <br />
	Path (Node): <c:out value="${node.path}"/> <br />
	Title (BaseElement): <c:out value="${node.title}"/> <br />
	Parent (Node): <c:out value="${node.parent.title}"/> <br />
	ID (BaseElement): <c:out value="${node.id}"/> <br />
	Type (Node): <c:out value="${node.type}"/> <br />
	URL (Node): <c:out value="${node.url}"/> <br />
	ACL (SecuredResource): <c:out value="${node.acl}"/> <br />
	Hidden (Node): <c:out value="${node.hidden}"/> <br />


	<c:choose>
		<c:when test="${folder != null}">
			Default Page (Folder): <c:out value="${folder.defaultPage}"/> <br />
			Default Theme (Folder): <c:out value="${folder.defaultTheme}"/> <br />
			
			
			<c:forEach var="field" items="${folder.metadataFields}">
				<c:out value="${field.name}"/> | <c:out value="${field.value}"/> | <c:out value="${field.locale}"/> <br />
			</c:forEach>
			
			
		</c:when>

		<c:when test="${page != null}">
		
			Name: <c:out value="${page.name}"/> <br />
			Title: <c:out value="${page.title}"/> <br />
			Parent: <c:out value="${page.parent.title}"/> <br />
			
		</c:when>
		

	</c:choose>
</c:if>

<c:if test="${currentTab.id == 'site_security'  && node != null}">
		<portlet:actionURL var="edit_page_link" />

		<form action="<c:out value="${edit_page_link}"/>" method="post">
			<c:if test="${folder != null}">
				<input type="hidden" name="action_type" value="folder"/>
			</c:if>
			<c:if test="${page != null}">
				<input type="hidden" name="action_type" value="page"/>
			</c:if>
			
			<input type="hidden" name="node_name" value="<c:out value="${node.name}"/>"/>
		<table>
			<tr>
				<td>Node Name</td>
				<td><c:out value="${node.name}"/></td>
			</tr>
			<tr>
				<td>Current ACL</td>
				<td>
					<%
						Node node = (Node)pageContext.findAttribute("node");
						out.write("" + node.getAcl());
					%>
				</td>
			</tr>
			<tr>
				<td>New Role</td>
				<td>
					<select name="acl">
						<option value="user">User</option>
						<option value="admin">Admin</option>
						<option value="guest">Guest</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="Add Role"/>
				</td>
			</tr>
		</table>
		</form>
	</c:if>
