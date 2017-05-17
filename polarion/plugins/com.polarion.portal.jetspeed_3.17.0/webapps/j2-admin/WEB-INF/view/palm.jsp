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
<%--
  @author <a href="mailto:ate@douma.nu">Ate Douma</a>
  @version $Id: palm.jsp 348264 2005-11-22 22:06:45Z taylor $
--%>
<%@taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<portlet:defineObjects/>
<script type="text/javascript">
  function doSubmit(a,pa) {
    for (ix = 0; ix < document.forms.length; ix++) {
      var f = document.forms[ix];
      if (f.name == "palmForm") {
        for (iy = 0; iy < f.elements.length; iy++) {
          var el = f.elements[iy];
          if (el.name == "action") el.value = a;
          if (el.name == "value") el.value = pa;
        }
        f.submit();
      }
    }
  }
  function checkSubmit(a,pa,d) { if (confirm("Are you Sure to "+a+" "+pa+"?")) doSubmit(a,pa); return false; }
</script>


<form name="palmForm" action="<portlet:actionURL/>" method="post">
  <input name="action" type="hidden" value="">
  <input name="value" type="hidden" value="">
</form>
<c:if test="${not empty requestScope.statusMsg}">
<br/>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
  <div class="<c:out value="${requestScope.statusMsg.type}"/>"><c:out value="${requestScope.statusMsg.text}"/></div>
</tr>
</table>
</c:if>
<table cellpadding=0 cellspacing=1 border=0 width="100%">
  <tr>
    <th colspan="5" class="portlet-section-header">Portlet Applications</th>
  </tr>
  <tr>
    <th class="portlet-section-subheader">Name</th>
    <th class="portlet-section-subheader">Version</th>
    <th class="portlet-section-subheader">Path</th>
    <th class="portlet-section-subheader">Running</th>
    <th class="portlet-section-subheader">Commands</th>
  </tr>
<c_rt:set var="list" value="<%=renderRequest.getPortletSession().getAttribute("list")%>"/>
<c:forEach var="pa" items="${list}" varStatus="status">
  <c:choose>
    <c:when test="${status.count % 2 != 0}">
      <c:set var="rowClass" value="portlet-section-body"/>
    </c:when>
    <c:otherwise>
      <c:set var="rowClass" value="portlet-section-alternate"/>
    </c:otherwise>
  </c:choose>
  <tr>
    <td class='<c:out value="${rowClass}"/>'>&nbsp;<c:out value="${pa.name}"/></td>
    <td class='<c:out value="${rowClass}"/>'>&nbsp;<c:out value="${pa.version}"/></td>
    <td class='<c:out value="${rowClass}"/>'>&nbsp;<c:out value="${pa.path}"/></td>
    <td style='text-align:center' class='<c:out value="${rowClass}"/>'><c:out value="${pa.running}"/></td>
    <td class='<c:out value="${rowClass}"/>'>
      &nbsp;
      <c:if test="${requestScope.serverManagerAvailable}">
        <c:choose>
          <c:when test="${pa.local}">
            <%-- management of local pa not yet supported --%>
            start&nbsp;&nbsp;stop&nbsp;&nbsp;undeploy&nbsp;
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${pa.running}">
                start&nbsp;&nbsp;<a href="#" onClick='return checkSubmit("stop","<c:out value="${pa.name}"/>")'>stop</a>&nbsp;
              </c:when>
              <c:otherwise>
                <a href="#" onClick='return checkSubmit("start","<c:out value="${pa.name}"/>")'>start</a>&nbsp;&nbsp;stop&nbsp;
              </c:otherwise>
            </c:choose>
            <a href="#" onclick='return checkSubmit("undeploy","<c:out value="${pa.name}"/>")'>undeploy</a>&nbsp;
          </c:otherwise>
        </c:choose>
      </c:if>
      <c:choose>
        <c:when test="${pa.running}">
          delete
        </c:when>
        <c:otherwise>
          <a href="#" onClick='return checkSubmit("delete","<c:out value="${pa.name}"/>")'>delete</a>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</c:forEach>
  <tr>
    <td class='portlet-section-footer' colspan='5'><input type="button" class="portlet-dlg-icon-label" value="refresh" onClick='doSubmit("refresh",null)'></td>
  </tr>
</table>