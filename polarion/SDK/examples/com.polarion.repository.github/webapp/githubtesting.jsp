<%-- <%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.polarion.alm.tracker.model.IWorkItem" %>
<%@ page import="com.polarion.repository.github.GitHubTesting.CommitInformation" %>
<%@ page import="org.json.simple.JSONObject" %> --%>

<%-- <%
	    ArrayList<CommitInformation> commits = (ArrayList<CommitInformation>)request.getAttribute("commits");
		JSONObject responseJSON = (JSONObject) request.getAttribute("json");
%> --%>
Here is the list of commits for the repository.<br />
<%-- <table border='1'>
<tr><th>SHA</th><th>Author</th><th>Date</th></th></tr>
<%

    for (int i = 0; i < commits.size(); i++){
    	CommitInformation commit = commits.get(i);
%>
	<tr><td><%=commit.sha%></td><td><%=commit.author%></td><td><%=commit.date%></td></tr> 
<% } %>
</table>

<%=responseJSON.toJSONString()%> --%>