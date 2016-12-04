package com.chrisdoyle.helloworld;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * <img alt="Class diagram around Marker class" width="686" height="413" src='./doc-files/marker-classes.png' >
 */
public final class BogusUtil {

    public static void setMessage(@NonNull final TextView view, @NonNull final Message message) {
        view.setText(message.getMessage());
    }

}
