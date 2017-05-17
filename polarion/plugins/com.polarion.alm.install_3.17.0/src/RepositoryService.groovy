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

import com.polarion.alm.install.SvnClient
import com.polarion.alm.install.ProjectListEditor
import com.polarion.alm.install.TextFileEditor
import com.polarion.alm.install.TimePointEditor
import com.polarion.alm.install.parsers.ReportParserFactory
import com.polarion.alm.install.parsers.AbstractReportParser
import com.polarion.alm.install.utils.Utils
import com.polarion.alm.install.utils.FileHelper
import org.tmatesoft.svn.core.SVNAuthenticationException
import java.text.SimpleDateFormat

public class RepositoryService {
	
	private PolarionInstallation install
	private AntHelper ant
	private boolean debug
	
	public RepositoryService(PolarionInstallation install) {
		this.install = install
		ant = new AntHelper()
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug
	}
	
	/**
	  * Creates repository with access and passwd files, and optionally initializes
	  * the repository with default configurations or demo data (for single-instance
	  * mode, or for given instance in multi-instance mode).
	  * Demo argument is used to select between default and demo versions of access 
	  * and passwd files (even if intialization is not requested).
	  * Initialization of a demo repository includes creation of demo RR and BIR.
	 */
	public void createRepository(String slaveId, boolean init, boolean demo) throws Exception {
		if ((slaveId != null) && !install.getSlaveIds().contains(slaveId)) {
			throw new Exception("Unknown instance id: "+slaveId)
		}
		File baseDir = new File(install.getDataDir(slaveId), "svn")
		File repoDir = new File(baseDir, "repo")
		if(repoDir.exists()) {
			throw new Exception("Repository folder already exists: "+repoDir.getAbsolutePath())
		}
		
		File accessFile = new File(baseDir, "access")
		if (accessFile.exists()) {
			throw new Exception("Access file already exists: "+accessFile.getAbsolutePath())
		}
		
		File passwdFile = new File(baseDir, "passwd")
		if (passwdFile.exists()) {
			throw new Exception("Password file already exists: "+accessFile.getAbsolutePath())
		}
		
		def File accessTemplate = install.getSvnAccessFileTemplate(demo) 
		if (!accessTemplate.exists()) {
			throw new Exception("Access file template not found: "+accessTemplate.getAbsolutePath())
		}
		
		def File passwdTemplate =  install.getSvnPasswdFileTemplate(demo)
		if (!passwdTemplate.exists()) {
			throw new Exception("Password file template not found: "+passwdTemplate.getAbsolutePath())
		}
		
		if (!baseDir.exists()) {
			ant.getBuilder().mkdir(dir:baseDir.getAbsolutePath())
			install.setFilePermissions(baseDir)
		}
		
		println "Creating subversion repository: "+repoDir.getAbsolutePath()
		createEmptyRepository(repoDir)

		println "Creating access file: "+accessFile.getAbsolutePath()
		ant.getBuilder().copy(
				file:accessTemplate.getAbsolutePath(),
				tofile:accessFile.getAbsolutePath())
		install.setFilePermissions(accessFile)

		println "Creating passwd file: "+passwdFile.getAbsolutePath()
		ant.getBuilder().copy(
				file:passwdTemplate.getAbsolutePath(),
				tofile:passwdFile.getAbsolutePath())
		install.setFilePermissions(passwdFile)

		if (init) {
			println "Initializing content of the repository"+(demo ? " with demo data" : "")
			Properties installProps = getInstallProperties()
			String user = installProps.getProperty("user")
			String password = installProps.getProperty("password")
			initializeRepository(slaveId, user, password, demo);
		}
	}
	
	private void createEmptyRepository(File repoDir) {
        // We use svnadmin command line tool so that the format version of created
        // repository corresponds to installed subversion - important on Linux 
        // where installed subversion might be older than bundled svnkit
        FileHelper.mkdirs(repoDir)
        String resultProp = ant.newPropertyName()
        if (debug) {
            String message = "Executing \"svnadmin create "+repoDir.getAbsolutePath()+"\""
            println message
        }
        ant.getBuilder().exec(executable:"svnadmin",resultproperty:resultProp) {
            arg(value:"create")     
            arg(value:repoDir.getAbsolutePath())
        }
        String result = ant.getProperty(resultProp)
        if (result != "0") {
            throw new RuntimeException("Failed to create repository using svnadmin: error code "+result)
        }
        install.setFilePermissions(repoDir)
	}
	
	private Properties getInstallProperties() throws Exception {
		File installPropsFile = new File(install.getInstallDir(), "polarion/install/install.properties")
		if (!installPropsFile.exists()) {
			throw new Exception("File not found: "+installPropsFile.getAbsolutePath())
		}
		return Utils.loadProperties(installPropsFile)
	}

	/**
	 * Initializes repository with default configuration if it is not present yet 
	 * (if it does not contain folder ".polarion").
	 */
	public void initializeRepository(String repoUrl, String user, String password) throws Exception {
		// verify url and credentials
		SvnClient svn = new SvnClient(repoUrl, user, password)
		svn.setDebug(debug)
        try {
            svn.exists("/")
        } catch (SVNAuthenticationException e) {
        	throw new Exception("Invalid user name or password", e)
        } catch (Exception e) {
        	throw new Exception("Failed to accces the repository "+repoUrl+", please check its availability", e)
        }
        
		// verify that repository does not already contain polarion configuration
        if (svn.exists(".polarion")) {
            println "Repository is already initialized (folder .polarion found)"
            return
        }
        
		// verify data archive 
		def File dataArchive = install.getRepositoryDataArchive(false)
		if (!dataArchive.exists()) {
			throw new Exception("Data archive file not found: "+dataArchive.getAbsolutePath())
		}
		File dataArchiveDir = FileHelper.createTempDir("polarion-repo-data-", null, null)
		ant.getBuilder().unzip(src:dataArchive.getAbsolutePath(),dest:dataArchiveDir.getAbsolutePath())
		
		// import data
		println "Importing data to repository: "+repoUrl
		String message = "Importing Polarion global configuration"
		svn.doImport("/", dataArchiveDir, "Polarion commit: "+message)
		ant.getBuilder().delete(dir:dataArchiveDir.getAbsolutePath())
	}
	
	/**
	 * Initializes repository with default configurations, or demo data.
	 * Part of demo data installation are also additions to RR and BIR. 
	 */
	public void initializeRepository(String slaveId, String user, String password, boolean demo) throws Exception {
		if ((slaveId != null) && !install.getSlaveIds().contains(slaveId)) {
			throw new Exception("Unknown instance id: "+slaveId)
		}
		
		File propsFile = install.getPropertiesFile(slaveId)
		Properties props = Utils.loadProperties(propsFile)
		if (slaveId != null) {
			props.setProperty(PolarionInstallation.PROP_SLAVE_ID, slaveId)
		}
		String repoUrl = Utils.expandVariables(props.getProperty(PolarionInstallation.PROP_REPO), props)
		println "Repository URL: "+repoUrl
		
		if (!demo) {
			initializeRepository(repoUrl, user, password)
			return
		}
		
		// verify url and credentials
		SvnClient svn = new SvnClient(repoUrl, user, password)
		svn.setDebug(debug)
        try {
            svn.exists("/")
        } catch (SVNAuthenticationException e) {
        	throw new Exception("Invalid user name or password", e)
        } catch (Exception e) {
        	throw new Exception("Failed to accces the repository "+repoUrl+", please check its availability", e)
        }
        
		// verify that repository does not already contain polarion configuration
        if (svn.exists(".polarion")) {
        	throw new Exception("Path .polarion already exists in the repository")
        }
        
		// verify data archive 
		def File dataArchive = install.getRepositoryDataArchive(true)
		if (!dataArchive.exists()) {
			throw new Exception("Data archive file not found: "+dataArchive.getAbsolutePath())
		}
		File dataArchiveDir = FileHelper.createTempDir("polarion-repo-data-", null, null)
		ant.getBuilder().unzip(src:dataArchive.getAbsolutePath(),dest:dataArchiveDir.getAbsolutePath())

		def File dataDir = install.getDataDir(slaveId)
		
		def File demoBIRAndRRArchive = install.getaDemoBIRAndRRArchive()
		if (!demoBIRAndRRArchive.exists()) {
			throw new Exception("BIR and RR demo data archive file not found: "+demoBIRAndRRArchive.getAbsolutePath())
		}
		def File rrDir = new File(dataDir, "RR")
		if (rrDir.exists() && (rrDir.listFiles().length > 0)) {
			throw new Exception("RR is not empty: "+rrDir.getAbsolutePath())
		}
		def File birDir = new File(dataDir, "BIR")
		if (birDir.exists() && (birDir.listFiles().length > 0)) {
			throw new Exception("BIR is not empty: "+birDir.getAbsolutePath())
		}
		
		Properties installProps = getInstallProperties()
		def Date rrTimestamp = readDate(installProps.getProperty("demo.bir-rr.creation.date"))
/* 		def Date dataTimestamp = readDate(installProps.getProperty("demo.data.creation.date"))
		
		// preprocess data
		println "Demo data: preprocessing repository data..."
		// shift time points
		ProjectListEditor projectListEd = new ProjectListEditor()
		projectListEd.open(new File(dataArchiveDir, ".polarion/projects.xml"))
		Collection projectPaths = projectListEd.readProjects().values()
		updateTimestamps(dataArchiveDir, projectPaths, dataTimestamp) */

		// import data
		println "Importing data to repository: "+repoUrl
		String message = "Importing Polarion demo data"
		svn.doImport("/", dataArchiveDir, "Polarion commit: "+message)
		ant.getBuilder().delete(dir:dataArchiveDir.getAbsolutePath())

		println "Creating RR and BIR demo data"
		if (!rrDir.exists()) {
			ant.getBuilder().mkdir(dir:rrDir.getAbsolutePath())
		}
		if (!birDir.exists()) {
			ant.getBuilder().mkdir(dir:birDir.getAbsolutePath())
		}
		ant.getBuilder().unzip(src:demoBIRAndRRArchive.getAbsolutePath(),dest:dataDir.getAbsolutePath())
		install.setFilePermissions(rrDir)
		install.setFilePermissions(birDir)
		
		// shift dates in RR
		updateTimestampsRR(rrDir, new ReportParserFactory(rrTimestamp))
	}
	
	private Date readDate(String s) {
		if (s == null) {
			return null
		}
		new SimpleDateFormat("yyyy-MM-dd").parse(s.trim())
	}
	
	private void updateTimestamps(File dataDir, Collection projectPaths, Date dataTimestamp) {
    	TimePointEditor ed = new TimePointEditor()
    	for (String projectPath : projectPaths) {
    		File projectDir = new File(dataDir, projectPath)
    		File timepointsDir = new File(projectDir, ".polarion/tracker/timepoints")
    		if (timepointsDir.exists()) {
    			for (File file : timepointsDir.listFiles()) {
    				if (file.isFile() && file.getName().endsWith(".xml")) {
    					ed.open(file)
    					ed.shiftTime(dataTimestamp)
    					ed.save()
    				}
                }
    		}
    	}
	}
	
	private void updateTimestampsRR(File root, ReportParserFactory factory) throws Exception {		
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				updateTimestampsRR(file, factory)
			} else {
				AbstractReportParser parser = factory.createParser(file)				
				if (parser != null) {
					parser.execute()
				}
			}
		}
	}
	
}
