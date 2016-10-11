package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * verify dependency scope are correct
 * Created by alex on 9/2/16.
 */
public class Test_Issue38 {

    @Test
    public void testPomDependencyScope() throws Exception {

        String[] search = new String[]{
        //#provided#
        "<dependency>\\s*<groupId>com.chrisdoyle</groupId>\\s*<artifactId>hello-universe-lib</artifactId>\\s*<scope>provided</scope>",
        //#test
        "<dependency>\\s*<groupId>junit</groupId>\\s*<artifactId>junit</artifactId>\\s*<scope>test</scope>" ,
        //compile
        //"<dependency>\\s*<groupId>org.osmdroid</groupId>\\s*<artifactId>osmdroid-android</artifactId>\\s*<scope>compile</scope>"
        
        };


        for (int i = 0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-apk/")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k = 0; k < search.length; k++) {
                    Pattern p = Pattern.compile(search[k]);
                    Matcher matcher = p.matcher(str);
                    Assert.assertTrue(search[k] + " not found in " + f.getAbsolutePath(), matcher.find());
                }
            }
        }

    }

    @Test
    public void testPomDependencyScopeAAR() throws Exception {

        String[] search = new String[]{
                //compile and type
                "<dependency>\\s*<groupId>org.osmdroid</groupId>\\s*<artifactId>osmdroid-android</artifactId>\\s*<scope>compile</scope>\\s*<version>5.4.1</version>\\s*<type>aar</type>"

        };


        for (int i = 0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-aar/")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k = 0; k < search.length; k++) {
                    Pattern p = Pattern.compile(search[k]);
                    Matcher matcher = p.matcher(str);
                    Assert.assertTrue(search[k] + " not found in " + f.getAbsolutePath(), matcher.find());
                }
            }
        }

    }
}
