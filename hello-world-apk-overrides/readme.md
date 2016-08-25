# Purpose

This project is an identical clone of `hello-world-apk` module and designed to show you how to override the default values
produced by gradle-fury.

# Notable files

`build.gradle` is the only thing you need to look at.

# Pro Tip

Include `apply from: 'https://raw.githubusercontent.com/chrisdoyle/gradle-fury/master/gradle/android-support.gradle'`
 in the build file before the `android { ... }` block. (Someone ask why)
 
## Why?

Simple, if you apply fury after your android block, any settings covered by `gradle-fury` will be overridden with the values
that the gradle-fury wants. It's like Chuck Norris dividing by zero. 

## Is there a way to change this behavior?

No. I'm sorry. I can't do that Dave.
 - Hal 9000

## How are the default values determined?

Great question. Most values are determined from your settings in `gradle.properties` in the root of your project, however a few are not.

 - ApplicationId - if you don't define it, gradle-fury will generate it from the pom groupId, the module name, some string manipulation, the build flavor (if any), and some magic pixie dust.
 
Aside from that, there's a few hard coded defaults that you should be aware of.
Note: these are subject to change and are defined in `gradle/android-support.gradle`
 
 - minSdkVersion = 15
 - targetSdkVersion = 23
 - versionCode = 1
 - compileSdkVersion = 23
 - buildToolsVersion = 23.0.3