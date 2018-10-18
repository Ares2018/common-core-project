package com.zjrb.core.recycleView;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView的拓展, 结合BaseRecyclerAdapter使用
 *
 * @param <T> 数据的泛型
 * @author a_liYa
 * @date 16/10/19 09:52.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public T mData;

    public BaseRecyclerViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        this(inflate(layoutRes, parent, false));
    }

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setData(T data) {
        this.mData = data;
        bindView();
    }

    public T getData() {
        return mData;
    }

    /**
     * bind data to view
     */
    public abstract void bindView();

    /**
     * Inflate a new view hierarchy from the specified xml resource
     *
     * @param resource     ID for an XML layout
     * @param parent       the parent of
     * @param attachToRoot .
     * @return
     * @see LayoutInflater#inflate(int, ViewGroup, boolean)
     */
    protected static View inflate(@LayoutRes int resource, ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, attachToRoot);
    }
}
