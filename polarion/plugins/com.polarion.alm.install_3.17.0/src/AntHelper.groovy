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

import java.io.InputStream
import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.apache.tools.ant.DefaultLogger

public class AntHelper {

    private AntBuilder ant
	
	public AntHelper() {
        ant = new AntBuilder()
        List listeners = ant.getProject().getBuildListeners();
        for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ) {
            Object listener = iterator.next();    
            if (listener instanceof DefaultLogger) {
                ((DefaultLogger) listener).setEmacsMode(true)
            }
        }
	}
    
    public AntBuilder getBuilder() {
    	return ant;
    }
    
    private int propIdx = 0;
    
    public String newPropertyName() {
    	return "property."+System.currentTimeMillis()+"_"+propIdx++
    }
    
    public String getProperty(String name) {
        return ant.getProject().getProperty(name)
    }
    
}
