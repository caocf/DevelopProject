package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.SearchShopModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.bar.ShopStateBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/4/20.
 */
public class SearchDataHolder extends RecyclerDataHolder<SearchShopModel> {

    public SearchDataHolder(SearchShopModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new SearchViewHolder(View.inflate(context, R.layout.item_search_nearby, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, SearchShopModel data) {
        SearchViewHolder holder = (SearchViewHolder) vHolder;
        holder.onBindData(data);
    }

    class SearchViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.tv_shop_name)
        private TextView tv_shop_name;

        @ViewInject(R.id.tv_shop_location)
        private TextView tv_shop_location;

        @ViewInject(R.id.shop_state)
        private ShopStateBar shop_state;

        @ViewInject(R.id.tv_shop_type_name)
        private TextView tv_shop_type_name;

        @ViewInject(R.id.tv_shop_distance)
        private TextView tv_shop_distance;

        private SearchShopModel mShop;

        public SearchViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
            view.setOnClickListener(this);
        }

        public void onBindData(SearchShopModel shop) {
            if (shop == null) {
                return;
            }
            mShop = shop;
            tv_shop_name.setText(shop.getShopName());
            tv_shop_location.setText(mContext.getString(R.string.location, shop.getShopLocation()));
            int shopType = shop.getShopType();
            if (shopType == 1) {
                //店铺类型
                ViewHelper.setViewVisible(shop_state, true);
                shop_state.showState(shop.getMember());
            } else {
                ViewHelper.setViewVisible(shop_state, false);
            }
            String type = shop.getCompanyType();
            if (!TextUtils.isEmpty(type)) {
                tv_shop_type_name.setText("类型:" + type);
                tv_shop_type_name.setVisibility(View.VISIBLE);
            } else {
                tv_shop_type_name.setVisibility(View.GONE);
            }
            tv_shop_distance.setText(shop.getDistance() + "米");
        }

        @Override
        public void onClick(View v) {
            if (mShop == null) {
                return;
            }
            EventHelper.postSearchShop(mShop);
        }
    }
}
