package com.chrisdoyle.helloworld;

import android.support.annotation.NonNull;
import android.widget.TextView;

public final class BogusUtil {

    public static void setMessage(@NonNull final TextView view, @NonNull final Message message) {
        view.setText(message.getMessage());
    }

}
