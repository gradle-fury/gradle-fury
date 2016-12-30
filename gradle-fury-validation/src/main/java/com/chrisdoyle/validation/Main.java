package com.chrisdoyle.validation;

import com.chrisdoyle.validation.tests.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Command line junit test runner for Gradle-Fury validation
 *
 * borrowed from Apache jUDDI's TCK Test Runner
 * @author alex
 */
public class Main {

    /**
     * used for string replacement
     */
    static final String VERSION = "\\$version";

    /**
     * run these tests after 'gradlew install -Pprofile=sources,javadoc
     */
    static final Class[] normalTests = new Class[]{

            Test_Issue12.class,
            Test_Issue22.class,
            Test_Issues_23_27.class,
            Test_Issue25.class,
            Test_Issue27.class,
            Test_Issue31.class,
            Test_Issue38.class,
            Test_Issue46.class,
            Test_Issue51.class,
            Test_Issue59.class,
            Test_Issue144.class,
            ValidatePoms.class
    };

    /**
     * run these tests after
     * 'gradlew install -Pprofile=sources,javadoc'
     * 'gradlew publishArtifacts -Pprofile=sources,javadoc,sign'
     *
     */
    static final Class[] signatureTests = new Class[]{
            Test_Issue12Sigs.class,
            TestPostPublication.class,
    };

    /**
     * run these tests after
     * 'gradlew install -Pprofile=sources,javadoc'
     * 'gradlew publishArtifacts -Pprofile=sources,javadoc'
     *
     */
    static final Class[] noSignatureTests = new Class[]{
            TestPostPublicationNoSig.class,
    };

    static final Class[] siteTests = new Class[]{
            SiteTests.class,
    };


    /**
     * all artifacts that are expected to be signed, in the gradle build folders
     */

    public final static String[] allSignedArtifacts = new String[]{
            "./hello-world-aar/build/outputs/aar/hello-world-aar-$version-debug.aar" ,
            "./hello-world-aar/build/outputs/aar/hello-world-aar-$version-release.aar" ,
            "./hello-world-aar/build/libs/hello-world-aar-$version-debug-javadoc.jar" ,
            "./hello-world-aar/build/libs/hello-world-aar-$version-debug-sources.jar" ,
            "./hello-world-aar/build/libs/hello-world-aar-$version-release-javadoc.jar" ,
            "./hello-world-aar/build/libs/hello-world-aar-$version-release-sources.jar" ,
            "./hello-world-aar/build/publications/androidArtifacts/pom-default.xml" ,



            "./hello-world-gmaps/build/outputs/apk/hello-world-gmaps-$version-debug.apk" ,
            "./hello-world-gmaps/build/outputs/apk/hello-world-gmaps-$version-release.apk" ,
            "./hello-world-gmaps/build/libs/hello-world-gmaps-$version-debug-javadoc.jar" ,
            "./hello-world-gmaps/build/libs/hello-world-gmaps-$version-debug-sources.jar" ,
            "./hello-world-gmaps/build/libs/hello-world-gmaps-$version-release-javadoc.jar" ,
            "./hello-world-gmaps/build/libs/hello-world-gmaps-$version-release-sources.jar" ,
            "./hello-world-gmaps/build/publications/androidArtifacts/pom-default.xml" ,


            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-barDebug.apk" ,
            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-barRelease.apk" ,
            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-bazDebug.apk" ,
            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-bazRelease.apk" ,
            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-fooDebug.apk" ,
            "./hello-world-apk/build/outputs/apk/hello-world-apk-$version-fooRelease.apk" ,

            "./hello-world-apk/build/libs/hello-world-apk-$version-barDebug-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-barDebug-sources.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-barRelease-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-barRelease-sources.jar" ,

            "./hello-world-apk/build/libs/hello-world-apk-$version-bazDebug-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-bazDebug-sources.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-bazRelease-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-bazRelease-sources.jar" ,

            "./hello-world-apk/build/libs/hello-world-apk-$version-fooDebug-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-fooDebug-sources.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-fooRelease-javadoc.jar" ,
            "./hello-world-apk/build/libs/hello-world-apk-$version-fooRelease-sources.jar" ,
            "./hello-world-apk/build/publications/androidArtifacts/pom-default.xml" ,



            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-barDebug.apk" ,
            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-barRelease.apk" ,
            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-bazDebug.apk" ,
            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-bazRelease.apk" ,
            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-fooDebug.apk" ,
            "./hello-world-apk-overrides/build/outputs/apk/hello-world-apk-overrides-$version-fooRelease.apk" ,

            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-barDebug-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-barDebug-sources.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-barRelease-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-barRelease-sources.jar" ,

            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-bazDebug-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-bazDebug-sources.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-bazRelease-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-bazRelease-sources.jar" ,

            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-fooDebug-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-fooDebug-sources.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-fooRelease-javadoc.jar" ,
            "./hello-world-apk-overrides/build/libs/hello-world-apk-overrides-$version-fooRelease-sources.jar" ,
            "./hello-world-apk-overrides/build/publications/androidArtifacts/pom-default.xml" ,



            "./hello-world-lib/build/libs/hello-world-lib-$version.jar" ,
            "./hello-world-lib/build/libs/hello-world-lib-$version-javadoc.jar" ,
            "./hello-world-lib/build/libs/hello-world-lib-$version-sources.jar" ,
            "./hello-world-lib/build/publications/javaArtifacts/pom-default.xml" ,
            "./gradle-fury-validation/build/libs/gradle-fury-validation-$version.jar" ,
            "./gradle-fury-validation/build/libs/gradle-fury-validation-$version-javadoc.jar" ,
            "./gradle-fury-validation/build/libs/gradle-fury-validation-$version-sources.jar" ,
            "./gradle-fury-validation/build/distributions/gradle-fury-validation-$version.zip" ,
            "./gradle-fury-validation/build/publications/javaArtifacts/pom-default.xml" ,


            "./hello-child/hello-grandchild-lib/build/libs/hello-grandchild-lib-$version.jar" ,
            "./hello-child/hello-grandchild-lib/build/libs/hello-grandchild-lib-$version-javadoc.jar" ,
            "./hello-child/hello-grandchild-lib/build/libs/hello-grandchild-lib-$version-sources.jar" ,
            "./hello-child/hello-grandchild-lib/build/publications/javaArtifacts/pom-default.xml" ,


            "./hello-universe-lib/build/libs/hello-universe-lib-$version.jar" ,
            "./hello-universe-lib/build/libs/hello-universe-lib-$version-javadoc.jar" ,
            "./hello-universe-lib/build/libs/hello-universe-lib-$version-sources.jar" ,
            "./hello-universe-lib/build/publications/javaArtifacts/pom-default.xml" ,


            "./hello-world-war/build/libs/hello-world-war-$version.war" ,
            "./hello-world-war/build/libs/hello-world-war-$version-javadoc.jar" ,
            "./hello-world-war/build/libs/hello-world-war-$version-sources.jar" ,
            "./hello-world-war/build/publications/webApp/pom-default.xml" ,

            "./hello-world-warDistAppJar/build/libs/hello-world-warDistAppJar-$version.war" ,
            "./hello-world-warDistAppJar/build/distributions/hello-world-warDistAppJar-$version.zip" ,
            "./hello-world-warDistAppJar/build/libs/hello-world-warDistAppJar-$version-javadoc.jar" ,
            "./hello-world-warDistAppJar/build/libs/hello-world-warDistAppJar-$version-sources.jar" ,
            "./hello-world-warDistAppJar/build/publications/javaArtifacts/pom-default.xml" ,


            "./hello-world-app/build/libs/hello-world-app-$version.jar" ,
            "./hello-world-app/build/libs/hello-world-app-$version-sources.jar" ,
            "./hello-world-app/build/libs/hello-world-app-$version-javadoc.jar" ,
            "./hello-world-app/build/publications/javaArtifacts/pom-default.xml" ,
            "./hello-world-app/build/distributions/hello-world-app-$version.zip",

            "./hello-world-dist/build/distributions/hello-world-dist-$version.zip" ,
            "./hello-world-dist/build/distributions/pom-default.xml" ,


            "./hello-world-kotlin/build/libs/hello-world-kotlin-$version.jar" ,
            "./hello-world-kotlin/build/libs/hello-world-kotlin-$version-sources.jar" ,
            "./hello-world-kotlin/build/libs/hello-world-kotlin-$version-javadoc.jar" ,
            "./hello-world-kotlin/build/publications/javaArtifacts/pom-default.xml" ,


    };

    /**
     * all artifacts that are expected to be published to maven local
     */
    public final static String[] allArtifacts = new String[]{
            "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug.aar",
            "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version-debug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version.pom",

            //gradhell is also published locally, but shouldn't be, ignoring for now

            "~/.m2/repository/com/chrisdoyle/hello-grandchild-lib/$version/hello-grandchild-lib-$version.jar",
            "~/.m2/repository/com/chrisdoyle/hello-grandchild-lib/$version/hello-grandchild-lib-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-grandchild-lib/$version/hello-grandchild-lib-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-grandchild-lib/$version/hello-grandchild-lib-$version.pom",


            "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.pom",

            "~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version.jar",
            "~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version.pom",
            //broke for local publishing
            //"~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version.zip",

            "~/.m2/repository/com/chrisdoyle/hello-universe-lib/$version/hello-universe-lib-$version.jar",
            "~/.m2/repository/com/chrisdoyle/hello-universe-lib/$version/hello-universe-lib-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-universe-lib/$version/hello-universe-lib-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-universe-lib/$version/hello-universe-lib-$version.pom",

            //TODO there should be a hello-world-dist too but it's broken for local publishing

            "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version.war",
            "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version.pom",

            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-barDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-bazDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version-fooDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom",


            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-barDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-bazDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug.apk",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version-fooDebug-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version.pom",

            "~/.m2/repository/com/chrisdoyle/hello-world-kotlin/$version/hello-world-kotlin-$version.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-kotlin/$version/hello-world-kotlin-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-kotlin/$version/hello-world-kotlin-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-kotlin/$version/hello-world-kotlin-$version.pom",


            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version.war",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version.zip",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version-sources.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version-javadoc.jar",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version.pom",

    };

    /**
     * all POMs
     */
    public final static String[] allPoms = new String[]{
            "~/.m2/repository/com/chrisdoyle/hello-world-aar/$version/hello-world-aar-$version.pom",
            "~/.m2/repository/com/chrisdoyle/gradle-fury-validation/$version/gradle-fury-validation-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-lib/$version/hello-world-lib-$version.pom",
            //broken with local publishing
            // "~/.m2/repository/com/chrisdoyle/hello-world-dist/$version/hello-world-dist-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-universe-lib/$version/hello-universe-lib-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-grandchild-lib/$version/hello-grandchild-lib-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk/$version/hello-world-apk-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-apk-overrides/$version/hello-world-apk-overrides-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-war/$version/hello-world-war-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-app/$version/hello-world-app-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-dist/$version/hello-world-dist-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-kotlin/$version/hello-world-kotlin-$version.pom",
            "~/.m2/repository/com/chrisdoyle/hello-world-warDistAppJar/$version/hello-world-warDistAppJar-$version.pom",

    };

    /**
     * from gradle.properties, pom.version
     */
    public static String version;

    /**
     * raw access to everything in gradle.properties
     */
    public static Properties gradleProperties = new Properties();
    /**
     * the folder you're running this jar from. it should be the $rootDir of gradle-fury
     */
    public static String cwdDir;
    /**
     * some location to gpg, loaded from gradle.properties
     */
    public static String gpg = "/usr/bin/gpg";

    /**
     * main entry point
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //init
        File cwd = new File(".");
        System.out.println("============ Gradle Fury Validation ============");
        System.out.println("CWD is " + cwd.getAbsolutePath());
        cwdDir = cwd.getAbsolutePath();
        init(cwd);

        System.out.println("using " + gpg );
        System.out.println("using " + version );

        //this part does some basic string replacements for home dir, versioning etc
        String homeDir = System.getProperty("user.home");
        homeDir=homeDir.replace("\\","/");  //stupid windows!
        System.out.println("Homedir is " + homeDir);
        for (int i=0; i < allArtifacts.length; i++){
            allArtifacts[i] = allArtifacts[i].replaceAll(VERSION, version);
            allArtifacts[i] = allArtifacts[i].replaceAll( "~", homeDir);
        }

        for (int i=0; i < allPoms.length; i++){
            allPoms[i] = allPoms[i].replaceAll(VERSION, version);
            allPoms[i] = allPoms[i].replaceAll("~", homeDir);
        }

        for (int i=0; i < allSignedArtifacts.length; i++){
            allSignedArtifacts[i] = allSignedArtifacts[i].replaceAll(VERSION, version);
            allSignedArtifacts[i]=allSignedArtifacts[i].replaceFirst("\\,", cwdDir);
        }


        Class[] classesToRun=null;

        //cli stuff
        // create Options object
        Options options = new Options();

        options.addOption("publishWithSig", false, "Also run tests against nexus with gpg signatures");
        options.addOption("publishWithNoSig", false, "Also run tests against nexus without signatures");
        options.addOption("withSite", false, "Also run tests for the site");
        options.addOption("help", false, "Help");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        if (cmd.hasOption("help")){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "gradle-fury-validation", options );
            return;
        }

        if (cmd.hasOption("publishWithSig")){
            classesToRun = new Class[normalTests.length + signatureTests.length];
            int index=0;
            for (int i=0; i < normalTests.length; i++){
                classesToRun[index] = normalTests[i];
                index++;
            }
            for (int i=0; i < signatureTests.length; i++){
                classesToRun[index] = signatureTests[i];
                index++;
            }

        } else if (cmd.hasOption("publishWithNoSig")){
            classesToRun = new Class[normalTests.length + noSignatureTests.length];
            int index=0;
            for (int i=0; i < normalTests.length; i++){
                classesToRun[index] = normalTests[i];
                index++;
            }
            for (int i=0; i < noSignatureTests.length; i++){
                classesToRun[index] = noSignatureTests[i];
                index++;
            }
        } else if (cmd.hasOption("withSite")){
            classesToRun = new Class[normalTests.length + siteTests.length];
            int index=0;
            for (int i=0; i < normalTests.length; i++){
                classesToRun[index] = normalTests[i];
                index++;
            }
            for (int i=0; i < siteTests.length; i++){
                classesToRun[index] = siteTests[i];
                index++;
            }
        }else {
            classesToRun = normalTests;
        }


        //execute the unit tests

        Result result = null;
        JUnitCore junit = new JUnitCore();


        result = junit.run(classesToRun);

        //String filename = "tck-results-" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + ".txt";
        //FileWriter fw = new FileWriter(filename);

        PrintStream bw = System.out;
        bw.println("Technical Conformance Kit (TCK) Test Results generated " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        bw.println("____________________________________________");
        bw.println("Summary");
        bw.println("Failed Test Cases: " + result.getFailureCount());
        bw.println("Skipped Test Cases: " + result.getIgnoreCount());
        bw.println("Ran Test Cases: " + result.getRunCount());
        bw.println("Time: " + result.getRunTime());
        bw.println("____________________________________________");
        bw.println("Tests Ran");
        
        for (int i = 0; i < normalTests.length; i++) {
            bw.println(normalTests[i].getCanonicalName());
        }
        bw.println("____________________________________________");
        bw.println("Failed Test cases");
        bw.println("____________________________________________");
        
        for (int i = 0; i < result.getFailures().size(); i++) {
            try {
                try {
                    bw.println(result.getFailures().get(i).getTestHeader());
                } catch (Exception ex) {
                    bw.println(ex.getMessage());
                }
                
                try {
                    bw.println(result.getFailures().get(i).getDescription().getClassName());
                }
                catch (Exception ex) {
                    bw.println(ex.getMessage());
                }

                
                try {
                    bw.println(result.getFailures().get(i).getDescription().getMethodName() == null ? "null method!" : result.getFailures().get(i).getDescription().getMethodName());
                } catch (Exception ex) {
                    bw.println(ex.getMessage());
                }
                try{
                    System.out.println("[FAIL] " + result.getFailures().get(i).getDescription().getClassName()+":" + result.getFailures().get(i).getDescription().getMethodName());
                }catch (Exception ex){}
                
                try {
                    bw.println(result.getFailures().get(i).getMessage() == null ? "no message" : result.getFailures().get(i).getMessage());
                } catch (Exception ex) {
                    bw.println(ex.getMessage());
                }
                try {
                    bw.println(result.getFailures().get(i).getTrace());
                } catch (Exception ex) {
                    bw.println(ex.getMessage());
                }
                
                bw.println("____________________________________________");
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("____________________________________________");
        System.out.println("Summary");
        System.out.println("Failed Test Cases: " + result.getFailureCount());
        System.out.println("Skipped Test Cases: " + result.getIgnoreCount());
        System.out.println("Ran Test Cases: " + result.getRunCount());
        System.out.println("Time: " + result.getRunTime() + "ms which is " +
                org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS(result.getRunTime()));
        System.out.println("-------------------------------------");
        
        junit = null;
        System.out.println("Exit code: " + result.getFailureCount());
        System.exit(result.getFailureCount());

    }

    /**
     * loads the gradle.properties file
     * @param cwd
     * @throws Exception
     */
    static void init(File cwd) throws Exception {
        File prop = new File(cwd.getAbsolutePath() + File.separator + "gradle.properties");
        if (!prop.exists()) {
            throw new Exception("can't find gradle.properties");
        }
        Properties p = new Properties();
        p.load(new FileInputStream(prop));

        prop = new File(cwd.getAbsolutePath() + File.separator + "local.properties");
        if (prop.exists()){
            p.load(new FileInputStream(prop));
        }
        if (p.containsKey("GPG_PATH"))
            gpg = p.getProperty("GPG_PATH");
        version = p.getProperty("pom.version");
        gradleProperties=p;


    }

}
