package com.xhl.world.chat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.ui.activity.BaseAppActivity;
import com.xhl.world.ui.view.SwitchButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/12/24.
 * 系统推送消息和用户聊天消息
 */

@ContentView(R.layout.chat_activity_chat_base)
public class MessageActivity extends BaseAppActivity {


    @Event(R.id.title_back)
    private void onBackClick(View view) {
        finish();
    }

    @ViewInject(R.id.container)
    private ViewPager mViewPager;

    @ViewInject(R.id.switch_button)
    private SwitchButton switchButton;

    private int mTagKey;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onReloadClick() {

    }

    @Override
    protected void initParams() {

        switchButton.setOnText("用户消息");

        switchButton.setOffText("系统消息");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

    static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return new ConversationFragment();
                }
                case 1: {
                    return new PushMessageFragment();
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
