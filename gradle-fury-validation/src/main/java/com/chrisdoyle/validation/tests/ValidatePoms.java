package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.io.FileUtils;
import org.apache.maven.pom._4_0.Model;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import javax.xml.bind.JAXB;

/**
 * Created by alex on 10/12/16.
 */
public class ValidatePoms {

    @Test
    public void validateAllPoms() throws Exception{
        for (int i = 0; i < Main.allPoms.length; i++) {

                File f = new File(Main.allPoms[i]);

                JAXB.unmarshal(f, Model.class);

        }

    }
}
