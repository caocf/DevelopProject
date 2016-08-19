package com.xhl.bqlh.view.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.ui.fragment.OrderQueryFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/12.
 */
@ContentView(R.layout.activity_order_query)
public class OrderQueryActivity extends BaseAppActivity {

    public static final String TAG_ORDER_TYPE = "order_type";

    public static final int ORDER_ALL = 1;

    public static final int ORDER_PAY = 2;

    public static final int ORDER_TAKE = 3;

    public static final int ORDER_JUDGE = 4;

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    private List<Fragment> mFragments;

    @Override
    protected void initParams() {
        super.initBackBar(getString(R.string.order_my), true, false);
        int mTagKey = getIntent().getIntExtra(TAG_ORDER_TYPE, 0);

        int colorNor = ContextCompat.getColor(this, R.color.main_check_color_nor);
        int colorSelect = ContextCompat.getColor(this, R.color.main_check_color_select);

        tabLayout.setTabTextColors(colorNor, colorSelect);

        //显示数据
        mFragments = new ArrayList<>();
        mFragments.add(OrderQueryFragment.instance(ORDER_ALL));
        mFragments.add(OrderQueryFragment.instance(ORDER_PAY));
        mFragments.add(OrderQueryFragment.instance(ORDER_TAKE));
        mFragments.add(OrderQueryFragment.instance(ORDER_JUDGE));

        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTagKey - 1);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.order_all);
                case 1:
                    return getResources().getString(R.string.order_my_wait_for_pay);
                case 2:
                    return getResources().getString(R.string.order_my_wait_for_take);
                case 3:
                    return getResources().getString(R.string.order_my_wait_for_judge);
            }
            return null;
        }
    }
}
