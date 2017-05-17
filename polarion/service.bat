@echo off
rem Type 'service.bat -help' for usage.
call "%~dp0\exec.bat" PolarionServiceTool.groovy -cmd %0 -args %*
