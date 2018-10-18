package com.zjrb.core.base.toolbar;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * TopBarFactory(生产TopBarHolder的工厂)
 *
 * @author a_liYa
 * @date 16/8/14 11:01.
 */
public final class TopBarFactory {

    /**
     * @param view
     * @param act
     * @param title
     * @return 通用顶部栏
     */
    public static DefaultTopBarHolder createDefault(ViewGroup view, Activity act, String title) {
        return new DefaultTopBarHolder(view, act, title);
    }

    /**
     * 白色样式 ： 左返回 + 中标题 + 右收藏(可隐藏) + 右分享(可隐藏)
     */
    public static TopBarWhiteStyle createWhiteStyle(ViewGroup view, Activity act) {
        return new TopBarWhiteStyle(act);
    }



}
