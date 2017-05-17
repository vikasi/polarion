		
		function testTemplateLoad()
		{
			alert('Test ok!');
		}
		
		function loadPages(space)
		{
			document.forms['template'].page.options.length = 0;
			for(var i = 0; i < pgs[space].length; i++)
			{
				document.forms['template'].page.options[i] = new Option(pgs[space][i],i);
			}
		}
		
		function loadPages2(space)
		{
			$('page').options.length = 0;
			for(var i = 0; i < pgs[space].length; i++)
			{
				$('page').options[i] = new Option(pgs[space][i],i);
			}
		}
		
		function getArrayItem(arr,num)
		{
			return arr[num];
		}

		function setFocus()
		{

 		        document.forms['template'].title.focus();
		}

		function loadSpaces(isPages)
		{
	   			for(var i = 0; i < spc.length; i++)
 				{
 					document.forms['template'].space.options[i] =  new Option(spc[i],i);
 				}
//					alert(isPages);
				if (isPages)
				{
	 				loadPages(0);
				}			
				document.forms['template'].onshow = setFocus(); 
		}
		
		function loadSpaces2()
		{
			
			for(var i = 0; i < spc.length; i++)
			{
				$('space').options[i] =  new Option(spc[i],i);
			}
			loadPages2(0);
			document.forms['template'].onshow = setFocus(); title.focus();		
		}		
		
		
		function spaceChange()
		{
				loadPages(document.forms['template'].space.selectedIndex);
		}
		
		function copyChange()
		{
			if(document.forms['template'].copy[0].checked)
			{
				document.forms['template'].page.disabled = true;
			}
			else
			{
				document.forms['template'].page.disabled = false;
			}
		}
		
		function getSpace()
		{
			var spcIdx = document.forms['template'].space.selectedIndex;
			return spc[spcIdx];
		}
		
		function getFullName()
		{
			var spcIdx = document.forms['template'].space.selectedIndex;
			var pgsIdx = document.forms['template'].page.selectedIndex;
			return spc[spcIdx] + "." + pgs[spcIdx][pgsIdx];
		}
		
		function getUrl()
		{
			if (!document.forms['template'].page.disabled)
				return initUrl+currAction+tplAction+getFullName();
			else 
				return initUrl+currAction+cpyAction+getSpace();
		}
		
		function yesNo()
		{
			var answer;
			var question;
			
			if (document.forms['template'].page.disabled)
			{
				question = "Copy whole space?";
			}
			else
			{
				//question = "Use this page as template?";
				question = "Overwrite current page with selected page?";
			}
			
			answer = confirm (question);
			if(answer)
			{
				//alert("Go to: " + getUrl());
				window.location = getUrl();
			}
		}
		