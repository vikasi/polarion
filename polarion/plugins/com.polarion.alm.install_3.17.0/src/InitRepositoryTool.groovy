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

////////////////////////////
// INIT REPOSITORY TOOL //
////////////////////////////

public void printHelp(String cmd) {
	if (cmd == null) {
		cmd = "..."
	}
	/////////--------------------------------------------------------------------------------
	println ""
	println "Initializes repository (for single-instance mode, or for given"
	println "instance in multi-instance mode) with default configurations, or with demo data."
	println "If demo data installation is requested, demo data is installed"
	println "into the repository, and RR and BIR with demo content are created."
	println ""
	println "Usage:"
	println "  "+cmd+" [-instanceId ARG] -user ARG -password ARG [-demo]"
	println "  "+cmd+" -help"
	println ""
	println "Options:"
	println "  -instanceId ARG  : id of an instance"
	println "  -user ARG     : name of repository user"
	println "  -password ARG : password of repository user"
	println "  -demo         : install demo data"
	println "  -help         : only print help"
}

CommandLine cli = new CommandLine(args, ["-instanceId", "-user", "-password"], ["-help", "-debug", "-demo"])
String installDir = cli.getInstallDir()
if (installDir == null) {
	println ""
	println "ERROR: Missing argument "+CommandLine.ARG_INSTALL_DIR
    System.exit(1)
}
if (cli.hasUserSwitch("-help")) {
	printHelp(cli.getCommand())
    System.exit(0)
}

String slaveId = cli.getUserArg("-instanceId")
String user = cli.getUserArg("-user")
String password = cli.getUserArg("-password")
boolean demo = cli.hasUserSwitch("-demo")
boolean debug = cli.hasUserSwitch("-debug")

if (user == null) {
	println ""
	println "ERROR: Missing argument -user"
	printHelp(cli.getCommand())
    System.exit(1)
}
if (password == null) {
	println ""
	println "ERROR: Missing argument -password"
	printHelp(cli.getCommand())
    System.exit(1)
}

try {
	def PolarionInstallation install = new PolarionInstallation(new File(installDir))
	def RepositoryService service = new RepositoryService(install)
	service.setDebug(debug)
	service.initializeRepository(slaveId, user, password, demo)
	println ""
	println "Done."
} catch (Exception e) {
	if (debug) {
		e.printStackTrace()
	}
	println ""
    println "ERROR: "+e.getMessage()+"\n\nGet support at http://www.polarion.com/techsupport if the problem persists" 
    System.exit(1)
}
