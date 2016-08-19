package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.ui.activity.OrderSearchActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/5/15.
 * 订单查询
 */
@ContentView(R.layout.fragment_home_order_query)
public class HomeOderQueryFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener {
    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    private SectionsPagerAdapter mPagerAdapter;
    private List<OrderManagerFragment> mFragments;

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        mToolbar.inflateMenu(R.menu.user_menu_order_manager);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.user_nav_main_order_manager_all);

        mFragments = new ArrayList<>();

        mFragments.add(OrderManagerFragment.newInstance(getCondition(OrderType.order_type_car)));
        mFragments.add(OrderManagerFragment.newInstance(getCondition(OrderType.order_type_self)));
        mFragments.add(OrderManagerFragment.newInstance(getCondition(OrderType.order_type_shop)));

        mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private OrderQueryCondition getCondition(int orderType) {
        OrderQueryCondition condition = new OrderQueryCondition();
        condition.orderType = orderType;
        condition.isNeedOrderTag = true;
        if (orderType == OrderType.order_type_car) {
            condition.hint = "没有车销订单";
        } else if (orderType == OrderType.order_type_self) {
            condition.hint = "没有拜访订单";
        } else if (orderType == OrderType.order_type_shop) {
            condition.hint = "没有门店订单";
        }
        return condition;
    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(getContext(), OrderSearchActivity.class));
        return false;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.order_by_car);
                case 1:
                    return getString(R.string.order_by_self);
                case 2:
                    return getString(R.string.order_by_shop);
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
