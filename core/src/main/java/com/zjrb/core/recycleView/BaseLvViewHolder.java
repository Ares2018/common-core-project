package com.zjrb.core.recycleView;


import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.R;

/**
 * BaseViewHolder基类(主要用于ListView、GridView)
 *
 * @param <T> 数据的泛型
 * @author a_liYa
 * @date 2016-3-1 下午12:51:16
 */
public abstract class BaseLvViewHolder<T> {

    private View mView;
    public T mData;
    private int position;

    public BaseLvViewHolder(ViewGroup parent) {
        mView = initView(parent);
        mView.setTag(R.id.tag_holder, this);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * 在设置数据之前。是默认的数据
     *
     * @param data
     */
    public void setData(T data) {
        this.mData = data;
        refreshView();
    }

    /**
     * 返回当前的数据
     *
     * @return
     */
    public T getData() {
        return mData;
    }

    /**
     * 刷新界面
     */
    public abstract void refreshView();

    /**
     * 初始化界面
     *
     * @return
     */
    public abstract View initView(ViewGroup parent);

    /**
     * 返回最根的view
     *
     * @return
     */
    public View getRootView() {
        return mView;
    }

}
