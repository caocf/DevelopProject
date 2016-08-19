package com.xhl.bqlh.business.view.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.AddCarAnimHelper;
import com.xhl.bqlh.business.view.ui.callback.ProductAnimListener;
import com.xhl.bqlh.business.view.ui.fragment.ProductSelectChildFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Sum on 16/4/20.
 * 车销下单商品
 */
@ContentView(R.layout.activity_select_car_product)
public class SelectCarProductActivity extends BaseAppActivity implements ProductAnimListener {

    @ViewInject(R.id.car_fab)
    private FloatingActionButton mCar;

    @Event(R.id.car_fab)
    private void onCarClick(View view) {

        ArrayList<ShopCarModel> shopCars = mCarFragment.getCars();
        if (shopCars.size() > 0) {
            Intent intent = new Intent(this, ConfirmProductActivity.class);
            intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_ORDER_STORE);
            intent.putParcelableArrayListExtra("data", shopCars);
            intent.putExtra(GlobalParams.Intent_shop_id, mShopId);
            startActivity(intent);
        } else {
            SnackUtil.shortShow(view, R.string.car_null);
        }
    }

    private AddCarAnimHelper mAnimHelper;
    private int[] mViewPos = new int[2];

    private static final int ANIM_DURATION = 1000;

    private ProductSelectChildFragment mCarFragment;
    private String mShopId;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.shop_detail_order_store);

        String shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        mShopId = shopId;

        mAnimHelper = new AddCarAnimHelper(this);
        mCarFragment = ProductSelectChildFragment.newInstance(ProductSelectChildFragment.TYPE_CAR, shopId);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mCarFragment).commit();
    }

    @Override
    public void loadFinishAnim() {
        if (mCar.getVisibility() == View.GONE) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mCar, View.SCALE_X, 0, 1f);
            scaleX.setInterpolator(new BounceInterpolator());
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mCar, View.SCALE_Y, 0, 1f);
            scaleY.setInterpolator(new BounceInterpolator());
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mCar, View.ALPHA, 0.8f, 1f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY, alpha);
            animatorSet.setDuration(ANIM_DURATION);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mCar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.start();
        }
    }

    /**
     * 上滚动画
     */
    @Override
    public void scrollUpAnim() {

    }

    /**
     * 下滚动画
     */
    @Override
    public void scrollDownAnim() {

    }

    /**
     * 添加商品动画
     *
     * @param view
     * @param drawable
     */
    @Override
    public void startAnim(View view, Drawable drawable) {
        if (view != null) {
            view.getLocationInWindow(mViewPos);
            mAnimHelper.setStartPos(mViewPos).setDefaultView(drawable).setTargetView(mCar).startProperty();
        }
    }

}
