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
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/jetspeed-tree.tld" prefix="pam" %>
<portlet:defineObjects/>

<portlet:actionURL var="nodeLink" >
	<portlet:param name="node" value="${name}" />
</portlet:actionURL>

<pam:tree tree="j2_tree" images="/j2-admin/images" scope="portlet_request"
          action="<%= nodeLink %>"
          style="tree-control"
          styleSelected="portlet-section-alternate"
  />

<h3 class="portlet-section-subheader">Add User</h3>

<div class="portlet-section-text">

<portlet:actionURL var="addUser" />

<form action="<c:out value="${addUser}"/>" method="post">
<input type='hidden' name='browser.action' value='adduser'/>
<table>
  <tr colspan="2" align="right">
    <td nowrap class="portlet-section-alternate" align="right">New User Name:&nbsp;</td>
    <td class="portlet-section-body" align="left">
      <input type="text" name="jetspeed.user" size="30" value="" class="portlet-form-field-label">
    </td>
  </tr>
  <tr colspan="2" align="right">
    <td nowrap class="portlet-section-alternate" align="right">Password:&nbsp;</td>
    <td class="portlet-section-body" align="left">
      <input type="password" name="jetspeed.password" size="30" value="" class="portlet-form-field-label">
    </td>
  </tr>
  
  <!-- Select Roles -->
  <tr colspan="2" align="right">
    <td nowrap class="portlet-section-alternate" align="right">Default Role:&nbsp;</td>
    <td class="portlet-section-body" align="left">
 		<select name="jetspeedRoles" class="portlet-form-field-label">		
			<option value=""/> 		 		
			<c:forEach var="roleName" items="${jetspeedRoles}">			    
			    <option value="<c:out value='${roleName}'/>"
  			    <c:if test="${roleName == 'user'}">selected="true"</c:if>>			    
				  <c:out value="${roleName}"/>
			    </option>
			</c:forEach>
		</select>      
    </td>
  </tr>

  <!-- Select Profiling Rules -->
  <tr colspan="2" align="right">
    <td nowrap class="portlet-section-alternate" align="right">Profiling Rule:&nbsp;</td>
    <td class="portlet-section-body" align="left">
 		<select name="jetspeedRules" class="portlet-form-field-label">		
			<option value=""/> 		
			<c:forEach var="ruleName" items="${jetspeedRules}">
			    <option value="<c:out value='${ruleName}'/>"
  			    <c:if test="${ruleName == 'role-fallback'}">selected="true"</c:if>>
				  <c:out value="${ruleName}"/>
			    </option>
			</c:forEach>
		</select>      
    </td>
  </tr>
  
</table>
<br/>
<input type="submit" value="Add User" class="portlet-form-button"/>
</form>
<c:if test="${errorMessage != null}">
  <li style="color:red"><c:out value="${errorMessage}"/></li>
</c:if>
  