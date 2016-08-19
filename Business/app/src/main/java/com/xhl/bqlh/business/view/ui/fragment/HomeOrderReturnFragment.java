package com.xhl.bqlh.business.view.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xhl.bqlh.business.Model.Type.OrderReturnType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/27.
 */
@ContentView(R.layout.fragment_home_order_return)
public class HomeOrderReturnFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTab;

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;

    private SectionsPagerAdapter mViewPagerAdapter;

    private List<Fragment> mFragments;

    @Override
    protected void initParams() {
        super.initHomeToolbar();
//        mToolbar.inflateMenu(R.menu.user_menu_order_manager);
//        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.user_nav_main_order_return);

        mFragments = new ArrayList<>();
        mFragments.add(OrderReturnFragment.instance(OrderReturnType.TYPE_APPLY));
        mFragments.add(OrderReturnFragment.instance(OrderReturnType.TYPE_HANDLE));
        mFragments.add(OrderReturnFragment.instance(OrderReturnType.TYPE_FINISH));

        mViewPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "待审核";
                    break;
                case 1:
                    title = "处理中";
                    break;
                case 2:
                    title = "已完成";
                    break;
            }
            return title;
        }
    }

}
