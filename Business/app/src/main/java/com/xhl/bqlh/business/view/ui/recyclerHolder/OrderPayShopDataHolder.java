package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.StatisticsShopModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.ui.activity.StatisticsShopDetailsActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/4/15.
 */
public class OrderPayShopDataHolder extends RecyclerDataHolder<StatisticsShopModel> {

    public OrderPayShopDataHolder(StatisticsShopModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        return new ShopViewHolder(View.inflate(context, R.layout.item_account_shop, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, StatisticsShopModel data) {
        ShopViewHolder holder = (ShopViewHolder) vHolder;
        holder.onBindData(data);
    }

    public class ShopViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.tv_shop_name)
        private TextView tv_shop_name;

        @ViewInject(R.id.tv_shop_money)
        private TextView tv_shop_money;

        @ViewInject(R.id.ll_details)
        private View ll_details;

        StatisticsShopModel model;

        public ShopViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
            ll_details.setOnClickListener(this);
        }

        public void onBindData(StatisticsShopModel data) {
            if (data == null) {
                return;
            }
            model = data;
            tv_shop_name.setText(data.getCompanyName());
            tv_shop_money.setText(mContext.getString(R.string.price, data.getArrears()));
        }

        @Override
        public void onClick(View v) {
            if (model == null) {
                return;
            }
            Intent intent = new Intent(mContext, StatisticsShopDetailsActivity.class);
            intent.putExtra(GlobalParams.Intent_shop_id, model.getRetailerId());
            intent.putExtra(GlobalParams.Intent_shop_name, model.getCompanyName());
            mContext.startActivity(intent);
        }
    }
}
