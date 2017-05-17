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
<%@ taglib uri="/WEB-INF/pam.tld" prefix="pam" %>

<%@ page import="java.util.Collection" %>
<%@ page import ="java.util.Map" %>
<%@ page import="org.apache.jetspeed.search.ParsedObject" %>

<portlet:defineObjects/>
<h2 class="portlet-section-header">Portlet Application Manager</h2>

<h3 class="portlet-section-subheader">Search</h3>

<div class="portlet-section-text">

<portlet:actionURL var="searchLink" />
<portlet:actionURL var="searchSelectLink"/>

<form action="<c:out value="${searchLink}"/>" method="post">

	<input type="text" name="query" value="" class="portlet-form-field-label"/>
	<input type="submit" value="Search" class="portlet-form-button"/>

</form>

<c:set var="results" value="${requestScope.search_results}" />

<c:if test="${results != null}">
	<c:forEach var="result" items="${search_results}">
	
		<%
			String name = "";
			ParsedObject po = (ParsedObject)pageContext.findAttribute("result");
			Map fields = po.getFields();
			if(fields != null)
			{
				Object id = fields.get("ID");
		
				if(id != null)
				{
					if(id instanceof Collection)
					{
						Collection coll = (Collection)id;
						name = (String) coll.iterator().next();
					}
					else
					{
						name = (String)id;
					}
				}
				
				if(po.getType().equals("portlet"))
				{
					Object pa = fields.get("portlet_application");
					String paName = "";
					if(pa != null)
					{
						if(id instanceof Collection)
						{
							Collection coll = (Collection)pa;
							paName = (String) coll.iterator().next();
						}
						else
						{
							paName = (String)pa;
						}
					}
					name = paName + "::" + name;
				}
			}
		%>
	
		<a href="<c:out value="${searchSelectLink}"/>?select_node=<%=name%>"><c:out value="${result.title}"/></a> <br />
		 <%-- | <c:out value="${result.description}"/> <br /> --%>
	</c:forEach>
</c:if>

</div>
<h3 class="portlet-section-subheader">Application Tree View</h3>

<div class="portlet-section-text">

<portlet:actionURL var="nodeLink" >
	<portlet:param name="node" value="${name}" />
</portlet:actionURL>

<pam:tree tree="j2_tree" images="/j2-admin/images" scope="portlet_request"
          action="<%= nodeLink %>"
          style="tree-control"
          styleSelected="portlet-section-alternate"
          
  />
  
</div>