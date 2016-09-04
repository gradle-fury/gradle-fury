package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Issue 31 War file support
 * Created by alex on 9/2/16.
 */
public class Test_Issue31 {

    @Test
    public void testWarPom() throws Exception{

        String[] search = new String[]{
                "<packaging>war</packaging>",
            "<groupId>org.apache.commons</groupId>",
            "<artifactId>hello-world-lib</artifactId>",
        };


        for (int i = 0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-war/")) {
                File f = new File(Main.allPoms[i]);

                String str = FileUtils.readFileToString(f, "utf-8");
                for (int k = 0; k < search.length; k++) {
                    Assert.assertTrue(search[k] + " not found in " + f.getAbsolutePath(), str.contains(search[k]));
                }
            }
        }

    }
}
