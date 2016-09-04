package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * # these are all the files we're testing for the existence of
 * Created by alex on 9/2/16.
 */
public class Test_Issue12  {

    @Test
    public void verifyAllArtifactsExists() throws Exception{

        for (int i=0; i  < Main.allArtifacts.length; i++){
            Assert.assertTrue(Main.allArtifacts[i] + " doesn't exist", new File(Main.allArtifacts[i]).exists());
        }
    }
}
