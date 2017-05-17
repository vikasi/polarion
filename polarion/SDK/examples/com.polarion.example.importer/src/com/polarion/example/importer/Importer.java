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
package com.polarion.example.importer;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.rpc.ServiceException;

import com.polarion.alm.ws.client.Utils;
import com.polarion.alm.ws.client.WebServiceFactory;
import com.polarion.alm.ws.client.projects.ProjectWebService;
import com.polarion.alm.ws.client.session.SessionWebService;
import com.polarion.alm.ws.client.tracker.TrackerWebService;
import com.polarion.alm.ws.client.types.Text;
import com.polarion.alm.ws.client.types.projects.Project;
import com.polarion.alm.ws.client.types.tracker.Category;
import com.polarion.alm.ws.client.types.tracker.EnumOptionId;
import com.polarion.alm.ws.client.types.tracker.Module;
import com.polarion.alm.ws.client.types.tracker.WorkItem;

/**
 * The importer class provides a stand alone application to import POLARION work
 * items using Web Service API. Format of input data are in <em>CSV</em> file 
 * format in consequent order:
 * - title
 * - one line description
 * - severity (case insensitive, when not found 
 * in POLARION then new severity for just this WI will be defined)
 * - categories (there
 * can be more categories separated by comma. Category can be written as category id 
 * or category name defined in POLARION, or in other case, category for just this WI 
 * will be defined)
 * 
 * Example of data:
 * in POLARION are defines:
 * - severity Major(id=major, name=Major)
 * - categories Defect(id=defect, name=Defect), Test Suite(id=test-suite,
 * name=Test Suite)
 *
 * so the text source can be:
 * Filter also administration topics by Hats;description1;Major;Administration
 * Watch the available disk space;description2;Major;Notifications,defect,Test Suite
 * 
 * 
 * The very important part of this application are properties saved in file
 * "settings.properties" (format is: key=value)
 * example:
 * - polarion_server_address=http://localhost
 * - polarion_server_port=81
 * - user=admin
 * - passwd=admin
 * - project_id=playground
 * - item_delimiter=;
 * 
 * @author Michal Antolik
 */
public class Importer {

	private static final int FIELD_TITLE = 1;
	private static final int FIELD_DESCRIPTION = 2;
	private static final int FIELD_SEVERITY = 3;
	private static final int FIELD_CATEGORY = 4;

	private Properties prop = new Properties();
	private int lineNum;
	private EnumOptionId type;
	LinkedList<String> categories = null;
	private WebServiceFactory factory;

	private SessionWebService sessionService;
	private TrackerWebService trackerService;
	private ProjectWebService projectService;

	private Project project;
	private Module module;
	private HashMap<String, Category> categoriesMap = new HashMap<String, Category>();

	public Importer() {
		super();
	}

	/**
	 * 
	 * @throws MalformedURLException
	 *             If the string defined in properties specifies an unknown
	 *             protocol.
	 */
	private void setUpPolarionAddress() throws MalformedURLException {
		StringBuffer buf = new StringBuffer();
		buf.append(prop.getProperty("polarion_server_address")).append(":")
				.append(prop.getProperty("polarion_server_port")).append(
						"/polarion/ws/services/");

		factory = new WebServiceFactory(buf.toString());
	}

	/**
	 * 
	 * @throws ServiceException
	 *             If services are not accessable
	 */
	private void setUpPolarionWebServices() throws ServiceException {
		sessionService = factory.getSessionService();
		trackerService = factory.getTrackerService();
		projectService = factory.getProjectService();
	}

	/**
	 * This is the main method of {@link Importer} which will import work items.
	 * First, the input properties from file {@code settings.properties} will be
	 * loaded and then checked if there were set properly. To do the import, it
	 * will log in to POLARION and then import all work items from the input
	 * file.
	 * 
	 * @param file
	 *            name of the input data
	 * @return true, if import of work items was successful
	 * @throws Exception
	 */
	public boolean importWIs(String file) throws Exception {
		// prepare to import
		try {
			prop.load(new FileInputStream("settings.properties")); // load
																	// properties

			setUpPolarionAddress();

			setUpPolarionWebServices();

			sessionService.logIn(prop.getProperty("user"), prop
					.getProperty("passwd"));

		} catch (MalformedURLException me) {
			printError("Provided URL is malformed - protocol unknown.");
			throw me;
		} catch (ServiceException se) {
			printError("Unreachable web services at Polarion server.");
			throw se;
		} catch (RemoteException re) {
			printWarning("Log in unsuccessful");
			throw re;
		} catch (IOException ioe) {
			printError("Properties not loaded.");
			throw ioe;
		}

		// start importing
		try {
			String projectId = prop.getProperty("project_id");
			project = projectService.getProject(projectId);
			if (project.isUnresolvable()) {
				printError("Project not found: "+projectId);
				return false;
			}
			
			String moduleName = prop.getProperty("module");
			if (moduleName != null) {
			    String moduleLocation = Utils.encodeRelativeLocation(moduleName, null);
			    module = trackerService.getModuleByLocation(project.getId(), moduleLocation);
			    if ((module == null) || module.isUnresolvable()) {
	                printError("Module not found: " + moduleName);
	                return false;
			    }
			}

			Category[] prjCats = getProjectCategoriesIDs();
			// on the input there could be also category name, not just ID
			if (prjCats != null) {
				for (int i = 0, maxi = prjCats.length; i < maxi; i++) {
					categoriesMap.put(prjCats[i].getName(), prjCats[i]);
				}
			}

			// set the work item type
			type = new EnumOptionId(prop.getProperty("wi_type", "requirement"));

			LineNumberReader reader = new LineNumberReader(new FileReader(file));
			String line;
			sessionService.beginTransaction(); // to do just one TX

			while ((line = reader.readLine()) != null) {
				lineNum = reader.getLineNumber();
				WorkItem wi = getWorkItemFromLine(line, prop.getProperty(
						"item_delimiter", ";"));
				String wiURI = trackerService.createWorkItem(wi);

				// set categories
				Iterator<String> it = categories.iterator();
				while (it.hasNext())
					trackerService.addCategory(wiURI, it.next());
			}

			sessionService.endTransaction(false);

			sessionService.endSession();
		} catch (RemoteException re) {
			printError("Error occured during the execution of a remote method call.");
			throw re;
		} catch (IOException ioe) {
			printError("Error occured during parsing input file.");
			throw ioe;
		}

		return true;
	}

	private Category[] getProjectCategoriesIDs() throws RemoteException {
		return trackerService.getCategories(prop.getProperty("project_id"));
	}

	private WorkItem getWorkItemFromLine(String line, String delimiter) {
		String title = "";
		Text desc = new Text("text/plain", "", false);
		String cats = null;
		String severity = "";

		StringTokenizer tok = new StringTokenizer(line, delimiter);
		int i = 1;
		while (tok.hasMoreTokens()) {
			switch (i) {
			case FIELD_TITLE:
				title = tok.nextToken();
				i++;
				break;
			case FIELD_DESCRIPTION:
				desc.setContent(tok.nextToken());
				i++;
				break;
			case FIELD_SEVERITY:
				severity = tok.nextToken();
				i++;
				break;
			case FIELD_CATEGORY:
				cats = tok.nextToken();
				i++;
				break;
			default: /* ignore more delimiters */
			}
		}

		WorkItem wi = new WorkItem();
		wi.setTitle(title);
		wi.setDescription(desc);
		wi.setSeverity(new EnumOptionId(severity.toLowerCase()));
		wi.setProject(project);
		if (module != null) {
		    wi.setModuleURI(module.getUri());
		}
		wi.setType(type);

		categories = new LinkedList<String>();
		if (cats != null)
			parseCategories(cats);

		return wi;
	}

	private void parseCategories(String cats) {
		StringTokenizer tok = new StringTokenizer(cats, ",");

		while (tok.hasMoreTokens()) {
			String cat = tok.nextToken();

			// INFO Maybe cat from input data is the name of category which is
			// already in POLARION
			if (categoriesMap.containsKey(cat))
				cat = categoriesMap.get(cat).getId();

			categories.add(cat);
		}
	}

	public int countParsedLines() {
		return lineNum;
	}

	private void printError(String msg) {
		System.err.println("ERROR: " + msg);
	}

	private void printWarning(String msg) {
		System.err.println("WARNING: " + msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp();
			return;
		}

		Importer importer = new Importer();
		try {
			if (importer.importWIs(args[0]))
				System.out.println("RESULT: operation successful - "
						+ importer.countParsedLines() + " lines were parsed");
			else
				System.err.println("RESULT: operation unsuccessful");
		} catch (Exception e) {
			System.err.println("RESULT: operation unsuccessful");
			e.printStackTrace();
		}

	}

	private static void printHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("USAGE: ImportWorkItems <csv_file>\n\n").append(
				"Format of csv file:\n").append(
				"TITLE;ONE_LINE_DESCRIPTION;SEVERITY;CATEGORY");

		System.out.print(buffer.toString());
	}

}
