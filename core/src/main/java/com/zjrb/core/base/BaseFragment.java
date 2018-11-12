package com.zjrb.core.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.R;
import com.zjrb.core.permission.IPermissionOperate;
import com.zjrb.core.permission.PermissionManager;

import java.lang.reflect.Field;

/**
 * Fragment基类
 *
 * @author a_liYa
 * @date 2016-3-27 下午8:43:53
 */
public abstract class BaseFragment extends Fragment implements IPermissionOperate {

    /**
     * Fragment所依附的父容器
     */
    protected ViewGroup container;

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            view.setTag(R.id.tag_fragment, this);
            if (view.getParent() instanceof ViewGroup) {
                this.container = (ViewGroup) view.getParent();
            }
        }
    }

    /**
     * 返回Fragment自身<br/>
     * 匿名内部类中使用
     *
     * @return this
     */
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isAdded()) {
            try {
                Fragment fragment = getChildFragmentManager().findFragmentByTag("video.manager");
                if (fragment != null) {
                    fragment.onHiddenChanged(hidden);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getView() != null) {
            getView().setTag(R.id.tag_fragment, null);
        }
    }

    /**
     * findViewById
     */
    public <T extends View> T findViewById(int id) {
        if (getView() != null)
            return (T) getView().findViewById(id);
        return null;
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang
    // -illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            // throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            // throw new RuntimeException(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.get().onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
    }

    @Override
    public void exeRequestPermissions(@NonNull String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @Override
    public boolean exeShouldShowRequestPermissionRationale(@NonNull String permission) {
        return shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    /**
     * 查找View所依附的Fragment
     *
     * @param v view
     * @return Fragment
     */
    public static Fragment findAttachFragmentByView(View v) {
        while (v != null) {
            Object tag = v.getTag(R.id.tag_fragment);
            if (tag instanceof Fragment) {
                return (Fragment) tag;
            }
            if (v.getParent() instanceof View) {
                v = (View) v.getParent();
            } else {
                v = null;
            }
        }
        return null;
    }

}
