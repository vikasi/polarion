@echo off
rem Type 'init_repository.bat -help' for usage.
call "%~dp0\exec.bat" InitRepositoryTool.groovy -cmd %0 -args %*
