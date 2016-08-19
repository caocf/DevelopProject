package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ShopApplyModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.activity.CustomerUpdateActivity;
import com.xhl.bqlh.business.view.ui.bar.ShopStateBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/5/4.
 */
public class CustomerApplyDataHolder extends RecyclerDataHolder<ShopApplyModel> {
    public CustomerApplyDataHolder(ShopApplyModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new ShopViewHolder(View.inflate(context, R.layout.item_shop_apply, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopApplyModel data) {
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

        @ViewInject(R.id.tv_shop_apply_state)
        private TextView tv_shop_apply_state;//店铺审核状态

        @ViewInject(R.id.shop_state)
        private ShopStateBar shop_state;

        private ShopApplyModel model;

        public ShopViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model != null) {
                        Intent intent = new Intent(mContext, CustomerUpdateActivity.class);
                        intent.putExtra("data", model);
                        intent.putExtra(CustomerUpdateActivity.TAG_UPDATE_TYPE, CustomerUpdateActivity.TYPE_EXIT_INFO);
                        ((Activity) mContext).startActivityForResult(intent, 1);
                    }
                }
            });
        }

        public void onBindData(ShopApplyModel data) {
            if (data == null) {
                return;
            }
            model = data;
            tv_shop_name.setText(data.getCompanyName());
            tv_shop_location.setText(TextUtils.concat("地址: ", data.getAddress()));
            tv_shop_user.setText(data.getLiableName());
            int state = data.getState();
            if (state == 1) {//审核通过
                ViewHelper.setViewGone(shop_state, false);
                ViewHelper.setViewGone(tv_shop_apply_state, true);
                shop_state.showState(data.getMember());
            } else {
                ViewHelper.setViewGone(shop_state, true);
                ViewHelper.setViewGone(tv_shop_apply_state, false);
                if (state == 0) {
                    tv_shop_apply_state.setText("审核中");
                    tv_shop_apply_state.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                } else {
                    tv_shop_apply_state.setText("未通过");
                    tv_shop_apply_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_grey));
                }
            }

        }
    }

}
