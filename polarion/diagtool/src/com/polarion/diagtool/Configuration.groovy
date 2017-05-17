/*
 * Copyright (C) 2004-2016 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2016 Polarion Software
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

import org.apache.log4j.Logger

import com.polarion.diagtool.internal.OrderedProperties

class Configuration {

    final String[] args
    File home
    File polarionHome
    File resultsParentDir

    File propertiesFile
    final properties = [:] as LinkedHashMap

    long packedFileSizeThreshold = 1024 * 1024 * 1024 // 1 GB
    final List modules = []

    def loadConfigurationFromArgs() {
        def i = 0
        while (i < args.length) {
            if (!args[i].startsWith("-")) {
                modules.addAll(args[i..<args.size()] as List)
                break
            }
            String key = args[i++].substring(1)
            String value = args[i++]

            switch (key) {
                case "home":
                    home = new File(value)
                    break
                case "realPolarionHome":
                    polarionHome = new File(value)
                    break
                case "polarionHome":
                // ignored
                    break
                case "resultsDir":
                    resultsParentDir = new File(value)
                    break
                case "configuration":
                    propertiesFile = new File(value)
                    break;
                default:
                    properties[key] = value
                    break
            }
        }

        if (resultsParentDir == null) {
            resultsParentDir = new File(home, "results")
        }
        if (propertiesFile == null) {
            propertiesFile = new File(home, "diagtool.properties")
        }
    }

    def loadConfigurationFromPropertiesFile() {
        OrderedProperties loadedProperties = new OrderedProperties()
        propertiesFile.withInputStream { loadedProperties.load(it) }
        loadedProperties.each { key, value ->
            if (!properties.containsKey(key)) {
                properties[key] = value
            }
        }
    }

    def getAndRemoveProperty(String key) {
        def value = properties.remove(key)
        if (value != null && !value.trim().isEmpty()) {
            return value
        }
        return null
    }

    def postprocessProperties() {
        def propPackedFileSizeThreshold = getAndRemoveProperty("packedFileSizeThreshold")
        if (propPackedFileSizeThreshold != null) {
            packedFileSizeThreshold = Long.parseLong(propPackedFileSizeThreshold)
        }
        def propModules = getAndRemoveProperty("modules")
        if (propModules != null) {
            modules.addAll(propModules.split(/\s+/))
        }
    }

    Configuration(String[] args) {
        this.args = args

        loadConfigurationFromArgs()
        loadConfigurationFromPropertiesFile()
        postprocessProperties()

        if (modules.isEmpty()) {
            modules = ["default"]
        }
        modules = modules.collectMany {
            it == "default" ? [com.polarion.diagtool.core.EnvironmentCheck.class.name, com.polarion.diagtool.core.PolarionCheck.class.name]
            : [it]
        }
    }

    def propertyMissing(String name) {
        properties[name]
    }

    def recordProbes(Context context) {
        def log = Logger.getLogger(this.class)
        context.probeRecorder.with {
            recordProbe(new Probe(name:"configuration.args", label:"Command-line arguments", value:args.join(" ")), log)
            recordProbe(new Probe(name:"configuration.home", label:"Diagnostic tool installation location", value:home), log)
            recordProbe(new Probe(name:"configuration.file", label:"Configuration file location", value:propertiesFile), log)
            recordProbe(new Probe(name:"configuration.polarionHome", label:"Polarion installation location", value:polarionHome), log)
            recordProbe(new Probe(name:"configuration.resultsParentDir", label:"Results location", value:resultsParentDir), log)
            recordProbe(new Probe(name:"configuration.packedFileSizeThreshold", label:"Packed files size threshold (in bytes)", value:packedFileSizeThreshold), log)
            recordProbe(new Probe(name:"configuration.modules", label:"Modules to run", value:modules.join(" ")), log)
        }
        properties.each { name, value ->
            context.probeRecorder.recordProbe(new Probe(name:"configuration.${name}", label:"Configuration property ${name}", value:value), log)
        }
    }
}
