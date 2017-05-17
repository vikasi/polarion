/*
            Syntax Attachments Plugin
		   
	     XWiki WYSIWYG Syntax Editor
	Created by Pedro Ornelas for XWiki.org 
	under the Google Summer of Code 2005 program.
*/

WikiEditor.prototype.initAttachmentsPlugin = function() {
	if(!this.isPluginLoaded('core')) {
		alert("Attachment Plugin: You must load the core syntax plugin before!");
		return;
	}
	
	this.addExternalProcessorBefore("convertTableExternal", (/{\s*image\s*:\s*(.*?)(\|(.*?))?(\|(.*?))?(\|(.*?))?(\|(.*?))?}/i), 'convertImageExternal');
	this.addInternalProcessorBefore("convertStyleInternal", (/(<div\s*class=\"img(.*?)\"\s*>\s*)?<img\s*([^>]*)(class=\"wikiimage\")\s*([^>]*)\/>(<\/div>)?/i), 'convertImageInternal');

	this.addExternalProcessorBefore("convertTableExternal", (/{\s*attach\s*:\s*(.*?)(\|(.*?))?}/i), 'convertAttachmentExternal');
    this.addInternalProcessorBefore('convertLinkInternal', (/<a\s*href=\"wikiattachment:-:(.*?)\"\s*([^>]*)>(.*?)<\/a>/i), 'convertAttachmentInternal');

	this.addToolbarHandler('handleAttachmentsButtons');
}

wikiEditor.initAttachmentsPlugin();

WikiEditor.prototype.ATTACHMENT_CLASS_NAME = "";

WikiEditor.prototype.insertAttachment = function(editor_id, title, name) {
    var text = ((title != null) && this.trimString(title) != "") ? title : name;
    this.core.execInstanceCommand(editor_id, "mceInsertRawHTML", false, '<a href="wikiattachment:-:' + escape(name) + '" class="' + this.ATTACHMENT_CLASS_NAME + '">' + text + '<\/a>');
}

WikiEditor.prototype.convertImageInternal = function(regexp, result, content) {
    var str="";
    var href;
    var alt;
    var halign = "";
    var attributes = this.trimString(result[3] + " " + result[5]+ " " + result[6] );
    var att = this.readAttributes(attributes);
    if (result[2] && this.trimString(result[2]) != "") {
        halign = this.trimString(result[2]);
    }

    if(att && (href = att["src"]) != null) {

        href = this.trimString(href);
        var contextPathImage = tinyMCE.getParam("wiki_images_path").toString();
        //href = contextPathImage.substring(0, contextPathImage.indexOf("/",2)) +
        //       "/bin/" + href.substring(href.indexOf("/",12) + 1);
        href = contextPathImage.substring(0, contextPathImage.indexOf("/bin")) +
               "/bin" + href.substring(href.indexOf("/download"));
        //href = unescape(href);
        if (this.trimString(att["alt"]) != "" && att["alt"] != null)
	        alt = unescape(this.trimString(att["alt"]));
		else 
	        alt = href;

		alt = alt.replace(/%20/g, ' ');
        var imgname_reg = new RegExp(this.getImagePath() + "(.*)", "i");
        //var r = imgname_reg.exec(href);

        var r = imgname_reg.exec(alt);
       
		// DMA if path is absolute then do not change it
        if(r || tinyMCE.isPathAbsolute(alt) || tinyMCE.isPathAbsolute(att["id"])) 
        {
	     	var imgname = tinyMCE.isPathAbsolute(alt) ? alt : (r ? r[1] : "")

    	    if(this.trimString(att["id"]) != "" && att["id"] )
	        {
		        imgname = (this.trimString(att["id"]));
			}

			    str = "{image:" + imgname;
		        var width=att["width"] ? this.trimString(att["width"]) : "";
				var height=att["height"] ? this.trimString(att["height"]) : "";
		        var align=att["align"] ? this.trimString(att["align"]) : "";
		        if (width != "" || height != "" || align != "" || halign != "") 
		        {
					str += "|" + (height ? height : " ") + "|" + (width ? width : " ");
		            if (halign && halign != "")
		            {
		                str += "|" + (align ? align : " ") + "|" + (halign ? halign : "");
		            } else if (align != "") {
		                    str += "|" + (align ? align : "");
						   }
		            }
		
		            str += "}";
			}
	}
    return content.replace(regexp, str);	
}

WikiEditor.prototype.IMAGE_CLASS_NAME = "wikiimage";

WikiEditor.prototype.convertImageExternal = function(regexp, result, content) {
    var width, height, align;
    var halign; this.trimString(result[9]);
    var str = "";
    if (result[9]) {
        halign = this.trimString(result[9]);
    } else {
        halign = "";
    }
    if (halign != "") {
        str += "<div class=\"img" + halign + "\">"
    }

	// DMA if path is absolute then do not change it
	if( tinyMCE.isPathAbsolute(result[1]) )
		str += "<img id=\"" + result[1] + "\" class=\"" + this.IMAGE_CLASS_NAME + "\" src=\"" + result[1] + "\" ";
	else
		str += "<img id=\"" + result[1] + "\" class=\"" + this.IMAGE_CLASS_NAME + "\" src=\"" + this.getImagePath() + result[1] + "\" ";
	if( result[5] && (width = this.trimString(result[5])) != "") {
		str += "width=\"" + width + "\" ";
	}
	if( result[3] && (height = this.trimString(result[3])) != "") {
		str += "height=\"" + height + "\" ";
	}

    if( result[7] && (align = this.trimString(result[7])) != "") {
		str += "align=\"" + align + "\" ";
	}
    if(halign != "") {
		str += "halign=\"" + halign + "\" ";
	}

    str += "\/>";
    
    if (halign != "") {
        str += "</div>";
    }
    return content.replace(regexp, str);
}

WikiEditor.prototype.handleAttachmentsButtons = function(editor_id, node, undo_index, undo_levels, visual_aid, any_selection) {
   tinyMCE.switchClass(editor_id + '_image', 'mceButtonNormal');
	do
	{
		switch (node.nodeName.toLowerCase())
		{
			case "img":
					tinyMCE.switchClass(editor_id + '_image', 'mceButtonSelected');
				break;
		}
	} while ((node = node.parentNode));
}

WikiEditor.prototype.getAttachmentsToolbar = function() {
	return this.getAttachmentsControls("image") + this.getAttachmentsControls("attachment");
}

WikiEditor.prototype.getAttachmentsControls = function(button_name) {
	var str="";
	switch(button_name) {
		case 'image':
			str = this.createButtonHTML('image', 'image.gif', 'lang_image_desc', 'mceImage', true);
			break;
		case 'attachment':
			str = this.createButtonHTML('attachment', 'attachment.gif', 'lang_attachment_desc', 'wikiAttachment', true);
			break;
	}
	return str;
}

WikiEditor.prototype.convertAttachmentExternal = function(regexp, result, content) {
    var href = ((typeof(result[3]) == "undefined") || (this.trimString(result[3]) == "")) ? escape(result[1]) : escape(result[3]) ;
    var str = '<a href="wikiattachment:-:' + escape(href) + '" class="' + this.ATTACHMENT_CLASS_NAME + '">' + escape(result[1]) + '<\/a>';
    return content.replace(regexp, str);
}

WikiEditor.prototype.convertAttachmentInternal = function(regexp, result, content) {
    result[1] = result[1].replace(/%20/gi, " ");
    result[3] = result[3].replace(/%20/gi, " ");
    var str;
    if (result[1] == result[3]) str = "{attach:" + result[1] + "}";
    else  if ((result[1] == "undefined") || (this.trimString(result[1]) == "")) str = "{attach:" + result[3] + "}";
    else str = "{attach:" + result[3] + "|" + result[1] + "}";
    return content.replace(regexp, str);
}