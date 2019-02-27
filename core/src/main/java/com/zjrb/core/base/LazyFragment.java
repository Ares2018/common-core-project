package com.zjrb.core.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 懒加载Fragment
 *
 * @author a_liYa
 * @date 2016-3-27 下午8:53:53
 */
public class LazyFragment extends BaseFragment {

    private boolean isLoaded;

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLazyViewCreated(view, savedInstanceState);
        if (getUserVisibleHint() && !isLoaded) {
            isLoaded = true;
            onLazyLoad();
        }
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoaded && isVisible()) {
            isLoaded = true;
            onLazyLoad();
        }
    }

    /**
     * 因为final修饰 {@link #onViewCreated(View, Bundle)}，实现此方法等于onViewCreated
     */
    public void onLazyViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * 在onViewCreated之后执行
     */
    public void onLazyLoad() {

    }

}
