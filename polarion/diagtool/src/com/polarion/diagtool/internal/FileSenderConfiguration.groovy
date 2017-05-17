/*
 * Copyright (C) 2004-2013 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2013 Polarion Software
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
package com.polarion.diagtool.internal

import org.apache.log4j.Logger

class FileSenderConfiguration {
    String mailSubject
    String mailSender
    long mailAttachmentThreshold
    String mailRecipient
    String mailProtocol
    String mailHost
    int mailPort = -1
    boolean mailAuth = false
    String mailUser
    String mailPassword
    Properties mailProtocolSettings
    String ftpHost
    String ftpUser
    String ftpPassword

    public boolean isValid(Logger log) {
        def valid = isValidProperty(log, "mail.subject", mailSubject)
        valid &= isValidProperty(log, "mail.sender", mailSender)
        valid &= isValidProperty(log, "mail.recipient", mailRecipient)
        valid &= isValidProperty(log, "mail.protocol", mailProtocol)
        valid &= isValidProperty(log, "mail.host", mailHost)
        if (mailAuth) {
            valid &= isValidProperty(log, "mail.user", mailUser)
            valid &= isValidProperty(log, "mail.password", mailPassword)
        }
        valid &= isValidProperty(log, "ftp.host", ftpHost)
        valid &= isValidProperty(log, "ftp.user", ftpUser)
        valid &= isValidProperty(log, "ftp.password", ftpPassword)
        valid
    }

    private boolean isValidProperty(Logger log, String name, String value) {
        if ((value == null) || value.isEmpty()) {
            log.error("Required configuration property ${name} is empty")
            return false
        }
        return true
    }
}
