#!/bin/sh

# Abort build on first failure
set +x

mvn clean install
mvn -f tycho/pom.xml clean install
mvn -f net.bpelunit.dist/pom.xml clean install
