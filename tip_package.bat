@echo off
setlocal

:: Set the name of the output archive
set ZIPNAME=card-editor.zip

:: Delete existing zip if it exists
if exist "%ZIPNAME%" del "%ZIPNAME%"

:: Create the zip using PowerShell
powershell -Command "Compress-Archive -Path launch.bat, card-editor.jar, resources -DestinationPath %ZIPNAME%"

echo.
echo Done: Created %ZIPNAME%
pause
