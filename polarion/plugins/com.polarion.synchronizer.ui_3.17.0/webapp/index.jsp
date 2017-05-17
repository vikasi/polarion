<%@page import="com.polarion.portal.jetspeed.gwt.GWTServletProxy"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% 
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setDateHeader("Expires", System.currentTimeMillis() - 10L * 365L * 24L * 60L * 60L * 1000L);
pageContext.setAttribute("build", "?buildId=" + com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId());
%>

<jsp:useBean id="uiContributionBean" class="com.polarion.synchronizer.ui.UIContributionBean" scope="application"/>
    


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Polarion Synchronizer Configuration</title>
<script type="text/javascript">
ENV = {
	'FORCE_JQUERY': true
}
</script>
<%@ include file="WEB-INF/includes/fragments/stylesheets.html" %>
<%@ include file="WEB-INF/includes/fragments/jslibs.html" %>

<%@ include file="WEB-INF/includes/fragments/localization.jsp" %>

<%@ include file="WEB-INF/includes/fragments/init.html" %>

<script type="text/javascript">


window.onbeforeunload = function() {
    if(App.hasChanges) {
        return '_discardChanges'.loc();
    } 
};

areThereUnsavedChanges = function() {
    return App.hasChanges;
};

</script>

<script type="text/javascript" src="js/app.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

<script type="text/javascript" src="js/model/resource.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/connection.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/mapping.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/proxyConfiguration.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/proxyContribution.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/syncPair.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/model/meta.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

<script type="text/javascript" src="js/controllers/controllers.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/controllers/proxyConfigurationController.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

<script type="text/javascript" src="js/router.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

<script type="text/javascript" src="js/polarion.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/jira.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>
<script type="text/javascript" src="js/hp.js?buildId=<%=com.polarion.portal.jetspeed.gwt.GWTServletProxy.getBuildId()%>"></script>

<!-- UI Contributions  -->
<script type="text/javascript">
<c:forEach var="contribution" items="${uiContributionBean.contributions}">
//Contribution from ${contribution.source}
${contribution.content}

</c:forEach>
</script>

<script type="text/x-handlebars" data-template-name="main">
<div id="errorPopup" class="modal hide" tabindex="-1">
<div class="modal-header">
  <h3>{{t errorPopupTitle}}</h3>
</div>
<div class="modal-body" style="overflow-y: auto;">
{{controller.error}}
</div>
<div class="modal-footer">
  <button class="btn" data-dismiss="modal">{{t definition.close}}</button>
</div>
</div>
{{outlet}}
</script>

<script type="text/x-handlebars" data-template-name="polarionProxyConfiguration">
  <div class="control-group">
    <label class="control-label">{{t polarion.accountVaultKey}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="accountVaultKey"}}
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">{{t polarion.query}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="query"}}
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">{{t definition.document}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="space"}} / {{view Ember.TextField valueBinding="document"}}
    </div>
  </div>
</script>

<script type="text/x-handlebars" data-template-name="jiraProxyConfiguration">
  <div class="control-group">
    <label class="control-label">{{t definition.project}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="project"}}
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">{{t jira.query}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="query"}}
    </div>
  </div>
</script>

<script type="text/x-handlebars" data-template-name="configuration">
{{outlet connections}}
{{outlet syncPairs}}
</script>

<script type="text/x-handlebars" data-template-name="syncPairs">
<h2>{{t syncPairs}}</h2>
{{#if loading}}
<img src="img/progress.gif" class="loading"></img>
{{else}}
<table class="table table-hover">
<thead>
<tr>
<th width="10%">{{t definition.id}}</th>
<th width="70%">{{t definition.description}}</th>
<th width="20%">{{t definition.actions}}</th></tr>
</thead>
<tbody>
{{#each syncPair in controller.scopeSyncPairs}}
<tr>
<td>{{syncPair.id}}</td>
<td>{{syncPair.right.description}}</td>
<td class="actions">
{{#if controller.isProject}}
<a {{action showSyncPair syncPair href="true"}}><i class="icon-edit"></i> {{t definition.edit}}</a>
{{/if}}
<a {{action deleteSyncPair syncPair}} href="#" {{bindAttr disabled="syncPair.noModifyPermission"}} {{bindAttr title="syncPair.noModifyTooltip"}}><i class="icon-trash"></i> {{t definition.delete}}</a>
{{#if controller.isProject}}
<a {{action executeSyncPair syncPair}} href="#"><i class="icon-execute"></i> {{t execute}}</a>
{{#if syncPair.lastJobId}}<a {{bindAttr href="syncPair.logUrl"}} target="_blank"><i class="icon-log"></i> {{t openLog}}</a>{{/if}}
{{/if}}
</td>
</tr>
{{/each}}
{{#if controller.isProject}}
<tr>
<td></td>
<td></td>
<td class="actions">
<a {{bindAttr class="controller.noModifyPermission:disabled"}} href="#" {{bindAttr disabled="controller.noModifyPermission"}} 
	{{bindAttr title="controller.noModifyTooltip"}} {{action showCreateSyncPair}}><i class="icon-plus-sign"></i> {{t addSyncPair}}</a>
</td>
</tr>
{{/if}}
</tbody>
</table>
{{/if}}
           
{{view App.CreateSyncPairView 
	connectionsBinding="App.router.connectionsController.active" syncPairsBinding="App.router.syncPairsController" 
	visibleBinding="controller.showCreateSyncPair"}}
</script>

<script type="text/x-handlebars" data-template-name="createSyncPair">
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal"><img src="/polarion/ria/images/dialogs/x.png"></button>
  <h3>{{t createSyncPair}}</h3>
</div>
<div class="modal-body">
	<div class="form-horizontal">
		<div class="control-group">
    		<label class="control-label">{{t template}}{{t definition.colon}}</label>
    		<div class="controls">
      			{{view App.StyledSelect valueBinding="view.templateSyncPair" contentBinding="view.syncPairTemplates" 
          			optionValuePath="content" optionLabelPath="content.id" promptBinding="view.templatePrompt"}}
    		</div>
 		</div>
  		<div {{bindAttr class="view.hasCreateError:error :control-group"}}>
    		<label class="control-label">{{t syncPairId}}{{t definition.colon}}</label>
    		<div class="controls">
      			{{view Ember.TextField valueBinding="view.createId"}}
      			{{#if view.hasCreateError}}
      			<span class="help-inline">{{view.createError}}</span>
       			{{/if}}
    		</div>
 		</div>
 		<div class="control-group">
    		<label class="control-label">{{t connectTo}}{{t definition.colon}}</label>
    		<div class="controls">
      			{{view App.StyledSelect valueBinding="view.connectTo" 
          			valueBinding="view.connection" contentBinding="view.validConnections" 
          			optionValuePath="content" optionLabelPath="content.id" selectFirst="true"}}
    		</div>
 		</div>
	</div>
</div>
<div class="modal-footer">
  <button class="btn"{{action create}} {{bindAttr disabled="view.noConnections"}} {{bindAttr title="view.disableCreateReason"}}>{{t definition.create}}</button>
  <button class="btn" data-dismiss="modal">{{t definition.cancel}}</button>
</div>
</script>

<script type="text/x-handlebars" data-template-name="info">
<div class="infomessage">
<span class="messagetype">{{t connectorsInfo}} 
<a target="_blank" href="/polarion/help/?topic=/com.polarion.xray.doc.user/agchConfigConnectors.html">{{t connectorsDocuLinkText}}</a></span>
</div>
</script>

<script type="text/x-handlebars" data-template-name="connections">
<h1>{{t syncConfiguration}}</h1>

{{view App.HelpView}}

<h2>{{t connections}}</h2>
{{#if loading}}
<img src="img/progress.gif" class="loading"></img>
{{else}}
<table class="table table-hover">
<thead>
<tr>
<th width="10%">{{t definition.id}}</th>
<th width="70%">{{t connectionTo}}</th>
<th width="20%">{{t definition.actions}}</th></tr>
</thead>
<tbody>
{{#each connection in controller}}
<tr>
<td {{bindAttr style="connection.style"}}>{{connection.id}}{{#if connection.showGlobal}} {{t global}}{{/if}}</td>
<td {{bindAttr style="connection.style"}}>{{connection.description}}</td>
<td class="actions">
<a {{action viewEditConnection connection}} href="#"><i class="icon-edit"></i>
{{t definition.edit}}
</a>
{{#if connection.canModify}}
<a {{action deleteConnection connection}} href="#" {{bindAttr disabled="connection.noModifyPermission"}} {{bindAttr title="connection.noModifyTooltip"}}><i class="icon-trash"></i> {{t definition.delete}}</a>
{{/if}}
</td>
</tr>
{{/each}}
<tr>
<td></td>
<td></td>
<td class="actions">
<a {{action viewCreateConnection connection}} href="#" {{bindAttr disabled="App.router.applicationController.noModifyPermission"}} {{bindAttr title="App.router.applicationController.noModifyTooltip"}}><i class="icon-plus-sign"></i> {{t addConnection}}</a>
</td>
</tr>
</tbody>
</table>
{{/if}}


<div id="editConnection" class="modal hide" tabindex="-1">
{{outlet editConnectionDialog}}
</div>

{{outlet createConnection}}
</script>

<script type="text/x-handlebars" data-template-name="editConnectionDialog">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"><img src="/polarion/ria/images/dialogs/x.png"></button>
    <h3>{{header}}</h3>
    {{#if hasError}}<div style="color:red">{{view.error}}</div>{{/if}}
  </div>
  <div class="modal-body">
    <div class="form-horizontal">
	  {{#if create}}
      <div class="rows">
      	<div {{bindAttr class="hasIdError:error :control-group"}}>
        	<label class="control-label "><strong>{{t definition.id}}{{t definition.colon}}</strong></label>
        	<div class="controls">
          		{{view Ember.TextField valueBinding="id"}}
          		{{#if hasIdError}}<span class="help-inline">{{idError}}</span>{{/if}}
        	</div>
		</div>
      </div>
      {{/if}} 
      {{outlet connection}}
    </div> 
  </div>
  <div class="modal-footer">
    <button class="btn" {{bindAttr disabled="validating"}} {{action save target="controller"}} {{bindAttr disabled="controller.content.noModifyPermission"}} {{bindAttr title="controller.content.noModifyTooltip"}}>
	{{#if validating}}{{t validating}}{{else}}
		{{#if create}}{{t definition.create}}{{else}}{{t definition.save}}{{/if}}
    {{/if}}
	</button>
	<button class="btn"  {{bindAttr disabled="validating"}} {{action close target="controller"}}>{{t definition.cancel}}</button>
  </div>
</script>



<script type="text/x-handlebars" data-template-name="createConnection">
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal"><img src="/polarion/ria/images/dialogs/x.png"></button>
  <h3>{{t createConnectionHeader1}}</h3>
</div>
<div class="modal-body">
   {{view App.StyledSelect 
        contentBinding="App.router.proxyContributionController.connectionContributions" 
		valueBinding="controller.selection" 
        optionValuePath="content" optionLabelPath="content.name"}}
</div>
<div class="modal-footer">
  <button class="btn"{{action nextCreateConnection}} data-dismiss="modal">{{t definition.next}}</button>
  <button class="btn" data-dismiss="modal">{{t definition.cancel}}</button>
</div>
</script>

<script type="text/x-handlebars" data-template-name="syncPair">
{{#with controller.content}}
<div class="navbar navbar-inverse navbar-fixed-top">
      <table class="navbar-polarion">
		<tr>
			<td>{{view App.SaveButton saveDisabledBinding="notModified" disabledBinding="noModifyPermission"}}</td>
			<td><div class="polarion-btn" {{action back}}><span>{{t definition.cancel}}</span></div></td>
			<td style="width: 100%"/>
		</tr>
    </table>
</div>

<br/>
<br/>
<br/>

{{view App.HelpView}}


<h1>{{view.heading}}<br/><small>{{view.subHeading}}</small></h1>
<h2>{{t connection}}</h2>
<div class="container-fluid">
<div class="row-fluid">
    <div class="span6">
      {{outlet left}}
    </div>
    <div class="span6">
       {{outlet right}}
    </div>
  </div>
</div>

<h2>{{t generalSettings}}</h2>
<hr/>
<div class="form-horizontal">
  <div class="control-group">
    <label class="control-label">{{t propagateNewItems}}{{t definition.colon}}</label>
    <div class="controls">{{view App.OptionalDirectionView valueBinding="newItemDirection"}}</div>
  </div>
  <div class="control-group">
    <label class="control-label">{{t propagateDeletes}}{{t definition.colon}}</label>
    <div class="controls">{{view App.OptionalDirectionView valueBinding="deleteDirection"}}</div>
  </div>
</div>
<h2>{{t mapping}}</h2>
{{#if controller.proxiesConfigured}}
{{outlet mapping}}
{{else}}
<br/>
<div class="alert alert-error">
{{t proxiesNotConfigured}}
</div>
{{/if}}
{{/with}}
</div>

{{view App.EditFieldMappingView controllerBinding="App.router.fieldMappingController"}}
{{view App.SaveAsDialog viewName="saveAsDialog" allPairsBinding="App.router.syncPairsController" currentPairBinding="App.router.syncPairController.content"}}
</script>

<script type="text/x-handlebars" data-template-name="proxyConfiguration">
<div class="well">
  {{#if proxyError}}
  <div class="alert alert-error"><strong>{{t errorPrefix}}</strong> {{proxyError}}</div>
  {{/if}}
  <div class="clearfix">
    <div style="width:30px;height:30px;position:relative;top:-2px;margin-right:15px" class="pull-left">
      {{#if loading}}
      <img src="img/gray_11slides_002sec.gif" style="width:30px" class="pull-left"></img>
      {{else}}
      {{#if configurationOk}}
      <img src="img/tick.png" style="width:25px" class="pull-left"></img>
      {{/if}}
      {{/if}}
    </div>
    <h3>
      {{name}}
    </h3>
  </div>
  <div class="form-horizontal">
    {{outlet}}
    <div class="rows">
    <div class="control-group">
      <label class="control-label"></label>
      <div class="controls">
        <button class="btn" {{action loadProxyInstance target="controller"}} {{bindAttr disabled="loading"}}>
          {{#if loading}}{{t loading}}{{else}}{{t testConnection}}{{/if}}
        </button>
      </div>
    </div>
	</div>
  </div>
</div>
</script>

<script type="text/x-handlebars" data-template-name="mapping">
{{#if controller.hierarchySupported}}
<h3>{{t hierarchy}}</h3>
<div class="form-horizontal">
  <div class="control-group">
    <label class="control-label">{{t direction}}{{t definition.colon}}</label>
    <div class="controls">{{view App.DirectionView valueBinding="controller.content.hierarchyDirection"}}</div>
  </div>
  {{#if controller.hierarchyBidirectional}}
  <div class="control-group">
    <label class="control-label">{{t primaryDirection}}{{t definition.colon}}</label>
    <div class="controls">{{view App.SideView valueBinding="controller.content.primaryHierarchyDirection"}}</div>
  </div>
  {{/if}}
</div>
{{/if}}

<hr/>
<h3>{{t typeMapping}}</h3>

<table class="table table-hover table-condensed">
<thead>
<tr><th>{{t leftType}}</th><th>{{t rightType}}</th><th>{{t definition.actions}}</th></tr>
</thead>
<tbody>
{{#each typeMapping in controller.typeMappings}}
<tr>
<td>
{{view App.StyledSelect 
        valueBinding="typeMapping.left" 
        contentBinding="controller.leftMetadata.types" 
        optionValuePath="content.id" optionLabelPath="content.name"}}
</td>
<td>
{{view App.StyledSelect 
        valueBinding="typeMapping.right" 
        contentBinding="controller.rightMetadata.types" 
        optionValuePath="content.id" optionLabelPath="content.name"}}
</td>
<td class="actions">
<a {{action deleteTypeMapping typeMapping target="controller"}} href="#"><i class="icon-trash"></i> {{t definition.delete}}</a>
</td>
</tr>
{{/each}}
<tr>
<td colspan="2"></td>
<td class="actions">
<a {{action addTypeMapping target="controller"}} href="#"><i class="icon-plus-sign"></i> {{t addTypeMapping}}</a>
</td>
</tr>
</tbody>
</table>
<hr/>
<h3>{{t fieldMappings}}</h3>
{{view App.FieldMappingsView controllerBinding="controller.content.defaultMappingGroup"}}
{{#each fieldMappingGroup in controller.content.fieldMappingGroups}}
{{view App.TypeFieldMappingsView controllerBinding="fieldMappingGroup"}}
{{/each}}
</script>

<script type="text/x-handlebars" data-template-name="fieldMappings">
<h4>{{view.heading}}</h4>
<table class="table">
  <colgroup>
    <col width="*">
    <col width="*">
    <col width="*">
    <col width="*">
    <col width="186px">
  </colgroup>
<thead>
<tr><th>{{t definition.status}}</th><th>{{t leftField}}</th><th></th><th>{{t rightField}}</th><th>{{t definition.actions}}</th></tr>
</thead>
{{#each fieldMapping in controller.fieldMappings}}
<tbody class="field-mapping">
<tr>
  <td rowspan="2" style="max-width:150px">
    <span {{bindAttr class="fieldMapping.valid:badge-success:badge-important :badge"}}fieldMapping.check>
       {{#if fieldMapping.valid}}{{t definition.ok}}{{else}}{{t invalidMapping}}{{/if}}
    </span>
    <br/>
    <br/>
    <p>{{fieldMapping.check}}</p>
  </td>
  <td>
  {{view App.StyledSelect 
        valueBinding="fieldMapping.left" 
        contentBinding="leftFields" 
        optionValuePath="content.key" optionLabelPath="content.label" disabledBinding="fieldMapping.readOnly"}}
  </td>
  <td>{{view App.DirectionImageView valueBinding="fieldMapping.direction"}}</td>
  <td style="text-align:right">
  {{view App.StyledSelect 
        valueBinding="fieldMapping.right" 
        contentBinding="rightFields" 
        optionValuePath="content.key" optionLabelPath="content.label" disabledBinding="fieldMapping.readOnly"}}
  </td>
  <td rowspan="2" class="actions">  
    {{#unless fieldMapping.readOnly}}	
  	    <a {{action editFieldMapping fieldMapping}} href="#"><i class="icon-edit"></i> {{t definition.edit}}</a>
        <a {{action deleteFieldMapping fieldMapping  target="controller"}} href="#"><i class="icon-trash"></i> {{t definition.delete}}</a>    
  	{{/unless}}
  </td>
</tr>
<tr>
  <td  colspan="3" class="no-border">
  <div class="form-horizontal mapping-direction">
    <div class="control-group">
      <label class="control-label">{{t direction}}{{t definition.colon}}</label>
      <div class="controls">
        {{view App.DirectionView valueBinding="fieldMapping.direction"}}
      </div>
    </div>
{{#if fieldMapping.bidirectional}}
    <div class="control-group">
      <label class="control-label">{{t primaryDirection}}{{t definition.colon}}</label>
      <div class="controls">
        {{view App.SideView valueBinding="fieldMapping.primaryDirection"}}
      </div>
    </div>
{{/if}}
  </div>

  </td>
</tr>
</tbody>
{{/each}}
<tfoot>
<tr>
  <td colspan="4"></td>
  <td class="actions">
  <a {{action createFieldMapping target="controller"}} href="#"><i class="icon-plus-sign"></i> {{t addFieldMapping}}</a>
  </td>
</tr>
</tfoot>
</table>
</script>

<script type="text/x-handlebars" data-template-name="fieldMapping">
<div id="editFieldMapping" class="modal hide fieldMapping" tabindex="-1">
<div class="modal-header">
  <h3>{{view.heading}}</h3>
</div>
<div class="modal-body form-horizontal">
<h4>{{t valueMappings}}</h4>
<table class="table table-condensed">
<thead>
<tr><th>{{t leftValue}}</th><th>{{t rightValue}}</th><th>{{t definition.actions}}</th></tr>
</thead>
<tbody>
{{#each valueMapping in controller.content.valueMappings}}
<tr>
<td>
<div {{bindAttr class="valueMapping.leftMissing:error :control-group"}}>
{{view Ember.TextField valueBinding="valueMapping.left"}}
{{#if "valueMapping.leftMissing"}}<span class="help-inline">{{t enterMatchValue}}</span>{{/if}}
</div>
</td>
<td>
<div {{bindAttr class="valueMapping.rightMissing:error :control-group"}}>
{{view Ember.TextField valueBinding="valueMapping.right"}}
{{#if "valueMapping.rightMissing"}}<span class="help-inline">{{t enterMatchValue}}</span>{{/if}}
</div>
</td>
<td><a {{action removeValueMapping valueMapping target="controller"}} href="#"><i class="icon-trash"></i></a></td>
</tr>
{{/each}}
<tr>
<td colspan="2"/>
<td><a {{action addValueMapping target="controller"}} href="#"><i class="icon-plus-sign"></i> {{t definition.add}}</a></td>
</tr>
</tbody>
</table>
</div>
<div class="modal-footer">
  <button class="btn" {{action check target="view"}}>{{t definition.ok}}</button>
</div>
</div>

</script>

<script type="text/x-handlebars" data-template-name="jiraConnection" style="display:table-row-group">
  <div {{bindAttr class="view.hasCreateError:error :control-group"}}>
    <label class="control-label">{{t serverUrl}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="controller.content.serverUrl" disabledBinding="controller.readOnly"}}
    </div>
  </div>
  <div {{bindAttr class="view.hasCreateError:error :control-group"}}>
    <label class="control-label">{{t definition.user}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="controller.content.user"}}
    </div>
  </div>
  <div {{bindAttr class="view.hasCreateError:error :control-group"}}>
    <label class="control-label">{{t password}}{{t definition.colon}}</label>
    <div class="controls">
      {{view Ember.TextField valueBinding="controller.content.password" type="password"}}
    </div>
  </div>
</script>

<script type="text/x-handlebars" data-template-name="saveButton">
<table cellspacing="0" cellpadding="0" border="0" class="com_polarion_reina_web_js_widgets_JSPopupButton_Button">
<tbody><tr  {{bindAttr title="controller.noModifyTooltip"}}>
<td {{bindAttr class=":bt-icon view.saveDisabled:disab"}} {{action save this target="view"}}>
<img src="/polarion/wiki/skins/sidecar/save.gif${build}" style="width: 16px; height: 16px;">
</td>
<td {{bindAttr class=":bt-arrow-label view.saveDisabled:disabled"}}{{action save this target="view"}}>{{t definition.save}}</td>
<td {{bindAttr class=":bt-arrow view.disabled:disab"}} {{action "toggle" target="view"}}><img src="/polarion/wiki/skins/sidecar/button_arrow2.gif${build}" style="width: 5px; height: 5px; vertical-align: middle;"></td>
</tr>
</tbody></table>
<span class="submenu enab">                                                    	                	                       	    
<span class="submenuitem">
<table cellspacing="0" cellpadding="0" style="" border="0"><tbody>
<tr {{action saveAs this target="view"}}><td height="20" width="20"><a><img src="/polarion/wiki/skins/sidecar/save.gif${build}" border="0" height="16" width="16"></a></td><td style="text-align: left; vertical-align: middle; width: 160;"><a>{{t saveAs}}</a></td></tr>
</tbody></table>
</span>
</span>
</script>

<script type="text/x-handlebars" data-template-name="saveAsDialog">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"><img src="/polarion/ria/images/dialogs/x.png"></button>
    <h3>{{t saveAsDialogHeader}}</h3>
	{{#if view.syncPair.saveError}}<div style="color:red">{{view.syncPair.saveError}}</div>{{/if}}
  </div>
  <div class="modal-body">
	<table cellspacing="5" class="optionTable">
		<tr>
			<td class="radioCell">{{view Ember.RadioButton groupBinding="view.saveOption" value="create"}}</td>
        	<td>
  				<span>{{t createNewConfiguration}}</span><br/>
				{{#if view.isCreate}}
                <div class="form-horizontal">
					<div class="control-group">
        				<label class="control-label"><strong>{{t definition.scope}}{{t definition.colon}}</strong></label>
        				<div class="controls">{{view App.ScopeSelect valueBinding="view.scope"}}</div>
					</div>
					<div class="control-group">
        				<label class="control-label"><strong>{{t definition.id}}{{t definition.colon}}</strong></label>
        				<div class="controls">{{view Ember.TextField valueBinding="view.createSyncPairId"}}</div>
					</div>
      			</div>
				{{/if}}
			</td>
		</tr>
		<tr>
			<td class="radioCell">{{view Ember.RadioButton groupBinding="view.saveOption" value="update"}}</td>
        	<td>
  				{{t updateExistingConfiguration}}<br/>
				{{#if view.isUpdate}}
				<table cellspacing="5">
					<tr>
        				<td><label class="control-label "><strong>{{t definition.configuration}}{{t definition.colon}}</strong></label></td>
        				<td>{{view App.StyledSelect contentBinding="view.compatiblePairs" optionValuePath="content" optionLabelPath="content.id" valueBinding="view.updateSyncPair"}}</td>
					</tr>
      			</table>
				{{/if}}
			</td>
		</tr>
	</table>
    <p>{{t saveAsHint}}</p>
  </div>
  <div class="modal-footer">
    <button class="btn" {{action save controller.content target="view"}}>{{t definition.save}}</button>
	<button class="btn"  data-dismiss="modal">{{t definition.cancel}}</button>
  </div>
</script>

</head>
<body>
</body>
</html>