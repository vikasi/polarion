/*
 * Copyright (C) 2004-2015 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2015 Polarion Software
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
package com.polarion.example.widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.polarion.alm.shared.api.Scope;
import com.polarion.alm.shared.api.model.rp.parameter.EnumParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ObjectSelectorParameter;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetDependenciesContext;
import com.polarion.alm.shared.api.model.wi.WorkItem;

public class WorkRecordReportWidgetDependenciesProcessor {
    @NotNull
    private final RichPageWidgetDependenciesContext context;
    @Nullable
    private final Scope scope;

    public WorkRecordReportWidgetDependenciesProcessor(@NotNull RichPageWidgetDependenciesContext context) {
        this.context = context;

        ObjectSelectorParameter parameter = context.parameter(WorkRecordReportWidget.PARAMETER_USER_STORY);
        WorkItem workItem = (WorkItem) parameter.value();

        scope = workItem == null ? null : workItem.getReference().scope();
    }

    public void process() {
        EnumParameter parameter = context.parameter(WorkRecordReportWidget.PARAMETER_LINK_ROLE);
        parameter.set().scope(scope);
    }
}
