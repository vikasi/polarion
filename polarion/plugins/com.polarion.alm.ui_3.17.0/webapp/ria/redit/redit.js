var actualEdit;
var init = "no";
var rng;
var comm;

var __isMSIE = navigator.userAgent.indexOf("MSIE")!=-1;

function isMSIE() {
    return __isMSIE;
}

var res_root;

function setREditResourceRoot(root) {
    res_root = root;
}

function toggleDesign(uid) {
    if (isMSIE()) { // == is MSIE ???
        var new_frame = window.document.frames[uid];
		if(new_frame.document.designMode==="off"){
			new_frame.document.designMode = "on";
		}else{
			new_frame.document.designMode = "off";
		}        
	}else{
        var o = document.getElementById(uid).contentWindow;
        if(o.document.designMode==="off"){
	        o.document.designMode = "on";
        }else{
	        o.document.designMode = "off";
	    }	
	}

}
function turnDesign(uid,mode) {
    if (isMSIE()) { // == is MSIE ???
        var new_frame = window.document.frames[uid];
        new_frame.document.designMode = mode;
	}else{
        var o = document.getElementById(uid).contentWindow;
        o.document.designMode = mode;
	}
}
function getEditFrameHTML(uid) {
    return '<iframe id="'+uid+'" name="'+uid+'"' + 
    'frameborder="0" marginwidth="0" class="riframe" marginheight="0" align="top"' +
    'onkeydown="Woo()" onfocus="changeo(\''+uid+'\',false)"></iframe>';
}
function createEditFrame(uid) {
    document.writeln(getEditFrameHTML(uid));
}
function fillEditArea (html, uid) {
    var frameHtml = ""+
    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"+
    "<html>" +
        "<head>"+
            "<style>"+
                "p{margin:0px}"+
                "body {"+
                    "background: white;"+
                    "margin: 0px;"+
                    "padding: 0px;"+
                    "font-family: Arial, Helvetica, sans-serif;"+
                    "font-size: 8pt;"+
                "}"+
            "</style>"+
        "</head>"+
        "<body>"+
            html +
        "</body>"+
    "</html>";
    if (isMSIE()) { // == is MSIE ???
        var new_frame = window.document.frames[uid];
        
        new_frame.document.open();
        new_frame.document.write(frameHtml);
        new_frame.document.close();
        setInterval(function(){updateEditHeight(window.document.frames[uid]);}, 1000);
    }else{
        var o = document.getElementById(uid).contentWindow;
        
        o.document.open();
        o.document.write(frameHtml)
        o.document.close();
        updateEditHeight(o);
    }
    
}
function initEditArea (html, active, uid) {
    fillEditArea(html, uid);
    
    if (isMSIE()) { // == is MSIE ???
        var new_frame = window.document.frames[uid];
        new_frame.document.designMode = "on";
        new_frame.document.attachEvent('onkeydown', Woo);
        new_frame.document.attachEvent('onkeypress', Woo);
        new_frame.document.attachEvent('onkeyup', Woo);
        new_frame.document.attachEvent('onresize', Woo);
        new_frame.document.attachEvent('onmouseup', Woo);
        new_frame.document.attachEvent('onmousemove', Woo);
        new_frame.document.attachEvent('ondrag', Woo);
        new_frame.document.attachEvent('ondragstart', Woo);
        new_frame.document.attachEvent('ondragend', Woo);
        new_frame.document.attachEvent('ondragover', Woo);
        new_frame.document.attachEvent('ondragleave', Woo);
        new_frame.document.attachEvent('ondragenter', Woo);
        new_frame.document.attachEvent('onpaste', Woo);
        //new_frame.document.attachEvent('onmouseover', Woo);
        if (active){
            actualEdit = new_frame;
            //actualEdit.setActive();
            //actualEdit.focus();
            init="ok";
        }
    }else{
        document.getElementById(uid).contentWindow.document.designMode = "on";
        document.getElementById(uid).contentWindow.addEventListener("mousedown", Foo, true)
        document.getElementById(uid).contentWindow.addEventListener("keydown", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("keypress", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("keyup", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("resize", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("mouseup", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("drag", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("dragstart", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("dragend", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("dragover", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("dragleave", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("dragenter", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("paste", Woo, true)
        document.getElementById(uid).contentWindow.addEventListener("mousemove", Woo, true)
        
        if (active){
            actualEdit = document.getElementById(uid).contentWindow;
            //actualEdit.setActive();
            actualEdit.focus();
            init="ok";
        }
    }

}
function createEditArea (html, active, uid) {
    createEditFrame(uid);
    initEditArea (html, active, uid);
}
function Foo(){
    changeo(this.name,false);
}
function updateEditHeight(edit) {
    if (edit!=null && edit.document!=null) {
        if(edit.document.body.scrollHeight > 100){
            document.getElementById(edit.name).style.height = edit.document.body.scrollHeight + 20;
        } else {
            document.getElementById(edit.name).style.height = 100;
        }
    } else if (edit!=null){
        setTimeout(function(){updateEditHeight(edit);}, 50);
    }
}
function Woo(){
    //alert(actualEdit.document.body.scrollHeight);
    updateEditHeight(actualEdit);
}
function insertHTML(html) {
    
    actualEdit.focus(actualEdit);
    if (isMSIE()) {
        var oRng = actualEdit.document.selection.createRange();
        oRng.pasteHTML(html);
        oRng.collapse(false);
        oRng.select();
    } else {
        actualEdit.document.execCommand('insertHTML', false, html);
    }
}
function dlgInsertLink(command) {
    if(1==2){
    actualEdit.document.execCommand("CreateLink",true,'');
    }else{
    
    
    parent.command = command;
    
    InsertLink = popUpWin(res_root+'insert_link.htm', 'InsertLink', 360, 180, '');
    
    
    var linkText = '';
    //if (isMSIE()) {
        //linkText = stripHTML(rng.htmlText);
    //} else {
        //linkText = stripHTML(rng.toString());
    //}
    }
    return false;
}   

function dlgInsertTable(command) {
    
    parent.command = command;
    InsertTable = popUpWin(res_root+'insert_table.htm', 'InsertTable', 360, 180, '');
    return false;
}   

function popUpWin (url, win, width, height, options) {
    var leftPos = (screen.availWidth - width) / 2;
    var topPos = (screen.availHeight - height) / 2;
    options += 'width=' + width + ',height=' + height + ',left=' + leftPos + ',top=' + topPos;
    return window.open(url, win, options);
}


function callFormatting(sFormatString){
    actualEdit.focus();
    actualEdit.document.execCommand(sFormatString,false,null);
    actualEdit.focus();
    return false;
}

function saveAs(){
    actualEdit.focus();
    actualEdit.document.execCommand('SaveAs',true,null);
    actualEdit.focus();
    return false;    
}

function insertImage () {
    actualEdit.focus();
    actualEdit.document.execCommand("InsertImage", true, '');
    actualEdit.focus();
    return false;
}

function changeFontSize(sender){
    var size = sender.value;
    actualEdit.focus();
    actualEdit.document.execCommand("FontSize", false, size);
    actualEdit.focus();
    return false;
}
 

function changeFont(sender){    
    var name = sender.value;
    actualEdit.focus();
    actualEdit.document.execCommand("FontName", false, name);
    actualEdit.focus();
    return false;
}

function changeo(sender,dm){
    if(init == "ok"){
        if (isMSIE()) {
            //actualEdit.document.designMode = "Off";
            actualEdit = window.document.frames[sender];
            if(dm == true){
                if(actualEdit.document.designMode !== "on"){
                    actualEdit.document.designMode = "on";
                }else{
                    actualEdit.document.designMode = "Off";
                    
                }
            }
        }else{
            actualEdit = document.getElementById(sender).contentWindow;
            if(document.getElementById(sender).contentDocument.designMode !== "on"){
                 document.getElementById(sender).contentDocument.designMode = "on";
            }
        }
    }
}

function setRange() {
    
    if (isMSIE()) {
        
        var selection = actualEdit.document.selection; 
        if (selection != null) rng = selection.createRange();
    } else {
        
        var selection = actualEdit.getSelection();
        rng = selection.getRangeAt(selection.rangeCount - 1).cloneRange();
    }
    return rng;
}
function rbuttonOver(sender){
    sender.className='rbutton_over';
}
function rbuttonOut(sender){
    sender.className='rbutton';
}

function dlgColorPalette(sender, command) {
    //function to display or hide color palettes
    setRange();
    
    //get dialog position
    var oDialog = document.getElementById('cp');
    var iLeftPos = sender.offsetLeft;
    var iTopPos = sender.offsetTop + (sender.offsetHeight + 4);
    oDialog.style.left = (iLeftPos) + "px";
    oDialog.style.top = (iTopPos) + "px";
    
    
        if (oDialog.style.visibility == "hidden") {
            showHideElement(oDialog, 'show');
        } else {
            showHideElement(oDialog, 'hide');
        }
    
    comm = command;
    return false;
}

function setColor(color) {
    
    var parentCommand = comm;
    
    if (isMSIE()) {
        if (parentCommand == "hilitecolor") parentCommand = "backcolor";
        
        //retrieve selected range
        rng.select();
    }
    
    actualEdit.document.execCommand(parentCommand, false, color);
    showHideElement('cp', "hide");
    return false;
}

function showHideElement(element, showHide) {
    if (document.getElementById(element)) {
        element = document.getElementById(element);
    }
    
    if (showHide == "show") {
        element.style.visibility = "visible";
    } else if (showHide == "hide") {
        element.style.visibility = "hidden";
    }
}
function createRButton(on_click, img, title) {
    return '<button class="rbutton" title="'+title+'" onclick="'+on_click+';return false;" onmouseover="rbuttonOver(this)" onmouseout="rbuttonOut(this)">' +
               '<img src="'+res_root+'images/'+img+'" alt="'+title+'" border="0" align="absmiddle">' +
           '</button>\n';
}

function createRPanel (){
    var panel = '<div id="rpanel" class="rpanel" style="width:100%">'+
        createRButton("callFormatting('Cut');", "UI_tool-cut.gif", "Cut")+
        createRButton("callFormatting('Copy');", "UI_form-copy.gif", "Copy")+
        createRButton("callFormatting('Paste');", "UI_paste.gif", "Paste")+
        createRButton("callFormatting('Undo');", "UI_undo.gif", "Undo")+
        createRButton("callFormatting('Redo');", "UI_redo.gif", "Redo")+
        createRButton("callFormatting('Bold');", "UI_bold.gif", "Bold")+
        createRButton("callFormatting('Italic');", "UI_italic.gif", "Italic")+
        createRButton("callFormatting('Underline');", "UI_underline.gif", "Underline")+
        createRButton("dlgColorPalette(this,'forecolor');", "UI_text.gif", "Text Color")+
//         '<select class="rselect" onchange="changeFontSize(this);">'+
//            '<option value="6pt"> 6 pt</option>'+
//            '<option value="8pt"> 8 pt</option>'+
//            '<option value="10pt">10 pt</option>'+
//            '<option value="10pt">12 pt</option>'+
//            '<option value="10pt">14 pt</option>'+
//        '</select>\n'+
//        '<select class="rselect" onchange="changeFont(this)">'+
//             '<option value="Arial, Helvetica, sans-serif">Arial</option>'+
//             '<option value="Courier New, Courier, mono">Courier New</option>'+
//             '<option value="Times New Roman, Times, serif">Times New Roman</option>'+
//             '<option value="Verdana, Arial, Helvetica, sans-serif">Verdana</option>'+
//        '</select>\n'+
//        createRButton("callFormatting('StrikeThrough');", "UI_form-strike.gif", "Strike")+
//        createRButton("callFormatting('SuperScript');", "UI_superscript.gif", "Super Script")+
//        createRButton("callFormatting('SubScript');", "UI_subscript.gif", "Sub Script")+
//        createRButton("callFormatting('InsertOrderedList');", "UI_numberlist.gif", "Ordered List")+
        createRButton("callFormatting('InsertUnorderedList');", "UI_bulletlist.gif", "Unordered List")+
        createRButton("callFormatting('JustifyLeft');", "UI_leftalign.gif", "Justify Left")+
        createRButton("callFormatting('JustifyCenter');", "UI_centeralign.gif", "Justify Center")+
        createRButton("callFormatting('JustifyRight');", "UI_rightalign.gif", "Justify Right")+
//        createRButton("dlgInsertLink('link');", "UI_link.gif", "Insert Link")+
//        createRButton("callFormatting('InsertHorizontalRule');", "UI_hr.gif", "Horizontal Rule")+
//        createRButton("dlgInsertTable('table');", "UI_table.gif", "Insert Table")+
//        createRButton("dlgColorPalette(this,'hilitecolor');", "UI_back.gif", "Background Color")+
        '</div>\n'+
        '<iframe width="154" height="104" id="cp" src="'+res_root+'palette.htm" marginwidth="0" marginheight="0" frameborder=0 scrolling="no" style="visibility:hidden;z-index:1000; position: absolute;"></iframe>\n';
        
    document.writeln(panel);
}

function getREditValue(uid) {
    var edit;
    if(isMSIE()){
        edit = window.document.frames[uid];
    }else{
        edit = document.getElementById(uid).contentWindow;
    }
    return edit.document.body.innerHTML;
}
