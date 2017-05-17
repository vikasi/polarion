@echo off

rem The path must not end with "\" otherwise there are problems with passing of arguments
set "HOME=%~dp0\."
set _SCRIPT_NAME=%HOME%/src/com/polarion/diagtool/DiagTool.groovy

rem Allow user to point to Polarion installation with -polarionHome argument
set "_SET_POLARION_HOME="
for %%A in (%*) do (
  if defined _SET_POLARION_HOME (
    set "POLARION_HOME=%%A"
    set "_SET_POLARION_HOME="
  )
  if /i "%%A"=="-polarionHome" (
    set _SET_POLARION_HOME=true
  )
  if /i "%%A"=="-locktest" (
	set _SCRIPT_NAME=%HOME%/src/com/polarion/diagtool/FileLockTest.groovy
  )
)

if defined POLARION_HOME goto gotPolarion

rem Let's assume this is bundled with Polarion (in e.g. C:\Polarion\polarion\diagtool)
set "_POLARION_HOME=%HOME%\..\.."
if exist "%_POLARION_HOME%\polarion\.eclipseproduct" set POLARION_HOME=%_POLARION_HOME%

if defined POLARION_HOME goto gotPolarion

rem Let's at least try some default
set POLARION_HOME=C:\Polarion

:gotPolarion

set "USED_JAVA_CMD=%JAVA_CMD%"

if defined USED_JAVA_CMD goto gotJava

rem Use bundled Java if Polarion installation was successfully found
set "_JAVA_CMD=%POLARION_HOME%\bundled\java\bin\java.exe"
if exist "%_JAVA_CMD%" set USED_JAVA_CMD=%_JAVA_CMD%

if defined USED_JAVA_CMD goto gotJava

rem Use user's Java if available (based on JAVA_HOME)
set "_JAVA_CMD=%JAVA_HOME%\bin\java.exe"
if exist "%_JAVA_CMD%" set USED_JAVA_CMD=%_JAVA_CMD%

if defined USED_JAVA_CMD goto gotJava

rem Use user's Java if available (based on JDK_HOME)
set "_JAVA_CMD=%JDK_HOME%\bin\java.exe"
if exist "%_JAVA_CMD%" set USED_JAVA_CMD=%_JAVA_CMD%

if defined USED_JAVA_CMD goto gotJava

rem Let's hope some Java is on the PATH
set USED_JAVA_CMD=java.exe

:gotJava

echo Diagnostic tool installation folder: %HOME%
echo Polarion installation folder: %POLARION_HOME%
echo Java command: %USED_JAVA_CMD%

set "GROOVY_CLASSPATH=%HOME%/lib/groovy-all-2.1.5.jar"
"%USED_JAVA_CMD%" -classpath "%GROOVY_CLASSPATH%" org.codehaus.groovy.tools.GroovyStarter --classpath "%GROOVY_CLASSPATH%" --main groovy.ui.GroovyMain -c UTF-8 "%HOME%/src/com/polarion/diagtool/Launcher.groovy" "%_SCRIPT_NAME%" "%HOME%/lib" -home "%HOME%" -realPolarionHome "%POLARION_HOME%" %*