package com.zjrb.core.swipeback.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.zjrb.core.R;
import com.zjrb.core.swipeback.SwipeBackLayout;
import com.zjrb.core.swipeback.Utils;
import com.zjrb.core.swipeback.ViewDragHelper;
import com.zjrb.core.utils.AppManager;


/**
 * @author Yrom
 */
public class SwipeBackActivityHelper {
    private static final String TAG = "swipe";
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(
                R.layout.module_core_swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Log.e(TAG, "into--[onScrollStateChange] state:" + state + " scrollPercent:" + scrollPercent);
                int index = AppManager.get().getAllActivity().indexOf(mActivity);
                if (index > 0 && state== ViewDragHelper.STATE_DRAGGING) {
                    index--;
                    Activity activity = AppManager.get().getAllActivity().get(index);
                    View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                    view.setScaleX(scrollPercent+0.7f);
                    view.setScaleY(scrollPercent+0.7f);
                    Log.e(TAG, activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
                Log.e(TAG, "into--[onEdgeTouch] edgeFlag:" + edgeFlag);

                int index = AppManager.get().getAllActivity().indexOf(mActivity);
                if (index > 0) {
                    index--;
                    Activity activity = AppManager.get().getAllActivity().get(index);
                    activity.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                    view.setScaleX(0.7f);
                    view.setScaleY(0.7f);
                    Log.e(TAG, activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onScrollOverThreshold() {
                Log.e(TAG, "into--[onScrollOverThreshold]");

            }

            @Override
            public void onScrollFinish() {
                int index = AppManager.get().getAllActivity().indexOf(mActivity);
                if (index > 0) {
                    index--;
                    Activity activity = AppManager.get().getAllActivity().get(index);
                    View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    Log.e(TAG, activity.getClass().getSimpleName());
                }
            }
        });
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
}
