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

import java.lang.management.ManagementFactory

import org.apache.log4j.Logger

import com.polarion.diagtool.AbstractProbesCheck
import com.polarion.diagtool.Context
import com.polarion.diagtool.Probe

class EnvironmentCheck extends AbstractProbesCheck {

    private static final Logger log = Logger.getLogger(this)

    EnvironmentCheck(Context context) {
        super(context)
    }

    protected Closure<Probe>[] createProbeClosures() {
        def probeClosures = createJVMProbeClosures()
        if (context.isWindows) {
            probeClosures.addAll(createWindowsProbeClosures())
        } else {
            probeClosures.addAll(createLinuxProbeClosures())
        }
        probeClosures.addAll(createApacheProbeClosures())
        probeClosures.addAll(createSubversionProbeClosures())
        probeClosures.addAll(createPostgreSQLProbeClosures())
        return probeClosures
    }

    def createJVMProbeClosures() {
        [
            {
                def probes = []
                System.properties.entrySet().sort{ it.key }.each {
                    def name = it.key
                    def value = it.value
                    probes << new Probe(name:"environment.jvm.systemProperty.$name", label:"JVM system property $name", value:value)
                }
                probes
            },
            {
                def probes = []
                System.env.entrySet().sort{ it.key }.each {
                    def name = it.key
                    def value = it.value
                    probes << new Probe(name:"environment.jvm.os.environmentVariable.$name", label:"OS environment variable $name (according to JVM)", value:value)
                }
                probes
            },
            {
                new Probe(name:"environment.jvm.os.name", label:"OS name (according to JVM)", value:ManagementFactory.operatingSystemMXBean.name)
            },
            {
                new Probe(name:"environment.jvm.os.arch", label:"OS architecture (according to JVM)", value:ManagementFactory.operatingSystemMXBean.arch)
            },
            {
                new Probe(name:"environment.jvm.os.version", label:"OS version (according to JVM)", value:ManagementFactory.operatingSystemMXBean.version)
            },
            {
                new Probe(name:"environment.jvm.availableProcessors", label:"Number of processors (according to JVM)", value:ManagementFactory.operatingSystemMXBean.availableProcessors)
            },
            {
                // totalPhysicalMemorySize is available only through com.sun.management.OperatingSystemMXBean
                // will return at max 4 GB when running with 32-bit Java
                def totalMemory = ManagementFactory.operatingSystemMXBean.totalPhysicalMemorySize
                new Probe(name:"environment.jvm.totalPhysicalMemorySize", label:"Total physical memory size (according to JVM)", value:totalMemory, stringValue:formatBytes(totalMemory))
            },
            {
                def probes = []
                def roots = File.listRoots()
                if (roots != null) {
                    roots.each {
                        probes << totalSpaceProbe(it)
                        probes << freeSpaceProbe(it)
                        probes << usableSpaceProbe(it)
                    }
                }
                probes
            },
            {
                def value = null
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost()
                    value = inetAddress.getHostName() + " (" + inetAddress.getHostAddress() + ")"
                } catch (UnknownHostException e) {
                    log.error("Unable to determine local host name: ${e.message}", e)
                    context.errors++
                }
                new Probe(name:"environment.jvm.localhost", label:"Local host name (according to JVM)", value:value)
            },
        ]
    }

    def createWindowsProbeClosures() {
        [
            { wmicProbe("OS") },
            { wmicProbe("COMPUTERSYSTEM") },
            { wmicProbe("LOGICALDISK") },
        ]
    }

    def createLinuxProbeClosures() {
        [
            {
                new Probe(name:"environment.linux.uname", label:"'uname -a' output", value:uname("-a"))
            },
            {
                def dist = detectLinuxDistribution()
                def probes = []
                probes << new Probe(name:"environment.linux.distribution.name", label:"Linux distribution name", value:dist.name)
                probes << new Probe(name:"environment.linux.distribution.release", label:"Linux distribution release", value:dist.release)
                probes
            },
            { fileContentProbe("/proc/cpuinfo") },
            { fileContentProbe("/proc/meminfo") },
            {
                new Probe(name:"environment.linux.df", label:"'df' output", value:exec("df"))
            },
        ]
    }

    def Probe wmicProbe(String alias) {
        def value = wmic(alias)
        new Probe(name:"environment.windows.wmic.${alias}", label:"WMIC ${alias} output", value:value, stringValue:"\n" + value)
    }

    def Probe totalSpaceProbe(File f) {
        return diskSpaceProbe(f, "Total", f.totalSpace)
    }

    def Probe freeSpaceProbe(File f) {
        return diskSpaceProbe(f, "Free", f.freeSpace)
    }

    def Probe usableSpaceProbe(File f) {
        return diskSpaceProbe(f, "Usable", f.usableSpace)
    }

    def Probe diskSpaceProbe(File f, String name, long bytes) {
        new Probe(name:"environment.jvm.disk.${f}.${name.toLowerCase()}Space", label:"${name} disk space for ${f} (according to JVM)", value:bytes, stringValue:formatBytes(bytes))
    }

    def Probe fileContentProbe(String fileName) {
        fileContentProbe("environment.", fileName)
    }

    def createApacheProbeClosures() {
        [
            {
                def probes = []
                def binaries = (context.isWindows) ? possibleApacheBinariesWindows() : possibleApacheBinariesLinux()
                binaries.each { binary ->
                    if (binary.isFile()) {
                        try {
                            def output = exec("${binary} -V")
                            probes << new Probe(name:"environment.apache.${binary}", label:"Found Apache installation at ${binary}", value:output, stringValue:"\n" + output)
                        } catch (Exception e) {
                            log.error("Unable to execute Apache binary ${binary}: ${e.message}", e)
                            context.errors++
                        }
                    }
                }
                probes
            }
        ]
    }

    def createSubversionProbeClosures() {
        [
            {
                def probes = []
                def modules = (context.isWindows) ? possibleSubversionModulesWindows() : possibleSubversionModulesLinux()
                modules.each { module ->
                    if (module.isFile()) {
                        try {
                            def version = findSubversionModuleVersion(module)
                            probes << new Probe(name:"environment.subversion.${module}", label:"Found Subversion module at ${module}", value:version)
                        } catch (Exception e) {
                            log.error("Unable to inspect Subversion module ${module}: ${e.message}", e)
                            context.errors++
                        }
                    }
                }
                probes
            }
        ]
    }

    def createPostgreSQLProbeClosures() {
        [
            {
                def probes = []
                if(context.isWindows){
                    def binaries = possiblePostgreSQLBinariesWindows()
                    binaries.each { binary ->
                        if( binary.exists() ){
                            checkPostgreSql("${binary.path} --version", probes, binary.path)
                        }
                    }
                }else{
                    def psqlPaths = execWithLinesReturn('find /usr -iname psql -print')
                    psqlPaths.each { psqlPath ->
                        def file = new File(psqlPath.trim())
                        if(file.isFile() && file.canExecute()){
                            checkPostgreSql("${psqlPath} --version", probes, psqlPath)
                        }
                    }
                }
                probes
            }
        ]
    }

    def checkPostgreSql(command, probes, suffix){
        try {
            def output = exec(command)
            probes << new Probe(name:"environment.postgresql.version.${suffix}", label:"Found PostgreSQL installation at ${suffix}", value:output)
        } catch (Exception e) {
            log.error("Unable to find PostgreSQL version: ${e.message}", e)
            context.errors++
        }
    }

    public static final apacheWindowsInstallationPathInfixes = [
        "\\Polarion\\bundled\\apache",
        "\\Program Files\\Apache Software Foundation\\Apache2.0",
        "\\Program Files (x86)\\Apache Software Foundation\\Apache2.0",
        "\\Program Files\\Apache Software Foundation\\Apache2.2",
        "\\Program Files (x86)\\Apache Software Foundation\\Apache2.2",
        "\\Program Files\\Apache Software Foundation\\Apache2.4",
        "\\Program Files (x86)\\Apache Software Foundation\\Apache2.4",
    ]

    def File[] possibleApacheBinariesWindows() {
        def pathPrefixes = File.listRoots()
        def pathSuffixes = ["\\bin\\httpd.exe"]
        return combinePaths(pathPrefixes, apacheWindowsInstallationPathInfixes, pathSuffixes)
    }

    def File[] possibleSubversionModulesWindows() {
        def pathPrefixes = File.listRoots()
        def pathSuffixes = ["\\modules\\mod_dav_svn.so"]
        return combinePaths(pathPrefixes, apacheWindowsInstallationPathInfixes, pathSuffixes)
    }

    def File[] possibleApacheBinariesLinux() {
        def pathPrefixes = [new File("/")]
        def pathInfixes = ["/bin", "/usr/bin", "/sbin", "/usr/sbin", "/usr/local/apache/bin", "/usr/local/apache/sbin", "/usr/local/apache2/bin", "/usr/local/apache2/sbin",]
        def pathSuffixes = ["apache", "apache2", "httpd", "httpd2",]
        return combinePaths(pathPrefixes, pathInfixes, pathSuffixes)
    }

    def File[] possibleSubversionModulesLinux() {
        def pathPrefixes = [new File("/usr/lib"), new File("/usr/lib64"), new File("/usr/local"),]
        def pathInfixes = ["/apache", "/apache2", "/httpd", "/httpd2",]
        def pathSuffixes = ["mod_dav_svn.so", "modules/mod_dav_svn.so",]
        return combinePaths(pathPrefixes, pathInfixes, pathSuffixes)
    }

    def File[] possiblePostgreSQLBinariesWindows() {
        def pathPrefixes = [new File("${context.configuration.polarionHome}\\bundled")]
        def pathInfixes = ["\\postgre", "\\postgres"]
        def pathSuffixes = ["\\bin\\psql.exe"]
        return combinePaths(pathPrefixes, pathInfixes, pathSuffixes)
    }

    def findSubversionModuleVersion(File f) {
        (f.text =~ /SVN\/(?:\d|\.)+/)[0]
    }

    def exec(String command) {
        def p = command.execute()
        def out = p.text
        p.waitFor()
        return out.replace("\r", "").trim()
    }

    def execWithLinesReturn(String command) {
        def p = command.execute()
        def out = p.text
        p.waitFor()
        return out.readLines()
    }

    def wmic(String command) {
        // wmic on Windows XP requires <nul hack, see http://stackoverflow.com/questions/9190805/
        // wmic on Windows XP with Java 1.6.0_45 requires that wmic is executed via batch file
        // playing it safe on other Windows as well
        return exec("cmd /c \"${context.configuration.home}\\scripts\\wmic.bat\" ${command}")
    }

    def uname(String option) {
        return exec("uname ${option}")
    }

    def linuxVersionFiles = [
        "/etc/debian_version" : "Debian",
        "/etc/redhat-release" : "RedHat/CentOS",
        "/etc/SuSE-release" : "SuSE",
    ]

    def LinuxDistribution detectLinuxDistribution() {
        def LinuxDistribution ret = new LinuxDistribution()
        def File lsbRelease = new File("/etc/lsb-release")
        if (lsbRelease.isFile()) {
            lsbRelease.eachLine {
                def distribDescriptionMatcher = (it =~ /DISTRIB_DESCRIPTION="?([^"]+)"?/)
                if (distribDescriptionMatcher.matches()) {
                    ret.name = distribDescriptionMatcher[0][1]
                } else {
                    def distribReleaseMatcher = (it =~ /DISTRIB_RELEASE="?([^"]+)"?/)
                    if (distribReleaseMatcher.matches()) {
                        ret.release = distribReleaseMatcher[0][1]
                    }
                }
            }
        }
        if (ret.name == null) {
            linuxVersionFiles.each {
                def fileName = it.key
                def distribution = it.value
                def File file = new File(fileName)
                if (file.isFile()) {
                    ret.name = distribution
                    ret.release = file.text.trim()
                }
            }
        }
        if (ret.name == null) {
            ret.name = uname("")
        }
        return ret
    }

    def String formatBytes(def bytes) {
        return ((long)(Long.valueOf(bytes) / 1024 / 1024)) + " MB"
    }
}

final class LinuxDistribution {
    String name
    String release
}
