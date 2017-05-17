/*
 * Copyright (C) 2004-2011 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2011 Polarion Software
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

import com.polarion.core.util.remote.client.SocketRemoteControlClient
import com.polarion.core.util.remote.server.SocketRemoteControlServer

CommandLine cli = new CommandLine(args, [], ["-help"])
String installDir = cli.getInstallDir()
if (installDir == null) {
    println ""
    println "ERROR: Missing argument "+CommandLine.ARG_INSTALL_DIR
    System.exit(1)
}

if (cli.getFreeUserArgs().isEmpty()) {
    println ""
    println "ERROR: Missing command name."
    System.exit(1)
}

PolarionInstallation inst = new PolarionInstallation(new File(cli.getInstallDir()))

String cmd = cli.getFreeUserArgs().get(0)
if ("shutdown".equals(cmd)) {
    SocketRemoteControlClient.main(inst.getControlHostname(), inst.getControlPort(), "false", cmd,"false")
    try {
        Thread.sleep(15000);
    } catch (InterruptedException e) {
        // nothing
    }
} else {
    if (cli.getFreeUserArgs().size() < 2) {
        println "Usage:"
        println cli.getCommand() + " INSTANCE_ID"
        println ""
        if ("kill".equals(cmd)) {
            println "Sends request to multi-instance controller to forcefully kill instance with given id."
            println "Use only if necessary, because data loss may occur."
        } else if ("reindex".equals(cmd)) {
            println "Sends request to multi-instance controller to reindex instance with given id."
            println "Instance must be stopped prior to reindex."
        } else if ("start".equals(cmd)) {
            println "Sends request to multi-instance controller to start instance with given id."
        } else if ("stop".equals(cmd)) {
            println "Sends request to multi-instance controller to gracefully stop instance with given id."
        }
        System.exit(0)
    }
    String slaveId = cli.getFreeUserArgs().get(1)
    if ("start".equals(cmd)){
        if (isCoordinatorRunning(inst)){
            handleStart(inst, slaveId);
        }
        else{
            println "Unable to start instance "+ slaveId +": coordinator application is not running. Please start the coordinator."
        }
    }
    else if ("kill".equals(cmd)){
        handleKill(inst, slaveId);
    }
    else{
        if ("reindex".equals(cmd)){
            handleReindex(inst, slaveId)
        }
        else if ("stop".equals(cmd)){
            handleStop(inst, slaveId,"false",null);
        } else
            println "Wrong command."
    }
}

def isCoordinatorRunning(PolarionInstallation inst){
    File workspace = inst.getCoordinatorWorkspaceDir();
    return isInstanceRunning(workspace)
}


def isInstanceRunning(File workspace){
    if (workspace!=null){
        File pidFile = new File(new File(workspace,".metadata"),"server.pid");
        if (pidFile.exists()){
            String pid = getPidFromFile(pidFile);
            OSHelper osHelper = new OSHelper();
            return osHelper.isProcessRunning(pid);
        }
        else{
            println "The pid file for the application does not exists. Most likely the process was already stopped."
            return false;
        }
    }else{
        println "Unable to find workspace directory for this application."
        return false;
    }
}

def handleReindex(PolarionInstallation inst, String slaveId){
    File workspaceDir = inst.getPolarionDataDir(slaveId)//polarion-data folder
    File destFile = new File(workspaceDir.getParentFile(), workspaceDir.getName()+".backup");
    if (!destFile.exists()) {
        if( workspaceDir.renameTo(destFile)){
            handleStart(inst,slaveId);
        }
        else
            println "Renaming did not succeded."
    }
    else{
        println "ERROR: Backup folder " + destFile + " already exists. Please remove it and try it again."
    }
}

def handleStop(PolarionInstallation inst, String slaveId, String coordinator,File privilegefile){
    if (slaveId!=null){
        String controlHostname = inst.getSlaveControlHostname(slaveId);
        String controlPort =inst.getSlaveControlPort(slaveId);
        try{
            if (privilegefile!=null){
                SocketRemoteControlClient.main(controlHostname, controlPort, "false","shutdown",coordinator, privilegefile.getAbsolutePath());
            }
            else{
                SocketRemoteControlClient.main(controlHostname, controlPort, "false","shutdown",coordinator);
            }
        }catch(Exception e){
            println "Unable to stop instance "+ slaveId + " (perhaps instance was already stopped) "+e.printStackTrace();
        }
    }
    else{
        println "Please specify an instance to stop.";
    }
}

def handleKill(PolarionInstallation inst, String slaveId){
    //find process id
    File pidFile = new File(new File(inst.getWorkspaceDir(slaveId),".metadata"),"server.pid");
    String pid = getPidFromFile(pidFile);
    if (pid != null){
        OSHelper osHelper = new OSHelper();
        //kill process if still running
        if (osHelper.isProcessRunning(pid)){
            osHelper.killProcess(pid);
        }
        else{
            println "Process "+pid+ " is not running anymore."
        }
    }
    else{
        println "There is no pid file associated. Most likely the instance was already stopped."
    }

}


def getPidFromFile(File pidFile) {
    String pid = null
    if (pidFile.exists()) {
        AntHelper antHelper = new AntHelper();
        String prop = antHelper.newPropertyName();
        AntBuilder ant = antHelper.getBuilder();
        ant.loadfile(property:prop,srcfile:pidFile.getAbsolutePath())
        pid = antHelper.getProperty(prop).trim()
    }
    return pid
}


def handleStart(PolarionInstallation inst, String slaveId){
    List<String> arguments= inst.prepareArguments(slaveId);
    AntBuilder ant = new AntBuilder();
    ant.exec(
            spawn:"true",
            executable: arguments.get(0)) {
                for (String a: arguments){
                    if (!a.equals(arguments.get(0))){
                        arg(line:a)
                    }
                }
            }
    println "Polarion instance "+slaveId+" is starting. Please see logs for status."
}


def startAll(String installDir){
    if (installDir!=null){
        PolarionInstallation inst = new PolarionInstallation(new File(installDir))
        for (String slave: inst.getSlaveIds()){
            handleStart(inst,slave);
        }
    }
    else
        println "ERROR: Missing argument "+installDir
}

def stopAll(String installDir){
    if (installDir!=null){
        PolarionInstallation inst = new PolarionInstallation(new File(installDir))
        File privilegefile = SocketRemoteControlServer.findPrivilegeFileInDir(new File(installDir,"polarion"))
        for (String slave: inst.getSlaveIds()){
            handleStop(inst,slave,"true",privilegefile);
        }
        println "Waiting for instances to shutdown."
        Thread.sleep(60000);
        for (String slave: inst.getSlaveIds()){
            File workspace = inst.getWorkspaceDir(slave);
            if (isInstanceRunning(workspace)){
                println "Instance "+slave+" will be killed."
                handleKill(inst,slave);
            }
            else{
                println "Instance "+slave+" was stopped."
            }
        }
    }
    else{
        println "ERROR: Missing argument "+installDir
    }
}