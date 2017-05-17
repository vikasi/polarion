App.Resource = Ember.Resource.extend(Ember.Evented, {
    init : function() {
	this._super();
	var modified = Ember.computed(this.checkModified);
	var allProperties = [];
	if (!!this.resourceProperties) {
	    this.resourceProperties.forEach(function(prop) {
		allProperties.push(prop);
	    });
	    this.resourceProperties.forEach(function(prop) {
		allProperties.push(prop + ".modified");
	    });
	    this.resourceProperties.forEach(function(prop) {
		allProperties.push(prop + ".@each");
	    });
	    this.resourceProperties.forEach(function(prop) {
		allProperties.push(prop + ".@each.modified");
	    });
	}
	modified.property.apply(modified, allProperties);
	Ember.defineProperty(this, 'modified', modified);
	if (this.get('doLoad')) {
	    this.load();
	}
    },
    serializeProperty : function(prop) {
	var value = this.get(prop);
	if (value instanceof Array) {
	    var result = [];
	    value.forEach(function(item) {
		item.set('nested', true);
		result.push(item.serialize());
	    });
	    return result;
	} else if (value && value.serialize !== undefined && typeof (value.serialize) == 'function') {
	    value.set('nested', true);
	    return value.serialize();
	} else {
	    return value;
	}
    },
    serialize : function() {
	var result = this._super()[this.resourceName];
	if (!this.nested)
	    console.debug("Serialized: " + JSON.stringify(result));
	return result;
    },
    duplicateProperties : function(source, props) {
	if (props === undefined)
	    props = this.resourceProperties;

	var duplicate = function(value) {
	    if (value) {
		if (value instanceof Array) {
		    var array = [];
		    value.forEach(function(item) {
			array.push(duplicate(item));
		    });
		    return array;
		} else if (typeof (value.copy) == 'function') {
		    return value.copy();
		} else {
		    return value;
		}
	    }
	    return null;
	};

	var _this = this;

	props.forEach(function(p) {
	    _this.set(p, duplicate(source.get(p)));
	});
    },
    isNew : function() {
	return this.create;
    },
    create : false,
    deserialize : function(hash) {
	var result = this._super(hash);
	this.old = this.copy(false);
	return result;
    },
    checkModified : function() {
	var check = function(value, oldValue) {
	    if (value == null)
		value = '';
	    if (oldValue == null)
		oldValue = '';
	    if (typeof (value) == 'object') {
		if (value instanceof Array && oldValue instanceof Array) {
		    if (value.length != oldValue.length)
			return false;
		    for ( var i = 0; i < value.length; i++) {
			if (!check(value[i], oldValue[i]))
			    return false;
		    }
		    return true;
		} else if (typeof (value.get) == 'function') {
		    return !value.get('modified');
		} else {
		    return true;
		}
	    } else {
		return value == oldValue;
	    }
	};
	var self = this;
	var modified = !this.resourceProperties.every(function(prop) {
	    var value = self.get(prop);
	    if (self.old == null)
		  return false;
	    var oldValue = self.old.get(prop);
	    return check(value, oldValue);
	});
	console.debug("Modified: " + modified);
	return modified;
    },
    modified : function() {
	return this.isNew();
    }.property(),
    load : function() {
	console.debug("LOAD: " + this[this.resourceIdField]);
	this.set('loading', true);
	var self = this;
	this.findResource().then(function() {
	    self.set('loading', false);
	}, function(error) {
	    if (error.status == 404) {
		self.set('notFound', true);
	    } else {
		self.set('error', error.responseText);
	    }
	    self.set('loading', false);
	});
    },
    save : function() {
	this.set('saving', true);
	this.set("saveError", null);
	var self = this;
	var req = this.saveResource();
	req.then(function(resp) {
	    self.set("saving", false);
	}, function(resp) {
	    self.set("saving", false);
	    self.set("saveError", resp.responseText);
	    self.set("saveResponse", resp);
	});
	return req;

    },
    saveError : null,
    nestedResourceProperties : {},
    deserializeProperty : function(prop, value) {
	var nested = this.nestedResourceProperties[prop];
	if (!!nested) {
	    var deserialize = function(value) {
		if (!value) {
		    return null;
		} else {
		    var nestedValue = nested.create();
		    return nestedValue.deserialize(value);
		}
	    };
	    var deserialized;
	    if (Ember.isArray(value)) {
		deserialized = [];
		value.forEach(function(item) {
		    deserialized.pushObject(deserialize(item));
		});
	    } else {
		deserialized = deserialize(value);
	    }
	    return this._super(prop, deserialized);
	} else {
	    return this._super(prop, value);
	}
    },
    loadingDone : function() {
	if (!this.loading && !this.error) {
	    if (this.notFound) {
		this.trigger('notFound');
	    } else {
		this.trigger('ready');
	    }
	} else if (!!this.error) {
	    this.trigger('error');
	}
    }.observes('loading')
});

App.Deferred = Ember.Object.extend(Ember.Deferred);

App.Resource.reopenClass({
    find : function(id) {
	var instance = this.create({
	    doLoad : false
	});
	instance.set(instance.resourceIdField, id);
	instance.load();
	return instance;
    }

});