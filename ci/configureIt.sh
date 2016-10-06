#!/bin/bash

# THIS FILE IS NOT YET USED


#this file verifies that the expected artifacts are published to maven local

# only run this script from a unix like system with bash
# and only run it after the following gradle command has been executed
# ./gradlew install -Pprofile=javadocs,sources

# Exit behavior:
#    Exit code 0: all tests passed
#    Exit code > 0: at least one test failed

# the version we're expecting
version=""

# the properties file that gradle uses
PROPERTIES_FILE=gradle.properties


# Reads property $2 from properties file $1 and echos the value. To call this method do:
# src http://gothither.blogspot.com/2012/06/read-value-from-java-properties-file-in.html
#     V=$(getProp filename property)
#
function getProp () {
    # ignore lines with '#' as the first non-space character (comments)
    # grep for the property we want
    # get the last match just in case
    # strip the "property=" part but leave any '=' characters in the value

    echo `sed '/^[[:space:]]*\#/d' $1 | grep $2  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
}

# get the version number from the gradle build
version=`eval getProp $PROPERTIES_FILE pom.version`



echo Setting gradle version to $version for IT tests

#TIME.HACKER.VERSION=1.0.10-SNAPSHOT
sed "s/TIME.HACKER.VERSION=1.0.15-SNAPSHOT/TIME.HACKER.VERSION=$version/g" it/GradleConsumers/gradle.properties > it/GradleConsumers/gradle.properties.new
mv it/GradleConsumers/gradle.properties it/GradleConsumers/gradle.properties.old
mv it/GradleConsumers/gradle.properties.new it/GradleConsumers/gradle.properties


#<version>1.0.10-SNAPSHOT</version>
sed "s/\<version\>1.0.15-SNAPSHOT\<\/version\\>/\<version\>$version\<\/version\>/g" it/MavenConsumers/MavenAPK/pom.xml >  it/MavenConsumers/MavenAPK/pom.xml.new
mv it/MavenConsumers/MavenAPK/pom.xml it/MavenConsumers/MavenAPK/pom.xml.old
mv it/MavenConsumers/MavenAPK/pom.xml.new it/MavenConsumers/MavenAPK/pom.xml