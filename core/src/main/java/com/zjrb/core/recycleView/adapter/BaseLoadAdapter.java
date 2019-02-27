package com.zjrb.core.recycleView.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjrb.core.R;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseLvViewHolder;
import com.zjrb.core.recycleView.MoreHolderBase;

import java.util.List;

/**
 * ListView、GridLayout适配器基类
 *
 * @param <T> Item数据类型
 * @param <M> 加载更多时返回的数据类型
 * @author a_liYa
 * @date 2016-3-1 下午1:57:51
 */
public abstract class BaseLoadAdapter<T, M> extends BaseAdapter implements
        LoadingCallBack<M> {

    public BaseLoadAdapter(List<T> mDatas) {
        setData(mDatas);
    }

    public List<T> mDatas;
    private BaseLvViewHolder holder;

    public void setData(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    public List<T> getData() {
        return mDatas;
    }

    /**
     * 删除指定索引的数据
     *
     * @param index 删除的索引
     * @return
     */
    public T deleData(int index) {
        if (index < 0)
            return null;
        if (mDatas == null)
            return null;

        if (index >= mDatas.size())
            return null;

        T delete = mDatas.remove(index);

        notifyDataSetChanged();

        return delete;
    }

    /**
     * 返回数据数量
     */
    public int getDataSize() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getCount() {
        // +1 ：表示加上更多的条目
        if (null != mDatas && mDatas.size() > 0) {
            return hasMore() ? mDatas.size() + 1 : mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 切记：一定需要注意当前的数据是从0开始 表示加载更多的数据
     */
    /**
     * 表示普通的数据类型
     */
    public final int ITEM_VIEW_TYPE = 0;

    /**
     * 表示加载更多的数据类型
     */
    public final int MORE_ITEM_VIEW_TYPE = 1;

    private MoreHolderBase moreHolder;

    /**
     * 返回view的类型 0 普通的数据类型 1 更多的数据类型
     */
    @Override
    public int getItemViewType(int position) {
        if (hasMore()) {
            if (position == getCount() - 1) {
                // 表示加载更多
                return MORE_ITEM_VIEW_TYPE;
            } else {
                // 表示普通的数据类型
                return getInnerItemViewType(position);
            }
        } else {
            return getInnerItemViewType(position);
        }
    }

    /**
     * 内部Item类型 如果需要增加类型可在子类中重写该方法 并重写getViewTypeCount()方法
     *
     * @param position 当前条目索引
     * @return Item类型
     */
    public int getInnerItemViewType(int position) {
        return ITEM_VIEW_TYPE;
    }

    /**
     * 返回一共有多少种数据类型
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1; // + 1 表示有一个加载更多类型
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView != null) {
            holder = (BaseLvViewHolder) convertView.getTag(R.id.tag_holder);
        } else {
            // 判断当前的返回数据类型是否是加载更多的数据类型
            if (MORE_ITEM_VIEW_TYPE == getItemViewType(position)) {
                holder = getMoreHolder(parent);
            } else {
                // 加载普通的holder
                holder = getHolder(parent);
            }
        }
        // 判断当前的数据类型是否是加载更多
        if (getItemViewType(position) != MORE_ITEM_VIEW_TYPE) {
            holder.setData(mDatas.get(position));
            holder.setPosition(position);
        }

        return holder.getRootView();
    }

    /**
     * 加载更多的holder
     */
    public BaseLvViewHolder getMoreHolder(ViewGroup parent) {
        if (null == moreHolder) {
            // 就说明当前有更多的数据
            moreHolder = new MoreHolderBase(parent, hasMore(), this);
        }
        return moreHolder;
    }

    /**
     * 是否有加载更多
     *
     * @return false 代表没有加载更多; true 代表有加载更多
     */
    public boolean hasMore() {
        return true;
    }

    /**
     * 获取ViewHolder
     *
     * @param parent 当前父视图 : listview或者gridview
     * @return
     */
    public abstract BaseLvViewHolder<T> getHolder(ViewGroup parent);

    /**
     * 是否加载
     */
    public boolean isLoading = false;

    /**
     * 加载更多
     */
    public void loadMore() {

        if (!isLoading) {

            isLoading = true;

            onLoadMore(BaseLoadAdapter.this);

        }
    }

    /**
     * 加载更多数据 异步处理 由子类去实现具体的业务
     */
    protected abstract void onLoadMore(LoadingCallBack<M> loadingCallBack);

    @Override
    public void onCancel() {
        getMoreHolder(null).setData(MoreHolderBase.ERROR);
        isLoading = false;
    }

    @Override
    public void onError(String errMsg, int errCode) {
        getMoreHolder(null).setData(MoreHolderBase.ERROR);
        isLoading = false;
    }

    @Override
    public void onSuccess(M m) {
        if (m == null) {
            getMoreHolder(null).setData(MoreHolderBase.NO_MORE);
        } else {
            onLoadMoreSuccess(m);
        }
        isLoading = false;
    }

    /**
     * 追加数据
     *
     * @param list 被追加的数据
     */
    protected void checkAddDatas(List<T> list) {
        if (null != list && list.size() > 0) {
            if (mDatas != null) {
                mDatas.addAll(list);
            } else {
                setData(list);
            }
            notifyDataSetChanged();
        } else {
            getMoreHolder(null).setData(MoreHolderBase.NO_MORE);
        }
    }

    /**
     * 加载更多数据成功 回调
     */
    abstract protected void onLoadMoreSuccess(M m);

}
