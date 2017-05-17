<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collection"%>
<%@page import="com.polarion.platform.i18n.Localization"%>
<%@page import="com.polarion.platform.i18n.I18nService"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%! 
private static final String LOCALIZATION_PREFIX = "synchronizer.ui.";
private static final Collection<String> LOCALIZATION_DEFINITIONS = 
  Arrays.asList("project","document","actions","id","description","save", "edit", 
          "delete", "next", "cancel", "create", "ok", "status", "add",
          "user", "scope", "colon", "name", "configuration", "close", "global"); 
private static final String DEFINITION_PREFIX = "definition.";
private static final String LOCALIZATION_ENTRY_FORMAT = "'_%s' : '%s',";
%>
<script type="text/javascript">
Ember.Handlebars.registerHelper('t', function(str, options) {
return Ember.String.loc('_' + str, options);
});

Ember.STRINGS = {
<%
//Load all used localization definitions.
for(String definitionKey: LOCALIZATION_DEFINITIONS) {
    String localizationKey = DEFINITION_PREFIX + definitionKey;
    out.println(String.format(LOCALIZATION_ENTRY_FORMAT, localizationKey, Localization.getString(localizationKey)));
}

//Load all localization entries starting with with prefix synchronizer.ui
Map<String,String> localizationData = I18nService.getAllLocalizations().get(new Locale(System.getProperty("com.polarion.locale", "en")));
for(String localizationKey: localizationData.keySet()) {
    if(localizationKey.startsWith(LOCALIZATION_PREFIX)) {
    	String localizationValue=Localization.getString(localizationKey);
    	localizationValue = localizationValue.replaceAll("\\\\(?=')", "");
    	localizationValue = localizationValue.replaceAll("\\\\", "\\\\\\\\");
        localizationValue = localizationValue.replaceAll("'", "\\\\'");
        out.println(String.format(LOCALIZATION_ENTRY_FORMAT, localizationKey.substring(LOCALIZATION_PREFIX.length()), 
                localizationValue));
    }
}

out.println(String.format(LOCALIZATION_ENTRY_FORMAT, "discardChanges", Localization.getString("form.message.discardChanges")));

%>
};
</script>