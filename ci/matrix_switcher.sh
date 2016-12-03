#!/usr/bin/env bash


echo Setting gradle version to $GRADLE_VERSION and android plugin to $GRADLE_PLUGIN_VERSION
#GRADLE_VERSION
#GRADLE_PLUGIN_VERSION
#original is distributionUrl=https\://services.gradle.org/distributions/gradle-2.14-bin.zip
cp gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.properties.old
sed "s/distributionUrl\=https\\\:\/\/services\.gradle\.org\/distributions\/gradle-2.14\-bin\.zip/distributionUrl=https\\\\:\/\/services\.gradle\.org\/distributions\/gradle-$GRADLE_VERSION\-bin\.zip/g" gradle/wrapper/gradle-wrapper.properties >  gradle/wrapper/gradle-wrapper.properties.new
mv gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.properties.bk
mv gradle/wrapper/gradle-wrapper.properties.new gradle/wrapper/gradle-wrapper.properties


#original android-plugin.version=2.1.0
cp gradle.properties gradle.properties.old
sed "s/android\-plugin\.version\=2\.1\.0/android\-plugin\.version\=$GRADLE_PLUGIN_VERSION/g" gradle.properties >  gradle.properties.new
mv gradle.properties gradle.properties.bk
mv gradle.properties.new gradle.properties


#android.buildToolsVersion=23.0.3
#original android-plugin.version=2.1.0
cp gradle.properties gradle.properties.old
sed "s/android\.buildToolsVersion\=23\.0\.3/android\.buildToolsVersion\=$ANDROID_GRADLE/g" gradle.properties >  gradle.properties.new
mv gradle.properties gradle.properties.bk
mv gradle.properties.new gradle.properties



cp gradle.properties gradle.properties.old
sed "s/android\.compileSdkVersion\=23/android\.compileSdkVersion\=$COMPILE_SDK/g" gradle.properties >  gradle.properties.new
mv gradle.properties gradle.properties.bk
mv gradle.properties.new gradle.properties