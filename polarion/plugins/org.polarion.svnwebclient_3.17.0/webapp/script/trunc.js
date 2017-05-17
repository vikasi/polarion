/*
Prerequisites:
1) <head></head> tags pair will include following lines:
<script src="script/trunc.js" type="text/javascript" media="screen"></script>
<script>window.onresize=truncateText;</script>
2) <body onLoad="truncateText();">

Usage:
<table ... width="xx%">
    <tr>
    ...
        <td ... width="xx%">
            <span id="label" name="label" style="width:100%">some text</span>
            <script>
                addTruncItem('label','comment');
            </script>
        </td>
    ...
    </tr>
</table>

For example in infoTab.xml will be included following snippet:
            <td style="padding-left:20px;width:100%">
                <xsl:attribute name="colspan">
                    <xsl:choose>
                        <xsl:when test="$page_type = 'list'">4</xsl:when>
                        <xsl:when test="$page_type = 'path'">4</xsl:when>
                        <xsl:otherwise>3</xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <table cellpadding="0" cellspacing="0" border="0" class="" style="width:100%">
                    <tr width="100%">
                        <td class="value" width="5%" style="padding:0">
                            <b>Comment:</b>
                        </td>
                        <td class="value" width="95%" style="padding:0">
                            <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text><span id="comment_label" name="comment_label" style="width:100%"><xsl:value-of select="head/comment/." disable-output-escaping="yes"/></span>
                        </td>
                    </tr>
                </table>
                <script>
                    addTruncItem('comment_label','comment');
                </script>
            </td>
*/
var items = new Array(0);
var itemTypes = new Array(0);
var originValues = new Array(0);
var typeWidths = new Array(0);
var typeNames = new Array(0);
//letter width depends on following string view
var testString = "  h E qUiCk B r O w N fOx JuMpS oVeR tHe La Z y D o G .";

function addTruncItem(pItemId, pTypeId)
{
    var item = document.getElementById(pItemId);
    items.push(item);
    var itemValue = item.innerHTML;
    originValues.push(itemValue);
    itemTypes.push(pTypeId);
}

function determineLetterWidth(pItem, pTypeId)
{
    var result;
    for (var i = 0; i < typeNames.length; i++)
    {
        if (typeNames[i] == pTypeId)
        {
            if (navigator.appName=="Netscape") {//Gecko fix
                for (var fillStr="",j=0;j<1000;j++,fillStr+="m ");
                pItem.innerHTML = fillStr;
            }
            return typeWidths[i];
        }
    }
    //not found, need calculations
    var originalString = pItem.innerHTML;
    pItem.innerHTML = "T";//one letter already in field (Gecko needs)
    var startHeight = pItem.offsetHeight;
    for (var letterCnt = 1; pItem.offsetHeight == startHeight && letterCnt<1000/*protection from incorrect tag params*/;letterCnt++)
    {
        pItem.innerHTML += testString.charAt(letterCnt%testString.length);
    }

    var letterWidth = pItem.offsetWidth/(letterCnt-2);//1 - first + 1 - last
    typeNames.push(pTypeId);
    typeWidths.push(letterWidth);

    return letterWidth;
}

function truncateText()
{
    for(var i = 0; i < items.length; i++)
    {
        var item = items[i];
        var itemValue = originValues[i];
        var letterWidth = determineLetterWidth(item, itemTypes[i]);

        var letterCnt = Math.floor(item.offsetWidth / letterWidth);
        if (letterCnt < itemValue.length)
        {
            item.innerHTML = itemValue.substr(0, letterCnt - 3) + "...";//"-3" - "..."
        }
        else
        {
            item.innerHTML = itemValue;
        }
    }
}

