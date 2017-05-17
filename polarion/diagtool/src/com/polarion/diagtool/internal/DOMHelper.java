/*
 * $Id: DOMHelper.java,v 1.3 2004/11/11 21:07:36 kratochvilr Exp $
 *
 * Copyright (C) 2000-2003 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2000-2003 Polarion Software
 * All Rights Reserved.  No use, copying or distribution of this
 * work may be made except in accordance with a valid license
 * agreement from Polarion Software  This notice must be
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class offers various helper methods for simpler manipulation
 * with XML and DOM.<br>
 * 
 * @author  <A HREF="mailto:dev@polarion.com">Stepan Roh</A>, Polarion Software
 * @version $Revision: 1.3 $
 */
@SuppressWarnings("nls")
public class DOMHelper {

	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"; //$NON-NLS-1$

	private static final String ELEM_PROPERTY = "property";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_VALUE = "value";

	public static class ElementIterator implements Iterator<Element> {

		private final Iterator<Element> it;

		public ElementIterator(Node parent) {
			this(parent.getChildNodes());
		}

		public ElementIterator(NodeList nl) {
			super();
			int len = nl.getLength();
			List<Element> list = new ArrayList<Element>(len);
			for (int i = 0; i < len; i++) {
				Node n = nl.item(i);
				if (Node.ELEMENT_NODE == n.getNodeType()) {
					list.add((Element) n);
				}
			}
			it = list.iterator();
		}

		/* @see java.util.Iterator#hasNext() */
		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		/* @see java.util.Iterator#next() */
		@Override
		public Element next() {
			return it.next();
		}

		/* @see java.util.Iterator#remove() */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Constructor for DOMHelper.
	 */
	private DOMHelper() {
		super();
	}

	/**
	 * Returns all the text contained directly inside given node
	 * concatenated.
	 * 
	 * @param node DOM node
	 */
	public static String getContainedText(Node node) {
		return getContainedText(node, false);
	}

	public static String getContainedText(Node node, boolean useNodeToString) {
		node = node.getFirstChild();
		StringBuffer buf = new StringBuffer();
		while (node != null) {
			if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
				buf.append(useNodeToString ? ((CDATASection) node).getNodeValue() : ((CharacterData) node).getData());
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				buf.append(useNodeToString ? nodeToString(node) : ((CharacterData) node).getData());
			} else if (useNodeToString) {
				buf.append(nodeToString(node));
			}
			node = node.getNextSibling();
		}
		return buf.toString();
	}

	/**
	 * Returns attribute value or <code>null</code> if attribute is not defined.
	 * 
	 * @param elem DOM element
	 * @param name attribute name
	 * @return attribute value or <code>null</code>
	 */
	public static String getAttributeValue(Element elem, String name) {
		return getAttributeValue(elem, name, null);
	}

	/**
	 * Returns trimmed attribute value or <code>null</code> if attribute is not defined OR attribute is empty string.
	 * 
	 * @param elem DOM element
	 * @param name attribute name
	 * @return attribute value or <code>null</code>
	 */
	public static String getTrimmedAttributeValue(Element elem, String name) {
		String result = getAttributeValue(elem, name, null);
		if (result != null && result.trim().length() > 0) {
			return result.trim();
		}
		return null;
	}

	/**
	 * Returns attribute value or default if attribute is not defined.
	 * 
	 * @param elem DOM element
	 * @param name attribute name
	 * @param def default value
	 * @return attribute value or <code>def</code>
	 */
	public static String getAttributeValue(Element elem, String name, String def) {
		if (!elem.hasAttribute(name)) {
			return def;
		}
		return elem.getAttribute(name);
	}

	/**
	 * Goes through the element and processes all it's immediate children
	 * in form
	 * <pre>
	 * 
	 *      &lt;property name="xxx" value="xxx"/&gt;
	 * 
	 * </pre>
	 * into java <code>Properties</code> object.
	 * @param element
	 */
	public static Properties getProperties(Element element) {
		Properties props = new Properties();

		Node n = element.getFirstChild();
		while (n != null) {
			if ((n.getNodeType() == Node.ELEMENT_NODE) && n.getNodeName().equals(ELEM_PROPERTY)) {
				Element e = (Element) n;

				String name = e.getAttribute(ATTR_NAME);
				if (name == null) {
					throw new IllegalArgumentException("Required attribute " + ATTR_NAME + " is missing");
				}

				String value = e.getAttribute(ATTR_VALUE);

				props.setProperty(name, value);
			}
			n = n.getNextSibling();
		}

		return props;
	}

	/**
	 * Returns element with given name directly contained in given node
	 * or <code>null</code>.
	 * 
	 * @param name searched element name
	 */
	public static Element getContainedElement(Node node, String name) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				String tagName = e.getTagName();
				if (tagName != null && tagName.equals(name)) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Returns elements with given name directly contained in given node
	 * or empty collection.
	 * 
	 * @param name searched element name
	 */
	public static List<Element> getContainedElements(Node node, String name) {
		List<Element> ret = new ArrayList<Element>();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if ((name == null) || e.getTagName().equals(name)) {
					ret.add(e);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns element with given name (from name space defined in nameSpaceURI) directly contained in given node
	 * or <code>null</code>.
	 * 
	 * @param name searched element name
	 */
	public static Element getContainedElementNS(Node node, String nameSpaceURI, String name) {
		List<Element> elements = getContainedElements(node);
		for (Element element : elements) {
			String prefix = getNameSpacePrefix(element, nameSpaceURI);
			if (prefix != null && element.getNodeName().equals(prefix + ":" + name)) {
				return element;
			}
		}

		return null;
	}

	/**
	 * Returns elements with given name (from name space defined in nameSpaceURI) directly contained in given node
	 * or empty collection.
	 * 
	 * @param name searched element name
	 */
	public static List<Element> getContainedElementsNS(Node node, String nameSpaceURI, String name) {
		List<Element> result = new ArrayList<Element>();
		List<Element> elements = getContainedElements(node);
		for (Element element : elements) {
			String prefix = getNameSpacePrefix(element, nameSpaceURI);
			if (prefix != null && element.getNodeName().equals(prefix + ":" + name)) {
				result.add(element);
			}
		}

		return result;
	}

	/**
	 * Searches for attribute xmlns:X="nameSpaceURI" in current node and parents and returns X.
	 * @param node
	 * @param nameSpaceURI
	 */
	public static String getNameSpacePrefix(Node node, String nameSpaceURI) {
		do {
			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {
				Node a = attr.item(i);
				String aname = a.getNodeName();
				if (aname.startsWith("xmlns") && nameSpaceURI.equals(a.getNodeValue())) {
					if ("xmlns".equals(aname)) {
						return "";
					}
					return aname.substring(6); // what is after xmlns:
				}
			}
			node = node.getParentNode();
		} while (node != null);

		return null;
	}

	/**
	 * Returns all noted of type ELEMENT
	 * directly contained in given node or empty collection.
	 * 
	 */
	public static List<Element> getContainedElements(Node node) {
		return getContainedElements(node, null);
	}

	/**
	 * Return element's tag name (without namespace).
	 * 
	 * @param e element
	 * @return tagname
	 */
	public static String getTagName(Element e) {
		String name = e.getLocalName();
		if (name == null) {
			name = e.getNodeName();
		}
		return name;
	}

	/**
	 * Return attribute's name (without namespace).
	 * 
	 * @param a attribute
	 * @return tagname
	 */
	public static String getAttrName(Attr a) {
		String name = a.getLocalName();
		if (name == null) {
			name = a.getNodeName();
		}
		return name;
	}

	/** shared factory */
	private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * Returns DOM document parsed from given {@link File}.
	 * 
	 * @param file file
	 */
	public static Document parseDocument(File file) throws IOException, ParserConfigurationException, SAXException {
		return parseDocument(new FileInputStream(file), null);
	}

	/**
	 * Byte Order Mark (BOM) as char.
	 * <p>
	 * <a href="http://www.unicode.org/faq/utf_bom.html#BOM">Byte Order Mark (BOM) FAQ</a>
	 */
	public static final char BOM_CHAR = 0xfeff;

	/**
	 * Returns DOM document parsed from given {@link Reader}
	 * (which will be automatically closed!).
	 * 
	 * @param r reader
	 * @return parsed {@link Document} (not <code>null</code>)
	 * @throws IOException if such exception occurs
	 * @throws ParserConfigurationException if such exception occurs
	 * @throws SAXException if such exception occurs
	 */
	public static Document parseDocument(Reader r) throws IOException, ParserConfigurationException, SAXException {
		return parseDocument(r, null);
	}

	/**
	 * Returns DOM document parsed from given {@link Reader}
	 * (which will be automatically closed!).
	 * 
	 * @param in reader
	 * @param baseURI base URI used for entity resolving (may be <code>null</code>)
	 * @return parsed {@link Document} (not <code>null</code>)
	 * @throws IOException if such exception occurs
	 * @throws ParserConfigurationException if such exception occurs
	 * @throws SAXException if such exception occurs
	 */
	public static Document parseDocument(Reader in, String baseURI) throws IOException, ParserConfigurationException, SAXException {
		Document doc = null;
		try {
			// workaround for XML parser's bug (see UDP-961)
			RestartableReader r = new RestartableReader(in, 4);
			if (r.read() != BOM_CHAR) {
				r.restart();
			}
			r.disableRestarting();

			InputSource is = new InputSource(r);
			if (baseURI != null) {
				is.setSystemId(baseURI);
			}
			DocumentBuilder builder;
			synchronized (builderFactory) {
				builder = builderFactory.newDocumentBuilder();
			}
			doc = builder.parse(is);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return doc;
	}

	/**
	 * Byte Order Mark (BOM) encoded in UTF-8.
	 * <p>
	 * <a href="http://www.unicode.org/faq/utf_bom.html#BOM">Byte Order Mark (BOM) FAQ</a>
	 */
	private static final int[] UTF8_BOM = new int[] { 0xef, 0xbb, 0xbf };

	/**
	 * Returns DOM document parsed from given {@link java.io.InputStream}
	 * (which will be automatically closed!).
	 * 
	 * @param stream input stream
	 * @param encoding input stream encoding or <code>null</code> for auto-detection (see {@link InputSource#setEncoding(java.lang.String)})
	 * @return parsed {@link Document} (not <code>null</code>)
	 * @throws IOException if such exception occurs
	 * @throws ParserConfigurationException if such exception occurs
	 * @throws SAXException if such exception occurs
	 */
	public static Document parseDocument(InputStream stream, String encoding) throws IOException, ParserConfigurationException, SAXException {
		return parseDocument(stream, encoding, null);
	}

	/**
	 * Returns DOM document parsed from given {@link java.io.InputStream}
	 * (which will be automatically closed!).
	 * 
	 * @param encoding input stream encoding or <code>null</code> for auto-detection (see {@link InputSource#setEncoding(java.lang.String)})
	 * @param baseURI base URI used for entity resolving (may be <code>null</code>)
	 * @return parsed {@link Document} (not <code>null</code>)
	 * @throws IOException if such exception occurs
	 * @throws ParserConfigurationException if such exception occurs
	 * @throws SAXException if such exception occurs
	 */
	public static Document parseDocument(InputStream in, String encoding, String baseURI) throws IOException, ParserConfigurationException, SAXException {
		Document doc = null;
		try {
			// workaround for XML parser's bug (see UDP-961)
			RestartableInputStream stream = new RestartableInputStream(in, 4);
			boolean hasUTF8_BOM = false;
			if (stream.read() == UTF8_BOM[0]) {
				if (stream.read() == UTF8_BOM[1]) {
					if (stream.read() == UTF8_BOM[2]) {
						hasUTF8_BOM = true;
					}
				}
			}
			if (!hasUTF8_BOM) {
				stream.restart();
			}
			stream.disableRestarting();

			InputSource is = new InputSource(stream);
			is.setEncoding(encoding);
			if (baseURI != null) {
				is.setSystemId(baseURI);
			}
			DocumentBuilder builder;
			synchronized (builderFactory) {
				builder = builderFactory.newDocumentBuilder();
			}
			doc = builder.parse(is);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return doc;
	}

	/**
	 * Creates new DOM document.
	 */
	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilder builder;
		synchronized (builderFactory) {
			builder = builderFactory.newDocumentBuilder();
		}
		return builder.newDocument();
	}

	/**
	 * Creates and returns a new child element of given parent.
	 * 
	 * @param parent parent
	 * @param tagName tag name
	 * @return new element (not <code>null</code>)
	 */
	public static Element createChildElement(Element parent, String tagName) {
		Element child = parent.getOwnerDocument().createElement(tagName);

		parent.appendChild(child);
		return child;
	}

	/**
	 * Create child element with given parent and text value (as {@link Text}).
	 * 
	 * @param parent parent
	 * @param tagName tag name
	 * @param value text value or <code>null</code>
	 * @return new element (not <code>null</code>)
	 */
	public static Element createChildElementWithTextValue(Element parent, String tagName, String value) {
		Element elem = createChildElement(parent, tagName);
		if (value != null) {
			Node text = elem.getOwnerDocument().createTextNode(value);
			elem.appendChild(text);
		}
		return elem;
	}

	/**
	 * Get or create child element with given parent.
	 * 
	 * @param parent parent element
	 * @param tagName tag name
	 * @return existing or new element (not <code>null</code>)
	 */
	public static Element getOrCreateChildElement(Element parent, String tagName) {
		Element elem = getContainedElement(parent, tagName);
		if (elem == null) {
			elem = createChildElement(parent, tagName);
		}
		return elem;
	}

	public static final String XALAN_INDENT_AMOUNT_PROP = "{http://xml.apache.org/xalan}indent-amount";
	public static final String XALAN_INDENT_AMOUNT_VALUE = "4";

	/**
	 * Writes DOM node to given output stream with given encoding.
	 * The encoding set as an output property, must be compatible with the writer.
	 * The writer is <em>not</em> closed upon finish.
	 * 
	 * @param node node to write
	 * @param writer output writer
	 * @param enc output encoding
	 */
	public static void writeNode(Node node, Writer writer, String enc) throws TransformerException {
		writeNode(node, writer, enc, true);
	}

	/**
	 * Writes DOM node to given output stream with given encoding.
	 * The encoding set as an output property, must be compatible with the writer.
	 * The writer is <em>not</em> closed upon finish.
	 * 
	 * @param node node to write
	 * @param writer output writer
	 * @param enc output encoding
	 * @param indent xml
	 */
	public static void writeNode(Node node, Writer writer, String enc, boolean indent) throws TransformerException {
		Transformer tx = TransformerFactory.newInstance().newTransformer();
		tx.setOutputProperty(OutputKeys.METHOD, "xml");
		tx.setOutputProperty(OutputKeys.ENCODING, enc);
		if (indent) {
			tx.setOutputProperty(OutputKeys.INDENT, "yes");
		}
		tx.setOutputProperty(XALAN_INDENT_AMOUNT_PROP, XALAN_INDENT_AMOUNT_VALUE);
		tx.transform(new DOMSource(node), new StreamResult(writer));
	}

	/**
	 * Writes DOM node to given writer with given encoding and
	 * indentation.
	 * 
	 * @param node node to write
	 * @param out output stream
	 * @param enc output encoding
	 */
	public static void writeNode(Node node, OutputStream out, String enc) throws IOException, TransformerException {
		writeNode(node, out, enc, true);
	}

	/**
	 * Writes DOM node to given writer with given encoding and
	 * indentation.
	 * 
	 * @param node node to write
	 * @param out output stream
	 * @param enc output encoding
	 */
	public static void writeNode(Node node, OutputStream out, String enc, boolean indent) throws IOException, TransformerException {
		Writer w = null;
		try {
			w = new OutputStreamWriter(out, enc);
			writeNode(node, w, enc, indent);
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}

	/**
	 * Writes DOM document to given file with given encoding and
	 * indentation.
	 * 
	 * @param doc document to write
	 * @param f file to create or overwrite
	 * @param enc output file encoding
	 */
	public static void writeDocument(Document doc, File f, String enc) throws IOException, TransformerException {
		writeNode(doc, new FileOutputStream(f), enc);
	}

	/**
	 * Writes DOM document to given file with UTF-8 encoding and
	 * indentation.
	 * 
	 * @param doc document to write
	 * @param f file to create or overwrite
	 */
	public static void writeDocument(Document doc, File f) throws IOException, TransformerException {
		writeDocument(doc, f, ENCODING_UTF8);
	}

	/**
	 * Writes DOM document to given output stream with UTF-8 encoding and
	 * indentation.
	 * 
	 * @param doc document to write
	 * @param out output stream
	 * @param indent
	 */
	public static void writeDocument(Document doc, OutputStream out, boolean indent) throws IOException, TransformerException {
		writeNode(doc, out, ENCODING_UTF8, indent);
	}

	/**
	 * Writes DOM document to given output stream with UTF-8 encoding and
	 * indentation.
	 * 
	 * @param doc document to write
	 * @param out output stream
	 */
	public static void writeDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		writeDocument(doc, out, true);
	}

	/**
	 * Writes DOM element to a string.
	 * 
	 * @param element element to write
	 */
	public static String writeElement(Element element) throws IOException, TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeNode(element, os, ENCODING_UTF8);
		return os.toString(ENCODING_UTF8);
	}

	public static String nodeToString(Node node) {
		if (node == null) {
			return null;
		}
		//if node is directly passed it cause NPE in LS on "<p/>"
		//try parse "<p/> and pass the document element, it fails
		//so I use document fragment here -> copying
		DocumentFragment fragment = node.getOwnerDocument().createDocumentFragment();
		fragment.appendChild(node.cloneNode(true));
		DOMImplementationLS ls;
		try {
			ls = (DOMImplementationLS) DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.getDOMImplementation().getFeature("LS", "3.0"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (ParserConfigurationException e) {
			//should be available if using java 1.5 or above
			throw new RuntimeException(e);
		}
		String str = ls.createLSSerializer().writeToString(fragment);
		//contains <?xml ... ?> header, remove
		if (str.startsWith("<?xml")) { //$NON-NLS-1$
			int i = str.indexOf("?>") + 2; //$NON-NLS-1$
			//handle end of line after xml header(\n\f\r or possible spaces)
			while (Character.isWhitespace(str.charAt(i))) {
				i = i + 1;
			}
			str = str.substring(i);
		}
		return str;
	}

	/**
	 * Returns DOM document parsed from a string.
	 * 
	 * @param string string to parse as xml
	 */
	public static Document parseDocument(String string) throws IOException, ParserConfigurationException, SAXException {
		Reader reader = new StringReader(string);
		return parseDocument(reader);
	}

	/**
	 * Returns DOM element parsed from a string.
	 * 
	 * @param string string to parse as xml
	 */
	public static Element parseElement(String string) throws IOException, ParserConfigurationException, SAXException {
		return parseDocument(string).getDocumentElement();
	}

	/**
	 * Returns a map mapping names of attributes of the given element
	 * to their attribute values.
	 */
	public static Map<String, String> getAttributes(Element element) {
		NamedNodeMap nnm = element.getAttributes();
		Map<String, String> ret = new HashMap<String, String>();
		for (int i = 0; i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			ret.put(n.getNodeName(), n.getNodeValue());
		}
		return ret;
	}

	/**
	 * Whether given stream looks like XML.
	 * <p>
	 * Stream is in XML if it starts with ASCII (UTF-8 compatible) string <code>&lt;?xml</code>,
	 * UTF-8 BOM is ignored.
	 * That means XML encoded in other encoding than UTF-8 or US-ASCII or some kind of Latin is
	 * not recognized.
	 * 
	 * @see <a href="http://www.unicode.org/faq/utf_bom.html#BOM">Byte Order Mark (BOM) FAQ</a>
	 * 
	 * @param stream input stream
	 * @return <code>true</code> if given stream looks like XML
	 * @throws IOException if such exception occurs
	 */
	public static boolean isXMLStream(InputStream stream) throws IOException {
		stream = new RestartableInputStream(stream, 4);
		boolean hasUTF8_BOM = false;
		if (stream.read() == UTF8_BOM[0]) {
			if (stream.read() == UTF8_BOM[1]) {
				if (stream.read() == UTF8_BOM[2]) {
					hasUTF8_BOM = true;
				}
			}
		}
		if (!hasUTF8_BOM) {
			((RestartableInputStream) stream).restart();
		}
		if (stream.read() == '<') {
			if (stream.read() == '?') {
				if (stream.read() == 'x') {
					if (stream.read() == 'm') {
						if (stream.read() == 'l') {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Find first element with given value of given attribute.
	 * <p>
	 * Is DOM 1 compatible.
	 * 
	 * @param node node to start from
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 * @return found {@link Element} or <code>null</code>
	 */
	public static Element getElementByAttributeValue(Node node, String attrName, String attrValue) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element elem = (Element) node;
			if (attrValue.equals(elem.getAttribute(attrName))) {
				return elem;
			}
		}
		node = node.getFirstChild();
		while (node != null) {
			Element elem = getElementByAttributeValue(node, attrName, attrValue);
			if (elem != null) {
				return elem;
			}
			node = node.getNextSibling();
		}
		return null;
	}

	/**
	 * Returns text value of given element.
	 * 
	 * @param parent parent element
	 * @param name wanted element name
	 * @param def default value if element is not found
	 * @return text value
	 */
	public static String getElementValue(Element parent, String name, String def) {
		Element elem = DOMHelper.getContainedElement(parent, name);
		if (elem == null) {
			return def;
		}
		return DOMHelper.getContainedText(elem);
	}

	public static List<Node> getNodes(NodeList nodeList) {
		List<Node> nodes = new ArrayList<Node>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			nodes.add(nodeList.item(i));
		}
		return nodes;
	}

	/**
	 * Removes nodes from their parents.
	 * Nodes without a parent node are ignored.
	 */
	public static void removeNodes(Collection<? extends Node> nodes) {
		for (Node node : nodes) {
			Node parent = node.getParentNode();
			if (parent != null) {
				parent.removeChild(node);
			}
		}
	}

	/**
	 * Removes nodes from their parents.
	 * Nodes without a parent node are ignored.
	 */
	public static void removeNodes(NodeList nodeList) {
		removeNodes(getNodes(nodeList));
	}

	public static void skipElement(Element element) {
		if (!(element.getParentNode() instanceof Element)) {
			throw new IllegalArgumentException("parent is not instance of Element");
		}
		Element parent = (Element) element.getParentNode();
		List<Node> childNodes = DOMHelper.getNodes(element.getChildNodes());
		for (Node childNode : childNodes) {
			if (!(childNode.getNodeType() == Node.ATTRIBUTE_NODE)) {
				element.removeChild(childNode);
				parent.insertBefore(childNode, element);
			}
		}
		parent.removeChild(element);
	}

	/**
	 * Returns prefix assigned to given namespace URI if declared in the root element of the document.
	 * 
	 * @param doc
	 * @param namespaceUri
	 */
	public static String findPrefixForNamespace(Document doc, String namespaceUri) {
		Element root = doc.getDocumentElement();
		Map<String, String> attributes = DOMHelper.getAttributes(root);
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			if (namespaceUri.equals(value)) {
				if (name.startsWith("xmlns:")) {
					return name.substring("xmlns:".length());
				}
			}
		}
		return null;
	}

	/**
	 * Documents are equal if they contain the same nodes in the same order,
	 * except for attributes of Elements, that can occurr in any order.
	 * 
	 * Warning: may not give accurate results for documents containing other nodes
	 * than Elements, Attributes, Texts.
	 * 
	 * @param doc1
	 * @param doc2
	 */
	public static boolean equalDocuments(Document doc1, Document doc2) {
		if ((doc1 == null) || (doc2 == null)) {
			return (doc1 == null) && (doc2 == null);
		}
		if (doc1 == doc2) {
			return true;
		}
		return equalNodes(doc1.getDocumentElement(), doc2.getDocumentElement());
	}

	private static boolean equalNodes(Node node1, Node node2) {
		if (!equalNodesShallow(node1, node2)) {
			return false;
		}
		List childNodes1 = DOMHelper.getNodes(node1.getChildNodes());
		removeNodesOfType(childNodes1);
		List childNodes2 = DOMHelper.getNodes(node2.getChildNodes());
		removeNodesOfType(childNodes2);
		if (childNodes1.size() != childNodes2.size()) {
			return false;
		}
		for (int i = 0; i < childNodes1.size(); i++) {
			Node subnode1 = (Node) childNodes1.get(i);
			Node subnode2 = (Node) childNodes2.get(i);
			if (!equalNodes(subnode1, subnode2)) {
				return false;
			}
		}
		return true;
	}

	private static void removeNodesOfType(List nodes) {
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			Node node = (Node) it.next();
			if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
				it.remove();
			}
		}
	}

	private static boolean equalNodesShallow(Node node1, Node node2) {
		if (node1.getNodeType() != node2.getNodeType()) {
			return false;
		}
		if (node1.getNodeType() == Node.ELEMENT_NODE) {
			Map<String, String> attrs1 = DOMHelper.getAttributes((Element) node1);
			Map<String, String> attrs2 = DOMHelper.getAttributes((Element) node2);
			if (!haveSameElements(attrs1.keySet(), attrs2.keySet())) {
				return false;
			}
			for (Object element : attrs1.keySet()) {
				String name = (String) element;
				if (!equalsWithNull(attrs1.get(name), attrs2.get(name))) {
					return false;
				}
			}
			return true;
		}
		return equalsWithNull(node1.getNodeValue(), node2.getNodeValue());
	}

	private static boolean equalsWithNull(Object o1, Object o2) {
		if (o1 == null) {
			return (o2 == null);
		}
		return o1.equals(o2);
	}

	private static boolean haveSameElements(Collection<?> a, Collection<?> b) {
		if (a.size() != b.size()) {
			return false;
		}
		Set<?> s1 = new HashSet<Object>(a);
		Set<?> s2 = new HashSet<Object>(b);
		return s1.containsAll(s2) && s2.containsAll(s1);
	}

	/**
	 * Returns whether given character is valid for XML.
	 * <p>
	 * See <a href="http://www.w3.org/TR/REC-xml/#NT-Char">http://www.w3.org/TR/REC-xml/#NT-Char</a> for
	 * more information.
	 * 
	 * @param ch
	 * @return <code>true</code> if character is valid
	 */
	public static boolean isXMLValidCharacter(char ch) {
		return (ch == 0x9)
				|| (ch == 0xA)
				|| (ch == 0xD)
				|| ((ch >= 0x20) && (ch <= 0xD7FF))
				|| ((ch >= 0xE000) && (ch <= 0xFFFD))
				|| ((ch >= 0x10000) && (ch <= 0x10FFFF));
	}

	/**
	 * Strips from input string any characters which are not {@link #isXMLValidCharacter(char)}.
	 * 
	 * @param str
	 * @return string without any invalid characters
	 */
	public static String stripXMLInvalidCharacters(String str) {
		if (str == null) {
			return null;
		}
		boolean hasUnsafe = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!isXMLValidCharacter(c)) {
				hasUnsafe = true;
				break;
			}
		}
		if (hasUnsafe) {
			char[] newStr = new char[str.length()];
			int j = 0;
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (isXMLValidCharacter(c)) {
					newStr[j++] = c;
				}
			}
			str = new String(newStr, 0, j);
		}
		return str;
	}

	public static Element getNextSiblingElement(Element element) {
		Node nextSibling = element.getNextSibling();
		while ((nextSibling != null) && !(nextSibling instanceof Element)) {
			nextSibling = nextSibling.getNextSibling();
		}
		return (nextSibling instanceof Element) ? (Element) nextSibling : null;
	}

}
