package com.xhl.world.ui.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.config.Constant;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/2/27.
 */
@ContentView(R.layout.fragment_coupon)
public class CouponFragment extends BaseAppFragment {

    @ViewInject(R.id.container)
    private ViewPager mViewPager;

    @ViewInject(R.id.tabs)
    private TabLayout tabLayout;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_other;

    @Event(R.id.title_other)
    private void onRuleClick(View view) {
        Intent intent = new Intent(mContext, WebPageActivity.class);
        intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_coupon_rule);
        intent.putExtra(WebPageActivity.TAG_TITLE, "优惠券使用规则");
        getContext().startActivity(intent);
    }

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().finish();
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mTagKey = 0;

    @Override
    protected void initParams() {
        title_name.setText(R.string.my_sign_in);
        title_other.setText("规则");

        int colorNor = ContextCompat.getColor(mContext, R.color.main_check_text_color);
        int colorSelect = ContextCompat.getColor(mContext, R.color.main_check_color);
        tabLayout.setTabTextColors(colorNor, colorSelect);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.setCurrentItem(mTagKey, true);

    }


    static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return CouponDetailsFragment.instance(CouponDetailsFragment.Coupon_status_new);
                }
                case 1: {
                    return CouponDetailsFragment.instance(CouponDetailsFragment.Coupon_status_used);
                }
                case 2: {
                    return CouponDetailsFragment.instance(CouponDetailsFragment.Coupon_status_time_out);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return "未使用";
                }
                case 1: {
                    return "已使用";
                }
                case 2: {
                    return "已过期";
                }
            }
            return null;
        }
    }
}
