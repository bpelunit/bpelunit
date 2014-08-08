#!/bin/sh

# Abort build on first failure
set +x

# Do not run the end-to-end ActiveBPEL test cases
# (some devs may not have it set up).
unset CATALINA_HOME

mvn clean install
mvn -f tycho/pom.xml clean install
mvn -f net.bpelunit.dist/pom.xml clean install
