package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ShopFriendsModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.ui.bar.ShopStateBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/5/4.
 */
public class CustomerFriendsDataHolder extends RecyclerDataHolder<ShopFriendsModel> {
    public CustomerFriendsDataHolder(ShopFriendsModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new ShopViewHolder(View.inflate(context, R.layout.item_shop_apply_friend, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopFriendsModel data) {
        ShopViewHolder holder = (ShopViewHolder) vHolder;
        holder.onBindData(data);
    }

    public class ShopViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.tv_shop_name)
        private TextView tv_shop_name;

        @ViewInject(R.id.tv_shop_location)
        private TextView tv_shop_location;

        @ViewInject(R.id.tv_shop_user)
        private TextView tv_shop_user;

        @ViewInject(R.id.shop_state)
        private ShopStateBar shop_state;

        ShopFriendsModel model;

        public ShopViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(ShopFriendsModel data) {
            if (data == null) {
                return;
            }
            model = data;
            tv_shop_name.setText(data.getRetailerCompanyName());
            tv_shop_location.setText(TextUtils.concat("地址: ", data.getRetailerAddress()));
            tv_shop_user.setText(data.getRetailerName());
            shop_state.showState(data.getMember());
        }
    }

}
