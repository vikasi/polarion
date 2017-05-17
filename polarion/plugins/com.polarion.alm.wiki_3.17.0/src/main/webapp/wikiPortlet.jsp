<%@ page import="javax.portlet.PortletRequest" %>

<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>
<portlet:defineObjects/>

<%
	String url = (String) renderRequest.getAttribute("url");
%>
<iframe src="<%=url%>" style="width: 100%; height: 100%; border: 0px; margin: 0px">
</iframe>
