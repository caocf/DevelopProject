package com.xhl.bqlh.view.ui.activity.web;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.ui.activity.ProductDetailsActivity;
import com.xhl.bqlh.view.ui.activity.SearchProductResActivity;
import com.xhl.bqlh.view.ui.activity.ShopDetailsActivity;


public class RemoteInvokeService {
    private Activity mContext;

    public RemoteInvokeService(Activity paramActivity) {
        mContext = paramActivity;
    }

    /**
     * Android 4.2以上需要添加注解@JavascriptInterface
     */
    @JavascriptInterface
    public void close() {
        mContext.finish();
    }

    @JavascriptInterface
    public void login() {
        FragmentContainerHelper.startFragment(mContext, FragmentContainerHelper.fragment_login);
        if (!mContext.isFinishing()) {
            mContext.finish();
        }
    }

    @JavascriptInterface
    public void productDetail(String productId) {
        EventHelper.postProduct(productId);
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("id", productId);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void shopDetail(String shopId) {
        EventHelper.postShop(shopId);
        Intent intent = new Intent(mContext, ShopDetailsActivity.class);
        intent.putExtra("id", shopId);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void openListForGoodsWithBrandId(String brandId) {
        Intent intent = new Intent(mContext, SearchProductResActivity.class);
        intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_BRAND);
        intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, brandId);
        mContext.startActivity(intent);
    }

}
