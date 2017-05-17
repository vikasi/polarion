@echo off
call "%~dp0\exec.bat" RemoteControlTool.groovy -cmd %0 -args shutdown %*
