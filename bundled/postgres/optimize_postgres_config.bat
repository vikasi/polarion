@ECHO off
setlocal enabledelayedexpansion

SET OLD_PATH=%PATH%
SET PATH=%PATH%;c:\windows\system32\;c:\windows\system32\wbem


IF [%1] == [] (
	SET "PGDATA=C:\Polarion\data\postgres-data"
) else (
	SET "PGDATA=%~1"
) 

REM Get memory size
for /f "skip=1" %%p in ('wmic os get totalvisiblememorySize') do ( 
  SET m=%%p
  GOTO :done
)
:done

if "%m%" == "" (
	set %ERRORLEVEL%=1
	GOTO: cleanup
) 

if %m% EQU 0 (
	set %ERRORLEVEL%=1
	GOTO: cleanup
)

SET sharedUnit=KB
SET /A sharedBuffer=(%m*15)/100
if !sharedBuffer! GEQ 1024 (
	SET /A sharedBuffer=%sharedBuffer/1024
	SET sharedUnit=MB
)

SET cachedUnit=KB
SET /A cacheSize=%m/3
if !cachedUnit! GEQ 1024 (
	SET /A cacheSize=%cacheSize/1024
	SET cachedUnit=MB
)

SET "max=max_connections = 80			# should be less than 10 * number of CPUs"
SET "shared=shared_buffers = !sharedBuffer!!sharedUnit!			# should be 10%%%% - 15%%%% of total system RAM"
SET "work_mem=work_mem = 10MB 			# should be 10MB - 100MB"
SET "main_mem=maintenance_work_mem = 200MB"
SET "fsync=fsync = off"
SET "syncCom=synchronous_commit = off"
SET "fullPage=full_page_writes = off"
SET "walBuff=wal_buffers = 256kB			# should be more than size of common transaction"
SET "checkSeg=checkpoint_segments = 32"
SET "effCach=effective_cache_size = !cacheSize!!cachedUnit!	# should be approx 1/3 of total system RAM"
SET "maxLocks=max_locks_per_transaction = 100		# specific for Polarion"
SET "logCollect=logging_collector = on"
SET "logDir=log_directory = '..//logs//postgresql'"
SET "logPre=log_line_prefix = '%%%%t %%%%d '"
SET "logRot=log_rotation_age = 7d"	
SET "logFileName=log_filename = 'postgresql-%%%%Y%%%%m%%%%d-%%%%H%%%%M-%%%%S.log'"	

SET INTEXTFILE="%PGDATA%\postgresql.conf"
SET OUTTEXTFILE="%PGDATA%\test_out.txt"

SET ignore=0

for /F "tokens=1,* delims=" %%A in ( '"findstr /n ^^ %INTEXTFILE%"') do (
	SET line=%%A
	SET "line=!line:*:=!"
	SET handled=0
	
	IF "!line:~0,15!" == "max_connections" (
		IF "!line!" NEQ "%max%" (
			call :replace "%max%" %OUTTEXTFILE%
			SET handled=1
		)
	)
	IF "!line:~0,14!" == "shared_buffers" (
		REM Must be done like this because of the % signs
		if "!line:~-3!" NEQ "RAM" (
			call :replace "%shared%" %OUTTEXTFILE%
			SET handled=1
		)
	)
	
	IF "!line:~0,8!" == "work_mem" (
		set ignore=1
	)
	IF "!line:~0,9!" == "#work_mem" (
		IF !ignore! == 0 (
			call :replace "%work_mem%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,20!" == "maintenance_work_mem" (
		set ignore=1
	)
	IF "!line:~0,21!" == "#maintenance_work_mem" (
		IF !ignore! == 0 (
			call :replace "%main_mem%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,5!" == "fsync" (
		set ignore=1
	)
	IF "!line:~0,6!" == "#fsync" (
		IF !ignore! == 0 (
			call :replace "%fsync%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,18!" == "synchronous_commit" (
		set ignore=1
	)
	IF "!line:~0,19!" == "#synchronous_commit" (
		IF !ignore! == 0 (
			call :replace "%syncCom%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,16!" == "full_page_writes" (
		set ignore=1
	)
	IF "!line:~0,17!" == "#full_page_writes" (
		IF !ignore! == 0 (
			call :replace "%fullPage%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,11!" == "wal_buffers" (
		set ignore=1	
	)
	IF "!line:~0,12!" == "#wal_buffers" (
		IF !ignore! == 0 (
			call :replace "%walBuff%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,19!" == "checkpoint_segments" (
		set ignore=1
	)
	IF "!line:~0,20!" == "#checkpoint_segments" (
		IF !ignore! == 0 (
			call :replace "%checkSeg%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,20!" == "effective_cache_size" (
		set ignore=1	
	)
	IF "!line:~0,21!" == "#effective_cache_size" (
		IF !ignore! == 0 (
			call :replace "%effCach%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,25!" == "max_locks_per_transaction" (
		set ignore=1
	)
	IF "!line:~0,26!" == "#max_locks_per_transaction" (
		IF !ignore! == 0 (
			call :replace "%maxLocks%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,17!" == "logging_collector" (
		set ignore=1
	)
	IF "!line:~0,18!" == "#logging_collector" (
		IF !ignore! == 0 (
			call :replace "%logCollect%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,13!" == "log_directory" (
		set ignore=1
	)
	IF "!line:~0,14!" == "#log_directory" (
		IF !ignore! == 0 (
			call :replace "%logDir%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,15!" == "log_line_prefix" (
		set ignore=1
	)
	IF "!line:~0,16!" == "#log_line_prefix" (
		IF !ignore! == 0 (
			call :replace "%logPre%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,16!" == "log_rotation_age" (
		set ignore=1
	)
	IF "!line:~0,17!" == "#log_rotation_age" (
		IF !ignore! == 0 (
			call :replace "%logRot%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	
	IF "!line:~0,12!" == "log_filename" (
		set ignore=1
	)
	IF "!line:~0,13!" == "#log_filename" (
		IF !ignore! == 0 (
			call :replace "%logFileName%" %OUTTEXTFILE%
		)
		set ignore=0
	)
	

	IF !handled! == 1 (
		SET modified=#!line!		
		ECHO !modified!>> %OUTTEXTFILE%	
	) ELSE (
		IF  "!line!" == "" (
			ECHO.>> %OUTTEXTFILE%
		) ELSE (
			ECHO(!line!>> %OUTTEXTFILE%
		)  
	)
)
if %ERRORLEVEL% NEQ 0 (
	del %OUTTEXTFILE%
) else (
	del %INTEXTFILE%
	rename %OUTTEXTFILE% "postgresql.conf"
)
GOTO :cleanup

:replace
	SET modified=%~1
	ECHO %modified%>> %OUTTEXTFILE%
GOTO :eof

:cleanup
SET PATH=%OLD_PATH%
if %ERRORLEVEL% NEQ 0 (
 exit /b %ERRORLEVEL%
)
GOTO :eof
