package com.xhl.bqlh.view.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductSkuInfo;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.xhl_library.ui.FragmentCacheManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/7.
 */
@ContentView(R.layout.fragment_product_extend_info)
public class ProductExInfoFragment extends BaseAppFragment {


    public static ProductExInfoFragment instance(ProductSkuInfo sku, String images) {
        ProductExInfoFragment fragment = new ProductExInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", images);
        bundle.putSerializable("data", sku);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.tv_info)
    private TextView tv_info;

    @ViewInject(R.id.tv_sku)
    private TextView tv_sku;

    @Event(R.id.tv_info)
    private void onInfoClick(View view) {
        mManager.setCurrentFragment(1);
        tv_info.setSelected(true);
        tv_sku.setSelected(false);
    }

    @Event(R.id.tv_sku)
    private void onSkuClick(View view) {
        mManager.setCurrentFragment(2);
        tv_info.setSelected(false);
        tv_sku.setSelected(true);
    }

    private FragmentCacheManager mManager;

    @Override
    protected void initParams() {

        mManager = new FragmentCacheManager();
        mManager.setUp(this, R.id.fl_content);
        //商品图片
        mManager.addFragmentToCache(1, ProductExInfoImageFragment.class, getArguments());
        //商品介绍
        mManager.addFragmentToCache(2, ProductExInfoSkuFragment.class, getArguments());

        mManager.setCurrentFragment(1);
        tv_info.setSelected(true);
        tv_sku.setSelected(false);
    }
}
