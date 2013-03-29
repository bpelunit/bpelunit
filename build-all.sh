#!/bin/sh

mvn install
mvn -f tycho/pom.xml install
mvn -f net.bpelunit.dist/pom.xml install
