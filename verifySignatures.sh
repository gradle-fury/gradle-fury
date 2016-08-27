#!/bin/bash

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


echo " ======== Validation script for gradle fury ======== "
echo " "


# BEGIN Issue 12
echo "     Issue #12 - verify all artifacts were published to mavenLocal"

# these are all the files we're testing for signatures

declare -a arr=(
      "./hello-world-aar/build/outputs/aar/hello-world-aar-$version-debug.aar" \
      "./hello-world-aar/build/outputs/aar/hello-world-aar-$version-release.aar" \
      "./hello-world-aar/build/libs/hello-world-aar-$version-debug-javadoc.jar" \
      "./hello-world-aar/build/libs/hello-world-aar-$version-debug-sources.jar" \
      "./hello-world-aar/build/libs/hello-world-aar-$version-release-javadoc.jar" \
      "./hello-world-aar/build/libs/hello-world-aar-$version-release-sources.jar" \
      "./hello-world-aar/build/publications/androidArtifacts/pom-default.xml" \

      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-barDebug.apk" \
      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-barRelease.apk" \
      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-bazDebug.apk" \
      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-bazRelease.apk" \
      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-fooDebug.apk" \
      "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-fooRelease.apk" \

      "./hello-world-apk/build/libs/hello-world-apk-$version-barDebug-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-barDebug-sources.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-barRelease-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-barRelease-sources.jar" \

      "./hello-world-apk/build/libs/hello-world-apk-$version-bazDebug-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-bazDebug-sources.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-bazRelease-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-bazRelease-sources.jar" \

      "./hello-world-apk/build/libs/hello-world-apk-$version-fooDebug-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-fooDebug-sources.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-fooRelease-javadoc.jar" \
      "./hello-world-apk/build/libs/hello-world-apk-$version-fooRelease-sources.jar" \
      "./hello-world-apk/build/publications/androidArtifacts/pom-default.xml" \

      "./hello-world-lib/build/libs/hello-world-lib-$version.jar" \
      "./hello-world-lib/build/libs/hello-world-lib-$version-javadoc.jar" \
      "./hello-world-lib/build/libs/hello-world-lib-$version-sources.jar" \
      "./hello-world-lib/build/publications/javaArtifacts/pom-default.xml" \
      )

for i in "${arr[@]}"
do
    # echo "Testing for $i"
    if [ ! -f "`eval echo ${i//>}`"  ] ; then
        echo "File $i is not there, aborting."
        exit 1

    else
        echo "$i" "checking signature";
        eval "/usr/local/bin/gpg --version $1.asc"
        ret_code=$?
        if [ret_code !=0]; then
            echo "Signatures didn't verify!"
            exit 1
        fi


    fi
done

echo " Result - PASS"

# END Issue 12


echo "     End Result - PASS"

echo "Done."