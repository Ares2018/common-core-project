package com.zjrb.core.recycleView;

import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.ui.divider.OverlayItemDecoration;

/**
 * 悬浮、吸顶功能 - 标识 ViewHolder
 * <p>
 * 注意：悬浮原理
 * {@link OverlayItemDecoration#onDrawOver(Canvas, RecyclerView, RecyclerView.State)} draw bitmap
 * 没有交互事件，{@link #getOverlayView()} 返回的view, 通过{@link View#getDrawingCache()} 获取bitmap，谨慎bitmap过大
 *
 * @author a_liYa
 * @date 2017/9/23 15:55.
 */
public abstract class OverlayViewHolder<T> extends BaseRecyclerViewHolder<T> {

    public OverlayViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    public OverlayViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void setData(T data) {
        if (mData != data) { // 悬浮类型 针对重复setData做优化
            super.setData(data);
            View overlayView = getOverlayView();
            if (overlayView != null && overlayView.isDrawingCacheEnabled()) { // 真正悬浮的 view holder
                overlayView.setDrawingCacheEnabled(false); // 解决文字长短变化时显示不全
//                overlayView.destroyDrawingCache();
//                overlayView.buildDrawingCache();
            }
        }
    }

    /**
     * 真正悬浮的view，子类可通过 Override 返回自己的view
     *
     * @return 默认返回 {@link #itemView}
     */
    public View getOverlayView() {
        return itemView;
    }

    /**
     * 悬浮View偏移量，
     * 由于 {@link #getOverlayView()} 重写返回不是 {@link #itemView} 为了悬浮View在顶部悬浮，需要重写此方法
     *
     * @return 默认：0
     */
    public int getOverlayOffset() {
        return 0;
    }

}
