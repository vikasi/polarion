@echo off

set POLARION_HOME=%~dp0\..
set JAVA_HOME=C:\Polarion\bundled\java

set SCRIPT=%1
if "%SCRIPT%"=="" goto HELP

for /D %%P in ("%POLARION_HOME%\polarion\plugins\org.codehaus.groovy_*") do set GROOVY_PLUGIN=%%P
for /D %%P in ("%POLARION_HOME%\polarion\plugins\com.polarion.alm.install_*") do set INSTALL_PLUGIN=%%P

set GROOVY_CLASSPATH=%GROOVY_PLUGIN%/groovy-all.jar
"%JAVA_HOME%\bin\java.exe" -classpath "%GROOVY_CLASSPATH%" org.codehaus.groovy.tools.GroovyStarter --classpath "%GROOVY_CLASSPATH%" --main groovy.ui.GroovyMain -c UTF-8 "%INSTALL_PLUGIN%/src/ScriptCaller.groovy" "%INSTALL_PLUGIN%/src/%SCRIPT%" "%POLARION_HOME%/polarion/plugins" -installDir "%POLARION_HOME%" %*

goto QUIT

:HELP
echo Usage: 
echo   %0 SCRIPT [-cmd ARG] [-args ...]
echo.
echo Options:
echo   SCRIPT   : name of the script to execute
echo   -cmd ARG : name of the command that calls this batch file 
echo   -args    : marks that user arguments for the script follow 
echo.
echo Executes a built-in script.
echo.
echo This script is not intended to be directly invoked by end users.
echo.

:QUIT
