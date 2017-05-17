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
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings("nls")
public class PolarionPropertiesReader {

    private static final String PROP_POLARION_DATA = "com.polarion.data";
    private static final String PROP_POLARION_WORKSPACE = "com.polarion.workspace";
    private static final String PROP_HOME = "com.polarion.home";
    private static final String PROP_WORK_DIR = "workDir";

    public final Properties polarionProperties;
    public final Properties mergedProperties = new Properties();
    public File propsFile;
    public final boolean loaded;
    public final Properties sharedProperties = new Properties();
    public File sharedPropertiesFile;

    public PolarionPropertiesReader(File polarionHome) {
        propsFile = getPropertiesFile(polarionHome);
        polarionProperties = new Properties();
        polarionProperties.put(PROP_HOME, new File(polarionHome, "polarion").getAbsolutePath());
        polarionProperties.put(PROP_POLARION_DATA, "$[com.polarion.home]/../data");
        polarionProperties.put(PROP_POLARION_WORKSPACE, "$[com.polarion.data]/workspace");
        polarionProperties.put(PROP_WORK_DIR, "$[com.polarion.workspace]/polarion-data");

        initSharedProperties(polarionHome);

        if (propsFile.exists()) {
            PropertiesLoader.loadProperties(propsFile, polarionProperties);
            mergedProperties.putAll(polarionProperties);

            loaded = true;
        } else {
            loaded = false;
        }
    }

    private void initSharedProperties(File polarionHome) {
        sharedPropertiesFile = getPropertiesFile(new File(polarionHome, "shared"));
        if (sharedPropertiesFile.exists()) {
            PropertiesLoader.loadProperties(sharedPropertiesFile, sharedProperties);
            mergedProperties.putAll(sharedProperties);
        }
    }

    private File getPropertiesFile(File startPath) {
        File file = new File(startPath, "polarion/configuration/polarion.properties");
        if (!file.exists()) {
            file = new File(startPath, "etc/polarion.properties");
        }
        return file;
    }

    public String getProperty(String name) {
        String value = mergedProperties.getProperty(name);
        if (value != null) {
            value = expandVariables(value, mergedProperties);
        }
        return value;
    }

    private static String expandVariables(String s, Properties props) {
        return expandVariablesInternal(s, props, new HashSet<String>());
    }

    private static String expandVariablesInternal(String s, Properties props, Set<String> expandingProps) {
        if (s == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        int i = 0, j = 0;
        while ((j = s.indexOf("$[", i)) != -1) {
            buf.append(s.substring(i, j));
            if (!((j > 0) && (s.charAt(j - 1) == '\\'))) {
                int l = s.indexOf(']', j + 2);
                if (l != -1) {
                    String varName = s.substring(j + 2, l);
                    String varValue = null;
                    if (!expandingProps.contains(varName)) {
                        varValue = props.getProperty(varName);
                        expandingProps.add(varName);
                        varValue = expandVariablesInternal(varValue, props, expandingProps);
                        expandingProps.remove(varName);
                    }
                    if (varValue != null) {
                        buf.append(varValue);
                    } else {
                        buf.append("$[" + varName + "]");
                    }
                    i = l + 1;
                    continue;
                } else {
                    break;
                }
            }
            buf.append("$[");
            i = j + 2;
        }
        if (i < s.length()) {
            buf.append(s.substring(i));
        }

        return buf.toString();
    }

}
