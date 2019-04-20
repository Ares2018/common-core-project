package com.zjrb.core.recycleView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjrb.core.R;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;

public class FooterLoadMoreV2<M> extends PageItem implements LoadMore, View.OnClickListener,LoadingCallBack<M> {

    private int state = 0;
    private boolean isLoading = false;
    LoadMoreListener loadMoreListener;

    private View mLoadMoreView;
    private View mErrorMoreView;
    private View mNoMoreView;
    private View fyContainer;
    //用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;

    public FooterLoadMoreV2(RecyclerView parent, LoadMoreListener<M> loadMoreListener) {
        super(parent, R.layout.module_core_item_footer_load_more);

        fyContainer = findViewById(R.id.fy_container);
        mLoadMoreView = findViewById(R.id.layout_more_loading);
        mErrorMoreView = findViewById(R.id.layout_more_error);
        mNoMoreView = findViewById(R.id.layout_no_more);

        mErrorMoreView.setOnClickListener(this);
        this.loadMoreListener = loadMoreListener;
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的itemPosition
                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                    int itemCount = manager.getItemCount();

                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                        if (!isLoading && state != TYPE_ERROR && state != TYPE_NO_MORE) {
                            loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                isSlidingUpward = dy > 0;
            }
        });
    }

    /**
     * 获取加载更多listener
     *
     * @return
     */
    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    /**
     * 获取加载更多控件
     *
     * @return
     */
    public View getLoadMore() {
        return fyContainer;
    }

    // 防止加载少量数据，加载更多布局没出屏幕，不会回调 onViewAttachedToWindow
    private Runnable mKeepRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isLoading && state != TYPE_ERROR && state != TYPE_NO_MORE) {
                loadMore();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_more_error) {
            loadMore();
        }
    }

    public void setState(int state) {
        this.state = state;
        isLoading = state == TYPE_LOADING;
        updateState();
    }

    protected void updateState() {
        mLoadMoreView.setVisibility(state == TYPE_LOADING ? View.VISIBLE : View.GONE);
        mErrorMoreView.setVisibility(state == TYPE_ERROR ? View.VISIBLE : View.GONE);
        mNoMoreView.setVisibility(state == TYPE_NO_MORE ? View.VISIBLE : View.GONE);
    }


    private void loadMore() {
        setState(TYPE_LOADING);
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore(this);
        }
    }

    @Override
    public void onCancel() {
        setState(TYPE_ERROR);
    }

    @Override
    public void onError(String errMsg, int errCode) {
        setState(TYPE_ERROR);
    }

    @Override
    public void onSuccess(M data) {
        itemView.removeCallbacks(mKeepRunnable);
        itemView.postDelayed(mKeepRunnable, 500);
        isLoading = false;
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMoreSuccess(data, this);
        }
    }
}
