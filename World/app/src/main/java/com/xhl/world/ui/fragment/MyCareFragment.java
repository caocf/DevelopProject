package com.xhl.world.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.ui.view.SwitchButton;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/3.
 */
@ContentView(R.layout.fragment_my_care)
public class MyCareFragment extends BaseAppFragment {

    @ViewInject(R.id.container)
    private ViewPager mViewPager;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @ViewInject(R.id.switch_button)
    private SwitchButton switchButton;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mTagKey;

    @Override
    protected void initParams() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        switchButton.setListener(new SwitchButton.switchCheckChangeListener() {
            @Override
            public void switchOn() {
                mViewPager.setCurrentItem(0);
            }

            @Override
            public void switchOff() {
                mViewPager.setCurrentItem(1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    switchButton.setSwitchOn(true);
                } else {
                    switchButton.setSwitchOn(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mTagKey == 0) {
            switchButton.setSwitchOn(true);
        } else {
            switchButton.setSwitchOn(false);
        }
    }

    @Override
    public void onEnter(Object data) {
        super.onEnter(data);
        try {
            mTagKey = (int) data;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePage(int page) {
        mViewPager.setCurrentItem(page, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        changePage(mTagKey);
    }

    static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return MyCareDetailFragment.instance(0);
                }
                case 1: {
                    return MyCareDetailFragment.instance(1);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
