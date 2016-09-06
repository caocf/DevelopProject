package com.xhl.bqlh.view.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AProductDetails;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.custom.MultiCheckBox;
import com.xhl.bqlh.view.custom.ProductOperatorBroad;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.bqlh.view.ui.fragment.HomeCarFragment;
import com.xhl.bqlh.view.ui.fragment.ProductExInfoFragment;
import com.xhl.bqlh.view.ui.fragment.ProductInfoFragment;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/2.
 */
@ContentView(R.layout.activity_details_product)
public class ProductDetailsActivity extends BaseAppActivity implements ProductOperatorBroad.OperatorListener {

    @ViewInject(R.id.tab_layout)
    private TabLayout tab_layout;

    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;

    @ViewInject(R.id.btn_op_2)//收藏状态
    private MultiCheckBox btn_op_2;

    @ViewInject(R.id.btn_op_3)//购物车
    private MultiCheckBox btn_op_3;

    @ViewInject(R.id.btn_op_4)
    private Button btn_op_4;

    @Event(R.id.btn_op_4) // 添加到购物车
    private void onAddShoppingCarClick(View view) {
        if (!AppDelegate.appContext.isLogin(this)) {
            return;
        }
        if (!mIsLoading) {
            ProductOperatorBroad broad = new ProductOperatorBroad(this, mDetails);
            broad.setListener(this);
            broad.show();
        }
    }

    @Event(R.id.btn_op_3) // 购物车
    private void onShoppingCarClick(View view) {
        if (AppDelegate.appContext.isLogin(this)) {
            pushFragmentToBackStack(HomeCarFragment.class, 1);
        }
    }

    @Event(R.id.btn_op_2)
    private void onCollectionClick(View view) {
        if (!AppDelegate.appContext.isLogin(this)) {
            return;
        }
        if (isCollectionProduct) {
            ToastUtil.showToastShort("已关注");
        } else {
            //1：店铺，2：商品
            ApiControl.getApi().collectAdd(mProductId, "2", new DefaultCallback<ResponseModel<Object>>() {
                @Override
                public void success(ResponseModel<Object> result) {
                    collectionState(true);
                }

                @Override
                public void finish() {

                }
            });
        }
    }

    //根据收藏状态显示
    private void collectionState(boolean collected) {
        isCollectionProduct = collected;
        if (collected) {
            btn_op_2.setText("已关注");
            btn_op_2.setImageRes(R.drawable.icon_p_collectioned);
        } else {
            btn_op_2.setText("关注");
            btn_op_2.setImageRes(R.drawable.icon_p_collection);
        }
    }

    @Event(R.id.btn_op_1)
    private void onHomeClick(View view) {
        if (mDetails == null) {
            return;
        }
        String storeId = mDetails.getStoreId();
        if (!TextUtils.isEmpty(storeId)) {
            Intent intent = new Intent(this, ShopDetailsActivity.class);
            intent.putExtra("id", storeId);
            startActivity(intent);
        }
    }

    @Event(R.id.fl_back)
    private void onBackClick(View view) {
        if (!mIsLoading) {
            finish();
        }
    }

    private boolean isCollectionProduct = false;
    private boolean mIsLoading = false;

    private List<Fragment> mFragments;
    private String mProductId, mStoreId;

    private ProductModel mDetails;

    @Override
    protected void initParams() {
        mProductId = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(mProductId)) {
            ToastUtil.showToastLong("产品数据异常");
            finish();
            return;
        }

        super.initBadgeView(btn_op_3);

        onRefreshLoadData();

        int colorNor = ContextCompat.getColor(this, R.color.main_check_color_nor);
        int colorSelect = ContextCompat.getColor(this, R.color.main_check_color_select);

        tab_layout.setTabTextColors(colorNor, colorSelect);
        btn_op_4.setEnabled(false);
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        mIsLoading = true;

        showLoadingDialog();

        ApiControl.getApi().productDetails(mProductId, new DefaultCallback<ResponseModel<AProductDetails>>() {
            @Override
            public void success(ResponseModel<AProductDetails> result) {
                btn_op_4.setEnabled(true);
                AProductDetails obj = result.getObj();
                mDetails = obj.getProduct();
                loadInfo(obj);
                networkErrorHide();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
                Logger.e("onError:" + ex.getMessage());
            }

            @Override
            public void finish() {
                hideLoadingDialog();
                mIsLoading = false;
            }
        });
    }

    private void loadInfo(AProductDetails details) {
        //零售商
        mStoreId = details.getProduct().getStoreId();
        //设置数量
        GlobalCarInfo.setProductNum(details.getProduct());

        mFragments = new ArrayList<>();
        //基础商品信息
        mFragments.add(ProductInfoFragment.instance(details.getProduct(), details.getShop()));
        //扩展信息
        mFragments.add(ProductExInfoFragment.instance(details.getSkuInfo(), details.getProduct().getProductDesc()));

        view_pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tab_layout.setupWithViewPager(view_pager);

        //收藏状态
        if (details.getProduct().getProductCollected() > 0) {
            isCollectionProduct = true;
        } else {
            isCollectionProduct = false;
        }
        collectionState(isCollectionProduct);
    }

    @Override
    public void onAddClick(int num) {
        //购物车中是否已经包含该商品
        boolean containsId = GlobalCarInfo.instance().containsId(mProductId);
        int fixNum = num;
        if (containsId) {
            //已经存在的数量
            String carNum = GlobalCarInfo.instance().getCarNum(mProductId);
            fixNum = num - Integer.valueOf(carNum);
        }
        showLoadingDialog();

        ApiControl.getApi().carAdd(mProductId, mStoreId, fixNum, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                GlobalCarInfo.instance().putCarInfo(mDetails);
                ToastUtil.showToastLong(R.string.product_op_add_success);

                EventHelper.postReLoadCarEvent();
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "商品";
                case 1:
                    return "详情";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    public void onEvent(CommonEvent event) {
        //用户登录成功刷新数据
        if (event.getEventTag() == CommonEvent.ET_RELOAD_USER_INFO) {
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        }
    }
}
