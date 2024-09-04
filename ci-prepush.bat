@echo off
setlocal enabledelayedexpansion

rem Check if gradlew exists in the project
if not exist "%~dp0gradlew" (
    echo Error: gradlew not found in the project.
    exit /b 1
)

echo Starting all checks and tests...

call :run_gradle_task "check -p build-logic"
call :run_gradle_task "spotlessApply --no-configuration-cache"
call :run_gradle_task "dependencyGuardBaseline"
call :run_gradle_task "detekt"
call :run_gradle_task "testDebug :lint:test :androidApp:lintRelease :lint:lint"
call :run_gradle_task "build"
call :run_gradle_task "updateReleaseBadging"

echo All checks and tests completed successfully.
exit /b 0

:run_gradle_task
echo ########################################################
echo Running: %~1
call "%~dp0gradlew" %~1
if %ERRORLEVEL% neq 0 (
    echo Error: Task %~1 failed
    exit /b 1
)
echo ########################################################
exit /b 0