package com.xhl.world.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Entities.BaseTargetEntities;
import com.xhl.world.model.Entities.FlashSaleDetailsEntities;
import com.xhl.world.model.FlashSaleTimeModel;
import com.xhl.world.model.Type.FlashSaleType;
import com.xhl.world.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.world.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.world.ui.fragment.FlashSaleDetailsByTagFragment;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.world.ui.recyclerHolder.FlashSaleDetailsTimeDataHolder;
import com.xhl.world.ui.utils.RecyclerViewScrollHelper;
import com.xhl.world.ui.utils.TimeCountHelper;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/11.
 */
@ContentView(R.layout.activity_flash_sale_details)
public class FlashSaleDetailsActivity extends BaseAppActivity implements RecycleViewCallBack {

    @ViewInject(R.id.time_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;

    @ViewInject(R.id.rl_content)
    private myLoadRelativeLayout mRelativeLayout;

    @ViewInject(R.id.title_name)
    private TextView mTitle;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    private boolean isLoadSuccess = false;
    private FragmentViewPagerAdapter mFragmentAdapter;
    private List<Fragment> mFragments;

    private SelectStateRecyclerAdapter mRecyclerAdapter;
    private List<RecyclerDataHolder> mRecyclerData;
    private LinearLayoutManager mLayoutManger;

    private RecyclerViewScrollHelper mHelper;

    private int mCurPage = -1;
    private int mFirstPage = -1;

    @Override
    protected void initParams() {
        mTitle.setText("抢购");
        //限时抢购的内容
        mFragments = new ArrayList<>();
        mFragmentAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        mFragmentAdapter.setFragments(mFragments);
        mViewPager.setAdapter(mFragmentAdapter);

        //限时抢购的时间
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new SelectStateRecyclerAdapter(this);
        mLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mHelper = new RecyclerViewScrollHelper(mLayoutManger, mRecyclerView);
        pageChangeListener();
    }

    private void pageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mCurPage != position) {
                    onItemClick(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private ArrayList<FlashSaleTimeModel> getTestData() {
        ArrayList<FlashSaleTimeModel> models = new ArrayList<>();

        FlashSaleTimeModel model = null;

        model = new FlashSaleTimeModel();
        model.setTime("2015-12-15 12:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG0";
        models.add(model);

        model = new FlashSaleTimeModel();
        model.setTime("2016-1-16 16:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG1";
        models.add(model);

        model = new FlashSaleTimeModel();
        model.setTime("2016-3-16 20:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG2";
        models.add(model);

        model = new FlashSaleTimeModel();
        model.setTime("2016-6-17 24:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG3";
        models.add(model);

        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);
        model = new FlashSaleTimeModel();
        model.setTime("2016-12-15 08:00:00");
        model.setState(getState(model.getTime()));
        model.content_tag = "TAG4";
        models.add(model);

        return models;
    }

    private boolean isExistStart = false;

    private int getState(String time) {
        long leftTime = TimeCountHelper.countLeftTime(time);
        //   LogUtil.e("leftTime :" + leftTime+" time:"+time);
        if (leftTime <= 0) {
            return 0;
        }

        if (leftTime > 0) {
            if (!isExistStart) {
                isExistStart = true;
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    private void getFlashSaleTimeData() {

        FlashSaleDetailsTimeDataHolder holder = null;
        ArrayList<FlashSaleTimeModel> testData = getTestData();

        for (int i = 0; i < testData.size(); i++) {

            FlashSaleTimeModel model = testData.get(i);
            int state = model.getState();
            holder = new FlashSaleDetailsTimeDataHolder(model);
            holder.setCallBack(this);

            FlashSaleDetailsEntities entities = new FlashSaleDetailsEntities();
            entities.setSale_state(state);

            if (state == FlashSaleType.Flash_sale_start) {
                //下一场开始时间 注意数组越界
                int next = i + 1;
                mFirstPage = i;
                entities.setShow_time(testData.get(next).getTime());
            } else {
                entities.setShow_time(model.getTime());
            }
            entities.setQuery_condition(model.content_tag);

            getFlashSaleDetailsByTime(entities);

            if (i == 0) {
                BaseTargetEntities targetEntities = new BaseTargetEntities();
                targetEntities.setImage_url("http://img1.imgtn.bdimg.com/it/u=2282547951,3816622274&fm=21&gp=0.jpg");
                targetEntities.setTarget(1);
                entities.setBase_entities(targetEntities);
            }
            if (i == 2) {
                BaseTargetEntities targetEntities = new BaseTargetEntities();
                targetEntities.setImage_url("http://img2.imgtn.bdimg.com/it/u=1992251649,697132307&fm=21&gp=0.jpg");
                targetEntities.setTarget(1);
                entities.setBase_entities(targetEntities);
            }
            if (i == 3) {
                BaseTargetEntities targetEntities = new BaseTargetEntities();
                targetEntities.setImage_url("http://img2.imgtn.bdimg.com/it/u=2861007124,1417425748&fm=21&gp=0.jpg");
                targetEntities.setTarget(1);
                entities.setBase_entities(targetEntities);
            }

            mRecyclerData.add(holder);
        }

        mRecyclerAdapter.setDataHolders(mRecyclerData);

        mFragmentAdapter.notifyDataSetChanged();
    }


    private void getFlashSaleDetailsByTime(FlashSaleDetailsEntities entities) {

        mFragments.add(FlashSaleDetailsByTagFragment.newInstance(entities));
    }

    /**
     *  重新加载数据事件
     */
    @Override
    protected void onReloadClick() {
//        mRelativeLayout.showLoadingView();
//        mRelativeLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRelativeLayout.showNetWorkErrorView();
//            }
//        }, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLoadSuccess) {
            mRelativeLayout.showLoadingView();
            isLoadSuccess = true;
        }
        mRelativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRelativeLayout.showNetWorkErrorView();
//                getFlashSaleTimeData();
//
//                onItemClick(mFirstPage);
            }
        }, 10000);
    }

    @Override
    public void onItemClick(int position) {
        if (mCurPage == position) {
            return;
        }
        mCurPage = position;

        mRecyclerAdapter.setSelectPosition(position);

        if (position < mFragments.size()) {
            mViewPager.setCurrentItem(position, true);
        }
       // LogUtil.e("onItemClick:" + position);

        mHelper.scrollToPos(position);
    }
}
