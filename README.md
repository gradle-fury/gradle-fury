# gradle-fury

*"Before I could pull the trigger, I was hit by lightning and bitten by a cobra. I blacked out, and
saw images of ancient Shaolin temples and monks mastering the art of kung-fu. There was an ancient
prophecy about a new form of kung-fu so powerful, only one man can master it: The Chosen One. When I
woke up, I saw the kung-fu master running towards me. I could feel my body mutate, into some sort of
kung-fu freak of nature." --[Kung Fury](https://www.youtube.com/embed/bS5P_LAqiVg)*


Purpose
-------

An alternative Gradle helper to publish multi-flavored Android artifacts to local and remote Maven
repositories with pretty POM files using simple property configuration. It also provides a wide
variety of things that Maven provides out of the box, in gradle, such as encrypted credentials and
the maven site plugin.

Why use Gradle-Fury
-------------------

Great question, with a sea of many maven publisher helpers (this alone should raise a red flag that
something is wrong) what stands gradle-fury apart? Check out our [Why would you want to use Gradle Fury? wiki page](https://github.com/gradle-fury/gradle-fury/wiki) for the most update to date set of features which is always growing.

Build Status
------------

Develop: [![Build Status](https://travis-ci.org/gradle-fury/gradle-fury.svg?branch=develop)](https://travis-ci.org/gradle-fury/gradle-fury)
Master: [![Build Status](https://travis-ci.org/gradle-fury/gradle-fury.svg?branch=master)](https://travis-ci.org/gradle-fury/gradle-fury)

Requirements
------------
Requirements for using Gradle-Fury

* JDK 7 or JDK 8 (depending on what gradle version and android plugin you're using)
* For Android support, gradle android plugin v1.3.0 or higher, we test using a variety of configurations. See the [Travis build matrix](https://github.com/gradle-fury/gradle-fury/blob/develop/.travis.yml)
* For digital signature support, GPG must be installed on your computer. We test with gnugpg.

Tested configurations for gradle and the android gradle build tools. 


| Gradle | Android Build Tools | JDK | Test Result 
| ------ | ------------------- | --- | -----------
| 3.2.1  | 2.3.0-alpha2        | 8   | OK
| 3.1    | 2.2.2               | 8   | OK
| 3.1    | 2.2.1               | 8   | OK
| 3.1    | 2.2.0               | 8   | OK 
| 3.0    | 2.2.0               | 8   | OK 
| 2.14.1 | 2.1.3               | 7,8 | OK 
| 2.10   | 2.1.2               | 7,8 | OK 
| 2.14   | 2.1.0               | 7,8 | OK 
| 2.14   | 2.0.0               | 7,8 | FAIL - Fails to generate poms for android projects, maven install local and publish to nexus 
| 2.2.1  | 1.5.0               | 7,8 | FAIL - Fails to generate poms for android projects, maven install local and publish to nexus 
| 2.2.1  | 1.3.1               | 7,8 | OK* 
| 2.2.1  | 1.3.0               | 7,8 | FAIL - Fails to generate poms for android projects, maven install local and publish to nexus 


So as long as you're not using one of those 3 versions of the android plugin, you're good to go.

* When using gradle 2.2.1 with the `application` or `distribution` and it's a Java project, there are
some problems with hooking in the install task with distZip (the thing that makes the distribution).
The fix is to include distZip with your command. 

| Gradle             | Build Command
| ------             | ------------------- 
| version <= 2.2.1   | gradlew clean distZip install 
| version > 2.2.1    | gradlew clean install

Again, distZip this only applies if you're using gradle 2.2.1 AND you have a project module that uses
the (distribution OR application plugin) AND it's a Java project. I tried to tie it in, but I can't 
make gold from lead - ao

Usage
-----

Please refer to the dummy "Hello World" subprojects along with the provided root project
`build.gradle` and `gradle.properties` files included with this project as a general usage guide:

* Example Android Library: [hello-world-aar](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-aar)
* Example Android Application: [hello-world-apk](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-apk)
* Example Java Library: [hello-world-lib](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-lib)
* Root [build.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/build.gradle)
* Root [gradle.properties](https://github.com/gradle-fury/gradle-fury/blob/master/gradle.properties)


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

The included root [gradle.properties](https://github.com/gradle-fury/gradle-fury/blob/master/gradle.properties)
file may be used as a template.


#### 2. Modify the `allprojects` closure in your root project `build.gradle`

The required modifications consist of defining the default `project.group` and `project.version`
properties, and applying the
[maven-support.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/gradle/maven-support.gradle)
script in the `allprojects` closure, which will subsequently apply the configuration to all subprojects.

Please note that `project.group` and `project.version` *must* be defined before including
[maven-support.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/gradle/maven-support.gradle)
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

    apply from: 'https://raw.githubusercontent.com/gradle-fury/gradle-fury/master/gradle/maven-support.gradle'

		.
		.
		.

}
```

The included root [build.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/build.gradle)
file may be used as a reference for usage.


#### 3. Apply the [android-support.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/gradle/android-support.gradle) script to each Android subproject [gradle.properties](https://github.com/gradle-fury/gradle-fury/blob/master/gradle.properties)

Add the following to the
[build.gradle](https://github.com/gradle-fury/gradle-fury/blob/master/build.gradle) of each Android
(Library or Application) subproject defined in your project:

```groovy
apply from: 'https://raw.githubusercontent.com/gradle-fury/gradle-fury/master/gradle/android-support.gradle'

```

ProTip: apply the `apply` line BEFORE the `android { ]` block. This will allow you to override any default values or
 values defined within your gradle.properties file. 

See the `build.gradle` files defined in the
[hello-world-aar](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-aar), 
[hello-world-apk](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-apk),
[hello-world-apk-overrides](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-apk-overrides) 
example projects for reference.

Please note that no similar inclusions are required for standard Java subprojects, as illustrated in
the [hello-world-lib](https://github.com/gradle-fury/gradle-fury/tree/master/hello-world-lib) example
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
[Impetus and Rage](https://github.com/gradle-fury/gradle-fury/tree/develop#impetus-and-rage) section
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

We're also not using the built in "publish" or "uploadArchive" task here because it does not 
publish pom signatures correctly. It also uploads an additional variant of android libraries and APKs
for no apparent reason and we can't find a way to sign those before uploading.

```bash
$ ./gradlew clean
$ ./gradlew install -Pprofile=sources,javadoc
$ ./gradlew publishArtifacts -Pprofile=sources,javadoc
```

## Publishing to Nexus like repos without GPG signatures

```bash
$ ./gradlew clean publishArtifacts -Pprofile=sources,javadoc -Pgpg.skip
```



## Encryption

We searched high and low for an encrypted password capable maven helper and we couldn't find one.

So thanks for that. I have no problem storing my password in clear text nor sending my private gpg keys
to jcenter/bintray.

Wait a sec, that makes no sense at all.

### Make a master key
```bash
./gradlew generateMasterKey
```

The key is stored in `USER_HOME/.gradle/fury.properties`. Nuke that if there's an unexpected knock on the door.

### Encrypt a password
```bash
./gradlew encryptPassword -PstoreField=xyz
```

Where 'xyz' is one of the supported password Java properties keys that we use. They are (subject to change)
 - NEXUS_PASSWORD
 - signing.passPhrase
 - GPG_PASSPHRASE
 - android.signingConfigs.release.storePassword
 - android.signingConfigs.release.keyPassword

The encrypted password is then merged and written to 'local.properties' and picked up later in the build process.

You can also run the following...

#### Encrypt without user interaction
```bash
./gradlew encryptPassword -PstoreField=NEXUS_PASSWORD -Ppassword=secret
```

#### Just GPG sign the artifacts
```bash
./gradlew signArtifacts -Pprofile=javadoc,sources
```

#### DIY approach
```bash
./gradlew encryptPassword
```

Then you'll have to manually edit local.properties to insert your cipher text.


## Gradle to Maven Scope Mappings

|Gradle qualifier   	| Maven Scope  	
|---	                |---	
| compile               | compile   
| releaseCompile        | compile
| debugCompile          | NOT MAPPED*
| runtime               | runtime
| testCompile  	        | test   	
| androidTestCompile    | test
| provided              | provided

* Items marked as not mapped are not referenced in the pom. Pom's are 
generally used for releases, such as, items specific to a scope, such as
'debugCompile' aren't useful since it won't be in the release version. 


## Quality Plugin

Runs findbugs, checkstyle, and pmd on all your projects (android and java).
Based off the work [done here](https://github.com/MasonLiuChn/AndroidCodeQuality).

To apply to your project, apply this file under 'allprojects' then copy the files from gradle-fury/config and place it in your project

```
allprojects {
        apply from 'https://raw.githubusercontent.com/gradle-fury/gradle-fury/master/gradle/quality.gradle'
  }
```

then execute with `gradelw build`

Since the checks can add a lot of time to your build, you probably want to make it optional...

```
 allprojects {
     if (project.hasProperty('profile') && project.profile.split(',').contains("ci")) {
        apply from 'https://raw.githubusercontent.com/gradle-fury/gradle-fury/master/gradle/quality.gradle'     }
  }
```

Then execute with `gradelw build -Pprofile=ci`


## Maven Site Plugin

### [Here's what it looks like](http://gradle-fury.github.io/gradle-fury)

Well it's not exactly the Maven Site Plugin, but it's our version of it. It's pretty darn close.
It uses theming inspired by [Apache Fluido](http://maven.apache.org/skins/maven-fluido-skin/) and is loosely based
on the work Paul Speed-2 @ filament did over [here](https://sourceforge.net/p/filament/code/HEAD/tree/trunk/site/build.gradle).

Instead of the APT based sites, we opted for a simpler solution, a singular page template 
which is then merged with generated content and your content using Markdown and the
[Common Mark](https://github.com/atlassian/commonmark-java) renderer. We also
looked at [Pegdown](https://github.com/sirthias/pegdown) but ran into performance issues.
[Asciidoc](https://github.com/asciidoctor/asciidoctorj)/docbook
 
### Make the site

Before making the site, you should run all your tests and any gradle tasks that generate reports.
If they were't created first, they won't be included with site generation.

Also before making, you need to make some directories and files.

```bash
mkdir src
mkdir src/site/
```

Next, you'll want to grab all the files from gradle-fury's [src/site/](https://github.com/gradle-fury/gradle-fury/tree/develop/src/site).
Put those files in your `src/site/` folder.

Next, create or edit `index.md`. This will be come your site's `index.html`, the first page people will look at it. 

**Note:** if you're looking for gradle-fury's index.md page, you won't find it. Instead, we copy this readme.md to the right place 
and rename it.

Most Github flavored markdown tags are supported. You can also any files put in `src/site/` will
be included in the site. Any html or pdf files in the root `src/site/` will be auto-linked on the left hand site
navigation menu.

Finally, put this in your root build.gradle file. Do not place it within `allprojects`.

```groovy

apply from 'https://raw.githubusercontent.com/gradle-fury/gradle-fury/master/gradle/site.gradle'

```

We also took some liberties from the maven-site-plugin. Many of the pages it generates can be reduced to just a link.
For instance, the location of the source (do we really need an entire web page that only shows the link to the source code?). 
Same goes for CI, distro management and issue tracking.

```bash
./gradlew install -Pprofile=javadoc,sources
# if needed, for distribution projects
./gradlew distZip -Pprofile=javadoc,sources
# if needed, for on device android test and reports
./gradlew cC
# if needed, for findbugs
./gradlew check
# if needed, for jacoco
./gradlew jacocoTestReport

# finally, generate the site
./gradlew site
```

The output goes to `rootDir/build/site/` and can be overridden with `ext.buildWebsiteDir = rootDir.absolutePath + "/docs/"`

In the future, we may add some extra tasks for zipping up the site, deploying to an ftp or something else.

### What is generated by the site plugin?

Here's a quick list

 - Project Summary, all of the artifacts of your project, derived from the project itself and gradle.properties
 - Project Team, from gradle.properties
 - Links to the source, issue tracker, CI and distribution pages
 - All javadocs are included and linked
 - Project repository list, from gradle.properties
 - Dependency report - for all the artifacts of your project, derived from the project itself
 - Project reports - for all artifacts of the project, any and all things from the build/reports folder is included and linked (see the quality plugin). Includes test reports, connectedCheck, Findbugs, PMD, etc
 - Converts and themes all your .md and .asciidoc files from the root/src/site and links them in

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
