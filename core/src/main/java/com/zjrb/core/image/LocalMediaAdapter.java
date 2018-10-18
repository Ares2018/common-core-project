package com.zjrb.core.image;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zjrb.core.R;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地媒体资源 Adapter
 *
 * @author a_liYa
 * @date 16/10/22 09:48.
 */
public class LocalMediaAdapter extends BaseRecyclerAdapter<MediaEntity> implements
        CompoundButton.OnCheckedChangeListener {

    private ArrayList<MediaEntity> selectedList;

    private int maxNum; // 最多可选素材

    private Callback mCallback;

    public LocalMediaAdapter(List<MediaEntity> list, int maxNum, Callback listener) {
        super(list);
        selectedList = new ArrayList<>();
        this.maxNum = maxNum;
        mCallback = listener;
    }

    @Override
    public LocalMediaViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalMediaViewHolder(parent, this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if (tag instanceof MediaEntity) {
            ((MediaEntity) tag).setSelected(isChecked);
            if (isChecked) {
                if (selectedList.size() == maxNum && 1 != maxNum) { // 已达最大个数
                    buttonView.setOnCheckedChangeListener(null);
                    buttonView.setChecked(false);
                    buttonView.setOnCheckedChangeListener(this);
                    ((MediaEntity) tag).setSelected(false);
                    if (mCallback != null) {
                        mCallback.onSelectedLimit(maxNum);
                    }
                    return;
                } else if (selectedList.size() == maxNum && 1 == maxNum) { // 单选
                    clearSelected();
                }
                selectedList.add((MediaEntity) tag);
            } else {
                selectedList.remove(tag);
            }
            if (mCallback != null) {
                mCallback.onCheckedChanged(buttonView, isChecked);
            }
        }
    }

    private void clearSelected() {
        if (selectedList != null) {
            for (MediaEntity entity : selectedList) {
                entity.setSelected(false);
                int indexOf = datas.indexOf(entity);
                if (indexOf >= 0) {
                    notifyItemChanged(indexOf);
                }
            }
            selectedList.clear();
        }
    }

    public ArrayList<MediaEntity> getSelectedList() {
        return selectedList;
    }

    static class LocalMediaViewHolder extends BaseRecyclerViewHolder<MediaEntity> implements View
            .OnClickListener {

        ImageView ivPicture;
        CheckBox checkBox;

        LocalMediaViewHolder(ViewGroup parent, CompoundButton.OnCheckedChangeListener listener) {
            super(inflate(R.layout.module_item_local_media_select, parent, false));
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
            checkBox.setOnCheckedChangeListener(listener);
        }

        @Override
        public void bindView() {
            itemView.setOnClickListener(this);
            if (TextUtils.isEmpty(mData.getThumbnail())) {
                Glide.with(ivPicture).load(mData.getPath()).into(ivPicture);
            } else {
                Glide.with(ivPicture).load(mData.getThumbnail()).into(ivPicture);
            }
            checkBox.setTag(null);
            checkBox.setChecked(mData.isSelected());
            checkBox.setTag(mData);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        }
    }

    public interface Callback {

        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);

        void onSelectedLimit(int limit);
    }

}
