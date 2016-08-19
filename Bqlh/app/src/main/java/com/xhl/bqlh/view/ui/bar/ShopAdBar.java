package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.bqlh.view.ui.fragment.ShopAdImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopAdBar extends BaseBar{

    public ShopAdBar(Context context) {
        super(context);
    }

    @Override
    protected boolean autoInject() {
        return false;
    }

    private ViewPager pageView;
    private boolean mLoadSuccess = false;

    @Override
    protected void initParams() {
        pageView = _findViewById(R.id.ad_image);
    }

    public void onBindData(String shopId) {
        if (!mLoadSuccess) {
            mLoadSuccess = true;
            ApiControl.getApi().shopAdInfo(shopId, new DefaultCallback<ResponseModel<AShopDetails>>() {
                @Override
                public void success(ResponseModel<AShopDetails> result) {
                    List<AdInfoModel> carousel = result.getObj().getCarousel();
                    if (carousel == null || carousel.size() == 0) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nullDo();
                            }
                        }, 200);
                    } else {
                        loadData(carousel);
                    }
                }

                @Override
                public void finish() {

                }
            });
        }
    }

    private void nullDo() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = 0;
        layoutParams.height = 0;
        setLayoutParams(layoutParams);
    }


    private void loadData(List<AdInfoModel> data) {

        try {
            List<Fragment> images = new ArrayList<>();
            for (AdInfoModel ads : data) {
                images.add(ShopAdImageFragment.instance(ads));
            }
            Context context = getContext();
            FragmentActivity activity = (FragmentActivity) context;
            FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(activity.getSupportFragmentManager());
            adapter.setFragments(images);
            pageView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_shop_ad;
    }
}
