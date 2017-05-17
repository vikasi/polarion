/*
 * $Id: RestartableReader.java 326060 2011-11-14 14:42:20Z ruzam $
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
import java.io.Reader;

/**
 * {@link java.io.Reader} whose reading may be restarted from the beginning.
 * 
 * @author <A HREF="mailto:dev@polarion.com">Stepan Roh</A>, Polarion Software
 * @version $Revision: 326060 $ $Date: 2011-11-14 15:42:20 +0100 (Po, 14 XI 2011) $
 *
 */
@SuppressWarnings("nls")
public class RestartableReader extends Reader {

    private static final int INITIAL_BUF_SIZE = 1024;

    /**
     * Constructor.
     * 
     * @param r overlayed reader
     * @throws IllegalArgumentException if r is <code>null</code>
     */
    public RestartableReader(Reader r) {
        this(r, INITIAL_BUF_SIZE);
    }

    /**
     * Constructor, which let's the client specify the initial rewinding buffer size.
     * @param r
     * @param bufSize
     */
    public RestartableReader(Reader r, int bufSize) {
        super();
        if (r == null) {
            throw new IllegalArgumentException("r is null");
        }
        this.r = r;
        buf = new char[bufSize];
    }

    /** overlayed reader */
    private final Reader r;
    /** internal buffer */
    private char[] buf;
    /** number of valid (read) chars in buffer */
    private int bufValid = 0;
    /** current position */
    private int pos = 0;

    private boolean restartingDisabled = false;

    private int readInternal() throws IOException {
        if (pos >= bufValid) {

            if (restartingDisabled) {
                return r.read();
            }

            int c = r.read();
            if (c >= 0) {
                bufValid++;
                if (bufValid > buf.length) {
                    // Shift the buffer size by one (=multiply by 2)
                    // usage of shift instead of multiply is taken from ByteArrayOutputStream
                    char[] newBuf = new char[buf.length << 1];
                    System.arraycopy(buf, 0, newBuf, 0, buf.length);
                    buf = newBuf;
                }
                buf[bufValid - 1] = (char) c;
                pos++;
            }
            return c;
        }
        return buf[pos++];
    }

    /**
     * Restart reading from first byte.
     * <p>
     * @see #disableRestarting()
     * 
     * @throws IllegalStateException If {@link #disableRestarting()}
     * was called before.
     */
    public void restart() {
        if (restartingDisabled) {
            throw new IllegalStateException("Restarts are no more possible after call to disableRestarting().");
        }

        pos = 0;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException {
        r.close();
    }

    /* (non-Javadoc)
     * @see java.io.Reader#read(char[], int, int)
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        // modelled after InputStream.read(byte[], int, int)
        if (cbuf == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = readInternal();
        if (c == -1) {
            return -1;
        }
        cbuf[off] = (char) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = readInternal();
                if (c == -1) {
                    break;
                }
                cbuf[off + i] = (char) c;
            }
        } catch (IOException ee) {
            // ignored
        }
        return i;
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

}
