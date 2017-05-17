(function()
{

    Sidebar.prototype.isToggledPalette = function(id) {
        var elts = this.palettes[id];
        if (elts != null) {
            return elts[0].style.display != 'none';
        }
        return false;
    };
    
    var clipartPalletteIds = ['computer', 'finance', 'clipart', 'networking', 'people', 'telco'];
    var palettePrefixes = [ 'mockup', 'bpmn', 'signs', 'rack', 'electrical', 'aws', 'pid', 'cisco', 'signs', 'aws', 'pid' ];
    
 	Sidebar.prototype.getToggledPalettesPrefixes = function() {
 	    var toggledPrefixes = [];
 		for (var id in this.palettes) {
 	        if (this.isToggledPalette(id)) {
 	        	var palettePrefix = getPalettePrefix(id);
 	        	if (toggledPrefixes.indexOf(palettePrefix) < 0) {
 	        		toggledPrefixes.push(palettePrefix);
 	        	}
 	        }
 		}

 		var result = '';
 	    for (var index in toggledPrefixes) {
 	        if (result != '') {
 	        	result = result + ',';
 	        }
 	       result = result + toggledPrefixes[index];
 	    }
 	    return result;
 	};
 	
 	function getPalettePrefix(paletteId) {
     	if (clipartPalletteIds.indexOf(paletteId) >= 0) {
     		return "clipart";
     	}
     	for (var prefix in palettePrefixes) {
     		if (paletteId.indexOf(prefix) == 0) {
     			return prefix;
     		}
     	}
     	return paletteId;
 	}

    Sidebar.prototype.setToggledPalettesPrefixes = function(prefixesStr) {
        var prefixes = prefixesStr.split(',');
        for (var id in this.palettes) {
            var on = false;
            for (var index in prefixes) {
                var prefix = prefixes[index];
                if (id.indexOf(prefix) == 0) {
                    on = true;
                    break;
                }
                if ((prefix == 'clipart') && (clipartPalletteIds.indexOf(id) >= 0)) {
                    on = true;
                    break;
                }
            }
            
            if (this.isToggledPalette(id) != on) {
                this.togglePalette(id);
            }
        }
    };

    Sidebar.prototype.getExpandedPalettes = function() {
        var expandedPaletteIds = '';
        for (var key in this.palettes) {
            if (this.palettes[key] != null) {
                var content = this.palettes[key][1].firstChild;
                if (content.style.display != 'none') {
                    if (expandedPaletteIds != '') {
                        expandedPaletteIds = expandedPaletteIds + ',';
                    }
                    expandedPaletteIds = expandedPaletteIds + key;
                }
            }
        }
        return expandedPaletteIds;
    };

    Sidebar.prototype.setExpandedPalettes = function(expandedPaletteIdsStr) {
        if (expandedPaletteIdsStr != null) {
            var expandedPaletteIds = expandedPaletteIdsStr.split(',');
            for (var key in this.palettes) {
                if (this.palettes[key] != null) {
                    var expand = expandedPaletteIds.indexOf(key) >= 0;
                    this.palettesExpandHandlers[key](null, expand);
                }
            }
        }
    };
    
})();
