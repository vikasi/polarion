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
<%@page import="org.apache.jetspeed.request.RequestContext"%>
<%@page import="org.apache.jetspeed.portlets.security.ChangePasswordPortlet"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<portlet:defineObjects/>
<fmt:setBundle basename="org.apache.jetspeed.portlets.security.resources.ChgPwdResources" />

<c:choose>
  <c:when test="${pageContext.request.userPrincipal != null}">

    <c:set var="whyKey"><%=ChangePasswordPortlet.WHY%></c:set>
    <c:set var="why" value="${requestScope[whyKey]}"/>
    <c:set var="requiredKey"><%=ChangePasswordPortlet.REQUIRED%></c:set>
    <c:set var="required" value="${requestScope[requiredKey]}"/>
    <c:set var="errorMessagesKey"><%=ChangePasswordPortlet.ERROR_MESSAGES%></c:set>
    <c:set var="errorMessages" value="${requestScope[errorMessagesKey]}"/>
    
    <c:if test="${why != null}">
      <i><c:out value="${why}"/></i>
      <br/>
    </c:if>
    <c:if test="${errorMessages != null}">
    <ul>
      <c:forEach items="${errorMessages}" var="error">
        <li style="color:red"><c:out value="${error}"/></li>
      </c:forEach>
    </ul>
    </c:if>

    <c_rt:set var="passwordChangedKey" value="<%=ChangePasswordPortlet.PASSWORD_CHANGED%>"/>
    <c:set var="p" value="${requestScope[passwordChangedKey]}"/>
    <c:if test="${requestScope[passwordChangedKey] != null}">
      <br>
      <i><fmt:message key="chgpwd.message.passwordChanged"/></i>
      <br><br>
    </c:if>

    <form method="POST" action='<portlet:actionURL/>'>
      <table border="0">
      <tr>
        <td><fmt:message key="chgpwd.label.currentPassword"/></td>
        <td><input type="password" size="30" name="<%=ChangePasswordPortlet.CURRENT_PASSWORD%>"></td>
      </tr>
      <tr>
        <td><fmt:message key="chgpwd.label.newPassword"/></td>
        <td><input type="password" size="30" name="<%=ChangePasswordPortlet.NEW_PASSWORD%>"></td>
      </tr>
      <tr>
        <td><fmt:message key="chgpwd.label.newPasswordAgain"/></td>
        <td><input type="password" size="30" name="<%=ChangePasswordPortlet.NEW_PASSWORD_AGAIN%>"></td>
      </tr>
      <tr>
        <td colspan="2">
          <input type="submit" value="<fmt:message key="chgpwd.label.save"/>">
          <c:if test="${why != null}">
            <c:choose>
              <c:when test="${required == null}">
                &nbsp;&nbsp;
                <c_rt:set var="cancelItem" value="<%=ChangePasswordPortlet.CANCELLED%>"/>
                <input type="checkbox" style="display:none" name="<c:out value="${cancelItem}"/>">
                <input type="submit" 
                       value="<fmt:message key="chgpwd.label.cancel"/>"
                       onClick="this.form.<c:out value="${cancelItem}"/>.checked=true">
              </c:when>
              <c:otherwise>
                <br/><br/>
                <c_rt:set var="requestContext" value="<%=request.getAttribute(RequestContext.REQUEST_PORTALENV)%>"/>
                <a href='<c:url context="${requestContext.request.contextPath}" value="/login/logout"/>'><fmt:message key="chgpwd.label.Logout"/></a>
              </c:otherwise>
            </c:choose>
          </c:if>
        </td>
      </tr>
      </table>
    </form>
  </c:when>
  <c:otherwise>
    <fmt:message key="chgpwd.error.notLoggedOn"/><br>
  </c:otherwise>
</c:choose>