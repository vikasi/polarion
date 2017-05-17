@echo off	

set REPOS=%1
set TXN=%2

set POLARION_SDK_DIR=C:\Polarion\polarion\SDK
set JAVA_HOME=c:\Polarion\bundled\java

set POL_PROP=%POLARION_SDK_DIR%\examples\com.polarion.example.commithook\settings.properties
set PRE_COMMIT_JAR=%POLARION_SDK_DIR%\examples\com.polarion.example.commithook\example-commithook.jar

set WSCLIENT_LIBDIR=%POLARION_SDK_DIR%\lib\com.polarion.alm.ws.client
set CLASSPATH=%WSCLIENT_LIBDIR%\wsclient.jar;%WSCLIENT_LIBDIR%\axis-patch.jar;%WSCLIENT_LIBDIR%\lib\axis.jar;%WSCLIENT_LIBDIR%\lib\commons-codec-1.4.jar;%WSCLIENT_LIBDIR%\lib\commons-discovery-0.2.jar;%WSCLIENT_LIBDIR%\lib\commons-logging-1.0.4.jar;%WSCLIENT_LIBDIR%\lib\commons-httpclient-3.1.patched.jar;%WSCLIENT_LIBDIR%\lib\jaxrpc.jar;%WSCLIENT_LIBDIR%\lib\saaj.jar;%WSCLIENT_LIBDIR%\lib\wsdl4j-1.5.1.jar;%PRE_COMMIT_JAR%;

%JAVA_HOME%\bin\java -cp %CLASSPATH% com.polarion.example.commithook.PreCommitHook %REPOS% %TXN% %POL_PROP%
if errorlevel 1 (
echo "Error Occured - commit should be interrupted"
exit 1
) ELSE (
echo "Everything is alright - commit granted"
exit 0
)
