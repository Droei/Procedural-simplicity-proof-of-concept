@echo off
setlocal

:: Set the folder path here
set "folder=C:\Users\Daan\Desktop\Proof of concept\Procedural-simplicity-proof-of-concept\JavaMinecraftProject\untitled\src"

:: Count all files recursively
for /f %%A in ('dir /a-d /s /b "%folder%" ^| find /c /v ""') do set count=%%A

echo Total files in "%folder%": %count%

pause