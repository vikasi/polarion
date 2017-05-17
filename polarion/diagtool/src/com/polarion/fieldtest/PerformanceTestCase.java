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
package com.polarion.fieldtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.polarion.diagtool.internal.PolarionPropertiesReader;
import com.polarion.diagtool.internal.XUnitXmlBuilder;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

@SuppressWarnings("nls")
public class PerformanceTestCase extends TestCase {

    private static final Logger logger = Logger.getLogger(PerformanceTestCase.class);

    private static XUnitXmlBuilder builder;

    private static Properties testProperties = new Properties();

    private static PolarionPropertiesReader polarionPropertiesReader;

    public XUnitXmlBuilder getBuilder() {
        if (builder == null) {
            init(null, null, null, null);
        }
        return builder;
    }

    public static final class PureJavaProbe {
        public String name;
        public String label;
        public Object value;
    }

    public static Collection<PureJavaProbe> init(File outputFile, File testPropertiesFile, Properties testProperties, File polarionHome) {
        Collection<PureJavaProbe> probes = new ArrayList<PureJavaProbe>();
        initBuilder(outputFile);
        PerformanceTestCase.testProperties = testProperties;
        probes.add(propertiesProbe("fieldtest.configuration.", testPropertiesFile, testProperties));
        if (polarionHome != null) {
            polarionPropertiesReader = new PolarionPropertiesReader(polarionHome);
            probes.add(propertiesProbe("fieldtest.polarion.shared.configuration.", polarionPropertiesReader.sharedPropertiesFile, polarionPropertiesReader.sharedProperties));
            probes.add(propertiesProbe("fieldtest.polarion.configuration.", polarionPropertiesReader.propsFile, polarionPropertiesReader.polarionProperties));
        }
        return probes;
    }

    private static PureJavaProbe propertiesProbe(String namePrefix, File file, Properties props) {
        PureJavaProbe probe = new PureJavaProbe();
        probe.name = namePrefix + file.getAbsolutePath();
        probe.label = "Pre-processed configuration file " + file.getAbsolutePath();
        StringBuilder buf = new StringBuilder();
        List<String> propList = new ArrayList<String>(props.stringPropertyNames());
        Collections.sort(propList);
        for (String prop : propList) {
            buf.append("\n  " + prop + " = " + props.getProperty(prop));
        }
        probe.value = buf;
        return probe;
    }

    public static void finish() {
        if (builder != null) {
            builder.endSuite();
            builder.end();
            builder = null;
        }
    }

    protected static void initBuilder(File outputFile) {
        try {
            File filePath = outputFile != null ? outputFile : new File("C:/Temp/perftests.xml");
            logger.info("Writing new performance results file to: " + filePath);

            builder = new XUnitXmlBuilder(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            builder.start();
            builder.startSuite("suite");

            Runtime.getRuntime().addShutdownHook(new Thread("Polarion performance tests shutdown hook.") {
                @Override
                public void run() {
                    finish();
                }
            });

        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
            builder = null;
        }
    }

    public PerformanceTestCase() {
        super();
    }

    public PerformanceTestCase(String name) {
        super(name);
    }

    private long startTime;

    private int count;
    private long timeSum;

    private long elapsedFirst;

    @Override
    protected void setUp() throws Exception {
        count = 0;
        timeSum = 0;
        super.setUp();
    }

    protected void startMeasuring() {
        startTime = System.currentTimeMillis();
    }

    protected void stopMeasuring() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (count == 0) {
            elapsedFirst = elapsed;
        }

        logger.info(getName() + " elapsed: " + elapsed); //$NON-NLS-1$
        timeSum += elapsed;
        count++;
    }

    protected void assertPerformance() {
        if (count > 0) {
            logger.info(getName() + " average: " + (timeSum / count)); //$NON-NLS-1$
        }
        if (count > 1) {
            long averageWithoutFirst = (timeSum - elapsedFirst) / (count - 1);
            assertFalse(String.format("First measuring is too slow compared to the rest: first=%d, average of the rest=%d", elapsedFirst, averageWithoutFirst), elapsedFirst > 3 * averageWithoutFirst);
        }
    }

    @Override
    public void runBare() throws Throwable {
        try {
            super.runBare();
            passed();
        } catch (AssertionFailedError e) {
            failed(e);
            throw e;
        } catch (ThreadDeath e) { // don't catch ThreadDeath by accident
            throw e;

        } catch (Throwable e) {
            failed(e);
            throw e;
        }
    }

    private void passed() {
        if (getBuilder() != null) {
            builder.passed(getClass().getName(), getName(), computeAverage());
        }
    }

    private void failed(Throwable e) {
        if (getBuilder() != null) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));

            builder.failed(getClass().getName(), getName(), computeAverage(), e.getMessage(), e.getClass().getName(), stringWriter.toString());
        }

    }

    protected long computeAverage() {
        return count > 0 ? (timeSum / count) : 0;
    }

    protected String getPolarionProperty(String name) {
        return (polarionPropertiesReader != null) ? polarionPropertiesReader.getProperty(name) : null;
    }

    protected File getPolarionDataFolder() throws IOException {
        String dataFolderName = getPolarionProperty("com.polarion.data"); //$NON-NLS-1$
        return (dataFolderName != null) ? new File(dataFolderName).getCanonicalFile() : null;
    }

    protected String getPolarionRepoURL() {
        return getPolarionProperty("repo"); //$NON-NLS-1$
    }

    protected String getPolarionSystemUserPassword() {
        return getPolarionProperty("password"); //$NON-NLS-1$
    }

    protected String getPolarionSystemUserName() {
        return getPolarionProperty("login"); //$NON-NLS-1$
    }

    protected String getTestProperty(String name) {
        return testProperties.getProperty(name);
    }

    protected int getTestPropertyAsInt(String name) {
        return Integer.parseInt(getTestProperty(name));
    }

    protected boolean isTestPropertyEnabled(String name) {
        return Boolean.parseBoolean(getTestProperty(name));
    }

    protected boolean executeLoadTests() {
        return !isTestPropertyEnabled("disableLoadTests");
    }

}
