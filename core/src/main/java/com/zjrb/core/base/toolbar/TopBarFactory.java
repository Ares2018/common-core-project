package com.zjrb.core.base.toolbar;

import android.app.Activity;
import android.view.ViewGroup;


/**
 * TopBarFactory(生产TopBarHolder的工厂)
 *
 * @author a_liYa
 * @date 16/8/14 11:01.
 */
public class TopBarFactory {

    /**
     * @param view
     * @param act
     * @param title
     * @return 通用顶部栏
     */
    public static DefaultTopBarHolder createDefault(ViewGroup view, Activity act, String title) {
        return new DefaultTopBarHolder(view, act, title);
    }
}
