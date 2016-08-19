package com.xhl.bqlh.business.doman;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.doman.callback.ContextValue;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by Sum on 16/4/27.
 */
public class ProductQueryHelper extends BaseValue {

    public static final int TYPE_SHOW_PRODUCT = 0;
    public static final int TYPE_SHOW_PRODUCT_ADD = 1;
    public static final int TYPE_SHOW_REFRESHING = 2;
    public static final int TYPE_SHOW_REFRESHING_FINISH = 3;
    public static final int TYPE_SHOW_NULL = 4;
    public static final int TYPE_SHOW_NULL_HIDE = 5;


    public static final int TYPE_GET_CONDITION = 11;

    private boolean mIsQuerying = false;

    private ProductQueryCondition mCondition;
    Callback.Cancelable mCarApply;

    public ProductQueryHelper(ContextValue value) {
        super(value);
    }

    public void onRefreshTop() {
        mCondition = (ProductQueryCondition) mValue.getValue(TYPE_GET_CONDITION);
        if (mCondition == null) {
            return;
        }
        mPageIndex = 1;
        int queryType = mCondition.queryType;
        //车销商品
        if (queryType == ProductQueryCondition.TYPE_CAR_PRODUCT) {
            loadCarData();
        }
    }

    public void onRefreshMore() {
        if (mCondition == null) {
            return;
        }
        mPageIndex++;
        int queryType = mCondition.queryType;
        //车销商品
        if (queryType == ProductQueryCondition.TYPE_CAR_PRODUCT) {
            loadCarData();
        }
    }

    //我的车销商品
    private void loadCarData() {
        if (mIsQuerying) {
            return;
        }
        mValue.showValue(TYPE_SHOW_REFRESHING, null);

        mIsQuerying = true;
        mCarApply = ApiControl.getApi().storeCarQueryApply(1000, mPageIndex, mCondition.shopId, new Callback.CommonCallback<ResponseModel<ApplyModel>>() {
            @Override
            public void onSuccess(ResponseModel<ApplyModel> result) {
                if (result.isSuccess()) {
                    List<ApplyModel> objList = result.getObjList();

                    showProduct(ModelHelper.ApplyModel2ProductModel(objList,null));
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
                mIsQuerying = false;
                showValue(TYPE_SHOW_REFRESHING_FINISH, null);
            }
        });
    }

    private void showProduct(List<ProductModel> products) {

        if (products.size() <= 0 && mPageIndex > 1) {
            toastShow("没有更多数据了");
            return;
        }
        if (mPageIndex == 1 && products.size() == 0) {
            showValue(TYPE_SHOW_NULL, null);
        } else {
            showValue(TYPE_SHOW_NULL_HIDE, null);
        }
        if (mPageIndex > 1) {
            showValue(TYPE_SHOW_PRODUCT_ADD, products);
        } else {
            showValue(TYPE_SHOW_PRODUCT, products);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCarApply != null) {
            mCarApply.cancel();
        }
    }
}
