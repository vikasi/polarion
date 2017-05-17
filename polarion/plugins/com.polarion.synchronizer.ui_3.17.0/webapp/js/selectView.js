App.Select = Ember.Select.extend({
	_triggerChange: function() {
		var content = this.get('content');
		if(content && this.get('value')) {
			console.debug('Update value.');
			this.valueDidChange();
		}
		this._super();
	},
//Preserves value when content changes and automatically sets first content value if no value is set
	_loadSelection: function(retry) {
		var content = this.get('content');
		if(!content)
			return;
		
		var valuePath = this.get('optionValuePath').replace(/^content\.?/, '');
		
		var value = this.get('value');
		
		if(!value) {
			value = content[0];
		}		
		
		var selection = content.find(function(obj) {
			var other = Ember.get(obj, valuePath);
      return value === other;
    });
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
			this.set('selection', selection);
		}
	},
	contentObserver: function() {
		this._loadSelection(true);
	}.observes('content')
});