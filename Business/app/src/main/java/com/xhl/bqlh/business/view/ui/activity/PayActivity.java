package com.xhl.bqlh.business.view.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.fragment.PayFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/21.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseAppActivity {

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTab;

    private List<Fragment> mFragments;
    private SectionsPagerAdapter mViewPagerAdapter;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_clear);
        setTitle(R.string.menu_pay);

        mFragments = new ArrayList<>();
        mFragments.add(PayFragment.newInstance(0));
        mFragments.add(PayFragment.newInstance(1));

        mViewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTab.setupWithViewPager(mViewPager);
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
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "支付宝";
            } else if (position == 1) {
                return "微信";
            }
            return null;
        }
    }
}
