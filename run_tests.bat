@echo off
setlocal enabledelayedexpansion

REM Default values
set iterations=30
set flask_test=false
set express_test=false
set springboot_test=false

REM Function to display usage information
:usage
echo Usage: %0 --flask --express --springboot --iter=<number>
exit /b 1

REM Parse command-line options
:parse_options
if "%~1"=="" goto end_parse
if "%~1"=="--flask" (
    set flask_test=true
) else if "%~1"=="--express" (
    set express_test=true
) else if "%~1"=="--springboot" (
    set springboot_test=true
) else if "%~1:~0,6%"=="--iter=" (
    set iterations=%~1:~7%
) else (
    goto usage
)
shift
goto parse_options

:end_parse

REM If no tests are specified, show usage
if "%flask_test%"=="false" if "%express_test%"=="false" if "%springboot_test%"=="false" goto usage

REM Install artillery as a package
npm install

REM Function to run tests
:run_test
set framework=%1
set result_dir=tests\results\%framework%
set test_file=tests\test_%framework%.yml

REM Check if results directory exists
if not exist "%result_dir%" (
    echo Results directory for %framework% does not exist. Creating it...
    mkdir "%result_dir%"
)

REM Run the test for the specified number of iterations
for /L %%i in (1,1,%iterations%) do (
    echo Test Iteration: %%i

    echo Starting Energibridge for %framework%...
    start /B cmd /c "sudo ./energibridge --output="%result_dir%\results_%framework%_%%i.csv" docker compose %framework% up > nul 2>&1"
    
    REM Give it some time to build the container
    timeout /t 30 > nul

    REM Run artillery test
    echo Starting Artillery test for %framework%...
    artillery run "%test_file%" > nul 2>&1
    
    REM Artillery is done, kill the energibridge process
    echo Artillery done, killing process...
    taskkill /F /IM energibridge.exe > nul 2>&1
    echo Iteration %%i: Process cleanup complete.

    REM Sleep for 60 seconds before running the next iteration
    echo Sleeping for 1 min...
    timeout /t 60 > nul
)

goto :eof

REM Run the selected tests
if "%flask_test%"=="true" call :run_test "flask"
if "%express_test%"=="true" call :run_test "express"
if "%springboot_test%"=="true" call :run_test "springboot"

echo All tests completed.

exit