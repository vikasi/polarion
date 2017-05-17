/*
  * Copyright (C) 2004-2013 Polarion Software
  * All rights reserved.
  * Email: dev@polarion.com
  *
  *
  * Copyright (C) 2004-2013 Polarion Software
  * All Rights Reserved.  No use, copying or distribution of this
  * work may be made except in accordance with a valid license
  * agreement from Polarion Software.  This notice must be
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

class ProgressMonitor {

    int current = 0
    final int target
    final wheel = "-\\|/"
    int wheelIndex = -1

    ProgressMonitor(int target) {
        this.target = target
    }

    def start() {
        tick(0)
    }

    def tick(worked = 1) {
        if (worked > 0) {
            current += worked
        }
        if (current > target) {
            current = target
        }
        print createShowLine((current / target * 100).intValue(), turnWheel())
        print "\r"
    }

    def finish() {
        current = target
        println createShowLine(100, " ")
    }

    def createShowLine(int percent, String wheelState) {
        String.format("%3d%% [%s%s] %s", percent, "=" * (int) (percent / 2), "-" * (50 - (int) (percent / 2)), wheelState)
    }

    def turnWheel() {
        wheelIndex++
        if (wheelIndex >= wheel.length()) {
            wheelIndex = 0
        }
        wheel[wheelIndex]
    }
}
