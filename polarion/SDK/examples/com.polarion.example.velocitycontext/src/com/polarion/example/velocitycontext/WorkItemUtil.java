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
package com.polarion.example.velocitycontext;

import org.jetbrains.annotations.NotNull;

import com.polarion.alm.shared.api.model.fields.DurationValue;
import com.polarion.alm.shared.api.model.wi.WorkItem;
import com.polarion.alm.shared.api.model.wr.WorkRecord;

public class WorkItemUtil {

    @NotNull
    public String printTimeSpent(@NotNull WorkItem workItem) {
        DurationValue total = null;
        for (WorkRecord workRecord : workItem.fields().workRecords()) {
            DurationValue durationValue = workRecord.fields().timeSpent().getIfCan();
            if (durationValue != null) {
                total = total == null ? durationValue : total.add(durationValue);
            }
        }

        return getMessage(workItem, total == null ? "0h" : total.normalize().toString()); //$NON-NLS-1$

    }

    private String getMessage(@NotNull WorkItem workItem, @NotNull String totalToRender) {
        return String.format("Time Spent on Work Item '%s' is %s.", workItem.fields().id().get(), totalToRender); //$NON-NLS-1$
    }
}
