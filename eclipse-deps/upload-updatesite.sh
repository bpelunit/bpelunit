#!/bin/bash

# Simple Bash script for uploading the update site for lftp
# Copyright (C) 2014 Antonio García-Domínguez
# Licensed under the EPL

# Note: username and password should be written in ~/.netrc,
# to avoid storing anything here and getting any prompts. The
# file should have 600 for its permissions mask.

# Uploads the update site folder to eclipse-deps-YYYYMMDD (UTC time)

lftp -e "mirror -R -x '.gitignore|.project' net.bpelunit.eclipse.extradeps.updatesite eclipse-deps-$(date -u +%Y%m%d%H%M)" bpelunit.net
