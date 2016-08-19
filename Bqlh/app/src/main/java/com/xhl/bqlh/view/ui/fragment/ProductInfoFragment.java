package com.xhl.bqlh.view.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ProductAttachModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.ImageEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.ui.activity.ShopDetailsActivity;
import com.xhl.bqlh.view.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.xhl_library.Base.Anim.AnimType;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/7.
 */
@ContentView(R.layout.fragment_product_info)
public class ProductInfoFragment extends BaseAppFragment {

    @ViewInject(R.id.goods_image)//商品描述图片
    private ViewPager goods_image;

    @ViewInject(R.id.tv_product_name)//商品名称
    private TextView tv_product_name;

    @ViewInject(R.id.ll_login_price_content)
    private View ll_login_price_content;

    @ViewInject(R.id.tv_price_or)//商品价格
    private TextView tv_price_or;

    @ViewInject(R.id.tv_price_business)//商品价格
    private TextView tv_price_business;

    @ViewInject(R.id.tv_min_num)//商品起定量
    private TextView tv_min_num;

    @ViewInject(R.id.tv_active_hint)//商品价格
    private TextView tv_active_hint;

    @ViewInject(R.id.tv_num_cur)//商品当前位置
    private TextView tv_num_cur;

    @ViewInject(R.id.tv_num_max)//商品最多
    private TextView tv_num_max;

    @ViewInject(R.id.tv_product_yf)//配送
    private TextView tv_product_yf;

    @ViewInject(R.id.tv_product_service)//时间
    private TextView tv_product_service;

    @ViewInject(R.id.tv_shop_name)//店铺名称
    private TextView tv_shop_name;

    @ViewInject(R.id.tv_shop_user_name)//名称
    private TextView tv_shop_user_name;

    @ViewInject(R.id.tv_shop_user_phone)//手机
    private TextView tv_shop_user_phone;

    @ViewInject(R.id.tv_shop_user_area)//区域
    private TextView tv_shop_user_area;

    @ViewInject(R.id.tv_shop_collect)//
    private TextView tv_shop_collect;

    @ViewInject(R.id.tv_shop_collection)//收藏个数
    private TextView tv_shop_collection;

    @ViewInject(R.id.fl_goods_image_num)//商品个数显示
    private FrameLayout fl_goods_image_num;

    @Event(R.id.tv_shop_enter)
    private void onShopEnterClick(View view) {
        String storeId = mProduct.getStoreId();
        if (!TextUtils.isEmpty(storeId)) {
            Intent intent = new Intent(getContext(), ShopDetailsActivity.class);
            intent.putExtra("id", storeId);
            startActivity(intent);
        }
    }

    @Event(R.id.tv_shop_collect)
    private void onShopCollectClick(View view) {
        if (!AppDelegate.appContext.isLogin(getContext())) {
            return;
        }
        String storeId = mProduct.getStoreId();
        //1：店铺，2：商品
        ApiControl.getApi().collectAdd(storeId, "1", new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                Object obj = result.getObj();
                String res = (String) obj;
                if (res.equals("success")) {
                    ToastUtil.showToastShort("收藏成功");
                } else {
                    ToastUtil.showToastShort("已收藏");
                }

            }

            @Override
            public void finish() {

            }
        });
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    public static ProductInfoFragment instance(ProductModel productModel, ShopModel shop) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", productModel);
        bundle.putSerializable("shop", shop);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ProductModel mProduct;
    private ShopModel mShop;
    private ArrayList<String> mImageUrls;//大图路径

    @Override
    protected void initParams() {

        mProduct = (ProductModel) getArguments().getSerializable("data");
        mShop = (ShopModel) getArguments().getSerializable("shop");

        if (mProduct == null) {
            return;
        }
        //登陆后的展示
        if (AppDelegate.appContext.isLogin()) {
            ll_login_price_content.setVisibility(View.VISIBLE);
        } else {
            ll_login_price_content.setVisibility(View.GONE);
        }
        //零售价
        tv_price_or.setText(getString(R.string.price, mProduct.getOriginalPrice()));
        //批发价
        tv_price_business.setText(getString(R.string.price, mProduct.getBussinessPrice()));
        //最小起定量
        tv_min_num.setText(getString(R.string.product_min_mun_text, mProduct.getOrderMinNum()));

        showProductImage();
        //信息
        tv_product_name.setText(mProduct.getProductName());
        //活动提示
        if (TextUtils.isEmpty(mProduct.getProductActive())) {
            tv_active_hint.setVisibility(View.GONE);
        } else {
            tv_active_hint.setText(mProduct.getProductActive());
        }
        //配送时间
        tv_product_service.setText(getString(R.string.product_service_hint, mProduct.getPromiseTime()));

        shopInfo();
    }

    private void shopInfo() {
        if (mShop != null) {
            tv_shop_name.setText(mShop.getShopName());
            tv_shop_collection.setText(getString(R.string.shop_i_collection, mShop.getCollectNum()));
            tv_shop_user_name.setText(getResources().getString(R.string.shop_i_name, mShop.getLiableName()));
            tv_shop_user_phone.setText(getResources().getString(R.string.shop_i_phone, mShop.getLiablePhone()));
            tv_shop_user_area.setText(getResources().getString(R.string.shop_i_area, mShop.getRegion()));
        }
    }

    private void showProductImage() {
        List<ProductAttachModel> attachment = mProduct.getAttachmentList();
        if (attachment.size() <= 0) {
            return;
        }
        fl_goods_image_num.setVisibility(View.VISIBLE);
        tv_num_cur.setText("1");
        tv_num_max.setText(String.valueOf(attachment.size()));

        List<Fragment> images = new ArrayList<>();

        mImageUrls = new ArrayList<>();

        for (ProductAttachModel product : attachment) {
            //大图
            mImageUrls.add(product.getLargeFilePath());

            images.add(ProductFastImageFragment.instance(product.getLargeFilePath()));
//            images.add(ProductFastImageFragment.instance(product.getFilePath()));
        }

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        adapter.setFragments(images);
        goods_image.setAdapter(adapter);
        //切换动画
//        goods_image.setPageTransformer(true, new ZoomOutPageTransformer());
        //商品位置
        goods_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_num_cur.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onEvent(ImageEvent event) {
        int type = event.getType();
        //商品大图片展示
        if (type == ImageEvent.DEFAULT) {
            getSumContext().pushFragmentToBackStack(ImageShowFragment.class, mImageUrls, AnimType.ANIM_CENTER);
        } else if (type == ImageEvent.MULIT_IMAGE) {
            getSumContext().pushFragmentToBackStack(ImageShowFragment.class, event.getUrls());
        }
    }

}
