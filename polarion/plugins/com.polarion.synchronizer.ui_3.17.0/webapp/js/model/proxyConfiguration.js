App.ProxyConfiguration = App.Resource.extend({
	proxyType : function() {
		var type=this.constructor.toString();
		//In IE the App prefix gets omitted
		if(type.charAt(0)=='.'){
			return type.slice(1);
		}else{
			return type.slice(4);
		}
	}.property(),
	deserializeProperty : function(prop, value) {
		if (prop != 'connection' || value == null) {
			this._super(prop,value);
		} else {
			var connection = App.Connection.createType(value.connectionType);
			connection.deserialize(value);
			this.set('connection',connection);
		}
	}
});