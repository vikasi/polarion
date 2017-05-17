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
import com.polarion.alm.install.TextFileEditor

import java.awt.datatransfer.StringSelection
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.ArrayList
import java.util.Arrays
import java.util.List


public class OSHelper {
    private static final String suseReleaseFile = "/etc/SuSE-release"

    private AntHelper ant

    public OSHelper() {
        ant = new AntHelper()
    }

    public boolean isWindows() {
        String prop = ant.newPropertyName()
        ant.getBuilder().condition(property:prop) { os(family:"windows") }
        return (ant.getProperty(prop) != null)
    }

    public boolean isDebian() {
        return new File("/etc/debian_version").isFile()
    }

    public boolean isRedHat() {
        return new File("/etc/redhat-release").isFile()
    }

    public boolean isSUSE() {
        return new File(suseReleaseFile).isFile()
    }

    public boolean isSuse11(){
        return "11".equals(getSUSEVersion())
    }

    public boolean isSuse11Sp3(){
        return "3".equals(getSUSEPatchLevel())
    }

    public  boolean isSuse11Sp4(){
        return "4".equals(getSUSEPatchLevel())
    }

    private String getSUSEVersion(){
        return getSUSEInfo("VERSION")
    }

    private String getSUSEPatchLevel(){
        return getSUSEInfo("PATCHLEVEL")
    }

    private String getSUSEInfo(String info){
        if (isSUSE()){
            String value = new File(suseReleaseFile).readLines().find() {  it.contains(info) }
            return value.contains("=") ? value.split("=")[1].trim(): null
        }
        return null
    }

    public boolean hasSystemd(){
        return !isWindows() && ["which", "systemctl"].execute().waitFor() == 0
    }

    public boolean is64Bit() {
        return "x86_64".equals(getMachineArch())
    }

    private String getMachineArch() {
        return isWindows() ? getMachineArchWindows() : getMachineArchLinux()
    }

    private String getMachineArchWindows() {
        ant.getBuilder().property(environment:"env")
        String arch = ant.getProperty("env.ProgramFiles(x86)")
        // keep conventions from uname -m: return x86_64 for 64bit Windows and i386 for 32bit Windows
        return arch != null && arch.contains("Program Files (x86)") ? "x86_64" : "i386"
    }

    private String getMachineArchLinux() {
        // os.arch gives arch of Java and not system, we must use "uname -m"
        String outputProp = ant.newPropertyName()
        ant.getBuilder().exec(executable:"uname",outputproperty:outputProp) { arg(value:"-m") }
        return ant.getProperty(outputProp).trim()
    }

    /** subset of isSUSE() */
    public boolean isSUSE64() {
        return isSUSE() && "x86_64".equals(getMachineArch())
    }

    public boolean isValidPort(int port) {
        return ((port >= 0) && (port <= 65535))
    }

    public boolean isPortAvailable(int port) {
        if (!isValidPort(port)) {
            throw new IllegalArgumentException("port is invalid: "+port)
        }
        String prop = ant.newPropertyName()
        ant.getBuilder().condition(property:prop) {
            socket(server:"localhost",port:String.valueOf(port))
        }
        return (ant.getProperty(prop) == null)
    }

    /**
     * Returns available port that is greater than or equal to given port number.
     * If no such port is available, returns -1.
     */
    public int getAvailablePort(int minimalPort) {
        if (!isValidPort(minimalPort)) {
            throw new IllegalArgumentException("minimalPort is invalid: "+minimalPort)
        }
        boolean available
        while (isValidPort(minimalPort) && !isPortAvailable(minimalPort)) {
            minimalPort++
        }
        return isValidPort(minimalPort) ? minimalPort : -1
    }

    public boolean isValidHost(String host) {
        String prop = ant.newPropertyName()
        ant.getBuilder().condition(property:prop) {
            isreachable(host:host,timeout:"10")
        }
        return (ant.getProperty(prop) != null)
    }

    public void killProcess(String pid){
        println "Killing process "+pid
        List<String> arguments
        if (isWindows()){
            arguments = prepareKillWindows(pid)
        }else{
            arguments = prepareKillLinux(pid)
        }
        TaskResult result = executeTask(arguments)
        if (result.getReturnCode()==0){
            //killing worked
            boolean killed = false
            while (!killed) {
                Thread.sleep(1000)
                killed = !isProcessRunning(pid)
            }
            println "Process killed."
        }
        else{
            println "There was a problem executing the kill for the process "+result.getOuput()
        }
    }

    public  boolean isProcessRunning(String pid){
        if (pid == null) {
            return false
        }
        List<String> arguments=null
        if (isWindows()){
            arguments = prepareSearchWindows(pid)
        }else{
            arguments = prepareSearchLinux(pid)
        }
        TaskResult result = executeTask(arguments)
        if (result.getReturnCode()>1){ // status code==1 means that no process is found, so not an error
            println "Execution for searching a process failed - "+ result.getOuput()
        }
        return result.getOuput().contains(pid)
    }

    public  TaskResult executeTask(List<String> arguments){
        AntBuilder antBuilder= ant.getBuilder()
        String outputProp = ant.newPropertyName()
        String resultProp = ant.newPropertyName()
        antBuilder.exec(executable:arguments.get(0),outputproperty:outputProp,resultproperty:resultProp) {
            for (String a: arguments){
                if (!a.equals(arguments.get(0))){
                    arg(line:a)
                }
            }
        }
        String result = ant.getProperty(resultProp)
        String output = ant.getProperty(outputProp)
        return new TaskResult(Integer.parseInt(result), output)
    }

    public List<String> prepareSearchLinux(String pid){
        List<String> list = new ArrayList<String>()
        list.add("ps")
        list.add("-p")
        list.add(pid)
        return list
    }

    public  List<String> prepareSearchWindows(String pid){
        List<String> list = new ArrayList<String>()
        list.add("tasklist")
        list.add("/NH")
        list.add("/FO")
        list.add("TABLE")
        list.add("/FI")
        list.add("\"PID eq "+pid+"\"")
        return list
    }

    public  List<String> prepareKillLinux(String pid){
        List<String> list = new ArrayList<String>()
        list.add("kill")
        list.add("-9")
        list.add(pid)
        return list
    }

    public List<String> prepareKillWindows(String pid){
        List<String> list = new ArrayList<String>()
        list.add("taskkill")
        list.add("/F")
        list.add("/T")
        list.add("/PID")
        list.add(pid)
        return list
    }

    private  List<String> getIsInstalledCommand(String packageName){
        if (isDebian()){
            String arg="\"dpkg -l "+packageName+" | grep "+packageName+"\""
            return ["sh", "-c", arg]
        }else if (isSUSE()){
            return ["rpm", "-q", packageName]
        }else if(this.isRedHat()){
            return ["rpm", "-q", packageName]
        }
        return null
    }

    public boolean isInstalled(String packageName){
        List<String> cmd = getIsInstalledCommand(packageName)
        if (cmd != null){
            TaskResult result = executeTask(cmd)
            if (result.getReturnCode()!=0){
                return false
            }
            if (isDebian() && result.getOuput().startsWith("ii")){
                return true
            }else if (isSUSE()&& result.getOuput().startsWith(packageName)){
                return true
            }else if(isRedHat()&& result.getOuput().startsWith(packageName)){
                return true
            }
        }
        return false
    }

    public boolean isSupportedLinux(){
        return isDebian() || isSUSE() || isRedHat()
    }
}

public class TaskResult{
    int returnCode
    String ouput

    TaskResult(int returnCode, String output){
        this.returnCode = returnCode
        this.ouput = output
    }
    int getReturnCode(){
        return returnCode
    }
    String getOutput(){
        return output
    }
    void setReturnCode(int val){
        returnCode = val
    }
    void setOutput(String val){
        output = val
    }

}
