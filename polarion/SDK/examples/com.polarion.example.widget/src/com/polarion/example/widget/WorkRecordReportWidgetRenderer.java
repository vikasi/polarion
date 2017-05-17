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

import com.polarion.alm.shared.api.model.eo.EnumOption;
import com.polarion.alm.shared.api.model.fields.DurationValue;
import com.polarion.alm.shared.api.model.rp.parameter.CompositeParameter;
import com.polarion.alm.shared.api.model.rp.parameter.EnumParameter;
import com.polarion.alm.shared.api.model.rp.parameter.ObjectSelectorParameter;
import com.polarion.alm.shared.api.model.rp.parameter.RichPageParameter;
import com.polarion.alm.shared.api.model.rp.parameter.StringParameter;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidget;
import com.polarion.alm.shared.api.model.rp.widget.RichPageWidgetRenderingContext;
import com.polarion.alm.shared.api.model.wi.WorkItem;
import com.polarion.alm.shared.api.model.wi.linked.LinkedWorkItem;
import com.polarion.alm.shared.api.model.wr.WorkRecord;
import com.polarion.alm.shared.api.utils.html.HtmlContentBuilder;
import com.polarion.alm.shared.api.utils.html.HtmlFragmentBuilder;
import com.polarion.alm.shared.api.utils.html.HtmlTagBuilder;

@SuppressWarnings("nls")
public class WorkRecordReportWidgetRenderer {
    @NotNull
    private final RichPageWidgetRenderingContext context;
    //@NotNull
    private final WorkItem userStory;

    //@NotNull
    private EnumOption linkRole;
    @Nullable
    private String tableStyle;
    @Nullable
    private String headerCellStyle;
    @Nullable
    private String rowCellStyle;
    //@Nullable
    private String warningMessage;

    private boolean hasPermission;

    public WorkRecordReportWidgetRenderer(@NotNull final RichPageWidgetRenderingContext context) {
        this.context = context;

        userStory = getUserStory();
        if (userStory == null) {
            return;
        }
        hasPermission = userStory.fields().timeSpent().can().modify();

        linkRole = getRequiredEnumParameterValue(WorkRecordReportWidget.PARAMETER_LINK_ROLE);

        CompositeParameter advanced = (CompositeParameter) context.parameter(RichPageWidget.PARAMETER_ADVANCED);
        tableStyle = getStringParameterValue(advanced, WorkRecordReportWidget.PARAMETER_TABLE_STYLE);
        headerCellStyle = getStringParameterValue(advanced, WorkRecordReportWidget.PARAMETER_TABLE_HEADER_CELL_STYLE);
        rowCellStyle = getStringParameterValue(advanced, WorkRecordReportWidget.PARAMETER_TABLE_ROW_CELL_STYLE);
    }

    private void reportWarning(@NotNull String message) {
        warningMessage = message;
    }

    @Nullable
    private WorkItem getUserStory() {
        ObjectSelectorParameter parameter = context.parameter(WorkRecordReportWidget.PARAMETER_USER_STORY);

        WorkItem workItem = (WorkItem) parameter.value();

        if (workItem == null) {
            reportWarning("User story is undefined."); //$NON-NLS-1$
            return null;
        }
        return workItem;
    }

    @NotNull
    public String render() {
        HtmlFragmentBuilder builder = context.createHtmlFragmentBuilder();
        if (warningMessage != null) {
            return context.renderWarning(warningMessage);
        }

        HtmlTagBuilder table = builder.tag().table();
        table.attributes().className("polarion-rpw-table-content").style(tableStyle);

        appendTableHeaderRow(table, userStory);
        DurationValue total = appendWorkItemRows(table, userStory);
        appendTotalRow(table, total);

        return builder.toString();
    }

    private void renderAction(@NotNull HtmlContentBuilder htmlContentBuilder, @NotNull DurationValue total) {
        HtmlTagBuilder a = htmlContentBuilder.tag().a();
        a.append().text("Update time spent in the user story");
        a.attributes().byName(RichPageWidget.ATTRIBUTE_ACTION_ID, total.toString());
        a.attributes().byName(RichPageWidget.ATTRIBUTE_CONFIRM_TITLE, "Update time spent");
        a.attributes().byName(RichPageWidget.ATTRIBUTE_CONFIRM_TEXT, "Do you want to update time spent?");

    }

    @Nullable
    private DurationValue appendWorkItemRows(@NotNull final HtmlTagBuilder table, @NotNull final WorkItem userStory) {
        DurationValue total = null;

        for (LinkedWorkItem linkedWorkItem : userStory.fields().linkedWorkItems().back()) {
            DurationValue sum = null;
            WorkItem workItem = linkedWorkItem.fields().workItem().getIfCan();
            EnumOption workItemRole = linkedWorkItem.fields().role().getIfCan();
            if (workItem != null && workItemRole != null && linkRole.id().equals(workItemRole.id())) {
                for (WorkRecord workRecord : workItem.fields().workRecords()) {
                    DurationValue durationValue = workRecord.fields().timeSpent().getIfCan();
                    if (durationValue != null) {
                        sum = sum == null ? durationValue : sum.add(durationValue);
                    }
                }
                if (total == null) {
                    total = sum;
                } else {
                    if (sum != null) {
                        total = total.add(sum);
                    }
                }
                appendWorkItemRow(table, workItem, sum);
            }
        }
        return total;
    }

    @NotNull
    private String getStringParameterValue(@NotNull final CompositeParameter advanced, @NotNull final String parameterId) {
        StringParameter stringParameter = advanced.get(parameterId);
        String value = stringParameter.value();
        return value == null ? "" : value;
    }

    @Nullable
    private EnumOption getRequiredEnumParameterValue(@NotNull final String parameterId) {
        EnumParameter parameter = context.parameter(parameterId);
        EnumOption value = parameter.singleValue();
        if (value == null) {
            setRequiredParameterWarningMessage(parameter);
        }
        return value;
    }

    private void setRequiredParameterWarningMessage(@NotNull final RichPageParameter parameter) {
        warningMessage = "Parameter '" + parameter.label() + "' is required.";
    }

    private void appendWorkItemRow(@NotNull final HtmlTagBuilder table, @NotNull final WorkItem workItem, @Nullable final DurationValue sum) {
        HtmlTagBuilder tr = table.append().tag().tr();
        tr.attributes().className("polarion-rpw-table-content-row");
        HtmlTagBuilder workItemCell = tr.append().tag().td();
        workItemCell.attributes().style(rowCellStyle);
        workItem.render().withTitle().htmlTo(workItemCell.append());

        HtmlTagBuilder durationCountCell = tr.append().tag().td();
        durationCountCell.attributes().style(rowCellStyle);

        String sumToRender = sum == null ? "0" : sum.normalize().toString();
        durationCountCell.append().text(sumToRender);
    }

    private void appendTotalRow(@NotNull final HtmlTagBuilder table, @Nullable final DurationValue total) {
        HtmlTagBuilder tr = table.append().tag().tr();
        tr.attributes().className("polarion-rpw-table-content-row");
        HtmlTagBuilder totalLabelCell = tr.append().tag().td();
        totalLabelCell.attributes().style("font-weight:bold;" + rowCellStyle);
        totalLabelCell.append().text("Total:");

        String totalToRender = total == null ? "0" : total.normalize().toString();
        HtmlTagBuilder totalCell = tr.append().tag().td();
        totalCell.attributes().style("font-weight:bold;" + rowCellStyle);
        totalCell.append().text(totalToRender);

        if (hasPermission && total != null && !total.equals(userStory.fields().timeSpent().get())) {
            totalCell.append().nbsp().text("(");
            renderAction(totalCell.append(), total);
            totalCell.append().text(")");
        }
    }

    private void appendTableHeaderRow(@NotNull final HtmlTagBuilder table, @NotNull final WorkItem userStory) {
        HtmlTagBuilder tr = table.append().tag().tr();
        tr.attributes().className("polarion-rpw-table-header-row");
        HtmlTagBuilder userStoryCell = tr.append().tag().th();
        userStoryCell.attributes().style(headerCellStyle);
        userStory.render().withTitle().withLinks().htmlTo(userStoryCell.append());

        HtmlTagBuilder timeSpentCell = tr.append().tag().th();
        timeSpentCell.attributes().style(headerCellStyle);
        timeSpentCell.append().text("Time Spent:");
    }
}