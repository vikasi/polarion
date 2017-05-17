<%@ page language="java" session="true" %>
<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="com.polarion.portal.server.portlet.IFramePortlet" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>
<div>
<portlet:defineObjects/>
<%
	    String url  = (String)renderRequest.getAttribute(IFramePortlet.URL);
	    String height  = (String)renderRequest.getAttribute(IFramePortlet.HEIGHT);
	    if(url!=null){
%>
<iframe src="<%=url%>" frameborder='0' style='padding:0px;margin:0px;height:<%=height%>; width:100%;' scroling='auto'></iframe>	
<% } %>
</div>
