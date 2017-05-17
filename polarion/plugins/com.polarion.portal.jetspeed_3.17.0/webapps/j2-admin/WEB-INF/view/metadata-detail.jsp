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
<form name="Edit_Metatdata_Form" action="<c:out value="${edit_metadata_link}"/>" method="post">
	<input type="hidden" name="portlet_action" value=""/>
	<table>
		<tr>
			<th class="portlet-section-header">&nbsp;</th>
			<th class="portlet-section-header"><fmt:message key="pam.details.name"/></th>
			<th class="portlet-section-header"><fmt:message key="pam.details.locale"/></th>
			<th class="portlet-section-header"><fmt:message key="pam.details.value"/></th>
		</tr>
	<c:forEach var="field" items="${md.fields}">
		<tr>
			<td class="portlet-section-body">
				<input type="checkbox" name="metadata_id" value="<c:out value="${field.id}"/>"/>
			</td>
			<td class="portlet-section-body">	
				<c:out value="${field.name}"/>
			</td>
			<td align="center" class="portlet-section-body">
				<c:out value="${field.locale}"/> 
				
			</td>
			<td class="portlet-section-body">
				<%--TODO:  value needs to escaped, or use textarea--%>
				<input type="text" name="<c:out value="${field.id}"/>:value" value="<c:out value="${field.value}"/>" size="50"/>
			</td>
		</tr>
	</c:forEach>
	</table>
			
	<input type="submit" value="<fmt:message key="pam.details.edit"/>" onClick="this.form.portlet_action.value = '<c:out value="${action_prefix}"/>edit_metadata'" class="portlet-form-button"/>
	<input type="submit" value="<fmt:message key="pam.details.remove"/>" onClick="this.form.portlet_action.value = '<c:out value="${action_prefix}"/>remove_metadata'" class="portlet-form-button"/>
</form>
		
<form action="<c:out value="${edit_metadata_link}"/>" method="post">
	<input type="hidden" name="portlet_action" value="<c:out value="${action_prefix}"/>add_metadata"/>
	<div>
		<table>
			<tr>
				<td class="portlet-section-alternate">
					<span class="portlet-form-label"><fmt:message key="pam.details.name"/></span>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="name" value="" class="portlet-form-field-label"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<span class="portlet-form-label"><fmt:message key="pam.details.value"/></span>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="value" value="" class="portlet-form-field-label"/>
				</td>
			</tr>
			<tr>
				<td class="portlet-section-alternate">
					<span class="portlet-form-label"><fmt:message key="pam.details.locale"/></span>
				</td>
				<td class="portlet-section-body">
					<input type="text" name="locale" value="" class="portlet-form-field-label"/>
				</td>
			</tr>
		</table>
	</div>
	<input type="submit" value="<fmt:message key="pam.details.add_metadata"/>"  class="portlet-form-button"/>
</form>