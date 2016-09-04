package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Issue #25 - verify overrides are correctly applied
 * Created by alex on 9/2/16.
 */
public class Test_Issue25 {

    @Test
    public void testOverrides() throws Exception {

        String[] search = new String[]{
                "package=\"com.chrisdoyle.alex.wuz.here\"" ,
        "android:versionCode=\"9999\"" ,
        "android:versionName=\"OU812\"" ,
        "android:minSdkVersion=\"16\"" ,
        };

        File f = new File(Main.cwdDir + File.separator + "hello-world-apk-overrides/build/intermediates/manifests/full/bar/debug/AndroidManifest.xml");
        Assert.assertTrue(f.exists());

        String str = FileUtils.readFileToString(f, "utf-8");
        for (int i=0; i < search.length; i++){
            Assert.assertTrue(search[i] + " not found in " + f.getAbsolutePath(),str.contains(search[i]));
        }



    }
}
