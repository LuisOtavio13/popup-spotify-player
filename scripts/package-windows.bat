@echo off
REM Requires: JDK 17+ with jpackage, WiX toolkit installed for exe installer creation
REM Run from project root in a Windows environment (or CI runner)














pauseecho Packaged installer is in %OUTDIR%\%APP_NAME%.exe
nREM Example jpackage call - adjust paths for WiX and installer type
njpackage --name %APP_NAME% --input target --main-jar %MAIN_JAR% --main-class com.exemple.demo.Launcher --type exe --java-options "-Xmx512m" "-XX:MaxRAMPercentage=60.0" --dest %OUTDIR%
nset OUTDIR=dist
nif exist %OUTDIR% rmdir /s /q %OUTDIR%)  for /f "delims=" %%i in ('dir /b target\*-SNAPSHOT.jar') do set MAIN_JAR=%%i  mvn -DskipTests package  echo Building project...if "%MAIN_JAR%"=="" (for /f "delims=" %%i in ('dir /b target\*-SNAPSHOT.jar') do set MAIN_JAR=%%iset APP_NAME=popup-spotify-player
nset MAIN_JAR=nIF NOT EXIST target ( mvn -DskipTests package )