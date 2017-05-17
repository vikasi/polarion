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

/////////////////// APACHE TOOL ///////////////////String installDirboolean startboolean stopfor(int i = 0; i < args.length; i++) {	String arg = args[i]	if(arg == "-installDir") {		if(i+1 < args.length) {			installDir = args[i+1]			i++		}	}	if(arg == "-start") {		start = true	}	if(arg == "-stop") {		stop = true	}}if (installDir == null) {	println ""	println "ERROR: Missing argument -installDir"    System.exit(1)}if (!start && !stop) {	println ""	println "ERROR: Missing argument -start or -stop"    System.exit(1)}if (start && stop) {	println ""	println "ERROR: Only one of arguments -start and -stop can be set"    System.exit(1)}File install = new File(polarionInstall)if (!install.isDirectory()) {	println ""	println "ERROR: Invalid value of argument -installDir - directory does not exist: "+install.getAbsolutePath()    System.exit(1)}
def ApacheService service = new ApacheService(new PolarionInstallation(install))if (start) {	service.start()} else {	service.stop()}
