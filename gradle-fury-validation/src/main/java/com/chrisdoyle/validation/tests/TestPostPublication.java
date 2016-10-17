package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 9/4/16.
 */
public class TestPostPublication {

    public void checkNexusPublicationsWithSignatures(boolean requireSignatures) throws Exception{
//do we have URL's defined?
        Assume.assumeTrue(!StringUtils.isEmpty(Main.gradleProperties.getProperty("RELEASE_REPOSITORY_URL")) &&
                !StringUtils.isEmpty(Main.gradleProperties.getProperty("SNAPSHOT_REPOSITORY_URL")));

        //also assume that we have actually signed the content

        boolean wasAndroidSigned = Main.gradleProperties.containsKey("android.signingConfigs.release.storeFile") &&
                !StringUtils.isEmpty(Main.gradleProperties.getProperty("android.signingConfigs.release.storeFile"));


        List<String> expectedModules = getModules();    //from file system
        expectedModules.remove("hello-gradhell");
        //TODO recursive iteration

        List<String> remoteModulesUrls;
        String postfix= Main.gradleProperties.getProperty("pom.groupId").replace(".","/")+"/";
        String baseUrl ="";
        if (Main.version.toLowerCase().endsWith("snapshot")) {
            baseUrl = Main.gradleProperties.getProperty("SNAPSHOT_REPOSITORY_URL")+postfix;
        } else {
            baseUrl = Main.gradleProperties.getProperty("RELEASE_REPOSITORY_URL")+postfix;
        }
        remoteModulesUrls = getDirectoryListing(baseUrl);
        Assert.assertEquals("the local module count (non-recursive) doesn't match what was found in nexus at " + baseUrl, remoteModulesUrls.size(), expectedModules.size());
        List<String> remoteModules = normalize(remoteModulesUrls,baseUrl);
        Assert.assertTrue("lists were not equal", compare(remoteModules, expectedModules));

        Assert.assertFalse("Remote nexus should not have the hello-gradhell artifacts", remoteModules.contains("hello-gradhell"));
        //ok so we've uploaded the right number of modules as expected...

        for (int i=0; i < remoteModulesUrls.size(); i++){
            List<String> artifacts;

            artifacts=getDirectoryListing(remoteModulesUrls.get(i) + Main.version + "/");

            //loop through artifacts, everything that ends with .asc should have somehting that exists without it .asc
            //loop through artifacts, everything that ends does not with .asc should have somehting that exists with it .asc
            //ignore metadata crap and parent dir
            for (int k=0; k < artifacts.size(); k++){
                if (!artifacts.get(k).endsWith(".asc") &&
                        !artifacts.get(k).endsWith(".md5") &&
                        !artifacts.get(k).endsWith(".sha1")){

                    Assert.assertTrue(artifacts.get(k) + " no md5 hash", artifacts.contains(artifacts.get(k) + ".md5"));
                    Assert.assertTrue(artifacts.get(k) + " no sha1 hash", artifacts.contains(artifacts.get(k) + ".sha1"));
                    if (requireSignatures) {
                        Assert.assertTrue(artifacts.get(k) + " wasn't signed", artifacts.contains(artifacts.get(k) + ".asc"));
                        Assert.assertTrue(artifacts.get(k) + " sig no hash", artifacts.contains(artifacts.get(k) + ".asc.md5"));
                        Assert.assertTrue(artifacts.get(k) + " sig no md5", artifacts.contains(artifacts.get(k) + ".asc.sha1"));
                    }
                }
            }
        }

        /*
        if (wasAndroidSigned) {
            verifyReleaseArtifactsWereUploaded();
        }
        */
    }
    @Test
    public void checkNexusPublications() throws Exception {
        checkNexusPublicationsWithSignatures(true);

    }

    private List<String> normalize(List<String> remoteModulesUrls, String baseUrl) {
        List<String> ret = new ArrayList<>();
        for (int i=0; i < remoteModulesUrls.size(); i++){
            String t = remoteModulesUrls.get(i);
            t = t.replace(baseUrl, "");
            t=t.replace("/","");
            ret.add(t);
        }
        return ret;
    }

    /**
     * return true if the both lists contain the same content (order doesn't matter)
     * @param remoteModules
     * @param expectedModules
     * @return
     */
    private boolean compare(List<String> remoteModules, List<String> expectedModules) {
        Collections.sort(remoteModules);
        Collections.sort(expectedModules);

        return remoteModules.equals(expectedModules);
    }

    private List<String> getDirectoryListing(String nexusUrl) throws Exception {
        List<String> ret = new ArrayList<>();

        Document doc = Jsoup.connect(nexusUrl).get();
        for (Element file : doc.select("td a")) {
            System.out.println(file.attr("href"));
            if (!file.attr("href").equalsIgnoreCase("../"))
                ret.add(file.attr("href"));

        }

        return ret;
    }

    public static List<String> getModules() {
        List<String> ret = new ArrayList<>();
        File[] files = new File(Main.cwdDir).listFiles();
        Assert.assertNotNull(files);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                if (!files[i].getName().equalsIgnoreCase("gradle") &&
                        !files[i].getName().startsWith(".") &&
                        !files[i].getName().equalsIgnoreCase("build") &&
                        !files[i].getName().equalsIgnoreCase("hello-child") &&
                        !files[i].getName().equalsIgnoreCase("..") &&
                        !files[i].getName().equalsIgnoreCase(".")) {
                    if (new File(files[i].getAbsolutePath() + File.separator + "build.gradle").exists()) {
                        System.out.println("module found: " + files[i].getName());
                        ret.add(files[i].getName());
                    }
                }
            }
        }
        ret.add("hello-grandchild-lib");
        return ret;
    }


}
