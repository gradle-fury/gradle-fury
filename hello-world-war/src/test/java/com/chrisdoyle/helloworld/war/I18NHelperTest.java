package com.chrisdoyle.helloworld.war;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by alex on 8/25/16.
 */

public class I18NHelperTest {
    @Test
    public void runTest() throws Exception{
        Assert.assertNotNull(I18NHelper.getHello("en"));
    }
}
