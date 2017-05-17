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

import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import com.google.gwt.core.shared.GwtIncompatible;
import com.polarion.alm.shared.api.SharedContext;
import com.polarion.alm.shared.api.model.PrototypeEnum;
import com.polarion.alm.shared.api.model.rp.parameter.CompositeParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ObjectSelectorParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ParameterFactory;
import com.polarion.alm.shared.api.model.rp.parameter.RichPageParameter;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidget;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetActionContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetDependenciesContext;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetRenderingContext;
import com.polarion.alm.shared.api.model.wi.UpdatableWorkItem;
import com.polarion.alm.shared.api.model.wi.WorkItem;
import com.polarion.alm.shared.api.utils.collections.StrictMap;
import com.polarion.alm.shared.api.utils.collections.StrictMapImpl;
import com.polarion.portal.shared.Duration;

@SuppressWarnings("nls")
public class WorkRecordReportWidget extends RichPageWidget {

    private static final String WORK_RECORDS_WIDGET_ICON = "workRecords.png";

    static final String PARAMETER_USER_STORY = "userStory";
    static final String PARAMETER_LINK_ROLE = "linkRole";

    static final String PARAMETER_TABLE_ROW_CELL_STYLE = "tableRowCellStyle";
    static final String PARAMETER_TABLE_HEADER_CELL_STYLE = "tableHeaderCellStyle";
    static final String PARAMETER_TABLE_STYLE = "tableStyle";

    @Override
    @NotNull
    public String getIcon(@NotNull final RichPageWidgetContext context) {
        return context.resourceUrl(WORK_RECORDS_WIDGET_ICON);
    }

    @Override
    @NotNull
    @GwtIncompatible
    public InputStream getResourceStream(@NotNull final String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException(String.format("Requested resource '%s' was not found.", path));
        }
        return stream;
    }

    @Override
    @NotNull
    public String getLabel(@NotNull final SharedContext context) {
        return "Work Record Report Widget";
    }

    @Override
    @NotNull
    public String getDetailsHtml(@NotNull final RichPageWidgetContext widgetContext) {
        return "Work Record Report calculates work records from items defined by its parent and link role.";
    }

    @Override
    @NotNull
    public StrictMap<String, RichPageParameter> getParametersDefinition(@NotNull final ParameterFactory factory) {
        StrictMap<String, RichPageParameter> parameters = new StrictMapImpl<String, RichPageParameter>();
        parameters.put(PARAMETER_USER_STORY, factory.objectSelector("User Story").allowedPrototypes(PrototypeEnum.WorkItem).dependencySource(true).build());
        parameters.put(PARAMETER_LINK_ROLE, factory.enumeration("Link Role", "workitem-link-role").dependencyTarget(true).build());
        parameters.put(PARAMETER_ADVANCED, buildAdvancedParameter(factory));
        return parameters;
    }

    @NotNull
    private CompositeParameter buildAdvancedParameter(@NotNull final ParameterFactory factory) {
        return factory.composite("Advanced")
                .collapsedByDefault(true)
                .add(PARAMETER_TABLE_STYLE, factory.string("Table Style").build())
                .add(PARAMETER_TABLE_HEADER_CELL_STYLE, factory.string("Table Header Cell Style").value("white-space:nowrap;").build())
                .add(PARAMETER_TABLE_ROW_CELL_STYLE, factory.string("Table Cell Style").build())
                .build();
    }

    @Override
    @NotNull
    public String renderHtml(@NotNull final RichPageWidgetRenderingContext context) {
        return new WorkRecordReportWidgetRenderer(context).render();
    }

    @Override
    public void processParameterDependencies(@NotNull RichPageWidgetDependenciesContext context) {
        new WorkRecordReportWidgetDependenciesProcessor(context).process();
    }

    @Override
    public void executeAction(@NotNull RichPageWidgetActionContext context) {
        ObjectSelectorParameter parameter = context.parameter(PARAMETER_USER_STORY);
        WorkItem workItem = (WorkItem) parameter.value();
        if (workItem == null) {
            return;
        }
        Duration value;
        try {
            value = new Duration(context.actionId());
        } catch (Exception e) {
            return;
        }
        UpdatableWorkItem updatableWorkItem = workItem.getUpdatable(context.transaction());
        updatableWorkItem.fields().timeSpent().set(value);
        updatableWorkItem.save();
        context.refresh(true);

    }
}
