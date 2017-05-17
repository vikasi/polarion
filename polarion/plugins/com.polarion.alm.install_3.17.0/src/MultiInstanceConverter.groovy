/*
 * Copyright (C) 2004-2012 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2012 Polarion Software
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
import java.util.regex.Matcher
import java.util.regex.Pattern

import com.polarion.alm.install.utils.FileHelper

/**
 *  Converts installation after change of terminology (multirepository -> multi-instance).
 */
public class MultiInstanceConverter {

    private PolarionInstallation installation
    private File backupDir
    private AntBuilder ant

    private boolean isWindows

    // maps old key prefixes to new ones
    private static final Map<String,String> propertiesMap = new HashMap();
    static {
        propertiesMap.put("multirepository.configuration", "multi-instance.configuration")
        propertiesMap.put("autostart.slaves", "autostart.instances")

        propertiesMap.put("slave.id", "instance.id")
        propertiesMap.put("slave.label", "instance.label")
        propertiesMap.put("multirepository.shared.data", "multi-instance.shared.data")
        propertiesMap.put("slave.", "instance.")

        propertiesMap.put("masterPort", "controllerPort")
        propertiesMap.put("masterHostName", "controllerHostName")
        propertiesMap.put("masterBaseURL", "controllerBaseURL")

        propertiesMap.put("multirepository.enabled", "multi-instance.enabled")
        propertiesMap.put("multirepository.configuration", "multi-instance.configuration")
    }

    private final Map<File,File> renameMap = new HashMap()

    public MultiInstanceConverter(PolarionInstallation installation, File backupDir, AntBuilder ant) {
        this.installation = installation
        this.backupDir = backupDir
        this.ant = ant
        isWindows = new OSHelper().isWindows()
    }

    public void convert() {
        convertMultiConfigDir()
        convertDataDir()
        convertScripts()
        convertApacheConfigs()
    }

    // convert polarion/configuration/multirepository (or etc/multirepository)
    private void convertMultiConfigDir() {
        File configDir = installation.getConfigDir();
        File multiConfigDirOld = new File(configDir, "multirepository")
        if (multiConfigDirOld.exists()) {
            // backup
            ant.copy(toDir:backupDir.getAbsolutePath()) {
                fileset(dir:configDir.getAbsolutePath()) { include(name:"multirepository/*") }
            }

            for (File file in multiConfigDirOld.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".properties")) {
                    convertPropertiesFile(file)
                }
            }
            File masterPropsOld = new File(multiConfigDirOld, "_master.properties")
            if (masterPropsOld.exists()) {
                rename(masterPropsOld, "_controller.properties")
            }
            rename(multiConfigDirOld, "multi-instance")
        }
    }

    // must be called after configuration dir is converted
    private void convertDataDir() {
        File multirepoDir = new File(installation.getMasterDataDir(), "multirepository")
        if (multirepoDir.exists()) {
            rename(multirepoDir, "multi-instance")
        }

        File multiLogsDir = new File(installation.getMasterDataDir(), "logs/multirepository")
        if (multiLogsDir.exists()) {
            rename(multiLogsDir, "multi-instance")
        }
    }

    // convert polarion/*slave.bat
    private void convertScripts() {
        // throw them away, new ones will be installed
        ant.move(toDir:backupDir.getAbsolutePath()) {
            fileset(dir:installation.getInstallDir().getAbsolutePath()) { include(name:"polarion/*slave.bat") }
        }
    }

    private void convertApacheConfigs() {
        File apacheCfgDir = installation.getPolarionApacheCfgDir()
        for (File file in apacheCfgDir.listFiles()) {
            String fileName = file.getName()
            if (file.isFile() && (fileName.equals("polarion.multirepository.conf") || fileName.endsWith(".slave"))) {
                ant.copy(todir:backupDir, file:file.getAbsolutePath())

                if (fileName.equals("polarion.multirepository.conf")) {
                    convertApacheCfgFile(file)
                    rename(file, "polarion.multi-instance.conf")
                }
                if (fileName.endsWith(".slave")) {
                    convertApacheCfgFile(file)
                    rename(file, fileName.substring(0, fileName.length() - ".slave".length()) + ".instance")
                }
            }
        }
        restartApache()
    }

    private void restartApache() {
        echo "Restarting Apache..."
        ApacheService apache = new ApacheService(installation)
        apache.stop()
        apache.start()
    }

    private void convertPropertiesFile(File source) {
        echo "Checking file " + getFileLabel(source)
        convertFile(source, { line, lines ->
            return convertPropertiesFileLine(line, lines)
        })
    }

    // returns true if the line was changed
    private boolean convertPropertiesFileLine(String line, List<String> lines) {
        boolean changed = false
        for (Map.Entry<String,String> entry in propertiesMap) {
            String prefixOld = entry.getKey()
            String prefixNew = entry.getValue()
            if (line.startsWith(prefixOld)) {
                line = prefixNew + line.substring(prefixOld.length())
                changed = true
            }
        }
        if (line.contains("\$[")) {
            for (Map.Entry<String,String> entry in propertiesMap) {
                String prefixOld = entry.getKey()
                String prefixNew = entry.getValue()
                if (line.contains("\$["+prefixOld)) {
                    line = line.replace("\$["+prefixOld, "\$["+prefixNew)
                    changed = true
                }
            }
        }
        Matcher matcher = Pattern.compile("(\\\\|/)multirepository(\\\\|/)").matcher(line)
        if (matcher.find()) {
            line = matcher.replaceAll("\$1multi-instance\$2")
            changed = true
        }

        lines.add(line)
        return changed
    }

    private void convertApacheCfgFile(File file) {
        echo "Checking file " + getFileLabel(file)
        convertFile(file, { line, lines ->
            return convertApacheCfgFileLine(line, lines)
        })
    }

    // returns true if the line was changed
    private boolean convertApacheCfgFileLine(String line, List<String> lines) {
        boolean changed = false

        // fix references to renamed files
        FileHelper.FileReferenceUpdater referenceUpdater = new FileHelper.FileReferenceUpdater()
        referenceUpdater.setCaseSensitive(!isWindows)
        for (Map.Entry<File,File> entry in renameMap.entrySet()) {
            File source = entry.getKey()
            File target = entry.getValue()
            line = referenceUpdater.replaceFileReference(line, source, target)
            changed = changed || referenceUpdater.modified();
        }

        if (line.startsWith("Include") && line.contains(".slave")) {
            line = line.replace(".slave", ".instance")
            changed = true
        }
        lines.add(line)
        return changed
    }

    private void rename(File file, String newName) {
        File target = new File(file.getParentFile(), newName)
        echo "Renaming " + getFileLabel(file) + " to " + getFileLabel(target)
        FileHelper.rename(file, target)
        renameMap.put(file, target)
    }

    private String getFileLabel(File file) {
        return file.getAbsoluteFile().getCanonicalFile()
    }

    private void convertFile(File source, lineConverter) {
        List<String> lines = new ArrayList<String>()
        boolean changed = false
        source.eachLine { line ->
            boolean lineChanged = lineConverter(line, lines)
            changed = changed || lineChanged
        }
        if (changed) {
            echo "Updating file " + getFileLabel(source)
            def tempFile = new File(source.getParentFile(), source.getName() + ".temp")
            if (tempFile.exists()) {
                FileHelper.delete(tempFile)
            }
            for (String line in lines) {
                tempFile.append(line + System.getProperty("line.separator"))
            }
            FileHelper.delete(source)
            FileHelper.rename(tempFile, source)
        }
    }

    protected void echo(String message) {
        if(message == "") {
            // echo would not produce empty line
            println ""
        } else {
            ant.echo message
        }
    }

}
