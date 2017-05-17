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
package com.polarion.example.notifications.targets;

import java.util.HashSet;
import java.util.Set;

import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.platform.persistence.model.IPObject;
import com.polarion.psvn.core.notifications.INotificationData;
import com.polarion.psvn.core.notifications.INotificationEvent;
import com.polarion.psvn.core.notifications.IPObjectNotificationData;
import com.polarion.psvn.core.notifications.ITarget;

/**
 * Implementation of new target to be used in notification targets configuration as
 * one of targets for any event.
 * The name of this target is "custom-field-targets" and has attribute "fieldId" which
 * specify the ID of custom field, from which users ids will be fetched. (users should 
 * be delimited by comma)
 * 
 * @author <a href="mailto:dev@polarion.com">Michal Antolik</a>, Polarion Software
 */

public class CustomFieldTargets implements ITarget {

	/* (non-Javadoc)
	 * @see com.polarion.psvn.core.notifications.ITarget#getNotificationTargets(com.polarion.psvn.core.notifications.INotificationEvent, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Set getNotificationTargets(INotificationEvent event,
			String paramValue) throws Exception {
		Set<String> ids = new HashSet<String>();
        
        INotificationData nData = event.getNotificationData();
        if(nData instanceof IPObjectNotificationData) {
            IPObjectNotificationData data = (IPObjectNotificationData) nData;
            
            IPObject target = data.getAfter();
            if (target == null || !(target instanceof IWorkItem)) {
            	return ids;
            }
            
            IWorkItem wi = (IWorkItem)target;
            if (wi != null && !wi.isUnresolvable()) {
            	Object cf = wi.getCustomField(paramValue);
            	if((cf != null) && (cf instanceof String)){
            		String[] userIds = ((String)cf).split("(\\s)*,(\\s)*");
            		for(int i = 0; i < userIds.length; i++){
            			ids.add(userIds[i]);
            		}
            	}
            }
        }
        return ids;
	}

	/* (non-Javadoc)
	 * @see com.polarion.psvn.core.notifications.ITargetMetaInfo#getParamId()
	 */
	public String getParamId() {
		return "fieldId";
	}

	/* (non-Javadoc)
	 * @see com.polarion.psvn.core.notifications.ITargetMetaInfo#getParamName()
	 */
	public String getParamName() {
		return getParamId();
	}

	/* (non-Javadoc)
	 * @see com.polarion.psvn.core.notifications.ITargetMetaInfo#getTagName()
	 */
	public String getTagName() {
		return "custom-field-targets";
	}

}
