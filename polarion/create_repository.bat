@echo off
rem Type 'create_repository.bat -help' for usage.
call "%~dp0\exec.bat" CreateRepositoryTool.groovy -cmd %0 -args %*
