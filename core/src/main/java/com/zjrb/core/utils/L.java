package com.zjrb.core.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * Log日志类
 *
 * @author a_liYa
 * @date 2016-3-30 下午11:44:55
 */
public class L {

    private static boolean isDebug = true;
    private static boolean debuggable = true;

    private static String TAG = L.class.getSimpleName();

    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 初始化必须调用，否则日志无效
     *
     * @param context .
     */
    public static void init(Context context) {
        if (context == null) return;
        try {
            L.debuggable = (context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES)
                    .applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            L.debuggable = false;
        }
        // app name
        TAG = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static void setIsDebug(boolean isDebug) {
        L.isDebug = isDebug;
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug && debuggable)
            Log.i(TAG, msg + "");
    }

    public static void d(String msg) {
        if (isDebug && debuggable)
            Log.d(TAG, msg + "");
    }

    public static void e(String msg) {
        if (isDebug && debuggable)
            Log.e(TAG, msg + "");
    }

    public static void v(String msg) {
        if (isDebug && debuggable)
            Log.v(TAG, msg + "");
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug && debuggable)
            Log.i(tag, msg + "");
    }

    public static void d(String tag, String msg) {
        if (isDebug && debuggable)
            Log.d(tag, msg + "");
    }

    public static void e(String tag, String msg) {
        if (isDebug && debuggable)
            Log.e(tag, msg + "");
    }

    public static void v(String tag, String msg) {
        if (isDebug && debuggable)
            Log.v(tag, msg + "");
    }

}
