@ECHO OFF
REM Must be ran as administrator. Expects as parameters: data folder for initialization of the database, the port for PostgreSQL 
REM and the password for the polarion database user. If run with no parameters, default values will be used.
IF [%1] == [] ( SET "PGDATA=C:\Polarion\data\postgres-data" 
) ELSE ( SET "PGDATA=%~1" )
IF [%2] == [] ( SET PGPORT=5433
) ELSE ( SET "PGPORT=%2" )
IF [%3] == [] ( SET  "POLARION_PSWD=polarion" 
) ELSE ( SET "POLARION_PSWD=%~3" )

@SET PG_HOME=%~dp0
@SET PG_PATH=%PG_HOME%bin
@SET PGUSER=postgres
@SET POLARIONDB=polarion
@SET POLARION_HISTORYDB=polarion_history
@SET POLARIONUSER=polarion
@SET "DB_LOG=%PG_HOME%dbloginstall.log"

if not exist "%PGDATA%" mkdir "%PGDATA%"
%SystemRoot%\system32\icacls "%PGDATA%" /grant %USERNAME%:(f)

call :initPostgres 
 if %ERRORLEVEL% NEQ 0 (
	exit /b %ERRORLEVEL%
 )
call :startPostgres && call :configurePostgres && call :setAuthMethod || echo "There was a problem configuring the database. Please correct it manually, you can find more instructions in the Install guide."
set status=%ERRORLEVEL%
if %ERRORLEVEL% LEQ 0  echo "The PostgreSQL database was configured for Polarion." 
call :stopPostgres 
set /a code=%ERRORLEVEL%+%status%
echo %code%
exit /b %code%


:initPostgres
echo "Initializing PostgreSQL database with command:"
echo "%PG_PATH%\initdb -D %PGDATA% -U postgres -E utf8"
"%PG_PATH%\initdb" -D "%PGDATA%" -U postgres -E utf8
exit /b %ERRORLEVEL%

:startPostgres
echo "Starting PostgreSQL with command:"
echo "%PG_PATH%\pg_ctl" -D %PGDATA% -l "%DB_LOG%" -o "-p %PGPORT%" start "
"%PG_PATH%\pg_ctl" -D "%PGDATA%" -o "-p %PGPORT%" start 
set exit_code=%ERRORLEVEL%
call :timeout
if %ERRORLEVEL% EQU 0 (
	echo "PostgreSQL was started on port %PGPORT%."
)
exit /b %exit_code%

:timeout
%SystemRoot%\system32\ping 127.0.0.1 -n 6 > nul
exit /b %ERRORLEVEL%

:stopPostgres
"%PG_PATH%\pg_ctl" -D "%PGDATA%" -l "%DB_LOG%" -o "-p %PGPORT%" stop 
exit /b %ERRORLEVEL%

:configurePostgres
call :createSqlFile
"%PG_PATH%\psql.exe" --set ON_ERROR_STOP= -U %PGUSER% -p %PGPORT% -q -f "%PG_HOME%createdbs.sql"
if %ERRORLEVEL% NEQ 0 (
	echo "The main Polarion structures were not successfully created."
	exit /b 1
)
call :createDBLink %POLARIONDB% && call :createDBLink %POLARION_HISTORYDB% || exit /b 1
del /f "%PG_HOME%createdbs.sql"
exit /b %ERRORLEVEL%

:createDBLink
"%PG_PATH%\psql.exe" -U %PGUSER% -p %PGPORT% -q -c "CREATE EXTENSION dblink;" %1 
if %ERRORLEVEL% NEQ 0 (
	echo "DbLink extension could not be created on database %1"
	exit /b 1
)
echo "Dblink extension was created on %1 database."
exit /b 0

:createSqlFile
echo CREATE USER %POLARIONUSER% WITH PASSWORD '%POLARION_PSWD%' CREATEROLE; > "%PG_HOME%createdbs.sql"
echo CREATE DATABASE %POLARIONDB% OWNER %POLARIONUSER% ENCODING 'UTF8'; >> "%PG_HOME%createdbs.sql"
echo CREATE DATABASE %POLARION_HISTORYDB% OWNER %POLARIONUSER% ENCODING 'UTF8'; >>"%PG_HOME%createdbs.sql"
exit /b %ERRORLEVEL%

:setAuthMethod
SET sourceFileName=pg_hba.conf
SET source="%PGDATA%\%sourceFileName%"
SET temp="%PGDATA%\pg_hba.conf.temp"
if exist %source% (
	for /F "usebackq tokens=*" %%A in (%source%) do call :process "%%A" 
	del /f %source%
	ren %temp% %sourceFileName%
)
exit /b %ERRORLEVEL%

:process
@setlocal enableDelayedExpansion
SET line=%~1
set prefix=!line:~0,4!
set "h=host"
if !prefix! EQU %h% ( 
	echo "Replaced authentication method."
    SET line=!line:trust=md5!
)
echo !line! >> !temp!
exit /b %ERRORLEVEL%
