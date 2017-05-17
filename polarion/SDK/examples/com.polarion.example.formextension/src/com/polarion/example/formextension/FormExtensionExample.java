/*
 * Copyright (C) 2004-2014 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2014 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software.  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * POLARION SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES 
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. POLARION SOFTWARE
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.example.formextension;

import java.util.Map;

import javax.inject.Inject;

import com.polarion.alm.tracker.model.ISeverityOpt;
import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.alm.ui.server.forms.extensions.IFormExtension;
import com.polarion.core.util.EscapeChars;
import com.polarion.platform.persistence.IDataService;
import com.polarion.platform.persistence.model.IPObject;

/**
 * Example of form extension.
 * To see it in Polarion:
 * 1) Install this example plugin to Polarion.
 * 2) add {@code <extension id="example" label="Example Extension"/> } to form layout in the Polarion administration. 
 */
public class FormExtensionExample implements IFormExtension {
    public static final String ID = "example";

    @Inject
    private IDataService service;

    @Override
    public String render(IPObject object, Map<String, String> attributes) {
        if (object.isPersisted() && object instanceof IWorkItem) {
            IWorkItem workItem = (IWorkItem) object;
            ISeverityOpt severity = workItem.getSeverity();
            if (severity != null) {
                String query = "project.id:" + workItem.getProjectId() + " AND severity:" + severity.getId();
                int count = service.getInstancesCount(IWorkItem.PROTO, query);
                return "There are " + count + " Work Items with " + EscapeChars.forHTMLTag(severity.getName()) + " severity in current project";
            }

        }
        return null;
    }

}
