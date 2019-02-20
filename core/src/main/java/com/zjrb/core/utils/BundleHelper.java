package com.zjrb.core.utils;

import android.os.Bundle;

import java.io.Serializable;

/**
 * BundleHelper
 *
 * @author a_liYa
 * @date 2016/10/9 15:04.
 */
public class BundleHelper {

    public static <T> Bundle creatBundle(String key, T value) {
        Bundle bundle = new Bundle();

        return put(bundle, key, value);
    }

    /**
     * 设置数据
     *
     * @param key   key 关键字
     * @param value 值
     * @param <T>   泛型可为：int、float、boolean、String、long、byte、Serializable
     * @return 返回本身
     */
    public static <T> Bundle put(Bundle bundle, String key, T value) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);

        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);

        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);

        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);

        } else if (value instanceof String) {
            bundle.putString(key, (String) value);

        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);

        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        }

        return bundle;
    }

}
