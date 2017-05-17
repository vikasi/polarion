/*
 * Copyright (C) 2004-2015 Polarion Software
 * All rights reserved.
 * Email: dev@polarion.com
 *
 *
 * Copyright (C) 2004-2015 Polarion Software
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
import java.io.File
import java.io.PrintStream
import java.util.List
import java.util.Map
import com.polarion.alm.install.TextFileEditor

abstract public class PostgresqlUpdate {
    protected static final OK_MSG = "PostgreSQL was successfully configured for Polarion."
    protected static final NOK_MSG = "Unable to successfully configure PostgreSQL. See update log for more details."
    
    protected static final OPT_OK_MSG = "PostgreSQL was successfully optimized for Polarion."

    private static final String POSTGRES_DATA_DIR = "postgres-data"
    private static final String POSTGRES_LOG_DIR= "/logs/postgresql"
    private static final int PG_DEFAULT_PORT = 5433

    protected final UpdateTool updateTool
    protected final AntHelper antHelper
    protected final AntBuilder ant
    protected final OSHelper osHelper
    protected final PolarionInstallation installation
    protected final File postgresDataDir
    protected final File postgresLogDir
    protected final File homeUpdateDir
    protected File configureScript
    protected File optimizeScript

    public PostgresqlUpdate(UpdateTool updateTool){
        this.updateTool = updateTool
        installation = updateTool.getInstallation()
        antHelper = updateTool.getAntHelper()
        ant = antHelper.getBuilder()
        osHelper = new OSHelper()
        postgresDataDir = new File(installation.getDataDir(null), POSTGRES_DATA_DIR)
        postgresLogDir = new File(installation.getDataDir(null), POSTGRES_LOG_DIR)
        homeUpdateDir = updateTool.getHomeDir()
    }

    abstract public void installPostgresql()

    abstract public boolean isPostgresqlInstalled()

    abstract protected void configurePostgresqlInternal(int pgPort, String pgPassword)
    
    abstract protected void optimizePostgresqlInternal()

    abstract public void registerService()

    abstract public boolean isPostgresqlRegistered()

    abstract public boolean startPostgresql()

    public void configurePostgresql(){
        ant.echo "PostgreSQL will now be configured for Polarion..."
        int pgPort = osHelper.getAvailablePort(PG_DEFAULT_PORT)
        ant.echo "PostgreSQL will use the port "+pgPort
        ant.echo ""
        String pgPassword=getPostgresqlPassword()
        ant.echo "Please wait while PostgreSQL is being configured for Polarion..."
        configurePostgresqlInternal(pgPort, pgPassword)
    }
    
    public void optimizePostgresql(){
    	ant.echo "PostgreSQL will now be optimized for Polarion..."
    	optimizePostgresqlInternal()
    }

    protected void setPostgresqlConnectionProperty(int pgPort, String password){
        TextFileEditor editor = new TextFileEditor()
        editor.open(installation.getSinglePropertiesFile())
        editor.removeLine("# End property file")
        editor.addLine("#Postgres database connection")
        editor.addLine(PolarionProperties.PROP_PG_CONNECTION+"="+"polarion:"+password+"@localhost:"+String.valueOf(pgPort))
        editor.addLine("# End property file")
        editor.save()
    }

    protected String getPostgresqlPassword(){
        String passwordProp = antHelper.newPropertyName()
        ant.input(message:"Please, enter a password for system user 'polarion' through which Polarion will connect to PostgreSQL:", addproperty:passwordProp)
        if (antHelper.getProperty(passwordProp).trim().size() == 0){
            ant.echo "Password cannot be empty or contain only spaces"
            return getPostgresqlPassword()
        }
        if (antHelper.getProperty(passwordProp).any{ ":@\\'\"".contains(it) }){
            ant.echo "Characters '@', ':', '\"', '\\' and ''' are not allowed!"
            return getPostgresqlPassword()
        }
        return antHelper.getProperty(passwordProp)
    }

    protected boolean executeCommandSilently(List<String> arguments){
        return executeCommandSilently(arguments, new HashMap<String, String>(), null)
    }

    protected boolean executeCommandSilently(List<String> arguments, Map<String,String> envVars, File execDir){
        String resultProp = antHelper.newPropertyName()
        File logFile = new File(homeUpdateDir, "temp"+System.currentTimeMillis()+".log")
        String cmd = ""
        arguments.each{ cmd += " " + it }
        updateTool.addToLogStream("\nExecuting: "+cmd+"\n")
        ant.exec(executable:arguments.get(0), outputproperty:"devnull", resultproperty:resultProp, output:logFile, dir:execDir != null ? execDir : "") {
            for (String a: arguments){
                if (!a.equals(arguments.get(0))){
                    arg(line:a)
                }
            }
            for (String var: envVars.keySet()){
                env(key: var, value: envVars.get(var))
            }
        }
        int result = Integer.parseInt(antHelper.getProperty(resultProp))
        if (result != 0){
            ant.echo "Failed to execute command:"+cmd+"\n"
            ant.echo logFile.getText()
        }else{
            updateTool.addToLogStream(logFile.getText())
        }
        logFile.delete()
        return result == 0
    }

    protected int getPort(){
        return Integer.parseInt(installation.getPostgresqlConnectionProperty().split(":")[2])
    }
}
