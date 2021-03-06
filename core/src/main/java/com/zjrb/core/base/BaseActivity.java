package com.zjrb.core.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.R;
import com.zjrb.core.permission.IPermissionOperate;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.AppManager;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.DensityHelper;

/**
 * Activity基类<br/>
 * <p>
 * 必备初始化放这里
 *
 * @author a_liYa
 * @date 2016-4-19 上午6:56:51
 */
public abstract class BaseActivity extends LifecycleActivity implements IPermissionOperate {

    private static final String BIG_SCREEN_CHANNEL = "big";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!onSetupTheme()) {
//            ThemeMode.fitActivityTheme(this);
        }
        // 设置夜间模式 inflater factor
        UiModeManager.setInflaterFactor(getLayoutInflater());
        super.onCreate(savedInstanceState);
        if (!onSetupScreenOrientation()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        }
        AppManager.get().addActivity(this);
//        mAnalytics = new Analytics.AnalyticsBuilder(this, "A0010", "A0010").build();
    }

    /**
     * 设置屏幕方向
     *
     * @return true:表示不走代码设置
     */
    public boolean onSetupScreenOrientation() {
        return false;
    }

    /**
     * 设置主题 回调方法
     *
     * @return true：Base里面不用处理
     */
    protected boolean onSetupTheme() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getIntent().getExtras() != null)
            outState.putAll(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        try {
            // android.support.v4.app.FragmentManagerImpl.checkStateLoss() 崩溃问题
            super.onBackPressed();
        } catch (Exception e) {
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        AppManager.get().removeActivity(this);
//        mAnalytics.sendWithDuration();
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (isUseSystemConfig()) {
            super.attachBaseContext(newBase);
        } else {
            final Resources res = newBase.getResources();
            final Configuration config = res.getConfiguration();
            config.densityDpi = DensityHelper.matchTheoryDpi();
            final Context newContext = newBase.createConfigurationContext(config);
            super.attachBaseContext(newContext);
        }
    }

    /**
     * 设置状态栏背景半透明暗色
     */
    public void setTranslucenceStatusBarBg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        }
    }

    /**
     * 取消状态栏背景半透明暗色
     */
    public void cancelTranslucenceStatusBarBg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 是否使用系统的Config
     *
     * @return true 跟随系统
     */
    protected boolean isUseSystemConfig() {
        //如果是大屏渠道直接适配大屏
        if (TextUtils.equals(AppUtils.getChannelName(), BIG_SCREEN_CHANNEL)) {
            return false;
        }

        // 大屏设备信息
        if (TextUtils.equals(Build.MODEL, "DS830")
                && TextUtils.equals(Build.BOARD, "exdroid")
                && TextUtils.equals(Build.BRAND, "Allwinner")
                && TextUtils.equals(Build.MANUFACTURER, "Allwinner")
                && TextUtils.equals(Build.ID, "KTU84Q")
                && TextUtils.equals(Build.DEVICE, "octopus-perf")) {
            return false;
        }
        return true;
    }

    /**
     * 获取Activity本身<br/>
     * 在匿名内部类中代替 - 类名.this的使用
     *
     * @return
     */
    public Activity getActivity() {
        return this;
    }

    /**
     * 重新封装findViewById(),返回类型不需要强行转换
     *
     * @param id resId
     * @return View
     */
    public <T extends View> T findById(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.get().onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
    }

    @TargetApi(23)
    @Override
    public void exeRequestPermissions(@NonNull String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @TargetApi(23)
    @Override
    public boolean exeShouldShowRequestPermissionRationale(@NonNull String permission) {
        return shouldShowRequestPermissionRationale(permission);
    }

}
