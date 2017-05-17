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
package com.polarion.diagtool.internal;

import java.io.File;

import org.apache.log4j.FileAppender;

public class ResultsFileAppender extends FileAppender {

    public ResultsFileAppender() {
        super();
    }

    public static File resultsDir;

    @Override
    public void activateOptions() {
        fileName = new File(resultsDir, fileName).getAbsolutePath();
        super.activateOptions();
    }

}
