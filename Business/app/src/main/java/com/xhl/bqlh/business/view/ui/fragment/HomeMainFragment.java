package com.xhl.bqlh.business.view.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Db.TaskShop;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.CheckServiceHelper;
import com.xhl.bqlh.business.doman.TaskAsyncHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.event.SelectTaskEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.activity.AboutActivity;
import com.xhl.bqlh.business.view.ui.activity.AddTemporaryTaskActivity;
import com.xhl.bqlh.business.view.ui.activity.SearchNearByActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_main)
public class HomeMainFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener {

    private static final int ANIM_DURATION = 400;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTab;

    @ViewInject(R.id.main_fab)
    private FloatingActionButton mFab;

    @ViewInject(R.id.view_pager)
    private ViewPager mViewPager;

    @Event(R.id.main_fab)
    private void onAddClick(View view) {
        Intent intent = new Intent(getContext(), AddTemporaryTaskActivity.class);
        startActivity(intent);
    }

    private int mTodayIndex = 0;
    private TaskFragment mTodayFragment;
    private List<TaskFragment> mFragments;

    private TaskAsyncHelper mAsync;
    private SectionsPagerAdapter mViewPagerAdapter;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        //菜单初始化
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.user_menu_main);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.user_nav_main);
        loadTaskFragment();
    }

    private void loadTaskFragment() {
        //今天所对应的星期
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        mTodayIndex = day - 1;
        //
        //接口返回的1表示星期天,2星期一
        int startIndex = 0;
        mFragments = new ArrayList<>();
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));
        mFragments.add(TaskFragment.newInstance(++startIndex));

        mTodayFragment = mFragments.get(mTodayIndex);

        mViewPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mTodayIndex) {
                    showFab(true);
                } else {
                    showFab(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTab.setupWithViewPager(mViewPager);
        goToday();
    }

    private void goToday() {
        mViewPager.setCurrentItem(mTodayIndex, true);
    }

    private boolean mHasShow = true;

    private void showFab(boolean show) {
        if (show) {
            if (!mHasShow) {
                mHasShow = true;
                fabShow();
            }
        } else {
            if (mHasShow) {
                mHasShow = false;
                fabHide();
            }
        }
    }

    private void fabShow() {
        ViewHelper.setViewGone(mFab, false);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFab, View.SCALE_X, 0f, 1.12f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFab, View.SCALE_Y, 0f, 1.12f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.start();
    }

    private void fabHide() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFab, View.SCALE_X, 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFab, View.SCALE_Y, 1f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(ANIM_DURATION - 200);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_near_by) {
            startActivity(new Intent(getContext(), SearchNearByActivity.class));
            AVAnalytics.onEvent(getContext(), "click nearby shop");
            return true;
        }
        if (id == R.id.menu_update_task) {
            AVAnalytics.onEvent(getContext(), "click update task");
            mAsync = new TaskAsyncHelper(new TaskAsyncHelper.TaskListener() {
                @Override
                public void onPullStart() {
                    showProgressLoading("更新任务中");
                }

                @Override
                public void onPullFinish() {
                    mToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();

                            reloadTaskInfo();

                            goToday();

                            SnackUtil.shortShow(mToolbar, "更新完成");
                        }
                    }, 1000);
                }

                @Override
                public void onPullFailed(String msg) {
                    hideLoadingDialog();
                    SnackUtil.shortShow(mToolbar, msg);
                }
            });
            //不需要更新记录
            mAsync.setmNeedPullRecord(false);
            mAsync.pullServiceTask();
            return true;
        }
        if (id == R.id.menu_update) {
            AVAnalytics.onEvent(getContext(), "click check version");
            CheckServiceHelper checkServiceHelper = new CheckServiceHelper(getActivity());
            checkServiceHelper.isNeedShowHint = true;
            checkServiceHelper.checkVersion();
            return true;
        }
        if (id == R.id.menu_about) {
            startActivity(new Intent(getContext(), AboutActivity.class));
            return true;
        }
        if (id == R.id.menu_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SUBJECT, "下载地址:" + PreferenceData.getInstance().getDownloadUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "分享到"));
            return true;
        }

        return true;
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final String[] WEEK = {"周日", "周一", "周二", "周三", "周四",
                "周五", "周六"};

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
            if (position < 7) {
                return WEEK[position];
            }
            return null;
        }
    }

    //任务操作
    public void onEvent(final SelectTaskEvent taskEvent) {
        int type = taskEvent.type;
        if (type == SelectTaskEvent.type_finish) {
            finishOneTask(taskEvent.shopId);

        } else {
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (TaskShop shop : taskEvent.shops) {
                        addTemporaryTask(shop);
                    }
                }
            }, 200);
        }
    }

    private void reloadTaskInfo() {
        for (TaskFragment f : mFragments) {
            f.reLoadInfo();
        }
    }

    public void onEvent(CommonEvent event) {
        if (event.eventType == CommonEvent.EVENT_RELOAD_TASK) {
            TaskAsyncHelper taskAsyncHelper = new TaskAsyncHelper(new TaskAsyncHelper.TaskListener() {
                @Override
                public void onPullStart() {

                }

                @Override
                public void onPullFinish() {
                    mToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reloadTaskInfo();
                        }
                    }, 1000);
                }

                @Override
                public void onPullFailed(String msg) {

                }
            });
            taskAsyncHelper.setmNeedPullRecord(false);
            taskAsyncHelper.pullServiceTask();
        }
    }

    /**
     * 完成一个店铺的拜访任务
     */
    private void finishOneTask(String shopId) {
        if (mTodayFragment != null) {
            mTodayFragment.finishOneTask(shopId);
        }
    }

    /**
     * 添加一个临时的店铺拜访任务
     */
    private void addTemporaryTask(final TaskShop temporaryTask) {
        if (mTodayFragment != null) {
            mTodayFragment.addOneTemporaryTask(temporaryTask);
        }
    }
}
