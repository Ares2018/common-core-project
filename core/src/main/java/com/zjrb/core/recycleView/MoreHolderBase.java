package com.zjrb.core.recycleView;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zjrb.core.R;
import com.zjrb.core.recycleView.adapter.BaseLoadAdapter;
import com.zjrb.core.utils.UIUtils;


/**
 * 加载更多Holder
 *
 * @author a_liYa
 * @date 2016-3-1 下午6:22:34
 */
public class MoreHolderBase extends BaseLvViewHolder<Integer> implements
        OnClickListener {
    /**
     * 跟服务器加载数据：表示有更多的数据
     */
    public static final int HAS_MORE = 1;
    /**
     * 表示没有更多的数据
     */
    public static final int NO_MORE = 2;
    /**
     * 表示跟服务器交互失败
     */
    public static final int ERROR = 3;

    private RelativeLayout mRlMoreLoading;
    private RelativeLayout mRlMoreError;

    public BaseLoadAdapter mAdapter;

    public MoreHolderBase(ViewGroup parent, boolean hasMore, BaseLoadAdapter adapter) {
        super(parent);
        // 设置数据
        setData(hasMore ? HAS_MORE : NO_MORE);
        this.mAdapter = adapter;
    }

    @Override
    public void refreshView() {
        Integer data = getData();
        mRlMoreLoading.setVisibility(data == HAS_MORE ? View.VISIBLE
                : View.GONE);
        mRlMoreError.setVisibility(data == ERROR ? View.VISIBLE : View.GONE);
    }

    @Override
    public View initView(ViewGroup parent) {
        View view = UIUtils.inflate(R.layout.module_core_item_lv_loading_more, parent, false);
        // 加载更多
        mRlMoreLoading = (RelativeLayout) view
                .findViewById(R.id.rl_more_loading);
        // 加载失败
        mRlMoreError = (RelativeLayout) view.findViewById(R.id.rl_more_error);
        // 重新加载
        mRlMoreError.setOnClickListener(this);
        return view;
    }

    @Override
    public View getRootView() {
        // 判断当前是否有更多的数据
        if (getData() == HAS_MORE) {
            loadMore();
        }

        return super.getRootView();
    }

    /**
     * 加载更多的数据
     */
    public void loadMore() {
        mAdapter.loadMore();
    }

    @Override
    public void onClick(View arg0) {

        setData(MoreHolderBase.HAS_MORE);

        loadMore();
    }

}
