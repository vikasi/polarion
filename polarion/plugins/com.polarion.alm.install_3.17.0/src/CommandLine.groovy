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

public class CommandLine {
	
	public static final String ARG_INSTALL_DIR = "-installDir"
	public static final String ARG_COMMAND = "-cmd"
	
	private String USER_ARGS_SEPARATOR = "-args"
	private Properties userArgs
	private Set userSwitches
	private String installDir
	private String command
	private List freeUserArgs
	
	public CommandLine(String[] args, List argNames, List switchNames) {
		userArgs = new Properties()
		userSwitches = new HashSet()
		freeUserArgs = new ArrayList()
		boolean user = false
		for(int i = 0; i < args.length; i++) {
			String arg = args[i]
			if (arg == USER_ARGS_SEPARATOR) {
				user = true
				continue
			}
			if (!user) {
				if (arg == ARG_INSTALL_DIR) {
					if(i+1 < args.length) {
						installDir = args[i+1]
						i++
					}
					continue
				}
				if (arg == ARG_COMMAND) {
					if(i+1 < args.length) {
						command = args[i+1]
						i++
					}
					continue
				}
			} else {
				if (argNames.contains(arg)) {
					if(i+1 < args.length) {
						userArgs.setProperty(arg, args[i+1])
						i++
					}
					continue
				}
				if (switchNames.contains(arg)) {
					userSwitches.add(arg)
					continue
				}
				freeUserArgs.add(arg)
			}
		}
		freeUserArgs = Collections.unmodifiableList(freeUserArgs)
	}
	
	public String getInstallDir() {
		return installDir
	}
	
	public String getCommand() {
		return command
	}
	
	public String getUserArg(String name) {
		return userArgs.get(name)
	}
	
	public boolean hasUserSwitch(String name) {
		return userSwitches.contains(name)
	}
	
	public List getFreeUserArgs() {
		return freeUserArgs;
	}
	
}
