package com.xhl.bqlh.view.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.ui.fragment.CollectionFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/18.
 */
@ContentView(R.layout.activity_details_collection)
public class CollectionActivity extends BaseAppActivity {

    public static final int TAG_PRODUCT = 0;

    public static final int TAG_SHOP = 1;

    @ViewInject(R.id.tab_layout)
    private TabLayout tab_layout;

    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;

    private List<Fragment> mFragments;

    @Override
    protected void initParams() {

        int colorNor = ContextCompat.getColor(this, R.color.main_check_color_nor);
        int colorSelect = ContextCompat.getColor(this, R.color.main_check_color_select);

        tab_layout.setTabTextColors(colorNor, colorSelect);


        int curPage = getIntent().getIntExtra("data", TAG_PRODUCT);

        mFragments = new ArrayList<>();

        mFragments.add(CollectionFragment.instance(TAG_PRODUCT));
        mFragments.add(CollectionFragment.instance(TAG_SHOP));

        view_pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tab_layout.setupWithViewPager(view_pager);

        view_pager.setCurrentItem(curPage);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "商品";
                case 1:
                    return "店铺";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
