package com.test.jnihelloworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.jnihelloworld.videoc.VideoUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String TAG  = MainActivity.class.getSimpleName();

    // Used to load the 'native-lib' library on application startup.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        Log.e("1111","" + Build.VERSION.SDK_INT );


        String uri = Environment.getDownloadCacheDirectory()  + File.separator + "test" + File.separator + "abcd.mp4";
        File file = new File(uri);
        Log.e(TAG, "onCreate: " + file.exists() );
        int videoDecoderFile = VideoUtil.getVideoDecoderFile(uri);
        Log.e(TAG, "onCreate: " + videoDecoderFile );


    }


}
