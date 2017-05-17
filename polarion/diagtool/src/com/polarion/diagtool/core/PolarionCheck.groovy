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
package com.polarion.diagtool.core

import org.apache.log4j.Logger

import com.polarion.diagtool.AbstractProbesCheck
import com.polarion.diagtool.Context
import com.polarion.diagtool.Probe

class PolarionCheck extends AbstractProbesCheck {

    private static final Logger log = Logger.getLogger(this)

    PolarionCheck(Context context) {
        super(context)
    }

    protected Closure<Probe>[] createProbeClosures() {
        def probeClosures = []
        def installed = isRealPolarionHome(context.configuration.polarionHome)
        probeClosures << {
            def probes = []
            probes << new Probe(name:"polarion.installed", label:"Is Polarion installed?", value:installed)
            if (installed) {
                probes << new Probe(name:"polarion.home", label:"Polarion installation home", value:context.configuration.polarionHome)
            }
            probes
        }
        if (installed) {
            probeClosures.addAll(createPolarionProbeClosures())
        }
        return probeClosures
    }

    def createPolarionProbeClosures() {
        [
            {
                fileContentProbe("polarion.", eclipseProductFile(context.configuration.polarionHome))
            },
            {
                def jvmConfigFile = context.isWindows ?
                        new File(new File(context.configuration.polarionHome, "polarion"), "polarion.ini") :
                        new File(new File(context.configuration.polarionHome, "etc"), "config.sh")
                packFileProbe("polarion.", jvmConfigFile)
            },
            {
                packFileProbe("polarion.", new File(new File(context.configuration.polarionHome, "polarion"), "configuration"))
            },
            {
                if (!context.isWindows) {
                    packFileProbe("polarion.", new File(context.configuration.polarionHome, "etc"))
                } else {
                    null
                }
            },
            {
                packFileProbe("polarion.", new File(new File(new File(context.configuration.polarionHome, "shared"), "polarion"), "configuration"))
            },
            {
                if (!context.isWindows) {
                    packFileProbe("polarion.", new File(new File(context.configuration.polarionHome, "shared"), "etc"))
                } else {
                    null
                }
            },
            {
                packFileProbe("polarion.", new File(new File(context.configuration.polarionHome, "polarion"), "license"))
            },
            {
                fileContentProbe("polarion.", new File(new File(context.configuration.polarionHome, "polarion"), "plugins"))
            },
            {
                def probes = []
                gatherDataDirs().each {
                    def jobsDir = new File(new File(new File(it, "workspace"), "polarion-data"), "jobs")
                    probes << packFileProbe("polarion.logs.jobs.", jobsDir,
                            "logs", { files ->
                                files.findAll { (it.isFile() && "log".equals(it.name)) || (it.isDirectory() && it.parentFile.equals(jobsDir)) }
                            }
                            )
                }
                probes
            },
            {
                def probes = []
                gatherDataDirs().each {
                    def metadataDir = new File(new File(it, "workspace"), ".metadata")
                    probes << packFileProbe("polarion.logs.metadata.", metadataDir,
                            "logs", { files ->
                                files.findAll { it.isFile() && it.name =~ /\.log$/ }
                            }
                            )
                }
                probes
            },
            {
                def probes = []
                gatherLogDirs().each {
                    probes << packFileProbe("polarion.logs.", it, "latest_logs", { files ->
                        def logs = []
                        logs.addAll(findLatestMainLogs(files))
                        logs.addAll(findLatestSpecialLogs(files, "rpc"))
                        def consoleLog = files.find { it.isFile() && (it.name == "console.log") }
                        if (consoleLog != null) {
                            logs << consoleLog
                        }
                        logs
                    }
                    )
                }
                probes
            },
            {
                def probes = []
                gatherLogDirs().each {
                    probes << packFileProbe("polarion.logs.", it, "gc_logs", { files ->
                        def logs = []
                        logs.addAll(findGCLogs(files))
                        logs.addAll(findSpecialGCLogs(files))
                        logs
                    }
                    )
                }
                probes
            },
            {
                packFileProbe("polarion.", new File("/var/log/polarion/polarion.log"))
            },
            {
                packFileProbe("polarion.", new File(new File(context.configuration.polarionHome, "/data/postgres-data"), "postgresql.conf"))
            },
            {
                def probes = []

                def logDir  = new File(new File(context.configuration.polarionHome, "data"), "logs/postgresql")
                probes << packFileProbe("polarion.logs.postgresql.", logDir,
                        "logs", { files -> findPostgreSQLLog(files) }
                        )
                probes
            },
            {
                def probes = []
                possibleApacheConfigDirs().each {
                    probes << packFileProbe("polarion.apache.config.", it, null)
                }
                probes
            },
            {
                def probes = []
                possibleApacheLogDirs().each {
                    probes << packFileProbe("polarion.apache.logs.", it, "latest_logs", { files ->
                        def logs = []
                        files.toList().groupBy {
                            def matcher = (it.name =~ /^(.*log)/)
                            if (matcher) {
                                matcher[0][1]
                            } else {
                                it.name
                            }
                        }.entrySet().each {
                            def logName = it.key
                            def logFiles = it.value
                            def latestLog = logFiles.max { it.lastModified() }
                            if (latestLog != null) {
                                logs << latestLog
                            }
                        }
                        logs
                    }
                    )
                }
                probes
            },
            {
                def probes = []
                gatherDataDirs().each {
                    def metadataDir = new File(it, "svn")
                    probes << packFileProbe("polarion.svn.access.", metadataDir,
                            "access", { files ->
                                files.findAll { it.isFile() && it.name == "access" }
                            }
                            )
                }
                probes
            },
        ]
    }

    def possibleApacheLogDirs() {
        def logDirs = gatherDataDirs().collect {
            new File(new File(it, "logs"), "apache")
        }
        if (!context.isWindows) {
            logDirs << new File("/var/log/apache")
            logDirs << new File("/var/log/apache2")
            logDirs << new File("/var/log/httpd")
            logDirs << new File("/var/log/httpd2")
        }
        logDirs
    }

    def possibleApacheConfigDirs() {
        if (context.isWindows) {
            possibleApacheConfigDirsWindows()
        } else {
            possibleApacheConfigDirsLinux()
        }
    }

    def possibleApacheConfigDirsWindows() {
        def pathPrefixes = File.listRoots()
        def pathSuffixes = ["\\conf"]
        combinePaths(pathPrefixes, EnvironmentCheck.apacheWindowsInstallationPathInfixes, pathSuffixes)
    }

    def possibleApacheConfigDirsLinux() {
        [new File("/etc/httpd"), new File("/etc/httpd2"), new File("/etc/apache"), new File("/etc/apache2"), new File("/usr/local/apache/etc"), new File("/usr/local/apache2/etc"),]
    }

    def Collection<File> findLatestMainLogs(files) {
        def mainLogs = files.findAll { it.name =~ /^log4j-\d.*/ }
        def foundLogs = [] as Set
        foundLogs.addAll(findLatestLogsByStartMode(mainLogs, "normal"))
        foundLogs.addAll(findLatestLogsByStartMode(mainLogs, "reindex"))
        if (foundLogs.isEmpty()) {
            foundLogs.addAll(findLatestLogsByStartMode(mainLogs, null))
        }
        foundLogs
    }

    def findSpecialGCLogs(files) {
        files.findAll { it.name =~ /^gc\.log(?:\.\d(?:\.current)?)?$/ }
    }

    def Collection<File> findGCLogs(files) {
        def gcLogs = files.findAll { it.name =~ /^(?:gc|gc.log)-\d.*/ }
        def foundLogs = [] as Set
        foundLogs.addAll(gcLogs)
        def latestLog = findCurrentLog( foundLogs.max({it.name}) )
        if (latestLog != null) {
            return files.findAll { it.name =~ /^\Q${latestLog.name}\E/ }
        }
        return []
    }

    def Collection<File> findLatestSpecialLogs(files, String name) {
        def logs = files.findAll { it.name =~ /^log4j-${name}-\d.*/ }
        findLatestLogsByStartMode(logs, null)
    }

    def Collection<File> findPostgreSQLLog(files) {
        [files.findAll { it.name =~ /^postgresql-.*\.log$/ }.max{ it.name }]
    }

    def findLatestLogsByStartMode(files, String mode) {
        def latestLog = findCurrentLog(findLogsByStartMode(files, mode).max { it.name })
        if (latestLog != null) {
            return files.findAll { it.name =~ /^\Q${latestLog.name}\E/ }
        }
        return []
    }

    def findLogsByStartMode(files, String mode) {
        if (mode == null) {
            return files
        }
        files.findAll {
            it.withReader { BufferedReader r ->
                def String line
                def int lineNum = 0
                while ((line = r.readLine()) != null) {
                    def matcher = line =~ /\QServer start mode:\E\s*(\S+)/
                    if (matcher) {
                        return matcher[0][1] == mode
                    }
                    if (lineNum++ > 100) {
                        break
                    }
                }
                return false
            }
        }
    }

    def File findCurrentLog(File f) {
        if (f == null) {
            return null
        }
        def matcher = f.name =~ /^(gc\.log-\d+).*$/
        if (matcher) {
            return new File(matcher[0][1])
        }

        matcher = f.name =~ /^(.*\.(?:log|csv)).*$/
        if (matcher) {
            return new File(matcher[0][1])
        }
        return f
    }

    def boolean isRealPolarionHome(File polarionHome) {
        eclipseProductFile(polarionHome).isFile() || polarionDataFolder(polarionHome).isDirectory()
    }

    def File eclipseProductFile(File polarionHome) {
        return new File(new File(polarionHome, "polarion"), ".eclipseproduct")
    }

    def File polarionDataFolder(File polarionHome) {
        return new File(new File(new File(polarionHome, "data"), "workspace"), "polarion-data")
    }
}
