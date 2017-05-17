App.showError = function(error) {
	App.router.applicationController.set('error', error);
	$('#errorPopup').modal('show');
};

App.clearError = function() {
	App.router.applicationController.set('error', null);
	$('#errorPopup').modal('hide');
};

App.cleanAndCheckId = function(object, missingMessage, invalidMessage) {
	var id = object.id;
	
	if(id == null) {
		return missingMessage;
	} else {
		id = id.trim();
		
		if(id.length == 0) {
			return missingMessage;
		}
		
		object.set('id', id);
	}
	
	var matches = id.match(/^[\w\d_\- ]+$/i); 
	if(matches == null || matches.length == 0) {
		return invalidMessage;
	}
	
	return null;
};

App.ModalView = Ember.View.extend({
	viewName: Ember.required(),
	elementIdBinding: 'viewName',
	templateNameBinding: 'viewName',
	classNames: ['modal', 'hide'],
	element: function() {
		return $('#' + this.get('elementId'));
	}.property(),
	show: function() {
		this.get('element').modal({
			backdrop: 'static'
		}).modal('show');
	},
	hide : function() {
		this.get('element').modal('toggle');
	},
	didInsertElement : function() {
		this._super();
		var self = this;
		this.get('element').on('hidden', function() {
			self.onHide();
		});
	},
	onHide: function() {
		
	}	
});

App.ApplicationView = Ember.View.extend({
	templateName : 'main',
	noModifyTooltip: function() {
		
	}.property
});

App.ConfigurationController = Ember.Controller.extend({
	error : null,
	project : null
});

App.ConfigurationView = Ember.View.extend({
	templateName : 'configuration'
});



App.SyncPairsView = Ember.View.extend({
	templateName : 'syncPairs'
});

App.CreateSyncPairView = App.ModalView.extend({
	viewName: 'createSyncPair',
	onHide : function() {
		this.set('createId', null);
		this.set('createError', null);
		this.set('configuration', null);
	},
	createErrorBinding : 'syncPair.saveError',
	hasCreateError : function() {
		return this.createError != null;
	}.property('createError'),
	syncPair: null,
	syncPairTemplates : function() {
		var self = this;
		return this.get('syncPairs').filter(function(syncPair) {
			return self.connectionsFor(syncPair).length > 0;
		});
	}.property('syncPairs.@each','connections.@each'),
	validConnections: function() {
		if(this.templateSyncPair) {	
			return this.connectionsFor(this.templateSyncPair);
		} else {
			return this.get('connections');
		}
	}.property('templateSyncPair','connections.@each'),
	noConnections: function() {
		return this.get('validConnections').length <= 0;
	}.property('validConnections'),
	disableCreateReason: function() {
		return this.get('noConnections') ? '_cantCreateNoConnection'.loc() : '';
	}.property('noConnections'),
	connectionsFor: function(syncPair) {
		var proxyType = syncPair.get('right.proxyType');
		var connectionTypes = this.proxyContributions.filterProperty('proxyType', proxyType).getEach('connectionType');
		return this.get('connections').filter(function(connection) {
			return connectionTypes.contains(connection.get('connectionType'));
		});
	},
	proxyContributionsBinding : 'App.router.proxyContributionController.contributions',
	templatePrompt: '_templatePrompt'.loc()
});

App.ProxyConfigurationView = Ember.View.extend({
	templateName : 'proxyConfiguration'
});

App.ConnectionsView = Ember.View.extend({
	templateName : 'connections'
});

App.EditConnectionDialogView = Ember.View.extend({
	templateName : 'editConnectionDialog',
	error: function() {
		return '_cantSaveConnection'.loc(this.get('controller').get('error'));
	}.property('this.controller.error'),
	changeVisibllity: function() {
		var controller = this.get('controller');
		if(controller != null && controller.visible) {
			$('#editConnection').modal({backdrop: 'static'}).modal('show');
		} else {
			$('#editConnection').modal({backdrop: 'static'}).modal('hide');
		}
	}.observes('this.controller.visible')
});

App.SyncPairView = Ember.View.extend({
	templateName : 'syncPair',
	heading: function() {
		return '_syncPairHeading'.loc(this.get('controller').content.id);
	}.property('controller.content.id'),
	subHeading: function() {
		return '_syncPairSubHeading'.loc(this.get('controller').content.right.get('description'));
	}.property('controller.content.right.description')
});

App.TestConnectionView = Ember.View.extend({
	templateName : 'connection'
});

App.CreateConnectionView = App.ModalView.extend({
	templateName : 'createConnection',
	viewName: 'createConnection'
});

App.MappingView = Ember.View.extend({
	templateName : 'mapping'
});

App.EditFieldMappingView = Ember.View.extend({
	templateName : 'fieldMapping',
	heading: function() {
		var content = this.get('controller').content;
		return content == null ? '_fieldMappingHeading'.loc() : 
			'_fieldMappingHeading'.loc(this.get('controller').content.left, this.get('controller').content.right);
		
	}.property('controller.content.left','controller.content.right'),
	check: function() {
		if(this.get('controller').validateValueMappings()) {
			$('#editFieldMapping').modal('hide');
		}
	}
});

App.FieldMappingsView = Ember.View.extend({
	templateName: 'fieldMappings',
	editFieldMapping : function(event) {
		App.router.get('fieldMappingController').set('content',event.context);
		$('#editFieldMapping').modal('show');
	},
	heading: function() {
		return '_forAllTypes'.loc();
	}.property()
});

App.TypeFieldMappingsView = App.FieldMappingsView.extend({
	heading: function() {
		return '_typeSpecificMappingHeading'.loc(
				this.get('controller').get('leftTypeLabel'),
				 this.get('controller').get('rightTypeLabel'));
	}.property('controller.leftTypeLabel','controller.rightTypeLabel')
});

App.Select = Ember.Select.extend({
	_triggerChange: function() {
		var content = this.get('content');
		if(content && this.get('value')) {
			console.debug('Update value.');
			this.valueDidChange();
		}
		this._super();
	},
	selectFirst: false,
//Preserves value when content changes and automatically sets first content value if no value is set
	_loadSelection: function(retry) {
		var content = this.get('content');
		if(!content)
			return;
		
		var valuePath = this.get('optionValuePath').replace(/^content\.?/, '');
		
		var value = this.get('value');
		
		if(!value && !this.get('prompt')) {
			value = content[0] ? content[0].get(valuePath) : null;
		}		
		console.debug("Value " + value);
		var selection = content.find(function(obj) {
			var other = Ember.get(obj, valuePath);
	      return value === other;
	    });
		if(!selection && retry && this.selectFirst) {
			this.set('value', content[0]);
			this._loadSelection(false);
		}
		if(!selection && value) {
//Disabled automatic addition of undefined value
//			console.debug("Value not found in content: " + value);
//			var phantomValue = Ember.Object.create();
//			phantomValue.set(valuePath, value);
//			phantomValue.set(this.get('optionLabelPath').replace(/^content\.?/, ''), value + " (invalid value)");
//			this.get('content').pushObject(phantomValue);
//			if(retry)
//				this._loadSelection(false);
		} else {
			console.debug("Select " + selection);
			this.set('selection', selection);
		}
	},
	contentObserver: function() {
		this._loadSelection(true);
	}.observes('content')
});

App.StyledSelect = Ember.View.extend({
	template: Ember.Handlebars.compile('{{view view.select}}'),
	classNames: ['styled-select'],
	select: App.Select.extend({
		contentBinding: 'parentView.content',
		optionValuePathBinding: 'parentView.optionValuePath',
		optionLabelPathBinding: 'parentView.optionLabelPath',
		valueBinding: 'parentView.value',
		promptBinding: 'parentView.prompt',
		selectFirstBinding: 'parentView.selectFirst',
		disabledBinding: 'parentView.disabled',
		didInsertElement : function() {
			var containerWidth = $(this.get('element.parentNode')).outerWidth();
			this.set("style", "width: " + (containerWidth - 2) + "px; padding-right: 20px;");
		},
		attributeBindings: ['style']
	}),
	optionValuePath: 'content',
	optionLabelPath: 'content'
});

App.DirectionView = App.StyledSelect.extend({
	content : [ Ember.Object.create({
		id : 'BIDIRECTIONAL',
		label : '_bidirectional'.loc(),
	}), Ember.Object.create({
		id : 'LEFT_TO_RIGHT',
		label : '_leftToRight'.loc(),
	}), Ember.Object.create({
		id : 'RIGHT_TO_LEFT',
		label : '_rightToLeft'.loc(),
	}) ],
	optionValuePath : "content.id",
	optionLabelPath : "content.label",
});

App.OptionalDirectionView = App.DirectionView.extend({
	prompt: '_disabled'.loc()
});

App.SideView = App.DirectionView.extend({
	content : [ Ember.Object.create({
		id : 'LEFT_TO_RIGHT',
		label : '_leftToRight'.loc()
	}), Ember.Object.create({
		id : 'RIGHT_TO_LEFT',
		label : '_rightToLeft'.loc()
	}) ]
});

App.DirectionImageView = Ember.View.extend({
	tagName : 'img',
	attributeBindings: ['src'],
	src: function() {
		var direction = this.get('value');
		if(direction == 'LEFT_TO_RIGHT')
			return 'img/leftright.png';
		else if(direction == 'RIGHT_TO_LEFT')
			return 'img/rightleft.png';
		else
			return 'img/bi.png';
	}.property('value'),
	value: null
});

App.HelpView = Ember.View.extend({
   templateName: 'info',
   classNames: ['infobox']
});

App.SaveButton = Ember.View.extend({
	templateName: 'saveButton',
	toggle: function() {
		if(!this.get('disabled')) {
			this.set('expanded', !this.expanded);
		}
	},
	expanded: false,
	classNameBindings: ['expanded'],
	classNames: ['popupButton'],
	save: function(syncPair) {
		if(!this.saveDisabled) {
			App.router.send('save', syncPair);
		}
	},
	saveAs: function(syncPair) {
		this.set('expanded', false);
		this.get('parentView.saveAsDialog').show();
	}
});

App.SaveAsDialog = App.ModalView.extend({
	viewName: 'saveAsDialog',
	init: function() {
		this._super();
		this.set('saveOption', Ember.RadioButtonGroup.create());
	},
	show: function() {
		this.saveOption.set('selectedValue','create');
		this.set('scope', App.get('router.applicationController.availableScopes').get(0));
		this.set('createSyncPairId', null);
		this.set('syncPair', null);
		this._super();
	},
	save: function(event) {
		var create = this.get('isCreate');
		var id = create ? this.get('createSyncPairId') : this.get('updateSyncPair.id');
		var global = create ? this.get('scope') == '_definition.global'.loc() : this.get('updateSyncPair.projectId') == null;
		var newSyncPair = event.context.saveAs(id, global, create);
		this.set('syncPair', newSyncPair);
		var self = this;
		newSyncPair.saveSuccess(function() {
			$('#saveAsDialog').modal('hide');
			if(create) {
				self.get('allPairs').addObject(newSyncPair);
			}
		});
		newSyncPair.saveFail(function() {
			if(newSyncPair.unexpectedSaveError) {
				$('#saveAsDialog').modal('hide');
				App.showError(newSyncPair.saveError);
			}
		});
	},
	isCreate: function() {
		return 'create' == this.get('saveOption.selectedValue');
	}.property('saveOption.selectedValue'),
	isUpdate: function() {
		return !this.get('isCreate');
	}.property('isCreate'),
	compatiblePairs : function() {
		console.log(this.get('allPairs'));
		console.log(this.get('allPairs.arrangedContent'));
		return this.get('allPairs.arrangedContent')
			.filterProperty("left.proxyType", this.get('currentPair.left.proxyType'))
			.filterProperty("right.proxyType", this.get('currentPair.right.proxyType'));
	}.property('allPairs.arrangedContent.@each', 'currentPair')
});

App.ScopeSelect = App.StyledSelect.extend({
	contentBinding : 'App.router.applicationController.availableScopes'
});



