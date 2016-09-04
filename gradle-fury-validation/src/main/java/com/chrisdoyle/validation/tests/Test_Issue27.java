package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 *  Issue #27 - verify poms has dependencies declared correctly with wildcard"
 * Created by alex on 9/2/16.
 */
public class Test_Issue27 {

    @Test
    public void testPomWildCards() throws Exception {

        String[] search = new String[]{
                "<version>[1.3,)</version>" ,
        "<version>LATEST</version>" ,
        "<version>[3,)</version>" ,
        "<artifactId>commons-math3</artifactId>" ,
        "<artifactId>commons-codec</artifactId>" ,
        "<version>[1,)</version>" ,
        };
//hello-world-apk/


        for (int i=0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-apk/")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k = 0; k < search.length; k++) {
                    Assert.assertTrue(search[k] + " not found in " + f.getAbsolutePath(), str.contains(search[k]));
                }
            }
        }



    }
}
