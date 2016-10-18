package com.chrisdoyle.validation.tests;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;


/**
 * Created by alex on 10/15/16.
 */
public class SiteTests {

    String[] modules = new String[]{
            "gradle-fury-validation",
            "hello-grandchild-lib",
            "hello-gradhell",
            "hello-universe-lib",
            "hello-world-app",
            //no reports here "hello-world-dist"
            "hello-world-lib",
            "hello-world-war",
    };

    String[] modulesWithDebugRelease = new String[]{
            "hello-world-aar",
            "hello-world-gmaps"
    };
    String[] modulesWithFlavors = new String[]{
            "hello-world-apk",
            "hello-world-apk-overrides",
    };


    @Test
    public void confirmJavadocsExist() throws Exception {
        for (int i = 0; i < modules.length; i++) {
            Assert.assertTrue("no javadoc at " + modules[i], new File("docs/javadocs/" + modules[i] + "/index.html").exists());
        }
        for (int i = 0; i < modulesWithDebugRelease.length; i++) {
            Assert.assertTrue("no javadoc at " + modulesWithDebugRelease[i], new File("docs/javadocs/" + modulesWithDebugRelease[i] + "/debug/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithDebugRelease[i], new File("docs/javadocs/" + modulesWithDebugRelease[i] + "/release/index.html").exists());
        }
        for (int i = 0; i < modulesWithFlavors.length; i++) {
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/barDebug/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/barRelease/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/bazDebug/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/bazRelease/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/fooDebug/index.html").exists());
            Assert.assertTrue("no javadoc at " + modulesWithFlavors[i], new File("docs/javadocs/" + modulesWithFlavors[i] + "/fooRelease/index.html").exists());
        }
    }


    @Test
    public void confirmCheckStyleReportsExist() throws Exception {
        //if (System.getenv().containsKey("GRADLE_VERSION")){
          //  Assume.assumeFalse("Skip, dependency plugin wasn't until after gradle 2.2.1", System.getenv().get("GRADLE_VERSION").contains("2.2.1"));
        //}

        test("checkstyle", "main.html","android.html");
    }

    @Test
    public void confirmDependenciesReportsExist() throws Exception {
        test("dependencies", "index.html","index.html");
    }

    @Test
    public void confirmFindbugsReportsExist() throws Exception {
        test("findbugs", "main.html","findbugs.html");
    }
    @Test
    public void confirmOWASPReportsExist() throws Exception {
        test("owasp", "dependency-check-report.html","dependency-check-vulnerability.html");
    }

    @Test
    public void confirmJDependReportsExist() throws Exception {
        test("jdepend", "main.html","index.html");
    }

    @Test
    public void confirmJavancssReportsExist() throws Exception {
        test("javancss", "index.html","index.html");
    }

    //jdepend?
    //dex-count

    @Test
    public void confirmPmdReportsExist() throws Exception {
        test("pmd", "main.html","android.html");
    }

    private void test(String title, String lastPart, String alternateName) throws Exception {
        for (int i = 0; i < modules.length; i++) {
            Assert.assertTrue("no " + title + " at " + modules[i],
                    new File("docs/reports/" + modules[i] + "/" + title + "/" + lastPart).exists() ||
                    new File("docs/reports/" + modules[i] + "/" + title + "/" + alternateName).exists());
        }
        for (int i = 0; i < modulesWithDebugRelease.length; i++) {
            Assert.assertTrue("no " + title + " at " + modulesWithDebugRelease[i],
                    new File("docs/reports/" + modulesWithDebugRelease[i] + "/" + title + "/"  + lastPart).exists() ||
                            new File("docs/reports/" + modulesWithDebugRelease[i] + "/" + title + "/" + alternateName).exists());
        }
        for (int i = 0; i < modulesWithFlavors.length; i++) {
            Assert.assertTrue("no " + title + " at " + modulesWithFlavors[i],
                    new File("docs/reports/" + modulesWithFlavors[i] + "/" + title + "/"  + lastPart).exists() ||
                            new File("docs/reports/" + modulesWithFlavors[i] + "/" + title + "/" + alternateName).exists());
        }

    }
}
