App.ApplicationController = Ember.Controller.extend({
	error : null,
	project : null,
	canModifyGlobalConfiguration: false,
	canModifyProjectConfiguration: false,
	init: function() {
		var self = this;
		$.get('/polarion/synchronizer/rest/global/canModifyConfiguration').success(function(resp) {
			self.set('canModifyGlobalConfiguration', resp);
		});
	},
	projectObserver: function() {
		var self = this;
		$.get('/polarion/synchronizer/rest/projects/%@/canModifyConfiguration'.fmt(this.get('project'))).success(function(resp) {
			self.set('canModifyProjectConfiguration', resp == true);
		});
	}.observes('project'),
	loadPermission: function(project) {
		
	}.property('project'),
	availableScopes : function() {
		return this.get('canModifyGlobalConfiguration') ? ['_definition.global'.loc(), '_definition.project'.loc()] : ['_definition.project'.loc()];
	}.property('canModifyGlobalConfiguration'),
	noModifyPermission: function() {
		return !(this.get('project') ? this.get('canModifyProjectConfiguration') : this.get('canModifyGlobalConfiguration'));
	}.property('project', 'canModifyProjectConfiguration', 'canModifyGlobalConfiguration'),
	noModifyTooltip: function() {
		return this.get('noModifyPermission') ? '_cannotModifyConfiguration'.loc() : null;
	}.property('noModifyPermission')
});

App.ProxyContributionController = Ember.ResourceController.extend({
	resourceType : App.ProxyContribution,
	contributions : function() {
		return this.content.filter(function(item) {
			return item.proxyType != 'PolarionProxyConfiguration'
					&& App.get(item.proxyType) != null;
		});
	}.property('this.content'),
	connectionContributions : function() {
		return this.content.filter(function(item) {
			console.debug("Connection Type: " + item.connectionType);
			return item.connectionType != null
					&& App.get(item.connectionType) != null;
		});
	}.property('this.content'),
	multiple : function() {
		return this.get('contributions').length > 1;
	}.property('this.contributions'),
});

App.ScopedController = Ember.ResourceController.extend({
	resourceUrl: function() {
		return this.get('resourceType').createResourceUrl(this.get('project'));
	}.property()
});

App.SyncPairsController = App.ScopedController.extend({
	resourceType : App.SyncPair,
	sortProperties : ['id'],
	sortAscending: true,
	error : null,
	project : null,
	find: function() {
		var self = this;
		self.set('loading',true);
		return this.findAll().fail(function(e) {
			App.showError("Failed to load sync pairs: " + e.responseText);
		}).done(function() {
			self.set('loading',false);
		});
	},
	scopeSyncPairs: function() {
		return this.get('isProject') ? this.filterProperty('isGlobal', false) : this;
	}.property('arrangedContent.@each'),
	loading: false,
	load: function(json) {
	    var resource = this.get('resourceType').create().deserialize(json);
	    this.addObject(resource);
	    resource.set('controller', this);
	    resource.set('noModifyPermissionBinding', 'controller.noModifyPermission');
	    resource.set('syncPairs', this.content);
	},
	isProject : function() {
		return this.project != null;
	}.property(),
	noModifyPermissionBinding: 'App.router.applicationController.noModifyPermission',
	noModifyTooltipBinding: 'App.router.applicationController.noModifyTooltip'
});


App.SyncPairController = Ember.Controller.extend({
	init: function() {
		this._super();
	},
	_loadProxy: function(side) {
		var left = this.content.get(side);
		if (left !== undefined) {
			var type = left.get('proxyType');
			//IE 8 Hack
			if((type==undefined)){
				var ie8type=left.toString().slice(2);
				var index=ie8type.indexOf(':');
				type=ie8type.substr(0,index);
				left.set('proxyType',type);
			}
			
			var controller = ProxyConfigurationController.create({
				projectIdBinding: 'App.router.applicationController.project'
			});
			this.connectOutlet({
				outletName : side,
				viewClass : App.get('ProxyConfigurationView'),
				controller : controller,
				context : left
			});
			controller.connectOutlet({
				viewClass : App.get(type + 'View'),
				controller : controller,
				context : left
			});
			this.set(side + 'Controller', controller);
		}
	},
	initializeProxies: function() {
		this._loadProxy('left');
		this._loadProxy('right');
	},
	leftObserver : function() {
		this._loadProxy('left');
	}.observes('content.left.proxyType'),
	rightObserver : function() {
		this._loadProxy('right');
	}.observes('content.right.proxyType'),
	proxiesConfigured: function() {
		var configured = this.leftController.get('metadata') != null && 
				this.rightController.get('metadata') != null;
		if(configured)
			App.router.get('syncPairController').connectOutlet('mapping','mapping', this.content.mapping);
		return configured;
	}.property('leftController.metadata','rightController.metadata'),
	noModifyPermissionBinding: 'App.router.applicationController.noModifyPermission',
	noModifyTooltipBinding: 'App.router.applicationController.noModifyTooltip'
});

App.ConnectionsController = App.ScopedController.extend({
	resourceType : App.Connection,
	selection : null,
	create : false,
	load : function(json) {
		var type = App.get(json.connectionType);
		if (type != null) {
			var resource = type.create({contextProject : this.project}).deserialize(json);
			this.pushObject(resource);
		}
	},
	find: function() {
		var self = this;
		self.set('loading',true);
		return this.findAll().fail(function(e) {
			App.showError("Failed to load connections: " + e.responseText);
		}).done(function() {
			self.set('loading',false);
		});
	},
	clearSelection : function() {
		this.set('idError', null);
		this.set('selection', null);
		this.set('createType', null);
	},
	createType: null,
	loading: false,
  active: function() {
	  return this.filterProperty('enabled', true);
  }.property('arrangedContent')
});

App.CreateConnectionController = Ember.Controller.extend({
	selection : null,
});

App.FieldMappingController = Ember.ObjectController.extend({
	addValueMapping : function(event) {
		this.content.valueMappings.pushObject(App.ValueMapping.create());
	},
	removeValueMapping : function(event) {
		this.content.valueMappings.removeObject(event.context);
	},
	validateValueMappings: function() {
		return this.content.valueMappings.every(function(valueMapping) {
			return valueMapping.validate();
		});
	},
	content: null
});

App.MappingController = Ember.ObjectController.extend({
	leftMetadata: null,
	leftMetadataBinding: 'App.router.syncPairController.leftController.metadata',
	rightMetadata: null,
	rightMetadataBinding: 'App.router.syncPairController.rightController.metadata',
	hierarchySupported: function() {
		return this.leftMetadata != null && this.leftMetadata.hierarchySupported && 
			this.rightMetadata != null &&this.rightMetadata.hierarchySupported;
	}.property('leftMetadata','rightMetadata'),
	hierarchyBidirectional: function() {
		return this.content.hierarchyDirection == 'BIDIRECTIONAL';
	}.property('content.hierarchyDirection'),
	deleteTypeMapping: function(event) {
		var typeMapping = event.context;
		this.get('typeMappings').removeObject(typeMapping);
		var deleteGroup = this.get('fieldMappingGroups').find(function(fieldMappingGroup) {
			return fieldMappingGroup.typeMapping == typeMapping;
		});
		this.get('fieldMappingGroups').removeObject(deleteGroup);
	},
	addTypeMapping: function(event) {
		var typeMapping = App.ValueMapping.create();
		this.content.get('typeMappings').pushObject(typeMapping);
		var fieldMappingGroup = App.FieldMappingGroup.create({
			typeMapping : typeMapping,
			mappingController: this,
			leftMetadataBinding: 'mappingController.leftMetadata',
			rightMetadataBinding: 'mappingController.rightMetadata',
		});
		this.content.fieldMappingGroups.pushObject(fieldMappingGroup);
	}
});

App.EditConnectionDialogController = Ember.ObjectController.extend({
	save: function() {	
		this.set('error',null);
		this.set('idError',null);	

		var connection = this.content;
		
		var error = App.cleanAndCheckId(connection, '_missingConnectionId'.loc(), '_invalidConnectionId'.loc());
		if(error != null) {
			this.set('idError', error);
			return error;
		}
		
		var request = connection.saveResource();
		this.set('validating',true);
		var self = this;
		request.complete(function() {
			self.set('validating',false);
		});
		request.fail(function(e) {
			if (e.status == 409) {
				self.set('idError', '_duplicateConnection'.loc([connection.id]));
			} else	if (e.status == 400) {
				self.set('error', e.responseText);
			} else {
				App.showError(e.responseText);
				self.close();
			}
		});
		request.success(function() {
			self.close();
			App.router.connectionsController.findAll().fail(function(e) {
				App.showError("Failed to load connections: " + e.responseText);
			});
		});
	},
  idError: null,
  hasIdError: function() {
  	return this.idError != null;
  }.property('idError'),
  error: null,
  hasError: function() {
  	return this.error != null;
  }.property('error'),
  header: function() {
  	return this.content.create ? 
  			'_createConnectionHeader2'.loc(this.content.get('proxyName')) : 
  			'_editConnectionHeader'.loc(this.content.id);
  }.property('this.content.create','this.content.id','this.content.proxyName'),
  validating: false,
  show: function() {
	  App.set('hasChanges', true);
	  this.set('visible', true);
  },
  close: function() {
	  App.get('router.connectionsController').find();
	  App.set('hasChanges', false);
	  this.set('visible', false);
  },
  visible: false
});

