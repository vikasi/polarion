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

import java.io.File
import java.util.List
import java.util.Map
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.polarion.alm.install.TextFileEditor

public class PostgresqlLinuxUpdate extends PostgresqlUpdate{

    private static final String PG_EXTENSION_PACKAGE_NAME = "postgresql-contrib"
    private static final String PG_OS_USER = "postgres"
    private static final String PG_CONFIG_SCRIPT = "configure_postgres.sh"
    private static final String RC_PREFIX = "rc"
    private static final String PG_INIT_NAME = "postgresql-polarion"
    private static final String PG_INIT_PATH = "/etc/init.d/"+PG_INIT_NAME
    private static final String PG_INIT_FILE_NAME = PG_INIT_NAME+".init"
    private static final String PG_PACKAGE_NAME = "postgresql"
    private static final String PG_PACKAGE_NAME_RH = "postgresql-server"
    private static final String PG_OLD_INIT_NAME = "postgresql"
    private static final List<String> DEBIAN_INSTALLER = ["apt-get", "-y", "install"]
    private static final List<String> RH_INSTALLER = ["yum", "-y", "install"]
    private static final List<String> SUSE_INSTALLER = ["zypper", "--non-interactive", "install"]
    private static final String PG_INIT_SERVICE = PG_INIT_NAME+".service"
    private static final String PG_OLD_INIT_SERVICE = PG_OLD_INIT_NAME+".service"
    private static final String SYSTEMD_PATH = "/usr/lib/systemd/system/"
    private static final String DIGIT_PATTERN = "[0-9]"
    private static final String PG_PACKAGE_NAME_SLES11SP3 = "postgresql91"
    private static final String PG_EXTENSION_PACKAGE_NAME_SLES11SP3 = "postgresql91-contrib"
    private static final String PG_PACKAGE_NAME_SLES11SP4 = "postgresql94"
    private static final String PG_EXTENSION_PACKAGE_NAME_SLES11SP4 = "postgresql94-contrib"


    private final File initScript
    private final File tempScriptDir

    public PostgresqlLinuxUpdate(UpdateTool updateTool){
        super(updateTool)
        tempScriptDir = new File(installation.getDataDir(null), "postgres-temp-dir")
        configureScript = new File(homeUpdateDir, "linux/"+PG_CONFIG_SCRIPT)
        initScript = new File(getPolarionBinDir(), PG_INIT_FILE_NAME)
    }

    public void installPostgresql(){
        installPostgresqlPackages()
    }

    private  void installPostgresqlPackages(){
        ant.echo "Polarion needs the following additional software: "+getPostgresqlPackageName()+" and "+PG_EXTENSION_PACKAGE_NAME+" packages for PostgreSQL database"
        ant.input("Would you like to check and install the additional software (recommended)?", validargs:"y,n", addproperty:"pg_install.ok")
        println ""
        if("Y".equalsIgnoreCase(antHelper.getProperty("pg_install.ok"))) {
            ant.echo ""
            ant.echo "Please wait while PostgreSQL is being installed..."
            runInstallPostgresql()
            updateTool.getUpdateState().setPostgresqlCleanInstall(true)
        }else{
            ant.echo "The Polarion server cannot start until PostgreSQL is installed and configured!"
            ant.echo "Please install "+getPostgresqlPackageName()+" and "+PG_EXTENSION_PACKAGE_NAME+" packages manually. See \"Installation Guide for Linux\" for instructions."
            ant.input(message:"Press ENTER after you have manually installed these packages.")
        }
    }

    private void runInstallPostgresql(){
        if (osHelper.isSUSE()){
            installPostgresqlSuse()
        }else{
            reportInstallationStatus(executeCommandSilently(getInstallPackagesStatement()))
        }
    }

    private void reportInstallationStatus(boolean installed){
        if (installed){
            ant.echo "PostgreSQL packages were successfully installed."
        }else{
            ant.echo "Unable to install PostgreSQL packages. Please see update log for more details."
        }
    }

    private void installPostgresqlSuse(){
        if (osHelper.isSuse11()){
            installPostgresqlSuse11()
        }else{
            reportInstallationStatus(installSusePackageByCapability())
        }
    }

    private void installPostgresqlSuse11(){
        // the additional check for postgresql package being installed is here to avoid unwanted update of already installed postgresql utilities (even if unsupported)
        if (isInstalledPackageWithVersion(getPostgresqlPackageName())){
            ant.echo "Found existing 'postgresql' package - skipping installation."
        }else{
            reportInstallationStatus(executeCommandSilently(getInstallPackagesStatementForSuse11()))
        }
    }

    private boolean installSusePackageByCapability(){
        List<String> installCmd = []
        installCmd.addAll(SUSE_INSTALLER)
        installCmd.addAll(["-C", PG_PACKAGE_NAME, PG_EXTENSION_PACKAGE_NAME])
        return executeCommandSilently(installCmd)
    }

    private List<String> getInstallPackagesStatementForSuse11(){
        List<String> installCmd = []
        if(osHelper.isSuse11Sp3()){
            installCmd.addAll(SUSE_INSTALLER)
            installCmd.addAll([PG_PACKAGE_NAME_SLES11SP3, PG_EXTENSION_PACKAGE_NAME_SLES11SP3])
        }else if(osHelper.isSuse11Sp4()){
            installCmd.addAll(SUSE_INSTALLER)
            installCmd.addAll([PG_PACKAGE_NAME_SLES11SP4, PG_EXTENSION_PACKAGE_NAME_SLES11SP4])
        }
        return installCmd
    }

    public String getPostgresqlPackageName(){
        if (osHelper.isRedHat()){
            return PG_PACKAGE_NAME_RH
        }
        return PG_PACKAGE_NAME
    }

    private List<String> getInstallPackagesStatement(){
        List<String> stmt = new ArrayList<String>()
        stmt.addAll(getSpecificInstallPackagesStatement())
        stmt.add(getPostgresqlPackageName())
        stmt.add(PG_EXTENSION_PACKAGE_NAME)
        return stmt
    }

    private List<String> getSpecificInstallPackagesStatement(){
        if (osHelper.isDebian()){
            return DEBIAN_INSTALLER
        }else if (osHelper.isSUSE()){
            return SUSE_INSTALLER
        }else if(osHelper.isRedHat()){
            return RH_INSTALLER
        }
        return null
    }

    public boolean isPostgresqlInstalled(){
        return checkInstalledPackage(getPostgresqlPackageName()) && checkInstalledPackage(PG_EXTENSION_PACKAGE_NAME)
    }

    private boolean checkInstalledPackage(String pgPackageName){
        if (osHelper.isSUSE()){
            return checkInstallPackageWithVersionAndOutput(pgPackageName)
        }else if (osHelper.isInstalled(pgPackageName)){
            ant.echo "Found "+pgPackageName
            return true
        }
        updateTool.fail "Required "+pgPackageName+" package was not found."
        return false
    }

    /**
     * If installed, reports the exact name of the package
     */
    private boolean checkInstallPackageWithVersionAndOutput(String pgPackageName){
        String foundPackageName = checkInstallPackageWithVersionImpl(pgPackageName)
        if (foundPackageName != null){
            ant.echo "Found "+foundPackageName
            return true
        }else{
            String version = getPsqlVersion()
            if (version.isEmpty()){
                updateTool.fail "Required "+pgPackageName+" package was not found."
            }
            updateTool.fail "Required "+pgPackageName+" package (version "+getShortVersion(version)+") was not found."
            return false
        }
    }
    private boolean isInstalledPackageWithVersion(String pgPackageName){
        return checkInstallPackageWithVersionImpl(pgPackageName) != null
    }
    /**
     * postgresql is installed if psql utility returns a version and if that version is supported. 
     * postgresql-contrib is installed if a package with the same version as the version returned by psql utility
     */
    private String checkInstallPackageWithVersionImpl(String pgPackageName){
        String version = getPsqlVersion()
        if (version.isEmpty()){
            return null
        }
        checkSupportedVersion(pgPackageName,version)
        List<String> versionedPackageNames = getPossibleVersionedPackageNames(pgPackageName,version)
        for (String versionedPackageName: versionedPackageNames){
            String installedPackageName = getInstalledPackageName(versionedPackageName)
            if (!installedPackageName.trim().isEmpty()){
                return installedPackageName
            }
        }
        return null
    }

    private void checkSupportedVersion(String pgPackageName, String version){
        if (!isSupportedVersion(version)){
            updateTool.fail "Unsupported version of PostgreSQL found: version "+getShortVersion(version)+". Polarion requires PostgreSQL 8.4 or higher."+
                    "\nPlease install manually a supported version of postgresql and postgresl-contrib packages and restart the update. "+
                    "\nSee the install guide documentation for package installation details."
        }
    }

    private String getPsqlVersion(){
        if (hasPsql()){
            Process p = ["psql", "--version"].execute()
            if (p.waitFor() == 0){
                String[] version = p.getText().split("\\s+")
                if (version.size() > 2){
                    return version[2]
                }
            }
        }
        return ""
    }

    private String getShortVersion(String version){
        getPostgresqlMajorVersion(version)+"."+getPostgresqlMinorVersion(version)
    }

    private boolean hasPsql(){
        return ["which", "psql"].execute().waitFor() == 0
    }

    private String getVersionedPackageName(String packageName, String version){
        int split = packageName.indexOf("-")
        if (split>-1){
            String part = packageName.substring(0,split)
            String suffix = packageName.substring(split)
            return part+version+suffix
        }
        return packageName+version
    }

    /**
     * opensuse and default sles repos have sligthly different package names with version.
     * We look for "postgresql", but it could come in the form "postgresqlNN-[0-9]" or "postgresql-N.N.[0-9]". 
     * Same for postgresql-contrib: postgresql-contrib-N.N.[0-9] or postgresqlNN-contrib-[0-9]
     * @param packageName
     * @param version
     * @return a list of possible package names with version
     */
    private List<String> getPossibleVersionedPackageNames(String packageName, String version){
        return [getVersionedPackageName(packageName, removeDots(version))+"-"+DIGIT_PATTERN
            , packageName+"-"+getShortVersion(version)+"."+DIGIT_PATTERN]
    }

    private String getInstalledPackageName(String versionedPackageName){
        Process p = ["sh", "-c", "rpm -qa | grep "+versionedPackageName].execute()
        p.waitFor()
        if (p.exitValue() == 0){
            return p.getText()
        }
        return ""
    }

    private String removeDots(String version){
        return getPostgresqlMajorVersion(version)+getPostgresqlMinorVersion(version)
    }

    private isSupportedVersion(String version){
        try{
            int major = Integer.parseInt(getPostgresqlMajorVersion(version))
            int minor = Integer.parseInt(getPostgresqlMinorVersion(version))
            return major > 8 || major == 8 && minor >= 4
        }catch (NumberFormatException e){
            return false
        }
    }

    private String getPostgresqlMinorVersion(String version){
        return version.contains(".") ? version.split("\\.")[1] : ""
    }

    private String getPostgresqlMajorVersion(String version){
        return version.contains(".") ? version.split("\\.")[0] : ""
    }

    protected void configurePostgresqlInternal(int pgPort, String pgPassword){
        setupTempFilesAndPermissions()
        runConfigurationScript(pgPort, pgPassword)
        cleanTempFiles()
    }

    protected void optimizePostgresqlInternal(){
        //Nothing to do here, optimization done by configuration script
    }

    private void setupTempFilesAndPermissions(){
        ["mkdir", tempScriptDir.getAbsolutePath()].execute().waitFor()
        ["chown", "-R", PG_OS_USER, tempScriptDir.getAbsolutePath()].execute().waitFor()
        File target = new File(tempScriptDir, PG_CONFIG_SCRIPT)
        ["cp", configureScript.getAbsolutePath(), target.getAbsolutePath()].execute().waitFor()
        ["chmod", "+x", target.getAbsolutePath()].execute().waitFor()
        ["chown", "-R", PG_OS_USER+":"+PG_OS_USER, target.getAbsolutePath()].execute().waitFor()
    }

    private void runConfigurationScript(int port, String password){
        preparePostgresLogDir()
        preparePostgresDataDir()
        ant.copy(file:new File(homeUpdateDir,"linux/etc/functions.sh").getAbsolutePath(),todir:new File(installation.getInstallDir(), "etc").getAbsolutePath(),overwrite:"true")
        Map<String, String> envVars = ["POLARION_HOME" : installation.getInstallDir().getAbsolutePath()
            , "POLARION_DATA" : installation.getDataDir(null).getAbsolutePath()
            , "POSTGRES_DATA" : postgresDataDir.getAbsolutePath()
            , "POSTGRES_PORT" : String.valueOf(port)
            , "POLARION_POSTGRES_PASSWORD" : password]
        if (executeCommandSilently(["su", PG_OS_USER, "-c", new File(tempScriptDir, PG_CONFIG_SCRIPT).getAbsolutePath()], envVars, tempScriptDir)){
            ant.echo OK_MSG
            setPostgresqlConnectionProperty(port, password)
        }else{
            ant.echo NOK_MSG
        }
    }

    private void preparePostgresDataDir(){
        ant.mkdir(dir:postgresDataDir.getAbsolutePath())
        ["chown", "-R", PG_OS_USER+":"+PG_OS_USER, postgresDataDir.getAbsolutePath()].execute().waitFor()
    }

    private void preparePostgresLogDir(){
        ant.mkdir(dir:postgresLogDir.getAbsolutePath())
        ["chown", "-R", PG_OS_USER+":"+PG_OS_USER, postgresLogDir.getAbsolutePath()].execute().waitFor()
    }

    private void cleanTempFiles(){
        ["rm", "-rf", tempScriptDir.getAbsolutePath()].execute().waitFor()
    }

    public void registerService(){
        int pgPort = getPort()
        ant.echo "Registering init script for "+PG_INIT_NAME+" service..."
        ant.copy(file: (new File(homeUpdateDir, "linux/bin/"+PG_INIT_FILE_NAME)).getAbsolutePath(), todir:getPolarionBinDir().getAbsolutePath(), overwrite:"true")
        customizePostgresqlPolarionInitFile(pgPort)
        setupPostgresqlInitService()
    }

    private void createInitSymLink(){
        makeInitScriptExecutable()
        if (!(new File(PG_INIT_PATH)).exists()){
            Files.createSymbolicLink(
                    Paths.get(PG_INIT_PATH),
                    Paths.get(initScript.getAbsolutePath())
                    )
        }
    }

    private void makeInitScriptExecutable(){
        ["chmod", "+x", initScript.getAbsolutePath()].execute().waitFor()
    }

    private void customizePostgresqlPolarionInitFile(int port){
        TextFileEditor editor = new TextFileEditor()
        editor.open(initScript)
        Map<String, String> replacements = ["@@PG_PORT@@" : String.valueOf(port)
            ,"@@PGDATA@@" : postgresDataDir.getAbsolutePath()
            ,"@@POLARION_ETC@@" : installation.getConfigDir().getAbsolutePath()]
        replacements.putAll(getOsSpecificStatements(osHelper))
        for (String keyword : replacements.keySet()){
            replaceInLines(editor, keyword, replacements.get(keyword))
        }
        editor.save()
    }

    private void customizePostgresqlPolarionServiceFile(File serviceFile){
        TextFileEditor editor = new TextFileEditor()
        editor.open(serviceFile)
        replaceInLines(editor, "@@POLARION_HOME@@", installation.getInstallDir().getAbsolutePath())
        editor.save()
    }

    private static void replaceInLines(TextFileEditor editor, String keyword, String replacement){
        List<String> lines = editor.searchLines(keyword)
        for (String line: lines){
            editor.replaceLine(line, line.replace(keyword, replacement))
        }
    }

    public static customizeInitScriptOsSpecificStatements(File initScript, OSHelper osHelper){
        TextFileEditor editor = new TextFileEditor()
        editor.open(initScript)
        Map<String, String> replacements = getOsSpecificStatements(osHelper)
        for (String keyword : replacements.keySet()){
            replaceInLines(editor, keyword, replacements.get(keyword))
        }
        editor.save()
    }

    private static Map<String, String> getOsSpecificStatements(OSHelper osHelper){
        if (osHelper.isDebian()){
            return ["@@os_startup_lib@@" : "/lib/lsb/init-functions"
                ,"@@status_ok@@" : "if [[ -n \$1 ]]; then log_progress_msg  \"\$1 (PID)\"; fi && log_end_msg 0"
                ,"@@status_pre@@" : "log_daemon_msg \"\$1\""
                ,"@@status_failed@@" : "log_end_msg 1"
                ,"@@pre_init_script@@" : "return 0"
                ,"@@post_init_script@@" : "return 0" ]
        }else if (osHelper.isRedHat()){
            return [ "@@os_startup_lib@@" : "/etc/rc.d/init.d/functions"
                ,"@@status_ok@@" : "success \necho"
                ,"@@status_pre@@" : "echo -n \"\$1\""
                ,"@@status_failed@@" : "failure \necho"
                ,"@@pre_init_script@@" : "return 0"
                ,"@@post_init_script@@" : "return 0" ]
        }else if (osHelper.isSUSE()){
            return ["@@os_startup_lib@@" : "/etc/rc.status"
                ,"@@status_ok@@" : "rc_status -v"
                ,"@@status_pre@@" : "echo -n \"\$1\""
                ,"@@status_failed@@" : "rc_status -v"
                ,"@@pre_init_script@@" : "rc_reset"
                ,"@@post_init_script@@" : "rc_exit"]
        }
    }

    private void setupPostgresqlInitService(){
        if (osHelper.hasSystemd()){
            registerSystemdService()
        }else{
            createInitSymLink()
            if (osHelper.isDebian()){
                registerSysvServiceDebian()
            }else if (osHelper.isRedHat()){
                registerSysvServiceRedHat()
            }else if (osHelper.isSUSE()){
                registerSysvServiceSuse()
            }
        }
    }

    private void registerSysvServiceDebian(){
        if (updateTool.getUpdateState().isPostgresqlCleanInstall()){
            executeCommandSilently(["service", PG_OLD_INIT_NAME, "stop"])
            executeCommandSilently(["update-rc.d", PG_OLD_INIT_NAME, "disable"])
        }
        executeCommandSilently(["update-rc.d", PG_INIT_NAME, "start", "90", "3", "5", ".", "stop", "16", "0", "1", "2", "6", "."])
    }

    private void registerSysvServiceRedHat(){
        executeCommandSilently(["chkconfig", "--add", PG_INIT_NAME])
        executeCommandSilently(["chkconfig", PG_INIT_NAME, "reset"])
    }

    private void registerSysvServiceSuse(){
        File link = new File("/usr/sbin/"+RC_PREFIX+PG_INIT_NAME)
        if (!link.exists()){
            Files.createSymbolicLink(Paths.get("/usr/sbin/"+RC_PREFIX+PG_INIT_NAME), Paths.get(PG_INIT_PATH))
        }
        executeCommandSilently(["chkconfig", "--add", PG_INIT_NAME])
    }

    private void registerSystemdService(){
        if (osHelper.isDebian() && updateTool.getUpdateState().isPostgresqlCleanInstall()){
            disableOldPostgresqlService()
        }
        makeInitScriptExecutable()
        installPostgresqlPolarionInSystemdPath()
        enablePostgresqlPolarionService()
    }

    private void disableOldPostgresqlService(){
        executeCommandSilently(["service", PG_OLD_INIT_NAME, "stop"])
        executeCommandSilently(["systemctl", "disable", PG_OLD_INIT_SERVICE])
        executeCommandSilently(["systemctl", "daemon-reload"])
    }

    private void installPostgresqlPolarionInSystemdPath(){
        File systemdPath = new File(SYSTEMD_PATH)
        if (!systemdPath.isDirectory()){
            systemdPath.mkdir()
        }
        File unitFile = new File(homeUpdateDir, "linux/bin/"+PG_INIT_SERVICE)
        File tempUnitFile = new File(getPolarionBinDir(), PG_INIT_SERVICE)
        ["cp", "-p", unitFile.getAbsoluteFile(), tempUnitFile.getAbsolutePath()].execute().waitFor()
        customizePostgresqlPolarionServiceFile(tempUnitFile)
        File targetUnitFile = new File(SYSTEMD_PATH+PG_INIT_SERVICE)
        executeCommandSilently(["install", "-m", "0755", tempUnitFile.getAbsolutePath(), targetUnitFile.getAbsolutePath()])
        tempUnitFile.delete()
    }

    private void enablePostgresqlPolarionService(){
        executeCommandSilently(["systemctl", "daemon-reload"])
        executeCommandSilently(["systemctl", "enable", PG_INIT_SERVICE])
    }

    public boolean isPostgresqlRegistered(){
        List<String> cmd = osHelper.hasSystemd() ? ["systemctl", "is-enabled", "postgresql-polarion"]: osHelper.isDebian() ? ["update-rc.d", "-n", PG_INIT_NAME, "disable"]: ["sh", "-c", "\"chkconfig | grep "+PG_INIT_NAME+"\""]
        boolean isRegistered = executeCommandSilently(cmd)
        if (isRegistered){
            String name = osHelper.hasSystemd() ? PG_INIT_SERVICE : PG_INIT_NAME
            ant.echo name+" init script was successfully registered and configured."
        }
        return isRegistered
    }

    public boolean startPostgresql(){
        return runPostgresqlService("start") && runPostgresqlService("status")
    }

    private boolean runPostgresqlService(String command){
        List<String> cmd = osHelper.isSUSE() && !osHelper.hasSystemd() ? [RC_PREFIX+PG_INIT_NAME, command]: ["service", PG_INIT_NAME, command]
        return executeCommandSilently(cmd)
    }

    private File getPolarionBinDir(){
        return new File(installation.getInstallDir(), "bin")
    }
}

