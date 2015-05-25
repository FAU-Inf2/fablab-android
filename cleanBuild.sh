#!/bin/bash
set -e

REMOVE_COMMON_REPO=no
PULL_COMMON_REPO=no
CLEAN_PROJECT=no
BUILD_COMMON_ONLY=no

MAINDIR=`dirname "$(readlink -f "$0")"`
COMMON_DIR="$MAINDIR/../fablab-common"

function usage
{
	echo
	echo Helper script to build android project:
	echo
	echo fablab-android: $MAINDIR
	echo fablab-common: $COMMON_DIR
	echo
	echo usage:
	echo -e  \\t -r fully delete common repo
	echo -e  \\t -c clean/remove IntellJ project
	echo -e  \\t -p pull changes in for common repo
	echo -e  \\t -b only build common repo
	echo
}

# Loop until all parameters are used up
while [ "$1" != "" ]; do
	case $1 in
		-r )
			REMOVE_COMMON_REPO=yes
		;;
		-p )
			PULL_COMMON_REPO=yes
		;;
		-c )
			CLEAN_PROJECT=yes
		;;
		-b )
			BUILD_COMMON_ONLY=yes
		;;
		* )
			echo wrong parameter: $1
			usage
			exit 1
	esac
	shift
done

if [ "$REMOVE_COMMON_REPO" == "yes" -a "$PULL_COMMON_REPO" == "yes" ]; then
	echo deleting and pulling does not work
	exit 1
fi

if [ "$REMOVE_COMMON_REPO" = "yes" ]; then
	echo Removing common repo
	rm -rvf $COMMON_DIR
fi

if [ "$PULL_COMMON_REPO" == "yes" ]; then
	echo Pulling changes for common repo
	git -C $COMMON_DIR pull
fi

echo Running gradle tasks clean and cleanup
./gradlew clean cleanup

if [ "$CLEAN_PROJECT" == "yes" ]; then
        echo Running gradle task cleanupProject
        ./gradlew cleanupProject
fi

if [ "$BUILD_COMMON_ONLY" == "yes" ]; then
	echo Starting gradle build for common repo
	./gradlew :../fablab-common:build
else
	echo Starting gradle build
	./gradlew build
fi
