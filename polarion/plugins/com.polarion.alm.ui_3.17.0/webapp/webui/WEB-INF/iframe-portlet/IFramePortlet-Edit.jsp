<%@ page language="java" session="true" %>

<%@ page import="java.lang.String" %>
<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="javax.portlet.PortletSession"%>
<%@ page import="com.polarion.portal.server.portlet.IFramePortlet" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>


<portlet:defineObjects/>

<portlet:actionURL var="doConfigure"/>

<c:out value="${message}" />

<%
    String url  = (String)renderRequest.getAttribute(IFramePortlet.URL);
    String title  = (String)renderRequest.getAttribute(IFramePortlet.TITLE);
    String height  = (String)renderRequest.getAttribute(IFramePortlet.HEIGHT);
%>


<FORM ACTION="<%=doConfigure%>" METHOD="POST">   
	Label: <INPUT type="text" name="<%=IFramePortlet.TITLE%>" value="<%=title%>"/>
	<br/>
	<br/>
	URL: <INPUT type="text" name="<%=IFramePortlet.URL%>" value="<%=url%>" size="80" />
	<br/>
	<br/>
	Height: <INPUT type="text" name="<%=IFramePortlet.HEIGHT%>" value="<%=height%>"/>
	<br/>
	<br/>
	<INPUT type="submit" name="<%=IFramePortlet.DO_CONFIGURE%>" value="Save"/>
</FORM>