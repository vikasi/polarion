#!/bin/bash

SCRIPT_PATH=`readlink -f $0`
HOME=`dirname "${SCRIPT_PATH}"`
_SCRIPT_NAME=$HOME/src/com/polarion/diagtool/DiagTool.groovy

# Allow user to point to Polarion installation with -polarionHome argument
_SET_POLARION_HOME=""
for arg in "$@"; do
  if [ -n "$_SET_POLARION_HOME" ]; then
    POLARION_HOME=$arg
    _SET_POLARION_HOME=""
  fi
  if [ "$arg" = "-polarionHome" ]; then
    _SET_POLARION_HOME=true
  fi
  if [ "$arg" = "-locktest" ]; then
    _SCRIPT_NAME=$HOME/src/com/polarion/diagtool/FileLockTest.groovy
  fi
done
# Let's assume this is bundled with Polarion (in e.g. /opt/polarion/polarion/diagtool)
if [ -z "$POLARION_HOME" ]; then
  _POLARION_HOME="$HOME/../.."
  if [ -e "$_POLARION_HOME/polarion/.eclipseproduct" ]; then
    POLARION_HOME=$_POLARION_HOME
  fi
fi
# Let's at least try some default
if [ -z "$POLARION_HOME" ]; then
  POLARION_HOME=/opt/polarion
fi

# Use user's Java if available (based on JAVA_HOME)
if [ -z "$JAVA_CMD" ]; then
  if [ -n "$JAVA_HOME" ]; then
    _JAVA_CMD="$JAVA_HOME/bin/java"
    if [ -x "$_JAVA_CMD" ]; then
      JAVA_CMD="$_JAVA_CMD"
    fi
  fi
fi
# Use user's Java if available (based on JDK_HOME)
if [ -z "$JAVA_CMD" ]; then
  if [ -n "$JDK_HOME" ]; then
    _JAVA_CMD="$JDK_HOME/bin/java"
    if [ -x "$_JAVA_CMD" ]; then
      JAVA_CMD="$_JAVA_CMD"
    fi
  fi
fi
# Let's hope some Java is on the PATH
if [ -z "$JAVA_CMD" ]; then
  JAVA_CMD=java
fi

echo Diagnostic tool installation folder: $HOME
echo Polarion installation folder: $POLARION_HOME
echo Java command: $JAVA_CMD

GROOVY_CLASSPATH="$HOME/lib/groovy-all-2.1.5.jar"
"$JAVA_CMD" -classpath "$GROOVY_CLASSPATH" org.codehaus.groovy.tools.GroovyStarter --classpath "$GROOVY_CLASSPATH" --main groovy.ui.GroovyMain -c UTF-8 "$HOME/src/com/polarion/diagtool/Launcher.groovy" "$_SCRIPT_NAME" "$HOME/lib" -home "$HOME" -realPolarionHome "$POLARION_HOME" "$@"
