package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * solution for repositories addition to the pom
 * Created by alex on 9/3/16.
 */
public class Test_Issue59 {

    @Test
    public void testPomProjectUrls() throws Exception {

        //update when #59 is merged
        String[] search = new String[]{
                "<url>http://github.com/gradle-fury/gradle-fury</url>"
        };


        for (int i = 0; i < Main.allPoms.length; i++) {
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
