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
import com.polarion.alm.install.TextFileEditor

class PostgresqlWindowsUpdate extends PostgresqlUpdate {

    private static final String PG_SERVICE_NAME = "PostgreSQLPolarion"
    private static final String PG_SERVICE_DESCRIPTION = "PostgreSQL service for Polarion."
    private static final String PG_START_SHORTCUT = "Start PostgreSQL Service.lnk"
    private static final String NET = "%SystemRoot%\\system32\\net.exe"
    private static final String PG_START_TARGET = "start "+PG_SERVICE_NAME
    private static final String PG_STOP_SHORTCUT = "Shutdown PostgreSQL Service.lnk"
    private static final String PG_STOP_TARGET = "stop "+PG_SERVICE_NAME
    private static final String PG_IMAGE_NAME = "slonik.ico"
    private static final String PG_ALL_IMAGE_NAME = "polarion\\polarion-16-run.ico"
    private static final String PG_START_ALL_SHORTCUT = "Start Apache, PostgreSQL and Polarion Server.lnk"
    private static final String OLD_START_ALL_SHORTCUT = "Start Apache and Polarion Server.lnk"
    private static final String PG_START_ALL_TARGET = "polarion\\run.bat"
    private static final String SHORTCUTS_DIR = "polarion shortcuts"
    private static final String VBS_SCRIPT_NAME = "createShortcuts.vbs"
    private static final String MARK_RUN_AS_ADMIN_EXE = "MarkRunAsAdmin.exe"
    private static final String POLARION_SERVER_SHORTCUT = "Start Polarion Server.lnk"
    private static final List<String> CHECK_SERVICE_CMD = ["SC", "qc", PG_SERVICE_NAME]
    private static final String BUNDLED_POSTGRES_LOCATION="bundled/postgres"

    private final File bundledPolarion
    private final File bundledUpdate

    public PostgresqlWindowsUpdate(UpdateTool updateTool){
        super(updateTool)
        bundledUpdate = new File(homeUpdateDir, BUNDLED_POSTGRES_LOCATION)
        bundledPolarion = new File(installation.getInstallDir(),BUNDLED_POSTGRES_LOCATION)
        configureScript = new File(bundledPolarion, "configure_postgres.bat")
        optimizeScript = new File(bundledPolarion, "optimize_postgres_config.bat")
    }

    public void installPostgresql(){
        ant.echo "From version 2016, Polarion requires PostgreSQL database server in order to function properly."
        ant.input("PostgreSQL server for Polarion will be automatically installed and configured. Would you like to continue?", validargs:"y,n", addproperty:"pg_install.ok")
        println ""
        if("Y".equalsIgnoreCase(antHelper.getProperty("pg_install.ok"))) {
            copyPostgresqlBinaries()
        }else{
            updateTool.fail "IMPORTANT! The Polarion server cannot start until PostgreSQL is installed and configured!"
        }
    }

    private void copyPostgresqlBinaries(){
        ant.echo "Copying PostgreSQL binaries to "+bundledPolarion.getAbsolutePath()+"..."
        ant.copy(todir:bundledPolarion.getAbsolutePath()) {
            fileset(dir:bundledUpdate.getAbsolutePath()){ exclude(name:MARK_RUN_AS_ADMIN_EXE) }
        }
    }

    public boolean isPostgresqlInstalled(){
        return bundledPolarion.isDirectory()
    }

    protected void configurePostgresqlInternal(int pgPort, String pgPassword){
        List<String> runConfigurationCmd = [configureScript.getAbsolutePath(), postgresDataDir.getAbsolutePath(), String.valueOf(pgPort), pgPassword]
        if (executeCommandSilently(runConfigurationCmd)){
            ant.echo OK_MSG
            setPostgresqlConnectionProperty(pgPort, pgPassword)
        }else{
            ant.echo NOK_MSG
        }
    }

    protected void optimizePostgresqlInternal(){
        List<String> runOptimizationCmd = [optimizeScript.getAbsolutePath(), postgresDataDir.getAbsolutePath()]
        if (executeCommandSilently(runOptimizationCmd)){
            ant.echo OPT_OK_MSG
        }
    }

    public void registerService(){
        checkAdminRights()
        deleteServiceIfExists()
        File pc_ctl = new File(bundledPolarion, "bin/pg_ctl.exe")
        List<String> registerCmd = [
            pc_ctl.getAbsolutePath(),
            "register",
            "-N",
            PG_SERVICE_NAME,
            "-D",
            postgresDataDir.getAbsolutePath(),
            "-o",
            "\"-p "+String.valueOf(getPort())+"\""
        ]
        ant.echo "Registering service "+PG_SERVICE_NAME+"..."
        if (executeCommandSilently(registerCmd)){
            executeCommandSilently (["SC", "description", PG_SERVICE_NAME, "\""+PG_SERVICE_DESCRIPTION+"\""])
            ant.echo PG_SERVICE_NAME+" service was successfully registered."
            setupShortcuts()
        }
    }

    private checkAdminRights(){
        if(["NET", "SESSION"].execute().waitFor() != 0){
            updateTool.fail "You must have Administrator access for registration of PostgreSQLPolarion service. Please re-run the update script as Administrator."
        }
    }

    private void deleteServiceIfExists(){
        if(CHECK_SERVICE_CMD.execute().waitFor() == 0){
            ["NET", "STOP", PG_SERVICE_NAME].execute().waitFor()
            executeCommandSilently(["SC", "DELETE", PG_SERVICE_NAME])
        }
    }

    public boolean isPostgresqlRegistered(){
        return executeCommandSilently(CHECK_SERVICE_CMD)
    }

    public boolean startPostgresql(){
        return executeCommandSilently(["NET", "START", PG_SERVICE_NAME])
    }

    private setupShortcuts(){
        File createShortcutsScript = generateVbsScript()
        if(executeCommandSilently(["cscript", createShortcutsScript.getAbsolutePath()]) && setRunAsAdmin()){
            modifyRunScript()
            deleteOldShortcut()
            ant.echo "Successfully created the following shortcuts: "
            ant.echo "\t"+PG_STOP_SHORTCUT
            ant.echo "\t"+PG_START_SHORTCUT
            ant.echo "\t"+PG_START_ALL_SHORTCUT
            ant.echo ""
        }else {
            ant.echo "There was a problem configuring the Polarion shortcuts for PostgreSQL. Please create or modify them manually if they are needed."
        }
        createShortcutsScript.delete()
    }

    private void deleteOldShortcut(){
        File shortcutsDir = new File(installation.getInstallDir(), SHORTCUTS_DIR)
        File oldShortcut = new File(shortcutsDir, OLD_START_ALL_SHORTCUT)
        oldShortcut.delete()
    }

    private File generateVbsScript(){
        File createShortcutsScript = new File(bundledPolarion, VBS_SCRIPT_NAME)
        String content = getLinesForStartStopShortcut(PG_START_SHORTCUT, PG_START_TARGET)
        content += getLinesForStartStopShortcut(PG_STOP_SHORTCUT, PG_STOP_TARGET)
        content += getLinesForAllShortcut()
        createShortcutsScript.setText(content)
        return createShortcutsScript
    }

    private void modifyRunScript(){
        File runbat = new File(installation.getInstallDir(), PG_START_ALL_TARGET)
        File shortcutsDir = new File(installation.getInstallDir(), SHORTCUTS_DIR)
        File shortcut = new File(shortcutsDir, PG_START_SHORTCUT)
        TextFileEditor editor = new TextFileEditor()
        editor.open(runbat)
        List<String> polarionStartLines=editor.searchLines(POLARION_SERVER_SHORTCUT)
        editor.removeLines(polarionStartLines)
        String pgLine = "start \""+PG_SERVICE_NAME+" Service\" \""+shortcut.getAbsolutePath()+"\""
        if (!editor.containsLine(pgLine)){
            editor.addLine(pgLine)
        }
        editor.addLines(polarionStartLines)
        editor.save()
    }

    private boolean setRunAsAdmin(){
        String markRunAsAdminScript = new File(bundledUpdate, MARK_RUN_AS_ADMIN_EXE).getAbsolutePath()
        File shortcutsDir = new File(installation.getInstallDir(), SHORTCUTS_DIR)
        boolean code = executeCommandSilently([markRunAsAdminScript, "\""+new File(shortcutsDir, PG_START_SHORTCUT).getAbsolutePath()+"\""])
        code = code && executeCommandSilently([markRunAsAdminScript, "\""+new File(shortcutsDir, PG_STOP_SHORTCUT).getAbsolutePath()+"\""])
        return code && executeCommandSilently([markRunAsAdminScript, "\""+new File(shortcutsDir, PG_START_ALL_SHORTCUT).getAbsolutePath()+"\""])
    }

    private String getLinesForStartStopShortcut(String shortcutName, String target){
        File shortcutsDir = new File(installation.getInstallDir(), SHORTCUTS_DIR)
        File shortcut = new File(shortcutsDir, shortcutName)
        File image = new File(bundledPolarion, PG_IMAGE_NAME)
        return getLinesForShortcut(shortcut, NET, target,image)
    }

    private String getLinesForAllShortcut(){
        File shortcutsDir = new File(installation.getInstallDir(), SHORTCUTS_DIR)
        File shortcut = new File(shortcutsDir, PG_START_ALL_SHORTCUT)
        File image = new File(installation.getInstallDir(), PG_ALL_IMAGE_NAME)
        File allTarget = new File(installation.getInstallDir(), PG_START_ALL_TARGET)
        return getLinesForShortcut(shortcut, allTarget.getAbsolutePath(), "", image)
    }

    private String getLinesForShortcut(File link, String target, String arguments, File image){
        return "\nSet oWS = WScript.CreateObject(\"WScript.Shell\")"+
                "\nsLinkFile = \""+link.getAbsolutePath()+"\""+
                "\nSet oLink = oWS.CreateShortcut(sLinkFile)"+
                "\n\toLink.TargetPath = \""+target+"\""+
                "\noLink.Arguments = \""+ arguments +"\""+
                "\n\toLink.IconLocation = \""+image.getAbsolutePath()+"\""+
                "\n\toLink.WorkingDirectory = \""+new File(installation.getInstallDir(), "polarion").getAbsolutePath()+"\""+
                "\noLink.Save\n"
    }
}
