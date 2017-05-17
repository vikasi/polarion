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

public class ApacheService {

    private static final String POLARION_APACHE_SERVICE = "Apache2Polarion"

    private boolean debug

    private AntHelper antHelper
    private AntBuilder ant
    private boolean isWindows
    private boolean isDebian
    private boolean isRedHat
    private boolean isSUSE
    private PolarionInstallation polarionInstall;

    public ApacheService(PolarionInstallation polarionInstall) {
        this.polarionInstall = polarionInstall
        antHelper = new AntHelper()
        ant = antHelper.getBuilder()
        OSHelper osHelper = new OSHelper()
        isWindows = osHelper.isWindows()
        isDebian = osHelper.isDebian()
        isRedHat = osHelper.isRedHat()
        isSUSE = osHelper.isSUSE()
    }

    public void setDebug(boolean debug) {
        this.debug = debug
    }

    /**
     * Start Polarion Apache if it is not running.
     */
    public void start() {
        if (isWindows) {
            ServiceStatus status = getServiceStatus(POLARION_APACHE_SERVICE)
            if (status != null) {
                if (!status.isRunning()) {
                    startService(POLARION_APACHE_SERVICE)
                } else {
                    println "Service is running."
                }
            } else {
                String pid = getPID()
                if (!isProcessRunning(pid)) {
                    File apache = new File(polarionInstall.getInstallDir(), "bundled/apache/bin/httpd.exe")
                    println "Starting process "+apache.getAbsolutePath()
                    ant.exec(executable:apache.getAbsolutePath(),spawn:"true")
                    boolean running = false
                    while (!running) {
                        print "."
                        Thread.sleep(1000)
                        running = isProcessRunning(getPID())
                    }
                    println ""
                    println "Process started."
                } else {
                    println "Process is running."
                }
            }
        } else {
            callApacheService("start")
        }
    }

    /**
     * Stop Polarion Apache if it is not stopped.
     */
    public void stop() {
        if (isWindows) {
            ServiceStatus status = getServiceStatus(POLARION_APACHE_SERVICE)
            if (status != null) {
                if (!status.isStopped()) {
                    stopService(POLARION_APACHE_SERVICE)
                } else {
                    println "Service is stopped."
                }
            } else {
                String pid = getPID()
                if (isProcessRunning(pid)) {
                    killProcess(pid)
                } else {
                    println "Process is not running."
                }
            }
        } else {
            callApacheService("stop")
        }
    }

    // LINUX
    private void callApacheService(String cmd) {
        if (isDebian) {
            ant.exec(executable:"/etc/init.d/apache2") { arg(value:cmd) }
        } else if (isRedHat) {
            ant.exec(executable:"service") {
                arg(value:"httpd")
                arg(value:cmd)
            }
        } else if (isSUSE) {
            ant.exec(executable:"rcapache2") { arg(value:cmd) }
        } else {
            throw new RuntimeException("Unsupported operating system - don't know how to start/stop Apache")
        }
    }

    // WINDOWS
    private String getPID() {
        File pidFile = new File(polarionInstall.getDataDir(null), "logs/apache/httpd.pid")
        String pid = null
        if (pidFile.exists()) {
            String prop = antHelper.newPropertyName()
            ant.loadfile(property:prop,srcfile:pidFile.getAbsolutePath())
            pid = antHelper.getProperty(prop).trim()
        }
        return pid
    }

    // WINDOWS
    private boolean isProcessRunning(String pid) {
        if (pid == null) {
            return false
        }
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        ant.exec(executable:"tasklist",outputproperty:outputProp,resultproperty:resultProp) {
            arg(value:"/NH")
            arg(value:"/FO")
            arg(value:"TABLE")
            arg(value:"/FI")
            arg(value:"\"PID eq "+pid+"\"")
        }
        String result = antHelper.getProperty(resultProp)
        String output = antHelper.getProperty(outputProp)
        if (result != "0") {
            println output
            throw new RuntimeException("Failed to get status of process "+pid+": error code "+result)
        }
        return output.contains(pid)
    }

    // WINDOWS
    private void killProcess(String pid) {
        println "Killing process "+pid;
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        ant.exec(executable:"taskkill",outputproperty:outputProp,resultproperty:resultProp) {
            arg(value:"/F")
            arg(value:"/T")
            arg(value:"/PID")
            arg(value:pid)
        }
        String result = antHelper.getProperty(resultProp)
        if (result != "0") {
            println antHelper.getProperty(outputProp)
            throw new RuntimeException("Failed to get status of process "+pid+": error code "+result)
        }
        boolean killed = false
        while (!killed) {
            print "."
            Thread.sleep(1000)
            killed = !isProcessRunning(pid)
        }
        println ""
        println "Process killed."
    }

    // WINDOWS
    private void startService(String service) {
        println "Starting service "+service;
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        ant.exec(executable:"sc",outputproperty:outputProp,resultproperty:resultProp) {
            arg(value:"start")
            arg(value:POLARION_APACHE_SERVICE)
        }
        String result = antHelper.getProperty(resultProp)
        if (result != "0") {
            if (debug) {
                println antHelper.getProperty(outputProp)
            }
            if (result == "5") {
                throw new RuntimeException("Failed to start service "+service+": access was denied."
                + " Please make sure current user has permission to start this service."
                + " On Windows Vista or newer please run this command as administrator.")
            }
            if (!debug) { // unknown error, print output even if debug is off
                println antHelper.getProperty(outputProp)
            }
            throw new RuntimeException("Failed to start service "+service+": sc start command returned error code "+result)
        }
        boolean running = false
        boolean stopped = false
        while (!running && !stopped) {
            print "."
            Thread.sleep(1000)
            ServiceStatus status = getServiceStatus(POLARION_APACHE_SERVICE)
            running = status.isRunning()
            stopped = status.isStopped()
        }
        if (stopped) {
            throw new RuntimeException("Failed to start service "+service+": service refused to start")
        }
        println ""
        println "Service started."
    }

    // WINDOWS
    private void stopService(String service) {
        println "Stopping service "+service;
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        ant.exec(executable:"sc",outputproperty:outputProp,resultproperty:resultProp) {
            arg(value:"stop")
            arg(value:POLARION_APACHE_SERVICE)
        }
        String result = antHelper.getProperty(resultProp)
        if (result != "0") {
            if (debug) {
                println antHelper.getProperty(outputProp)
            }
            if (result == "5") {
                throw new RuntimeException("Failed to stop service "+service+": access was denied."
                + " Please make sure current user has permission to stop this service."
                + " On Windows Vista or newer please run this command as administrator.")
            }
            if (!debug) { // unknown error, print output even if debug is off
                println antHelper.getProperty(outputProp)
            }
            throw new RuntimeException("Failed to stop service "+service+": error code "+result)
        }
        boolean stopped = false
        while (!stopped) {
            print "."
            Thread.sleep(1000)
            stopped = getServiceStatus(POLARION_APACHE_SERVICE).isStopped()
        }
        println ""
        println "Service stopped."
    }

    // WINDOWS
    private ServiceStatus getServiceStatus(String service) {
        String outputProp = antHelper.newPropertyName()
        String resultProp = antHelper.newPropertyName()
        ant.exec(executable:"sc",outputproperty:outputProp,resultproperty:resultProp) {
            arg(value:"query")
            arg(value:service)
        }
        String result = antHelper.getProperty(resultProp)
        String output = antHelper.getProperty(outputProp)
        if (debug) {
            println output
            println "Exit code: "+result
        }
        if ((result != "0") || output.contains("FAILED")) {
            // When service is not installed, exit code is nonzero on Windows Vista, but is still zero
            // on Windows XP and Windows Server 2003. Therefore we search for "FAILED"
            // ("[SC] EnumQueryServicesStatus:OpenService FAILED 1060)" is expected.
            return null
        }

        String[] lines = output.split("\n")
        boolean running = false
        boolean stopped = false
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i]
            int idx = line.indexOf(':')
            if (idx >= 0) {
                String value = line.substring(idx+1, line.length())
                if (value.contains("RUNNING") || value.contains("STOPPED")) {
                    running = value.contains("RUNNING")
                    stopped = value.contains("STOPPED")
                    break
                }
            }
        }
        return new ServiceStatus(running, stopped)
    }

}

public class ServiceStatus {

    private boolean running
    private boolean stopped

    public ServiceStatus(boolean running, boolean stopped) {
        this.running = running
        this.stopped = stopped
    }

    public boolean isRunning() {
        return running
    }

    public boolean isStopped() {
        return stopped
    }

}
