#!/bin/bash

set +e
ant clean build
VERSION=$(git describe)
MAIN_DIR=bpelunit-$VERSION

# Binary distribution
BIN_DIST=bpelunit-standalone-$VERSION.zip
ln -s build "$MAIN_DIR"
zip -r "$BIN_DIST" "$MAIN_DIR"

# Source distribution
SRC_DIST=bpelunit-src-$VERSION.zip
CWD=$(pwd)
pushd .. >/dev/null
git archive --prefix="$MAIN_DIR/" -o "$SRC_DIST" HEAD
mv "$SRC_DIST" "$CWD"
popd >/dev/null
