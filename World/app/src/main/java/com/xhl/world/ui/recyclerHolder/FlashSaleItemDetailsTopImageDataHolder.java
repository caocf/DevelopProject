package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xhl.world.R;
import com.xhl.world.model.Entities.BaseTargetEntities;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 15/12/12.
 */
public class FlashSaleItemDetailsTopImageDataHolder extends RecyclerDataHolder {

    private BaseTargetEntities mData;

    public FlashSaleItemDetailsTopImageDataHolder(BaseTargetEntities data) {
        super(data);
        this.mData = data;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        int height = context.getResources().getDimensionPixelOffset(R.dimen.special_sale_image_view_height);

        LifeCycleImageView imageView = new LifeCycleImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, height));

        AutoUtils.autoSize(imageView);

        return new RecyclerViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        LifeCycleImageView imageView = (LifeCycleImageView) vHolder.itemView;
        if (mData == null || TextUtils.isEmpty(mData.getImage_url())) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.bindImageUrl(this.mData.getImage_url());
        }
    }
}
