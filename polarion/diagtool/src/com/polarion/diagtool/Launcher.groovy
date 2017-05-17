/*
 * Copyright (C) 2004-2013 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2013 Polarion Software
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
package com.polarion.diagtool

import groovy.io.FileType

if (args.length < 2) {
    println "ERROR: Unexpected number of arguments"
    System.exit(1)
}

def scriptName = args[0]
def libDirName = args[1]

File libDir = new File(libDirName)
addDirJarsToClasspath(libDir)
addToClasspath(new File(libDir.parentFile, "bin"))
addToClasspath(new File(libDir.parentFile, "src"))

String[] scriptArgs = new String[args.length - 2]
for(int i = 2; i < args.length; i++) {
    scriptArgs[i - 2] = args[i]
}
run(new File(scriptName), scriptArgs)

def addDirJarsToClasspath(File dir) {
    dir.eachFileRecurse(FileType.FILES) {
        if (it.name =~ /\.jar$/) {
            addToClasspath(it)
        }
    }
}

def addToClasspath(File f) {
    this.getClass().classLoader.rootLoader.addURL(f.toURI().toURL())
}
