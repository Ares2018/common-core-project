package com.zjrb.core.recycleView;

import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.uimode.utils.UiModeUtils;
import com.zjrb.core.R;
import com.zjrb.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerAdapter 空页面
 *
 * @author a_liYa
 * @date 2017/9/27 21:06.
 */
public class EmptyPageHolder extends PageItem {

    @BindView(R2.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R2.id.tv_content)
    TextView mTvContent;

    /**
     * 构造方法
     *
     * @param parent a ViewGroup
     * @param args   通过 {@link ArgsBuilder#newBuilder()} 创建
     */
    public EmptyPageHolder(ViewGroup parent, ArgsBuilder args) {
        super(inflate(R.layout.module_core_layout_footer_empty, parent, false));
        ButterKnife.bind(this, itemView);

        bindView(args);

    }

    private void bindView(ArgsBuilder args) {
        if (args == null) return;
        if (args.resId != 0) {
            mIvIcon.setImageResource(args.resId);
        }
        if (args.attrId != 0) {
            UiModeUtils.applyImageSrc(mIvIcon, args.attrId);
        }
        if (!TextUtils.isEmpty(args.content)) {
            mTvContent.setText(args.content);
        }
    }

    /**
     * 参数 - Builder
     *
     * @author a_liYa
     * @date 2017/9/28 上午10:40.
     */
    public final static class ArgsBuilder {

        private int resId; // 图标资源
        private int attrId; // 属性资源

        private String content; // 提示内

        private ArgsBuilder() {
        }

        public static ArgsBuilder newBuilder() {
            return new ArgsBuilder();
        }

        public ArgsBuilder resId(@DrawableRes int resId) {
            this.resId = resId;
            return this;
        }

        public ArgsBuilder attrId(@AttrRes int attrId) {
            this.attrId = attrId;
            return this;
        }

        public ArgsBuilder content(String content) {
            this.content = content;
            return this;
        }

    }

}
