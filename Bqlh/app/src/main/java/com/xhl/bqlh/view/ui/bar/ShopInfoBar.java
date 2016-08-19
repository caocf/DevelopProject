package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.model.ShopExInfoModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.base.Common.DefaultCallback;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopInfoBar extends BaseBar {

    public ShopInfoBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.iv_shop_state)
    private ImageView iv_shop_state;

    @ViewInject(R.id.tv_shop_title)
    private TextView tv_shop_title;

    @ViewInject(R.id.tv_shop_i_name)
    private TextView tv_shop_i_name;

    @ViewInject(R.id.tv_shop_i_phone)
    private TextView tv_shop_i_phone;

    @ViewInject(R.id.tv_shop_i_money)
    private TextView tv_shop_i_money;

    @ViewInject(R.id.tv_shop_i_area)
    private TextView tv_shop_i_area;

    @ViewInject(R.id.tv_shop_collect)
    private TextView tv_shop_collect;

    @ViewInject(R.id.tv_shop_collect_num)
    private TextView tv_shop_collect_num;

    @Event(R.id.tv_shop_collect)
    private void onShopCollectClick(View view) {
        if (!AppDelegate.appContext.isLogin(getContext())) {
            return;
        }
        String storeId = mShop.getShopId();
        //1：店铺，2：商品
        ApiControl.getApi().collectAdd(storeId, "1", new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                Object obj = result.getObj();
                String res = (String) obj;
                if (res.equals("success")) {
                    ToastUtil.showToastShort("收藏成功");
                } else {
                    ToastUtil.showToastShort("已收藏");
                }

            }

            @Override
            public void finish() {

            }
        });
    }

    private ShopModel mShop;

    @Override
    protected void initParams() {

    }

    public void onBindData(AShopDetails shop) {

        ShopExInfoModel exInfoModel = shop.getAllinfo();

        ShopModel shops = shop.getShops();
        mShop = shops;
        if (exInfoModel != null) {
            tv_shop_i_name.setText(getResources().getString(R.string.shop_i_name, exInfoModel.getLiableName()));
            tv_shop_i_phone.setText(getResources().getString(R.string.shop_i_phone, exInfoModel.getLiablePhone()));
            tv_shop_i_area.setText(getResources().getString(R.string.shop_i_area, exInfoModel.getDeliveryRange()));
            tv_shop_i_money.setText(getResources().getString(R.string.shop_i_money, exInfoModel.getMin_order_price()));
        }
        tv_shop_collect_num.setText(getResources().getString(R.string.shop_i_collection, shops.getCollectNum()));
        tv_shop_title.setText(shops.getShopName());
        if (TextUtils.isEmpty(shops.getBail())) {
            iv_shop_state.setImageResource(R.drawable.icon_shop_bao);
        } else if (shops.getBail().equals("1")) {
            iv_shop_state.setImageResource(R.drawable.icon_shop_bao2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_info;
    }
}
