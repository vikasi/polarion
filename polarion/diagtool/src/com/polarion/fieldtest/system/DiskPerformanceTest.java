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
package com.polarion.fieldtest.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import com.polarion.fieldtest.PerformanceTestCase;

/**
 * 
 */
public class DiskPerformanceTest extends PerformanceTestCase {

	private static final long SEED = 12345678;
	private static Random s_random = new Random(SEED);

	private File getTestFile() throws IOException {
		File f = File.createTempFile("DiskPerformanceTest", ".bin", getPolarionDataFolder()); //$NON-NLS-1$ //$NON-NLS-2$
		f.deleteOnExit();
		return f;
	}

	private void doTest(int runs, int size) throws Exception {
		byte[] written = new byte[size];
		for (int i = 0; i < 10; ++i) {
			s_random.nextBytes(written);
			startMeasuring();
			for (int run = 0; run < runs; run++) {
				readWriteFile(written);
			}
			stopMeasuring();
		}
	}

	private void readWriteFile(byte[] written) throws FileNotFoundException, IOException {
		File f = getTestFile();
		try {
			//			System.out.println("Overwriting file " + f); //$NON-NLS-1$
			OutputStream os = new FileOutputStream(f);
			try {
				os.write(written);
			} finally {
				os.close();
			}
			InputStream is = new FileInputStream(f);
			try {
				int c, pos = 0;
				while ((c = is.read()) != -1) {
					if (written[pos] != (byte)c) {
						fail("Error re-reading the previously written file"); //$NON-NLS-1$
					}
					pos++;
				}
			} finally {
				is.close();
			}
		} finally {
			//			System.out.println("Deleting file " + f); //$NON-NLS-1$
			f.delete();
		}
	}

	public void testSmall() throws Exception {
		doTest(1000, 1000);
	}

	public void testLarge() throws Exception {
		doTest(1, 1000000);
	}

}
