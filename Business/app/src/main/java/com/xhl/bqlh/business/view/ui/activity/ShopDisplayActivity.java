package com.xhl.bqlh.business.view.ui.activity;

import android.os.Bundle;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.fragment.ShopDisplayFragment;

/**
 * Created by Sum on 16/6/3.
 */
public class ShopDisplayActivity extends BaseAppActivity {

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        String shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        String shopName = getIntent().getStringExtra(GlobalParams.Intent_shop_name);

        //参数
        Object[] data = new Object[]{shopId, shopName};

        pushFragmentToBackStack(ShopDisplayFragment.class,data);
    }

    @Override
    protected void initParams() {
    }
}
