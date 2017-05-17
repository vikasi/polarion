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
// CREATE REPOSITORY TOOL //
////////////////////////////

public void printHelp(String cmd) {
	if (cmd == null) {
		cmd = "..."
	}
	/////////--------------------------------------------------------------------------------
	println ""
	println "Creates repository with access and passwd files, and optionally initializes"
	println "the repository with default configurations or demo data (for single-instance"
	println "mode, or for given instance in multi-instance mode)."
	println "Demo argument is used to select between default and demo versions of access" 
	println "and passwd files (even if initialization is not requested)."
	println "Initialization of a demo repository includes creation of demo RR and BIR."
	println ""
	println "Usage:"
	println "  "+cmd+" [-instanceId ARG] [-demo] [-noinit]"
	println "  "+cmd+" -help"
	println ""
	println "Options:"
	println "  -instanceId ARG : id of an instance"
	println "  -demo        : repository is intended for demo data"
	println "  -noinit      : create empty repository"
	println "  -help        : only print help"
}


CommandLine cli = new CommandLine(args, ["-instanceId"], ["-help", "-debug", "-demo", "-noinit"])
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
boolean demo = cli.hasUserSwitch("-demo")
boolean noinit = cli.hasUserSwitch("-noinit")
boolean debug = cli.hasUserSwitch("-debug")

try {
	def PolarionInstallation install = new PolarionInstallation(new File(installDir))
	def RepositoryService service = new RepositoryService(install)
	service.setDebug(debug)
	service.createRepository(slaveId, !noinit, demo)
	println ""
	println "Done."
} catch (Exception e) {
	if (debug) {
		e.printStackTrace()
	}
	println ""
    println "ERROR: "+e.getMessage()+"\n\nGet support at http://www.polarion.com/techsupport if the problem persists." 
    System.exit(1)
}
