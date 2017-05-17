/*
 * $Id: OrderedProperties.java,v 1.4 2004/02/25 11:49:57 chyliko Exp $
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is almost drop-in replacement for {@link java.util.Properties}
 * (although this class does not extend Properties).
 * <p>
 * Missing features: no default properties, no deprecated features,
 * does not extend {@link java.util.Hashtable} (but we are thread-safe as well),
 * no date header.
 * <p>
 * This class persists property keys order as well as comments.
 *
 * @author  <A HREF="mailto:dev@polarion.com">Stepan Roh</A>, Polarion Software
 * @version $Revision: 1.4 $ $Date: 2004/02/25 11:49:57 $
 */
@SuppressWarnings("nls")
public class OrderedProperties implements Map {

    /**
     * Constructor for OrderedProperties.
     */
    public OrderedProperties() {
        super();
    }

    protected class Property {
        public String value;
        public String comment;

        public Property(String value, String comment) {
            this.value = value;
            this.comment = comment;
        }

        /**
         * @see java.lang.Object#equals(Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Property) {
                Property p = (Property) obj;
                return ((value == null) && (p.value == null))
                        || ((value != null) && value.equals(p.value));
            }
            return false;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return (value != null) ? value.hashCode() : 0;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Property [value=" + value + ", comment=" + comment + "]";
        }

    }

    // maps property key to Property - LOCK
    protected Map<String, Property> innerMap = new HashMap<String, Property>();
    // LOCK innerMap
    protected List<String> keysOrder = new ArrayList<String>();
    // LOCK innerMap
    private String eofComment = null;

    /**
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        synchronized (innerMap) {
            innerMap.clear();
            keysOrder.clear();
        }
    }

    /**
     * @see java.util.Map#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        synchronized (innerMap) {
            return innerMap.containsKey(key);
        }
    }

    /**
     * @see java.util.Map#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        synchronized (innerMap) {
            return innerMap.containsValue(new Property((String) value, null));
        }
    }

    protected class EntrySet extends AbstractSet {
        /**
         * @see java.util.Collection#iterator()
         */
        @Override
        public Iterator iterator() {
            return new Iterator() {
                private Iterator innerIter = keysOrder.iterator();
                private String lastKey = null;
                private boolean canRemove = false;

                /**
                 * @see java.util.Iterator#hasNext()
                 */
                @Override
                public boolean hasNext() {
                    return innerIter.hasNext();
                }

                /**
                 * @see java.util.Iterator#next()
                 */
                @Override
                public Object next() {
                    synchronized (innerMap) {
                        final String key = (String) innerIter.next();
                        lastKey = key;
                        canRemove = true;
                        final Property p = innerMap.get(key);
                        return new Map.Entry() {
                            /**
                             * @see java.util.Map.Entry#getKey()
                             */
                            @Override
                            public Object getKey() {
                                return key;
                            }

                            /**
                             * @see java.util.Map.Entry#getValue()
                             */
                            @Override
                            public Object getValue() {
                                return p.value;
                            }

                            /**
                             * @see java.util.Map.Entry#setValue(Object)
                             */
                            @Override
                            public Object setValue(Object value) {
                                return p.value = (String) value;
                            }

                            /**
                             * @see java.util.Map.Entry#equals(Object)
                             */
                            @Override
                            public boolean equals(Object o) {
                                if (o instanceof Map.Entry) {
                                    Map.Entry e = (Map.Entry) o;
                                    return (((getKey() == null) && (e.getKey() == null))
                                            || ((getKey() != null) && getKey().equals(e.getKey())))
                                            && (((getValue() == null) && (e.getValue() == null))
                                            || ((getValue() != null) && getValue().equals(e.getValue())));
                                }
                                return false;
                            }

                            /**
                             * @see java.util.Map.Entry#hashCode()
                             */
                            @Override
                            public int hashCode() {
                                return (getKey() == null ? 0 : getKey().hashCode()) ^
                                        (getValue() == null ? 0 : getValue().hashCode());
                            }
                        };
                    }
                }

                /**
                 * @see java.util.Iterator#remove()
                 */
                @Override
                public void remove() {
                    if (!canRemove) {
                        throw new IllegalStateException("No item to remove");
                    }
                    synchronized (innerMap) {
                        innerIter.remove();
                        innerMap.remove(lastKey);
                    }
                    canRemove = false;
                }
            };
        }

        /**
         * @see java.util.Collection#size()
         */
        @Override
        public int size() {
            return OrderedProperties.this.size();
        }

    }

    protected EntrySet entrySetCol = new EntrySet();

    /**
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set entrySet() {
        return entrySetCol;
    }

    /**
     * @see java.util.Map#get(Object)
     */
    @Override
    public Object get(Object key) {
        return getProperty((String) key);
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        synchronized (innerMap) {
            return innerMap.isEmpty();
        }
    }

    protected class KeySet extends AbstractSet {
        /**
         * @see java.util.Collection#iterator()
         */
        @Override
        public Iterator iterator() {
            return new Iterator() {
                private Iterator innerIter = entrySet().iterator();

                /**
                 * @see java.util.Iterator#hasNext()
                 */
                @Override
                public boolean hasNext() {
                    return innerIter.hasNext();
                }

                /**
                 * @see java.util.Iterator#next()
                 */
                @Override
                public Object next() {
                    Map.Entry e = (Map.Entry) innerIter.next();
                    return (e != null) ? e.getKey() : null;
                }

                /**
                 * @see java.util.Iterator#remove()
                 */
                @Override
                public void remove() {
                    innerIter.remove();
                }
            };
        }

        /**
         * @see java.util.Collection#size()
         */
        @Override
        public int size() {
            return OrderedProperties.this.size();
        }

    }

    protected KeySet keySetCol = new KeySet();

    /**
     * @see java.util.Map#keySet()
     */
    @Override
    public Set keySet() {
        return keySetCol;
    }

    /**
     * @see java.util.Map#put(Object, Object)
     */
    @Override
    public Object put(Object key, Object value) {
        return setProperty((String) key, (String) value);
    }

    /**
     * @see java.util.Map#putAll(Map)
     */
    @Override
    public void putAll(Map t) {
        synchronized (innerMap) {
            Iterator iter = t.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                put(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * @see java.util.Map#remove(Object)
     */
    @Override
    public Object remove(Object key) {
        synchronized (innerMap) {
            keysOrder.remove(key);
            return innerMap.remove(key);
        }
    }

    /**
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        synchronized (innerMap) {
            return innerMap.size();
        }
    }

    protected class ValuesCollection extends AbstractCollection {
        /**
         * @see java.util.Collection#iterator()
         */
        @Override
        public Iterator iterator() {
            return new Iterator() {
                private Iterator innerIter = entrySet().iterator();

                /**
                 * @see java.util.Iterator#hasNext()
                 */
                @Override
                public boolean hasNext() {
                    return innerIter.hasNext();
                }

                /**
                 * @see java.util.Iterator#next()
                 */
                @Override
                public Object next() {
                    Map.Entry e = (Map.Entry) innerIter.next();
                    return (e != null) ? e.getValue() : null;
                }

                /**
                 * @see java.util.Iterator#remove()
                 */
                @Override
                public void remove() {
                    innerIter.remove();
                }
            };
        }

        /**
         * @see java.util.Collection#size()
         */
        @Override
        public int size() {
            return OrderedProperties.this.size();
        }

    }

    protected ValuesCollection valuesCol = new ValuesCollection();

    /**
     * @see java.util.Map#values()
     */
    @Override
    public Collection values() {
        return valuesCol;
    }

    public Object setProperty(String key, String value) {
        return setProperty(key, value, null);
    }

    public String setProperty(String key, String value, String comment) {
        return setProperty(key, value, comment, null);
    }

    public String setProperty(String key, String value, String comment, String afterKey) {
        return setProperty(key, value, comment, afterKey, false);
    }

    private String setProperty(String key, String value, String comment, String afterKey, boolean duringLoad) {
        if (!duringLoad && (comment != null)) {
            comment = "#" + comment.replaceAll("\n", "\n#");
        }
        synchronized (innerMap) {
            String prevValue = null;
            boolean addedToEnd = false;
            if (!keysOrder.contains(key)) {
                if ((afterKey != null) && keysOrder.contains(afterKey)) {
                    int afterIndex = keysOrder.indexOf(afterKey);
                    keysOrder.add(afterIndex + 1, key);
                    addedToEnd = ((afterIndex + 1) == keysOrder.size());
                } else {
                    keysOrder.add(key);
                    addedToEnd = true;
                }
            } else {
                Property p = innerMap.get(key);
                if (comment == null) {
                    comment = p.comment;
                }
                prevValue = p.value;
            }
            if (!duringLoad && addedToEnd && (eofComment != null)) {
                if (comment != null) {
                    comment = eofComment + "\n" + comment;
                } else {
                    comment = eofComment;
                }
                eofComment = null;
            }
            innerMap.put(key, new Property(value, comment));
            return prevValue;
        }
    }

    private static final String keyValueSeparators = "=: \t\r\n\f";
    private static final String strictKeyValueSeparators = "=:";
    private static final String specialSaveChars = "=: \t\r\n\f#!";
    private static final String whiteSpaceChars = " \t\r\n\f";

    public void load(InputStream is) throws IOException {
        synchronized (innerMap) {
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "8859_1"));
            String comment = null;
            while (true) {
                // Get next line
                String line = in.readLine();
                if (line == null) {
                    if (comment != null) {
                        eofComment = comment;
                    }
                    return;
                }

                if (line.length() > 0) {
                    int i;
                    for (i = 0; i < line.length(); i++) {
                        if (whiteSpaceChars.indexOf(line.charAt(i)) < 0) {
                            break;
                        }
                    }
                    line = line.substring(i);
                }
                if (line.length() == 0) {
                    if (comment == null) {
                        comment = "";
                    } else {
                        comment += "\n";
                    }
                }
                if (line.length() > 0) {
                    char firstChar = line.charAt(0);
                    if ((firstChar == '#') || (firstChar == '!')) {
                        String s = loadConvert(line);
                        if (comment == null) {
                            comment = s;
                        } else {
                            comment += "\n" + s;
                        }
                    } else {
                        // Continue lines that end in slashes if they are not comments
                        while (continueLine(line)) {
                            String nextLine = in.readLine();
                            if (nextLine == null) {
                                nextLine = new String("");
                            }
                            String loppedLine = line.substring(0, line.length() - 1);
                            // Advance beyond whitespace on new line
                            int startIndex = 0;
                            for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
                                if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1) {
                                    break;
                                }
                            }
                            nextLine = nextLine.substring(startIndex, nextLine.length());
                            line = new String(loppedLine + nextLine);
                        }

                        // Find start of key
                        int len = line.length();
                        int keyStart;
                        for (keyStart = 0; keyStart < len; keyStart++) {
                            if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1) {
                                break;
                            }
                        }

                        // Blank lines are ignored
                        if (keyStart == len) {
                            continue;
                        }

                        // Find separation between key and value
                        int separatorIndex;
                        for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
                            char currentChar = line.charAt(separatorIndex);
                            if (currentChar == '\\') {
                                separatorIndex++;
                            } else if (keyValueSeparators.indexOf(currentChar) != -1) {
                                break;
                            }
                        }

                        // Skip over whitespace after key if any
                        int valueIndex;
                        for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
                            if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
                                break;
                            }
                        }

                        // Skip over one non whitespace key value separators if any
                        if (valueIndex < len) {
                            if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1) {
                                valueIndex++;
                            }
                        }

                        // Skip over white space after other separators if any
                        while (valueIndex < len) {
                            if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
                                break;
                            }
                            valueIndex++;
                        }
                        String key = line.substring(keyStart, separatorIndex);
                        String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                        // Convert then store key and value
                        key = loadConvert(key);
                        value = loadConvert(value);
                        setProperty(key, value, comment, null, true);
                        comment = null;
                    }
                }
            }
        }
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     */
    private String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    private boolean continueLine(String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\')) {
            slashCount++;
        }
        return (slashCount % 2 == 1);
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx
     * and writes out any of the characters in specialSaveChars
     * with a preceding slash
     */
    private static String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
            case ' ':
                if (x == 0 || escapeSpace) {
                    outBuffer.append('\\');
                }

                outBuffer.append(' ');
                break;
            case '\\':
                outBuffer.append('\\');
                outBuffer.append('\\');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            default:
                if ((aChar < 0x0020) || (aChar > 0x007e)) {
                    outBuffer.append('\\');
                    outBuffer.append('u');
                    outBuffer.append(toHex((aChar >> 12) & 0xF));
                    outBuffer.append(toHex((aChar >> 8) & 0xF));
                    outBuffer.append(toHex((aChar >> 4) & 0xF));
                    outBuffer.append(toHex(aChar & 0xF));
                } else {
                    if (specialSaveChars.indexOf(aChar) != -1) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(aChar);
                }
            }
        }
        return outBuffer.toString();
    }

    /**
     * Convert a nibble to a hex character
     * @param   nibble  the nibble to convert.
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /** A table of hex digits */
    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    private static void writeComment(BufferedWriter bw, String comment) throws IOException {
        comment = comment.replaceAll("\r", "");
        comment = comment.replaceAll("\n", System.getProperty("line.separator"));
        writeln(bw, comment);
    }

    public void store(OutputStream os, String header) throws IOException {
        BufferedWriter awriter;
        awriter = new BufferedWriter(new OutputStreamWriter(os, "8859_1"));
        if (header != null) {
            writeln(awriter, "#" + header);
        }
        synchronized (innerMap) {
            Iterator iter = keysOrder.iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Property p = innerMap.get(key);
                key = saveConvert(key, true);
                String comment = p.comment;
                if (comment != null) {
                    writeComment(awriter, comment);
                }
                String val = p.value;
                val = saveConvert(val, false);
                writeln(awriter, key + "=" + val);
            }
            if (eofComment != null) {
                writeComment(awriter, eofComment);
            }
        }
        awriter.flush();
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        synchronized (innerMap) {
            Property p = innerMap.get(key);
            String s = (p != null) ? p.value : null;
            return (s != null) ? s : defaultValue;
        }
    }

    protected class PropertyNamesEnumeration implements Enumeration {
        private Iterator iter = keySet().iterator();

        /**
         * @see java.util.Enumeration#hasMoreElements()
         */
        @Override
        public boolean hasMoreElements() {
            return iter.hasNext();
        }

        /**
         * @see java.util.Enumeration#nextElement()
         */
        @Override
        public Object nextElement() {
            return iter.next();
        }

    }

    public Enumeration propertyNames() {
        return new PropertyNamesEnumeration();
    }

    public boolean renameProperty(String oldKey, String newKey) {
        synchronized (innerMap) {
            Property p = innerMap.get(oldKey);
            if (p == null) {
                return false;
            }
            innerMap.remove(oldKey);
            innerMap.put(newKey, p);

            int propertyIndex = keysOrder.indexOf(oldKey);
            keysOrder.set(propertyIndex, newKey);
            return true;
        }
    }

    public void list(PrintStream ps) {
        ps.println("-- listing properties --");
        Set props = entrySet();
        Iterator iter = props.iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            String key = (String) e.getKey();
            String val = (String) e.getValue();
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            ps.println(key + "=" + val);
        }
    }

    public void list(PrintWriter pw) {
        pw.println("-- listing properties --");
        Set props = entrySet();
        Iterator iter = props.iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            String key = (String) e.getKey();
            String val = (String) e.getValue();
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            pw.println(key + "=" + val);
        }
    }

}
