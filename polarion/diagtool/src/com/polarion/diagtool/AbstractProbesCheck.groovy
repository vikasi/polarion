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
package com.polarion.diagtool
import groovy.io.FileType

import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger


abstract class AbstractProbesCheck {

    private static final Logger log = Logger.getLogger(this)

    protected final Context context

    AbstractProbesCheck(Context context) {
        this.context = context
    }

    protected abstract Closure<Probe>[] createProbeClosures();

    def run() {
        Closure<Probe>[] probeClosures = createProbeClosures()
        ProgressMonitor progress = context.newProgressMonitor(probeClosures.length)
        progress.start()
        try {
            probeClosures.each {
                try {
                    def closureReturnValue = it.call()
                    if (closureReturnValue != null) {
                        if (closureReturnValue instanceof Collection || closureReturnValue instanceof Object[]) {
                            closureReturnValue.each {
                                if (it != null) {
                                    context.probeRecorder.recordProbe(it, log)
                                }
                            }
                        } else {
                            context.probeRecorder.recordProbe(closureReturnValue, log)
                        }
                    }
                } catch (Exception e) {
                    log.error("Unexpected error: " + e.message, e)
                    context.errors++
                } finally {
                    progress.tick()
                }
            }
        } finally {
            progress.finish()
        }
    }

    def Probe fileContentProbe(String probeNamePrefix, String fileName) {
        fileContentProbe(probeNamePrefix, new File(fileName))
    }

    def Probe fileContentProbe(String probeNamePrefix, File f) {
        if (f.isDirectory()) {
            def lines = []
            f.eachFile { lines << "${it.name};${it.isDirectory() ? '<DIR>' : it.length()};${new Date(it.lastModified())}" }
            def value = lines.sort().join("\n")
            return new Probe(name:"${probeNamePrefix}${f}", label:"Content of directory ${f}", value:value, stringValue:"\n" + value)
        } else if (f.isFile()) {
            def value = f.text.trim()
            return new Probe(name:"${probeNamePrefix}${f}", label:"Content of file ${f}", value:value, stringValue:"\n" + value)
        }
        return null
    }

    def File getResultsFile(String name) {
        def f = new File(context.resultsDir, name)
        if (f.exists()) {
            f = File.createTempFile(name + "_", "", context.resultsDir)
            f.delete()
        }
        return f
    }

    def Probe packFileProbe(String probeNamePrefix, File f, String resultsName = null, Closure<Collection<File>> filter = null) {
        if (!f.exists()) {
            return null
        }
        def String fullResultsName = f.canonicalPath.replaceAll("[/\\\\:]", "_")
        if (fullResultsName.startsWith("_")) {
            fullResultsName = fullResultsName.substring(1)
        }
        if (resultsName != null) {
            fullResultsName += "_" + resultsName
        }
        def File resultsFile = getResultsFile(fullResultsName)
        def final value = resultsFile.name
        def boolean success = packFileOrDirectory(f, resultsFile, filter)
        if (!success) {
            if (!resultsFile.exists()) {
                value = "<not packed, see above for reasons>"
            } else {
                value += ";<some contained files were not packed, see above for reasons>"
            }
        }
        new Probe(name:"${probeNamePrefix}${f}", label:"Packed ${f} into results", value:value)
    }

    def boolean packDirectory(File src, File dest, Closure<Collection<File>> filter) {
        def boolean success = true
        def files = src.listFiles()
        if (filter != null) {
            files = filter.call(files)
        }
        files.each {
            def recurseDest = new File(dest, it.name)
            success &= packFileOrDirectory(it, recurseDest, filter)
        }
        return success
    }

    def boolean packFileOrDirectory(File src, File dest, Closure<Collection<File>> filter) {
        if (!src.canRead()) {
            log.warn("File ${src} will not be packed, because it is not readable (probably due to insufficient permissions)")
            context.warnings++
            return false
        } else if (FileUtils.isSymlink(src)) {
            log.warn("File ${src} will not be packed, because it is symlink")
            context.warnings++
            return false
        } else if (src.isDirectory()) {
            return packDirectory(src, dest, filter)
        } else if (src.isFile()) {
            return packFile(src, dest)
        } else {
            log.warn("File ${src} will not be packed, because it is not normal file or directory")
            context.warnings++
            return false
        }
    }

    def boolean packFile(File src, File dest) {
        if (!checkFileSize(src)) {
            return false
        }
        copyFile(src, dest)
        return true
    }

    def copyFile(File src, File dest) {
        dest.parentFile.mkdirs()
        src.withInputStream { is ->
            dest.withOutputStream { os -> os << is }
        }
    }

    def checkFileSize(File f) {
        def length = f.length()
        if (length > context.configuration.packedFileSizeThreshold) {
            log.warn("File ${f} is too big to be packed (${length} bytes)")
            context.warnings++
            return false
        }
        return true
    }

    def File[] combinePaths(prefixes, infixes, suffixes) {
        def ret = []
        prefixes.each { prefix ->
            infixes.each { infix ->
                suffixes.each { suffix ->
                    ret << new File(new File(prefix, infix), suffix)
                }
            }
        }
        ret
    }

    def File[] gatherDataDirs() {
        def ret = []
        def mainDataDir = new File(context.configuration.polarionHome, "data")
        if (mainDataDir.isDirectory()) {
            ret << mainDataDir
            def multiInstanceDir = new File(mainDataDir, "multi-instance")
            if (multiInstanceDir.isDirectory()) {
                multiInstanceDir.eachFile(FileType.DIRECTORIES) { ret << it }
            }
            def multiRepositoryDir = new File(mainDataDir, "multirepository")
            if (multiRepositoryDir.isDirectory()) {
                multiRepositoryDir.eachFile(FileType.DIRECTORIES) { ret << it }
            }
        }
        def sharedDataDir = new File(new File(context.configuration.polarionHome, "shared"), "data")
        if (sharedDataDir.isDirectory()) {
            ret << sharedDataDir
        }
        ret
    }

    def File[] gatherLogDirs() {
        def ret = []
        def parentLogDir = new File(new File(context.configuration.polarionHome, "data"), "logs")
        if (parentLogDir.isDirectory()) {
            def mainLogDir = new File(parentLogDir, "main")
            if (mainLogDir.isDirectory()) {
                ret << mainLogDir
            }
            def multiInstanceDir = new File(parentLogDir, "multi-instance")
            if (multiInstanceDir.isDirectory()) {
                multiInstanceDir.eachFile(FileType.DIRECTORIES) { ret << it }
            }
            def multiRepositoryDir = new File(parentLogDir, "multirepository")
            if (multiRepositoryDir.isDirectory()) {
                multiRepositoryDir.eachFile(FileType.DIRECTORIES) { ret << it }
            }
        }
        ret
    }
}
