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
<c:set var="portlet_tabs" value="${requestScope.portlet_tabs}"/>

<c:set var="selectedPortletTab" value="${requestScope.selected_portlet_tab}"/>

<table border="0"  cellspacing="0" cellpadding="0">
	<tr>
		<td class="portlet-menu-caption">Selected Portlet</td>
    </tr>
    <tr>
		<td class="portlet-menu">
            <div class="portlet-menu-item">
<c:out value="${selectedPDef.name}"/>
            </div>
		</td>
	</tr>
</table>
<br/>

<div id="portlet_tabs">

    <c:set var="tab_items" value="${portlet_tabs}"/>
    <c:set var="currentTab" value="${selectedPortletTab}"/>
    <c:set var="url_param_name" value="selected_portlet_tab"/>
    <%@ include file="tabs.jsp"%>
</div>

<c:if test="${selectedPortletTab.id == 'pd_details'}">
    <portlet:actionURL var="edit_portlet_link" >
    </portlet:actionURL>
    
    <form action="<c:out value="${edit_portlet_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.edit_portlet"/>
    <table>
        <tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.expiration_cache"/>
            </td>
            <td class="portlet-section-body">
                <c:out value="${selectedPDef.expirationCache}"/>
            </td>
        </tr>
        </tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.id"/>
            </td>
            <td class="portlet-section-body">
                <c:out value="${selectedPDef.portletIdentifier}"/>
            </td>
        </tr>
        </tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.unique_name"/>
            </td>
            <td class="portlet-section-body">
                <c:out value="${selectedPDef.uniqueName}"/>
            </td>
        </tr>
        </tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.preference_validator"/>
            </td>
            <td class="portlet-section-body">
                <c:out value="${selectedPDef.preferenceValidatorClassname}"/>
            </td>
        </tr>
        </tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.class_name"/>
            </td>
            <td class="portlet-section-body">
                <c:out value="${selectedPDef.className}"/>
            </td>
        </tr>
        <tr>
            <td class="portlet-section-alternate">
                <fmt:message key="pam.details.display_name"/>
            </td>
            <td class="portlet-section-body">
                <table>
                    <%
                        PortletDefinitionComposite portlet = (PortletDefinitionComposite)pageContext.findAttribute("selectedPDef");
                        pageContext.setAttribute("displayNameSet", portlet.getDisplayNameSet());
                    %>
                    <c:forEach var="displayName" items="${displayNameSet.innerCollection}" varStatus="displayNameStatus">
                        <tr>
                            
                            <td class="portlet-section-alternate"><c:out value="${displayName.locale}"/></td>
                            <td class="portlet-section-body"><input type="text" name="display_name:<c:out value="${displayNameStatus.index}"/>" value="<c:out value="${displayName.displayName}"/>" class="portlet-form-field-label"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </table>
    
        <input type="submit" value="<fmt:message key="pam.details.edit_display_name"/>" class="portlet-form-button"/>
    </form>
    
    <form action="<c:out value="${edit_portlet_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.edit_portlet"/>
        <table>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.display_name"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="display_name" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <span class="portlet-form-field-label"><fmt:message key="pam.details.display_name.description"/></span>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.locale"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="locale" value="" size="2"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <span class="portlet-form-field-label"><fmt:message key="pam.details.locale.description"/></span>
                </td>
            </tr>
        </table>
        <input type="submit" value="<fmt:message key="pam.details.add_display_name"/>"  class="portlet-form-button"/>
    </form>
</c:if>

<c:if test="${selectedPortletTab.id == 'pd_metadata'}">
    <div id="portlet_metadata">
            
        <c:set var="md" value="${selectedPDef.metadata}"/>
        <c:set var="action_prefix" value="portlet."/>
        <portlet:actionURL var="edit_metadata_link" >            
        </portlet:actionURL>
        
        <%@ include file="metadata-detail.jsp" %>
    </div>
</c:if>

<c:if test="${selectedPortletTab.id == 'pd_preferences'}">
    <c:set var="prefSet" value="${selectedPDef.preferenceSet}"/>
    <%
        PreferenceSetComposite comp = (PreferenceSetComposite)pageContext.findAttribute("prefSet");
        Iterator prefIter = comp.iterator();
        pageContext.setAttribute("prefIter", prefIter);
    %>
    
    <portlet:actionURL var="edit_preferenece_link" >
    </portlet:actionURL>
    
    <form action="<c:out value="${edit_preferenece_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value=""/>
        <table border="0">
            <tr>
                <th class="portlet-section-header">&nbsp;</th>
                <th class="portlet-section-header"><fmt:message key="pam.details.name"/></th>
                <th class="portlet-section-header"><fmt:message key="pam.details.value"/></th>
            </tr>
        <c:forEach var="pref" items="${prefIter}">
            <tr>
                <td class="portlet-section-body">
                    <input type="checkbox" name="pref_remove_id" value="<c:out value="${pref.name}"/>"/>
                </td>
                <td class="portlet-section-body">
                    <c:out value="${pref.name}"/>
                    <input type="hidden" name="pref_edit_id" value="<c:out value="${pref.name}"/>"/>
                </td>
                <td class="portlet-section-body">
                    <table>
                    <c:forEach var="value" items="${pref.values}" varStatus="status">
                        <tr>
                            <td>
                                <input type="text" name="<c:out value="${pref.name}"/>:<c:out value="${status.index}"/>" value="<c:out value="${value}"/>" class="portlet-form-field-label"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </table>
                </tr>
            </tr>
        </c:forEach>
        </table>
        
        <input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet.edit_preference'"  class="portlet-form-button"/>
        <input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet.remove_preference'"  class="portlet-form-button"/>
    </form>
    
    <hr />
    
    
    <form action="<c:out value="${edit_preferenece_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.add_preference"/>
        <table>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.name"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="name" class="portlet-form-field-label"/>
                </td>
                <%--TODO add combo box of existing keys--%>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.value"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="value" class="portlet-form-field-label"/>
                </td>
            </tr>
            <%--
            <tr>
                <td>
                    <fmt:message key="pam.details.type"/>
                </td>
                <td>
                    <select  class="portlet-form-field">
                        <option value="string">String</option>
                        <option value="int">Int</option>
                    </select>
                </td>
            </tr>
            --%>
        </table>
        <input type="submit" value="<fmt:message key="pam.details.add_preference"/>" class="portlet-form-button"/>
    </form>
</c:if>

<c:if test="${selectedPortletTab.id == 'pd_languages'}">
    <c:set var="langSet" value="${selectedPDef.languageSet}"/>
    
    <portlet:actionURL var="edit_language_link" >
    </portlet:actionURL>
    
    <form action="<c:out value="${edit_language_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value=""/>
    <table border="0">
        <tr>
            <th class="portlet-section-header">&nbsp;</th>
            <th class="portlet-section-header"><fmt:message key="pam.details.title"/></th>
            <th class="portlet-section-header"><fmt:message key="pam.details.short_title"/></th>
            <th class="portlet-section-header"><fmt:message key="pam.details.keyword"/></th>
            <th class="portlet-section-header"><fmt:message key="pam.details.locale"/></th>
        </tr>
    <c:forEach var="lang" items="${langSet.innerCollection}" varStatus="status">
        <tr>
            <td class="portlet-section-body">
                <input type="checkbox" name="language_remove_id" value="<c:out value="${status.index}"/>"/>
                <input type="hidden" name="language_edit_id" value="<c:out value="${status.index}"/>"/>
            </td>
            <td class="portlet-section-body">
                <input type="text" name="title:<c:out value="${status.index}"/>" value="<c:out value="${lang.title}"/>" class="portlet-form-field-label"/>
            </td>
            <td class="portlet-section-body">
                <input type="text" name="short_title:<c:out value="${status.index}"/>" value="<c:out value="${lang.shortTitle}"/>"  class="portlet-form-field-label"/>
            </td>
            <td class="portlet-section-body">
                <table>
                <c:forEach var="keyword" items="${lang.keywords}" varStatus="keywordStatus">
                    <tr>
                        <td>
                            <input type="text" name="keyword:<c:out value="${status.index}"/>:<c:out value="${keywordStatus.index}"/>" value="<c:out value="${keyword}"/>"  class="portlet-form-field-label"/>
                        </td>
                    </tr>
                </c:forEach>
                </table>
                <%--
                <input type="text" name="keyword:<c:out value="${status.index}"/>" value="<c:forEach var="keyword" items="${lang.keywords}" varStatus="keywordStatus"><c:out value="${keyword}"/>,</c:forEach>" class="portlet-form-field-label"/>
                --%>
            </td>
            <td class="portlet-section-body">
                <c:out value="${lang.locale}"/>
            </td>
        </tr>
    </c:forEach>
    </table>
    
        <input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet.edit_language'"  class="portlet-form-button"/>
        <input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet.remove_language'"  class="portlet-form-button"/>
    </form>
    
    <form action="<c:out value="${edit_language_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.add_language"/>
        
        <table>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.title"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="title" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.title.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.short_title"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="short_title" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.short_title.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.keyword"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="keyword" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.keyword.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.locale"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="locale" value="" size="2"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.locale.description"/>
                </td>
            </tr>
        </table>
        
        <input type="submit" value="<fmt:message key="pam.details.add_language"/>" class="portlet-form-button"/>
    </form>
</c:if>

<c:if test="${selectedPortletTab.id == 'pd_parameters'}">
    <c:set var="paramSet" value="${selectedPDef.initParameterSet}"/>
    
    <portlet:actionURL var="edit_parameter_link" >
    </portlet:actionURL>
    
    <%--
        TODO:  if needed, place iterator into page context
        see prefs section
    --%>
    
    <form action="<c:out value="${edit_parameter_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value=""/>
    
    <table border="0">
        <tr>
            <th class="portlet-section-header">&nbsp;</th>
            <th class="portlet-section-header"><fmt:message key="pam.details.name"/></th>
            <th class="portlet-section-header"><fmt:message key="pam.details.value"/></th>
            <th class="portlet-section-header"><fmt:message key="pam.details.locale"/> / <fmt:message key="pam.details.description"/></th>
        </tr>
    <c:forEach var="theparam" items="${paramSet.innerCollection}">
        <tr>
            <td class="portlet-section-body">
                <input type="checkbox" name="parameter_remove_id" value="<c:out value="${theparam.name}"/>" />
            </td>
            <td class="portlet-section-body">
                <input type="hidden" name="parameter_edit_id" value="<c:out value="${theparam.name}"/>" />
                <c:out value="${theparam.name}"/>
            </td>
            <td class="portlet-section-body">
                <input type="text" name="<c:out value="${theparam.name}"/>:value" value="<c:out value="${theparam.value}"/>"  class="portlet-form-field-label"/>
            </td>
            <td class="portlet-section-body">
                <%
                    ParameterComposite param = (ParameterComposite)pageContext.findAttribute("theparam");
                    pageContext.setAttribute("description_set", param.getDescriptionSet());
                %>
                <table>
                <c:forEach var="description" items="${description_set.innerCollection}" varStatus="descStatus">
                    <tr>
                        <td class="portlet-section-alternate">
                            <c:out value="${description.locale}"/>
                        </td>
                        <td class="portlet-section-body">
                            <input type="text" name="<c:out value="${theparam.name}"/>:description:<c:out value="${descStatus.index}"/>"
                                value="<c:out value="${description.description}"/>"  class="portlet-form-field-label"/>
                        </td>
                    </tr>
                </c:forEach>
                </table>
            </td>
        </tr>
    </c:forEach>
    </table>
    
        <input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet.edit_parameter'"  class="portlet-form-button"/>
        <input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet.remove_parameter'" class="portlet-form-button"/>
    
    </form>
    
    <form action="<c:out value="${edit_parameter_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.add_parameter"/>
        <table>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.name"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="name" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.name.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.value"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="value" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.value.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.description"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="description" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.description.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.locale"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="locale" value="" size="2"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.locale.description"/>
                </td>
            </tr>
        </table>
        <input type="submit" value="<fmt:message key="pam.details.add_parameter"/>"  class="portlet-form-button"/>
    </form>
    
</c:if>
<c:if test="${selectedPortletTab.id == 'pd_security'}">
    <c:set var="roleSet" value="${selectedPDef.initSecurityRoleRefSet}"/>
    
    <portlet:actionURL var="edit_security_link" >
    </portlet:actionURL>

    <c:if test="${! empty roleSet.innerCollection}">
    <form action="<c:out value="${edit_security_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value=""/>    
    
        <table border="0">
            <tr>
                <th class="portlet-section-header">&nbsp;</th>
                <th class="portlet-section-header"><fmt:message key="pam.details.role_name"/></th>
                <th class="portlet-section-header"><fmt:message key="pam.details.role_link"/></th>
                <th class="portlet-section-header"><fmt:message key="pam.details.locale"/> / <fmt:message key="pam.details.description"/></th>
            </tr>
        <c:forEach var="therole" items="${roleSet.innerCollection}">
            <tr>
                <td class="portlet-section-body">
                    <input type="checkbox" name="security_remove_id" value="<c:out value="${therole.roleName}"/>"/>
                </td>
                <td class="portlet-section-body">
                    <input type="hidden" name="security_edit_id" value="<c:out value="${therole.roleName}"/>"/>
                    <input type="text" name="<c:out value="${therole.roleName}"/>:name" value="<c:out value="${therole.roleName}"/>"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="<c:out value="${therole.roleName}"/>:link" value="<c:out value="${therole.roleLink}"/>"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <%
                        SecurityRoleRefComposite ref = (SecurityRoleRefComposite)pageContext.findAttribute("therole");
                        pageContext.setAttribute("description_set", ref.getDescriptionSet());
                    %>
                    <table>
                    <c:forEach var="description" items="${description_set.innerCollection}" varStatus="descStatus">
                        <tr>
                            <td class="portlet-section-alternate">
                                <c:out value="${description.locale}"/>
                            </td>
                            <td class="portlet-section-body">
                                <input type="text" name="<c:out value="${therole.roleName}"/>:description:<c:out value="${descStatus.index}"/>"
                                    value="<c:out value="${description.description}"/>"  class="portlet-form-field-label"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </table>
                </td>
            </tr>
        </c:forEach>
        </table>
    
        <input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet.edit_security'"  class="portlet-form-button"/>
        <input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet.remove_security'"  class="portlet-form-button"/>
    
    </form>
    </c:if>

    <form action="<c:out value="${edit_security_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.add_security"/>
        <table>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.role_name"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="name" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.role_name.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.role_link"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="link" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.role_link.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.description"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="description" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.description.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.locale"/>
                </td>
                <td class="portlet-section-body">
                    <input type="text" name="locale" value="" size="2"  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.locale.description"/>
                </td>
            </tr>
        </table>
        <input type="submit" value="<fmt:message key="pam.details.add_security"/>"  class="portlet-form-button"/>
    </form>

</c:if>

<c:if test="${selectedPortletTab.id == 'pd_content_type'}">
    <c:set var="contentTypeSet" value="${selectedPDef.contentTypeSet}"/>
    
    <portlet:actionURL var="edit_content_type_link" >
    </portlet:actionURL>

    <c:if test="${! empty contentTypeSet.innerCollection}">
    <form action="<c:out value="${edit_content_type_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value=""/>
        <table border="0">
            <tr>
                <th class="portlet-section-header">&nbsp;</th>
                <th class="portlet-section-header"><fmt:message key="pam.details.content_type"/></th>
                <th class="portlet-section-header"><fmt:message key="pam.details.modes"/></th>
            </tr>
        <c:forEach var="contentType" items="${contentTypeSet.innerCollection}">
            <tr>
                <td class="portlet-section-body">
                    <input type="checkbox" name="content_type_remove_id" value="<c:out value="${contentType.contentType}"/>"/>
                </td>
                <td class="portlet-section-body">
                    <c:out value="${contentType.contentType}"/>
                </td>
                <td class="portlet-section-body">            
                    <c:forEach var="mode" items="${contentType.portletModes}">
                        <c:out value="${mode}"/>, 
                    </c:forEach>
                </td>            
            </tr>
        </c:forEach>
        </table>
        
        <input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = 'portlet.edit_content_type'"  class="portlet-form-button"/>
        <input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = 'portlet.remove_content_type'" class="portlet-form-button"/>
    </form>
    </c:if>
    
    <form action="<c:out value="${edit_content_type_link}"/>" method="post">
        <input type="hidden" name="portlet_action" value="portlet.add_content_type"/>
        <table border="0">
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.content_type"/>
                </td>
                <td class="portlet-section-body">
                    <%--TODO:  this could be a select box--%>
                    <input type="text" name="content_type" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.content_type.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.portlet_mode"/>
                </td>
                <td class="portlet-section-body">
                    <select name="mode" multiple="true" class="portlet-form-field">
                        <option value="view"><fmt:message key="pam.details.portlet_mode.view"/></option>
                        <option value="edit"><fmt:message key="pam.details.portlet_mode.edit"/></option>
                        <option value="help"><fmt:message key="pam.details.portlet_mode.help"/></option>
                    </select>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.portlet_mode.description"/>
                </td>
            </tr>
            <tr>
                <td class="portlet-section-alternate">
                    <fmt:message key="pam.details.custom_mode"/>
                </td>
                <td class="portlet-section-body">
                    <%--TODO:  this could be a select box--%>
                    <input type="text" name="custom_modes" value=""  class="portlet-form-field-label"/>
                </td>
                <td class="portlet-section-body">
                    <fmt:message key="pam.details.custom_modes.description"/>
                </td>
            </tr>
        </table>
        <input type="submit" value="<fmt:message key="pam.details.add_content_type"/>"  class="portlet-form-button"/>
    </form>
</c:if>
