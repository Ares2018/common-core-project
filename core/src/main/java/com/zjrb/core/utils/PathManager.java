package com.zjrb.core.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 路径统一管理类
 *
 * @author a_liYa
 * @date 16/10/25 21:56.
 */
public class PathManager {

    private volatile static WeakReference<PathManager> sWeakInstance;
    /**
     * 保存到本地根文件夹名
     */
    private final String SAVE_ROOT_FOLDER = "24h";
    /**
     * 相机拍照存储目录
     */
    private final String SAVE_PICTURE_PATH = "/picture";
    /**
     * 音频存储目录
     */
    private final String SAVE_AUDIO_PATH = "/audio";
    /**
     * WebView缓存目录
     */
    private final String WEBVIEW_CACHE = "webview";

    private PathManager() {
    }

    // 获取实例
    public static PathManager get() {
        PathManager instance;
        if (sWeakInstance == null || (instance = sWeakInstance.get()) == null) {
            sWeakInstance = new WeakReference<>(instance = obtain());
        }
        return instance;
    }

    private synchronized static PathManager obtain() {
        return new PathManager();
    }


    /**
     * 获取一个拍照图片空文件
     *
     * @return 图片格式的空文件
     */
    public File obtainTakePicFile() {
        if (!isExistExternalStore()) return null;
        File dir = Environment.getExternalStoragePublicDirectory(SAVE_ROOT_FOLDER +
                SAVE_PICTURE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, getRandomName(".jpg"));
    }

    /**
     * 获取一个录音空文件
     *
     * @return 图片格式的空文件
     */
    public File obtainAudioFile() {
        if (!isExistExternalStore()) return null;
        File dir = Environment.getExternalStoragePublicDirectory(SAVE_ROOT_FOLDER +
                SAVE_AUDIO_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, getRandomName(".amr"));
    }

    /**
     * 获取随机的文件名称
     *
     * @param suffix 后缀 eg: .jpg
     * @return 返回名称字符串
     */
    public String getRandomName(String suffix) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffix;
    }

    /**
     * 存储获取根目录
     *
     * @return
     */
    public String getRootDir() {
        if (isExistExternalStore())
            return Environment.getExternalStoragePublicDirectory(SAVE_ROOT_FOLDER)
                    .getAbsolutePath();
        return null;
    }


    /**
     * 是否有外存卡
     *
     * @return true 可用
     */
    public boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取WebView
     * @return
     */
    public String getWebViewCacheDir() {
        return UIUtils.getContext().getFileStreamPath(WEBVIEW_CACHE).getAbsolutePath();
    }


    /*
    Environment.getDataDirectory() =                          /data
    Environment.getDownloadCacheDirectory() =                 /cache
    Environment.getExternalStorageDirectory() =               /storage/emulated/0
    Environment.getExternalStoragePublicDirectory(“test”) = /storage/emulated/0/test
    Environment.getRootDirectory() = /system

    getPackageCodePath()                    /data/app/com.zbjt.zj24h-2/base.apk
    getPackageResourcePath() =              /data/app/com.zbjt.zj24h-2/base.apk

    getCacheDir() =                         /data/user/0/com.zbjt.zj24h/cache
    getDatabasePath("test") =               /data/user/0/com.zbjt.zj24h/databases/test
    getDir("test", Context.MODE_PRIVATE) =  /data/user/0/com.zbjt.zj24h/app_test
    getFilesDir() =                         /data/user/0/com.zbjt.zj24h/files
    getFileStreamPath("test") =             /data/user/0/com.zbjt.zj24h/files/test

    getExternalCacheDir() =                 /storage/emulated/0/Android/data/com.zbjt.zj24h/cache
    getExternalFilesDir("test") =           /storage/emulated/0/Android/data/com.zbjt
    .zj24h/files/test
    getExternalFilesDir(null) =             /storage/emulated/0/Android/data/com.zbjt.zj24h/files
    */
    public void test(Context context) {
        L.e("Environment.getDataDirectory() = " + Environment.getDataDirectory());
        L.e("Environment.getDownloadCacheDirectory() = " + Environment.getDownloadCacheDirectory
                ());
        L.e("Environment.getExternalStorageDirectory() = " + Environment
                .getExternalStorageDirectory());
        L.e("Environment.getExternalStoragePublicDirectory(\"test\") = " + Environment
                .getExternalStoragePublicDirectory("test"));
        L.e("Environment.getRootDirectory() = " + Environment.getRootDirectory());
        L.e("getPackageCodePath() " + context.getPackageCodePath());
        L.e("getPackageResourcePath() = " + context.getPackageResourcePath());
        L.e("getCacheDir() = " + context.getCacheDir());
        L.e("getDatabasePath(\"test\") = " + context.getDatabasePath("test"));
        L.e("getDir(“test”, Context.MODE_PRIVATE) = " + context.getDir("test", Context
                .MODE_PRIVATE));
        L.e("getFilesDir() = " + context.getFilesDir());
        L.e("getFileStreamPath(\"test\") = " + context.getFileStreamPath("test"));

        L.e("getExternalCacheDir() = " + context.getExternalCacheDir());
        L.e("getExternalFilesDir(\"test\") = " + context.getExternalFilesDir("test"));
        L.e("getExternalFilesDir(null) = " + context.getExternalFilesDir(null));
    }

}
