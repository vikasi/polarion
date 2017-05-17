<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<f:loadBundle basename="org.apache.jetspeed.portlets.profiler.resources.ProfilerResources" var="MESSAGE" />

<f:view>
<h:panelGrid>
<h:panelGroup>
    <br/>
	<h:outputText styleClass='portlet-section-header' value="#{MESSAGE['criteria.title']}"/>
	<br/>
	<br/>

    <h:form id="criterionForm" name="criterionForm">
        <h:panelGrid columns="3">

        <h:outputLabel for="name" value="#{MESSAGE['criterion.name']}: "/>
        <h:inputText required='true' id="name" styleClass='portlet-form-label' value="#{criterion.name}" size='80'/>
        <h:message for="name" styleClass="portlet-msg-error" showDetail="true" showSummary="false" />

        <h:outputLabel for="value" value="#{MESSAGE['criterion.value']}: "/>
        <h:inputText id="value" styleClass='portlet-form-label' value="#{criterion.value}"  size='128'/>
        <h:message for="value" styleClass="portlet-msg-error" showDetail="true" showSummary="false" />

        <h:outputLabel for="resolver" value="#{MESSAGE['criterion.resolver']}: "/>
        <h:selectOneMenu id="resolver" styleClass='portlet-form-field-label' value="#{criterion.resolver}">
        	<f:selectItems value="#{criterion.resolvers}"/>
        </h:selectOneMenu>        
        <h:message for="resolver" styleClass="portlet-msg-error" showDetail="true" showSummary="false" />

        <h:outputLabel for="fallbackType" value="#{MESSAGE['criterion.fallback.type']}: "/>
        <h:selectOneMenu id="fallbackType" styleClass='portlet-form-field-label' value="#{criterion.fallbackType}">
        	<f:selectItems value="#{criterion.fallbackTypes}"/>
        </h:selectOneMenu>        
        <h:message for="fallbackType" styleClass="portlet-msg-error" showDetail="true" showSummary="false" />

        <h:outputLabel for="fallbackOrder" value="#{MESSAGE['criterion.fallback.order']}: "/>
        <h:inputText id="fallbackOrder" styleClass='portlet-form-label' value="#{criterion.fallbackOrder}"  />
        <h:message for="fallbackOrder" styleClass="portlet-msg-error" showDetail="true" showSummary="false" />
        
        </h:panelGrid>        
        
        <h:panelGroup/>
	    <h:commandButton id="saveCriterion" value="#{MESSAGE['profiler.rule.save']}" action="#{criterion.saveCriterion}"/>
	    <h:commandButton id="removeCriteria" value="#{MESSAGE['criteria.remove']}" action="#{criterion.removeCriterion}"/>	    
 	    <h:commandButton id="cancelCriterion" value="#{MESSAGE['profiler.rule.cancel']}" 
	                     action="returnFromCriterion" immediate='true'>
	    </h:commandButton>

        <h:panelGroup/>
                
    </h:form>        
    
</h:panelGroup>

</h:panelGrid>

</f:view>
