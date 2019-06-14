package com.test.jnihelloworld;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by 80004024 on 2019/5/24.
 */

public class MyGlSurfaceView  extends GLSurfaceView implements LifecycleObserver{

    private MyGlRender mMyGlRender;
    public MyGlSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mMyGlRender = new MyGlRender();
        setRenderer(mMyGlRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


}
