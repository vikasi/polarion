App.Connection = App.Resource.extend({
	resourceProperties: ['connectionTypeconnectionType'],
	connectionType : function() {
		var type=this.constructor.toString();
		//In IE the App prefix gets strangely omitted
		if(type.charAt(0)=='.'){
			return type.slice(1);
		} else {
			return type.slice(4);
		}
		
	}.property(),
  id: null,
  canModify: function() {
	  return this.projectId == this.contextProject ||  this.create;
  }.property(),
  showGlobal: function() {
	  return this.projectId != this.contextProject;
  }.property(),
  readOnly: function() {
	  return !this.get('canModify');
  }.property(),
  resourceUrl: function() {
		return App.Connection.createResourceUrl(this.get('contextProject'));
  }.property(),
  style: function() {
	  return this.get('enabled') || this.contextProject == null ? '' : 'color: grey';
  }.property(),
	noModifyPermissionBinding: 'App.router.applicationController.noModifyPermission',
	noModifyTooltipBinding: 'App.router.applicationController.noModifyTooltip'
});

App.Connection.reopenClass({
  createType: function(type) {
  	var connectionClass = App.get(type);
		return connectionClass.create({connectionType: type});
  },
  createResourceUrl : function(project) {
		return '/polarion/synchronizer/rest/' + (project ? ('projects/' + project) : 'global') + '/connections';
  }
});