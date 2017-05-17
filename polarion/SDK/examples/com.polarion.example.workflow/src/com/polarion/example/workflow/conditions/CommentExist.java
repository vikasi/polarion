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
package com.polarion.example.workflow.conditions;

import com.polarion.alm.tracker.model.IWorkItem;
import com.polarion.alm.tracker.workflow.IArguments;
import com.polarion.alm.tracker.workflow.ICallContext;
import com.polarion.alm.tracker.workflow.ICondition;

/**
 * Each implementation of workflow condition has to implement ICondition class.
 * This condition will simply count number of comments. The workflow condition
 * will be successful if the method 'passesCondition' will return true.
 * 
 * @return true if WI has at least 1 comment, or false otherwise
 * 
 * @author Michal Antolik
 */
public class CommentExist implements ICondition {

	public boolean passesCondition(ICallContext context, IArguments arguments) {
		IWorkItem wi = context.getWorkItem();
		return !wi.getComments().isEmpty();
	}

}
