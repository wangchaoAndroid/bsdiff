package com.test.jnihelloworld;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;


import org.reactivestreams.Subscriber;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wangheng on 18-10-31.
 */
public class ShotterUtil {

    private final SoftReference<Context> mRefContext;
    private ImageReader mImageReader;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private static final String TAG="SHOTTERUTIL";

    public ShotterUtil(Context context, int reqCode, Intent data) {
        this.mRefContext = new SoftReference<>(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            mMediaProjection = getMediaProjectionManager().getMediaProjection(reqCode, data);

            mImageReader = ImageReader.newInstance(
                    getScreenWidth(),
                    getScreenHeight(),
                    PixelFormat.RGBA_8888,//此处必须和下面 buffer处理一致的格式 ，RGB_565在一些机器上出现兼容问题。
                    1);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                Resources.getSystem().getDisplayMetrics().densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startScreenShot(final OnShotListener onShotListener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            virtualDisplay();

            //这里delay 时间过短容易导致 系统权限弹窗的阴影还没消失就完成了截图。 @see<a href="https://github.com/weizongwei5/AndroidScreenShot_SysApi/issues/4">issues</a>
            Observable.timer(800, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<Long, Image>() {
                        @Override
                        public Image apply(Long aLong) {

                            return mImageReader.acquireLatestImage();
                        }


                    }).observeOn(Schedulers.io())
                    .map(new Function<Image, Bitmap>() {
                        @Override
                        public Bitmap apply(Image image) {
                            int width = image.getWidth();
                            int height = image.getHeight();
                            final Image.Plane[] planes = image.getPlanes();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            //每个像素的间距
                            int pixelStride = planes[0].getPixelStride();
                            //总的间距
                            int rowStride = planes[0].getRowStride();
                            int rowPadding = rowStride - pixelStride * width;
                            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                                    Bitmap.Config.ARGB_8888);//虽然这个色彩比较费内存但是 兼容性更好
                            bitmap.copyPixelsFromBuffer(buffer);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                            image.close();
                            return bitmap;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bitmap>() {

                        @Override
                        public void onError(Throwable e) {
                            if (mVirtualDisplay != null) {
                                mVirtualDisplay.release();
                            }
                            if (onShotListener != null) {
                                onShotListener.onFinish(null);
                            }
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Bitmap bitmap) {

                            if (mVirtualDisplay != null) {
                                mVirtualDisplay.release();
                            }
                            if (onShotListener != null) {
                                onShotListener.onFinish(bitmap);
                            }
                        }
                    });


        }

    }

    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getContext().getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
    }

    private Context getContext() {
        return mRefContext.get();
    }


    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    // a  call back listener
    public interface OnShotListener {
        void onFinish(Bitmap bitmap);
    }
}
