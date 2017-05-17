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
package com.polarion.example.enumerationfactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.polarion.alm.tracker.ITimePointsManager;
import com.polarion.alm.tracker.ITrackerService;
import com.polarion.alm.tracker.model.ITimePoint;
import com.polarion.platform.persistence.IEnumOption;
import com.polarion.platform.persistence.IEnumeration;
import com.polarion.platform.persistence.model.IPObject;
import com.polarion.platform.persistence.spi.AbstractObjectEnumFactory;
import com.polarion.platform.persistence.spi.AbstractObjectEnumeration;
import com.polarion.platform.persistence.spi.EnumOption;
import com.polarion.subterra.base.data.identification.IContextId;

public class MyTimePointsEnumFactory extends AbstractObjectEnumFactory {

    private final ITimePointsManager timePointsManager;

    public MyTimePointsEnumFactory(ITrackerService trackerService) {
        super();
        timePointsManager = trackerService.getTimePointsManager();
    }

    @Override
    public String getName() {
        return "My Time Points"; //$NON-NLS-1$
    }

    @Override
    public String getOptionalFieldName() {
        return "Query"; //$NON-NLS-1$
    }

    @Override
    public IEnumeration getEnumeration(final String enumId, final IContextId contextId) {
        final String query = extractValueFromEnumId(enumId);
        return new AbstractObjectEnumeration(enumId) {

            @Override
            public IEnumOption wrapOption(String optionId) {
                LinkedHashMap<String, ITimePoint> timePoints = getTimePoints(query, contextId);
                ITimePoint timePoint = timePoints.get(optionId);
                if (timePoint == null) {
                    return createPhantomOption(enumId, optionId);
                }
                return wrapTimePoint(enumId, timePoint);
            }

            @Override
            public List getAvailableOptions(Object controlValue, IEnumOption currentValue) {
                List<IEnumOption> options = new ArrayList<IEnumOption>();
                LinkedHashMap<String, ITimePoint> timePoints = getTimePoints(query, contextId);
                boolean currentPresent = false;
                for (ITimePoint timePoint : timePoints.values()) {
                    if (currentValue != null && timePoint.getId().equals(currentValue.getId())) {
                        currentPresent = true;
                    }
                    options.add(wrapTimePoint(enumId, timePoint));
                }
                if (!currentPresent && currentValue != null) {
                    options.add(currentValue);
                }
                return options;
            }

            @Override
            @NotNull
            public IEnumOption wrapObject(@NotNull IPObject object) {
                if (object instanceof ITimePoint) {
                    ITimePoint timePoint = (ITimePoint) object;
                    return wrapTimePoint(enumId, timePoint);
                }
                throw new IllegalArgumentException();
            }

        };
    }

    protected LinkedHashMap<String, ITimePoint> getTimePoints(String query, IContextId contextId) {
        List<ITimePoint> timePoints = timePointsManager.getTimePoints(contextId, query, ITimePoint.KEY_TIME, true, false, true);
        LinkedHashMap<String, ITimePoint> timePointsMap = new LinkedHashMap<String, ITimePoint>();
        for (ITimePoint timePoint : timePoints) {
            if (!isNullOrUnresolvable(timePoint)) {
                timePointsMap.put(timePoint.getId(), timePoint);
            }
        }
        return timePointsMap;
    }

    protected IEnumOption wrapTimePoint(String enumId, ITimePoint timePoint) {
        return new EnumOption(enumId, timePoint.getId(), timePoint.getName() + " (" + timePoint.getTime() + ")", 0, false, getExtendedProperties(timePoint, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
