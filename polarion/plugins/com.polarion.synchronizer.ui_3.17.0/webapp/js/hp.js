App.HpAlmProxyConfiguration = App.ProxyConfiguration.extend({
	resourceProperties : [ 'proxyType', 'connection', 'domain', 'project',
			'entityType', 'parentId', 'filter' ],
	name : '_hpalm.configurationName'.loc(),
	description : function() {
		return this.connection == null ?
			'_configurationDescriptionNoConnection'.loc() :
			'_hpalm.configurationDescription'.loc(
				this.get('domainLabel'), 
				this.get('projectLabel'),
				this.get('connection.server'));
	}.property('domain', 'project'),
	domainLabel : function() {
		return this.domain == null ? '_hpalm.noDomain'.loc() : this.domain;
	}.property('domain'),
	projectLabel : function() {
		return this.project == null ? '_hpalm.noProject'.loc() : this.project;
	}.property('project')
});

App.HpAlmProxyConfigurationView = Ember.View.extend({
	classNames: ['rows'],
	template : Ember.Handlebars.compile([                                      
			'<div class="control-group">',
			  '<label class="control-label">{{t hpalm.domain}}{{t definition.colon}}</label>', 
			  '<div class="controls">',
			    '{{view Ember.TextField valueBinding="domain"}}', 
			  '</div>', 
			'</div>',
			'<div class="control-group">',
			  '<label class="control-label">{{t hpalm.project}}{{t definition.colon}}</label>', 
			  '<div class="controls">',
			    '{{view Ember.TextField valueBinding="project"}}', 
			  '</div>', 
			'</div>',
			'<div class="control-group">',
			  '<label class="control-label">{{t hpalm.type}}{{t definition.colon}}</label>', 
			  '<div class="controls">',
			    '{{view App.HpTypeView valueBinding="entityType"}}', 
			  '</div>', 
			'</div>',
			'<div class="control-group">',
			  '<label class="control-label">{{t hpalm.parentId}}{{t definition.colon}}</label>',
			  '<div class="controls">',
			    '{{view Ember.TextField valueBinding="parentId"}}', 
			  '</div>',
			'</div>', 
			'<div class="control-group">',
			  '<label class="control-label">{{t hpalm.filter}}{{t definition.colon}}</label>', 
			  '<div class="controls">',
			    '{{view Ember.TextField valueBinding="filter"}}', 
			  '</div>', 
			'</div>'
			  ].join('\n'))
});

App.HpAlmConnection = App.Connection
		.extend({
			resourceProperties : [ 'id', 'connectionType', 'server', 'user', 'password' , 'databaseConnection', 'databaseUser', 'databasePassword', 'adminDBName' ],
			description : function() {
				return '_hpalm.connectionDescription'.loc([this.server]);
			}.property('server')
		});

App.HpAlmProxyConfigurationController = Ember.ObjectController.extend({});

App.HpAlmConnectionController = Ember.ObjectController.extend({});

App.HpAlmConnectionView = Ember.View.extend({
	template : Ember.Handlebars
					.compile('<h4 class="text"><div><div>{{t hpalm.restConnection}}</div></div></h4>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t serverUrl}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.server"  disabledBinding="controller.readOnly"}}'
							+   '</div>'
							+ '</div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t definition.user}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.user"}}'
							+   '</div>'
							+ '</div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t password}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.password" type="password"}}'
							+   '</div>' 
							+ '</div>'
							+ '<h4 class="text"><div><div>{{t hpalm.databaseConnection}}</div></div></h4>'
							+ '<div  class="text"><div><div>{{t hpalm.databaseConnectionHint}}</div></div></div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t hpalm.databaseConnectionString}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.databaseConnection"  disabledBinding="controller.readOnly"}}'
							+   '</div>'
							+ '</div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t hpalm.databaseUser}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.databaseUser"}}'
							+   '</div>'
							+ '</div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t hpalm.databasePassword}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.databasePassword" type="password"}}'
							+   '</div>' 
							+'</div>'
							+ '<div class="control-group">'
							+   '<label class="control-label">{{t hpalm.adminDBName}}{{t definition.colon}}</label>'
							+   '<div class="controls">'
							+     '{{view Ember.TextField valueBinding="controller.content.adminDBName" disabledBinding="controller.readOnly"}}'
							+   '</div>'
							+ '</div>'),
		classNames : ['rows']
		});

App.HpTypeView = App.StyledSelect.extend({
	content : [ Ember.Object.create({
		id : 'REQUIREMENT',
		label : "Requirement"
	}), Ember.Object.create({
		id : 'TEST',
		label : "Test"
	}), Ember.Object.create({
		id : 'DEFECT',
		label : "Defect"
	}) ],
	optionValuePath : "content.id",
	optionLabelPath : "content.label"
});
