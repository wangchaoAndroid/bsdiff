package com.test.jnihelloworld;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OpenGLES20Activity extends Activity {

    private MyGlSurfaceView mMyGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyGlSurfaceView = new MyGlSurfaceView(this);
        setContentView(mMyGlSurfaceView);
    }
}
