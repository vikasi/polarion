/*
  * Copyright (C) 2004-2012 Polarion Software
  */
package com.polarion.fieldtest

import org.apache.log4j.Logger
import org.apache.tools.ant.Project

import com.polarion.diagtool.Context
import com.polarion.diagtool.Probe
import com.polarion.diagtool.ProgressMonitor
import com.polarion.diagtool.internal.Log4jAntLogger
import com.polarion.diagtool.internal.PropertiesLoader
import com.polarion.diagtool.internal.XUnitFileHelper

public class FieldTest {

    private static final Logger log = Logger.getLogger(this)

    private final Context context
    private final AntBuilder ant

    private Log4jAntLogger antLogger

    private Properties testProperties;

    public FieldTest(Context context) {
        this.context = context
        ant = new AntBuilder()
        initLog()
    }

    def run() {
        File testPropertiesFile = new File(context.configuration.home, "fieldtest.properties")
        testProperties = PropertiesLoader.loadProperties(testPropertiesFile)

        List<String> suites = collectSuites()
        if (suites.isEmpty()) {
            log.warn("No suites to run. Check property suites in fieldtest.properties.")
            context.warnings++
            return
        }

        ProgressMonitor progress = context.newProgressMonitor(suites.size + 2)
        progress.start()

        try {
            log.info("The following test suites will be executed:\n  " + suites.join("\n  "))

            File resultsDir = new File(context.resultsDir, "fieldtest")
            File resultsFile = new File(resultsDir, "/perftests.xml")
            resultsFile.getParentFile().mkdirs();
            PerformanceTestCase.init(resultsFile, testPropertiesFile, testProperties, context.configuration.polarionHome).each {
                context.probeRecorder.recordProbe(new Probe(name:it.name, label:it.label, value:it.value), log)
            }

            ant.junit(printsummary:"on",showoutput:"true") {
                for (String suite : suites) {
                    test(name:suite,todir:resultsDir) { formatter(type:"xml") }
                    progress.tick()
                }
            }

            PerformanceTestCase.finish();

            Map<String,String> times = XUnitFileHelper.readTimes(resultsFile)
            times.each { name, time ->
                context.probeRecorder.recordProbe(
                        new Probe(name:"fieldtest.time.${name}", label:"Average time to run for ${name} (in seconds)", value:time), log)
            }

            log.info("Copying measured times to test suites...")
            copyMeasuredTimesToReports(resultsFile, times);
            progress.tick()

            log.info("Generating html report...")
            ant.junitreport(todir:resultsDir) {
                fileset(dir:resultsDir) {
                    include(name:"TEST-*.xml");
                }
                report(format:"noframes",todir:resultsDir)
            }
            progress.tick()
        } finally {
            progress.finish()
            context.errors += antLogger.errorCounter
            context.warnings += antLogger.warnCounter
        }
    }

    private void initLog() {
        def antProject = ant.getProject()
        antProject.getBuildListeners().each { antProject.removeBuildListener(it) }
        antLogger = new Log4jAntLogger(log);
        antLogger.setMessageOutputLevel(Project.MSG_INFO);
        antProject.addBuildListener(antLogger);
    }

    private List<String> collectSuites() {
        List<String> result = new ArrayList<String>();
        String suitesProperty = testProperties.getProperty("suites");
        String[] suites = suitesProperty.split(" ");
        for (String suite : suites) {
            suite = suite.trim();
            if (!suite.startsWith("-")) {
                result.add(suite);
            }
        }
        return result;
    }

    private void copyMeasuredTimesToReports(File resultsFile, Map<String,String> times) {
        File[] files = resultsFile.getParentFile().listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith("TEST-") && fileName.endsWith(".xml")) {
                XUnitFileHelper.setTimes(file, times);
            }
        }
    }
}
