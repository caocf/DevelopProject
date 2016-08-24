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
import com.xhl.world.ui.fragment.OrderManagerFragment;
import com.xhl.world.ui.view.pub.ZoomOutPageTransformer;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/31.
 */
@ContentView(R.layout.activity_manager_order)
public class OrderManagerActivity extends BaseAppActivity {

    public static final String Tag_key = "tag_key";
    public static final int Tag_key_all = 0;
    public static final int Tag_key_wait_for_pay = 1;
    public static final int Tag_key_wait_for_sender = 2;
    public static final int Tag_key_wait_for_take = 3;
    public static final int Tag_key_wait_for_judge = 4;

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

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mTagKey;
    private List<OrderManagerFragment> mOrderFragments;

    @Override
    protected void initParams() {
        title_name.setText(R.string.order_my);
        mOrderFragments = new ArrayList<>();
        /**
         * 1    查询全部
         * 2    查询未付款
         * 3    查询待发货
         * 4    查询待收货
         * 5    查询未评价的订单
         * 6    查询退货订单
         */
        mOrderFragments.add(OrderManagerFragment.instance(1));
        mOrderFragments.add(OrderManagerFragment.instance(2));
        mOrderFragments.add(OrderManagerFragment.instance(3));
        mOrderFragments.add(OrderManagerFragment.instance(4));
        mOrderFragments.add(OrderManagerFragment.instance(5));
    }

    @Override
    protected boolean needEventBusRegister() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagKey = getIntent().getIntExtra(Tag_key, 0);

        int colorNor = ContextCompat.getColor(this, R.color.main_check_text_color);
        int colorSelect = ContextCompat.getColor(this, R.color.main_check_color);

        tabLayout.setTabTextColors(colorNor, colorSelect);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mOrderFragments);
        //切换动画
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);


        mViewPager.setCurrentItem(mTagKey, true);
    }

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<OrderManagerFragment> mFragments;

        public SectionsPagerAdapter(FragmentManager fm, List<OrderManagerFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.order_all);
                case 1:
                    return getResources().getString(R.string.order_my_wait_for_pay);
                case 2:
                    return getResources().getString(R.string.order_my_wait_for_sender);
                case 3:
                    return getResources().getString(R.string.order_my_wait_for_take);
                case 4:
                    return getResources().getString(R.string.order_my_wait_for_judge);
            }
            return null;
        }
    }
}
