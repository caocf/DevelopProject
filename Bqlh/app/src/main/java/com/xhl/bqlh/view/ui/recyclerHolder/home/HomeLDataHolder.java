package com.xhl.bqlh.view.ui.recyclerHolder.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.view.custom.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Summer on 2016/7/19.
 */
public class HomeLDataHolder extends RecyclerDataHolder<List<AdInfoModel>> {

    private int type;

    public HomeLDataHolder(List<AdInfoModel> data, int type) {
        super(data);
        this.type = type;
    }

    @Override
    public int getType() {
//        return type + 10;
    return 99;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        View content = View.inflate(context, R.layout.item_home_l, null);

        content.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

        AutoUtils.auto(content);

        return new AdViewHolder(content);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, List<AdInfoModel> data) {
        AdViewHolder holder = (AdViewHolder) vHolder;
        holder.onBindData(data);
    }

    static class AdViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.iv_top_head)
        private ImageView iv_top_head;

        @ViewInject(R.id.iv_top)
        private ImageView iv_top;

        @ViewInject(R.id.iv_product_image_1)
        private ImageView iv_product_image_1;

        @ViewInject(R.id.iv_product_image_2)
        private ImageView iv_product_image_2;

        @ViewInject(R.id.iv_product_image_3)
        private ImageView iv_product_image_3;

        @ViewInject(R.id.tv_product_name_1)
        private TextView tv_product_name_1;

        @ViewInject(R.id.tv_product_name_2)
        private TextView tv_product_name_2;

        @ViewInject(R.id.tv_product_name_3)
        private TextView tv_product_name_3;

        @ViewInject(R.id.tv_product_price_1)
        private TextView tv_product_price_1;

        @ViewInject(R.id.tv_product_price_2)
        private TextView tv_product_price_2;

        @ViewInject(R.id.tv_product_price_3)
        private TextView tv_product_price_3;

        private List<AdInfoModel> mData;

        public AdViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(List<AdInfoModel> data) {
            if (data.size() == 5) {
                if (mData == data) {
                    return;
                }
                mData = data;

                AdInfoModel ad1 = data.get(0);
                x.image().bind(iv_top_head, ad1.getImageUrl(), LifeCycleImageView.imageOptions);

                //设置数据
                AdInfoModel adInfoModel = data.get(1);
                x.image().bind(iv_top, adInfoModel.getImageUrl(), LifeCycleImageView.imageOptions);
                iv_top.setTag(adInfoModel);
                iv_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object tag = v.getTag();
                        if (tag != null && tag instanceof AdInfoModel) {
                            AdInfoModel.postEvent((AdInfoModel) tag);
                        }
                    }
                });

                //商品数据
                showData(data.get(2), iv_product_image_1, tv_product_name_1, tv_product_price_1);
                showData(data.get(3), iv_product_image_2, tv_product_name_2, tv_product_price_2);
                showData(data.get(4), iv_product_image_3, tv_product_name_3, tv_product_price_3);

            }
        }

        private void showData(AdInfoModel ad, ImageView pic, TextView name, TextView price) {
            //图片
            x.image().bind(pic, ad.getImageUrl(), LifeCycleImageView.imageOptions);
            //名称
            name.setText(ad.getAdName());
            if (TextUtils.isEmpty(ad.getRetailPrice())) {
                price.setVisibility(View.GONE);
            } else {
                //价格
                price.setVisibility(View.VISIBLE);
                price.setText(mContext.getString(R.string.price_s, ad.getRetailPrice()));
            }

            pic.setTag(ad);
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag != null && tag instanceof AdInfoModel) {
                        AdInfoModel.postEvent((AdInfoModel) tag);
                    }
                }
            });
        }
    }
}
