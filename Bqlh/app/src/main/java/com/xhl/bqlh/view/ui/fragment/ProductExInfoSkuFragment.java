package com.xhl.bqlh.view.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductSkuInfo;
import com.xhl.bqlh.view.base.BaseAppFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Sku信息介绍
 * Created by Sum on 16/7/8.
 */
@ContentView(R.layout.fragment_product_extend_info_sku)
public class ProductExInfoSkuFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_sku_1)
    private TextView tv_sku_1;

    @ViewInject(R.id.tv_sku_3)
    private TextView tv_sku_3;

    @ViewInject(R.id.tv_sku_4)
    private TextView tv_sku_4;

    @ViewInject(R.id.tv_sku_5)
    private TextView tv_sku_5;

    @ViewInject(R.id.tv_sku_6)
    private TextView tv_sku_6;

    @Override
    protected void initParams() {
        Bundle bundle = getArguments();
        ProductSkuInfo skuInfo = (ProductSkuInfo) bundle.getSerializable("data");

        tv_sku_1.setText(skuInfo.getSKU());
        tv_sku_3.setText(skuInfo.getBrandName());
        tv_sku_4.setText(skuInfo.getUnit());
        tv_sku_5.setText(skuInfo.getExpire_time());
        tv_sku_6.setText(skuInfo.getUnit());
    }
}
