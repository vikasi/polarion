Polarion Extensions Folder
==========================

The extensions folder ([INSTALL]/polarion/extensions) can be used to deploy 
additional plug-ins or features to Polarion without having them removed during
Polarion updates.

The structure of this folder is as follows:
  /extensions
    /my_extension_1
      /eclipse
        /features (optional)
        /plugins
    /my_extension_2
      /eclipse
        /features (optional)
        /plugins

How to install an extension
---------------------------
 1. Stop Polarion server
 2. Create a subfolder under the extensions folder using any name
 3. Add plugins and/or features to the corresponding subfolder 
    eclipse/features or eclipse/plugins
 4. Start Polarion server
 
How to uninstall an extension
-----------------------------
 1. Stop Polarion server
 2. Remove corresponding subfolder under the extensions folder
 3. Start Polarion server
