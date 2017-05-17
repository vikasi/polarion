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
package com.polarion.example.workflow.functions;

import java.util.Calendar;
import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.alm.tracker.workflow.IArguments;
import com.polarion.alm.tracker.workflow.ICallContext;
import com.polarion.alm.tracker.workflow.IFunction;
import com.polarion.core.util.exceptions.UserFriendlyRuntimeException;
import com.polarion.subterra.base.data.model.ICustomField;
import com.polarion.subterra.base.data.model.IPrimitiveType;
import com.polarion.subterra.base.data.model.IType;

/**
 * Each implementation of workflow function has to implement IFunction class
 * and implement 'execute' method. This function will count the time from 
 * creation of work item until now ( what usually is the action when status 
 * will be set to closed). The result will be written to custom field with name,
 * which is read from 'IArguments' class instance (the second parameter of 'execute' method) - 
 * these arguments are set in workflow designer in administration perspective
 * at Polarion Server web site. 
 * 
 * @author Michal Antolik
 */
public class ComputeTotalLifeTime implements IFunction {

	static final private long day = 24 * 3600 * 1000L;
	static final private long hour = 3600 * 1000L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.polarion.alm.tracker.workflow.IFunction#execute(com.polarion.alm.tracker.workflow.ICallContext,
	 *      com.polarion.alm.tracker.workflow.IArguments)
	 */
	public void execute(ICallContext context, IArguments arguments) {

		String field = arguments.getAsString("field");

		IWorkItem wi = context.getWorkItem();
		ICustomField cf = wi.getCustomFieldPrototype(field);

		if (cf == null) {
			throw new UserFriendlyRuntimeException(
					"Invalid action parameter: Specified field doesn't exist.");
		}

		IType type = cf.getType();
		if (!(type instanceof IPrimitiveType)
				|| !(((IPrimitiveType) type).getTypeName()
						.equals("java.lang.String"))) {
			throw new UserFriendlyRuntimeException(
					"Invalid action parameter: Incorrect field type - expected 'string'");
		}

		long creationDate = wi.getCreated().getTime();
		long now = Calendar.getInstance().getTimeInMillis();
		long diff = now - creationDate;

		long days = diff / day;
		long hours = (diff - days * day) / hour;

		StringBuffer time = new StringBuffer();
		if (days > 0) {
			time.append(days).append(" days ");
		}

		if (hours > 0) {
			time.append(hours).append(" hours");
		}

		if (time.length() == 0) {
			time.append("less than 1 hour");
		}

		wi.setCustomField(field, time.toString());
	}

}
