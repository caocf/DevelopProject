package com.xhl.bqlh.business.doman;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBackWithData;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductConfirmNewDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductTypeDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/5/9.
 */
public class StoreApplyAddHelper extends BaseValue implements Callback.CommonCallback<ResponseModel<ApplyModel>>, RecycleViewCallBackWithData {

    //最后显示的集合
    public static final int TYPE_SHOW_RES = 1;
    public static final int TYPE_SHOW_REFRESH_TRUE = 2;
    public static final int TYPE_SHOW_REFRESH_FALSE = 3;
    public static final int TYPE_SHOW_REMOVE = 4;//删除一个数据

    private List<ProductModel> mCarLeftProducts;

    private List<ShopCarModel> mCarProducts;

    private List<ProductModel> mOrderProducts;

    private boolean mIsLoading = false;
    Callback.Cancelable mCancel;

    public StoreApplyAddHelper(ContextValue value) {
        super(value);
        mCarProducts = new ArrayList<>();
    }

    public String orderIds() {
        return "";
    }

    //所有装车商品
    public List<ApplyModel> getAllApplyProducts() {
        List<ApplyModel> applyModels = new ArrayList<>();
        //订单商品
        if (mOrderProducts != null) {
            for (ProductModel car : mOrderProducts) {
                ApplyModel apply = new ApplyModel();
                apply.setApplyNum(car.getStock());//申请的数量
                apply.setProductId(car.getId());//申请的商品
                apply.setProductType(1);//订单商品
                apply.setUnitPrice(car.getProductPrice().floatValue());
                apply.setSkuResult(car.getSkuResult());
                applyModels.add(apply);
            }
        }
        //新增车销商品
        if (mCarProducts != null) {
            for (ShopCarModel car : mCarProducts) {
                if (car.curNum == 0) {
                    continue;
                }
                ApplyModel apply = new ApplyModel();
                apply.setApplyNum(car.curNum);//申请的数据
                apply.setProductId(car.productId);//申请的商品
                if (car.productType == ProductType.PRODUCT_GIFT) {
                    apply.setProductType(4);//车销赠品
                } else {
                    apply.setProductType(2);//车销商品
                }
                apply.setUnitPrice(car.productPrice);
                apply.setSkuResult(ModelHelper.ShopCarModel2SkuModel(car));
                applyModels.add(apply);
            }
        }

        return applyModels;
    }

    //我的车销商品
    public void loadCarLeftData() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        mCancel = ApiControl.getApi().storeCarQueryApply(5000, 0, "", this);
    }

    //订单商品（已过滤重复）
    public void loadOrderProducts(List<ProductModel> models) {
        mOrderProducts = models;
        showData();
    }

    //新增车销商品
    public void loadCarProducts(List<ShopCarModel> models) {
        ModelHelper.mergeShopCarModel(mCarProducts, models);
        showData();
    }


    private void showData() {

        List<RecyclerDataHolder> holders = new ArrayList<>();

        //订单商品
        if (mOrderProducts != null && mOrderProducts.size() > 0) {
            holders.add(new ProductTypeDataHolder("订单商品"));
            for (ProductModel pro : mOrderProducts) {
                holders.add(new ProductDataHolder(pro));
            }
        }

        //车销新增商品
        if (mCarProducts != null && mCarProducts.size() > 0) {
            holders.add(new ProductTypeDataHolder("车销 -- 新增商品"));

            for (ShopCarModel pro : mCarProducts) {
                holders.add(new ProductConfirmNewDataHolder(pro, this));
            }
        }

        //车销商品
        if (mCarLeftProducts != null && mCarLeftProducts.size() > 0) {
            holders.add(new ProductTypeDataHolder("车销 -- 剩余商品"));

            for (ProductModel pro : mCarLeftProducts) {
                holders.add(new ProductDataHolder(pro));
            }
        }
        //显示数据
        showValue(TYPE_SHOW_RES, holders);
    }

    @Override
    public void onSuccess(ResponseModel<ApplyModel> result) {
        if (result.isSuccess()) {
            mCarLeftProducts = ModelHelper.ApplyModel2ProductModel(result.getObjList(), null);
            showData();
        } else {
            toastShow(result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        toastShow(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
    }

    @Override
    public void onFinished() {
        mIsLoading = false;
        showValue(TYPE_SHOW_REFRESH_FALSE, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCancel != null) {
            mCancel.cancel();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClickData(Object o) {
        mCarProducts.remove(o);
        showData();
    }
}
