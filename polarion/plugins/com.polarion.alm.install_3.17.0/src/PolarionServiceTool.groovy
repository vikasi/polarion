/*
 * Copyright (C) 2004-2010 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2010 Polarion Software
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

import com.polarion.alm.install.utils.FileHelper

public class PolarionServiceManager {

    private static final String SERVICE_NAME = "Polarion"

    private PolarionInstallation install
    private boolean debug
    private AntHelper antHelper

    public PolarionServiceManager() {
        antHelper = new AntHelper()
    }

    public void setDebug(boolean debug) {
        this.debug = debug
    }

    public void setInstallDir(File installDir) {
        install = new PolarionInstallation(installDir)
    }

    public int installService() {
        File iniFile = new File(install.getInstallDir(), "polarion/polarion.ini")
        if (!iniFile.exists()) {
            throw new RuntimeException("File polarion.ini does not exist: "+iniFile.getAbsolutePath())
        }
        PolarionIniFile iniConf = new PolarionIniFile(iniFile)
        File vmFile = iniConf.getVM()
        if (vmFile == null) {
            throw new RuntimeException("Required argument -vm not found in polarion.ini: "+iniFile.getAbsolutePath())
        }
        File vmDllFile = getVMDllFile(vmFile)
        if (vmDllFile == null) {
            throw new RuntimeException("File jvm.dll not found for VM: "+vmFile.getAbsolutePath())
        }

        File startupLib = new File(install.getInstallDir(), "polarion/startup.jar")
        String classpath = startupLib.getAbsolutePath()

        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        File dataDir = install.getDataDir(null)
        File logsDir = new File(dataDir, "logs")

        List<String> args = new ArrayList<String>()
        args.add("-install")
        args.add(SERVICE_NAME)
        args.add(vmDllFile.getAbsolutePath())
        args.addAll(iniConf.getVMArgs())
        args.add("-Djava.class.path="+classpath)
        args.add("-start")
        args.add("org.eclipse.core.launcher.Main")
        args.add("-params")
        args.addAll(iniConf.getProgramArgs())
        args.add("-stop")
        args.add("com.polarion.platform.shutdown.client.ShutdownServiceClient")
        args.add("-params")
        args.add(install.getControlHostname())
        args.add(install.getControlPort())
        args.add("shutdown")
        args.add("-out")
        args.add(new File(logsDir, "service.out").getAbsolutePath())
        args.add("-err")
        args.add(new File(logsDir, "service.err").getAbsolutePath())
        args.add("-current")
        args.add(dataDir.getAbsolutePath())
        args.add("-path")
        args.add(vmFile.getParentFile().getAbsolutePath())
        args.add("-overwrite")
        args.add("-description")
        args.add("\"Polarion Server\"")

        println String.format("Installing service %s...", SERVICE_NAME)
        File javaServiceFile = getJavaServiceFile()
        if (debug) {
            println "Executing "+javaServiceFile.getAbsolutePath()
            println "with arguments "+args
        }
        antHelper.getBuilder().exec(executable:javaServiceFile.getAbsolutePath(),outputproperty:outputProp,resultproperty:resultProp) {
            for (String a in args) {
                arg(value:a)
            }
        }
        //Change the path to include quotes
		("SC CONFIG "+SERVICE_NAME+" binPath= \"\\\""+javaServiceFile.getAbsolutePath()+"\"\\\"").execute()
        String result = antHelper.getProperty(resultProp)
        if (result != "0") {
            println ""
            println antHelper.getProperty(outputProp)
            println "FAILED"
            return 1
        } else {
            println String.format("Service %s successfully installed.", SERVICE_NAME)
            return 0
        }
    }

    public int uninstallService() {
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()

        List<String> args = new ArrayList<String>()
        args.add("-uninstall")
        args.add(SERVICE_NAME)

        println String.format("Uninstalling service %s...", SERVICE_NAME)
        File javaServiceFile = getJavaServiceFile()
        if (debug) {
            println "Executing "+javaServiceFile.getAbsolutePath()
            println "with arguments "+args
        }
        antHelper.getBuilder().exec(executable:javaServiceFile.getAbsolutePath(),outputproperty:outputProp,resultproperty:resultProp) {
            for (String a in args) {
                arg(value:a)
            }
        }
        String result = antHelper.getProperty(resultProp)
        if (result != "0") {
            println ""
            println antHelper.getProperty(outputProp)
            println "FAILED"
            return 1
        } else {
            println String.format("Service %s successfully uninstalled.", SERVICE_NAME)
            return 0
        }
    }

    private File getJavaServiceFile() {
        File polarionService = new File(install.getInstallDir(), "polarion/polarion_service.exe")
        if (polarionService.exists()) {
            // we replace the service file everytime
            antHelper.getBuilder().delete(file:polarionService.getAbsolutePath())
        }
        File resourceFile = install.getPluginFile(PolarionInstallation.PLUGIN_ID_INSTALL, "resources/polarion_service.exe")
        FileHelper.copyFile(resourceFile, polarionService)

        return polarionService
    }

    private File getVMDllFile(File javaExeFile) {
        // javaExeFile = JAVA/bin/java.exe
        File javaDir = javaExeFile.getParentFile().getParentFile()
        // bundled Java: JAVA/bin/server/jvm.dll
        File vmDllFile = new File(javaDir, "bin/server/jvm.dll")
        if (!vmDllFile.exists()) {
            // standard SDK: JAVA/jre/bin/server/jvm.dll
            vmDllFile = new File(javaDir, "jre/bin/server/jvm.dll")
        }
        return vmDllFile.exists() ? vmDllFile : null
    }

    private File getInstalledPlugin(String pluginId) {
        File pluginsDir = new File(installDir, "eclipse"+File.separator+"plugins")
        File plugin = findPlugin(pluginsDir, pluginId)
        if (plugin == null) {
            File dropinsDir = new File(installDir, "eclipse"+File.separator+"dropins"+File.separator+"plugins")
            plugin = findPlugin(dropinsDir, pluginId)
        }
        return plugin
    }

    private File findPlugin(File pluginsDir, String pluginId) {
        File[] files = pluginsDir.listFiles()
        for (File file : files) {
            String name = file.getName()
            if (name.equals(pluginId) || name.startsWith(pluginId+"_")) {
                return file
            }
        }
    }

}

public class PolarionIniFile {

    private List<String> programArgs
    private List<String> vmArgs
    private File vmFile

    public PolarionIniFile(File iniFile) {
        programArgs = new ArrayList<String>()
        vmArgs = new ArrayList<String>()
        boolean inVmArgs = false
        List<String> lines = iniFile.readLines()
        for (Iterator it = lines.iterator();it.hasNext();) {
            String line = it.next()
            if (!inVmArgs) {
                if (line.equals("-vm")) {
                    if (it.hasNext()) {
                        vmFile = new File(it.next())
                    }
                } else if (line.equals("-vmargs")) {
                    inVmArgs = true
                } else {
                    programArgs.add(line)
                }
            } else {
                vmArgs.add(line)
            }
        }
    }

    public List<String> getProgramArgs() {
        return programArgs
    }

    public List<String> getVMArgs() {
        return vmArgs
    }

    public File getVM() {
        return vmFile
    }

}

////////////////////////////
// POLARION SERVICE TOOL //
////////////////////////////

public void printHelp(String cmd) {
    if (cmd == null) {
        cmd = "..."
    }
    /////////--------------------------------------------------------------------------------
    println ""
    println "Installs or uninstalls the Polarion service. Installed service is configured"
    println "with parameters read from polarion.ini. To reflect changes in polarion.ini"
    println "it is necessary to reinstall the Polarion service."
    println ""
    println "On Windows Vista or newer this tool must be run as administrator."
    println ""
    println "Usage:"
    println "  "+cmd+" [-install|-uninstall]"
    println "  "+cmd+" -help"
    println ""
    println "Options:"
    println "  -install   : install the Polarion service"
    println "  -uninstall : uninstall the Polarion service"
    println "  -help      : only print help"
}

CommandLine cli = new CommandLine(args, [], ["-help", "-debug", "-install", "-uninstall"])
String installDir = cli.getInstallDir()
if (installDir == null) {
    println ""
    println "ERROR: Missing argument "+CommandLine.ARG_INSTALL_DIR
    System.exit(1)
}
if (cli.hasUserSwitch("-help")) {
    printHelp(cli.getCommand())
    System.exit(0)
}

boolean debug = cli.hasUserSwitch("-debug")
boolean install = cli.hasUserSwitch("-install")
boolean uninstall = cli.hasUserSwitch("-uninstall")

if ((install && uninstall) || (!install && !uninstall)) {
    println ""
    println "ERROR: Exactly one of arguments -install and -uninstall is required"
    printHelp(cli.getCommand())
    System.exit(1)
}

try {
    def PolarionServiceManager serviceMgr = new PolarionServiceManager()
    serviceMgr.setInstallDir(new File(installDir))
    serviceMgr.setDebug(debug)
    int exitCode
    if (install) {
        exitCode = serviceMgr.installService()
    } else {
        exitCode = serviceMgr.uninstallService()
    }
    System.exit(exitCode)
} catch (Exception e) {
    if (debug) {
        e.printStackTrace()
    }
    println ""
    println "ERROR: "+e.getMessage()+"\n\nGet support at http://www.polarion.com/techsupport if the problem persists"
    System.exit(1)
}
