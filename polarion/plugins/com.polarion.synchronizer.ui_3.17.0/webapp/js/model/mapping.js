App.MappingConfiguration = App.Resource.extend({
	init: function() {
		this._super();
		this.set('fieldMappingGroups',[]);
		this.set('defaultMappingGroup', this._loadFieldMappingGroup());
	},
	resourceProperties : [ 'hierarchyDirection', 'primaryHierarchyDirection', 'defaultMappingGroup', 'fieldMappingGroups' ],
	_loadFieldMappingGroup: function() {
		var fieldMappingGroup = App.FieldMappingGroup.create({
			leftMetadataBinding: 'App.router.mappingController.leftMetadata',
			rightMetadataBinding: 'App.router.mappingController.rightMetadata',
		});
		return fieldMappingGroup;
	},
	deserializeProperty : function(prop, value) {
		if (prop == 'defaultMappingGroup') {
			this.get('defaultMappingGroup').deserialize(value);
		} else if(prop == 'fieldMappingGroups') {
			var self = this;
			var fieldMappingGroups = [];
			value.forEach(function(hash) {
				var fieldMappingGroup = self._loadFieldMappingGroup();
				fieldMappingGroups.pushObject(fieldMappingGroup);
				fieldMappingGroup.deserialize(hash);
			});
			this.set(prop, fieldMappingGroups);
		} else {
			this._super(prop,value);
		}
	},
	deserialize: function(hash) {
		var result = this._super(hash);
		var self = this;
		this.get('fieldMappingGroups').forEach(function(fieldMappingGroup){
			var typeMapping = self.get('typeMappings').find(function(typeMapping) {
				return fieldMappingGroup.leftType == typeMapping.left 
					&& fieldMappingGroup.rightType == typeMapping.right;
			});
			fieldMappingGroup.set('typeMapping', typeMapping);
		});
		return result;
	},
	relationBidirectional : function() {
		return this.relationDirection == 'BIDIRECTIONAL';
	}.property('relationDirection'),
	typeMappings: function() {
		
		var defaultFieldMappings = this.get('defaultMappingGroup').get('fieldMappings');
		
		var typeFieldMapping = defaultFieldMappings.find(function(mapping) {
			return mapping.get('left') == 'type' && mapping.get('right') == 'type';
		});
		if(typeFieldMapping == null) {
			console.debug('Creating type mapping.');
			typeFieldMapping = App.FieldMapping.create({
				left: 'type',
				right: 'type',
				direction: 'BIDIRECTIONAL',
				primaryDirection: 'LEFT_TO_RIGHT',
				fieldMappingGroup: this.get('defaultMappingGroup'),
			});
			defaultFieldMappings.push(typeFieldMapping);
		}
		typeFieldMapping.set('readOnly', true);
		return typeFieldMapping.valueMappings;
	}.property('defaultMappingGroup.fieldMappings.@each','defaultMappingGroup','defaultMappingGroup.fieldMappings'),
});

App.FieldMappingGroup = App.Resource.extend({
	init: function() {
		this._super();
		this.set('fieldMappings',[]);
	},
	resourceProperties : [ 'leftType', 'rightType', 'fieldMappings' ],
	deserializeProperty : function(prop, value) {
		if (prop == 'fieldMappings') {
			var fieldMappings = [];
			var self = this;
			value.forEach(function(fieldMappingHash) {
				var fieldMapping = App.FieldMapping.create({
					fieldMappingGroup: self
				});
				fieldMapping.deserialize(fieldMappingHash);
				fieldMappings.pushObject(fieldMapping);
			});
			this.set('fieldMappings', fieldMappings);
		} else {
			this._super(prop,value);
		}
	},
	createFieldMapping : function(router) {
		this.fieldMappings.pushObject(App.FieldMapping.create({
			fieldMappingGroup: this
		}));
	},
	deleteFieldMapping : function(event) {
		this.fieldMappings.removeObject(event.context);
	},
	_loadTypeLabel: function(side) {
		var self = this;
		var metadata = this.get(side + 'Metadata');
		var type = self.get(side + 'Type');
		var label = type + " (unknonw)";
		if(metadata) {
			var found = metadata.types.find(function(match) {
				return match.id == type;
			});
			if(found)
				label = found.name;
		}
		
		return label;
	},
	leftTypeBinding: 'typeMapping.left',
	rightTypeBinding: 'typeMapping.right',
	leftTypeLabel: function() {
		return this._loadTypeLabel('left');
	}.property('leftType','leftMetadata'),
	rightTypeLabel: function() {
		return this._loadTypeLabel('right');
	}.property('rightType','rightMetadata'),
	_loadFields: function(side) {
		var meta = this.get(side + "Metadata");
		var fields;
		if(meta == null) {
			fields = [];
		} else {
			if(this.get(side + 'Type') == null) {
				fields = meta.commonFields;
			} else {
				fields = meta.fieldDefinitions[this.get(side + 'Type')];
			}
		}
		if(fields == null) {
		    fields = [];
		}
		return fields;
	},
	leftFields: function() {
		return this._loadFields('left');
	}.property('leftType','leftMetadata'),
	rightFields: function() {
		return this._loadFields('right');
	}.property('rightType','rightMetadata')
});

App.FieldMapping = App.Resource.extend({
	init: function() {
		this._super();
		this.set('valueMappings',[]);
	},
	readOnly: false,
	resourceProperties : [ 'left', 'right', 'direction', 'primaryDirection', 'valueMappings' ],
	deserializeProperty : function(prop, value) {
		if (prop != 'valueMappings') {
			this._super(prop,value);
		} else {
			var valueMappings = [];
			value.forEach(function(valueMappingHash) {
				var valueMapping = App.ValueMapping.create();
				valueMapping.deserialize(valueMappingHash);
				valueMappings.push(valueMapping);
			});
			this.set('valueMappings', valueMappings);
		}
	},
	bidirectional : function() {
		return this.direction == 'BIDIRECTIONAL';
	}.property('direction'),
	_fieldDefinition: function(side) {
		var self = this;
		var fieldDefinitions = this.get('fieldMappingGroup').get(side + 'Fields');
		var definition = fieldDefinitions.find(function(field) {
			return self.get(side) == field.key;
		});
		return definition;
	},
	leftDefinition: function() {
		return this._fieldDefinition('left');
	}.property('fieldMappingGroup.leftFields','left'),
	rightDefinition: function() {
		return this._fieldDefinition('right');
	}.property('fieldMappingGroup.rightFields','right'),
	check: function() {
		var leftDefinition = this.get('leftDefinition');
		if(!leftDefinition)
			return '_invalidFieldLeft'.loc([this.get('left')]);
		
		var rightDefinition = this.get('rightDefinition');
		if(!rightDefinition)
			return '_invalidFieldRight'.loc([this.get('right')]);
		
		var leftType = (leftDefinition.multiValued ? "multi-" : "") + leftDefinition.type;
		var rightType = (rightDefinition.multiValued ? "multi-" : "") + rightDefinition.type;
		
		var fromLeftCompatibleFields = this.fieldMappingGroup.get('leftMetadata').compatibleFieldsFrom[leftType];
		
		if(this.direction != 'RIGHT_TO_LEFT') {
			if(!fromLeftCompatibleFields || !fromLeftCompatibleFields.contains(rightType))
				return '_invalidTypeFromLeft'.loc(leftType,rightType); 
		}
		
		var fromRightCompatibleFields = this.fieldMappingGroup.get('rightMetadata').compatibleFieldsFrom[rightType];
		
		if(this.direction != 'LEFT_TO_RIGHT') {
			if(!fromRightCompatibleFields || !fromRightCompatibleFields.contains(leftType))
				return '_invalidTypeFromRight'.loc(rightType, leftType);
		}
		
		return null;
		
	}.property('leftDefinition', 'rightDefinition', 'fieldMappingGroup.leftMetadata', 'fieldMappingGroup.rightMetadata', 'left', 'right', 'direction'),
	valid: function() {
		var check = this.get('check');
		console.log("Check result for mapping from " + this.left + " to " + this.right + ": " + check);
		return check == null;
	}.property('check','fieldMappingGroup.leftMetadata', 'fieldMappingGroup.rightMetadata'),
});

App.ValueMapping = App.Resource.extend({
	resourceProperties : [ 'left', 'right' ],
	leftError: null,
	rightError: null,
	validate: function() {
		var valid = true;
		
		this.set('leftMissing', false);
		this.set('rightMissing', false);
		
		if(this.left == null || this.left.length == 0) {
			this.set('leftMissing', true);
			valid = false;
		}
		if(this.right == null || this.right.length == 0) {
			this.set('rightMissing', true);
			valid = false;
		}
		return valid;
	}
});