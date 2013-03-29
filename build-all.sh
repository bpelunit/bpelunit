#!/bin/sh

# Abort build on first failure
set +x

mvn install
mvn -f tycho/pom.xml install
mvn -f net.bpelunit.dist/pom.xml install
