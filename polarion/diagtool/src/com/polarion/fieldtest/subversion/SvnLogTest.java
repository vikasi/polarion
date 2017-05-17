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
package com.polarion.fieldtest.subversion;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.tigris.subversion.javahl.ChangePath;
import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Info;
import org.tigris.subversion.javahl.LogMessageCallback;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.RevisionRange;
import org.tigris.subversion.javahl.SVNClient;
import org.tigris.subversion.javahl.SVNClientInterface;
import org.tmatesoft.svn.core.SVNRevisionProperty;

import com.polarion.diagtool.internal.SimuThreads;
import com.polarion.fieldtest.PerformanceTestCase;

@SuppressWarnings("nls")
public class SvnLogTest extends PerformanceTestCase {

	private static final Logger log = Logger.getLogger(SvnLogTest.class);

	public void testLogWithNoThreads() throws Exception {
		testLogWithThreads(0, 0);
	}

	public void testLogWith1ThreadWith1SecDelay() throws Exception {
		testLogWithThreads(1, 1000);
	}

	public void testLogWith10ThreadsWith1SecDelay() throws Exception {
		testLogWithThreads(10, 1000);
	}

	public void testLogWith20ThreadsWith1SecDelay() throws Exception {
		testLogWithThreads(20, 1000);
	}

	public void testLogWith30ThreadsWith1SecDelay() throws Exception {
		testLogWithThreads(30, 1000);
	}

	public void testLogWith1ThreadWith10SecDelay() throws Exception {
		testLogWithThreads(1, 10000);
	}

	public void testLogWith10ThreadsWith10SecDelay() throws Exception {
		testLogWithThreads(10, 10000);
	}

	public void testLogWith20ThreadsWith10SecDelay() throws Exception {
		testLogWithThreads(20, 10000);
	}

	public void testLogWith30ThreadsWith10SecDelay() throws Exception {
		testLogWithThreads(30, 10000);
	}

	private void testLogWithThreads(int threadsCount, long requestDelay) throws Exception {
		if (threadsCount > 0 && !executeLoadTests()) {
			log.info("Load tests disabled - skipping test " + getName());
			return;
		}
		doTestLog();
		for (int i = 0; i < 10; i++) {
			SimuThreads simuThreads = new SimuThreads(threadsCount, new SvnWorker(getPolarionRepoURL()), requestDelay);
			try {
				simuThreads.start();
				startMeasuring();
				doTestLog();
				stopMeasuring();
			} finally {
				simuThreads.stop();
			}
			if (simuThreads.failure() != null) {
				Throwable failure = simuThreads.failure();
				if (failure instanceof Exception) {
					throw (Exception) failure;
				}
				if (failure instanceof Error) {
					throw (Error) failure;
				}
				throw new RuntimeException(failure);
			}
		}
	}

	private void doTestLog() throws ClientException {
		SVNClientInterface client = createSvnClient();
		client.logMessages(getPolarionRepoURL(), Revision.HEAD, new RevisionRange[] { new RevisionRange(Revision.HEAD, Revision.getInstance(0) ) }, true, false, false, new String[]{SVNRevisionProperty.LOG, SVNRevisionProperty.DATE, SVNRevisionProperty.AUTHOR}, getTestPropertyAsInt("svn.logLimit"), new LogMessageCallback() { //$NON-NLS-1$

			int num = 0;

			@Override
			public void singleMessage(ChangePath[] changedPaths, long revision, Map revprops, boolean hasChildre) {
				num++;
				if ((num % 1000) == 0) {
					log.info("Just processing revision " + revision); //$NON-NLS-1$
				}
			}
		});
	}

	private SVNClientInterface createSvnClient() {
		SVNClientInterface client = new SVNClient();
		client.username(getPolarionSystemUserName());
		client.password(getPolarionSystemUserPassword());
		return client;
	}

	private final class SvnWorker implements Callable<Void> {

		public SvnWorker(String rootURL) {
			this.rootURL = rootURL;
			random = new Random();
		}

		private final String rootURL;
		private final Random random;

		@SuppressWarnings("deprecation")
		@Override
		public Void call() throws Exception {
			SVNClientInterface client = createSvnClient();
			Info info = client.info(rootURL);
			long lastRevision = info.getRevision();
			long checkRevision = random.nextInt((int) lastRevision);
			client.list(rootURL, Revision.getInstance(checkRevision), false);
			return null;
		}

	}

}
