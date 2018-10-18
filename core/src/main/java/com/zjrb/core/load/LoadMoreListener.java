package com.zjrb.core.load;

import com.zjrb.core.recycleView.LoadMore;

/**
 * LoadMoreListener
 *
 * @author a_liYa
 * @date 2017/8/24 20:02.
 */
public interface LoadMoreListener<M>{

    void onLoadMoreSuccess(M data, LoadMore loadMore);

    void onLoadMore(LoadingCallBack<M> callback);
}