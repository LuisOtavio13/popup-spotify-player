#!/usr/bin/env bash
set -euo pipefail

# Requires: JDK 17+ (with jlink/jpackage), mvn
# Run from project root

APP_NAME=popup-spotify-player
MAIN_JAR=$(ls target/*-SNAPSHOT.jar | head -n1 || true)
if [ -z "$MAIN_JAR" ]; then
  echo "Building project..."
  mvn -DskipTests package
  MAIN_JAR=$(ls target/*-SNAPSHOT.jar | head -n1)
fi

OUTDIR=dist
RUNTIME_IMAGE=runtime
rm -rf $OUTDIR $RUNTIME_IMAGE

# Create a reduced runtime image (adjust modules if needed)
jlink --output $RUNTIME_IMAGE --add-modules java.base,java.logging,java.net.http,java.sql,java.desktop --compress=2 --strip-debug --no-header-files --no-man-pages

# Package with jpackage (creates .deb or app image depending on --type)
jpackage \
  --type app-image \
  --input target \
  --name $APP_NAME \
  --main-jar $(basename "$MAIN_JAR") \
  --main-class com.exemple.demo.Launcher \
  --runtime-image $RUNTIME_IMAGE \
  --java-options "-Xmx512m" "-XX:MaxRAMPercentage=60.0" \
  --dest $OUTDIR

echo "Packaged app image is in $OUTDIR. For installer, run jpackage with --type deb or --type rpm as needed." 
