package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.Model.ProductBrandModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecyclerViewScrollStateListener;
import com.xhl.bqlh.business.view.helper.pub.PageScrollListener;
import com.xhl.bqlh.business.view.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.bqlh.business.view.ui.callback.ProductAnimListener;
import com.xhl.bqlh.business.view.ui.callback.SelectProductListener;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductBrandDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductChildDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 16/4/7.
 */
@ContentView(R.layout.fragment_product_select_child)
public class ProductSelectChildFragment extends BaseAppFragment implements RecycleViewCallBack, SelectProductListener, Callback.CommonCallback<ResponseModel<ProductModel>> {

    /**
     * 根据分类id查询
     */
    public static final int TYPE_ID = 0;

    /**
     * 搜索查询
     */
    public static final int TYPE_SEARCH = 1;
    /**
     * 扫码查询
     */
    public static final int TYPE_SCAN = 2;
    /**
     * 礼品查询
     */
    public static final int TYPE_GIFT = 4;

    /**
     * 我的车存数量可以勾选
     */
    public static final int TYPE_CAR = 3;

    public static final String TYPE = "data_type";

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mSwipeRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.card_view)
    private CardView mCardView;

    @ViewInject(R.id.tv_sku)
    private TextView tv_sku;

    @ViewInject(R.id.recycler_view_by_type)
    private RecyclerView mTypeRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;
    private SelectStateRecyclerAdapter mTypeAdapter;

    private String mCurBrandId = "";
    private boolean mIsQuerying = false;
    private boolean mHasAddNullProduct = false;
    private String mData;//搜索数据
    private String mShopId;//店铺id
    private int mType;//搜索类型
    //添加创建商品
    private HashMap<String, ShopCarModel> mMapCars;

    private ProductAnimListener mAnimListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductAnimListener) {
            mAnimListener = (ProductAnimListener) context;
        }
    }

    public static ProductSelectChildFragment newInstance(int type, String shopId) {
        ProductSelectChildFragment fragment = new ProductSelectChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(GlobalParams.Intent_shop_id, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {
        //获取参数
        this.mType = getArguments().getInt(TYPE, TYPE_CAR);
        this.mData = getArguments().getString("data");
        //店铺id
        this.mShopId = getArguments().getString(GlobalParams.Intent_shop_id);

        super.initRefreshStyle(mSwipeRefresh, SwipeRefreshLayoutDirection.BOTH);
        mPresent.setPageDefaultDo(false);//不处理分页逻辑

        //商品
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        PageScrollListener scrollListener = new PageScrollListener(manager);
        scrollListener.addStateListener(new RecyclerViewScrollListener());
        mRecyclerView.addOnScrollListener(scrollListener);

        //品牌
        mTypeAdapter = new SelectStateRecyclerAdapter(getContext());
        mTypeRecyclerView.setHasFixedSize(true);
        mTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mTypeRecyclerView.setAdapter(mTypeAdapter);

        mMapCars = new HashMap<>();
        loadData();
    }

    public void loadData() {
        if (mType == TYPE_ID) {
            queryBrand(mData);
            mSwipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(true);
                    }
                    query();
                }
            }, 500);

        } else if (mType == TYPE_SEARCH) {
            mCardView.setVisibility(View.GONE);
            search(mData);
        } else if (mType == TYPE_SCAN) {
            mCardView.setVisibility(View.GONE);
            scan(mData);
        } else if (mType == TYPE_GIFT) {
            mCardView.setVisibility(View.GONE);
            gift();
        } else if (mType == TYPE_CAR) {
            mCardView.setVisibility(View.GONE);
            mSwipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(true);
                    }
                    myCar();
                }
            }, 500);

        }
    }

    //加载品牌
    private void loadBrand(List<ProductBrandModel> data) {
        if (data.size() == 0) {
            mCardView.setVisibility(View.GONE);
        } else {
            mCardView.setVisibility(View.VISIBLE);
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        //添加全选按钮
        ProductBrandModel allModel = new ProductBrandModel();
        allModel.setBrandName("全部");
        allModel.setId("");
        ProductBrandDataHolder holder = new ProductBrandDataHolder(allModel, this);
        holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
        holders.add(holder);
        //添加查询条件
        for (ProductBrandModel mode : data) {
            holder = new ProductBrandDataHolder(mode, this);
            holders.add(holder);
        }
        //设置显示
        mTypeAdapter.setDataHolders(holders);
    }

    private void queryBrand(String id) {
        ApiControl.getApi().storeBrand(id, new DefaultCallback<ResponseModel<ProductBrandModel>>() {
            @Override
            public void success(ResponseModel<ProductBrandModel> result) {
                loadBrand(result.getObjList());
            }

            @Override
            public void finish() {

            }
        });
    }

    //我的车销商品
    private void myCar() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        ApiControl.getApi().storeCarQueryApply(getPageSize(), getPageIndex(), mShopId, new CommonCallback<ResponseModel<ApplyModel>>() {
            @Override
            public void onSuccess(ResponseModel<ApplyModel> result) {
                if (result.isSuccess()) {
                    List<ApplyModel> objList = result.getObjList();
                    loadProduct(ModelHelper.ApplyModel2ProductModel(objList, mShopId));
                    setTotalSize(result.getPageSize());
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
                mIsQuerying = false;
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    //分类查询
    private void query() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        String id = mData;
        String brandId = mCurBrandId;
        ApiControl.getApi().storeProduct(getPageSize(), getPageIndex(), mShopId, id, brandId, "", "", this);
    }

    /**
     * 搜索文本
     */
    public void search(String name) {
        mData = name;
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(true);
                }
                ApiControl.getApi().storeProduct(getPageSize(), getPageIndex(), mShopId, "", "", mData, "", ProductSelectChildFragment.this);
            }
        }, 300);
    }

    /**
     * 扫码
     */
    public void scan(String sku) {
        mData = sku;
        tv_sku.setVisibility(View.VISIBLE);
        tv_sku.setText("条形码:" + sku);
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(true);
                }
                ApiControl.getApi().storeProduct(getPageSize(), getPageIndex(), mShopId, "", "", "", mData, ProductSelectChildFragment.this);
            }
        }, 300);
    }

    public void gift() {
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(true);
                }
                loadGift();
            }
        }, 300);
    }

    private void loadGift() {
        ApiControl.getApi().giftQuery(new DefaultCallback<ResponseModel<GiftModel>>() {
            @Override
            public void success(ResponseModel<GiftModel> result) {
                List<GiftModel> objList = result.getObjList();
                loadProduct(ModelHelper.GitfModel2ProductModel(objList));
            }

            @Override
            public void finish() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 获取每个界面的购物车数据
     */
    public ArrayList<ShopCarModel> getCars() {
        ArrayList<ShopCarModel> cars = new ArrayList<>();

        Set<Map.Entry<String, ShopCarModel>> mapCars = this.mMapCars.entrySet();

        for (Map.Entry<String, ShopCarModel> car : mapCars) {
            ShopCarModel carValue = car.getValue();
            if (carValue.curNum != 0) {
                cars.add(carValue);
            }
        }
        return cars;
    }

    @Override
    public void onRefreshTop() {
        mHasAddNullProduct = false;
        if (mType == TYPE_ID) {
            query();
        } else if (mType == TYPE_SEARCH) {
            search(mData);
        } else if (mType == TYPE_CAR) {
            myCar();
        } else if (mType == TYPE_SCAN) {
            scan(mData);
        } else if (mType == TYPE_GIFT) {
            gift();
        }
    }

    @Override
    public void onRefreshMore() {
        if (mPresent.mPageIndex * mPresent.mPageSize >= mPresent.mTotalSize) {
            mSwipeRefresh.setRefreshing(false);
            if (!mHasAddNullProduct) {
                mHasAddNullProduct = true;
                mRecyclerAdapter.addDataHolder(new ProductChildDataHolder(null, null));
            }
            SnackUtil.longShow(mView, R.string.load_null);
            return;
        }
        mPresent.mPageIndex++;
        if (mType == TYPE_ID) {
            query();
        } else if (mType == TYPE_SEARCH) {
            search(mData);
        } else if (mType == TYPE_CAR) {
            myCar();
        } else if (mType == TYPE_SCAN) {
            scan(mData);
        } else if (mType == TYPE_GIFT) {
            if (!mHasAddNullProduct) {
                mHasAddNullProduct = true;
                mRecyclerAdapter.addDataHolder(new ProductChildDataHolder(null, null));
            }
            SnackUtil.longShow(mView, R.string.load_null);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mIsQuerying) {
            return;
        }
        mTypeAdapter.setSelectPosition(position);
        RecyclerDataHolder holder = mTypeAdapter.queryDataHolder(position);
        Object data = holder.getData();
        //点击品牌查询对应商品
        if (data != null && data instanceof ProductBrandModel) {
            ProductBrandModel brand = (ProductBrandModel) data;
            String brandId = brand.getId();
            if (!mCurBrandId.equals(brandId)) {
                //查询对应的品牌商品
                mCurBrandId = brandId;
                mPresent.mPageIndex = 1;
                mHasAddNullProduct = false;
                mSwipeRefresh.setRefreshing(true);
                query();
            }
        }
    }

    //回填购物车已有的数据
    private void refreshCarData(List<ProductModel> products) {
        //相同的商品id将内存中的数量添加的显示中
        for (ProductModel product : products) {
            String productId = product.getId();
            if (mMapCars.containsKey(productId)) {
                product.curNum = mMapCars.get(productId).curNum;
                product.checked = true;
            }
        }
    }

    //设置商品数据
    private void createProductData(List<ProductModel> products) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ProductModel product : products) {
            ProductChildDataHolder holder = new ProductChildDataHolder(product, this);
            holders.add(holder);
        }
        if (getPageIndex() == 1 && holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }

        if (getPageIndex() > 1) {
            mRecyclerAdapter.addDataHolder(holders);
        } else {
            mRecyclerAdapter.setDataHolders(holders);
        }
    }

    private void loadProduct(List<ProductModel> result) {

        if (mAnimListener != null && result.size() > 0) {
            mAnimListener.loadFinishAnim();
        }
        List<ProductModel> objList = result;
        refreshCarData(objList);
        createProductData(objList);
    }

    @Override
    public void onSuccess(ResponseModel<ProductModel> result) {
        if (result.isSuccess()) {
            setTotalSize(result.getPageSize());
            loadProduct(result.getObjList());
        } else {
            SnackUtil.shortShow(mView, result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        SnackUtil.shortShow(mView, ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mIsQuerying = false;
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onAddCar(ProductModel product, View view) {
        updateCar(product);
        if (mAnimListener != null) {
            ImageView anim = (ImageView) view;
            mAnimListener.startAnim(view, anim.getDrawable());
        }
    }

    @Override
    public void onReduceCar(ProductModel product) {
        updateCar(product);
    }

    private ShopCarModel updateCar(ProductModel product) {
        String id = product.getId();
        boolean containsKey = mMapCars.containsKey(id);
        ShopCarModel carModel;
        if (containsKey) {
            carModel = mMapCars.get(id);
            carModel.curNum = product.curNum;
        } else {
            carModel = ModelHelper.ProductModel2ShopCarModel(product, mShopId);
            mMapCars.put(id, carModel);
        }
        return carModel;
    }

    private class RecyclerViewScrollListener implements RecyclerViewScrollStateListener {
        @Override
        public void onScrollingUp() {
            if (mAnimListener != null) {
                mAnimListener.scrollUpAnim();
            }
        }

        @Override
        public void onScrollingDown() {
            if (mAnimListener != null) {
                mAnimListener.scrollDownAnim();
            }
        }

        @Override
        public void onScrollBottom() {

        }
    }
}
