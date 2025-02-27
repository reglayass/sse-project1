@echo off

REM Default values
set "iterations=0"

REM Enable delayed expansion
setlocal enabledelayedexpansion

REM Parse command-line options
for %%i in (%*) do (
    echo %%i | findstr /b /c:"--iter=" >nul
    if /i "%%i" NEQ "" (
        set "iterations=%%i"
        set "iterations=!iterations:--iter=!"
        echo Iterations: !iterations!
    )
)


REM Ensure --iter argument is provided
echo !iterations!
if !iterations! == 0 (
    call :usage
)

REM List of frameworks to test
set "frameworks=flask express springboot"

REM Start time recording
for /f "tokens=1-3 delims=:." %%a in ("%time%") do (
    set /a "start_time=(((%%a*60)+%%b)*60)+%%c"
)

REM Run the tests sequentially for each iteration
for /l %%i in (1,1,%iterations%) do (
    echo Test Iteration: %%i

    REM Run tests for each framework
    for %%f in (%frameworks%) do (
        call :run_test %%f %%i
        REM Sleep for 60 seconds before running the next framework
        echo Sleeping for 1 min before next framework...
        timeout /t 60 /nobreak >nul
    )
)

REM End time recording
for /f "tokens=1-3 delims=:." %%a in ("%time%") do (
    set /a "end_time=(((%%a*60)+%%b)*60)+%%c"
)

REM Calculate execution time
set /a "execution_time=(end_time - start_time) / 60"
echo Total Execution time: %execution_time% minutes

exit /b 0

REM ---------------------------------------------
REM FUNCTION DEFINITIONS
REM ---------------------------------------------

REM Function to display usage information
:usage
    echo Usage: %0 --iter=^<number^>
    exit /b

REM Function to run tests
:run_test
set "framework=%~1"
set "iter=%~2"
set "result_dir=tests\results\%framework%"

REM Assign ports based on the framework
set "port="
if "%framework%"=="flask" set "port=5000"
if "%framework%"=="express" set "port=3000"
if "%framework%"=="springboot" set "port=8080"
if not defined port (
    echo Unknown framework: %framework%
    exit /b
)

REM Check if results directory exists
if not exist "%result_dir%" (
    echo Results directory for %framework% does not exist. Creating it...
    mkdir "%result_dir%"
)

REM Start the framework
echo Starting framework: %framework%...
docker-compose up %framework% -d

REM Give it some time to build the container
echo Letting Docker start up...
timeout /t 15 /nobreak >nul

REM Run artillery test
echo Starting test for %framework%...
energibridge.exe -g --output="%result_dir%\results_%framework%_%iter%.csv" ab.exe -c 150 -n 10000 "http://localhost:%port%/filter?genre=Drama&rating=9&votes=100"

echo Test done for %framework%.
docker-compose down >nul 2>&1

echo Process cleanup complete for %framework%.
exit /b
