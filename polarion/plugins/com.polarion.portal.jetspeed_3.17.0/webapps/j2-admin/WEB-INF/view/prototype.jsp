<%--
Copyright 2004 The Apache Software Foundation
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ taglib uri='/WEB-INF/portlet.tld' prefix='portlet'%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<link href='css/security-admin.css' type='text/css'/>

<f:view>
<h:panelGrid columns='2'>
<h:panelGroup>
<h:dataTable
    value="#{users.users}"
    var="user"
    styleClass="portlet-section-body"
    headerClass="portlet-section-header"
    rowClasses="portlet-section-body,portlet-section-alternate"
>
    <h:column>
       <f:facet name="header">
         <h:outputText value="User"
            style="font-weight: bold"/>
       </f:facet>
        <h:outputText value='#{user.last}'/>
    </h:column>
    <h:column>
        <f:verbatim>
        ...
       </f:verbatim>        
    </h:column>    
    <h:column>
        <h:selectBooleanCheckbox value="false"/>
    </h:column>
</h:dataTable>
</h:panelGroup>
<h:panelGroup>

<x:panelTabbedPane bgcolor="#FFFFCC" >

    <f:verbatim>
        <p>User Information</p>
    </f:verbatim>

    <x:panelTab id="tab1" label="Attributes" rendered="#{tabbedPaneBean.tab1Visible}">
        <h:inputText id="inp1"/><f:verbatim><br></f:verbatim>
        <h:inputText id="inp2" required="true" /><h:message for="inp2" showSummary="false" showDetail="true" />
    </x:panelTab>

    <x:panelTab id="tab2" label="Roles" rendered="#{tabbedPaneBean.tab2Visible}">

    <f:verbatim>
        <p> TAB TWO </p>
    </f:verbatim>
    </x:panelTab>

    <x:panelTab id="tab3" label="Prefs" rendered="#{tabbedPaneBean.tab3Visible}">
        <h:inputText id="inp3"/><f:verbatim><br></f:verbatim>
        <h:inputText id="inp4"/><f:verbatim><br></f:verbatim>
        <h:inputText id="inp5"/><f:verbatim><br></f:verbatim>
    </x:panelTab>

    <f:verbatim><br></f:verbatim>

    <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab1Visible}"/><f:verbatim>Tab 1 visible<br></f:verbatim>
    <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab2Visible}"/><f:verbatim>Tab 2 visible<br></f:verbatim>
    <h:selectBooleanCheckbox value="#{tabbedPaneBean.tab3Visible}"/><f:verbatim>Tab 3 visible<br></f:verbatim>

    <h:commandButton value="Save" />

</x:panelTabbedPane>


</h:panelGroup>
</h:panelGrid>
</f:view>
