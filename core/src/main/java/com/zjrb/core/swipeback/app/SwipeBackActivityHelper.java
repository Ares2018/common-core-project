package com.zjrb.core.swipeback.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.R;
import com.zjrb.core.swipeback.SwipeBackLayout;
import com.zjrb.core.swipeback.Utils;
import com.zjrb.core.utils.AppManager;


/**
 * @author Yrom
 */
public class SwipeBackActivityHelper implements SwipeBackLayout.SwipeListener {
    private static final String TAG = "Swipe";
    private static final float ACTIVITY_SCALE = 0.7f;
    private Activity mActivity;
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        View contentView = mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.setBackgroundColor(Color.WHITE);

        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.module_core_swipeback_layout, null);
        mSwipeBackLayout.setScrollThresHold(1f - ACTIVITY_SCALE);
        mSwipeBackLayout.addSwipeListener(this);
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        Activity activity = AppManager.get().preActivity(mActivity);
        if (activity != null) {
            ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            View view = contentView.getChildAt(0);
            if (view != null) {
                float percent = scrollPercent + ACTIVITY_SCALE;
                if (percent >= 1) {
                    percent = 1;
                }
                view.setScaleX(percent);
                view.setScaleY(percent);
            }
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        Utils.convertActivityToTranslucent(mActivity);
        Activity activity = AppManager.get().preActivity(mActivity);
        if (activity != null) {
            ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            View view = contentView.getChildAt(0);
            if (view != null) {
                view.setScaleX(ACTIVITY_SCALE);
                view.setScaleY(ACTIVITY_SCALE);
            }
        }
    }

    @Override
    public void onScrollOverThreshold() {

    }
}
