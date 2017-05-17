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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
  <br/>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <c:forEach items="${errorMessages}" var="error">
    <tr>
      <td class="portlet-msg-error"><c:out value="${error}"/></td>
    </tr>
    </c:forEach>
  </table>
 
