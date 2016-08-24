package com.xhl.world.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.InitReturnOrderModel;
import com.xhl.world.ui.fragment.OrderReturnManagerFragment;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/19.
 */
@ContentView(R.layout.activity_manager_return_order)
public class OrderReturnManagerActivity extends BaseAppActivity {

    @ViewInject(R.id.container)
    private ViewPager mViewPager;

    @ViewInject(R.id.tabs)
    private TabLayout tabLayout;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    @Override
    protected void initParams() {
        title_name.setText("退货管理");
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int colorNor = ContextCompat.getColor(this, R.color.main_check_text_color);
        int colorSelect = ContextCompat.getColor(this, R.color.main_check_color);
        tabLayout.setTabTextColors(colorNor, colorSelect);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        showLoadingDialog();

        ApiControl.getApi().orderReturnInit(new Callback.CommonCallback<ResponseModel<InitReturnOrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<InitReturnOrderModel> result) {
                if (result.isSuccess()) {
                    tabLayout.setVisibility(View.VISIBLE);
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), result.getResultObj());
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    tabLayout.setupWithViewPager(mViewPager);
                } else {
                    SnackMaker.shortShow(mViewPager, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mViewPager, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private InitReturnOrderModel returnOrder;

        public SectionsPagerAdapter(FragmentManager fm, InitReturnOrderModel order) {
            super(fm);
            returnOrder = order;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return OrderReturnManagerFragment.instance(0, returnOrder);
                }
                case 1: {
                    return OrderReturnManagerFragment.instance(1, returnOrder);
                }
                case 2: {
                    return OrderReturnManagerFragment.instance(2, returnOrder);
                }
                case 3: {
                    return OrderReturnManagerFragment.instance(3, returnOrder);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.order_return_apply, returnOrder.getSize1());
                case 1:
                    return getResources().getString(R.string.order_return_doing, returnOrder.getSize2());
                case 2:
                    return getResources().getString(R.string.order_return_refuse, returnOrder.getSize3());
                case 3:
                    return getResources().getString(R.string.order_return_finish, returnOrder.getSize4());
            }
            return null;
        }
    }
}
