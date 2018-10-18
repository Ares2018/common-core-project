package com.zjrb.core.base.toolbar;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.zjrb.core.R;
import com.zjrb.core.ui.widget.CircleImageView;

/**
 * 5.3.3版本通用顶部栏
 *
 * @author wanglinjie
 * @date 2018/4/28 14:33.
 */
public class CommonTopBarHolder extends TopBarViewHolder {
    FrameLayout mContainer;
    TextView tvTitle, tvSubscribe;
    ImageView mIvshare;
    CircleImageView mIvIcon;
    FitWindowsRelativeLayout mRelativeLayout;

    public CommonTopBarHolder(ViewGroup view, Activity activity) {
        super(view, activity);
        mContainer = findViewById(R.id.layout_title_bar);
        tvTitle = findViewById(R.id.tv_top_bar_title);
        mIvshare = findViewById(R.id.iv_top_share);
        mIvIcon = findViewById(R.id.iv_top_subscribe_icon);
        mRelativeLayout = findViewById(R.id.frl_title);
        tvSubscribe = findViewById(R.id.tv_top_bar_subscribe_text);
        setBackOnClickListener(R.id.iv_top_bar_back);
    }

    /**
     * @param title 动态设置title
     */
    public void setTopBarText(String title) {
        tvTitle.setText(title);
    }

    /**
     * @return 获取分享view
     */
    public ImageView getShareView() {
        return mIvshare;
    }


    /**
     * @return 获取标题
     */
    public TextView getTitleView() {
        return tvTitle;
    }

    /**
     * @return 获取中间栏目布局
     */
    public FitWindowsRelativeLayout getFitRelativeLayout() {
        return mRelativeLayout;
    }

    /**
     * @return 获取栏目头像
     */
    public CircleImageView getIvIcon() {
        return mIvIcon;
    }

    /**
     * @return 订阅状态
     */
    public TextView getSubscribe() {
        return tvSubscribe;
    }

    /**
     * @param v
     * @param visible View.VISIBLE/View.GONE
     *                设置控件显示/隐藏
     */
    public void setViewVisible(View v, int visible) {
        v.setVisibility(visible);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.module_core_layout_top_comment_detail;
    }

}
