<%--
  - Copyright (c) 2004, 2005 Polarion Software, All rights reserved.
  - Email: community@polarion.org
  -
  - This program and the accompanying materials are made available under the
  - terms of the Apache License, Version 2.0 (the "License"). You may not use
  - this file except in compliance with the License. Copy of the License is
  - located in the file LICENSE.txt in the project distribution. You may also
  - obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
  -
  -
  - POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES
  - ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED,
  - INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
  - SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
  - OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
  --%>

<head>
    <meta equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>
        <%=request.getParameter("jsp.head.title")%>
    </title>
    
    <script type="text/javascript" src="script/selection.js" media="screen">
    </script>
    
    <script media="screen" src="script/scroll.js" type="text/javascript">;
    </script>
    
    <script type="text/javascript" src="script/tooltip.js" media="screen">
    </script>        
    
    <script type="text/javascript" src="script/picker.js" media="screen">
    </script>        
	<script type="text/javascript">
		function browserPageLoaded(page, path, parameters){
			try {
    			if (parent && parent != self && parent.browserPageLoaded) {
				    parent.browserPageLoaded(page, path, parameters);
			    }
			} catch(e) {
			}
		}
    </script> 
    
    <link href="style/default.css" type="text/css" rel="stylesheet" media="screen"/>
    <link href="style/table.css" type="text/css" rel="stylesheet" media="screen"/>
    <link href="style/jhighlight.css" type="text/css" rel="stylesheet" media="screen"/>
</head>