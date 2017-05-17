/*
 * Copyright (C) 2004-2007 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2007 Polarion Software
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
package com.polarion.fieldtest.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import com.polarion.diagtool.internal.Base64;
import com.polarion.fieldtest.PerformanceTestCase;

/**
 * Performance unit test for {@link Base64}.
 * <p>
 * Source: http://iharder.sourceforge.net/base64/Base64Test.java (in public domain)
 * 
 * @author <A HREF="mailto:dev@polarion.com">Stepan Roh</A>, Polarion Software
 * @version $Revision$ $Date$
 *
 */
public class Base64PerformanceTest extends PerformanceTestCase {

	private static final long SEED = 12345678;

	private static Random s_random = new Random(SEED);

	private byte[] createData(int length) throws Exception {
		byte[] bytes = new byte[length];
		s_random.nextBytes(bytes);
		return bytes;
	}

	private void runStreamTest(int length) throws Exception {
		byte[] data = createData(length);
		ByteArrayOutputStream out_bytes = new ByteArrayOutputStream();
		OutputStream out = new Base64.OutputStream(out_bytes);
		out.write(data);
		out.close();
		byte[] encoded = out_bytes.toByteArray();
		byte[] decoded = Base64.decode(encoded, 0, encoded.length);
		assertTrue(Arrays.equals(data, decoded));

		Base64.InputStream in = new Base64.InputStream(
				new ByteArrayInputStream(encoded));
		out_bytes = new ByteArrayOutputStream();
		byte[] buffer = new byte[3];
		for (int n = in.read(buffer); n > 0; n = in.read(buffer)) {
			out_bytes.write(buffer, 0, n);
		}
		out_bytes.close();
		in.close();
		decoded = out_bytes.toByteArray();
		assertTrue(Arrays.equals(data, decoded));
	}

	private void doTestStreams() throws Exception {
		for (int i = 0; i < 100; ++i) {
			runStreamTest(i);
		}
		for (int i = 100; i < 2000; i += 250) {
			runStreamTest(i);
		}
		for (int i = 2000; i < 80000; i += 1000) {
			runStreamTest(i);
		}
		for (int i = 80000; i < 1000000; i += 100000) {
			runStreamTest(i);
		}
		for (int i = 1000000; i < 10000000; i += 1000000) {
			runStreamTest(i);
		}
	}

	public void testStreams() throws Exception {
		for (int i = 0; i < 10; ++i) {
			startMeasuring();
			doTestStreams();
			stopMeasuring();
		}
	}

}
