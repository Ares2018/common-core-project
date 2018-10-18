package com.zjrb.core.base.toolbar;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.R;

/**
 * 白色样式 ： 左返回 + 中标题 + 右收藏(可隐藏) + 右分享(可隐藏)
 *
 * @author a_liYa
 * @date 2017/7/25 17:15.
 */
public class TopBarWhiteStyle extends TopBarViewHolder {
    TextView tvTitle;
    ImageView ivShare;
    ImageView ivCollect;
    LinearLayout rightLayout;

    public TopBarWhiteStyle(Activity activity) {
        this(activity, false, false);
    }

    public TopBarWhiteStyle(Activity activity, boolean shareVisible) {
        this(activity, shareVisible, false);
    }

    public TopBarWhiteStyle(Activity activity, boolean shareVisible, boolean collectVisible) {
        super((ViewGroup) activity.getWindow().getDecorView(), activity);
        initView();
        if (!shareVisible && !collectVisible) {
            rightLayout.setVisibility(View.GONE);
        } else {
            rightLayout.setVisibility(View.VISIBLE);
            ivShare.setVisibility(shareVisible ? View.VISIBLE : View.GONE);
            ivCollect.setVisibility(collectVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_top_bar_title);
        setBackOnClickListener(R.id.iv_top_bar_back);
        ivShare = findViewById(R.id.iv_top_share);
        ivCollect = findViewById(R.id.iv_top_collect);
        rightLayout = findViewById(R.id.right_layout);
    }

    public void setTopBarText(String title) {
        tvTitle.setText(title);
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public ImageView getShareView() {
        return ivShare;
    }

    public ImageView getCollectView() {
        return ivCollect;
    }

    public LinearLayout getRightLayout() {
        return rightLayout;
    }

    public void setRightVisible(boolean visible) {
        rightLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setShareVisible(boolean visible) {
        ivShare.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setCollectVisible(boolean visible) {
        ivCollect.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.module_core_layout_top_bar_white_style;
    }

}
