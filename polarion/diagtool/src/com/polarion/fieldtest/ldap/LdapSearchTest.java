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
package com.polarion.fieldtest.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.log4j.Logger;

import com.polarion.diagtool.internal.ILDAPClient;
import com.polarion.diagtool.internal.LDAPClient;
import com.polarion.diagtool.internal.SimuThreads;
import com.polarion.diagtool.internal.LDAPClient.Variable;
import com.polarion.fieldtest.PerformanceTestCase;

@SuppressWarnings("nls")
public class LdapSearchTest extends PerformanceTestCase {

	private static final Logger log = Logger.getLogger(LdapSearchTest.class);

	private ILDAPClient ldapClient;

	private String ldapHost;
	private String bindDN;
	private String bindPassword;
	private String usersDN;
	private String searchFilter;

	@Override
	protected void setUp() throws Exception {
		ldapHost = getTestProperty("ldap.host");
		bindDN = getTestProperty("ldap.bindDN");
		bindPassword = getTestProperty("ldap.bindPassword");
		usersDN = getTestProperty("ldap.usersDN");
		searchFilter = getTestProperty("ldap.searchFilter");

		ldapClient = new LDAPClient();
		Collection<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("java.naming.referral", "follow"));
		vars.add(new Variable("java.naming.batchsize", "0"));
		vars.add(new Variable("java.naming.security.authentication", "simple"));
		vars.add(new Variable("java.naming.ldap.derefAliases", "never"));
		vars.add(new Variable("java.naming.ldap.deleteRDN", "true"));
		vars.add(new Variable("java.naming.ldap.version", "3"));
		vars.add(new Variable("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory"));
		vars.add(new Variable("com.sun.jndi.ldap.connect.pool", "true"));
		((LDAPClient) ldapClient).setEnvironmentTemplate(vars);
	}

	public void testSearch() throws Exception {
		for (int i = 0; i < 10; i++) {
			startMeasuring();
			doSearch();
			stopMeasuring();
		}
	}

	protected void doSearch()  {
		try {
			for (int i = 0; i < 10; i++) {
				DirContext ctx = null;
				try {
					ctx = ldapClient.createContext(bindDN, bindPassword, ldapHost);
					ldapClient.search(ctx, usersDN, searchFilter, true);
				} finally {
					if (ctx != null) {
						ldapClient.closeContext(ctx);
					}
				}
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public void testSearchConcurrent1ThreadsWith10MillisDelay() throws Exception {
		testSearchConcurrent(1, 10);
	}

	public void testSearchConcurrent10ThreadsWith10MillisDelay() throws Exception {
		testSearchConcurrent(10, 10);
	}

	public void testSearchConcurrent20ThreadsWith10MillisDelay() throws Exception {
		testSearchConcurrent(20, 10);
	}

	public void testSearchConcurrent30ThreadsWith10MillisDelay() throws Exception {
		testSearchConcurrent(30, 10);
	}

	public void testSearchConcurrent1ThreadsWith30MillisDelay() throws Exception {
		testSearchConcurrent(1, 30);
	}

	public void testSearchConcurrent10ThreadsWith30MillisDelay() throws Exception {
		testSearchConcurrent(10, 30);
	}

	public void testSearchConcurrent20ThreadsWith30MillisDelay() throws Exception {
		testSearchConcurrent(20, 30);
	}

	public void testSearchConcurrent30ThreadsWith30MillisDelay() throws Exception {
		testSearchConcurrent(30, 30);
	}

	private void testSearchConcurrent(int threadsCount, long requestDelay) throws Exception {
		if (!executeLoadTests()) {
			log.info("Load tests disabled - skipping test " + getName());
			return;
		}
		doSearch();
		for (int i = 0; i < 10; i++) {
			SimuThreads simuThreads = new SimuThreads(threadsCount, new ConcurrentWorker(), requestDelay);
			try {
				simuThreads.start();
				startMeasuring();
				doSearch();
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

	private final class ConcurrentWorker implements Callable<Void> {

		@Override
		public Void call() {
			doSearch();
			return null;
		}

	}

}
