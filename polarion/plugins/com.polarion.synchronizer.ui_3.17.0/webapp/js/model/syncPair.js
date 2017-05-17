App.SyncPair = App.Resource.extend({
	resourceProperties : [ 'id', 'left', 'right', 'mapping', 'newItemDirection',
			'deleteDirection', 'projectId' ],
	resourceName : 'sync_pair',
	deserialize : function(json) {
		this._super(json);

		
		var properties = [
        'mapping.hierarchyDirection',
		    'mapping.primaryHierarchyDirection',
				'mapping.fieldMappings.@each',
				'mapping.fieldMappings.@each.left',
				'mapping.fieldMappings.@each.right',
				'mapping.fieldMappings.@each.direction',
				'mapping.fieldMappings.@each.primaryDirection',
				'mapping.fieldMappings.@each.valueMappingsHack'];
		
		properties.pushObjects(this.resourceProperties);
		
		if(this.left != null) {
			console.log("Loading configuration type " + this.left.proxyType);
			var leftType = App.get(this.left.proxyType);
			if(leftType) {
				var left = leftType.create();
				left.deserialize(this.left);
				this.set('left', left);
				left.resourceProperties.forEach(function(val) {
					properties.push("left." + val);
				});
			}
		}

		var mapping = App.MappingConfiguration.create();
		mapping.deserialize(this.mapping);
		this.set('mapping', mapping);

		if(this.right != null) {
			var rightType = App.get(this.right.proxyType);
			if(rightType) {
				var right = rightType.create();
				right.deserialize(this.right);			
				this.set('right', right);
				right.resourceProperties.forEach(function(val) {
					properties.push("right." + val);
				});
			}
		}
		if(this.get('projectId')) {
			this.loadLastJobId();
		}

		return this;
	},
	notModified : function() {
		return !this.get('modified');
	}.property('modified'),
	newItemDirection : 'BIDIRECTIONAL',
	deleteDirection : null,
	loadLastJobId: function() {
		var url = '%@/lastJobId'.fmt(this._resourceUrl());
		var self = this;
		$.get(url).success(function(resp) {
			self.set('lastJobId', resp);
		});
	},
	logUrl: function() {
		return '/polarion/job-report?jobId=%@'.fmt(this.get('lastJobId'));
	}.property('lastJobId'),
	isGlobal: function() {
		return this.get('projectId') == null;
	}.property(),
	saveAs: function(id, global, create) {
		var copy = this.copy(false);
		copy.set('id', id);
		copy.set('create', create);
		if(global) {
			copy.set('projectId', null);
		} else {
			copy.set('projectId', App.router.get('applicationController.project'));
		}
		copy.save();
		return copy;
	},
	save: function() {
		this.set('saveRequest', null);
		this.set('saveError', null);
		this.set('unexpectedSaveError', null);
		if(this.create) {
			var error = App.cleanAndCheckId(this, '_missingSyncPairId'.loc(), '_invalidSyncPairId'.loc());
			if (error != null) {
			    this.set('saveError', error);
			    return;
			}
		}

		var self = this;
		var request = this._super();
		this.set('saveRequest', request);
		request.fail(function(e) {
		    if (e.status == 409) {
		    	self.set('saveError', '_duplicateId'.loc(self.id));
		    	self.set('unexpectedSaveError', false);
		    } else {
		    	self.set('saveError', e.responseText);
		    	self.set('unexpectedSaveError', true);
		    }
		});
		request.success(function() {
			self.set('create', false);
		});
		return request;
	},
	saveSuccess: function(callback) {
		if(this.saveRequest) {
			this.saveRequest.success(callback);
		}
	},
	saveFail: function(callback) {
		if(this.saveRequest) {
			this.saveRequest.fail(callback);
		} else {
			callback.call();
		}
	},
	resourceUrl: function() {
		return App.SyncPair.createResourceUrl(this.get('projectId'));
	}.property(),
	noModifyPermissionBinding: 'App.router.applicationController.noModifyPermission',
	noModifyTooltipBinding: 'App.router.applicationController.noModifyTooltip'
});

App.SyncPair.reopenClass({
	createResourceUrl : function(project) {
		return '/polarion/synchronizer/rest/' + (project ? ('projects/' + project) : 'global') + '/sync_pairs';
	}
});