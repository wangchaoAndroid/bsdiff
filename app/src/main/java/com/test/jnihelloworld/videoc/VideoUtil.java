package com.test.jnihelloworld.videoc;

/**
 * Created by 80004024 on 2019/6/13.
 */

public class VideoUtil {







    static {
        System.loadLibrary("videoc");
        System.loadLibrary("avcodec");
        System.loadLibrary("avcodec-58");
        System.loadLibrary("avformat");
        System.loadLibrary("avformat-58");
        System.loadLibrary("avutil");
        System.loadLibrary("avutil-56");
        System.loadLibrary("swscale-5");
        System.loadLibrary("swscale");
    }


    public static native int getVideoDecoderFile(String sourcePath);
}
