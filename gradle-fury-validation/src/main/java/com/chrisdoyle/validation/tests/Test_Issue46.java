package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JAR projects have incorrect scope's, test dependencies are missing, name and description are missing
 *
 * <a href="https://github.com/chrisdoyle/gradle-fury/issues/46">https://github.com/chrisdoyle/gradle-fury/issues/46</a>
 * Issue #46 - verify jar poms have dependencies, names, descriptions, etc declared
 * Created by alex on 9/2/16.
 */
public class Test_Issue46 {

    /**
     * this test is commented out because gradle is inconsistent with provided/compile only dependencies
     * it's only supported with certain versions and on certain versions of the android plugin
     * @throws Exception
     */
    @Ignore
    @Test
    public void pomScopeDependency() throws Exception{
        //name, description etc, are now covered under {@link com.chrisdoyle.validation.tests.Test_Issues_23_27#checkPomNameDescriptionUrlLicenseEtcAreDefined}



        String[] search = new String[]{
                //compile
                "<dependency>\\s*<groupId>org.apache.commons</groupId>\\s*<artifactId>commons-lang3</artifactId>\\s*<scope>compile</scope>",
                //test
                "<dependency>\\s*<groupId>junit</groupId>\\s*<artifactId>junit</artifactId>\\s*<scope>test</scope>" ,
                //provided
                "<dependency>\\s*<groupId>commons-io</groupId>\\s*<artifactId>commons-io</artifactId>\\s*<scope>provided</scope>"

        };


        for (int i = 0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-lib/")) {
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
