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

import com.polarion.alm.install.Product
import com.polarion.alm.install.Product.ProductEnum

public class QuickUpdateTool extends Tool {

    private PolarionInstallation installation

    public QuickUpdateTool(File updateDir) {
        super(updateDir)
    }

    protected void executeInternal() {
        Product currentALM = readCurrentProduct(ProductEnum.ALM.getId())

        echoTitle("Polarion Quick Update", '=')

        verifyIntegrity()

        echo ""
        echo "Please go through this checklist before continuing:"
        echo ""
        echo "- make sure that Polarion server is not running"
        echo "and nobody (e.g. console window) holds the Polarion"
        echo "installation folder lock"
        echo ""
        echo "- make sure that current user has write permission"
        echo "for the whole Polarion installation folder"
        echo "(running as root is recommended on Linux)"
        echo ""
        pause()

        installation = inputInstallDir()

        File installDir = installation.getInstallDir()

        deleteDirIfExists(new File(installDir, "polarion/features"))
        deleteDirIfExists(new File(installDir, "polarion/plugins"))

        deleteDirIfExists(new File(installDir, "data/workspace/.config"))
        deleteDirIfExists(new File(installDir, "data/workspace/.metadata/.plugins"))

        ant.delete(file:new File(installDir, "polarion/.eclipseproduct").getAbsolutePath())

        ant.copy(todir:installDir.getAbsolutePath()) {
            fileset(dir:homeDir.getAbsolutePath()) {
                include(name:"polarion/plugins/org.apache.ant_*/**/*")
                include(name:"polarion/plugins/org.codehaus.groovy_*/**/*")
                include(name:"polarion/plugins/org.tmatesoft.svnkit_*")
                include(name:"polarion/plugins/com.polarion.alm.install_*/**/*")
                include(name:"polarion/plugins/com.polarion.core.util_*/**/*")
                include(name:"polarion/plugins/org.tmatesoft.sqljet_*")
                include(name:"polarion/plugins/com.polarion.qcentre_*/**/*")
                include(name:"polarion/plugins/org.apache.log4j_*/**/*")
            }
        }
        ant.move(todir:installDir.getAbsolutePath()) {
            fileset(dir:homeDir.getAbsolutePath()) {
                include(name:"polarion/features/**/*")
                include(name:"polarion/plugins/**/*")
                exclude(name:"polarion/plugins/org.apache.ant_*/**/*")
                exclude(name:"polarion/plugins/org.codehaus.groovy_*/**/*")
                exclude(name:"polarion/plugins/org.tmatesoft.svnkit_*")
                exclude(name:"polarion/plugins/com.polarion.alm.install_*/**/*")
                exclude(name:"polarion/plugins/com.polarion.core.util_*/**/*")
                exclude(name:"polarion/plugins/org.tmatesoft.sqljet_*")
                exclude(name:"polarion/plugins/com.polarion.qcentre_*/**/*")
                exclude(name:"polarion/plugins/org.apache.log4j_*/**/*")
            }
        }
        String productDir = Product.getProductFolder(installation.getProduct().getId())
        ant.copy(file:getAbsolutePath(productDir+"/polarion/.eclipseproduct"), todir:new File(installDir, "polarion").getAbsolutePath())

        if(!osHelper.isWindows()) {
            installation.executeUnixShell(installDir, "chmod ugo+rx ./polarion/plugins/com.polarion.alm.ui_*/phantomjs/phantomjs")
        }

        echo ""
        echo "================"
        echo "Update COMPLETE."
        echo "================"
        echo ""
        pause()
    }

    private void deleteDirIfExists(File srcDir) {
        if(srcDir.exists()) {
            ant.delete(dir:srcDir.getAbsolutePath())
        }
    }
}

//QUICK UPDATE
String input = null;
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

def QuickUpdateTool update = new QuickUpdateTool(new File(home))
update.setDebug(debug)
if(input != null) {
    update.setInput(new File(input))
}
update.execute()
