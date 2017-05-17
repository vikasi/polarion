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
<%@ page language="java" session="true" %>
<%@ page import="javax.portlet.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.portals.bridges.beans.TabBean" %>
<%@ page import="org.apache.jetspeed.om.common.preference.*" %>
<%@ page import="org.apache.jetspeed.om.common.*" %>
<%@ page import="org.apache.jetspeed.om.common.portlet.*" %>

<%@ taglib uri='/WEB-INF/portlet.tld' prefix='portlet'%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<fmt:setBundle basename="org.apache.jetspeed.portlets.pam.resources.PAMResources" />


<portlet:defineObjects/>

<c:set var="pa" value="${requestScope.portletApplication}" />
<c:set var="name" value="${pa.name}" />
<c:set var="version" value="${pa.version}" />

<c:set var="tabs" value="${requestScope.tabs}"/>
<c:set var="selectedTab" value="${requestScope.selected_tab}"/>
<c:set var="selectedPDef" value="${requestScope.portletDefinition}"/>

<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="portlet-menu-caption">
      Information
    </td>

  </tr>
  <tr>
    <td class="portlet-menu">
      <table border="0" cellspacing="2" cellpadding="0">
        <tr>
          <td class="portlet-menu-item">
<fmt:message key="pam.details.name"/> = <c:out value="${name}"/>
          </td>
        </tr>
        <tr>
          <td class="portlet-menu-item">
<fmt:message key="pam.details.version"/> = <c:out value="${version}"/> 
          </td>
        </tr>
      </table>

    </td>
  </tr>
</table>
<br/>

<div id="tabs">
	<c:set var="tab_items" value="${tabs}"/>
	<c:set var="currentTab" value="${selectedTab}"/>
	<c:set var="url_param_name" value="selected_tab"/>
	<%@ include file="tabs.jsp"%>
</div>

<%--Beginning of Portlets tab data--%>
<%--TODO:  switch to c:choose --%>
<c:if test="${selectedTab.id == 'pa_portlets'}">
  <div id="portlets">
	
	<portlet:actionURL var="select_portlet_link" >
        <%--<portlet:param name="select_portlet" value="<%= pdefName %>" />--%>
    </portlet:actionURL>
	<form action="<c:out value="${select_portlet_link}"/>" method="post">
		<select name="select_portlet" onChange="this.form.submit();" class="portlet-form-field">
		
			<option value="" <c:if test="! ${selectedPDef}"> selected="true"</c:if> >
				<fmt:message key="pam.details.choose_portlet"/>
			</option>

			<c:forEach var="portletDef" items="${pa.portletDefinitions}">
			    <c:set var="pdefName" value="${portletDef.name}"/>
			    
			    <%--We must do this since portlet taglib doesn't support expressions--%>
			    <% String pdefName = (String) pageContext.getAttribute("pdefName"); %>
			    
			    <option value="<c:out value="${portletDef.name}"/>" <c:if test="${selectedPDef.name == portletDef.name}">selected="true"</c:if>>
				  <c:out value="${portletDef.name}"/>
			    </option>
				<%--
			    <a href="<c:out value="${select_portlet_link}"/>">
			        <c:out value="${portletDef.name}" /><br />
			    </a>
			    --%>
			</c:forEach>
		</select>
		
		<!--<input type="submit" value="Select"/>-->
    </form>
  </div>
    
  <div id="selectedPortlet" class="">
	
	<c:if test="${selectedPDef != null}">
		<%@ include file="portlet-detail.jsp" %>
	</c:if>
    
  </div>
</c:if>
<%--End of Portlets tab data--%>

<%--Beginning of UserAttr tab data--%>
<%--TODO:  switch to c:choose --%>
<c:if test="${selectedTab.id == 'pa_user_attribtues'}">
  <div id="Details">
	<portlet:actionURL var="edit_user_attr_link" >
	</portlet:actionURL>
		
	<form name="Edit_UserAttr_Form" action="<c:out value="${edit_user_attr_link}"/>" method="post">
		<input type="hidden" name="portlet_action" value="portlet_app.edit_user_attribute"/>
		
		<table>
			<tr>
				<th class="portlet-section-header">&nbsp;</th>
				<th class="portlet-section-header"><fmt:message key="pam.details.name"/></th>
				<th class="portlet-section-header"><fmt:message key="pam.details.value"/></th>
			</tr>
		<c:forEach var="userAttr" items="${pa.userAttributes}">
			<tr>
			<%--<input type="hidden" name="user_attr_name" value="<c:out value="${userAttr.name}"/>"/>--%>
			
				<td class="portlet-section-body">
					<input type="checkbox" name="user_attr_id" value="<c:out value="${userAttr.name}"/>"/>
				</td>
				<td class="portlet-section-body">
					<c:out value="${userAttr.name}"/>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="<c:out value="${userAttr.name}"/>:description" value="<c:out value="${userAttr.description}"/>" class="portlet-form-field-label"/>
				</td>
			</tr>
		</c:forEach>
		</table>
		
		<input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet_app.edit_user_attribute'" class="portlet-form-button"/>
		<input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet_app.remove_user_attribute'" class="portlet-form-button"/>
	</form>
	
	<form action="<c:out value="${edit_user_attr_link}"/>" method="post">
		<input type="hidden" name="portlet_action" value="portlet_app.add_user_attribute"/>
		
		<table>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.name"/>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="user_attr_name" value="" class="portlet-form-field-label"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.description"/>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="user_attr_desc" value="" class="portlet-form-field-label"/>
				</td>
			</tr>
		</table>
		<input type="submit" value="<fmt:message key="pam.details.add_user_attribute"/>" class="portlet-form-button"/>
	</form>
  </div>
</c:if>
<%--End of UserAttr tab data--%>


<%--Beginning of Metadata tab data--%>
<%--TODO:  switch to c:choose --%>
<c:if test="${selectedTab.id == 'pa_metadata'}">
	<div id="metadata">
		<c:set var="md" value="${pa.metadata}"/>
	
		<portlet:actionURL var="edit_metadata_link" >
			
		</portlet:actionURL>
		<c:set var="action_prefix" value="portlet_app."/>
		
		<%@ include file="metadata-detail.jsp" %>
		
	</div>
</c:if>
<%--End of Metadata tab data--%>

<%--Beginning of Details tab data--%>
<%--TODO:  switch to c:choose --%>
<c:if test="${selectedTab.id == 'pa_details'}">
	<div id="details">
		<table>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.name"/>
                </td>
				<td class="portlet-section-body">
					<c:out value="${name}"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.version"/>
				</td>
				<td class="portlet-section-body">
					<c:out value="${version}"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">		
					<fmt:message key="pam.details.description"/>
				</td>
				<td class="portlet-section-body">
					<c:out value="${pa.description}"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.type"/>
				</td>
				<td class="portlet-section-body">
					<c:choose>
						<c:when test="${pa.applicationType == '0'}">
							<fmt:message key="pam.details.type.webapp"/>
						</c:when>
						<c:when test="${pa.applicationType == '1'}">
							<fmt:message key="pam.details.type.local"/>
						</c:when>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<fmt:message key="pam.details.id"/>
				</td>
				<td class="portlet-section-body">
					<c:out value="${pa.applicationIdentifier}"/>
				</td>
			</tr>
		</table>
	
		
		<c:if test="${! empty pa.jetspeedServices}">
            <br/>
            <table border="0"  cellspacing="0" cellpadding="0">
                <tr>
                    <td class="portlet-menu-caption">
			<fmt:message key="pam.details.services"/>
                    </td>
                </tr>
                <tr>
                    <td class="portlet-menu">
            <div class="portlet-section-body">
			<c:forEach var="service" items="${pa.jetspeedServices}">
				<c:out value="${service.name}"/> <br /> <%--| <c:out value="${service.appId}"/> | <c:out value="${service.id}"/><br />--%>
			</c:forEach>
            </div>
                    </td>
                </tr>
            </table>
		</c:if>
	</div>
</c:if>
<%--End of Details tab data--%>

<br />
<br />


