<%@page import="com.polarion.platform.i18n.Localization"%>
		</div>
		<% if("true".equals(request.getParameter("showHelpLink"))) { %>
			<div class="activationHelpLink">
				<a class="polarionLink" href="<%=System.getProperty("com.polarion.activation.activationHelpLink")%>"><%= Localization.getString("activation.link.activationHelp")%></a>
			</div>
		<% } %>
	</div>
	</div>

</body>
</html> 
