/*
 * Copyright (C) 2004-2012 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2012 Polarion Software
 * All Rights Reserved. No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software. This notice must be
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
package com.polarion.diagtool.internal;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;

public class Log4jAntLogger extends DefaultLogger {

    private final Logger log;
    public volatile int errorCounter;
    public volatile int warnCounter;

    public Log4jAntLogger(Logger log) {
        super();
        this.log = log;
        setEmacsMode(true);
    }

    @Override
    protected void printMessage(String message, PrintStream stream, int priority) {
        if (priority == Project.MSG_ERR) {
            log.error(message);
            errorCounter++;
        } else if (priority == Project.MSG_WARN) {
            log.warn(message);
            warnCounter++;
        } else {
            log.info(message);
        }
    }

}
