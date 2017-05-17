dojo.hostenv.conditionalLoadModule({
	common: ["dojo.storage"],
	browser: ["dojo.storage.browser"]
});
dojo.hostenv.moduleLoaded("dojo.storage.*");

