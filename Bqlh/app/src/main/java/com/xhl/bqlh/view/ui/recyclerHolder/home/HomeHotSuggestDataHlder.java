package com.xhl.bqlh.view.ui.recyclerHolder.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by Summer on 2016/7/19.
 * 热门推荐
 */
public class HomeHotSuggestDataHlder extends RecyclerDataHolder<List<AdInfoModel>> {

    public HomeHotSuggestDataHlder(List<AdInfoModel> data) {
        super(data);
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View content = View.inflate(context, R.layout.item_home_hot_product, null);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(-1, -2);
        params.topMargin = 20;
        params.bottomMargin = 20;
        content.setLayoutParams(params);

        AutoUtils.auto(content);

        return new HotProViewHolder(content);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, List<AdInfoModel> data) {
        HotProViewHolder holder = (HotProViewHolder) vHolder;
        holder.onBindData(data);
    }

    static class HotProViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        private ImageView iv_1, iv_2, iv_3;

        private List<AdInfoModel> mData;

        public HotProViewHolder(View view) {
            super(view);
            iv_1 = (ImageView) view.findViewById(R.id.iv_1);
            iv_1.setOnClickListener(this);
            iv_2 = (ImageView) view.findViewById(R.id.iv_2);
            iv_2.setOnClickListener(this);
            iv_3 = (ImageView) view.findViewById(R.id.iv_3);
            iv_3.setOnClickListener(this);
        }

        public void onBindData(List<AdInfoModel> data) {
            if (data.size() == 3) {
                if (mData == data) {
                    return;
                }
                mData = data;
                AdInfoModel ad0 = data.get(0);
                iv_1.setTag(ad0);
                x.image().bind(iv_1, ad0.getImageUrl(), LifeCycleImageView.imageOptions);

                AdInfoModel ad1 = data.get(1);
                iv_2.setTag(ad1);
                x.image().bind(iv_2, ad1.getImageUrl(), LifeCycleImageView.imageOptions);

                AdInfoModel ad2 = data.get(2);
                iv_3.setTag(ad2);
                x.image().bind(iv_3, ad2.getImageUrl(), LifeCycleImageView.imageOptions);

            }
        }

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof AdInfoModel) {
                AdInfoModel.postEvent((AdInfoModel) tag);
            }
        }
    }
}
