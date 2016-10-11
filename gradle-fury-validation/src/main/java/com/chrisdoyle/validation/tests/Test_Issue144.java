package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXB;

/**
 * Created by alex on 10/10/16.
 */
public class Test_Issue144 {

    @Test
    public void validatePom() throws Exception {
        for (int i=0; i < Main.allPoms.length; i++) {
            Model project = JAXB.unmarshal(new File(Main.allPoms[i]), Model.class);
            Assert.assertNotNull(project);
        }
    }

    @Test
    public void testExclusions() throws Exception{
        for (int i=0; i < Main.allPoms.length; i++) {
            if (Main.allPoms[i].contains("hello-world-apk")) {
                Model project = JAXB.unmarshal(new File(Main.allPoms[i]), Model.class);
                Assert.assertNotNull(project);
                for (int k=0; k < project.getDependencies().getDependency().size(); k++){
                    Dependency dependency = project.getDependencies().getDependency().get(k);
                    if (dependency.getGroupId().equals("ch.acra") &&
                            dependency.getArtifactId().equals("acra")){
                        Assert.assertNotNull("no exclusions were present", dependency.getExclusions());
                        Assert.assertEquals("no exclusions were present", 1,dependency.getExclusions().getExclusion().size());
                    }

                }
            }
        }
    }
}
