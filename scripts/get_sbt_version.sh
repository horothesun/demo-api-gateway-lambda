#!/bin/bash

SBT_VERSION=$(grep "sbt.version=" "project/build.properties" | grep -o "[^=]*$")

[[ -z "${SBT_VERSION}" ]] && echo "Error: SBT_VERSION not found in project/build.properties" && exit 123

export SBT_VERSION
