App.HashLocation = Ember.HashLocation.extend({
    onUpdateURL : function(callback) {
	var self = this;
	var guid = Ember.guidFor(this);

	Ember.$(window).bind('hashchange.ember-location-' + guid, function() {
	    var path = location.hash.substr(1);
	    if (self.get('lastSetURL') === path) {
		return;
	    }

	    if(App.hasChanges && ! confirm('_discardChanges'.loc())) {
		self.setURL(self.lastSetURL);
	    } else {
		self.set('lastSetURL', path);
		callback(location.hash.substr(1));
		App.set('hasChanges', false);
	    }
	});
    },
});

Ember.Location.registerImplementation('confirmhash', App.HashLocation);

App.MainRoute = Ember.Route.extend({
    route : Ember.required(),
    connectOutlets : function(router, context) {
	    router.get('proxyContributionController').findAll().fail(function(e) {
	    	App.showError("Failed to load proxy contributions:" + e.responseText);
	    });
	},
    index : Ember.Route.extend({
		route : '/',
		connectOutlets : function(router, context) {
		    App.clearError();
		    router.get('syncPairsController').find();
		    router.get('connectionsController').find();
		    var applicationController = router.get('applicationController');
		    applicationController.connectOutlet('configuration');
		    var configurationController = router.get('configurationController');
		    
		    configurationController.connectOutlet('syncPairs', 'syncPairs');
		    configurationController.connectOutlet('connections', 'connections');
		    router.connectionsController.connectOutlet('createConnection', 'createConnection');
		},
		showCreateUpdateConnection : function(router, connection) {
	
		    var controller = App.EditConnectionDialogController.create();
			router.connectionsController.connectOutlet({
				outletName : 'editConnectionDialog',
				controller : controller,
				context : connection,
				viewClass : App.get('EditConnectionDialogView')
		    });
	
		    controller.show();
	
		    var type = connection.get('connectionType');
		    var name = type.charAt(0).toUpperCase() + type.substr(1);
		    controller.connectOutlet({
				outletName : 'connection',
				controller : App.get(name + 'Controller').create(),
				context : connection,
				viewClass : App.get(name + 'View')
		    });
		},
		viewEditConnection : function(router, event) {
		    var connection = event.context;
		    this.showCreateUpdateConnection(router, connection);
		},
		viewCreateConnection : function(router, event) {
			if(router.get('applicationController.noModifyPermission')) {
				return;
			}
			router.get('connectionsController.createConnection').show();
		},
		nextCreateConnection : function(router, event) {
		    var connectionType = router.createConnectionController.selection.connectionType;
		    var connection = App.Connection.createType(connectionType);
		    this.showCreateUpdateConnection(router, connection);
		    connection.set('create', true);
		    connection.set('projectId', router.connectionsController.project);
		    connection.set('contextProject', router.connectionsController.project);
		    connection.set('proxyName', router.createConnectionController.selection.name);
		},
		deleteConnection : function(router, event) {
		    var connection = event.context;
		    if(connection.get('noModifyPermission')) {
				return;
			}
		    if (confirm('_confirmDeleteConnection'.loc(connection.id))) {
			App.clearError();
			var request = connection.destroyResource();
			request.fail(function(e) {
			    App.showError(e.responseText);
			});
			request.success(function() {
			    router.get('connectionsController').findAll().fail(function(e) {
			    	App.showError(e.responseText);
			    });
			});
		    }
		},
		exit: function(router) {
		    $('#createConnection').modal('hide');
		    $('#editConnection').modal('hide');
		    $('#createSyncPair').modal('hide');
		}
    }),
    showSyncPair : Ember.Route.transitionTo('syncPair'),
    showCreateSyncPair: function(router) {
    	if(router.get('syncPairsController.noModifyPermission')) {
    		return;
    	}
    	router.get('configurationController.syncPairs.createSyncPair').show();    	
    },
    create : function(router, event) {
		var view = router.get('configurationController.syncPairs.createSyncPair');

		var proxyType = null;

		var connection = view.connection;
		if (connection == null) {
		    connection = router.connectionsController.content[0];
		}	

		router.proxyContributionController.content.forEach(function(item) {
		    if (item.connectionType == connection.get('connectionType')) {
		    	proxyType = item.proxyType;
		    }
		});

		if (proxyType == null) {
		    view.hide();
		    App.showError("Internal error: Failed to load proxy for connection.");
		}

		var right = App.get(proxyType).create({
		    connection : connection
		});
		
		var template = view.templateSyncPair;
		var syncPair;
		var create;
		if(template) {
			syncPair = template.copy(false);
			syncPair.right.set('connection', connection);
			create = false;
		} else {
			syncPair = App.SyncPair.create();
			syncPair.setProperties({
			    left : App.PolarionProxyConfiguration.create(),
			    right : right
			});
			create = true;
		}
		syncPair.setProperties({
			    id : view.createId,
			    projectId : router.get('applicationController.project'),
			    create : true
		});
		
		view.set('syncPair', syncPair);
		
		App.clearError();
		syncPair.save();

		syncPair.saveSuccess(function() {
			router.syncPairsController.addObject(syncPair);
		    view.hide();
	    	router.transitionTo('syncPair', {
	    		id : syncPair.id,
	    		create : create
	    	});
		});
		syncPair.saveFail(function() {
			if(syncPair.unexpectedSaveError) {
				view.hide();
				App.showError(syncPair.saveError);
			}
		});
    },
    deleteSyncPair : function(router, event) {
		var syncPair = event.context;
		if(syncPair.get('noModifyPermission')) {
			return;
		}
		if (confirm('_confirmDeleteSyncPair'.loc(syncPair.id))) {
		    App.clearError();
		    var request = syncPair.destroyResource();
		    request.fail(function(e) {
			App.showError(e.responseText);
		    });
		    request.success(function() {
			router.get('syncPairsController').findAll().fail(function(e) {
			    App.showError(e.responseText);
			});
		    });
		}
    },
    executeSyncPair : function(router, event) {
	var syncPair = event.context;
	jQuery.ajax({
	    url : syncPair._resourceUrl() + "/execute",
	    type : "POST"
	}).success(function() {
	    syncPair.loadLastJobId();
	}).fail(function(e) {
	    App.showError("Failed to start synchronization: " + e.responseText);
	});
    },
    syncPair : Ember.Route.extend({
		route : '/:id',
		connectOutlets : function(router, context) {
		    var applicationController = router.get('applicationController');
		    var syncPairController = router.get('syncPairController');
		    var syncPair = null;
		    var connect = function() {
				applicationController.connectOutlet('syncPair', syncPair);
				if (syncPairController.proxiesConfigured) {
				    console.debug('Connect mapping');
				    syncPairController.connectOutlet('mapping', 'mapping', syncPair.mapping);
				    router.get('mappingController').connectOutlet('fieldMapping');
				}
		    };
		    var loadMetadata = function() {
		    	if (!context.create) {
				    syncPairController.leftController.loadProxyInstance();
				    syncPairController.rightController.loadProxyInstance();
				}
		    };
		    if (typeof (context.findResource) == 'function') {
				syncPair = context;
				connect();
				syncPairController.initializeProxies();
				loadMetadata();
		    } else {
				syncPair = App.SyncPair.create({
				    id : context.id,
				    projectId : router.get('applicationController.project')
				});
				var request = syncPair.findResource();
				request.fail(function(e) {
				    App.showError(e.responseText);
				});
				request.success(function() {
				    connect();
				    loadMetadata();
				});
		    }
	
		    syncPair.addObserver('modified', function() {
			App.set('hasChanges', syncPair.get('modified'));
		    });
		},
		save : function(router, event) {
		    App.clearError();
		    var save = event.context.saveResource();
		    save.fail(function(e) {
		    	App.showError(e.responseText);
		    });
		    save.success(function(data, textStatus, jqXHR) {
		    	router.send('back');
		    });
		},
		back : function(router, event) {
		    App.set('hasChanges', false);
		    router.transitionTo('index');
		},
		refreshConfiguration : function(router, event) {
		    event.context.loadProxyInstance();
		}
    })
});


App.Router = Ember.Router.extend({
    root : Ember.Route.extend({
		index : Ember.Route.extend({
		    route : '/',
		    connectOutlets : function(router) {
		    	router.get('applicationController').set('error', 'You have to select a project. Example: /polarion/synchronizer/#/projects/playground');
		    }
		}),
		projectConfiguration : App.MainRoute.extend({
			route : '/projects/:project',
			connectOutlets : function(router, context) {
				router.get('syncPairsController').set('project', context.project);
				router.get('connectionsController').set('project', context.project);
				router.get('applicationController').set('project', context.project);
				this._super(router, context);
		    }
		}),
		globalConfiguration : App.MainRoute.extend({route : '/global'})
    })
});

App.Router.reopen({
    location : 'confirmhash'
});