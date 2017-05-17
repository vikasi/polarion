/*
 * $Id: RestartableInputStream.java 326060 2011-11-14 14:42:20Z ruzam $
 *
 * Copyright (C) 2004-2005 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2005 Polarion Software
 * All Rights Reserved. No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software. This notice must be
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

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link java.io.InputStream} whose reading may be restarted from the beginning.
 * 
 * @author <A HREF="mailto:dev@polarion.com">Stepan Roh</A>, Polarion Software
 * @version $Revision: 326060 $ $Date: 2011-11-14 15:42:20 +0100 (Po, 14 XI 2011) $
 *
 */
@SuppressWarnings("nls")
public class RestartableInputStream extends InputStream {

    private static final int INITIAL_BUF_SIZE = 1024;

    /**
     * Constructor, initial buffer size is determined automatically.
     * 
     * @param is overlayed input stream
     * @throws IllegalArgumentException if is is <code>null</code>
     */
    public RestartableInputStream(InputStream is) {
        this(is, INITIAL_BUF_SIZE);
    }

    /**
     * Constructor, which let's the client specify the initial buffer size
     * 
     * @param is
     * @param bufSize
     */
    public RestartableInputStream(InputStream is, int bufSize) {
        super();
        if (is == null) {
            throw new IllegalArgumentException("is is null");
        }
        this.is = is;
        buf = new byte[bufSize];
    }

    /** overlayed input stream */
    private final InputStream is;
    /** internal buffer */
    private byte[] buf;
    /** number of valid (read) bytes in buffer */
    private int bufValid = 0;
    /** current position */
    private int pos = 0;

    private boolean restartingDisabled = false;

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        if (pos >= bufValid) {

            if (restartingDisabled) {
                return is.read();
            }

            int c = is.read();
            if (c >= 0) {
                bufValid++;
                if (bufValid > buf.length) {
                    // Shift the buffer size by one (=multiply by 2)
                    // usage of shift instead of multiply is taken from ByteArrayOutputStream
                    byte[] newBuf = new byte[buf.length << 1];
                    System.arraycopy(buf, 0, newBuf, 0, buf.length);
                    buf = newBuf;
                }
                buf[bufValid - 1] = (byte) c;
                pos++;
            }
            return c;
        }
        // convert to range <0,255> (idea from ByteArrayInputStream)
        return (buf[pos++] & 0xff);
    }

    /**
     * Restart reading from the first byte.
     * <p>
     * @see #disableRestarting()
     * 
     * @throws IllegalStateException If {@link #disableRestarting()}
     * was called before.
     *
     */
    public void restart() {
        if (restartingDisabled) {
            throw new IllegalStateException("Restarts are no more possible after call to disableRestarting().");
        }

        pos = 0;
    }

    /**
     * Disables the restarting feature, which leads to improved performance
     * of reading and less memory consumption, since the stream doesn't
     * have to buffer all the data any more.
     * <p>
     * Subsequent calls to {@link #restart()} will lead to illegal state exception.
     *
     */
    public void disableRestarting() {
        restartingDisabled = true;
    }

    /**
     * @return <code>true</code> is the stream can be restarted, false otherwise.
     */
    public boolean isRestartingEnabled() {
        return !restartingDisabled;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException {
        is.close();
    }

}
