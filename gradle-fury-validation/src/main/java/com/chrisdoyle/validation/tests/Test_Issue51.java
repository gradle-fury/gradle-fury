package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * issue 51 pom's with URL
 * Created by alex on 9/3/16.
 */
public class Test_Issue51 {

    @Test
    public void testPomProjectUrls() throws Exception {

        String[] search = new String[]{
                "<repositories>",
                "<repository>\\s*<id>BintrayJCenter</id>\\s*<name>BintrayJCenter</name>\\s*<url>https://jcenter.bintray.com/</url>\\s*</repository>",
                "<repository>\\s*<id>MavenRepo</id>\\s*<name>MavenRepo</name>\\s*<url>https://repo1.maven.org/maven2/</url>\\s*</repository>",
                "</repositories>",
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
