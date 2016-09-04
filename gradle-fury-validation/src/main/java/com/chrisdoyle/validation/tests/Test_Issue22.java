package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Issue 22, pom overrides
 * Created by alex on 9/2/16.
 */
public class Test_Issue22 {

    @Test
    public void testPomOverries() throws Exception{


        String search[] = new String[]{
                "LGPL"
        };

        for (int i = 0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-apk-overrides/")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k = 0; k < search.length; k++) {
                    Assert.assertTrue(search[k] + " not found in " + f.getAbsolutePath(), str.contains(search[k]));
                }
            }
        }

    }
}
