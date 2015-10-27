package com.android.mytumblr.ui;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * Created by Sunita on 8/22/2015.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static void CopyStream(InputStream is, OutputStream os) {
        Log.d(TAG, "inside CopyStream");
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                //Read byte from input stream
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.d(TAG, "exception in CopyStream !!!");
            ex.printStackTrace();
        }
    }
}