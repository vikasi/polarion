@echo off	

set CSV_FILE=%1

set POLARION_SDK_DIR=C:\Polarion\polarion\SDK
set JAVA_HOME=C:\Polarion\bundled\java

set IMPORTER_JAR=%POLARION_SDK_DIR%\examples\com.polarion.example.importer\example-importer.jar
set WSCLIENT_LIBDIR=%POLARION_SDK_DIR%\lib\com.polarion.alm.ws.client
set CLASSPATH=%WSCLIENT_LIBDIR%\wsclient.jar;%WSCLIENT_LIBDIR%\axis-patch.jar;%WSCLIENT_LIBDIR%\lib\axis.jar;%WSCLIENT_LIBDIR%\lib\commons-codec-1.4.jar;%WSCLIENT_LIBDIR%\lib\commons-discovery-0.2.jar;%WSCLIENT_LIBDIR%\lib\commons-logging-1.0.4.jar;%WSCLIENT_LIBDIR%\lib\commons-httpclient-3.1.patched.jar;%WSCLIENT_LIBDIR%\lib\jaxrpc.jar;%WSCLIENT_LIBDIR%\lib\saaj.jar;%WSCLIENT_LIBDIR%\lib\wsdl4j-1.5.1.jar;%IMPORTER_JAR%;

%JAVA_HOME%\bin\java -cp %CLASSPATH% com.polarion.example.importer.Importer %CSV_FILE%
