@echo off

set WORKSPACE=%~dp0\..\data\workspace

if exist "%WORKSPACE%\polarion-data.backup" goto ERROR

@echo Backing up old polarion-data to polarion-data.backup...
rename "%WORKSPACE%\polarion-data" polarion-data.backup

@echo Launching Polarion server (will take very long)...
call "%~dp0\run.bat"

goto QUIT

:ERROR
@echo ERROR: Backup folder %WORKSPACE%\polarion-data.backup already exists. Please remove it and try it again.

:QUIT
