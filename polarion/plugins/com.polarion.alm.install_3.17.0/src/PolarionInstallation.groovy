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

import com.polarion.alm.install.IllegalProductException
import com.polarion.alm.install.InstallationType;
import com.polarion.alm.install.Product
import com.polarion.alm.install.utils.FileHelper
import com.polarion.alm.install.utils.Utils
import com.polarion.core.util.eclipse.EclipseRunner
import com.polarion.core.util.properties.PropertiesResolver

public class PolarionInstallation {

    public static final String PROP_CONTROL_PORT = "controlPort"
    public static final String PROP_SHUTDOWN_PORT = "shutdownPort"
    public static final String PROP_CONTROL_HOSTNAME = "controlHostname"
    public static final String PROP_SHUTDOWN_HOSTNAME = "shutdownHostname"
    public static final String PROP_TOMCAT_PORT = "TomcatService.ajp13-port"
    public static final String PROP_SLAVE_ID = "instance.id"
    public static final String PROP_REPO = "repo"
    public static final String PROP_POLARION_DATA = "com.polarion.data"
    public static final String PROP_POLARION_WORKSPACE = "com.polarion.workspace"
    public static final String PROP_SVN_ACCESS = "svn.access.file"
    public static final String PROP_SVN_PASSWD = "svn.passwd.file"
    public static final String PROP_BUILD_DEFAULT_DEPLOY_REPO_URL = "polarion.build.default.deploy.repository.url"
    public static final String PROP_RR_DIR = "RRDir"
    public static final String PROP_BIR_DIR = "BIRDir"
    public static final String PROP_BASE_URL = "base.url"
    public static final String PROP_HOME = "com.polarion.home"
    public static final String PROP_WORK_DIR = "workDir"

    public static final String PROP_MULTIREPOSITORY_ENABLED = "multi-instance.enabled"

    public static final String PLUGIN_ID_INSTALL = "com.polarion.alm.install"

    public static final int DEFAULT_TOMCAT_PORT = 8889
    public static final int DEFAULT_CONTROL_PORT = 8887
    public static String DEFAULT_CONTROL_HOSTNAME="localhost"

    private static final String TEMPLATE_PROPS = "_template.properties"
    private static final String TEMPLATE_APACHE = "polarion.conf_template"
    private static final String TEMPLATE_APACHE_NOSVN = "polarion.conf"
    private static final String TEMPLATE_MULTI = "polarion.multi-instance.conf"

    private static final String PROP_CLUSTER_ID="com.polarion.clusterId";
    private static final String PROP_NODE_ID="com.polarion.nodeId";
    private static final String PROP_ZOOKEEPER="com.polarion.zookeeper";

    private static final String COMMON_PROPERTY_KEY = "com.polarion.commonPropertyFile";
    private static final String EXTERNAL_PROPERTY_KEY = "com.polarion.propertyFile";
    private static final String OLD_MULTI_INSTANCE = "old.multi-instance.enabled";

    private static final String INDEX_ACTIVITIES = "index.activities";

    private static final String DEFAULT_NODE_ID="node1";
    private static final String DEFAULT_ZOOKEEPER="localhost:2181";

    private Product product
    private File installDir
    private File multiCfgDir
    private File multiTemplatesDir
    private File polarionApacheCfgDir
    private OSHelper osHelper;
    private boolean isWindows
    private AntBuilder ant

    public PolarionInstallation(File installDir) throws IOException, IllegalProductException {
        this.installDir = installDir.getCanonicalFile()
        product = Product.fromInstallDir(this.installDir)

        AntHelper antHelper = new AntHelper()
        ant = antHelper.getBuilder()
        osHelper = new OSHelper()
        isWindows = osHelper.isWindows()
        multiCfgDir = new File(this.installDir, isWindows ? "polarion/configuration/multi-instance" : "etc/multi-instance")
        multiTemplatesDir = new File(this.installDir, "polarion/install/multi-instance")
    }

    public File getInstallDir() {
        return installDir
    }

    public boolean isMultirepository() {
        File masterPropsFile = getMasterPropertiesFile();
        if (masterPropsFile.exists()) {
            Properties props = Utils.loadProperties(getMasterPropertiesFile())
            return Boolean.valueOf(props.getProperty(PROP_MULTIREPOSITORY_ENABLED))
        }
        return false
    }

    public boolean usesPostgresql(){
        return getPostgresqlConnectionProperty() != null;
    }
    
    public boolean isPostgresqlOptimized(){
    	String instDir=getInstallDir();
    	instDir=instDir.plus("/data/postgres-data")
    	File f = new File(instDir, "postgresql.conf");
    	String l=f.readLines().get(63)
    	//If this line contains this part of the comment, it was already optimized
    	return l.contains("should be less")
    }

    public String getPostgresqlConnectionProperty(){
        PolarionProperties props = getSingleProperties();
        return props.getPGConnection()
    }

    public InstallationType getType() {
        PolarionProperties properties = getSingleProperties()
        if ("polarion.coordinator".equals(properties.getApplication())) {
            return InstallationType.COORDINATOR
        }
        if (properties.getNodeId() != null) {
            return (getSharedFolder() != null) ? InstallationType.CLUSTER_NODE : InstallationType.INSTANCE_NODE
        }
        if (isMultirepository()) {
            return InstallationType.CONTROLLER
        }
        return InstallationType.STANDALONE
    }

    public File getSharedFolder() {
        String sharedFolder = getSingleProperties().getSharedFolder();
        if (sharedFolder == null) {
            return null;
        }
        return new File(sharedFolder).getCanonicalFile();
    }


    public File getSharedActivitiesIndexFolder() {
        String indexActivities = getSlaveProperty(INDEX_ACTIVITIES, null);
        if (indexActivities != null) {
            return new File(indexActivities);
        }

        File sharedFolder = getSharedFolder();
        if (sharedFolder != null) {
            return new File(sharedFolder, "data/workspace/polarion-data/index")
        }
        return null;
    }

    private File getSharedPropertiesFile() {
        File sharedFolder = getSharedFolder();
        if (sharedFolder == null) {
            return null;
        }
        return new File(getConfigDirForInstallationDir(sharedFolder), "polarion.properties").getCanonicalFile();
    }

    public Product getProduct() {
        return product
    }

    public File getConfigDir() {
        return getConfigDirForInstallationDir(installDir);
    }

    private File getConfigDirForInstallationDir(File installationDir) {
        if(isWindows) {
            return new File(installationDir, "polarion/configuration")
        }
        // linux
        return new File(installationDir, "etc")
    }

    public PolarionProperties getSingleProperties() {
        return new PolarionProperties(getSinglePropertiesFile())
    }

    public File getSinglePropertiesFile() {
        return new File(getConfigDir(), "polarion.properties").getCanonicalFile()
    }

    public File getMultirepositoryDataDir() {
        return new File(getMasterDataDir(), "multi-instance").getCanonicalFile()
    }

    public File getWorkspaceDir(String slaveId) {
        new File(getSlaveProperty(PROP_POLARION_WORKSPACE, slaveId)).getCanonicalFile()
    }

    public File getPolarionDataDir(String slaveId) {
        return new File(getSlaveProperty(PROP_WORK_DIR, slaveId)).getCanonicalFile()
    }

    public File getDataDir(String slaveId) {
        File dataDir;

        String dataDirStr = getSlaveProperty(PROP_POLARION_DATA, slaveId)
        if (dataDirStr != null) {
            dataDir = new File(dataDirStr)
        } else if (slaveId != null) {
            dataDir = new File(getMultirepositoryDataDir(), slaveId)
        } else {
            dataDir = new File(installDir, "data")
        }
        return dataDir.getCanonicalFile()
    }

    public File getMasterDataDir() {
        return new File(getMasterProperty(PROP_POLARION_DATA)).getCanonicalFile()
    }

    public File getMavenDir() {
        return new File(installDir, "maven").getCanonicalFile()
    }

    public String getRepository(String slaveId) {
        return getSlaveProperty(PROP_REPO, slaveId)
    }

    public String getControlPort() {
        if (isMultirepository()) {
            return getMasterProperty(PROP_CONTROL_PORT)
        }
        return getSingleProperties().getControlPort()
    }

    public String getControlHostname() {
        if (isMultirepository()) {
            return getMasterProperty(PROP_CONTROL_HOSTNAME)
        }
        return getSingleProperties().getControlHostname()
    }

    public String getSlaveControlHostname(String slaveId) {
        if (slaveId!=null){
            Properties specific = getSlaveSpecificProperties(slaveId)
            if (specific.getProperty("controlHostname")!=null){
                return specific.getProperty("controlHostname");
            }
            Properties common = Utils.loadProperties(getCommonPropertiesFile())
            if (common.getProperty("controlHostname")!=null){
                return common.getProperty("controlHostname");
            }
            return DEFAULT_CONTROL_HOSTNAME;
        }
        else
            return null;
    }


    public String getSlaveControlPort(String slaveId) {
        if (slaveId!=null){
            Properties specific = getSlaveSpecificProperties(slaveId)
            if (specific.getProperty("controlPort")!=null){
                return specific.getProperty("controlPort");
            }
            Properties common = Utils.loadProperties(getCommonPropertiesFile())
            if (common.getProperty("controlPort")!=null){
                return common.getProperty("controlPort");
            }
            return DEFAULT_CONTROL_PORT.toString();
        }
        else
            return null;
    }

    public Properties getSlaveSpecificProperties(String slaveId){
        if (slaveId !=null){
            Properties props = Utils.loadProperties(getSlavePropertiesFile(slaveId));
            return props;
        }
        else{
            println "Slave id is invalid"
            return null;
        }
    }

    private String getSlaveProperty(String prop, String slaveId) {
        Properties props
        if (slaveId != null) {
            Properties commonProps = Utils.loadProperties(getCommonPropertiesFile())
            File slavePropsFile = getSlavePropertiesFile(slaveId);
            Properties slaveProps = slavePropsFile.exists() ? Utils.loadProperties(getSlavePropertiesFile(slaveId), commonProps) : commonProps

            if (slaveProps.getProperty(PROP_POLARION_DATA) == null) {
                slaveProps.setProperty(PROP_POLARION_DATA, new File(getMultirepositoryDataDir(), slaveId).getAbsolutePath())
            }
            slaveProps.put(PROP_SLAVE_ID, slaveId)

            props = new Properties(getDefaultProperties())
            props.putAll(slaveProps)
        } else {
            Properties defaultProps = getDefaultProperties()
            File sharedPropertiesFile = getSharedPropertiesFile()
            if (sharedPropertiesFile != null) {
                defaultProps = Utils.loadProperties(sharedPropertiesFile, defaultProps)
            }
            props = Utils.loadProperties(getSinglePropertiesFile(), defaultProps)
        }

        String value = props.getProperty(prop)
        if (value != null) {
            value = Utils.expandVariables(value, props)
        }
        return value
    }

    private String getMasterProperty(String prop) {
        Properties props = Utils.loadProperties(getMasterPropertiesFile(), getDefaultProperties())

        String value = props.getProperty(prop)
        if (value != null) {
            value = Utils.expandVariables(value, props)
        }
        return value
    }

    public Properties getSlaveJvmProperties(String slaveId){
        if (slaveId != null){
            Properties masterProps = Utils.loadProperties(getMasterPropertiesFile());
            Properties jvmSpecificProps = masterProps.findAll {
                it.getKey().contains("instance." + slaveId + ".jvm.")
            }
            Properties jvmProps = masterProps.findAll {
                it.getKey().contains("instance.jvm.") && it.getKey().substring("instance.jvm.".length())
            }
            Properties jvmPropsFiltered = jvmProps.findAll {
                !jvmSpecificProps.containsKey("instance." + slaveId + ".jvm."+it.getKey().substring("instance.jvm.".length()))
            }
            return jvmPropsFiltered + jvmSpecificProps;
        }
        else{
            println "Slave id is invalid"
            return null;
        }
    }

    public List<String> prepareArguments(String slaveId){
        Properties jvmProps = getSlaveJvmProperties(slaveId);
        if (jvmProps == null){
            println "Jvm properties could not be read."
            return null;
        }
        List<String> args = new ArrayList<String>();
        args.add(EclipseRunner.getVmExecutable(System.getProperty("java.home")).getAbsolutePath())
        for (String prop: jvmProps.propertyNames() ){
            args.add(jvmProps.getProperty(prop));
        }
        String dataDir = getDefaultProperties().get(PROP_POLARION_DATA);
        Properties props = getDefaultProperties();
        System.setProperty(PROP_HOME,props.get(PROP_HOME));
        PropertiesResolver.resolveReferenceKeys(props, new PropertiesResolver.SimpleValueResolver(props));
        args.add("-D"+PROP_HOME+"=" + props.get(PROP_HOME))
        Properties specificProps = getSlaveSpecificProperties(slaveId)
        args.add("-D"+PROP_SLAVE_ID+"="+slaveId); //it is used for other properties
        args.add("-D"+OLD_MULTI_INSTANCE+"=true");
        args.add("-D"+PROP_CLUSTER_ID+"="+slaveId);
        args.add("-D"+PROP_NODE_ID+"="+DEFAULT_NODE_ID);
        args.add("-D"+PROP_ZOOKEEPER+"="+DEFAULT_ZOOKEEPER)
        args.add("-Dcom.polarion.data=" + getDataDir(slaveId));
        args.add("-Dcom.polarion.logs.main=" + getSlaveLogDir(slaveId));
        args.add("-Dcom.polarion.application=polarion.server");
        args.add("-D"+EXTERNAL_PROPERTY_KEY+"=" + getPropertiesFile(slaveId)); // get instance property file
        args.add("-D"+COMMON_PROPERTY_KEY+"=" + getCommonPropertiesFile()); // get common property file
        args.add("-jar");
        args.add(getStartupLib().getAbsolutePath());
        args.add("-nosplash");
        args.add("-data");
        String workspace = getWorkspaceDir(slaveId);
        args.add(workspace);
        args.add("-configuration");
        args.add(new File(workspace, ".config").toURL().toExternalForm());
        args.add("-application");
        args.add("com.polarion.core.boot.app");
        return args;
    }

    private File getSlaveLogDir(String slaveId){
        return new File(getSlavesLogDir(), slaveId);
    }

    private File getSlavesLogDir() {
        return new File(new File(getMasterDataDir(), "logs"), "multi-instance");
    }

    public static File getStartupLib() throws IOException {
        File startupLibFile = null;
        String startupLib = System.getProperty("instance.startupLib");
        if (startupLib != null) {
            startupLibFile = new File(startupLib);
            if (!startupLibFile.exists()) {
                throw new IOException("Failed to find startup library - path in property instance.startupLib does not exist: " + startupLib);
            }
        } else {
            String homePath = System.getProperty("com.polarion.home");
            if (homePath == null) {
                println "Failed to find startup library - property com.polarion.home is not set"
            } else {
                startupLibFile = new File(homePath, "startup.jar");
                if (!startupLibFile.exists()) {
                    println "Failed to find startup library - file not found: " + startupLibFile.getAbsolutePath() ;
                    startupLibFile = null;
                }
            }
            if (startupLibFile == null) {
                String cp = System.getProperty("java.class.path");
                startupLibFile = new File(cp);
                if (!startupLibFile.exists()) {
                    throw new IOException("Failed to find startup library using classpath: " + cp);
                }
            }
        }
        return startupLibFile;
    }

    public File getCoordinatorWorkspaceDir(){
        return new File(getMasterProperty(PROP_POLARION_WORKSPACE)).getCanonicalFile()
    }

    public File getCoordinatorDataDir(){
        return new File(getMasterProperty(PROP_WORK_DIR)).getCanonicalFile()
    }

    private Properties getDefaultProperties() {
        Properties defaultProps = new Properties()
        defaultProps.put(PROP_HOME, new File(installDir, "polarion").getAbsolutePath())
        defaultProps.put(PROP_POLARION_DATA, "\$[com.polarion.home]/../data")
        defaultProps.put(PROP_POLARION_WORKSPACE, "\$[com.polarion.data]/workspace")
        defaultProps.put(PROP_WORK_DIR, "\$[com.polarion.workspace]/polarion-data")
        return defaultProps
    }

    public File getMultiConfigDir() {
        return multiCfgDir
    }

    public File getMasterPropertiesFile() {
        return new File(multiCfgDir, "_controller.properties")
    }

    public File getCommonPropertiesFile() {
        return new File(multiCfgDir, "_common.properties")
    }

    public File getSlavePropertiesFile(String slaveId) {
        return new File(multiCfgDir, slaveId+".properties")
    }

    public File getSlavePropertiesFileTemplate() {
        return new File(multiCfgDir, "_template.properties")
    }

    public File getPropertiesFile(String slaveId) {
        if (slaveId == null) {
            return getSinglePropertiesFile()
        }
        return getSlavePropertiesFile(slaveId)
    }

    public File getBundledApacheHttpdConfFile() {
        if(isWindows) {
            return new File(installDir, "bundled/apache/conf/httpd.conf")
        }
        return null
    }

    public File getPolarionApacheCfgDir() {
        if(polarionApacheCfgDir == null) {
            if(isWindows) {
                polarionApacheCfgDir = new File(installDir, "bundled/apache/conf/extra")
            } else {
                polarionApacheCfgDir = new File("/etc/httpd/conf.d")
                if(!polarionApacheCfgDir.exists()) {
                    polarionApacheCfgDir = new File("/etc/apache2/conf.d")
                }
                if(!polarionApacheCfgDir.exists()) {
                    polarionApacheCfgDir = new File("/etc/apache2/conf-enabled")
                }
                if(!polarionApacheCfgDir.exists()) {
                    polarionApacheCfgDir = null;
                    throw new RuntimeException("Apache configuration directory was not found")
                }
            }
        }
        return polarionApacheCfgDir
    }

    public File getApacheConfFile() {
        return new File(getPolarionApacheCfgDir(), "polarion.conf")
    }

    public File getApacheSvnConfFile() {
        return new File(getPolarionApacheCfgDir(), "polarionSVN.conf")
    }

    public File getMultirepositoryApacheConfFile() {
        return new File(getPolarionApacheCfgDir(), "polarion.multi-instance.conf")
    }

    public File getSlaveApacheConfFile(String slaveId) {
        return new File(getPolarionApacheCfgDir(), "polarion."+slaveId+".conf.instance")
    }

    public File getMultirepositoryApacheConfFileTemplate() {
        String name = TEMPLATE_MULTI+getOSSpecificFileNameSuffix()
        File template = new File(multiTemplatesDir, name)
        if (!template.exists()) {
            template = new File(multiTemplatesDir, TEMPLATE_MULTI)
        }
        return template
    }

    public File getSlaveApacheConfFileTemplateNoSvn() {
        String name = TEMPLATE_APACHE_NOSVN+getOSSpecificFileNameSuffix()
        File template = new File(multiTemplatesDir, name)
        if (!template.exists()) {
            template = new File(multiTemplatesDir, TEMPLATE_APACHE_NOSVN)
        }
        return template
    }

    public File getSlaveApacheConfFileTemplate() {
        File template = new File(getPolarionApacheCfgDir(), TEMPLATE_APACHE)
        if (!template.exists()) {
            String name = TEMPLATE_APACHE+getOSSpecificFileNameSuffix();
            template = new File(multiTemplatesDir, name)
        }
        if (!template.exists()) {
            template = new File(multiTemplatesDir, TEMPLATE_APACHE)
        }
        return template
    }

    private String getOSSpecificFileNameSuffix() {
        if (osHelper.isDebian()) {
            return ".debian"
        }
        if (osHelper.isRedHat()) {
            return ".redhat"
        }
        if (osHelper.isSUSE64()) {
            return ".suse_64"
        }
        if (osHelper.isSUSE()) {
            return ".suse"
        }
        return ""
    }

    public File getSvnAccessFileTemplate(boolean demo) {
        return new File(installDir, demo ? "data/svn/access.demo" : "data/svn/access.init")
    }

    public File getSvnPasswdFileTemplate(boolean demo) {
        return new File(installDir, demo ? "data/svn/passwd.demo" : "data/svn/passwd.init")
    }

    public File getRepositoryDataArchive(boolean demo) {
        return new File(installDir, demo ? "polarion/install/demo-data.zip" : "polarion/install/default-data.zip")
    }

    public File getaDemoBIRAndRRArchive() {
        return new File(installDir, "polarion/install/demo-bir+rr.zip")
    }

    public Set<String> getSlaveIds() {
        Set<String> ids = new HashSet()
        if(multiCfgDir.exists()) {
            File[] files = multiCfgDir.listFiles()
            for (int i = 0; i < files.length; i++) {
                File file = files[i]
                if(isSlavePropertiesFile(file)) {
                    String filename = file.getName()
                    ids.add(filename.substring(0, filename.lastIndexOf('.')))
                }
            }
        }
        return ids
    }

    private boolean isSlavePropertiesFile(File file) {
        String name = file.getName();
        return name.endsWith(".properties") && !name.startsWith("_")
    }

    public void setFilePermissions(File file) {
        if (isWindows) {
            return
        }

        if (FileHelper.isAncestor(getPolarionApacheCfgDir(), file)) {
            setFilePermissionsByArea(file, PA_APACHE)
            return
        }

        if (FileHelper.isAncestor(getDataDir(null), file)) {
            boolean repository = FileHelper.isAncestor(new File(installDir, "data/svn/repo"), file)
            if (!repository) {
                if (getMasterPropertiesFile().exists()) {
                    File multiData = getMultirepositoryDataDir()
                    if (FileHelper.isAncestor(multiData, file)) {
                        File nextAncestor = FileHelper.getChildAncestor(multiData, file)
                        if ((nextAncestor != null) && FileHelper.isAncestor(new File(nextAncestor, "svn/repo"), file)) {
                            repository = true
                        }
                    }
                }
            }

            if (repository) {
                setFilePermissionsByArea(file, PA_REPO)
            } else {
                setFilePermissionsByArea(file, PA_DATA)
            }
            return
        }

        if (FileHelper.isAncestor(installDir, file)) {
            setFilePermissionsByArea(file, PA_POLARION)
            return
        }
    }

    private void changeFileOwnerAndMode(File file, File refFile) {
        ["chown", "--recursive", "--reference=" + refFile.getAbsolutePath(), file.getAbsolutePath()].execute()
        ["chmod", "--recursive", "--reference=" + refFile.getAbsolutePath(), file.getAbsolutePath()].execute()
    }

    private void changeFileOwnerAndMode(File file, File refFile, File refDir) {
        if (file.isDirectory()) {
            changeFileOwnerAndMode(file, refDir)
            if (!refDir.isDirectory()) {
                ["chmod", "--recursive", "a+X", file.getAbsolutePath()].execute()
            }
        } else {
            changeFileOwnerAndMode(file, refFile)
        }
    }
    public def executeUnixShell(File workingDirectory, String command) {
        def fullCommand = ["sh", "-c", command]
        Process process = new ProcessBuilder(fullCommand).directory(workingDirectory).redirectErrorStream(true).start()
        process.inputStream.eachLine {println it}
        process.waitFor();
        return process.exitValue()
    }

    private static final int PA_POLARION = 0
    private static final int PA_DATA = 1
    private static final int PA_REPO = 2
    private static final int PA_APACHE = 3

    private void setFilePermissionsByArea(File file, int area) {
        switch (area) {
            case PA_POLARION:
            // root:psvnadm rw-r--r--
                changeFileOwnerAndMode(file, getSlavePropertiesFileTemplate(), getSlavePropertiesFileTemplate())
                break;
            case PA_DATA:
            // polarion:webgroup rwxrwsr-x
                changeFileOwnerAndMode(file, new File(installDir, "data/svn/access.init"), new File(installDir, "data/svn"))
                break;
            case PA_REPO:
            // webuser:webgroup rwxr-sr-x
                changeFileOwnerAndMode(file, new File(installDir, "data/svn/repo/README.txt"), new File(installDir, "data/svn/repo"))
                break;
            case PA_APACHE:
            // no need to change
                break;
        }
    }

    public File getPluginFile(String pluginId, String path) {
        File pluginsDir = new File(installDir, "polarion/plugins")
        File file = findPlugin(pluginsDir, pluginId);
        if (file != null) {
            file = new File(file, path)
        }
        return file;
    }

    private File findPlugin(File pluginsDir, String pluginId) {
        File[] files = pluginsDir.listFiles();
        for (File file : files) {
            String name = file.getName();
            if (name.equals(pluginId) || name.startsWith(pluginId+"_")) {
                return file;
            }
        }
    }

    // For debugging purposes
    public void printSummary() {
        println "======================================================================"
        println "Installation directory: "+getInstallDir().getAbsolutePath()
        println "Product id: "+getProduct().getId()
        println "Product name: "+getProduct().getName()
        println "Version: "+getProduct().getVersion()
        println "Version name: "+getProduct().getVersionName()
        println "Build: "+getProduct().getBuild()
        println "Mode: "+(isMultirepository() ? "multi" : "single")
        if (isMultirepository()) {
            println "Controller data directory: "+getMasterDataDir().getAbsolutePath()
            println "Instances: "+getSlaveIds()
            for(String slaveId in getSlaveIds()) {
                println "Instance: "+slaveId
                println "  Data directory: "+getDataDir(slaveId).getAbsolutePath()
                println "  Workspace directory: "+getWorkspaceDir(slaveId).getAbsolutePath()
                println "  Polarion-data directory: "+getPolarionDataDir(slaveId).getAbsolutePath()
                println "  Repository: "+getRepository(slaveId)
            }
        } else {
            println "Data directory: "+getDataDir(null).getAbsolutePath()
            println "Workspace directory: "+getWorkspaceDir(null).getAbsolutePath()
            println "Polarion-data directory: "+getPolarionDataDir(null).getAbsolutePath()
            println "Repository: "+getRepository(null)
        }
        println "======================================================================"
    }
    
    public boolean isWindows(){
        return isWindows
    }
}
