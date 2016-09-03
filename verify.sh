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

# these are all the files we're testing for the existence of

declare -a arr=(
      "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug.aar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom" \

      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug.apk" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version.pom" \

      "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version.war" \
      "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version-sources.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version-javadoc.jar" \
      "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version.pom" \

      )



declare -a pomFiles=(
      "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-overrides.pom" \
      "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-war-$version.pom" \
       )





for i in "${arr[@]}"
do
    # echo "Testing for $i"
    if [ ! -f "`eval echo ${i//>}`"  ] ; then
        echo "File $i is not there, aborting."
        exit 1

    #else
       # echo "$i" "OK";

    fi
done

echo " Result - PASS"

# END Issue 12



# BEGIN 23 verify that the dependencies within the AAR are declared
# our AAR should have at least two dependencies lists
echo "     Issue #23 and 27 - verify poms has dependencies declared, name and descriptions"

# strings to search for in our aar pom
declare -a strs=(
      "com.android.support" \
      "support-annotations" \
      "hello-world-lib" \
      "\<description\>" \
      "\<name\>" \
       )


for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in aar pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in aar pom"
        exit 1
    fi
done

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in aar pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in aar pom"
        exit 1
    fi
done


# check the jar
declare -a strs=(
      "commons-lang3" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in jar pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in jar pom"
        exit 1
    fi
done



# check the apk
declare -a strs=(
      "hello-world-aar" \
      "appcompat-v7" \
      "cardview-v7" \
      "design" \
      "recyclerview-v7" \
      "support-v4" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in apk pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in apk pom"
        exit 1
    fi
done



# END 23 verify that the dependencies within the AAR are declared





# BEGIN Issue 25

echo "     Issue #25 - verify overrides are correctly applied"

# strings to search for in our aar pom
declare -a strs=(
      "package=\"com.chrisdoyle.alex.wuz.here\"" \
      "android:versionCode=\"9999\"" \
      "android:versionName=\"OU812\"" \
      "android:minSdkVersion=\"16\"" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i hello-world-apk-overrides/build/intermediates/manifests/full/bar/debug/AndroidManifest.xml`" ];
    then
        # code if found
        echo " PASS - $i found in overrides APK AndroidManifest.xml"
    else
        # code if not found
        echo " FAIL - $i NOT found in overrides APK AndroidManifest.xml"
        exit 1
    fi
done

# END Issue 25



# BEGIN 27 verify dependency wildcards are correct

echo "     Issue #27 - verify poms has dependencies declared correctly with wildcard"
# check the apk
declare -a strs=(
      "\<version\>\[1\.3\,\)\<\/version\>" \
      "\<version\>LATEST\<\/version\>" \
      "\<version\>\[3\,\)\<\/version\>" \
      "\<artifactId\>commons\-math3\<\/artifactId\>" \
      "\<artifactId\>commons\-codec\<\/artifactId\>" \
      "\<version\>\[1\,\)\<\/version\>" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in apk pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in apk pom"
        exit 1
    fi
done

# END 27 verify dependency wildcards are correct





# BEGIN 38 verify dependency scope are correct
# we use sed here because we're searching for multiline strings
echo "     Issue #31 - verify poms has dependencies declared correctly with scope"
# check the apk

declare -a strs=(
    #provided
      "'/\<dependency\>/,/\<groupId\>com\.chrisdoyle\<\/groupId\>/,/\<artifactId\>hello\-universe\-lib\<\/artifactId\>/,/\<scope\>provided\<\/scope\>/!d'" \
    #test
      "'/\<dependency\>/,/\<groupId\>junit\<\/groupId\>/,/\<artifactId\>junit\<\/artifactId\>/,/\<scope\>test\<\/scope\>/'" \
    #compile
      "'/\<dependency\>/,/\<groupId\>org.osmdroid\<\/groupId\>/,/\<artifactId\>osmdroid-android\<\/artifactId\>/./\<scope\>compile\<\/scope\>/'" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo sed -e $i ~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in apk pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in apk pom"
        exit 1
    fi
done

# END 38 verify dependency scope are correct


# BEGIN Issue 31 War file support

# strings to search for in our war pom
declare -a strs=(
      "\<packaging\>\"war\"\</packaging\>" \
      "\<groupId\>org\.apache\.commons\<\/groupId\>" \
      "\<artifactId\>hello\-world\-lib\<\/artifactId\>" \
       )

for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i hello-world-war/build/publications/webApp/pom-default.xml`" ];
    then
        # code if found
        echo " PASS - $i found in WAR pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in WAR pom"
        exit 1
    fi
done


# END Issue 31


# BEGIN Issue 22, pom overrides

echo "     Issue #22 pom overrides"

# strings to search for in our aar pom
declare -a strs=(
      "LGPL" \
       )


for i in "${strs[@]}"
do
    if [ "`eval echo grep -Fxq $i ~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-$version.pom`" ];
    then
        # code if found
        echo " PASS - $i found in override apk pom"
    else
        # code if not found
        echo " FAIL - $i NOT found in override apk pom"
        exit 1
    fi
done



# BEGIN issue 51 pom's with URL
echo "     Issue #51 pom URL's missing"
# strings to search for in our pom's
declare -a strs=(
      "\\<url\>http\:\/\/chrisdoyle\.github\.io\/gradle\-fury\/\<\/url\>" \
       )


for i in "${pomFiles[@]}"
do
    for k in "${strs[@]}"
    do
        # echo "Testing for $i"
        if [ "`eval echo grep -Fxq $k $i`" ]; then
            echo " PASS - $k found in $i found apk pom"

        else
            echo "FAIL $k not found in file $i, aborting."
            exit 1

        fi
    done
done


echo " Result - PASS"



echo "     End Result - PASS"

echo "Done."