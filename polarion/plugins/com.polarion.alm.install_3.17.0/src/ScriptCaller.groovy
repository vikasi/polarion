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

/*
 * ///////////////////
 * // SCRIPT CALLER //
 * ///////////////////
 *
 * Purpose of this script is to set the classpath and call the target script.
 *
 * First argument is name of the target script,
 * second argument is path to folder with plugins,
 * the rest of arguments are passed to the target script.
 */

if (args.length < 2) {
    println "ERROR: Unexpected number of arguments"
    System.exit(1)
}

setProperties()
setClasspath()
runTargetScript()

private void setProperties() {
    if (System.getProperty("svnkit.library.gnome-keyring.enabled") == null) {
        // see DPP-51568 - Polarion crashes on SIGSEGV during runtime with SVNKit 1.8
        System.setProperty("svnkit.library.gnome-keyring.enabled", Boolean.FALSE.toString())
    }
}

private void runTargetScript() {
    String scriptName = args[0]
    String[] scriptArgs = new String[args.length - 2]
    for(int i = 2; i < args.length; i++) {
        scriptArgs[i - 2] = args[i]
    }
    run(new File(scriptName), scriptArgs)
}

private setClasspath() {
    File pluginsDir = new File(args[1])
    addPluginJarsToClasspath(pluginsDir, "com.polarion.alm.install")
    addPluginFolderToClasspath(pluginsDir, "com.polarion.alm.install", "src")
    addPluginJarsToClasspath(pluginsDir, "org.apache.ant")
    addPluginJarsToClasspath(pluginsDir, "org.tmatesoft.svnkit")
    addPluginJarsToClasspath(pluginsDir, "org.tmatesoft.sqljet")
    addPluginJarsToClasspath(pluginsDir, "com.polarion.qcentre")
    addPluginJarsToClasspath(pluginsDir, "com.polarion.core.util")
    addPluginJarsToClasspath(pluginsDir, "org.apache.log4j")
}

private void addPluginFolderToClasspath(File pluginsDir, String pluginId, String path) {
    File pluginFile = findPlugin(pluginsDir, pluginId)
    if (pluginFile != null && pluginFile.isDirectory()) {
        File file = new File(pluginFile, path)
        addToClasspath(file)
    }
}

private void addPluginJarsToClasspath(File pluginsDir, String pluginId) {
    File pluginFile = findPlugin(pluginsDir, pluginId)
    if (pluginFile != null) {
        if (pluginFile.isFile()) {
            if (isJar(pluginFile)) {
                addToClasspath(pluginFile)
            }
        } else {
            // we do not add all plugin libraries, only jars found directly
            // in the plugin folder or in subfolder 'lib'
            addDirJarsToClasspath(pluginFile)
            File libDir = new File(pluginFile, "lib")
            if (libDir.isDirectory()) {
                addDirJarsToClasspath(libDir)
            }
        }
    }
}

private void addDirJarsToClasspath(File dir) {
    File[] files = dir.listFiles()
    for (File file : files) {
        if (isJar(file)) {
            addToClasspath(file)
        }
    }
}

private void addToClasspath(File libFile) {
    this.getClass().classLoader.rootLoader.addURL(libFile.toURI().toURL())
}

private boolean isJar(File file) {
    return file.isFile() && file.getName().toLowerCase().endsWith(".jar")
}

private File findPlugin(File pluginsDir, String pluginId) {
    File[] files = pluginsDir.listFiles()
    for (File file : files) {
        String name = file.getName()
        if (name.equals(pluginId) || name.startsWith(pluginId+"_")) {
            return file
        }
    }
    return null
}
