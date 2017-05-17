<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String error = "";//$NON-NLS-1$
	if (request.getAttribute("exception") != null) {//$NON-NLS-1$ 
		error = (String)request.getAttribute("exception");//$NON-NLS-1$    
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Polarion</title>
<script type="text/javascript">
	window.onunload = closeHandler();
	
	function closeHandler() {
		var error = "<%=error%>";
		window.opener.updatePreview(error);
	}
</script>
</head>

<body onload="window.close()">
	  
</body>
</html>