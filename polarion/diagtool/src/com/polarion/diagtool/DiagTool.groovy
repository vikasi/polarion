/*
 * Copyright (C) 2004-2016 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2016 Polarion Software
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
package com.polarion.diagtool

import java.text.SimpleDateFormat

import org.apache.log4j.Logger

import com.polarion.diagtool.internal.Banner
import com.polarion.diagtool.internal.FileSender
import com.polarion.diagtool.internal.FileSenderConfiguration
import com.polarion.diagtool.internal.PolarionPropertiesReader
import com.polarion.diagtool.internal.ResultsFileAppender

class DiagTool {

    private final Logger log
    private final Configuration configuration
    private final File resultsDir
    private final Context context
    private String sendResults
    private FileSenderConfiguration fileSenderConfiguration

    public DiagTool(Configuration configuration) {
        this.configuration = configuration

        resultsDir = createResultsDir(configuration)
        println "Results will be stored in $resultsDir"
        ResultsFileAppender.resultsDir = resultsDir
        log = Logger.getLogger(this.class)

        context = new Context(configuration, resultsDir, log)

        def bnum = new File(configuration.home, ".bnum")
        context.probeRecorder.recordProbe(new Probe(name:"build.id", label:"Build id", value:bnum.isFile() ? bnum.text.trim() : "<unknown>"), log)

        configuration.recordProbes(context)

        prepareFileSenderConfiguration()
    }

    private void prepareFileSenderConfiguration() {
        sendResults = configuration.sendResults
        if (sendResults != "no") {
            def mailHost = configuration."mail.host"
            if ((mailHost == null) || mailHost.isEmpty()) {
                loadMailConfigurationFromPolarionProperties()
            }
            fileSenderConfiguration = new FileSenderConfiguration(
                    mailSubject:configuration."mail.subject",
                    mailSender:configuration."mail.sender",
                    mailAttachmentThreshold:(configuration."mail.attachmentThreshold" as long),
                    mailRecipient:configuration."mail.recipient",
                    mailProtocol:configuration."mail.protocol",
                    mailHost:configuration."mail.host",
                    mailAuth:Boolean.parseBoolean(configuration."mail.auth"),
                    mailUser:configuration."mail.user",
                    mailPassword:configuration."mail.password",
                    mailProtocolSettings:(configuration.properties.findAll { it.key.startsWith("mail.") } as Properties),
                    ftpHost:configuration."ftp.host",
                    ftpUser:configuration."ftp.user",
                    ftpPassword:configuration."ftp.password",
                    )
            if (configuration."mail.port" != null) {
                fileSenderConfiguration.mailPort = configuration."mail.port" as int
            }
            if (!fileSenderConfiguration.isValid(log)) {
                log.warn("Configuration for sending the results is not valid - continue without sending...")
                sendResults = "no"
            }
        }
    }

    private void loadMailConfigurationFromPolarionProperties() {
        PolarionPropertiesReader propertiesReader = new PolarionPropertiesReader(configuration.polarionHome)
        if (!propertiesReader.loaded) {
            return
        }
        log.info("Using mail settings from ${propertiesReader.propsFile}")
        loadMailConfigurationFromPolarionProperties(propertiesReader, "mail.host", "announcer.smtp.host")
        loadMailConfigurationFromPolarionProperties(propertiesReader, "mail.port", "announcer.smtp.port")
        loadMailConfigurationFromPolarionProperties(propertiesReader, "mail.auth", "announcer.smtp.auth")
        loadMailConfigurationFromPolarionProperties(propertiesReader, "mail.user", "announcer.smtp.user")
        loadMailConfigurationFromPolarionProperties(propertiesReader, "mail.password", "announcer.smtp.password")
    }

    private void loadMailConfigurationFromPolarionProperties(PolarionPropertiesReader propertiesReader, String diagtoolName, String polarionName) {
        def value = propertiesReader.getProperty(polarionName)
        if (value != null) {
            log.info("... ${polarionName}: ${value} -> ${diagtoolName}")
            configuration.properties[diagtoolName] = value
        }
    }

    public void run() {
        runModules()

        context.probeRecorder.storeProbes()

        new AntBuilder().zip(destfile:context.resultsZip.absolutePath, basedir:resultsDir.absolutePath)

        showBanner()

        sendResults()
    }

    private void runModules() {
        configuration.modules.eachWithIndex { module, i ->
            log.info("Running module $module (${i + 1}/${configuration.modules.size})")
            try {
                def moduleInstance = Class.forName(module, true, this.getClass().classLoader).newInstance(context)
                moduleInstance.run()
            } catch (Exception e) {
                log.error("Unexpected error: " + e.message, e)
                context.errors++
            }
        }
    }

    private void showBanner() {
        def Banner banner = new Banner()
        if (context.errors > 0 || context.warnings > 0) {
            banner << (context.errors > 0 ? "FAILURE" : "Success with warnings")
            banner << ""
            banner << "There were ${context.errors} errors and ${context.warnings} warnings logged."
        } else {
            banner << "Success"
        }
        log.info("\n" + banner)
    }

    private void sendResults() {
        while ((sendResults != "yes") && (sendResults != "no")) {
            sendResults = System.console().readLine("Send results? [yes/no] ")
        }
        if (sendResults == "yes") {
            FileSender fileSender = new FileSender(log, fileSenderConfiguration)
            fileSender.sendFile(context.resultsZip)
        }
    }

    private static File createResultsDir(Configuration configuration) {
        def File resultsParentDir = configuration.resultsParentDir
        def File resultsDir = new File(resultsParentDir, new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date()))
        if (resultsDir.exists()) {
            println("Results directory ${resultsDir} already exists")
            System.exit(3)
        }
        if (!resultsDir.mkdirs()) {
            println("Results directory ${resultsDir} cannot be created")
            System.exit(4)
        }
        return resultsDir
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            println "ERROR: Unexpected number of arguments"
            System.exit(1)
        }
        new DiagTool(new Configuration(args)).run()
    }
}
