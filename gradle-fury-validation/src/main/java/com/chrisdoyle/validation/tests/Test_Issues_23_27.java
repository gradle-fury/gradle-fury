package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Issue #23 and 27 - verify poms has dependencies declared, name and descriptions
 * Created by alex on 9/2/16.
 */
public class Test_Issues_23_27 {
    static String[] elementsThatShouldBeThere = new String[]{
            "<groupId>",
            "<artifactId>",
            "<name>",
            "<description>",
            "<version>",
            "<packaging>",
            "<inceptionYear>",
            "<licenses>",
            "<developers>",
            "<issueManagement>",
            "<scm>",
            "<repositories>",
            "<dependencies>",
            "<url>"

    };

    @Test
    public void checkPomNameDescriptionUrlLicenseEtcAreDefined() throws Exception{

        for (int i=0; i < Main.allPoms.length; i++){
            File f = new File(Main.allPoms[i]);

            String str = FileUtils.readFileToString(f, "utf-8");
            for (int k=0; k < elementsThatShouldBeThere.length; k++){
                if (f.getAbsolutePath().contains("hello-world-dist") &&
                        "<repositories>".equalsIgnoreCase(elementsThatShouldBeThere[k])){
                    //do nothing, skip this
                } else if (f.getAbsolutePath().contains("hello-world-dist") &&
                        "<dependencies>".equalsIgnoreCase(elementsThatShouldBeThere[k])){
                    //do nothing, skip this
                } else
                    Assert.assertTrue(elementsThatShouldBeThere[k] + " not found in " + f.getAbsolutePath() + " contents is as follows" + str,str.contains(elementsThatShouldBeThere[k]));
            }

        }


    }


    @Test
    public void checkAndroidDependenciesArePresent() throws Exception{
        String[] aarDeps =new String[]{
                "com.android.support",
                "support-annotations" ,
                "hello-world-lib"

        } ;

        String[] apkDeps =new String[]{
                "appcompat-v7",
                "cardview-v7" ,
                "design",
                "recyclerview-v7",
                "support-v4",
                "commons-io",
                "commons-math3",
                "commons-codec",
                "osmdroid-android",
                "junit",
                "com.squareup.leakcanary",
                "support-annotations",
                "com.android.support.test",
                "hello-world-aar"

        } ;
        String[] libDeps = new String[]{
                "commons-lang3",
                "commons-logging"
        };
        for (int i=0; i < Main.allPoms.length; i++){
            if (Main.allPoms[i].contains("hello-world-aar")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k=0; k < aarDeps.length; k++){
                    Assert.assertTrue(aarDeps[k] + " not found in " + f.getAbsolutePath() + " contents is as follows" + str,str.contains(aarDeps[k]));
                }
            }
            if (Main.allPoms[i].contains("hello-world-apk-" + Main.version)){
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k=0; k < apkDeps.length; k++){
                    Assert.assertTrue(apkDeps[k] + " not found in " + f.getAbsolutePath() + " contents is as follows" + str,str.contains(apkDeps[k]));
                }
            }
            if (Main.allPoms[i].contains("hello-world-lib")){
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k=0; k < libDeps.length; k++){
                    Assert.assertTrue(libDeps[k] + " not found in " + f.getAbsolutePath() + " contents is as follows" + str,str.contains(libDeps[k]));
                }
            }

        }

    }
}
