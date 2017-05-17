 
var ModalDialogWindow;
var ModalDialogInterval;
var ModalDialog = new Object;

ModalDialog.value = '';
ModalDialog.eventhandler = '';
var ynFormName = '';
var ynInputName = '';
var ynTarget = '';
 
function ModalDialogMaintainFocus()
{
  try
  {
    if (ModalDialogWindow.closed)
     {
        window.clearInterval(ModalDialogInterval);
        eval(ModalDialog.eventhandler);       
        return;
     }
    ModalDialogWindow.focus(); 
  }
  catch (everything) {   }
}
        
 function ModalDialogRemoveWatch()
 {
    ModalDialog.value = '';
    ModalDialog.eventhandler = '';
 }
        
 function ModalDialogShow(Target,BodyText,Title,Buttons,EventHandler)
 {
	
   ModalDialogRemoveWatch();
   ModalDialog.eventhandler = EventHandler;

   var args='width=350,height=175,toolbar=0,';
       args+='location=0,status=0,menubar=0,scrollbars=1,resizable=0';  

   ModalDialogWindow=window.open("","",args); 
   ModalDialogWindow.document.open(); 
   ModalDialogWindow.document.write('<html>');
   ModalDialogWindow.document.write('<head>'); 
   ModalDialogWindow.document.write('<title>' + Title + '</title>');
   ModalDialogWindow.document.write('<script' + ' language=JavaScript>');
   ModalDialogWindow.document.write('function CloseForm(Response) ');
   ModalDialogWindow.document.write('{ ');
   ModalDialogWindow.document.write(' window.opener.ModalDialog.value = Response; ');
   ModalDialogWindow.document.write(' window.close(); ');
   ModalDialogWindow.document.write('} ');
   ModalDialogWindow.document.write('</script' + '>');        
   ModalDialogWindow.document.write('</head>');   
   ModalDialogWindow.document.write('<body onblur="window.focus();">');
   ModalDialogWindow.document.write('<table border=0 width="95%" align=center cellspacing=0 cellpadding=2>');
   ModalDialogWindow.document.write('<tr><td align=left><h2>' + BodyText + '</h2></td></tr>');
   ModalDialogWindow.document.write('<tr><td align=left><h3>' + Target + '</h3></td></tr>');
   ModalDialogWindow.document.write('<tr><td align=left><br></td></tr>');
   ModalDialogWindow.document.write('<tr><td align=center>' + Buttons + '</td></tr>');
   ModalDialogWindow.document.write('</body>');
   ModalDialogWindow.document.write('</html>'); 
   ModalDialogWindow.document.close(); 
   ModalDialogWindow.focus(); 
   ModalDialogInterval = window.setInterval("ModalDialogMaintainFocus()",5);

 }


  function YesNoPopup(message,title,formName,inputName,targetValue,EventHandler)
  {
     var Buttons=''; 
     ynFormName = formName;
     ynInputName = inputName;
     ynTarget = targetValue;
     Buttons = '<a href=javascript:CloseForm("Yes");>Yes</a>  ';
     Buttons += '<a href=javascript:CloseForm("No");>No</a>  ';
     ModalDialogShow(targetValue,message,title,Buttons,EventHandler);
  }
 
 function YesNoReturnMethod()
 {
    if (ModalDialog.value == 'Yes')
    {
    	for (ix = 0; ix < document.forms.length; ix++)
    	{
    	    var tform = document.forms[ix];
    	    if (tform.name == ynFormName)
    	    {
    	    	for (iy = 0; iy < tform.elements.length; iy++)
    	    	{
    	    	    var el = tform.elements[iy];
    	    	    if (el.name == ynInputName)
    	    	    {
    	    	    	el.value = ynTarget;
    	    	    	tform.submit();
    	    	    }
    	    	} 
    	    }
    	}
    }   
    ModalDialogRemoveWatch();
 }

