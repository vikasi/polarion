/*******************************************************************************
 * Copyright (c) pure-systems GmbH All rights reserved.
 ******************************************************************************/
// set user settings to default value if not yet defined:
if (typeof useHistory === 'undefined') {
        useHistory = true;
}
if (typeof staticBinding === 'undefined') {
        staticBinding = false;
}
if (typeof spellchecking === 'undefined') {
        spellchecking = false;
}
if (typeof saveFMtoLocalStorage === 'undefined') {
        saveFMtoLocalStorage = function() {};
}

var pv_ICON_PATH;

var pv_FEATURE_NAMES = new Array();
var pv_PVSCL_EDITOR;
var pv_FEATURE_NOT_FOUND="FeatureNotFound";
var pv_HINT_KEYWORD_CLASS = "hintkeyword";
var pv_HINT_FEATURE_CLASS = "hintfeature";
var pv_KEYWORD_LIST = [ "AND", "OR", "NOT", "TRUE", "FALSE"];
var pv_KEYWORDS_OBJECT = {
        'AND' : 'keyword',
        'OR' : 'keyword',
        'NOT' : 'keyword',
        'FALSE' : 'keyword',
        'TRUE' : 'keyword',

        'pv:Fail' : 'variable',
        'pv:Warn' : 'variable',
        'pv:Name' : 'variable',
        'pv:Item' : 'variable'
};

var pv_DICTIONARY_OBJECT = {
                length : pv_FEATURE_NAMES.length + pv_KEYWORD_LIST.length,
                getDict : function() {
                        var length = pv_FEATURE_NAMES.length + pv_KEYWORD_LIST.length;
                        var keywords = pv_KEYWORD_LIST.join("\n");
                        var features = pv_FEATURE_NAMES.join("\n");
                        var result = length.toString().concat("\n").concat(keywords).concat(
                        "\n").concat(features);

                        return result;
                },
                update : function() {
                        if (typeof pv_PVSCL_EDITOR != 'undefined') {
                                pv_PVSCL_EDITOR.removeOverlay("typooverlay");
                                registerTypoOverlay();
                                pv_PVSCL_EDITOR.addOverlay("typooverlay");
                        }
                }
};

function setFeatureIconUrl(iconUrl){
	pv_ICON_PATH=iconUrl;
}


function registerFeatureName(featureName){
	if(pv_FEATURE_NAMES.indexOf(featureName)<0){
		pv_FEATURE_NAMES.push(featureName);
	}
}

CodeMirror.defineMode("pvscl", function() {

                return {
                        token : function(stream) {
                                var token_name = "";
                                stream.pos;
                                var c = stream.next();
                                if (c == "'") { // string literal
                                        c = stream.next();
                                        while (c != null && c != "'") {
                                                if (c == "\\")
                                                        stream.next();
                                                c = stream.next();
                                        }
                                        stream.current();
                                        token_name = "string";
                                } else if (/[0-9]/.test(c)) { // number literal
                                    stream.eatWhile(/[0-9]/);
                                    token_name = "atom";
                                } else if (/[A-Za-z_]/.test(c)) { // any word
                                        stream.eatWhile(/[A-Za-z0-9_-]/);
                                        var w = stream.current().toUpperCase();
                                        token_name = pv_KEYWORDS_OBJECT[w];
                                        if (!token_name)
                                                token_name = "variable";
                                        if (w.indexOf(pv_FEATURE_NOT_FOUND.toUpperCase()) == 0)
                                        	 token_name = "feature-not-found";
                                        
                                }
                                return token_name;
                        }
                };
        });

        CodeMirror.defineMIME("text/x-pvscl", "pvscl");

        Array.prototype.foreach = function(callback) {
                for (var k = 0; k < this.length; k++) {
                        callback(k, this[k]);
                }
        };

        CodeMirror.commands.autocomplete = function(cm) {
                CodeMirror.showHint(cm, CodeMirror.hint.pvscl);
        };

        CodeMirror.registerHelper("hint", "pvscl", function(cm, options) {
                var cur = cm.getCursor(), token = cm.getTokenAt(cur);
                var word = token.string.toLowerCase(), start = token.start, end = token.end;
                var result = [];
                

                if (/[^\w$_-]/.test(word)) {
                        word = "";
                        start = end = cur.ch;
                }
                
                function inputMatchesToken(keyword){
                	return keyword.toLowerCase().indexOf(word) > -1;
                }
                
                // addKeywords:
                pv_KEYWORD_LIST.sort(function(a, b) {
                        return a.toLowerCase().localeCompare(b.toLowerCase());
                });
                pv_KEYWORD_LIST.foreach(function(k, v) {
                        if (inputMatchesToken(v))
                                result.push({
                                        text : v+' ',
                                        className : pv_HINT_KEYWORD_CLASS
                                });
                });
                // addFeatures:
                pv_FEATURE_NAMES.sort();
                pv_FEATURE_NAMES.foreach(function(k, v) {
                        if (inputMatchesToken(v))
                                result.push({
                                        text : v+' ',
                                        className : pv_HINT_FEATURE_CLASS
                                });
                });
                /*
                 * addFunctions:
                 *
                 * var funs = [ "pv:Warn()", "pv:Fail()", "pv:Name()", "pv:Item()" ];
                 * funs.sort(function(a, b) { return
                 * a.toLowerCase().localeCompare(b.toLowerCase()); });
                 * funs.foreach(function(k, v) { if (v.lastIndexOf(word, 0) == 0)
                 * result.push({ text : v, className : "hintvariable" }); });
                 */

                if (result.length)
                        return {
                                list : result,
                                from : CodeMirror.Pos(cur.line, start),
                                to : CodeMirror.Pos(cur.line, end)
                        };
        });
