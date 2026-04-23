#!/bin/sh
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"

REPO_ROOT="$PODS_TARGET_SRCROOT"
"$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
    -Pkotlin.native.cocoapods.archs="$ARCHS" \
    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
