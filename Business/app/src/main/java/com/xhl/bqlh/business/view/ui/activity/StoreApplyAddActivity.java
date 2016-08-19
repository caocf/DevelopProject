package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.ui.fragment.StoreApplyFragment;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/7.
 * 装车单申请
 */
@ContentView(R.layout.activity_store_apply_add)
public class StoreApplyAddActivity extends BaseAppActivity {

    private StoreApplyFragment storeApplyFragment;

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
        List<ApplyModel> allProducts = storeApplyFragment.getAllProducts();
        if (allProducts.size() <= 0) {
            ToastUtil.showToastShort("暂无装车商品");
        } else {
            String ids = storeApplyFragment.getOrderIds();
            String toJson = new Gson().toJson(allProducts);
            showProgressLoading("创建装车单中");
            ApiControl.getApi().storeApplyAdd(toJson, ids, new Callback.CommonCallback<ResponseModel<Object>>() {
                @Override
                public void onSuccess(ResponseModel<Object> result) {
                    if (result.isSuccess()) {
                        EventHelper.postCommonEvent(CommonEvent.EVENT_REFRESH_STORE);
                        finish();
                    } else {
                        SnackUtil.shortShow(mToolbar, result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    SnackUtil.shortShow(mToolbar, ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    hideLoadingDialog();
                }
            });
        }

    }

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle("添加装车单");
        //装车商品
        storeApplyFragment = new StoreApplyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, storeApplyFragment).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int type = intent.getIntExtra("type", 0);

        if (type == 0) {

        } else if (type == 1) {//新增车销商品
            ArrayList<ShopCarModel> cars = intent.getParcelableArrayListExtra("data");
            storeApplyFragment.addCarProducts(cars);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_store_apply_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, AddOrderActivity.class));
        return true;
    }

}
