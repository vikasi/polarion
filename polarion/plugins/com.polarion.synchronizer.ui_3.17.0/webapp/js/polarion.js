App.PolarionProxyConfiguration = App.ProxyConfiguration.extend({
	resourceProperties : [ 'proxyType', 'accountVaultKey', 'query', 'space', 'document' ],
	name : '_polarion.configurationName'.loc(),
});

App.PolarionProxyConfigurationView = Ember.View.extend({
	templateName : 'polarionProxyConfiguration',
	classNames: ["rows"]
});

App.PolarionProxyConfigurationController = Ember.ObjectController.extend({});