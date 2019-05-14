package com.test.jnihelloworld;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 80004024 on 2019/5/7.
 */

public class BsPatchUtils {

    //int bsdiff(const uint8_t* old, int64_t oldsize, const uint8_t* new, int64_t newsize, struct bsdiff_stream* stream)
//    public static native int bspatch(String oldPath,String newPath,String patchPatch);

    static {
        System.loadLibrary("bsdiffcore");
        System.loadLibrary("BsPatch");
    }


    public  native int bsdiff(String oldPath,String newPath,String patchPatch);


    public  void bsdiff(Context context)  {
        boolean b = copyAssetAndWrite(context, "oldfile.txt");
        boolean b1 = copyAssetAndWrite(context, "newfile.txt");
        File oldFile = new File(context.getCacheDir(),"oldfile.txt");
        File newFile = new File(context.getCacheDir(),"newfile.txt");
        String patchPath = new File(context.getCacheDir(),"patch.txt").getAbsolutePath();
        int bsdiff = bsdiff(oldFile.getAbsolutePath(), newFile.getAbsolutePath(), patchPath);
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream stream = null;
        try {
            fileInputStream = new FileInputStream(patchPath);
            stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) != -1){
                stream.write(buffer);
            }

            Log.e("TEST",stream.toString() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  native int bspatch(String o,String n,String p);


    public  int  bspatch(Context context){

//        boolean b = copyAssetAndWrite(context, "oldfile.txt");
//        Log.e("bspathc","oldFile---"+  b);
//
//        boolean patchB = copyAssetAndWrite(context, "patch.txt");
//        Log.e("bspathc","patchB---"+  patchB);

        File oldFile = new File(context.getCacheDir(),"oldfile.txt");
        File patchFile = new File(context.getCacheDir(),"patch.txt");
        String newPath = new File(context.getCacheDir(),"newandpatch.txt").getAbsolutePath();
        File newFile = new File(newPath);
        Log.e("bspathc","oldFile exists"+ oldFile.exists());
        if(oldFile.exists()){
            int bsdiff = bspatch(oldFile.getAbsolutePath(), newPath, patchFile.getAbsolutePath());
            Log.e("bspathc","result"+ bsdiff);
        }

        if(newFile.exists()){
            //installApkO(context,newPath);
            Log.e("bspathc","newFile exists"+ newFile.length());
        }
        return 1;
    }


    /**
     * 将asset文件写入缓存
     */
    private static boolean copyAssetAndWrite(Context context,String fileName){
        try {
            File cacheDir=context.getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return false;
                }
            }else {
                if (outFile.length()>10){//表示已经写入一次
                    return true;
                }
            }
            InputStream is=context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 兼容8.0安装位置来源的权限
     */
    private static void installApkO(Context context, String downloadApkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否有安装位置来源的权限
            boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                installApk(context, downloadApkPath);
            } else {
                Log.e("bspathc","安装应用需要打开安装未知来源应用权限，请去设置中开启权限");
//                new CakeResolveDialog(context, "", new CakeResolveDialog.OnOkListener() {
//                    @Override
//                    public void onOkClick() {
//                        Uri packageUri = Uri.parse("package:"+ AppUtils.getAppPackageName());
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageUri);
//                    }
//                }).show();
            }
        } else {
            installApk(context, downloadApkPath);
        }
    }

    public static  void installApk(Context context,String downloadApk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }
}
