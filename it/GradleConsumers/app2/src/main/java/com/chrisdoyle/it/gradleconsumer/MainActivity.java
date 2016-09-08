package com.chrisdoyle.it.gradleconsumer;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.chrisdoyle.helloworld.Message;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.helloworld);
        com.chrisdoyle.helloworld.BogusUtil.setMessage(tv, new Message("hi"));
    }
}
