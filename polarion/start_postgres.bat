REM Starts PostgreSQL service (use only with run_1st.bat)
@ECHO OFF
cd ..\bundled\postgres\bin\

IF [%1] == [] ( SET "PGDATA=C:\Polarion\data\postgres-data" 
) ELSE ( SET "PGDATA=%~1" )
IF [%2] == [] ( SET PGPORT=5433
) ELSE ( SET "PGPORT=%2" )

pg_ctl -D %PGDATA% -l ..\dbinstall.log -o "-p %PGPORT%" start
ECHO Press any key to stop the server
pause >nul
pg_ctl -D "%PGDATA%" stop
exit
