package com.zjrb.core.recycleView;

import android.view.View;

/**
 * item点击事件的回调 - 接口
 * 使用方式：让你的ViewHolder实现此接口
 *
 * @author a_liYa
 * @date 2018/1/4 16:30.
 */
public interface ItemClickCallback {

    /**
     * 在 {@link OnItemClickListener#onItemClick(View, int)} 之后被调用
     *
     * @param itemView {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}
     * @param position .
     */
    void onItemClick(View itemView, int position);

}
