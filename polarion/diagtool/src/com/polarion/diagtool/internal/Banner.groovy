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


class Banner implements Writable {

    private final lines = []

    @Override
    public Writer writeTo(Writer w) throws IOException {
        int width = (lines.max { it.length() }).length() + 4
        w.write("=" * width + "\n")
        lines.each {
            w.write("| ")
            w.write(it)
            w.write(" " * (width - it.length() - 4))
            w.write(" |\n")
        }
        w.write("=" * width + "\n")
    }

    @Override
    public String toString() {
        def w = new StringWriter()
        this.writeTo(w)
        return w.toString()
    }

    public void addLine(line) {
        lines << String.valueOf(line)
    }

    public void leftShift(obj) {
        addLine(obj)
    }
}
