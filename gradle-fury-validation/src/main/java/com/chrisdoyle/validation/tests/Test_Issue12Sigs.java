package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by alex on 9/2/16.
 */
public class Test_Issue12Sigs {

    @Test
    public void verifyGpgSignatures() throws Exception{

        for (int i=0; i < Main.allSignedArtifacts.length; i++){

            File f= new File(Main.allSignedArtifacts[i]);
            Assert.assertTrue(Main.allSignedArtifacts[i] + " does not exist", f.exists());

            f= new File(Main.allSignedArtifacts[i] + ".asc");
            Assert.assertTrue(Main.allSignedArtifacts[i] + " does not exist", f.exists());

            Process p = Runtime.getRuntime().exec(new String[]{Main.gpg, f.getAbsolutePath()});
            int exitcode =p.waitFor();
            p.destroy();
            Assert.assertEquals("Signature validation failed for " + f.getAbsolutePath(), 0, exitcode);
        }

    }
}
