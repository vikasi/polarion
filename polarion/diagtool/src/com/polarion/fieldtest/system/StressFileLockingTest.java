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
package com.polarion.fieldtest.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.polarion.fieldtest.PerformanceTestCase;

public class StressFileLockingTest extends PerformanceTestCase {
    private static final Logger log = Logger.getLogger(StressFileLockingTest.class);
    private static final long DEFAULT_DELAY = 0;
    private static final int DEFAULT_TRIES = 1000;

    public void testLock() throws IOException, InterruptedException {
        File file = getFile();
        if (file != null) {
            startMeasuring();
            startLocking(file, getDelayProperty(), getTriesProperty());
            stopMeasuring();
        }
    }

    private void startLocking(File file, long delay, int tries) throws IOException, InterruptedException {
        log.info("Stress-testing locking of " + file.getCanonicalPath()); //$NON-NLS-1$
        long num = 0;
        long total = 0;
        while (true) {
            long time = stressIteration(file, delay);
            num++;
            total += time;
            tries--;
            if (tries <= 0) {
                break;
            }
        }
        log.info(num + " measurements: average=" + (total / (float) num) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private long stressIteration(File file, long delay) throws FileNotFoundException, IOException, InterruptedException {
        FileOutputStream stream = new FileOutputStream(file);
        long time = System.currentTimeMillis();
        stream.getChannel().lock();
        time = System.currentTimeMillis() - time;
        if (delay != 0) {
            Thread.sleep(delay);
        }
        stream.close();
        if (delay != 0) {
            Thread.sleep(delay);
        }
        return time;
    }

    private int getTriesProperty() {
        String tries = getTestProperty("fileLocking.tries"); //$NON-NLS-1$
        if (tries != null) {
            return Integer.parseInt(tries);
        }
        return DEFAULT_TRIES;
    }

    private long getDelayProperty() {
        String delay = getTestProperty("fileLocking.delay"); //$NON-NLS-1$
        if (delay != null) {
            return Long.parseLong(delay);
        }
        return DEFAULT_DELAY;
    }

    private File getFile() {
        String path = getTestProperty("fileLocking.path"); //$NON-NLS-1$
        if (path != null) {
            return new File(path);
        }
        File sharedFolder = new File(new File(getPolarionProperty("com.polarion.home")).getParentFile(), "shared"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!sharedFolder.exists()) {
            log.info("There is no shared data folder at location " + sharedFolder.getAbsolutePath() + ", probably because this is not clustered environment - skipping " + getName()); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }
        return new File(sharedFolder, "locking_test"); //$NON-NLS-1$

    }
}
