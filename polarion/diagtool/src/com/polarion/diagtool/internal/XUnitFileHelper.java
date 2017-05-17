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

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XUnitFileHelper {

	public static Map<String,String> readTimes(File xunitFile) {
		Map<String,String> times = new HashMap<String, String>();

		List<Element> testsuiteElements = new ArrayList<Element>();
		Document document = parseDocument(xunitFile);
		Element rootElement = document.getDocumentElement();
		if ("testsuite".equals(rootElement.getTagName())) { //$NON-NLS-1$
			testsuiteElements.add(rootElement);
		} else {
			testsuiteElements.addAll(DOMHelper.getContainedElements(rootElement, "testsuite")); //$NON-NLS-1$
		}

		for (Element suiteElement : testsuiteElements) {
			List<Element> testCaseElements = DOMHelper.getContainedElements(suiteElement, "testcase"); //$NON-NLS-1$
			for (Element testCaseElement : testCaseElements) {
				String classname = DOMHelper.getAttributeValue(testCaseElement, "classname"); //$NON-NLS-1$
				String name = DOMHelper.getAttributeValue(testCaseElement, "name"); //$NON-NLS-1$
				String time = DOMHelper.getAttributeValue(testCaseElement, "time"); //$NON-NLS-1$
				if (classname != null && name != null && time != null) {
					times.put(getKey(classname, name), time);
				}
			}
		}
		return times;
	}

	public static void setTimes(File xunitFile, Map<String,String> times) {

		List<Element> testsuiteElements = new ArrayList<Element>();
		Document document = parseDocument(xunitFile);
		Element rootElement = document.getDocumentElement();
		if ("testsuite".equals(rootElement.getTagName())) { //$NON-NLS-1$
			testsuiteElements.add(rootElement);
		} else {
			testsuiteElements.addAll(DOMHelper.getContainedElements(rootElement, "testsuite")); //$NON-NLS-1$
		}

		for (Element testsuiteElement : testsuiteElements) {
			double totalTime = 0;
			List<Element> testcaseElements = DOMHelper.getContainedElements(testsuiteElement, "testcase"); //$NON-NLS-1$
			for (Element testcaseElement : testcaseElements) {
				String classname = DOMHelper.getAttributeValue(testcaseElement, "classname"); //$NON-NLS-1$
				String name = DOMHelper.getAttributeValue(testcaseElement, "name"); //$NON-NLS-1$
				String key = getKey(classname, name);
				String time = null;
				if (times.containsKey(key)) {
					time = times.get(key);
					testcaseElement.setAttribute("time", time); //$NON-NLS-1$
				} else {
					throw new RuntimeException("Time not found for test: "+key); //$NON-NLS-1$
				}
				totalTime += readTime(time);
			}
			testsuiteElement.setAttribute("time", writeTime(totalTime)); //$NON-NLS-1$
		}
		writeDocument(xunitFile, document);
	}

	private static double readTime(String time) {
		return time != null ? Double.parseDouble(time) : 0;
	}

	private static DecimalFormat timeFormat = new DecimalFormat("#0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH)); //$NON-NLS-1$

	private static String writeTime(double time) {
		return timeFormat.format(time);
	}

	private static String getKey(String classname, String name) {
		return classname + "." + name; //$NON-NLS-1$
	}

	private static Document parseDocument(File xmlFile) {
		try {
			return DOMHelper.parseDocument(xmlFile);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse xml file: " + xmlFile.getAbsolutePath(), e); //$NON-NLS-1$
		}
	}


	private static void writeDocument(File xunitFile, Document document) {
		try {
			DOMHelper.writeDocument(document, xunitFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
