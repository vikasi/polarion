/*
 * Copyright (C) 2004-2014 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2014 Polarion Software
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
package com.polarion.example.commithook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import com.polarion.alm.ws.client.WebServiceFactory;
import com.polarion.alm.ws.client.session.SessionWebService;
import com.polarion.alm.ws.client.tracker.TrackerWebService;
import com.polarion.alm.ws.client.types.tracker.EnumOptionId;
import com.polarion.alm.ws.client.types.tracker.WorkItem;

/**
 * Commit Hook is an application used in pre-commit script. It checks
 * revision comment of commit message to ensure, that the revision will be
 * linked to certain work item in Polarion, which represent the revision reason.
 * Message has to contain a link to work item in form: ID - title, and 
 * this work item has to be resolvable but not resolved. 
 * 
 * @author Michal Antolik
 */
public class PreCommitHook {

	private final String repos_path;
	private final String txs_number;
	private Properties prop = new Properties();

	// dSTH is default value which is used, when property no found in input file
	private final String dUSER = "admin";
	private final String dPASSWD = "admin";
	private final String dPOLARION_SERVER_ADR = "http://localhost";
	private final String dPOLARION_SERVER_PORT = "81";

	private String commitAuthor;
	private FileOutputStream file;

	private final String dSVNLOOK_DIR = "C:\\Polarion\\bundled\\svn\\bin\\";
	private final String dSVNLOOK_CMD = "svnlook.exe";

	private final String dAPACHE_LOG_FOLDER = "C:\\Polarion\\data\\logs\\apache\\";
	private final String dAPACHE_LOG_FILE_NAME = "commit_audit.log";
	List<String> wis_id = new ArrayList<String>(); // commit message lines

	private WebServiceFactory factory;

	private SessionWebService sessionService;
	private TrackerWebService trackerService;

	/**
	 * 
	 * @param repo
	 *            absolute path to repository (this is one of input parameter of
	 *            pre-commit script; e.g. "C:/Polarion/data/svn/repo")
	 * @param txsn
	 *            transaction number (this is one of input parameter of
	 *            pre-commit script; e.g. "773-1 ")
	 * @param settingsFileName
	 *            absolute file path of "settings.properties" (see example file
	 *            in project directory)
	 */
	public PreCommitHook(String repo, String txsn, String settingsFileName)
			throws Exception {
		super();
		this.repos_path = repo;
		this.txs_number = txsn;

		prop.load(new FileInputStream(settingsFileName));

		file = new FileOutputStream(prop.getProperty("apache_log_folder",
				dAPACHE_LOG_FOLDER)
				+ prop.getProperty("apache_log_file_name",
						dAPACHE_LOG_FILE_NAME), true);

		loadCommitAuthor();
		setUpConnection();
	}

	private void loadCommitAuthor() {
		StringBuffer buf = new StringBuffer();
		buf.append(prop.getProperty("svnlook_dir", dSVNLOOK_DIR)).append(
				prop.getProperty("svnlook_cmd", dSVNLOOK_CMD)).append(
				" author ").append(repos_path).append(" -t ")
				.append(txs_number);
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(buf.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			commitAuthor = reader.readLine();
			if (commitAuthor == null)
				commitAuthor = "Unknown";

			reader.close();
		} catch (IOException ioe) {
			logError("Retrieving author of commit failed.");
			commitAuthor = "Unknown";
		}
	}

	private void setUpConnection() throws MalformedURLException {
		StringBuffer buf = new StringBuffer();
		buf.append(
				prop.getProperty("polarion_server_adr", dPOLARION_SERVER_ADR))
				.append(":").append(
						prop.getProperty("polarion_server_port",
								dPOLARION_SERVER_PORT)).append(
						"/polarion/ws/services/");

		factory = new WebServiceFactory(buf.toString());
	}

	private void setUpSession() throws Exception {
		try {
			sessionService = factory.getSessionService();
			trackerService = factory.getTrackerService();

			sessionService.logIn(prop.getProperty("user", dUSER), prop
					.getProperty("passwd", dPASSWD));
		} catch (ServiceException se) {
			logError("WebServices not available");
			throw se;
		} catch (RemoteException re) {
			logError("Login unsuccessful");
			throw re;
		}
	}

	private List<String> getMessageLines() throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append(prop.getProperty("svnlook_dir", dSVNLOOK_DIR)).append(
				prop.getProperty("svnlook_cmd", dSVNLOOK_CMD)).append(" log ")
				.append(repos_path).append(" -t ").append(txs_number);
		Process proc = Runtime.getRuntime().exec(buf.toString());
		BufferedReader reader = new BufferedReader(new InputStreamReader(proc
				.getInputStream()));
		String line;
		List<String> l = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			l.add(line);
		}

		reader.close();

		return l;
	}

	/**
	 * Validate if the commit message contains WorkItem link, each link have to
	 * be on separate line, and should look like: ID-Title.
	 */
	private boolean checkMessage(List<String> lines) throws Exception {
		Iterator<String> it = lines.iterator();
		while (it.hasNext()) {
			parseLine(it.next().trim());
		}

		if (wis_id.isEmpty()) {
			String err = "Commit message has to contain a workItem, to which commit will be linked! Commit interrupted. ";
			logError(err);
			return false;
		}

		return true;
	}

	/**
	 * Regular expression to find workitem IDs in revision messages. It is
	 * assumed, that the workitem ID has the following formats at least one
	 * "allowed character" followed by dash and then followed by at least one
	 * "allowed character". Where "allowed character" is any not white space
	 * character except of (){}[]?!"':,
	 */
	private static final String ALLOWED_CHARS_EXPR = "[^(){}\\[\\]?!\"':\\,\\s]+"; //$NON-NLS-1$
	private static final Pattern SEARCH_WI_ID_PATTERN = Pattern
			.compile(ALLOWED_CHARS_EXPR + "-" + ALLOWED_CHARS_EXPR);

	private void parseLine(String line) {
		Matcher matcher = SEARCH_WI_ID_PATTERN.matcher(line);
		while (matcher.find()) {
			wis_id.add(matcher.group());
		}
	}

	/**
	 * The main method to start checking the commit message.
	 * 
	 * @throws Exception
	 *             throwing exception if fatal error occurred (commit should be
	 *             interrupted)
	 */
	public boolean check() throws Exception {

		try {
			System.out.println("Setting up Session");
			setUpSession(); // throwing exception if connection to webServices
			// was unsuccessful

			List<String> list = getMessageLines();

			if (list.isEmpty()) {
				logError("Commit message has to contain a workItem, to which commit will be linked! Commit interrupted.");
				return false;
			}

			if (list.get(0).startsWith("Polarion commit")) { // if this is
				// internal
				// commit
				// performed by
				// Polarion, do
				// not continue
				// checking for
				// linked items
				return true;
			}

			System.out.println("checking message");
			if (!checkMessage(list)) {
				// return false if commit message doesn't content a WorkItem ID
				// with title
				System.err.println("please add a valid work item ID");
				return false;
			}

			System.out.println("checking for unresolved links");
			if (!checkUnresolvedLinks()) {
				// return false when error in connection with Polarion occurred
				return false;
			}

			System.out.println("passed");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (sessionService != null) {
				sessionService.endSession();
			}
			file.close();
		}

		return true;
	}

	private String getLinkedWIsDirectQuery() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> iter = wis_id.iterator(); iter.hasNext();) {
			String id = (String) iter.next();
			String[] parts = id.split("/");
			String projectId = null;
			String workitemId = null;
			if (parts.length == 2) {
				// we have project part and id part
				// but the project part may be empty (for messages containing
				// '/PREFIX-nnn')
				projectId = (parts[0].length() > 0) ? parts[0] : null;
				workitemId = parts[1];
			} else {
				// we have only id part
				workitemId = parts[0];
			}
			if (projectId != null) {
				sb.append("(project.id:");
				sb.append(projectId);
				sb.append(" AND id:");
				sb.append(workitemId);
				sb.append(")");
			} else {
				sb.append("id:");
				sb.append(workitemId);
			}
			if (iter.hasNext()) {
				sb.append(" OR ");
			}
		}
		String query = sb.toString();
		return query;
	}

	/**
	 * validate if the linked item exists (is resolvable)
	 * 
	 * @throws Exception
	 */
	private boolean checkUnresolvedLinks() throws RemoteException {
		String query = getLinkedWIsDirectQuery();
		String[] fields = { "resolution", "id" };

		WorkItem[] linkedWIs = trackerService.queryWorkItems(query, null,
				fields);

		if ((linkedWIs == null) || (linkedWIs.length == 0)) {
			String err = "No valid IDs of WorkItem found.";
			logError(err);
			return false;
		}

		boolean res = false;

		for (int i = 0; i < linkedWIs.length; i++) {
			WorkItem wi = linkedWIs[i];

			if (wi.isUnresolvable()) {
				String err = "ID of WorkItem in commit message is not valid";
				logError(err);
				res = false;
				break;
			}

			// check if WI has unresolved state
			EnumOptionId resId = wi.getResolution();
			if (resId != null) {
				String err = "WI is already resolved and resolution is set.";
				logError(err, wi.getId());
				res = false;
				break;
			}

			// everything is OK
			res = true;
		}

		return res;
	}

	private void logError(String msg) {
		log("ERROR: " + msg);
	}

	private void logError(String msg, String WI_id) {
		StringBuffer buf = new StringBuffer();
		buf.append("ERROR: ").append(msg).append(" {workItemID=").append(WI_id)
				.append(", transaction_number=").append(txs_number).append(
						", author=").append(commitAuthor).append("}");
		log(buf.toString());
	}

	private void log(String msg) {
		System.err.println(msg);
		StringBuffer buf = new StringBuffer();
		buf.append("[").append(Calendar.getInstance().getTime()).append("] - ")
				.append(msg).append("\n");

		try {
			file.write(buf.toString().getBytes());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.exit(1);
			return;
		}

		try {
			PreCommitHook hook = new PreCommitHook(args[0], args[1], args[2]);
			if (!hook.check())
				System.exit(1);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}
