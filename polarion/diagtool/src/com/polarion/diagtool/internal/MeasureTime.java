/*
 * $Id: MeasureTime.java,v 1.4 2004/04/08 09:04:02 vlkm Exp $
 *
 * Copyright (C) 2000-2003 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2000-2003 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software  This notice must be
 * included on all copies, modifications and derivatives of this
 * work.
 *
 * Polarion Software MAKES NO REPRESENTATIONS OR WARRANTIES
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Polarion Software
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.polarion.diagtool.internal;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

/**
 * @author <A HREF="mailto:dev@polarion.com">Jiri Walek</A>, Polarion Software
 * @version $Revision: 1.4 $ $Date: 2004/04/08 09:04:02 $
 */
@SuppressWarnings("nls")
public class MeasureTime {

    private static final Logger log = Logger.getLogger(MeasureTime.class);

    private final long start;
    private final long altStart;

    public MeasureTime() {
        start = System.nanoTime();
        altStart = getAlternativeNanos();
    }

    public long getElapsedTime() {
        long elapsed = System.nanoTime() - start;
        long altElapsed = getAlternativeNanos() - altStart;
        // DPP-5235
        // use alternative counter if time went backwards or if it differs by more than 1s from alternative
        if ((elapsed < 0) || (Math.abs(elapsed - altElapsed) > 1000l * 1000l * 1000l)) {
            reportUnreliableCounter();
            return altElapsed;
        }
        return elapsed;
    }

    private static long getAlternativeNanos() {
        return System.currentTimeMillis() * 1000l * 1000l;
    }

    // for performance reasons we don't make this volatile and allow to report unreliability more than once
    private static boolean unreliabilityReported = false;

    private static void reportUnreliableCounter() {
        if (unreliabilityReported) {
            return;
        }
        unreliabilityReported = true;
        if (log.isInfoEnabled()) {
            log.info("System.nanoTime() is unreliable - error has been compensated");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return " [ TIME " + getSecondsString() + " s. ]";
    }

    public String getSecondsString() {
        return getSecondsString(getNumSeconds(getElapsedTime()));
    }

    public static double getNumSeconds(long nanos) {
        return nanos / (1000d * 1000d * 1000d);
    }

    private static final NumberFormat df = new ThreadSafeNumberFormatWrapper(new DecimalFormat("0.#########"));

    public static String getSecondsString(double seconds) {
        return df.format(seconds);
    }

}
