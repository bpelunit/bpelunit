#!/bin/bash

# Data source inliner wrapper script
# Copyright (C) 2010 Antonio García-Domínguez
#
# Licensed under the terms of BPELUnit itself.

if [ -z "${JAVA_HOME}" ]; then
  echo \
    "You should set the JAVA_HOME environment variable to the absolute\n" \
    "\rpath of the root directory of your Java Virtual Machine. Look for\n" \
    "\rlib/tools.jar on your system if you do not know where it is." 1>&2
  exit 1
fi

if [ -z "${BPELUNIT_HOME}" ]; then
  echo \
    "You should set the BPELUNIT_HOME environment variable to the BPELUnit\n" \
    "\rinstallation directory. This should be where you unpacked the\n" \
    "\rstandalone distribution." 1>&2
  exit 2
fi

## CONFIGURATION SECTION ##############

MAIN_CLASS=org.bpelunit.utils.datasourceinliner.CLIRunner

## UTILITIES ##########################

echo_classpath() {
  echo "$CLASSPATH"
  find "$BPELUNIT_HOME/lib" -name '*.jar' -print | \
    (while read i; do echo -n ":$i"; done)
}

## MAIN BODY ##########################

# Add all BPELUnit .jar files to the classpath and run BPELUnit
CLASSPATH=$(echo_classpath) "$JAVA_HOME/bin/java" "$MAIN_CLASS" $@
