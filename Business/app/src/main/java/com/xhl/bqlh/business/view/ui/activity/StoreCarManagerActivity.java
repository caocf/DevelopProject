package com.xhl.bqlh.business.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.ui.recyclerHolder.StoreApplyCarReturnDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/25.
 */
@ContentView(R.layout.activity_store_car_manager)
public class StoreCarManagerActivity extends BaseAppActivity {

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Event(R.id.btn_return)
    private void onReturnClick(View view) {
        List<ProductModel> datas = this.mDatas;
        boolean has = false;
        final JSONArray array = new JSONArray();
        for (ProductModel pro : datas) {
            if (pro.checked) {
                has = true;
                JSONObject object = new JSONObject();
                try {
                    object.put("id", pro.applyId);
                    object.put("productId", pro.getId());
                    object.put("applyNum", pro.curNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(object);
            }
        }
        if (!has) {
            ToastUtil.showToastShort("请勾选退库商品");
            return;
        }

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("退库提示");
        dialog.setMessage("商品数量全部退仓后将无法在车削商品中查看到相关商品");
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rePro(array.toString());
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void rePro(String pro) {
        showLoadingDialog();
        ApiControl.getApi().storeCarConfirmDelete(pro, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                ToastUtil.showToastShort("退仓成功");
                onRefreshLoadData();
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }

    private RecyclerAdapter mAdapter;
    private List<ProductModel> mDatas;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.menu_store_manager);
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                onRefreshLoadData();
            }
        }, 500);

    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();

        ApiControl.getApi().storeCarQueryApply(10000, getPageIndex(), "", new DefaultCallback<ResponseModel<ApplyModel>>() {
            @Override
            public void success(ResponseModel<ApplyModel> result) {
                setTotalSize(result.getPageSize());
                List<ApplyModel> objList = result.getObjList();
                loadCarProduct(ModelHelper.ApplyModel2ProductModel(objList, null));
            }

            @Override
            public void finish() {
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void loadCarProduct(List<ProductModel> data) {
        mDatas = data;
        if (data == null) {
            return;
        }
        if (data.size() == 0 && getPageIndex() == 1) {
            tv_text_null.setVisibility(View.VISIBLE);
        } else {
            tv_text_null.setVisibility(View.GONE);
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ProductModel pro : data) {
            //商品为0的数据自动删除，不做显示
            if (pro.getStock() == 0) {
                pro.checked = true;
                continue;
            }
            pro.curNum = pro.getStock();
            holders.add(new StoreApplyCarReturnDataHolder(pro));
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_store_apply_manager, menu);

        menu.getItem(0).setVisible(false);

        menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, StoreCarReturnApplyActivity.class));
        return true;
    }

}
