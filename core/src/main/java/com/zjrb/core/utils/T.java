package com.zjrb.core.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Toast工具类
 *
 * @author a_liYa
 * @date 2016-3-30 下午11:46:39
 */
public class T {

    private static WeakReference<Toast> toastWeak;

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow && !TextUtils.isEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 立刻短时间显示Toast
     *
     * @param context 上下文
     * @param message 为空不现实
     */
    public static void showShortNow(Context context, CharSequence message) {
        showNow(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context 上下文
     * @param message 文本
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 立刻短时间显示Toast
     *
     * @param context 上下文
     * @param message 文本
     */
    public static void showShortNow(Context context, int message) {
        showNow(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow && !TextUtils.isEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 立刻长时间显示Toast
     *
     * @param context 上下文
     * @param message 为空不现实
     */
    public static void showLongNow(Context context, CharSequence message) {
        showNow(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * 立刻长时间显示Toast
     *
     * @param context 上下文
     * @param message 文本
     */
    public static void showLongNow(Context context, int message) {
        showNow(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  上下文
     * @param message  为空不显示
     * @param duration 时长类型
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow && !TextUtils.isEmpty(message))
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间 - 立刻显示
     *
     * @param context  上下文
     * @param message  为空不显示
     * @param duration 时长类型
     */
    public static void showNow(Context context, CharSequence message, @Duration int duration) {
        if (isShow) {
            if (TextUtils.isEmpty(message))
                return;
            if (isShow) {
                getToast(context, message, duration).show();
            }
        }
    }

    public static void hideLast() {
        if (toastWeak != null) {
            Toast toast = toastWeak.get();
            if (toast != null) {
                toast.cancel();
            }
            toastWeak = null;
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间 - 立刻显示
     *
     * @param context  上下文
     * @param message  显示文本
     * @param duration 时长类型
     */
    public static void showNow(Context context, int message, @Duration int duration) {
        if (isShow) {
            getToast(context, context.getString(message), duration).show();
        }
    }

    /**
     * 获取Toast
     *
     * @return Toast
     */
    private static Toast getToast(Context context, CharSequence message, int duration) {
        Toast toast;
        if (toastWeak != null) {
            toast = toastWeak.get();
            if (toast != null) {
                toast.setText(message);
                toast.setDuration(duration);
                return toast;
            }
        }
        toast = Toast.makeText(context, message, duration);
        toastWeak = new WeakReference<>(toast);

        return toast;
    }


    /**
     * 注解,限定取值范围
     */
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

}
