@echo off
set MAVEN_OPTS=-XX:MaxPermSize=128m -Xss32m -Xmx800m -server
mvn %* clean install -DskipTests
