
package com.zjrb.core.swipeback.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.swipeback.SwipeBackLayout;
import com.zjrb.core.swipeback.Utils;
import com.zjrb.core.utils.AppManager;


/**
 * 边缘手势 - 侧滑退出Activity
 *
 * @author a_liYa
 * @date 2016/9/30 18:38.
 */
public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    private boolean mNeverSwipeBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!mNeverSwipeBack) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!mNeverSwipeBack) {
            mHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    /**
     * 设置是否支持边缘手势
     *
     * @param enable
     */
    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (!mNeverSwipeBack) {
            getSwipeBackLayout().setEnableGesture(enable);
        }
    }

    /**
     * 设置永不支持边缘手势
     */
    public void setNeverSwipeBack() {
        mNeverSwipeBack = true;
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewGroup contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        View view = contentView.getChildAt(0);
        if (view != null && view.getScaleX() < 1f) {
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
    }

    @Override
    public void finish() {
        super.finish();
        Activity activity = AppManager.get().preActivity(this);
        if (activity != null) {
            ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            View view = contentView.getChildAt(0);
            if (view != null) {
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        }
    }

    /**
     * 设置边缘滑动退出方式
     *
     * @param edgeFlags SwipeBackLayout.EDGE_LEFT 左边缘滑动退出
     *                  SwipeBackLayout.EDGE_RIGHT 右边缘滑动退出
     *                  SwipeBackLayout.EDGE_BOTTOM 下边缘滑动退出
     *                  SwipeBackLayout.EDGE_ALL 左、右、下边缘滑动退出
     */
    public void setEdgeTrackingEnabled(int edgeFlags) {
        getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlags);
    }

}
