package com.test.jnihelloworld;

import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.util.logging.Handler;

/**
 * Created by 80004024 on 2019/5/14.
 */

public class ToastHook {
    public static final String TAG = "ToastHook";
    public  static void hook(Toast toast){
        Class clazz = Toast.class;
        try {

            Field localLOGV = clazz.getDeclaredField("localLOGV");
            localLOGV.setAccessible(true);
            boolean b = (boolean) localLOGV.get(toast);
            Log.e(TAG,"localLOGV" + b);

            localLOGV.set(toast,true);
            boolean b2 = (boolean) localLOGV.get(toast);
           /* //反射 TN
            Field mTN = clazz.getDeclaredField("mTN");
            mTN.setAccessible(true);
            //获取 TN 对象
            Object tn = mTN.get(toast);
            Field mShow = tn.getClass().getDeclaredField("mShow");
            mShow.setAccessible(true);
            Runnable runnable = (Runnable) mShow.get(tn);
            mShow.set(tn,new ToastShowRunnable(runnable));*/

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static class  ToastShowRunnable implements Runnable{

        private Runnable mRunnable;

        public ToastShowRunnable(Runnable runnable) {
            mRunnable = runnable;
        }

        @Override
        public void run() {
            try {
                mRunnable.run();
            }catch (Exception e){

            }
        }
    }
}
