package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.CategoryItemModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductParentDataHolder;
import com.xhl.xhl_library.ui.FragmentCacheManager;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/7.
 */
@ContentView(R.layout.fragment_product_select_parent)
public class ProductSelectParentFragment extends BaseAppFragment implements RecycleViewCallBack {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecycleView;

    private SelectStateRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private FragmentCacheManager mFragmentCacheManager;

    private boolean mHasAddSearch = false;
    private boolean mHasAddScan = false;
    private boolean mLoadCategory = false;

    private int mSearchIndex, mSearchAdapterIndex;
    private int mScanIndex, mScanAdapterIndex;
    private ProductSelectChildFragment mSearch, mScan;

    private String mShopId;

    public static ProductSelectParentFragment newInstance(String shopId) {
        ProductSelectParentFragment fragment = new ProductSelectParentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.Intent_shop_id, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {
        //店铺id
        mShopId = getArguments().getString(GlobalParams.Intent_shop_id);

        mFragmentCacheManager = new FragmentCacheManager();
        mFragmentCacheManager.setUp(this, R.id.fl_content);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new SelectStateRecyclerAdapter(getContext());
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mRecyclerAdapter);
        loadCategory();
    }

    //添加搜索文本
    public void addSearchData(String name) {
        if (!mLoadCategory) {
            return;
        }
        if (!mHasAddSearch) {
            mHasAddSearch = true;

            CategoryItemModel model = new CategoryItemModel();
            model.setId("search");
            model.setCategoryName("搜索");
            model.index = mRecyclerAdapter.getItemCount();

            ProductParentDataHolder holder = new ProductParentDataHolder(model);
            holder.setCallBack(this);
            mRecyclerAdapter.addDataHolder(0, holder);
            if (mHasAddScan) {
                mScanAdapterIndex = 1;
            }
            mSearchAdapterIndex = 0;

            Bundle bundle = new Bundle();
            bundle.putString("data", name);
            bundle.putString(GlobalParams.Intent_shop_id, mShopId);
            bundle.putInt(ProductSelectChildFragment.TYPE, ProductSelectChildFragment.TYPE_SEARCH);

            mFragmentCacheManager.addFragmentToCache(model.index, ProductSelectChildFragment.class, model.getId(), bundle);
            mSearchIndex = model.index;
        } else {
            if (mSearch == null) {
                mSearch = (ProductSelectChildFragment) mFragmentCacheManager.getCacheFragment(mSearchIndex);
            }
            if (mSearch != null) {
                mSearch.search(name);
            }
        }
        onItemClick(mSearchAdapterIndex);
    }

    //添加扫码文本
    public void addScanData(String sku) {
        if (!mLoadCategory) {
            return;
        }
        if (!mHasAddScan) {
            mHasAddScan = true;
            CategoryItemModel model = new CategoryItemModel();
            model.setId("scan");
            model.setCategoryName("扫码");
            model.index = mRecyclerAdapter.getItemCount();

            ProductParentDataHolder holder = new ProductParentDataHolder(model);
            holder.setCallBack(this);
            mRecyclerAdapter.addDataHolder(0, holder);
            if (mHasAddSearch) {
                mSearchAdapterIndex = 1;
            }
            mScanAdapterIndex = 0;
            Bundle bundle = new Bundle();
            bundle.putString("data", sku);
            bundle.putString(GlobalParams.Intent_shop_id, mShopId);
            bundle.putInt(ProductSelectChildFragment.TYPE, ProductSelectChildFragment.TYPE_SCAN);

            mFragmentCacheManager.addFragmentToCache(model.index, ProductSelectChildFragment.class, model.getId(), bundle);
            mScanIndex = model.index;
        } else {
            if (mScan == null) {
                mScan = (ProductSelectChildFragment) mFragmentCacheManager.getCacheFragment(mScanIndex);
            }
            if (mScan != null) {
                mScan.scan(sku);
            }
        }
        onItemClick(mScanAdapterIndex);
    }


    /**
     * @return 获取全部购物车数据
     */
    public ArrayList<ShopCarModel> getShopCars() {
        List<Fragment> fragments = mFragmentCacheManager.getAllCacheFragment();
        ArrayList<ShopCarModel> cars = new ArrayList<>();
        for (Fragment f : fragments) {
            ProductSelectChildFragment child = (ProductSelectChildFragment) f;
            cars.addAll(child.getCars());
        }
        return cars;
    }

    //获取分类
    public void loadCategory() {

        showLoadingDialog();

        //加载时重置状态
        mHasAddSearch = false;
        mHasAddScan = false;
        mLoadCategory = false;

        ApiControl.getApi().storeCategory(mShopId, new DefaultCallback<ResponseModel<CategoryItemModel>>() {
            @Override
            public void success(ResponseModel<CategoryItemModel> result) {
                createCategory(result.getObjList());
                mLoadCategory = true;
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });

    }

    //创建分类
    private void createCategory(List<CategoryItemModel> models) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        //添加赠品数据
        CategoryItemModel model = new CategoryItemModel();
        model.setId("gift");
        model.setCategoryName("赠品");
        models.add(model);
        boolean isFirst = true;
        for (CategoryItemModel mode : models) {
            ProductParentDataHolder holder = new ProductParentDataHolder(mode);
            holder.setCallBack(this);
            if (isFirst) {
                isFirst = false;
                holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
            }
            holders.add(holder);
        }
        //设置显示
        mRecyclerAdapter.setDataHolders(holders);

        createChildManager(models);
    }

    //创建导航栏数据
    private void createChildManager(List<CategoryItemModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CategoryItemModel mode = list.get(i);
            Bundle bundle = new Bundle();
            bundle.putString("data", mode.getId());
            bundle.putString(GlobalParams.Intent_shop_id, mShopId);
            if (i != size - 1) {
                bundle.putInt(ProductSelectChildFragment.TYPE, ProductSelectChildFragment.TYPE_ID);
            } else {
                //最后一个是赠品
                bundle.putInt(ProductSelectChildFragment.TYPE, ProductSelectChildFragment.TYPE_GIFT);
            }
            //默认索引对应集合的位置
            mode.index = i;

            mFragmentCacheManager.addFragmentToCache(mode.index, ProductSelectChildFragment.class, mode.getId(), bundle);
        }
        mFragmentCacheManager.setCurrentFragment(0);
    }

    @Override
    public void onItemClick(int position) {
        mRecyclerAdapter.setSelectPosition(position);

        RecyclerDataHolder holder = mRecyclerAdapter.queryDataHolder(position);
        Object data = holder.getData();
        if (data instanceof CategoryItemModel) {
            CategoryItemModel itemModel = (CategoryItemModel) data;
            mFragmentCacheManager.setCurrentFragment(itemModel.index);
        }
    }
}
