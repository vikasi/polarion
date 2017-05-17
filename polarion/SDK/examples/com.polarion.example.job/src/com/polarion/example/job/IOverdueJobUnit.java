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
package com.polarion.example.job;

import com.polarion.platform.jobs.IJobUnit;

/**
 * @author Polarion Software
 */
public interface IOverdueJobUnit extends IJobUnit {

    static final String JOB_NAME = "overdue.job";
    
    void setQuery(String query);
    void setSort(String sort);
    void setNotificationSubjectPrefix(String notificationSubjectPrefix);
    void setNotificationSender(String notificationSender);
    void setNotificationRecipients(String notificationRecipients);
    void setAllowedDelay(Integer allowedDelay);
    
}
