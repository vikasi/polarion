ProxyConfigurationController = Ember.ObjectController.extend({
    projectId : null,
    loadProxyInstance : function() {
	this.set('loading', true);
	this.set('proxyInstance', null);
	var params = {
	    url : '/polarion/synchronizer/rest/proxyInstance?projectId=' + this.get('projectId'),
	    type : 'POST',
	    dataType : 'text',
	    contentType : 'application/json; charset=utf-8',
	    data : JSON.stringify(this.content.serialize())
	};
	var self = this;
	var req = jQuery.ajax(params);
	req.done(function(resp) {
	    self.set('proxyError', null);
	    self.set('proxyInstance', resp);
	});
	req.error(function(resp) {
	    self.set('proxyError', resp.responseText);
	    self.set('loading', false);
	});

	return req;
    },
    proxyError : null,
    hasProxyError : function() {
	return proxyError != null;
    }.property('proxyError'),
    proxyObserver : function() {
	if (this.get('proxyInstance') != null) {
	    var url = '/polarion/synchronizer/rest/proxyInstance/' + this.proxyInstance + '/metadata';
	    var params = {
		url : url,
		type : 'GET',
		contentType : 'application/json; charset=utf-8',
	    };
	    var self = this;
	    var req = jQuery.ajax(params);
	    req.complete(function() {
		self.set('loading', false);
	    });
	    req.done(function(metadata) {
		console.log('ProxyMetadata received.');

		var sortFields = function(target, key) {
		    if (target[key]) {
			target[key] = target[key].sort(function(a, b) {
			    return a.label == b.label ? 0 : (a.label < b.label) ? -1 : 1;
			});
		    }
		};
		
		sortFields(metadata, 'commonFields');

		if (!!metadata.types) {
		    metadata.types = metadata.types.sort(function(a, b) {
			return a.name == b.name ? 0 : (a.name < b.name) ? -1 : 1;
		    });
		    if(!!metadata.fieldDefinitions) {
			metadata.types.forEach(function (type) {
			    sortFields(metadata.fieldDefinitions, type.id);
			});
		    }
		}

		

		self.set('metadata', metadata);
	    });
	    req.error(function(resp) {
		self.set('proxyError', resp.responseText);
		self.set('proxyInstance', null);
	    });
	} else {
	    this.set('metadata', null);
	}
    }.observes('proxyInstance'),
    proxyInstance : null,
    loading : false,
    configurationOk : function() {
	return this.get('metadata') != null;
    }.property('metadata')
});
