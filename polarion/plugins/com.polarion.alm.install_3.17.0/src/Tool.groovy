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

import java.text.SimpleDateFormat

import org.apache.tools.ant.BuildLogger
import org.apache.tools.ant.DefaultLogger
import org.apache.tools.ant.Project
import org.tmatesoft.svn.core.SVNAuthenticationException

import com.polarion.alm.install.IEditor
import com.polarion.alm.install.IllegalProductException
import com.polarion.alm.install.Product
import com.polarion.alm.install.SvnClient
import com.polarion.alm.install.Product.ProductEnum
import com.polarion.alm.install.utils.Utils

abstract public class Tool {

    protected String timestamp
    protected AntBuilder ant
    protected AntHelper antHelper
    protected OSHelper osHelper
    protected String backupDirName

    private File warningsFile
    protected PrintStream logStream

    protected boolean debug = false

    // Home directory of the update
    protected File homeDir

    public Tool(File homeDir) {
        this.homeDir = homeDir
        timestamp = new SimpleDateFormat("yyyyMMdd-HHmm.ss").format(new Date())
        osHelper = new OSHelper()
        antHelper = new AntHelper()
        ant = antHelper.getBuilder()

        warningsFile = new File(homeDir, "warnings.txt")
        File logFile = new File(homeDir, "log-"+timestamp+".txt")
        logStream = new PrintStream(new FileOutputStream(logFile), true)        Product currentALM = readCurrentProduct(ProductEnum.ALM.getId())        logStream.println("Platform version: "+currentALM.getVersion()+" "+currentALM.getBuild())
        BuildLogger logger = new DefaultLogger()
        logger.setMessageOutputLevel(Project.MSG_INFO)
        logger.setOutputPrintStream(logStream)
        logger.setErrorPrintStream(logStream)
        logger.setEmacsMode(true)
        ant.getProject().addBuildListener(logger)

        backupDirName = "_backup-"+timestamp
    }

    public File getBackupDir() {
        return new File(homeDir, backupDirName)
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    protected String getAbsolutePath(String path) {
        return new File(homeDir, path).getAbsolutePath()
    }

    protected Properties input = null;

    public void setInput(File file) {
        input = Utils.loadProperties(file)
    }

    protected Product readCurrentProduct(String productId) {
        def String productDir = Product.getProductFolder(productId)
        return Product.fromProductFile(new File(homeDir, productDir+"/polarion/.eclipseproduct"))
    }

    public void execute() {
        try {
            executeInternal()
        } catch (Throwable e) {
            e.printStackTrace(logStream)
            fail "Internal error occurred:\n"+e.getMessage()+"\n\nGet support at http://www.polarion.com/techsupport if the problem persists"
        }
    }

    abstract protected void executeInternal()

    protected void echo(String message) {
        if(message == "") {
            // echo would not produce empty line
            println ""
        } else {
            ant.echo message
        }
    }

    protected void warn(String message) {
        echo("WARNING: "+message)
        warningsFile.append(message+System.getProperty("line.separator"))
    }

    protected void pause() {
        if(input == null) {
            ant.input(message:"Press Enter key to continue...")
        }
    }

    protected void fail(String message) {
        echo ""
        echo "======"
        echo "FAILED"
        echo "======"
        echo ""
        echo message
        echo ""
        pause()
        System.exit(1)
    }

    protected SvnClient inputRepositoryCredentials(String repo) {
        echo ""
        echo "Please provide credentials of a user that has write"
        echo "permission for the entire repository "+repo
        echo "- configuration updates will be done on behalf of this user."

        def SvnClient svn
        def boolean verified = false;
        while(!verified) {
            String loginProp = antHelper.newPropertyName()
            String passwordProp = antHelper.newPropertyName()
            echo ""
            ant.input(message:"Login: ", addproperty:loginProp)
            ant.input(message:"Password: ", addproperty:passwordProp)

            svn = new SvnClient(repo, antHelper.getProperty(loginProp), antHelper.getProperty(passwordProp))
            svn.setDebug(debug)
            try {
                svn.exists("/");
                verified = true;
            } catch (SVNAuthenticationException e) {
                echo ""
                echo "Invalid login name or password."
            } catch (Exception e) {
                e.printStackTrace(logStream)
                echo ""
                fail "Failed to accces the repository "+repo+". Please check its availability, perhaps Apache is stopped? ("+e.getMessage()+")"
            }
        }
        echo "OK"
        echo ""
        return svn
    }

    protected PolarionInstallation inputInstallDir() {
        String polarionInstall = osHelper.isWindows() ? "C:/Polarion" : "/opt/polarion"
        echo ""
        if(input == null) {
            ant.input(
                    message:"Please enter Polarion installation directory or press Enter key to use the default value",
                    addproperty:"polarion.install",
                    defaultvalue:polarionInstall)
            polarionInstall = antHelper.getProperty("polarion.install")
        } else {
            if(input.keySet().contains("polarion.install")) {
                polarionInstall = input.getProperty("polarion.install")
            }
        }
        polarionInstall = Utils.backslashesToSlashes(polarionInstall)
        echo ""
        echo "Checking installation directory: "+polarionInstall
        def PolarionInstallation installation;
        try {
            installation = new PolarionInstallation(new File(polarionInstall))
        } catch(IllegalProductException e) {
            fail "Installation directory is not valid: "+e.getMessage()
        }
        def Product product = installation.getProduct()
        echo "Found product: '" + product.getName() + "' version " + (product.getVersionName() != null ? product.getVersionName() : "unsupported")
        return installation;
    }

    protected void performEdit(String repoRoot, String repoPath, IEditor editor, boolean create, String localRoot, SvnClient svn, action) {
        String fullRepoPath = repoRoot+"/"+repoPath        echo "  - processing "+fullRepoPath        File localFile = new File(homeDir, localRoot+"/"+repoPath);        if (svn.exists(fullRepoPath)) {            localFile.getParentFile().mkdirs()            svn.export(fullRepoPath, localFile.getParentFile())            editor.open(localFile)        } else if(create) {            localFile.getParentFile().mkdirs()            editor.create(localFile)            editor.open(localFile)        } else {            return        }        action(editor)        if(!editor.save()) {            ant.delete(file:localFile.getAbsolutePath())        } else {            echo "  prepared for commit"        }    }
    // returns true if at least one file (not directory) was found inside (deeply)
    public static boolean removeEmptyDirs(File dir) {
        if(!dir.exists()) {
            return false;
        }
        File[] files = dir.listFiles()        boolean fileFound = false
        for (int i = 0; i < files.length; i++) {
            File file = files[i]
            if(file.isFile()) {
                fileFound = true;
            } else if(removeEmptyDirs(file)) {
                fileFound = true
            } else {
                if(!file.delete()) {                    throw new IOException("Failed to delete file: "+file.getAbsolutePath())                }            }
        }
        return fileFound
    }

    protected void createUpdateMarkFile(File workDir) {
        if(workDir.exists()) {
            new File(workDir, "update").createNewFile()
        }
    }

    protected void setSharedFolderVersion(PolarionInstallation installation, String version) {
        File sharedFolder = installation.getSharedFolder()
        if (sharedFolder != null) {
            File markFile = getSharedUpdateMarkFile(sharedFolder);
            if (!markFile.exists()) {
                markFile.createNewFile()
            }
            markFile.write(version)
        }
    }

    protected String getSharedFolderVersion(PolarionInstallation installation) {
        File sharedFolder = installation.getSharedFolder()
        if (sharedFolder != null) {
            File markFile = getSharedUpdateMarkFile(sharedFolder);
            if (markFile.exists()) {
                return markFile.getText();
            }
        }
        return null;
    }

    private File getSharedUpdateMarkFile(File sharedFolder) {
        return new File(sharedFolder, "update");
    }

    private String repeatChar(String ch, int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(ch);
        }
        return s.toString();
    }

    protected void echoTitle(String s, String ch) {
        String separator = repeatChar(ch, s.length())
        echo separator
        echo s
        echo separator
    }

    protected void verifyIntegrity() {
        String problem = checkIntegrity();
        if (problem != null) {
            fail "UPDATE FOLDER IS CORRUPTED:\n" +
                    problem + "\n" +
                    "Please unpack the update archive into an empty folder and try installing the update again.";
        }
    }

    protected String checkIntegrity() {
        File pluginsDir = new File(homeDir, "polarion/plugins").getCanonicalFile();
        File[] files = pluginsDir.listFiles();
        if (files == null || files.length == 0) {
            return "No plugins found in " + pluginsDir.getAbsolutePath();
        }
        // check that there are not two versions of the same plugin
        Set<String> pluginIds = new HashSet<String>();
        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.endsWith(".zip")) {
                int index = fileName.indexOf('_');
                if (index >= 0) {
                    String pluginId = fileName.substring(0, index);
                    if (pluginIds.contains(pluginId)) {
                        return "Two versions of the same plugin (" + pluginId + ") found in " + pluginsDir.getAbsolutePath();
                    }
                    pluginIds.add(pluginId);
                }
            }
        }
        return null;

    }

    public void addToLogStream(String content){
        logStream.append(content)
    }

    public AntHelper getAntHelper(){
        return antHelper
    }

    public File getHomeDir(){
        return homeDir
    }

}
