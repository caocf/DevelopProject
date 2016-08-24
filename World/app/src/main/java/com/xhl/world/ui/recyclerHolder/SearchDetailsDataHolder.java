package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.Product;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 15/12/7.
 */
public class SearchDetailsDataHolder extends RecyclerDataHolder {

    public SearchDetailsDataHolder(Product data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_search_item_details, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        AutoUtils.auto(view);

        return new SearchDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        SearchDetailsViewHolder holder = (SearchDetailsViewHolder) vHolder;
        holder.onBindData((Product) data);
    }

    static class SearchDetailsViewHolder extends RecyclerViewHolder implements View.OnClickListener {
        private LifeCycleImageView iv_item_search_icon;
        private TextView tv_item_search_title;
        private TextView tv_item_search_price;
        private TextView tv_item_search_country;
        private TextView tv_item_search_judge;
        private Product product;

        public SearchDetailsViewHolder(View view) {
            super(view);
            iv_item_search_icon = (LifeCycleImageView) view.findViewById(R.id.iv_item_search_icon);
            tv_item_search_title = (TextView) view.findViewById(R.id.tv_item_search_title);
            tv_item_search_price = (TextView) view.findViewById(R.id.tv_item_search_price);
            tv_item_search_country = (TextView) view.findViewById(R.id.tv_item_search_country);
            tv_item_search_judge = (TextView) view.findViewById(R.id.tv_item_search_judge);

            view.setOnClickListener(this);
        }

        public void onBindData(Product product) {
            if (product == null) {
                return;
            }

            this.product = product;

            iv_item_search_icon.bindImageUrl(product.getProductPicUrl());
            tv_item_search_country.setText(product.getProductProperty());
            tv_item_search_title.setText(product.getProductName());
            tv_item_search_price.setText(AppApplication.appContext.getString(R.string.price, product.getProductPrice()));
        }


        @Override
        public void onClick(View v) {
            String id = product.getId();
            if (!TextUtils.isEmpty(id)) {
                EventBusHelper.postProductDetails(id);
            }
        }
    }
}
