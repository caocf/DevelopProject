package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.fragment.OrderProductStatisticsFragment;

import org.xutils.view.annotation.ContentView;

/**
 * Created by Sum on 16/4/22.
 */
@ContentView(R.layout.activity_store_car)
public class StoreCarActivity extends BaseAppActivity {


    private OrderProductStatisticsFragment fragment;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.menu_store);

        ProductQueryCondition condition = new ProductQueryCondition();
        condition.queryType = ProductQueryCondition.TYPE_CAR_PRODUCT;

        fragment = OrderProductStatisticsFragment.newInstance(condition);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_store_apply_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_store_manager) {
            startActivity(new Intent(this, StoreCarManagerActivity.class));
        } else if (item.getItemId() == R.id.menu_store_manager_apply) {
            startActivity(new Intent(this, StoreCarReturnApplyActivity.class));
        }
        return true;
    }

}
