package com.xhl.bqlh.business.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.Model.Type.StoreProductType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductChildDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.StoreApplyUpdateDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Sum on 16/4/22.
 * 装车单更新
 */
@ContentView(R.layout.activity_store_car_apply_update)
public class StoreApplyUpdateActivity extends BaseAppActivity {

    @Event(R.id.fab_add)
    private void onAddClick(View view) {
        Intent intent = new Intent(this, SelectProductActivity.class);
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_CAR_UPDATE_ADD);
        startActivity(intent);
    }

    @ViewInject(R.id.fab_add)
    private View mAddView;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;

    private ApplyModel model;

    //分开存放控制数量显示
    private List<ApplyModel> mOrderProduct;//订单商品

    private List<ApplyModel> mCarProduct;//车销商品

    @Override
    protected void initParams() {
        //初始化参数
        model = (ApplyModel) getIntent().getSerializableExtra("data");
        if (model == null) {
            ToastUtil.showToastLong(R.string.data_error);
            finish();
            return;
        }

        mOrderProduct = new ArrayList<>();
        mCarProduct = new ArrayList<>();

        //状态
        int shstate = model.getShstate();
        if (shstate == 0) {
            setTitle("装车单修改");
            mAddView.setVisibility(View.VISIBLE);
            super.initToolbar(TYPE_child_other_clear);
        } else {
            setTitle("装车单明细");
            mAddView.setVisibility(View.GONE);
            super.initToolbar();
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        List<RecyclerDataHolder> holders = loadData(model);

        mAdapter = new RecyclerAdapter(this, holders);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    //加载数据
    private List<RecyclerDataHolder> loadData(ApplyModel apply) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        //原来的商品数据
        ArrayList<ApplyModel> list = apply.getList();

        //可以修改
        if (apply.getShstate() == 0) {
            for (ApplyModel model : list) {
                //分出商品
                if (model.getProductType() == 1) {
                    //添加到订单商品中
                    mOrderProduct.add(model);
                } else {
                    //添加到车销集合中
                    mCarProduct.add(model);
                }
            }
            //合并商品和组合数量
            ModelHelper.mergeApplyProductModel(mOrderProduct, mCarProduct);
            for (ApplyModel model : mCarProduct) {
                holders.add(new StoreApplyUpdateDataHolder(model));
            }
            //站位显示
            holders.add(new ProductChildDataHolder(null, null));
        } else {
            //合并相同商品
            ModelHelper.mergeApplyProductModel(list);
            //需要组合商品
            for (ApplyModel model : list) {
                ProductDataHolder holder = getProductDataHolder(model);
                holders.add(holder);
            }
        }
        return holders;
    }

    @NonNull
    private ProductDataHolder getProductDataHolder(ApplyModel model) {
        ProductModel product = new ProductModel();
        product.setProductName(model.getProductName());
        float unitPrice = model.getUnitPrice();
        //默认设置为建议零售价文字提示
        if (unitPrice != 0) {
            product.setOriginalPrice(new BigDecimal(unitPrice));
        } else {
            product.setOriginalPrice(model.getProductPrice());
        }
        product.setStock(model.getApplyNum());
        product.setProductPic(model.getProductPic());
        //赠品商品
        if (model.getProductType() == 4) {
            product.setProductType(ProductType.PRODUCT_GIFT);
        }
        //sku信息
        product.setSkuResult(model.getSkuResult());

        return new ProductDataHolder(product);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int type = intent.getIntExtra("type", 0);
        //装车单新赠商品，需要去重和新增，不操作订单商品集合
        if (type == 1) {
            ArrayList<ShopCarModel> cars = intent.getParcelableArrayListExtra("data");
            //转成申请的车销商品
            List<ApplyModel> applyModels = ModelHelper.ShopCarModel2ApplyModel(cars);
            //当前有的车销商品
            List<ApplyModel> carProduct = this.mCarProduct;
            //当前的装车单商品
            String logId = model.getId();
            //去重
            LinkedHashMap<String, ApplyModel> pros = new LinkedHashMap<>();
            for (ApplyModel apply : carProduct) {
                pros.put(apply.getProductId(), apply);
            }

            //遍历新增商品
            for (ApplyModel newApply : applyModels) {
                String productId = newApply.getProductId();
                if (pros.containsKey(productId)) {
                    ApplyModel exitApply = pros.get(productId);
                    int appNum = exitApply.getApplyNum() + newApply.getApplyNum();
                    exitApply.setApplyNum(appNum);
                } else {
                    //关联的装车单id
                    newApply.setLogId(logId);
                    //设置类型
                    //赠品，设置为新增赠品=3+4
                    if (newApply.getProductType() == ProductType.PRODUCT_GIFT) {
                        newApply.setProductType(StoreProductType.TYPE_UPDATE_PRODUCT_GIFT);
                    } else {
                        newApply.setProductType(StoreProductType.TYPE_UPDATE_PRODUCT);
                    }
                    pros.put(productId, newApply);
                }
            }
            //保存到车销商品中
            mCarProduct.clear();
            mCarProduct.addAll(pros.values());

            //更新显示
            List<RecyclerDataHolder> holders = new ArrayList<>();
            for (ApplyModel apply : mCarProduct) {
                RecyclerDataHolder holder = new StoreApplyUpdateDataHolder(apply);
                holders.add(holder);
            }
            //站位显示
            holders.add(new ProductChildDataHolder(null, null));
            mAdapter.setDataHolders(holders);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (model.getShstate() == 0) {
            getMenuInflater().inflate(R.menu.user_menu_store_apply_update, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (model != null) {
            AlertDialog.Builder dialog = DialogMaker.getDialog(this);
            dialog.setTitle(R.string.dialog_title);
            dialog.setMessage("确定更新当前装车单中的商品数量吗?");
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commit();
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }
        return true;
    }

    private void commit() {
        showProgressLoading("提交更新中");
        List<ApplyModel> carProduct = mCarProduct;
//        JSONArray array = new JSONArray();
//        for (ApplyModel apply : carProduct) {
//            JSONObject object = new JSONObject();
//            try {
//                object.put("applyNum", apply.getApplyNum());
//                object.put("productId", apply.getProductId());
//                object.put("productType", apply.getProductType());
//                object.put("logId", apply.getLogId());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
////            Logger.v("product:" + object.toString());
//            array.put(object);
//        }

//        //提交更新车销商品数据
        String toJson = new Gson().toJson(carProduct);
        ApiControl.getApi().storeApplyUpdate("1", toJson, new Callback.CommonCallback<ResponseModel<Object>>() {
            @Override
            public void onSuccess(ResponseModel<Object> result) {
                if (result.isSuccess()) {
                    SnackUtil.shortShow(mRecyclerView, "更新成功");

                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventHelper.postCommonEvent(CommonEvent.EVENT_REFRESH_STORE);
                            StoreApplyUpdateActivity.this.finish();
                        }
                    }, 800);

                } else {
                    SnackUtil.shortShow(mRecyclerView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mRecyclerView, ex.getMessage());
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
