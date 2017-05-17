//alert("createPolarionWindow is being loaded..");

function createPolarionWindow(URL, windowTitle)
{
alert("createPolarionWindow " + URL + " " +  windowTitle);

var w = (screen.availWidth-360-250)/2;
var h = (screen.availHeight-210-180)/2;

var c = "<form id='PolarionWindow' name='PolarionWindow' method='post' onsubmit='return false;'>"+
"<table border=2 cellpadding='0' cellspacing='0' style='width:502px;height:402px;border:2px solid #3E5F80; margin: 0;'>"+
"<tr>"+
"<td>"+
/*
"<table valign='top' cellpadding='0' cellspacing='0' width='100%' style='border-top:1px solid #E9F6FF;border-left:1px solid #E9F6FF; border-bottom:0px solid #3E5F80;'>"+
"<tr>"+
"<td class='popupwindowheaderNew'>" + windowTitle + "</td>"+
"<td class='popupwindowheaderNewx'></td>"+
"<td class='popupwindowheaderNewx1'>"+
"<a href='javascript:closePopup();'><img src='$xwiki.getSkinFile("images/popup_close.gif")' border='0'></a></td>"+
"</tr>"+
"</table>"+
"</td>"+
"</tr>"+
"<tr>"+
"<td>"+
"<table border='0' cellpadding='0' cellspacing='0' width='100%' style='border-bottom:0px solid red;'>"+
"<tr>"+
"<td width='477' style='background-color:#778CA5;'></td>"+
"<td class='popupwindow2'></td>"+
"</tr>"+
"</table>"+
*/
"</td>"+
"</tr>"+
"</table>"+
"</form>";

$('popupWindow').innerHTML = c;
$('popupWindow').style.left = w;
$('popupWindow').style.top = h;
$('popupWindow').style.display = '';

new Draggable('popupWindow', {});

}

function closePopup(){
	$('popupWindow').style.display = 'none';
}
