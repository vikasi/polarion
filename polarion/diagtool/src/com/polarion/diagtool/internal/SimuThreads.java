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
package com.polarion.diagtool.internal;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

/**
 * 
 */
public class SimuThreads {

	private static final Logger log = Logger.getLogger(SimuThreads.class);

	public SimuThreads(int threadCount, Callable<Void> action, long actionDelay) {
		threads = new SimuThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new SimuThread(action, actionDelay, "SimuThread-" + action.getClass().getName() + "-" + i); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static final class SimuThread extends Thread {

		public SimuThread(Callable<Void> action, long actionDelay, String name) {
			super(name);
			this.action = action;
			this.actionDelay = actionDelay;
			setDaemon(true);
		}

		private final Callable<Void> action;
		private final long actionDelay;
		private volatile boolean stopped = false;
		private volatile Throwable failure = null;

		@Override
		public void run() {
			while (!stopped) {
				try {
					action.call();
				} catch (Throwable e) {
					if (stopped) {
						// failure is probably due to the interruption
						return;
					}
					failure = e;
					log.error(e.getLocalizedMessage(), e);
				}
				try {
					Thread.sleep(actionDelay);
				} catch (InterruptedException e) {
					// ignored
				}
			}
		}

		public void requestStop() {
			stopped = true;
			interrupt();
		}

		public Throwable getFailure() {
			return failure;
		}

	}

	private final SimuThread[] threads;

	public void start() {
		for (SimuThread thread : threads) {
			thread.start();
		}
	}

	public void stop() {
		for (SimuThread thread : threads) {
			thread.requestStop();
		}
		for (SimuThread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// ignored
			}
		}
	}

	public Throwable failure() {
		for (SimuThread thread : threads) {
			if (thread.getFailure() != null) {
				return thread.getFailure();
			}
		}
		return null;
	}

}
