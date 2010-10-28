@echo off
REM *********** BPELUnit Runner *************
REM Expects the following ENV VARIABLES TO BE SET
REM * JAVA_HOME
REM * BPELUNIT_HOME
REM to be set

setlocal

if not defined JAVA_HOME echo ERROR: Environment variable JAVA_HOME is undefined & goto :eof
if not defined BPELUNIT_HOME echo ERROR: Environment variable BPELUNIT_HOME is undefined & goto :eof

set LIB_HOME="%BPELUNIT_HOME%\lib"
set CLASSPATH=
    
for %%1 in (%LIB_HOME%\*.jar) do call :addItem %%~f1

goto :run

:run
REM Classpath is set up, go run bpelunit

%JAVA_HOME%\bin\java net.bpelunit.framework.ui.command.BPELUnitCommandLineRunner %*

endlocal
goto :eof

:addItem
set CLASSPATH=%CLASSPATH%;%1
