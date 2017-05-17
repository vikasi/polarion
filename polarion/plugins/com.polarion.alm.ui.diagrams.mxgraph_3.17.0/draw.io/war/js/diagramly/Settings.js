/**
 * Utility class for working with persisted application settings
 */

var mxSettings =
{
	// NOTE: Hardcoded in index.html due to timing of JS loading
	key: '.drawio-config',

	settings:
	{
		language: '',
		plugins: [],
		version: 2
	},

	getLanguage: function()
	{
		return this.settings.language;
	},
	setLanguage: function(lang)
	{
		this.settings.language = lang;
	},
	getPlugins: function()
	{
		return this.settings.plugins;
	},
	setPlugins: function(plugins)
	{
		this.settings.plugins = plugins;
	},
	save: function()
	{
		if (isLocalStorage)
		{
			localStorage.setItem(mxSettings.key, JSON.stringify(this.settings));
		}
	},
	load: function()
	{
		if (isLocalStorage)
		{
			var value = localStorage.getItem(mxSettings.key);

			if (value != null)
			{
				this.settings = JSON.parse(value);

				if (this.settings.plugins == null)
				{
					this.settings.plugins = [];
				}
			}
			
			this.settings.version = 2;
		}
	},
	clear: function() 
	{
		if (isLocalStorage)
		{
			localStorage.removeItem(mxSettings.key);
		}
	}
}

// Loads initial content
mxSettings.load();
