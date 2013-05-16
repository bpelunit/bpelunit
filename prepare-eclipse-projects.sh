#!/bin/sh

mvn $@ clean install -DskipTests
for dep in tycho-deps/*/pom.xml; do
    mvn $@ -f "$dep" install
done
