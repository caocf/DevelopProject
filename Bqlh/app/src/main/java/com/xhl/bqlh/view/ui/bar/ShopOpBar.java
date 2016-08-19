package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.view.View;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.event.ShopEvent;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.helper.EventHelper;

import org.xutils.view.annotation.Event;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopOpBar extends BaseBar {

    @Event(R.id.ll_product)
    private void onProductClick(View view) {
        postEvent(ShopEvent.TAG_PRODUCT);
    }

    @Event(R.id.ll_category)
    private void onCategoryClick(View view) {
        postEvent(ShopEvent.TAG_CATEGORY);
    }

    @Event(R.id.ll_brand)
    private void onBrandClick(View view) {
        postEvent(ShopEvent.TAG_BRAND);
    }

    @Event(R.id.ll_service)
    private void onServiceClick(View view) {
        postEvent(ShopEvent.TAG_SERVICE);
    }

    private void postEvent(int tag) {
        ShopEvent event = new ShopEvent();
        event.setTag(tag);
        EventHelper.postDefaultEvent(event);
    }

    public ShopOpBar(Context context) {
        super(context);
    }

    @Override
    protected void initParams() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_op;
    }
}
