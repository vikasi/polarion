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

import java.io.IOException;
import java.io.Writer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@SuppressWarnings("nls")
public class XUnitXmlBuilder {
	private final XMLStreamWriter xml;
	private final Writer writer;

	public XUnitXmlBuilder(Writer writer) {
		this.writer = writer;

		try {
			xml = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		} catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		try {
			xml.writeStartDocument("UTF-8", "1.0");
			writeNewLine();
			xml.writeStartElement("testsuites");
			writeNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	public void end() {
		try {
			xml.writeEndElement();
			xml.writeEndDocument();
			writer.close();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeNewLine() {
		try {
			xml.writeCharacters("\r\n"); //$NON-NLS-1$
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeEndElementAndNewLine() throws XMLStreamException {
		xml.writeEndElement();
		writeNewLine();
	}

	public void startSuite(String name) {
		try {
			xml.writeStartElement("testsuite");
			xml.writeAttribute("name", name);
			writeNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	public void endSuite() {
		try {
			writeEndElementAndNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	public void passed(String classname, String name, long time) {
		try {
			xml.writeEmptyElement("testcase");
			writeTestCaseAttributes(classname, name, time);
			writeNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeTestCaseAttributes(String classname, String name, long time) throws XMLStreamException {
		xml.writeAttribute("classname", classname);
		xml.writeAttribute("name", name);
		xml.writeAttribute("time", formatTime(time));
	}

	private String formatTime(long time) {
		long seconds = time / 1000;
		long milisecons = time % 1000;
		String milisecondsString = "" + milisecons;
		milisecondsString = "000".substring(milisecondsString.length()) + milisecondsString;
		return seconds + "." + milisecondsString;
	}

	public void failed(String classname, String name, long time, String message, String type, String content) {
		try {
			xml.writeStartElement("testcase");
			writeTestCaseAttributes(classname, name, time);
			writeNewLine();

			xml.writeStartElement("failure");
			writeFailureAttributes(message, type);
			xml.writeCharacters(content);
			writeEndElementAndNewLine();

			writeEndElementAndNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	public void error(String classname, String name, long time, String message, String type, String content) {
		try {
			xml.writeStartElement("testcase");
			writeTestCaseAttributes(classname, name, time);
			writeNewLine();

			xml.writeStartElement("error");
			writeFailureAttributes(message, type);
			xml.writeCharacters(content);
			writeEndElementAndNewLine();

			writeEndElementAndNewLine();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeFailureAttributes(String message, String type) throws XMLStreamException {
		if (message != null) {
			xml.writeAttribute("message", message);
		}
		if (type != null) {
			xml.writeAttribute("type", type);
		}
	}

}
