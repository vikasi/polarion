App.JiraProxyConfiguration = App.ProxyConfiguration.extend({
	resourceProperties : [ 'proxyType', 'project', 'query', 'issueType', 'connection'],
	type : "",
	description : function() {
		return this.connection == null ? 
				'_configurationDescriptionNoConnection'.loc() :
				this.project == null ? 
						'_jira.configurationDescriptionNoProject'.loc(this.connection.serverUrl) :
						'_jira.configurationDescription'.loc(this.project, this.connection.serverUrl);
	}.property('project','connection'),
	name : '_jira.configurationName'.loc(),
	connection: null
});

App.JiraProxyConfigurationView = Ember.View.extend({
	templateName : 'jiraProxyConfiguration',
	classNames: ["rows"]
});

App.JiraConnectionController = Ember.ObjectController.extend({});

App.JiraConnection = App.Connection.extend({
	resourceProperties : [ 'id', 'connectionType', 'serverUrl', 'user', 'password' ],
	description : function() {
		return '_jira.connectionDescription'.loc(this.serverUrl);
	}.property('serverUrl')
});

App.JiraConnectionView = Ember.View.extend({
	templateName : 'jiraConnection',
	classNames : ['rows']
});

App.JiraProxyConfigurationController = Ember.ObjectController.extend({});