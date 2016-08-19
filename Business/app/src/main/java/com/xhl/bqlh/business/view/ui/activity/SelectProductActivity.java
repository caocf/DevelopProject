package com.xhl.bqlh.business.view.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.barcode.ui.CaptureActivity;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.AddCarAnimHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.callback.ProductAnimListener;
import com.xhl.bqlh.business.view.ui.fragment.ProductSelectParentFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Sum on 16/3/23.
 */

@ContentView(R.layout.activity_select_shop_product)
public class SelectProductActivity extends BaseAppActivity implements ProductAnimListener {

    @ViewInject(R.id.car_fab)
    private FloatingActionButton mCar;

    @Event(R.id.car_fab)
    private void onCarClick(View view) {
        ArrayList<ShopCarModel> shopCars = mParentFragment.getShopCars();

        if (shopCars.size() > 0) {
            Intent intent = new Intent(SelectProductActivity.this, ConfirmProductActivity.class);
            //选择商品操作类型
            intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, getIntent().getIntExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, 0));
            intent.putParcelableArrayListExtra("data", shopCars);
            intent.putExtra(GlobalParams.Intent_shop_id, mShopId);
            startActivity(intent);
        } else {
            SnackUtil.shortShow(view, R.string.car_null);
        }
    }

    private ProductSelectParentFragment mParentFragment;

    private SearchView mSearch;
    private String mShopId;
    private AddCarAnimHelper mAnimHelper;
    private int[] mViewPos = new int[2];
    private static final int ANIM_DURATION = 1000;

    private boolean mIsNeedScan = false;
    private String mSKU;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle("选择商品");
        //店铺id
        String shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        mShopId = shopId;
        mParentFragment = ProductSelectParentFragment.newInstance(shopId);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mParentFragment).commit();
        mAnimHelper = new AddCarAnimHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_select_product, menu);

        SearchView search = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        search.setQueryHint("搜索商品名称");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearch = search;
        return true;
    }

    private void query(String text) {
        ViewHelper.KeyBoardHide(mSearch);
        mParentFragment.addSearchData(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scan) {
            startActivityForResult(new Intent(this, CaptureActivity.class), 1);
            return true;
        }
        if (item.getItemId() == R.id.menu_refresh) {
            mParentFragment.loadCategory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            mSKU = data.getStringExtra(CaptureActivity.BARCODE);
            mIsNeedScan = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsNeedScan) {
            mIsNeedScan = false;
            mParentFragment.addScanData(mSKU);
        }
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


    @Override
    public void scrollUpAnim() {
      /*  int offset = getResources().getDimensionPixelOffset(R.dimen.pub_dimen_16dp);
        Logger.v("mCarY :" + mCarY + " offset:" + offset);
        ObjectAnimator mUpAnim = ObjectAnimator.ofFloat(mCar, View.TRANSLATION_Y, 0, mCarY + offset);
        mUpAnim.setDuration(1000);
        mUpAnim.start();*/
    }

    @Override
    public void scrollDownAnim() {
    /*    Logger.v("mCarY :" + mCarY);
        int offset = getResources().getDimensionPixelOffset(R.dimen.pub_dimen_16dp);

        ObjectAnimator mDownAnim = ObjectAnimator.ofFloat(mCar, "translationY", 0, -offset);
        mDownAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mDownAnim.setDuration(1000);
        mDownAnim.start();*/
    }


    @Override
    public void startAnim(View view, Drawable drawable) {
        if (view != null) {
            view.getLocationInWindow(mViewPos);
            mAnimHelper.setStartPos(mViewPos).setDefaultView(drawable).setTargetView(mCar).startProperty();
        }
    }
}
