package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.FlashSaleMode;
import com.xhl.world.ui.activity.LimitSaleActivity;
import com.xhl.world.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.world.ui.main.home.FlashSaleFragment;
import com.xhl.world.ui.utils.TimeCountHelper;
import com.xhl.xhl_library.ui.viewPager.viewpagerindicator.LinePageIndicator;
import com.xhl.xhl_library.utils.TimeCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/27.
 */
public class HomeFlashSaleBar extends BaseHomeBar {

    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_sec;
    private LinearLayout ll_more;
    private LinearLayout ll_content;

    private ViewPager home_flash_sale_viewpager;
    private FragmentViewPagerAdapter mAdapter;
    private LinePageIndicator mLinePageIndicator;
    private List<Fragment> mFragments;
    private TimeCount mTimeCount;

    @Override
    protected boolean autoInject() {
        return false;
    }

    public HomeFlashSaleBar(Context context) {
        super(context);
    }

    public HomeFlashSaleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {

        ll_content = _findViewById(R.id.ll_content);
        tv_hour = _findViewById(R.id.tv_hour);
        tv_min = _findViewById(R.id.tv_min);
        tv_sec = _findViewById(R.id.tv_sec);
        ll_more = _findViewById(R.id.ll_more);
        ll_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().startActivity(new Intent(getContext(), LimitSaleActivity.class));

            }
        });
        mLinePageIndicator = _findViewById(R.id.home_flash_sale_viewpager_indicator);
        home_flash_sale_viewpager = _findViewById(R.id.home_flash_sale_viewpager);

        FragmentActivity activity = (FragmentActivity) getContext();
        mAdapter = new FragmentViewPagerAdapter(activity.getSupportFragmentManager());
        mAdapter.setFragments(mFragments);
        home_flash_sale_viewpager.setAdapter(mAdapter);
    }


    /**
     * ex: 下场抢购开始时间 16:00:00
     * 抢购剩余时间是从当前时间计算到下场开始时间结束所剩余时间
     *
     * @param time 下一场开始时间
     */
    public void setNextSaleStartTime(String time) {
        stopCount();
        long total = TimeCountHelper.countTodayLeftTime();
        long inter = 1000;
        mTimeCount = new TimeCount(total, inter, new TimeCount.TimeOutCallback() {
            @Override
            public void onFinish() {
                ll_content.setVisibility(INVISIBLE);
            }

            @Override
            public void onTick(long st) {
                getLeftTime(st);
            }
        });
        mTimeCount.start();
    }

    private void getLeftTime(long leftTime) {
        int second = (int) (leftTime / 1000);
        int day = second / 86400;
        if (day != 0) {
            second = second % 86400;
        }
        int hour = second / 3600;
        int leftSecond = second % 3600;
        int min = leftSecond / 60;
        int sec = leftSecond % 60;

        tv_hour.setText(TimeCountHelper.checkTime(hour));
        tv_min.setText(TimeCountHelper.checkTime(min));
        tv_sec.setText(TimeCountHelper.checkTime(sec));
    }


    public void stopCount() {
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
    }

    public void setFlashSaleData(List<String> data) {
        mFragments = new ArrayList<>();

        FlashSaleMode mode = new FlashSaleMode();
        mode.urls = data;
        FlashSaleFragment fragment = FlashSaleFragment.newInstance(mode);

        FlashSaleMode mode1 = new FlashSaleMode();
        mode1.urls = data;
        FlashSaleFragment fragment1 = FlashSaleFragment.newInstance(mode1);

        FlashSaleMode mode2 = new FlashSaleMode();
        mode2.urls = data;
        FlashSaleFragment fragment2 = FlashSaleFragment.newInstance(mode2);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        fragments.add(fragment1);
        fragments.add(fragment2);


        mFragments.addAll(fragments);
        mAdapter.setFragments(mFragments);
        mAdapter.notifyDataSetChanged();

        mLinePageIndicator.setViewPager(home_flash_sale_viewpager);
        mLinePageIndicator.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_flash_sale;
    }

}
