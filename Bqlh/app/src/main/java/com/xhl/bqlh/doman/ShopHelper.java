package com.xhl.bqlh.doman;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.callback.ContextValue;
import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.model.BrandModel;
import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.app.SearchParams;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.ui.recyclerHolder.ClassifyChildDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.SearchBrandDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.SearchProductDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.ShopAdBarDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.ShopInfoBarDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.ShopOpBarDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopHelper extends BaseValue implements Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>> {

    //获取店铺id
    public static final int TYPE_GET_SHOP_ID = 1;

    public static final int TYPE_GET_BRAND_ID = 8;

    public static final int TYPE_RES_ALL_DATA = 2;//店铺数据

    public static final int TYPE_RES_ALL_DATA_MORE = 7;//店铺数据

    public static final int TYPE_RES_FILTER_CATEGORY_DATA = 3;//过滤数据

    public static final int TYPE_RES_FILTER_BRAND_DATA = 8;//过滤数据

    public static final int TYPE_RES_FILTER_OPEN = 4;

    public static int TYPE_RES_SHOW_LOADING = 5;

    public static int TYPE_RES_HIED_LOADING = 6;

    //品牌数据
    private List<RecyclerDataHolder> mBrands;
    private List<RecyclerDataHolder> mCreatory;
    private String mShopId;

    private SearchParams mCondition;

    public ShopHelper(ContextValue value) {
        super(value);
    }

    @Override
    public void onCreate() {
        mShopId = (String) mValue.getValue(TYPE_GET_SHOP_ID);
        mCondition = new SearchParams();
        mCondition.shopId = mShopId;
        mCondition.brandId = (String) mValue.getValue(TYPE_GET_BRAND_ID);
        loadShopInfo();
    }

    //分类
    public void onShopCategoryClick() {

        loadShopCategory();

        showValue(TYPE_RES_FILTER_OPEN, null);
    }

    //品牌
    public void onShopBrandClick() {
        showValue(TYPE_RES_FILTER_BRAND_DATA, mBrands);

        showValue(TYPE_RES_FILTER_OPEN, null);
    }

    public void onShopServiceClick() {
        ToastUtil.showToastShort(R.string.building);
    }

    public void onShopProductClick() {

    }

    public void onRefreshTop() {
        mPageIndex = 1;
        loadShopInfo();
    }

    public void onRefreshMore() {
        if (mPageIndex * mPageSize >= mTotalSize) {
            showValue(TYPE_RES_HIED_LOADING, null);
            showValue(TYPE_NO_MORE, null);
        } else {
            mPageIndex++;
            loadShopProduct();
        }
    }

    //店铺信息
    private void loadShopInfo() {

        loadingShow();

        ApiControl.getApi().shopInfo(mShopId, new Callback.CommonCallback<ResponseModel<AShopDetails>>() {
            @Override
            public void onSuccess(ResponseModel<AShopDetails> result) {
                if (result.isSuccess()) {
                    showShopInfo(result.getObj());
                } else {
                    toastShow(result.getMessage());
                    dialogHide();
                    showValue(TYPE_RES_HIED_LOADING, null);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toastShow(ex.getMessage());
                Logger.v(ex.getMessage());
                dialogHide();
                showValue(TYPE_RES_HIED_LOADING, null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void showShopInfo(AShopDetails details) {

        //品牌数据
        mBrands = new ArrayList<>();
        List<BrandModel> brand = details.getAllinfo().getBrand();
        for (BrandModel b : brand) {
            mBrands.add(new SearchBrandDataHolder(b));
        }

        //分段信息
        List<RecyclerDataHolder> shopHolder = new ArrayList<>();

        //头部信息
        shopHolder.add(new ShopInfoBarDataHolder(details));
        //操作信息
        shopHolder.add(new ShopOpBarDataHolder(null));

        //广告信息
        shopHolder.add(new ShopAdBarDataHolder(mShopId));
        //商品tip
//        shopHolder.add(new ProductTip(null));
        //商品信息
        loadShopProduct();
        //所有数据
        showValue(TYPE_RES_ALL_DATA, shopHolder);
        //默认侧滑显示品牌数据
        showValue(TYPE_RES_FILTER_BRAND_DATA, mBrands);

    }

    private void loadShopProduct() {

        mCondition.pageSize = mPageSize;

        mCondition.pageNum = mPageIndex;

        ApiControl.getApi().searchProduct(mCondition, this);
    }


    private void loadShopCategory() {
        if (mCreatory != null) {
            showValue(TYPE_RES_FILTER_CATEGORY_DATA, mCreatory);
        } else {
            loadingShow();
            ApiControl.getApi().shopCategoryInfo(mShopId, new Callback.CommonCallback<ResponseModel<GarbageModel>>() {
                @Override
                public void onSuccess(ResponseModel<GarbageModel> result) {
                    if (result.isSuccess()) {
                        List<ClassifyModel> cateList = result.getObj().getCategoryList();
                        loadCategory(cateList);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Logger.e(ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    dialogHide();
                }
            });
        }
    }

    private void loadCategory(List<ClassifyModel> data) {

        //组合一级分类
        List<ClassifyModel> firstData = new ArrayList<>();
        for (ClassifyModel cP : data) {
            firstData.addAll(cP.getChildren());
        }

        //显示分类
        List<RecyclerDataHolder> holders = new ArrayList<>();
        int index = 0;
        for (ClassifyModel classify : firstData) {
            int size = classify.getChildren().size();
            if (size > 0) {
                index++;
                classify.shopId = mShopId;
                holders.add(new ClassifyChildDataHolder(classify, index));
            }
        }
        showValue(TYPE_RES_FILTER_CATEGORY_DATA, holders);
        mCreatory = holders;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSuccess(ResponseModel<GarbageModel<ProductModel>> result) {
        if (result.isSuccess()) {
            List<ProductModel> resultData = result.getObj().getResultData();
            mTotalSize = result.getObj().getTotalResult();
            loadData(resultData);
        }
    }

    private void loadData(List<ProductModel> data) {
        if (data == null) {
            return;
        }
        List<RecyclerDataHolder> orders = new ArrayList<>();

        for (ProductModel pro : data) {
            orders.add(new SearchProductDataHolder(pro));
        }
        showValue(TYPE_RES_ALL_DATA_MORE, orders);
    }


    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        toastShow(ex.getMessage());
        Logger.e(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        dialogHide();
        showValue(TYPE_RES_HIED_LOADING, null);
    }
}
