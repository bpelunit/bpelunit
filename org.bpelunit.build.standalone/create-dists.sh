#!/bin/bash

set +e
ant clean build
VERSION=$(git describe)
MAIN_DIR=bpelunit-$VERSION

# Binary distribution
BIN_DIST=bpelunit-standalone-$VERSION.zip
ln -s build bpelunit
zip -r "$BIN_DIST" bpelunit

# Source distribution
SRC_DIST=bpelunit-src-$VERSION.zip
CWD=$(pwd)
pushd .. >/dev/null
git archive -o "$SRC_DIST" HEAD
mv "$SRC_DIST" "$CWD"
popd >/dev/null
