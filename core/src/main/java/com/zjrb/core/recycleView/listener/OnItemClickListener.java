package com.zjrb.core.recycleView.listener;

import android.view.View;

/**
 * OnItemClickListener
 *
 * @author a_liYa
 * @date 16/9/4 09:36.
 */
public interface OnItemClickListener {

    /**
     * item 点击回调
     *
     * @param itemView {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}
     * @param position .
     */
    void onItemClick(View itemView, int position);
}
