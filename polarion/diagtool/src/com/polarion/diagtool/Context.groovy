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
package com.polarion.diagtool

import org.apache.log4j.Logger

class Context {

    final Configuration configuration
    final File resultsDir
    final File resultsZip
    final Logger consoleLog
    final ProbeRecorder probeRecorder
    final boolean isWindows = System.getProperty("os.name").toLowerCase().contains('windows')
    final boolean isWindowsXP = System.getProperty("os.name").toLowerCase().contains('windows xp')
    int errors = 0
    int warnings = 0

    Context(Configuration configuration, File resultsDir, Logger consoleLog) {
        this.configuration = configuration
        this.resultsDir = resultsDir
        this.resultsZip = new File(resultsDir.parentFile, "diagtool_results_" + getLocalHostName() + "_" + resultsDir.name + ".zip")
        this.consoleLog = consoleLog
        this.probeRecorder = new ProbeRecorder(new File(resultsDir, "probes.properties"))
    }

    def getLocalHostName() {
        try {
            return InetAddress.localHost.hostName
        } catch (UnknownHostException e) {
            return "UNKNOWN_LOCAL_HOST_NAME"
        }
    }

    def ProgressMonitor newProgressMonitor(int ticks) {
        new ProgressMonitor(ticks)
    }
}
