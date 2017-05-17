/*
  * Copyright (C) 2004-2013 Polarion Software
  * All rights reserved.
  * Email: dev@polarion.com
  *
  *
  * Copyright (C) 2004-2013 Polarion Software
  * All Rights Reserved.  No use, copying or distribution of this
  * work may be made except in accordance with a valid license
  * agreement from Polarion Software.  This notice must be
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

import org.apache.log4j.Logger

import com.polarion.diagtool.internal.OrderedProperties

class ProbeRecorder {

    final File probeStorageFile
    final probes = [] as List<Probe>

    ProbeRecorder(File probeStorageFile) {
        this.probeStorageFile = probeStorageFile
    }

    def recordProbe(Probe probe, Logger log) {
        log.info(probe)
        probes.push(probe)
    }

    def storeProbes() {
        OrderedProperties props = new OrderedProperties()
        probes.sort { it.name }.each { Probe probe ->
            props.setProperty(probe.name, String.valueOf(probe.value != null ? probe.value : "<unknown>"))
        }
        probeStorageFile.withOutputStream { os ->
            props.store(os, "Probes")
        }
        def log = Logger.getLogger(this.class)
        log.info("Probes stored in ${probeStorageFile}")
    }
}
