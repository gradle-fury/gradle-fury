package com.chrisdoyle.helloworld;

import org.junit.Test;

/**
 * Created by alex on 9/23/16.
 */

public class MessageTest {
    @Test
    public void santityTest(){

    }

    @Test(expected = Exception.class)
    public void insantityTest() throws Exception{
        throw new Exception("insane");
    }

}
