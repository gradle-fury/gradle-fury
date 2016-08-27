# gradle-fury

*"Before I could pull the trigger, I was hit by lightning and bitten by a cobra. I blacked out, and
saw images of ancient Shaolin temples and monks mastering the art of kung-fu. There was an ancient
prophecy about a new form of kung-fu so powerful, only one man can master it: The Chosen One. When I
woke up, I saw the kung-fu master running towards me. I could feel my body mutate, into some sort of
kung-fu freak of nature." --Kung Fury*


Purpose
-------

An alternative Gradle helper to publish multi-flavored Android artifacts to local and remote Maven
repositories with pretty POM files using simple property configuration.

Build Status
------------

Develop: [![Build Status](https://travis-ci.org/chrisdoyle/gradle-fury.svg?branch=develop)](https://travis-ci.org/chrisdoyle/gradle-fury)
Master: [![Build Status](https://travis-ci.org/chrisdoyle/gradle-fury.svg?branch=master)](https://travis-ci.org/chrisdoyle/gradle-fury)

Usage
-----

Please refer to the dummy "Hello World" subprojects along with the provided root project
`build.gradle` and `gradle.properties` files included with this project as a general usage guide:

* Example Android Library: [hello-world-aar](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-aar)
* Example Android Application: [hello-world-apk](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-apk)
* Example Java Library: [hello-world-lib](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-lib)
* Root [build.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/build.gradle)
* Root [gradle.properties](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle.properties)


#### 1. Create or modify the root gradle.properties file in your project

This is the secret sauce.  I crave simplicity, and I just adore the idea of being able to house
simple project configuration in a singular, standard location in each of my projects, instead of
littering it in several locations throughout the codebase.

There are a handful of key properties which the `gradle-fury` helper scripts use to make the magic
happen, which are defined in the following sub-sections.

##### Maven Repository (e.g. Sonatype Nexus Repository Manager) Configuration Properties

Define the properties which are used by the ```maven-publish``` plugin for artifact publication:

  * The `NEXUS_USERNAME` and `NEXUS_PASSWORD` properties define the username/password credentials used
to authenticate to the target repository.  Though the properties are prefixed with `NEXUS_`, they
could really apply to any Maven repository.

  * The `RELEASE_REPOSITORY_URL` and `SNAPSHOT_REPOSITORY_URL` properties define the
[repositories](https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:repositories)
in the `PublishingExtension.getRepositories()` container.

For example:

```groovy
NEXUS_USERNAME=president.skroob
NEXUS_PASSWORD=12345

RELEASE_REPOSITORY_URL=https://oss.sonatype.org/service/local/staging/deploy/maven2/
SNAPSHOT_REPOSITORY_URL=https://oss.sonatype.org/content/repositories/snapshots/
```

##### Android Configuration Properties

Define the general
[Android Build Configuration properties](http://developer.android.com/tools/building/configuring-gradle.html)
to apply to all Android Library and Application projects:

```groovy
android.buildToolsVersion=23.0.2
android.compileSdkVersion=23
android.minSdkVersion=15
android.targetSdkVersion=23
```

Define the Android `versionCode` property (per the
[Android Documentation](http://developer.android.com/tools/publishing/versioning.html), "an
integer value that represents the version of the application code, relative to other versions"):

```groovy
android.versionCode=1234
```

Define the app [signing configuration](http://developer.android.com/tools/publishing/app-signing.html) for
Release artifacts:

```groovy
android.signingConfigs.release.storeFile=/path/to/your/keystore.jks
android.signingConfigs.release.storePassword=Tr0ub4dor&3
android.signingConfigs.release.keyAlias=MyAndroidKey
android.signingConfigs.release.keyPassword=correcthorsebatterystaple
```

##### Maven POM Generation Properties

These properties map directly to the properties defined in the
[Maven POM Reference](https://maven.apache.org/pom.html) documentation:

```groovy
pom.packaging
pom.name
pom.description
pom.url
pom.inceptionYear
pom.licenses.license.[n].name ***
pom.licenses.license.[n].url ***
pom.licenses.license.[n].distribution ***
pom.organization.name
pom.organization.url
pom.developers.developer.[n].id ***
pom.developers.developer.[n].name ***
pom.developers.developer.[n].email ***
pom.developers.developer.[n].organization ***
pom.developers.developer.[n].role.[n] ***
pom.issueManagement.system
pom.issueManagement.url
pom.ciManagement.system
pom.ciManagement.url
pom.scm.url
pom.scm.connection
pom.scm.developerConnection
pom.distributionManagement.site.id
pom.distributionManagement.site.url
```

__***NOTE__: Multiple licenses, developers, and developer roles are supported by using a numeric
suffix `[n]` as an identifier for each entry; for example:

```groovy
pom.developers.developer.0.id=kung.fury
pom.developers.developer.0.name=Kung Fury
pom.developers.developer.0.email=kungfury@knuckl.es
pom.developers.developer.0.organization=Miami Police Department
pom.developers.developer.0.role.0=Police Officer
pom.developers.developer.0.role.1=Kung Fu Master

pom.developers.developer.1.id=hackerman
pom.developers.developer.1.name=Hackerman
pom.developers.developer.1.email=hackerbot@hackerm.an
pom.developers.developer.1.organization=Hackerman Worldwide
pom.developers.developer.1.role.0=Computer Whiz
pom.developers.developer.1.role.1=Hackerbot
```

The included root [gradle.properties](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle.properties)
file may be used as a template.


#### 2. Modify the `allprojects` closure in your root project `build.gradle`

The required modifications consist of defining the default `project.group` and `project.version`
properties, and applying the
[maven-support.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle/maven-support.gradle)
script in the `allprojects` closure, which will subsequently apply the configuration to all subprojects.

Please note that `project.group` and `project.version` *must* be defined before including
[maven-support.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle/maven-support.gradle)
since it uses these values.  Also note that these property values may be overridden by individual
subprojects.

```groovy
allprojects  {
    // NOTE: project.group and project.version must be defined before including
    // maven-support.gradle since it uses these values...
    project.group = ( project.hasProperty('pom.groupId')
            ? project.property('pom.groupId') : "" )

    project.version =
            ( project.hasProperty('pom.version') ? project.property('pom.version') : "1.0" )

    apply from: 'https://raw.githubusercontent.com/chrisdoyle/gradle-fury/master/gradle/maven-support.gradle'

		.
		.
		.

}
```

The included root [build.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/build.gradle)
file may be used as a reference for usage.


#### 3. Apply the [android-support.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle/android-support.gradle) script to each Android subproject [gradle.properties](https://github.com/chrisdoyle/gradle-fury/blob/master/gradle.properties)

Add the following to the
[build.gradle](https://github.com/chrisdoyle/gradle-fury/blob/master/build.gradle) of each Android
(Library or Application) subproject defined in your project:

```groovy
apply from: 'https://raw.githubusercontent.com/chrisdoyle/gradle-fury/master/gradle/android-support.gradle'

```

ProTip: apply the `apply` line BEFORE the `android { ]` block. This will allow you to override any default values or
 values defined within your gradle.properties file. 

See the `build.gradle` files defined in the
[hello-world-aar](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-aar), 
[hello-world-apk](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-apk),
[hello-world-apk-overrides](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-apk-overrides) 
example projects for reference.

Please note that no similar inclusions are required for standard Java subprojects, as illustrated in
the [hello-world-lib](https://github.com/chrisdoyle/gradle-fury/tree/master/hello-world-lib) example
Java project.


#### 4. Publish multi-flavored Android artifacts with pretty POM files!

##### Install Artifacts to a Local `.m2` Repository

```bash
$ gradle clean build publishToMavenLocal
```

*OR*

```bash
$ gradle clean build install
```

The Gradle pipeline has been hacked such that the `install` task invokes the `publishToMavenLocal`
task, along with a friendly "warning".  Why?  Because the `install` task does not work with Android
projects.  See also the
[Impetus and Rage](https://github.com/chrisdoyle/gradle-fury/tree/develop#impetus-and-rage) section
below.

Also, please note that at the time this project was published the
[Gradle documentation for the really swell 'maven-plugin'](https://docs.gradle.org/current/userguide/publishing_maven.html)
indicated that its Maven publishing support was still in incubation, and that:

> Eventually this new publishing support will replace publishing via the Upload task.

Don't hold your breath.  Apparently "incubation" is perpetual with Gradle.

##### Publish Artifacts to a Remote Repository

```bash
$ gradle clean build publish
```

*OR*

```bash
$ gradle clean build uploadArchives
```

Similar to the `install` task, the Gradle pipeline has been "hacked" such that the `uploadArchives`
task simply invokes the `publish` task, along with a friendly "warning".  So technically, you should
be able to use `uploadArchives` in place of the `publish` task.

##### Bonus: Javadoc and Source jars

Gradle Fury supports build profiles (similar to how Maven does things). Here's the profiles that exist today and how to use them.

To generate Javadocs (Android projects included)
```bash
$ gradle install -Pprofile=javadoc
```

To generate Sources (Android projects included)
```bash
$ gradle install -Pprofile=sources
```

To generate Javadocs and Sources (Android projects included)
```bash
$ gradle install -Pprofile=sources,javadoc
```


*"Well then. It's hacking time." --Kung Fury*

## Publishing to maven central with GPG signatures

Not tested yet, but there's a specific procedure in place. It's tempting to slam then all
together into a single command, but unfortunately it won't work. _Why?_ Because gradle.

_Seriously why?_
Well gradle simply won't let us do what needs to be done. It's lifecycle taskgraph thing
is strange and fires in all kinds of strange unintuitive ways. For instance, Android pom
generation is out of our hands. We can alter it but not control when it's written to disk.
That's a problem if you're trying to sign it. Unfortunately, we couldn't find a way to get
the hooks in place at the right time in the cycle to both sign and not get cleaned and still
be present in the right locations for the publish steps.

```bash
$ ./gradlew clean
$ ./gradlew install -Pprofile=sources,javadoc
$ ./gradlew publish -Pprofile=sources,javadoc,sign
```

## Publishing to Nexus like repos without GPG signatures

```bash
$ ./gradlew clean publish -Pprofile=sources,javadoc,sign
```

Acknowledgements
----------------

I absolutely want to extend a very sincere thanks to [Chris Banes](https://github.com/chrisbanes),
whose work served as the foundation and inspiration for this work.  I would not be anywhere had it
not been for his outstanding efforts to overcome several of the glaring deficiencies within Gradle's
"support" of basic development lifecycle operations -- you know, like simple artifact publishing.

+ [gradle-mvn-push](https://github.com/chrisbanes/gradle-mvn-push)
+ [Pushing AARs to Maven Central](http://chris.banes.me/2013/08/27/pushing-aars-to-maven-central/)

I have quoted it before, and here I am quoting it again:

*"If I have seen further than others, it is by standing upon the shoulders of giants." --Isaac Newton*


Impetus and Rage
----------------

Don't get me started.

+ [Please enjoy the music](http://chrisdoyle.com/2016/01/25/please-enjoy-the-music/)
+ [How can I upload multiple flavors/artifacts with different POM-files using mavenDeployer?](https://discuss.gradle.org/t/how-can-i-upload-multiple-flavors-artifacts-with-different-pom-files-using-mavendeployer/5887)
+ [Gradle Maven plugin “install” task does not work with Android library project](http://stackoverflow.com/questions/18559932/gradle-maven-plugin-install-task-does-not-work-with-android-library-project)
+ [Gradle plugin does not propagate debug/release to dependencies](https://code.google.com/p/android/issues/detail?id=52962)
+ [samskivert: Gradle Difficulties](http://samskivert.com/blog/2015/03/gradle-difficutles/)
+ [It's not yet possible to upload artifact signatures...is not currently on our priority list.](https://discuss.gradle.org/t/how-to-publish-artifacts-signatures-asc-files-using-maven-publish-plugin/7422)
+ [Why can't our builds have the same levels of complexity and non-deterministic failures as our front end code does?](https://recordnotfound.com/gradle-js-danveloper-131869)

*"Knock...kles" --Kung Fury*


License
-------

Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).
