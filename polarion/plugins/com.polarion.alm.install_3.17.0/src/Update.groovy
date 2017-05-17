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

import java.util.Arrays
import java.util.Collections
import java.util.List
import java.util.Map.Entry
import java.nio.file.Paths
import java.nio.file.Files

import com.polarion.alm.install.ConnectorsConfigurationEditor
import com.polarion.alm.install.DescriptorsEditor
import com.polarion.alm.install.EclipseIniEditor
import com.polarion.alm.install.EnumEditor
import com.polarion.alm.install.HttpdConf
import com.polarion.alm.install.InstallationType
import com.polarion.alm.install.PerspectivesEditor
import com.polarion.alm.install.Product
import com.polarion.alm.install.ProjectListEditor
import com.polarion.alm.install.PropertiesEditor
import com.polarion.alm.install.RichPageCreator
import com.polarion.alm.install.ScheduleEditor
import com.polarion.alm.install.ShortcutsEditor
import com.polarion.alm.install.SvnClient
import com.polarion.alm.install.TextFileEditor
import com.polarion.alm.install.TopicsEditor
import com.polarion.alm.install.XMLEditor
import com.polarion.alm.install.Product.ProductEnum
import com.polarion.alm.install.utils.FileHelper
import com.polarion.alm.install.utils.Utils

public class UpdateTool extends Tool {
    private final static int MIN_JAVA_VERSION=8

    private final String OLDEST_SUPPORTED_BUILD = "20080403-0111"

    private PolarionInstallation installation
    private Product currentALM
    private InstallationType installationType

    private boolean updateRepository = true
    private boolean updateBundledSoftware = true
    private boolean markUpdate = true
    private boolean clearCache = true
    private boolean backupImportantData = true

    private UpdateState state

    private String versionNameSuffix = ""

    private String versionJava = "1.8.0_111"
    private String versionApache = "2.4.25"
    private String versionSvn = "1.9.4"
    private String expectedLicenseFormat = "2017"


    public UpdateTool(File updateDir) {
        super(updateDir)
        state = new UpdateState(updateDir)
    }

    public void setUpdateRepository(boolean update) {
        this.updateRepository = update
    }

    public void setUpdateBundledSoftware(boolean update) {
        this.updateBundledSoftware = update
    }

    public void setMarkUpdate(boolean markUpdate) {
        this.markUpdate = markUpdate
    }

    public void setClearCache(boolean clearCache) {
        this.clearCache = clearCache
    }

    public void setBackupImportantData(boolean backupImportantData) {
        this.backupImportantData = backupImportantData
    }

    protected void executeInternal() {
        currentALM = readCurrentProduct(ProductEnum.ALM.getId())

        echoTitle("Polarion Update "+currentALM.getVersionName()+versionNameSuffix, '=')

        verifyIntegrity()

        echo ""
        echo "Please go through this checklist before continuing:"
        echo ""
        echo "- read file HOW_TO_INSTALL_THIS_UPDATE.txt"
        echo ""
        echo "  ==================================================================="
        echo "  IMPORTANT: Please read carefully section PREPARING FOR THE UPDATE."
        echo "  ==================================================================="
        echo ""
        echo "- make sure that Polarion server is not running"
        echo "and nobody (e.g. console window) holds the Polarion"
        echo "installation folder lock"
        echo ""
        echo "- make sure that current user has write permission"
        echo "for the whole Polarion installation folder"
        echo ""
        if (osHelper.isWindows()) {
            echo "- running as Administrator is recommended"
        } else {
            echo "- running as root is recommended"
        }
        echo ""
        pause()

        echo "-----------------"
        echo "Update properties"
        echo "-----------------"
        if(state.getInstallDir() != null) {
            installation = new PolarionInstallation(state.getInstallDir())
        } else {
            installation = inputInstallDir()
            def Product product = installation.getProduct()

            // check product and version
            if(product.isALM()) {
                if(product.isOlderBuildThan(OLDEST_SUPPORTED_BUILD)) {
                    fail "THIS ARCHIVE DOES NOT UPDATE POLARION ALM older than version 3.1.0.\n"+
                            "If you have Polarion ALM version older than 3.1.0 you will need an intermediate\n"+
                            "update to v. 3.1.x before you can update to current version. Please get help\n"+
                            "with such updates at http://www.polarion.com/techsupport."
                }
            }

            if(!Product.isOlderOrEqualVersion(product.getVersion(), currentALM.getVersion())) {
                fail "Installed product version is newer than this update! ("+product.getVersion()+" > "+currentALM.getVersion()+")"
            }

            if(product.getVersion().equals(currentALM.getVersion())) {
                if(!product.isOlderBuildThan(currentALM.getBuild())) {
                    fail "Installed product build is not older than this update! ("+product.getBuild()+" >= "+currentALM.getBuild()+")"
                }
            }

            state.setInstallDir(installation.getInstallDir())
            state.setProductVersion(installation.getProduct().getVersion())
        }
        installationType = installation.getType()

        checkLicense(currentALM)

        convertMultiRepository()

        echo ""
        echo "Summary"
        echo "  Product ................... "+installation.getProduct().getName()
        echo "  Installation type ......... "+installationType
        echo "  Installation directory .... "+installation.getInstallDir().getAbsolutePath()
        if (installationType == InstallationType.CLUSTER_NODE) {
            echo "  Shared directory .......... "+installation.getSharedFolder().getAbsolutePath()
        }

        if (installationType != InstallationType.COORDINATOR) {
            echo "  Maven directory ........... "+installation.getMavenDir()
        }

        if (installation.isMultirepository()) {
            for(String slaveId in installation.getSlaveIds()) {
                echo "  Instance: "+slaveId
                echo "    Repository URL ............ "+installation.getRepository(slaveId)
                echo "    Data directory ............ "+installation.getDataDir(slaveId).getAbsolutePath()
                echo "    Workspace directory ....... "+installation.getWorkspaceDir(slaveId).getAbsolutePath()
                echo "    Polarion-data directory ... "+installation.getPolarionDataDir(slaveId).getAbsolutePath()
            }
        } else {
            if (installationType.hasRepository()) {
                echo "  Repository url ............ "+installation.getRepository(null)
            }
            echo "  Data directory ............ "+installation.getDataDir(null).getAbsolutePath()
            echo "  Workspace directory ....... "+installation.getWorkspaceDir(null).getAbsolutePath()
            echo "  Polarion-data directory ... "+installation.getPolarionDataDir(null).getAbsolutePath()
        }

        if(input == null) {
            echo ""
            ant.input(message:"Proceed with the update?", validargs:"y,n", addproperty:"data.ok")
            echo ""
            if("N".equalsIgnoreCase(antHelper.getProperty("data.ok"))) {
                fail "Update cancelled."
            }
        }

        state.save()
        if (installation.getType()!=InstallationType.COORDINATOR
        && (!installation.usesPostgresql() || isPostgresqlConfigurationIncomplete())){
            upgradeToPostgresql()
            state.save()
            echo "DONE"
            echo ""
        }

        echo ""
        echo "--------------------"
        echo "Update program files"
        echo "--------------------"
        updateProgramFiles()
        echo "DONE"

        echo ""
        echo "------------------------"
        echo "Update SVN configuration"
        echo "------------------------"
        updateSvnConfig(null)
        echo "DONE"

        echo ""
        echo "-----------------"
        echo "Update data files"
        echo "-----------------"
        updateFiles(null)
        echo "DONE"

        if (installation.isMultirepository()) {
            for(String slaveId in installation.getSlaveIds()) {
                echo ""
                echo "Processing instance: "+slaveId
                echo ""
                echo "------------------------"
                echo "Update SVN configuration"
                echo "------------------------"
                updateSvnConfig(slaveId)
                echo "DONE"

                echo ""
                echo "  -----------------"
                echo "  Update data files"
                echo "  -----------------"
                updateFiles(slaveId)
                echo "DONE"

                echo ""
                echo "  -----------------"
                echo "  Update repository"
                echo "  -----------------"
                if(updateRepository) {
                    updateRepo(slaveId)
                    echo "DONE"
                } else {
                    echo "Skipped"
                }
            }
        } else {
            if (installationType.hasRepository()) {
                echo ""
                echo "------------------"
                echo "Update repository"
                echo "------------------"
                if(updateRepository) {
                    updateRepo(null)
                    echo "DONE"
                } else {
                    echo "Skipped"
                }
            }
        }

        if(osHelper.isWindows()) {
            echo ""
            echo "----------------------------------"
            echo "Update bundled software components"
            echo "----------------------------------"

            if (!osHelper.is64Bit()){
                echo "You are running Polarion on 32-bit operating system (OS). Beginning with version 2016, Polarion supports only 64-bit OS, therefore 3rd party components on your existing system will not be updated. Please plan to migrate your Polarion instance to a supported 64-bit system."
                updateBundledSoftware=false
            }
            if(updateBundledSoftware) {
                updateBundled()
            } else {
                echo "Skipped"
            }
        } else {
            String productVersion = state.getProductVersion()
            boolean olderThan372 = Product.isOlderVersion(productVersion, "3.7.2") // Java was updated to Java 7
            if(olderThan372) {
                echo ""
                echo "----------------------------------"
                echo "    Update software components    "
                echo "----------------------------------"
                echo "New versions of 3rd party software components are supported and recommended:"
                echo " Java "+versionJava
                echo ""
                echo "To actually use new version of Java please"
                echo "update manually according to instructions for your distribution."
            }
        }

        setSharedFolderVersion(installation, currentALM.getVersion())

        echo ""
        echo "================"
        echo "Update COMPLETE."
        echo "================"
        echo ""
        pause()
    }


    private boolean isPostgresqlConfigurationIncomplete(){
        return state.isTaskDone(UpdateState.TASK_INSTALL_PG) && !state.isTaskDone(UpdateState.TASK_REGISTER_PG_SERVICE)
    }

    private void failOn32BitSystem(){
        if (osHelper.isWindows() && !osHelper.is64Bit()){
            fail "Upgrade to PostgreSQL is not supported on 32-bit operating systems since Polarion 2016. Please plan to migrate your Polarion instance to a 64-bit operating system."
        }
    }

    private void upgradeToPostgresql(){
        echo ""
        echo "------------------------------"
        echo "Upgrade to PostgreSQL database"
        echo "------------------------------"
        echo ""
        failOn32BitSystem()
        PostgresqlUpdate pgUpdate = osHelper.isWindows()? new PostgresqlWindowsUpdate(this) :new PostgresqlLinuxUpdate(this)
        if (!state.isTaskDone(UpdateState.TASK_INSTALL_PG)){
            pgUpdate.installPostgresql()
            if (pgUpdate.isPostgresqlInstalled()){
                state.markTaskDone(UpdateState.TASK_INSTALL_PG)
            }
        }
        if (!state.isTaskDone(UpdateState.TASK_CONFIGURE_PG)){
            echo ""
            pgUpdate.configurePostgresql()
            if (installation.usesPostgresql()){
                state.markTaskDone(UpdateState.TASK_CONFIGURE_PG)
            }else{
                fail "Polarion server cannot start unless PostgreSQL is properly configured."
            }
        }
        if(osHelper.isWindows()){
            echo ""
            echo "--------------------------------------------"
            echo "Optimizing PostgreSQL database for Polarion"
            echo "--------------------------------------------"
            echo ""
            if (!state.isTaskDone(UpdateState.TASK_OPTIMIZE_PG)){
                pgUpdate.optimizePostgresql()
                if (installation.isPostgresqlOptimized()){
                    state.markTaskDone(UpdateState.TASK_OPTIMIZE_PG)
                }else{
                    warn "Automatic optimization of the PostgreSQL database failed."
                    echo "Manual optimization is recommended for optimal performance. Please see \"Optimizing the PostgreSQL Database\" in the installation guide for Windows (or online Help)."
                }
            }

        }

        if (!state.isTaskDone(UpdateState.TASK_REGISTER_PG_SERVICE)){
            echo ""
            pgUpdate.registerService()
            if (pgUpdate.isPostgresqlRegistered()){
                echo "Starting PostgreSQL..."
                if (pgUpdate.startPostgresql()){
                    state.markTaskDone(UpdateState.TASK_REGISTER_PG_SERVICE)
                    ant.echo "PostgreSQL for Polarion was started."
                }else{
                    fail "PostgreSQL could not be started. Please see the update log for more details."
                }
            }else{
                fail "Polarion was unable to register the service for PostgreSQL. Please see the update log for more details."
            }
        }
    }

    private checkLicense(Product currentALM) {
        if (!installationType.hasLocalLicense()) {
            return
        }

        File licenseDir = new File(installation.getInstallDir(), "polarion/license")
        boolean isLicenseValid = false
        File licenseFile = null
        if(licenseDir.exists() && licenseDir.isDirectory()) {
            File[] files = licenseDir.listFiles()
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getName().endsWith(".lic") &&
                Product.isLicenseValid(files[i].getAbsolutePath(), expectedLicenseFormat)) {
                    isLicenseValid = true
                    break
                }
            }
        }

        if(!isLicenseValid && input == null) {
            echo ""
            echo "======="
            echo "WARNING"
            echo "======="
            echo "We're sorry, but the license stored in"
            echo "  "+licenseDir.getAbsolutePath()
            echo "is not compatible with version "+currentALM.getVersionName()+"."
            echo "You will have to activate Polarion after the update is finished."
            echo "For this you will need License Key (for online activation) or"
            echo "Polarion license file (for offline activation)."
            echo ""
            echo "You should have received your License Key together with the confirmation"
            echo "of your purchase. If you need to activate Polarion in an offline mode and"
            echo "do not have Polarion license file yet then follow instructions provided"
            echo "by Polarion user interface after the update has finished."
            echo ""
            ant.input(message:"Proceed with the update?", validargs:"y,n", addproperty:"license.ok")
            echo ""
            if("N".equalsIgnoreCase(antHelper.getProperty("license.ok"))) {
                fail "Update cancelled."
            }
        }
    }

    //############################
    //### UPDATE PROGRAM FILES ###
    //############################
    public void updateProgramFiles() {
        if (state.isTaskDone(UpdateState.TASK_UPDATE_PROGRAM_FILES)) {
            return
        }

        String productDir = Product.getProductFolder(installation.getProduct().getId())
        File installDir = installation.getInstallDir()
        File backupDir = getBackupDir()

        File mavenDir = installation.getMavenDir()
        File mavenBackupDir = new File(backupDir, "maven")

        moveDirIfExists(new File(installDir, "polarion/features"), new File(backupDir, "polarion/features"))
        moveDirIfExists(new File(installDir, "polarion/plugins"), new File(backupDir, "polarion/plugins"))
        moveDirIfExists(new File(installDir, "polarion/install"), new File(backupDir, "polarion/install"))
        moveDirIfExists(new File(installDir, "polarion/SDK"), new File(backupDir, "polarion/SDK"))
        moveDirIfExists(new File(installDir, "polarion/diagtool"), new File(backupDir, "polarion/diagtool"))

        ant.move(toDir:backupDir.getAbsolutePath()) {
            fileset(dir:installDir.getAbsolutePath()) {
                include(name:"polarion/configuration/patch*.txt")
                include(name:"polarion/startup.jar")
                include(name:"polarion_alm_install_guide.pdf")
                include(name:"polarion_alm_linux_install_guide.pdf")
            }
        }

        moveDirIfExists(new File(mavenDir, "distribution"), new File(mavenBackupDir, "distribution"))
        moveDirIfExists(new File(mavenDir, "repository"), new File(mavenBackupDir, "repository"))

        if (mavenDir.exists()) {
            ant.move(toDir:mavenBackupDir.getAbsolutePath()) {
                fileset(dir:mavenDir.getAbsolutePath()) {
                    include(name:"calculations-plugin-registry.xml")
                    include(name:"plugin-registry.xml")
                }
            }
        }

        ant.copy(todir:installDir.getAbsolutePath()) {
            fileset(dir:homeDir.getAbsolutePath()) {
                include(name:"polarion/features/**/*")
                include(name:"polarion/plugins/**/*")
                include(name:"polarion/startup.jar")

                include(name:"polarion/install/**/*")
                include(name:"maven/**/*")
            }
            fileset(dir:getAbsolutePath(productDir)) {
                include(name:"polarion/install/**/*")
                include(name:"*.html")
                include(name:"*.pdf")
            }
        }

        File extensionsDir = new File(installDir, "polarion/extensions")
        if (!extensionsDir.exists()) {
            ant.copy(todir:installDir.getAbsolutePath()) {
                fileset(dir:homeDir.getAbsolutePath()) { include(name:"polarion/extensions/**/*") }
            }
            installation.setFilePermissions(extensionsDir)
        }

        if(!osHelper.isWindows()) {
            ant.copy(todir:installDir.getAbsolutePath()) {
                fileset(dir:getAbsolutePath("linux")) { include(name:"polarion/install/**/*") }
            }
        }

        ant.copy(todir:installDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("")) { include(name:"polarion/SDK/**/*") }
        }

        ant.copy(todir:installDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("docs")) {
                include(name:"*.html")
                include(name:"*.pdf")
                exclude(name:"install_guide_*.pdf")
            }
        }
        if(osHelper.isWindows()) {
            ant.copy(file:getAbsolutePath("docs/install_guide_win.pdf"),todir:installDir.getAbsolutePath(),overwrite:"true")
        } else { // linux
            ant.copy(file:getAbsolutePath("docs/install_guide_linux.pdf"),todir:installDir.getAbsolutePath(),overwrite:"true")
        }

        ant.copy(todir:installDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("")) { include(name:"polarion/diagtool/**/*") }
        }
        ant.chmod(file:new File(installDir, "polarion/diagtool/run.sh").getAbsolutePath(), perm:"ugo+rx")

        if(!osHelper.isWindows()) {
            installation.executeUnixShell(installDir, "chmod ugo+rx ./polarion/plugins/com.polarion.alm.ui_*/phantomjs/phantomjs")
        }

        if (Product.isOlderVersion(state.getProductVersion(), "3.4.0")) {
            String javaPath = null
            if (osHelper.isWindows()) {
                // read Java path from shutdown.bat
                javaPath = readJavaPath(new File(installDir, "polarion/shutdown.bat"))
                if (javaPath == null) {
                    javaPath = new File(installDir, "bundled/java").getAbsolutePath()
                }
                javaPath = Utils.backslashesToSlashes(javaPath)
            } else {
                // TODO
            }

            if (osHelper.isWindows()) {
                // update scripts for creation/initialization of repository (DPP-10173)
                ant.move(toDir:new File(backupDir, "polarion").getAbsolutePath()) {
                    fileset(dir:new File(installDir, "polarion").getAbsolutePath()) {
                        include(name:"create_repository.bat")
                        include(name:"create_demodata.bat")
                        include(name:"init_cfg.bat")
                        include(name:"init_demodata.bat")
                        include(name:"install.exe")
                        include(name:"install.ini")
                    }
                }
                ant.copy(toDir:new File(installDir, "polarion").getAbsolutePath()) {
                    fileset(dir:getAbsolutePath("win32/polarion")) {
                        include(name:"init_repository.bat")
                        include(name:"create_repository.bat")
                    }
                }
                ant.copy(todir:new File(installDir, "polarion").getAbsolutePath()) {
                    fileset(dir:getAbsolutePath("win32/polarion")) { include(name:"exec.bat.in") }
                    mapper(type:"glob",from:"*.in",to:"*")
                }
                ant.replace(dir:new File(installDir, "polarion").getAbsolutePath()) {
                    include(name:"exec.bat")
                    replacefilter(token:"#JrePath#",value:javaPath)
                }
            }
        }

        if(Product.isOlderVersion(state.getProductVersion(), "3.4.2")) {
            if (osHelper.isWindows()) {
                EclipseIniEditor ed = new EclipseIniEditor()
                ed.open(new File(installDir, "polarion/polarion.ini"))
                ed.addJavaProperty("com.polarion.logs.main", new File(installDir, "data/logs/main").getAbsolutePath())
                ed.save()
            }
        }

        if(Product.isOlderVersion(state.getProductVersion(), "3.4.2")) {
            if (osHelper.isWindows()) {
                // add Polarion service support
                ant.copy(todir:new File(installDir, "polarion").getAbsolutePath()) {
                    fileset(dir:getAbsolutePath("win32/polarion")) { include(name:"service.bat") }
                }
                EclipseIniEditor ed = new EclipseIniEditor()
                ed.open(new File(installDir, "polarion/polarion.ini"))
                ed.setFirstLine("\"WARNING: If Polarion is installed as a service, it is necessary to reinstall the service for changes in this file to take effect. Run service.bat to do this. \"")
                ed.save()
            }
        }

        if (Product.isOlderVersion(state.getProductVersion(), "3.8.0")) {
            ant.move(toDir:new File(backupDir, "polarion").getAbsolutePath()) {
                fileset(dir:new File(installDir, "polarion").getAbsolutePath()) { include(name:"create_instance.bat") }
            }
        }

        if (osHelper.isWindows()) {
            updateWindowsBatches()
            updateWindowsExeAndIcoFiles();
        } else {
            updateLinuxScripts()
        }

        ant.move(toDir:new File(backupDir, "polarion").getAbsolutePath()) {
            fileset(dir:new File(installDir, "polarion").getAbsolutePath()) {
                include(name:".bnum")
                include(name:".eclipseproduct")
            }
        }
        ant.copy(file:getAbsolutePath(productDir+"/polarion/.eclipseproduct"), todir:new File(installDir, "polarion").getAbsolutePath())

        state.markTaskDone(UpdateState.TASK_UPDATE_PROGRAM_FILES)
    }

    private void moveDirIfExists(File srcDir, File destDir) {
        if(srcDir.exists()) {
            ant.move(file:srcDir.getAbsolutePath(),tofile:destDir.getAbsolutePath())
        }
    }

    private String readJavaPath(File batchFile) {
        String javaPath = null
        if (batchFile.isFile()) {
            batchFile.eachLine { ln ->
                if ( ln.startsWith("set JAVA_HOME")) {
                    println ">"+ln
                    String path = ln.substring("set JAVA_HOME".length()).trim()
                    if (path.startsWith("=")) {
                        javaPath = path.substring(1)
                    }
                }
            }
        }
        return javaPath
    }

    private void updateLinuxScripts() {
        File installDir = installation.getInstallDir()
        ant.copy(file:getAbsolutePath("linux/etc/functions.sh"),todir:new File(installDir, "etc").getAbsolutePath(),overwrite:"true")
        FileHelper.replaceFileContent(
                new File(getAbsolutePath("linux/etc/config.sh.in")),
                new File(installDir, "etc/config.sh"), "export PSVN_debug")
        FileHelper.replaceFileContent(
                new File(getAbsolutePath("linux/bin/polarion.init.in")),
                new File(installDir, "bin/polarion.init"), "start() {")
        updatePostgresqlInitScript()
        ant.copy(file:getAbsolutePath("linux/bin/install_polarion_service.sh"),todir:new File(installDir, "bin").getAbsolutePath(),overwrite:"true")
        ant.copy(file:getAbsolutePath("linux/bin/uninstall_polarion_service.sh"),todir:new File(installDir, "bin").getAbsolutePath(),overwrite:"true")
    }

    private void updatePostgresqlInitScript(){
        File postgresqlInitScriptOriginal = new File(getAbsolutePath("linux/bin/postgresql-polarion.init"))
        File postgresqlInitScriptCopy = new File(getAbsolutePath("linux/bin/postgresql-polarion.init.temp"))
        ant.copy(file: postgresqlInitScriptOriginal, tofile:postgresqlInitScriptCopy, overwrite:"true")
        PostgresqlLinuxUpdate.customizeInitScriptOsSpecificStatements(postgresqlInitScriptCopy, osHelper)
        FileHelper.replaceFileContent(
                postgresqlInitScriptCopy,
                new File(installation.getInstallDir(), "bin/postgresql-polarion.init"), "start() {")
    }

    private void updateWindowsBatches() {
        File installDir = installation.getInstallDir()
        FileHelper.replaceFileContent(
                new File(getAbsolutePath("win32/polarion/exec.bat.in")),
                new File(installDir, "polarion/exec.bat"), "set SCRIPT=%1")
        FileHelper.replaceFileContent(
                new File(getAbsolutePath("win32/polarion/shutdown.bat.in")),
                new File(installDir, "polarion/shutdown.bat"), "@echo off")
    }

    private void updateWindowsExeAndIcoFiles() {
        File installDir = installation.getInstallDir()
        ant.copy(todir:installDir.getAbsolutePath(),overwrite:"true") {
            fileset(dir:getAbsolutePath("win32")) {
                include(name:"polarion/*.ico")
                include(name:"polarion/*.exe")
            }
        }
        if (new File(installDir, "polarion/polarion_service.exe").exists()) {
            ant.copy(todir:new File(installDir, "polarion").getAbsolutePath(),overwrite:"true",flatten:"true") {
                fileset(dir:getAbsolutePath("polarion")) { include(name:"plugins/com.polarion.alm.install_*/resources/polarion_service.exe") }
            }
        }
    }

    //#########################
    //### UPDATE SVN CONFIG ###
    //#########################
    public void updateSvnConfig(String slaveId) {
        if (state.isTaskDone(UpdateState.TASK_UPDATE_SVN_CONFIG, slaveId)) {
            return
        }

        String productVersion = state.getProductVersion()
        File dataDir = installation.getDataDir(slaveId)

        if (Product.isOlderVersion(productVersion, "3.10.2")) {
            File accessFile = new File(dataDir, "svn/access")
            if (!accessFile.exists()) {
                fail "SVN access file not found at " + accessFile
            }

            Properties accessProperties = new Properties()
            accessFile.withInputStream { accessProperties.load(it) }
            if (!accessProperties.containsKey("[/.polarion/oslc/configuration.xml]")) {
                accessFile.append("\n[/.polarion/oslc/configuration.xml]\n* =\npolarion = rw\n@admin = rw\n")
            }
        }

        state.markTaskDone(UpdateState.TASK_UPDATE_SVN_CONFIG, slaveId)
    }

    //####################
    //### UPDATE FILES ###
    //####################
    public void updateFiles(String slaveId) {
        if (state.isTaskDone(UpdateState.TASK_UPDATE_FILES, slaveId)) {
            return
        }

        File dataDir = installation.getDataDir(slaveId)
        File workspaceDir = installation.getWorkspaceDir(slaveId)
        File polarionDataDir = installation.getPolarionDataDir(slaveId)

        File dataBackupDir = new File(backupDir, (slaveId != null) ? "data/multi-instance/"+slaveId : "data")
        File workspaceBackupDir = new File(dataBackupDir, "workspace")
        File polarionDataBackupDir = new File(workspaceBackupDir, "polarion-data")

        String productVersion = state.getProductVersion()


        if (slaveId == null && installationType == InstallationType.CONTROLLER) {
            if (!productVersion.equals(currentALM.getVersion())) {
                File coordinatorData = installation.getCoordinatorDataDir()
                echo "Ensuring reindex by deleting " + coordinatorData
                moveDirIfExists(coordinatorData, new File(backupDir, "data/workspace/polarion-data"))
            }
        } else if (slaveId == null && installationType == InstallationType.COORDINATOR) {
            if (!productVersion.equals(currentALM.getVersion())) {
                echo "Ensuring reindex by deleting " + polarionDataDir
                moveDirIfExists(polarionDataDir, polarionDataBackupDir)
            }
        } else {
            boolean fullReindex = Product.isOlderVersion(productVersion, currentALM.getVersion())
            if (fullReindex) {
                echo "Ensuring reindex by deleting " + polarionDataDir
                moveDirIfExists(polarionDataDir, polarionDataBackupDir)
                if (installationType == InstallationType.CLUSTER_NODE) {
                    File sharedActivitiesIndexFolder = installation.getSharedActivitiesIndexFolder()
                    if (sharedActivitiesIndexFolder != null) {
                        moveDirIfExists(sharedActivitiesIndexFolder, new File(backupDir, "shared/data/workspace/polarion-data/index"))
                    }
                }
            } else {
                if (backupImportantData) {
                    if (polarionDataDir.exists()) {
                        ant.copy(todir:polarionDataBackupDir.getAbsolutePath()) {
                            fileset(dir:polarionDataDir.getAbsolutePath()) {
                                include(name:"projectGroups.list")
                                include(name:"repos.properties")
                                include(name:"index/**/*")
                                include(name:"object-maps/**/*")
                                include(name:"promotion-logs/**/*")
                                include(name:"database/**/*")
                            }
                        }
                    }
                }
                if (clearCache) {
                    moveDirIfExists(new File(polarionDataDir, "cache"), new File(polarionDataBackupDir, "cache"))
                }
            }
        }

        moveDirIfExists(new File(dataDir, "maven-repo"), new File(dataBackupDir, "maven-repo"))
        moveDirIfExists(new File(workspaceDir, ".config"), new File(workspaceBackupDir, ".config"))
        moveDirIfExists(new File(workspaceDir, ".metadata/.plugins"), new File(workspaceBackupDir, ".metadata/.plugins"))

        moveDirIfExists(new File(polarionDataDir, "ui-data"), new File(workspaceDir, "ui-data"))

        if (Product.isOlderVersion(state.getProductVersion(), "3.5.0")) {
            // delete ui-data in incompatible format
            moveDirIfExists(new File(workspaceDir, "ui-data"), new File(workspaceBackupDir, "ui-data"))
        }
        if(markUpdate) {
            createUpdateMarkFile(polarionDataDir)
        }

        state.markTaskDone(UpdateState.TASK_UPDATE_FILES, slaveId)
    }

    //###################
    //### UPDATE REPO ###
    //###################
    public void updateRepo(String slaveId) {
        if (state.isTaskDone(UpdateState.TASK_UPDATE_REPO, slaveId)) {
            return
        }

        if (installationType == InstallationType.CLUSTER_NODE) {
            if (currentALM.getVersion().equals(getSharedFolderVersion(installation))) {
                echo "Already done during update of another node."
                return
            }
        }

        String repo = installation.getRepository(slaveId)
        def SvnClient svn = inputRepositoryCredentials(repo)

        ant.delete(dir:getAbsolutePath("temp"))

        // get list of projects
        ant.mkdir(dir:getAbsolutePath("temp/_root/.polarion"))
        svn.export("/.polarion/projects.xml", new File(homeDir, "temp/_root/.polarion"))
        def ProjectListEditor projectsEditor = new ProjectListEditor()
        projectsEditor.open(new File(homeDir, "temp/_root/.polarion/projects.xml"))
        def Map projects = projectsEditor.readProjects()
        ant.delete(file:getAbsolutePath("temp/_root/.polarion/projects.xml"))

        // update projects
        for(Iterator it = projects.entrySet().iterator(); it.hasNext();) {
            def Entry entry = (Entry) it.next()
            def String projectId = (String) entry.getKey()
            def String projectPath = (String) entry.getValue()

            if (!state.isTaskDone(UpdateState.TASK_UPDATE_CONTEXT, slaveId+":"+projectId)) {
                updateRepoPath(projectId, projectPath, svn)
                state.markTaskDone(UpdateState.TASK_UPDATE_CONTEXT, slaveId+":"+projectId)
            }
        }

        // update repo root
        updateRepoPath(null, "", svn)

        state.markTaskDone(UpdateState.TASK_UPDATE_REPO, slaveId)
    }

    private void updateRepoPath(String projectId, String repoPath, SvnClient svn) {
        def boolean root = (projectId == null)
        def String localPath
        if(projectId != null) {
            echo "Processing project "+projectId+" on path "+repoPath
            localPath = "temp/"+projectId
        } else {
            echo "Processing repository root"
            localPath = "temp/_root"
        }

        Product product = installation.getProduct()
        String productVersion = state.getProductVersion()
        String productDir = Product.getProductFolder(product.getId())

        if (Product.isOlderVersion(productVersion, "3.17.0")) {
            def String oslcSemanticEnum = ".polarion/oslc/oslc-semantics.xml"
            ant.copy(todir:getAbsolutePath(localPath),overwrite:"true") {
                fileset(dir:getAbsolutePath(productDir+"/default-data")) { include(name:oslcSemanticEnum) }
            }            
		}

        if (Product.isOlderVersion(productVersion, "3.10.3")) {
            addConfiguration(".polarion/oslc/oslc-semantics.xml", localPath, repoPath, false, svn)
            addConfiguration(".polarion/oslc/oslc-mapping.xml", localPath, repoPath, false, svn)

            def String oslcLinkRoleEnum = ".polarion/tracker/fields/oslc-link-role-enum.xml"
            ant.copy(todir:getAbsolutePath(localPath),overwrite:"true") {
                fileset(dir:getAbsolutePath(productDir+"/default-data")) { include(name:oslcLinkRoleEnum) }
            }
        }

        if (Product.isOlderVersion(productVersion, "3.10.2")) {
            if (root) {
                addConfiguration(".polarion/tracker/fields/oslc-link-role-enum.xml", localPath, repoPath, false, svn)
                performEdit(repoPath, ".polarion/jobs/schedule.xml", new ScheduleEditor(), false, localPath, svn, { editor ->
                    editor.addJob("Cleanup of Activities", "activityCleanerJob", "0 0 0 ? * SUN")
                })
            }
        }

        if (Product.isOlderVersion(productVersion, "3.10.1")) {
            String relativeRichSpacesDir = ".polarion/pages/spaces"
            String richSpacesDir = repoPath + "/" + relativeRichSpacesDir
            String wikiSpacesDir = repoPath + "/_wiki"

            List<String> richSpaceIds = svn.exists(richSpacesDir) ? Arrays.asList(svn.listSubfolders(richSpacesDir)) : Collections.emptyList()
            List<String> wikiSpaceIds = svn.exists(wikiSpacesDir) ? Arrays.asList(svn.listSubfolders(wikiSpacesDir)) : Collections.emptyList()
            Set<String> spaceIdsToProcess = new HashSet<String>(richSpaceIds)
            spaceIdsToProcess.addAll(wikiSpaceIds)
            spaceIdsToProcess.add("_default") //Project / Repository root can have no space folders at all (ex. empty project template), add _default space manually to ensure converting Project/Repository Home Page

            for(String spaceId in spaceIdsToProcess) {
                if(!spaceId.isEmpty()){
                    String relativeRichSpaceHomeDir = relativeRichSpacesDir+"/"+spaceId+"/Home"
                    String richSpaceHomeDir = richSpacesDir+"/"+spaceId+"/Home"
                    String wikiSpaceHomeDir = wikiSpacesDir+"/"+spaceId+"/Home"

                    String relativeRichSpaceHomePage = relativeRichSpaceHomeDir + "/page.xml"
                    String richSpaceHomePage = richSpaceHomeDir + "/page.xml"
                    String wikiSpaceHomePage = wikiSpaceHomeDir + "/page.xml"

                    if(!svn.exists(richSpaceHomePage) && !svn.exists(wikiSpaceHomePage)) {
                        //Generate new Rich Home page
                        performEdit(repoPath, relativeRichSpaceHomePage, new RichPageCreator(spaceId), true, localPath, svn, { editor ->
                            editor.setPageParameters()
                        })
                    }
                }
            }
        }

        if (Product.isOlderVersion(productVersion, "3.10.0")) {
            if (root) {
                addConfiguration(".polarion/documents/merge/actions.xml", localPath, repoPath, false, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.9.0")) {
            if (root) {
                def connectorEditor = new ConnectorsConfigurationEditor(new File(getAbsolutePath(productDir+"/default-data/.polarion/synchronizer/configuration.xml")))
                performEdit(repoPath, ".polarion/synchronizer/configuration.xml", connectorEditor, true, localPath, svn, { editor ->
                    editor.addTemplateSyncPairs()
                })
                addConfiguration(".polarion/pages", localPath, repoPath, true, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.6.3")) {
            if (root) {
                addConfiguration(".polarion/testing/configuration/test-step-keys-enum.xml", localPath, repoPath, false, svn)
                addConfiguration(".polarion/testing/configuration/templates/TestStepsTemplate.xlsx", localPath, repoPath, false, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.6.1")) {
            if (root) {
                addConfiguration(".polarion/portal/languages.xml", localPath, repoPath, false, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.6.0")) {
            if (root) {
                addConfiguration(".polarion/testing", localPath, repoPath, true, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.5.2")) {
            if (root) {
                addConfiguration(".polarion/tracker/export_templates/excel/Basic.xlsx", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/excel/Empty.xlsx", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/excel/Time Report.xlsx", localPath, repoPath, false, svn)
            }
        }

        if (Product.isOlderVersion(productVersion, "3.5.0")) {
            if (root && (product.isALM() || product.isRequirements())) {
                addConfiguration(".polarion/tracker/import_configurations/word", localPath, repoPath, true, svn)
                addConfiguration(".polarion/tracker/templates", localPath, repoPath, true, svn)
            }
        }

        if(Product.isOlderVersion(productVersion, "3.4.0")) {
            performEdit(repoPath, ".polarion/portal/shortcuts.xml", new ShortcutsEditor(), false, localPath, svn, { editor ->
                editor.updateShortcutsFormat()
            })
            if (root) {
                String usersUrl = repoPath+"/.polarion/user-management/users"
                if (svn.exists(usersUrl)) {
                    String[] userIds = svn.listSubfolders(usersUrl)
                    for(String userId in userIds) {
                        performEdit(repoPath, ".polarion/user-management/users/"+userId+"/shortcuts.xml", new ShortcutsEditor(), false, localPath, svn, { editor ->
                            editor.updateShortcutsFormat()
                        })
                    }
                }
            }
        }

        if(Product.isOlderVersion(productVersion, "3.4.0") && (product.isALM() || product.isRequirements())) {
            def String hatsPath = repoPath+"/.polarion/hats"
            def String localHatsPath = localPath+"/.polarion/hats"
            if(svn.exists(hatsPath)) {
                svn.export(hatsPath, new File(homeDir, localHatsPath))
                ant.delete {
                    fileset(dir:getAbsolutePath(localHatsPath),excludes:"**/topics.xml,**/perspectives.xml")
                }

                File[] files = new File(homeDir, localHatsPath).listFiles()
                boolean addRepositoryTopicDefault = true
                File perspectivesFileDefault = new File(homeDir, localHatsPath+"/_default/portal/perspectives.xml")
                if (perspectivesFileDefault.exists()) {
                    PerspectivesEditor ed = new PerspectivesEditor()
                    ed.open(perspectivesFileDefault)
                    addRepositoryTopicDefault = ed.hasRepository()
                }

                for (int i = 0; i < files.length; i++) {
                    File file = files[i]
                    if(file.isDirectory()) {
                        File topicsFile = new File(file, "portal/topics.xml")
                        File perspectivesFile = new File(file, "portal/perspectives.xml")

                        boolean addRepositoryTopic = addRepositoryTopicDefault
                        if (perspectivesFile.exists()) {
                            PerspectivesEditor ed = new PerspectivesEditor()
                            ed.open(perspectivesFile)
                            addRepositoryTopic = ed.hasRepository()
                            svn.markForDelete(perspectivesFile)
                        }

                        if(topicsFile.exists()) {
                            TopicsEditor editor = new TopicsEditor()
                            editor.open(topicsFile)

                            // remove obsolete comment
                            editor.replaceComments("The <projects> element defines the topics", null)

                            // update documentation comment
                            String docComment = null
                            if (file.getName().equals("_default")) {
                                File defaultTopicsFile = new File(homeDir, productDir+"/default-data/.polarion/hats/_default/portal/topics.xml")
                                if (defaultTopicsFile.exists()) {
                                    docComment = Utils.getMainComment(defaultTopicsFile)
                                }
                            }
                            editor.replaceComments("The \"My Hat\" feature", docComment)

                            if(Product.isOlderVersion(productVersion, "3.2.0") && product.isALM()) {
                                editor.renameTopic("overview", "home")
                                editor.addTopic("modules")
                            }

                            editor.addTopic(TopicsEditor.TOPIC_DOCUMENTS)
                            if (addRepositoryTopic) {
                                editor.addTopic(TopicsEditor.TOPIC_REPOSITORY)
                            }
                            editor.addTopic(TopicsEditor.TOPIC_GLOBAL_SHORTCUTS)
                            editor.addTopic(TopicsEditor.TOPIC_PROJECT_SHORTCUTS)
                            editor.addTopic(TopicsEditor.TOPIC_USER_SHORTCUTS)

                            if(!editor.save()) {
                                topicsFile.delete()
                            }
                        }
                    }
                }
            }
        }

        if(Product.isOlderVersion(productVersion, "3.3.3") && (product.isALM() || product.isRequirements())) {
            if (root) {
                performEdit(repoPath, ".polarion/tracker/export_templates/Sample-fo.xsl", new XMLEditor(), false, localPath, svn, { editor ->
                    editor.addAttribute("fo:root", "font-family", "Arial")
                })
            }
        }

        if(Product.isOlderVersion(productVersion, "3.2.0") && product.isALM()) {
            if(root) {
                if(!svn.exists("/library")) {
                    performEdit(repoPath, ".polarion/projects.xml", new ProjectListEditor(), false, localPath, svn, { editor ->
                        if(editor.markProject("library", "/library")) {
                            ant.copy(todir:getAbsolutePath(localPath)) {
                                fileset(dir:getAbsolutePath(productDir+"/default-data")) { include(name:"library/**/*") }
                            }
                        } else {
                            warn "Path "+svn.getUrl("library")+" is already marked as project, or project with ID 'library' already exists. Project 'Module Library' will not be added."
                        }
                    })
                } else {
                    warn "Path "+svn.getUrl("library")+" already exists in the repository. Project 'Module Library' will not be added"
                }
            }
        }

        if(Product.isOlderVersion(productVersion, "3.2.1") && product.isALM()) {
            performEdit(repoPath, ".polarion/jobs/schedule.xml", new ScheduleEditor(), false, localPath, svn, { editor ->
                editor.convertCalculationToJob("updateplan", "update.plan")
            })

            performEdit(repoPath, ".polarion/reports/descriptors.xml", new DescriptorsEditor(), false, localPath, svn, { editor ->
                editor.removeCustomDescriptor("updateplan")
            })
        }

        if(Product.isOlderVersion(productVersion, "3.3.0") && root) {
            addConfiguration(".polarion/tracker/fields/calculated-fields.xml", localPath, repoPath, false, svn)
            addConfiguration(".polarion/tracker/fields/work-record-type-enum.xml", localPath, repoPath, false, svn)
            addConfiguration(".polarion/wiki/export.xml", localPath, repoPath, false, svn)

            performEdit(repoPath, ".polarion/reports/calculation.properties", new PropertiesEditor(), true, localPath, svn, { editor ->
                editor.addProperty("polarion.build.maven.extra.jvm.options", "-Xmx640m")
            })

            if(product.isRequirements() || product.isALM()) {
                if(svn.exists("/library")) {
                    addConfiguration("library/modules/Software Test Case Specification", localPath, repoPath, true, svn)
                    addConfiguration("library/modules/Software Test Design Specification", localPath, repoPath, true, svn)
                    addConfiguration("library/modules/Software Test Plan", localPath, repoPath, true, svn)
                    addConfiguration("library/modules/Software Test Procedure Specification", localPath, repoPath, true, svn)
                    addConfiguration("library/modules/Test Plan", localPath, repoPath, true, svn)
                    addConfiguration("library/modules/Test Specification", localPath, repoPath, true, svn)
                    addConfiguration("library/.polarion/tracker/fields/testcase-status-enum.xml", localPath, repoPath, true, svn)
                    addConfiguration("library/.polarion/tracker/workflow/testcase-workflow.xml", localPath, repoPath, true, svn)
                    performEdit(repoPath, "library/.polarion/tracker/fields/workitem-type-enum.xml", new EnumEditor(), true, localPath, svn, { editor ->
                        editor.addOption("testcase", "Test Case", null, "A test case.", "images/polarion/enums/type_test.gif", 6)
                    })
                } else {
                    warn "Path "+svn.getUrl("library")+" does not exist in the repository - new library modules will not be added."
                }
                addConfiguration(".polarion/tracker/export_templates/Copy-xml.xsl", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/Sample-fo.xsl", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/Sample-html.xsl", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/Sample-txt.xsl", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/Test Log.xls", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/export_templates/xhtml2fo.xsl", localPath, repoPath, false, svn)
                addConfiguration(".polarion/tracker/fields/testcase-custom-fields.xml", localPath, repoPath, false, svn)
                performEdit(repoPath, ".polarion/tracker/fields/workitem-type-enum.xml", new EnumEditor(), true, localPath, svn, { editor ->
                    editor.addOption("testcase", "Test Case", null, "A test case.", "images/polarion/enums/type_test.gif", 8)
                })
            }
            if(product.isRequirements()) {
                performEdit(repoPath, ".polarion/tracker/fields/workitem-link-role-enum.xml", new EnumEditor(), true, localPath, svn, { editor ->
                    editor.addOption("derived_from", "is derived from", "is derived by", null, null, 7)
                })
            }

            if(product.isALM()) {
                performEdit(repoPath, ".polarion/tracker/fields/workitem-link-role-enum.xml", new EnumEditor(), true, localPath, svn, { editor ->
                    editor.addOption("refines", "refines", "is refined by", null, null, 7)
                    editor.setOptionProperty("refines", "parent", "true")
                    editor.addOption("derived_from", "is derived from", "is derived by", null, null, 8)
                })
            }
        }

        if(removeEmptyDirs(new File(homeDir, localPath))) { // at least one file is left
            echo "Committing changes..."
            def String msg = root ? "update repository root" : "update project "+projectId
            long revision = svn.load(repoPath, new File(homeDir, localPath), true, "Polarion commit: "+timestamp+" - "+msg)
            echo "revision: "+revision
        } else {
            echo "Nothing to commit"
        }
    }

    private void addConfiguration(String repoPathRelative, String localRoot, String repoRoot, boolean folder, SvnClient svn) {
        def String fullRepoPath = Utils.empty(repoRoot) ? repoPathRelative : repoRoot+"/"+repoPathRelative
        if(!svn.exists(fullRepoPath)) {
            def String name = folder ? repoPathRelative+"/**/*" : repoPathRelative
            Product product = installation.getProduct()
            String productDir = Product.getProductFolder(product.getId())
            ant.copy(todir:getAbsolutePath(localRoot)) {
                fileset(dir:getAbsolutePath(productDir+"/default-data")) { include(name:name) }
            }
        } else {
            warn "Path "+svn.getUrl(fullRepoPath)+" already exists in the repository - will not be added."
        }
    }

    //######################
    //### UPDATE BUNDLED ###
    //######################
    private void updateBundled() {
        String productVersion = state.getProductVersion()
        boolean updateJava = Product.isOlderVersion(productVersion, "3.17.0")
        boolean updateApacheSvn = Product.isOlderVersion(productVersion, "3.17.0")

        if(!updateJava && !updateApacheSvn) {
            echo "No updates available."
            return
        }

        echo ""
        echo "New versions of 3rd party software components are bundled"
        echo "with current Polarion and are highly recommended:"
        if (updateJava) {
            echo " Java "+versionJava
        }
        if (updateApacheSvn) {
            echo " Subversion "+versionSvn
            echo " Apache "+versionApache
        }
        echo ""
        echo "This script can prepare the update of these components."

        if (updateJava) {
            echo ""
            echo "----"
            echo "Java"
            echo "----"
            echo ""

            int oldJavaVersion=getOldJavaVersion()
            boolean isUpToDate=oldJavaVersion >= MIN_JAVA_VERSION
            if(!isUpToDate){
                println "As of version "+currentALM.getVersionName()+" Polarion has dropped the support of Java "+ oldJavaVersion +". To ensure full functionality of Polarion the update of the bundled Java to version "+MIN_JAVA_VERSION+" is required."
            }
            ant.input(message:"Do you want to prepare update of bundled Java now?",
            validargs:"y,n",addproperty:"update.java")
            echo ""
            if("y".equalsIgnoreCase(antHelper.getProperty("update.java"))) {
                if(updateJava()) {
                    echo "DONE"
                } else {
                    echo "SKIPPED"
                }
            } else {
                if(isUpToDate){
                    echo "SKIPPED"
                }else{
                    echo "Please manually update your system to Java version "+MIN_JAVA_VERSION+" after the update is finished."
                }
            }
            echo ""
            pause()
        }

        if (updateApacheSvn) {
            echo ""
            echo "---------------------"
            echo "Apache and Subversion"
            echo "---------------------"
            echo ""
            ant.input(message:"Do you want to prepare update of bundled Apache and Subversion now?",
            validargs:"y,n",addproperty:"update.apache.svn")
            echo ""
            if("y".equalsIgnoreCase(antHelper.getProperty("update.apache.svn"))) {
                if(updateApacheSvn()) {
                    echo "DONE"
                } else {
                    echo "SKIPPED"
                }
            } else {
                echo "SKIPPED"
            }
            echo ""
            pause()
        }
    }

    private int getOldJavaVersion(){
        String command=installation.getInstallDir().getAbsolutePath()+"\\bundled\\java\\bin\\java.exe"
        Process getJavaVersionProcess=new ProcessBuilder(command,"-version").redirectErrorStream(true).start()
        String versionLine=getJavaVersionProcess.text.split("\\r?\\n")[0]
        versionLine=versionLine.substring(versionLine.indexOf("\"") + 1, versionLine.lastIndexOf("\""))
        try{
            double versionNumber = Double.parseDouble(versionLine.substring(0,3))
            return ((versionNumber*10)-10)
        }catch(NumberFormatException e){
            return 0
        }
    }

    private boolean updateJava() {
        File targetDir = new File(installation.getInstallDir(), "bundled/java_"+versionJava)
        if(targetDir.exists()) {
            echo "Target folder for new bundled Java already exists: "+targetDir.getAbsolutePath()
            return false
        }

        echo "Copying new Java to "+targetDir.getAbsolutePath()
        ant.copy(todir:targetDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("bundled/java"))
        }

        echo ""
        echo "Update of Java is prepared. To actually use new version of Java please"
        echo "finish the update manually according to instructions in 3rdParty.txt."
        return true
    }

    private boolean updateApacheSvn() {
        File installDir = installation.getInstallDir()

        File targetSvnDir = new File(installDir, "bundled/svn_"+versionSvn)
        if(targetSvnDir.exists()) {
            echo "Target folder for new bundled Subversion already exists: "+targetSvnDir.getAbsolutePath()
            return false
        }
        File targetApacheDir = new File(installDir, "bundled/apache_"+versionApache)
        if(targetApacheDir.exists()) {
            echo "Target folder for new bundled Apache already exists: "+targetApacheDir.getAbsolutePath()
            return false
        }

        echo "Copying new Subversion to "+targetSvnDir.getAbsolutePath()
        ant.copy(todir:targetSvnDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("bundled/svn"))
        }

        echo "Copying new Apache to "+targetApacheDir.getAbsolutePath()
        ant.copy(todir:targetApacheDir.getAbsolutePath()) {
            fileset(dir:getAbsolutePath("bundled/apache"))
        }

        echo ""
        echo "Configuring new Apache..."
        File oldApacheDir = new File(installDir, "bundled/apache")
        if (!oldApacheDir.exists()) {
            oldApacheDir = new File(installDir, "bundled/apache_2.0.59")
        }
        if(oldApacheDir.exists()) {
            String tomcatPort = installation.getSingleProperties().getTomcatPort()

            HttpdConf httpdConf = new HttpdConf(new File(oldApacheDir, "conf/httpd.conf"))
            String serverName = httpdConf.getNameFromServerName()
            String serverPort = httpdConf.getPortFromServerName()
            String serverAdmin = httpdConf.getServerAdmin()
            String installDirStr = Utils.backslashesToSlashes(installDir.getAbsolutePath())
            String serverRoot = installDirStr+"/bundled/apache"
            String homePath = installDirStr+"/polarion"
            String repositoryPath = installDirStr+"/data/svn"
            String logsDir = installDirStr+"/data/logs/apache"

            echo ""
            echo "Used values"
            echo "  Server Root: "+serverRoot
            echo "  Server Name: "+serverName
            echo "  Server Port: "+serverPort
            echo "  Server Admin: "+serverAdmin
            echo "  Home Path: "+homePath
            echo "  Tomcat Port: "+tomcatPort
            echo "  Repository Path: "+repositoryPath
            echo "  Logs Dir "+logsDir
            echo ""

            ant.move(todir:new File(targetApacheDir, "conf").getAbsolutePath()) {
                fileset(dir:new File(targetApacheDir, "conf").getAbsolutePath()) { include(name:"**/*.in") }
                mapper(type:"glob",from:"*.in",to:"*")
            }

            ant.replace(dir:targetApacheDir.getAbsolutePath()) {
                include(name:"conf/**/*.conf")
                include(name:"bin/stopApache.js")
                replacefilter(token:"#ServerRoot#",value:serverRoot)
                replacefilter(token:"#ServerName#",value:serverName)
                replacefilter(token:"#ServerPort#",value:serverPort)
                replacefilter(token:"#ServerAdmin#",value:serverAdmin)
                replacefilter(token:"#HomePath#",value:homePath)
                replacefilter(token:"#TomCatPort#",value:tomcatPort)
                replacefilter(token:"#RepositoryPath#",value:repositoryPath)
                replacefilter(token:"#LogsDir#",value:logsDir)
            }
        } else {
            warn "Old Apache directory not found, skipping Apache configuration"
        }

        echo ""
        echo "Update of Apache and Subversion is prepared. To actually use new versions"
        echo "of Apache and Subversion please finish the update manually according"
        echo "to instructions in 3rdParty.txt."
        return true
    }

    private void convertMultiRepository() {
        if (Product.isOlderVersion(state.getProductVersion(), "3.6.0")) {
            if(input == null) {
                echo ""
                echo "======="
                echo "WARNING"
                echo "======="
                echo "Update script needs to rename multirepository configuration files"
                echo "and properties to conform to new multi-instance terminology"
                echo "introduced in Polarion 2012."
                echo ""
                echo "Please read related update note in Configuration.txt (in the same folder"
                echo "as this script) as some manual steps might be required"
                echo "in customized installations."
                echo ""
                echo "Please do not interrupt the installation script after this step as your"
                echo "Polarion installation might not work until the update script completes."
                echo ""

                ant.input(message:"Proceed with the update?", validargs:"y,n", addproperty:"convert.multirepo.ok")
                echo ""
                if("N".equalsIgnoreCase(antHelper.getProperty("convert.multirepo.ok"))) {
                    fail "Update cancelled."
                }
            }

            echo "Converting multirepository -> multi-instance..."
            MultiInstanceConverter multiInstanceConverter = new MultiInstanceConverter(installation, backupDir, ant)
            multiInstanceConverter.convert()
            echo "Converting multirepository -> multi-instance... DONE"
        }
    }


    public PolarionInstallation getInstallation(){
        return installation
    }

    public UpdateState getUpdateState(){
        return state
    }

}

public class UpdateState {

    private static final String PROP_INSTALL_DIR = "install.dir"
    private static final String PROP_PRODUCT_VERSION = "product.version"
    private static final String PROP_PG_CLEAN_INSTALL = "pg.clean.install"

    public static final String TASK_UPDATE_PROGRAM_FILES = "task.update.program.files"
    public static final String TASK_UPDATE_FILES = "task.update.files"
    public static final String TASK_UPDATE_REPO = "task.update.repo"
    public static final String TASK_UPDATE_CONTEXT = "task.update.context"
    public static final String TASK_INSTALL_PG = "task.postgresql.install"
    public static final String TASK_CONFIGURE_PG = "task.postgresql.configure"
    public static final String TASK_OPTIMIZE_PG = "task.postgresql.optimize"
    public static final String TASK_REGISTER_PG_SERVICE = "task.postgresql.service"
    public static final String TASK_UPDATE_SVN_CONFIG = "task.update.svn.config"

    private Properties props
    private File stateFile

    public UpdateState(File updateDir) {
        stateFile = new File(updateDir, "state")
        if (stateFile.exists()) {
            props = Utils.loadProperties(stateFile)
        } else {
            props = new Properties()
        }
    }

    public File getInstallDir() {
        String installDirStr = props.getProperty(PROP_INSTALL_DIR)
        return (installDirStr != null) ? new File(installDirStr) : null
    }

    public void setInstallDir(File installDir) {
        setUpdateProperty(PROP_INSTALL_DIR, installDir.getAbsolutePath())
    }

    public String getProductVersion() {
        return props.getProperty(PROP_PRODUCT_VERSION)
    }

    public void setProductVersion(String productVersion) {
        setUpdateProperty(PROP_PRODUCT_VERSION, productVersion)
    }

    private void setUpdateProperty(String name, String value) {
        props.setProperty(name, value)
    }

    public void save() {
        Utils.saveProperties(props, stateFile)
    }

    public boolean isTaskDone(String taskId) {
        return isTaskDone(taskId, null)
    }

    public boolean isTaskDone(String taskId, String subtaskId) {
        return (props.getProperty(createTaskId(taskId, subtaskId)) != null)
    }

    public void markTaskDone(String taskId) {
        markTaskDone(taskId, null)
    }

    public void markTaskDone(String taskId, String subtaskId) {
        props.setProperty(createTaskId(taskId, subtaskId), "done")
        save()
    }

    private static String createTaskId(String taskId, String subtaskId) {
        return (subtaskId != null) ? taskId + "." + subtaskId : taskId
    }

    public void setPostgresqlCleanInstall(boolean cleanInstall){
        setUpdateProperty(PROP_PG_CLEAN_INSTALL, String.valueOf(cleanInstall))
    }

    public boolean isPostgresqlCleanInstall(){
        String cleanInstall = props.getProperty(PROP_PG_CLEAN_INSTALL)
        return  cleanInstall!=null ? Boolean.valueOf(cleanInstall):false
    }

}

//UPDATE
String input = null
String updateRepository = null
String updateBundledSoftware = null
String markUpdate = null
String clearCache = null
String backupImportantData = null
String home = null
boolean debug = false

for(int i = 0; i < args.length; i++) {
    String arg = args[i]
    if(arg == "-input") {
        if(i+1 < args.length) {
            input = args[i+1]
            i++
        }
    }
    if(arg == "-updateRepository") {
        if(i+1 < args.length) {
            updateRepository = args[i+1]
            i++
        }
    }
    if(arg == "-updateBundledSoftware") {
        if(i+1 < args.length) {
            updateBundledSoftware = args[i+1]
            i++
        }
    }
    if(arg == "-markUpdate") {
        if(i+1 < args.length) {
            markUpdate = args[i+1]
            i++
        }
    }
    if(arg == "-clearCache") {
        if(i+1 < args.length) {
            clearCache = args[i+1]
            i++
        }
    }
    if(arg == "-backupImportantData") {
        if(i+1 < args.length) {
            backupImportantData = args[i+1]
            i++
        }
    }
    if(arg == "-home") {
        if(i+1 < args.length) {
            home = args[i+1]
            i++
        }
    }
    if(arg == "-debug") {
        debug = true
    }
}

def UpdateTool update = new UpdateTool(new File(home))
update.setDebug(debug)
if(input != null) {
    update.setInput(new File(input))
}
if(updateRepository != null) {
    update.setUpdateRepository(Boolean.valueOf(updateRepository))
}
if(updateBundledSoftware != null) {
    update.setUpdateBundledSoftware(Boolean.valueOf(updateBundledSoftware))
}
if(markUpdate != null) {
    update.setMarkUpdate(Boolean.valueOf(markUpdate))
}
if(clearCache != null) {
    update.setClearCache(Boolean.valueOf(clearCache))
}
if(backupImportantData != null) {
    update.setBackupImportantData(Boolean.valueOf(backupImportantData))
}
update.execute()
