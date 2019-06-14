package com.test.jnihelloworld;

import android.graphics.Bitmap;

/**
 * Created by 80004024 on 2019/5/15.
 */

public class CompressUtil {

    static {
        System.loadLibrary("native-lib");
    }

    public native static int compressBitmap(Bitmap bitmap, int quality, byte[] fileNameBytes,
                                            boolean optimize);
}

