package com.chrisdoyle.helloworld.app;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class HelloWorldActivityTest extends ActivityInstrumentationTestCase2<HelloWorldActivity> {

    public HelloWorldActivityTest() {
        super(HelloWorldActivity.class);
    }


    /**
     * This test will check whether calling setCenter() will position the maps so the location is
     * at the center of the screen.
     */
    @UiThreadTest
    public void test_toMapPixels_0_0() {
    }

}