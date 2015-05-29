
set REMOVE_COMMON_REPO=no
set PULL_COMMON_REPO=no
set CLEAN_PROJECT=no
set BUILD_COMMON_ONLY=no

set MAINDIR=%~dp0
cd %MAINDIR%..\fablab-common
set COMMON_DIR=%~dp0
cd %MAINDIR%

REM Loop until all parameters are used up
:DoWhile
IF "%1" == "" GOTO EndDoWhile
IF "%1" == "-r" GOTO Remove
IF "%1" == "-p" GOTO Pull
IF "%1" == "-c" GOTO Clean
IF "%1" == "-b" GOTO Build

:Default
ECHO wrong parameter: %1
CALL :usage
EXIT 1

:Remove
set REMOVE_COMMON_REPO = yes
SHIFT
GOTO :DoWhile

:Pull
set PULL_COMMON_REPO = yes
SHIFT
GOTO :DoWhile

:Clean
set CLEAN_PROJECT = yes
SHIFT
GOTO :DoWhile

:Build
set BUILD_COMMON_ONLY = yes
SHIFT
GOTO :DoWhile

:EndDoWhile
IF "%REMOVE_COMMON_REPO%" == "yes" (
	IF "%PULL_COMMON_REPO%" == "yes" (
		ECHO deleting and pulling does not work
		exit 1
	)
)	

IF "%REMOVE_COMMON_REPO%" == "yes" (
	ECHO Removing common repo
	DELTREE %COMMON_DIR%
	
)

IF "%PULL_COMMON_REPO%" == "yes" (
	ECHO Pulling changes for common repo
	git -C %COMMON_DIR% pull
)

ECHO Running gradle tasks clean and cleanup
start gradlew clean cleanup

IF "%CLEAN_PROJECT%" == "yes" (
	ECHO Running gradle task cleanProject
	start gradlew cleanupProject
)
        
IF "%BUILD_COMMON_ONLY%" == "yes" (
	ECHO Starting gradle buildfor common repo
	start gradlew :..\fablab-common:build
) ELSE (
	ECHO Starting gradle build
	start gradlew build
)	
goto end

:usage
ECHO
ECHO Helper script to build android project:
ECHO
ECHO fablab-android: %MAINDIR%
ECHO fablab-common: %COMMON_DIR%
ECHO
ECHO usage:
ECHO -e  \\t -r fully delete common repo
ECHO -e  \\t -c clean/remove IntellJ project
ECHO -e  \\t -p pull changes in for common repo
ECHO -e  \\t -b only build common repo
ECHO
EXIT /B 0

:end