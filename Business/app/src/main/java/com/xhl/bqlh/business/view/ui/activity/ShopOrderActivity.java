package com.xhl.bqlh.business.view.ui.activity;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.fragment.OrderManagerFragment;

import org.xutils.view.annotation.ContentView;

/**
 * Created by Sum on 16/5/30.
 */
@ContentView(R.layout.activity_shop_order)
public class ShopOrderActivity extends BaseAppActivity {

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.shop_detail_order);

        OrderQueryCondition mCondition = new OrderQueryCondition();
        mCondition.shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        mCondition.hint = "门店暂无订单";
        final OrderManagerFragment fragment = OrderManagerFragment.newInstance(mCondition);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).commit();

        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment.onRefreshTop();
            }
        }, 50);
    }
}
