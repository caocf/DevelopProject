package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.moge.gege.R;
import com.moge.gege.model.enums.MyOperateType;
import com.moge.gege.model.enums.MyPublishType;
import com.moge.gege.ui.fragment.BaseFragment;
import com.moge.gege.ui.fragment.MyActivityFragment;
import com.moge.gege.ui.fragment.MyFavoriteFragment;
import com.moge.gege.ui.fragment.MyPublishFragment;
import com.moge.gege.ui.fragment.MyPurchaseFragment;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.MyPageAdapter;
import com.moge.gege.util.widget.viewpagerindicator.LinePageIndicator;

public class MyRelatedActivity extends BaseActivity
{
    private Context mContext;
    private LinearLayout mMyPublishLayout;
    private RadioGroup mTabBoardGroup;
    private MyPageAdapter mAdapter;
    private ViewPager mViewPager;
    private LinePageIndicator mIndicator;
    private RadioButton mTabBtn1;
    private RadioButton mTabBtn2;
    private RadioButton mTabBtn3;
    private List<BaseFragment> mFragmentList;

    private int mOperateType;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelated);

        mOperateType = getIntent().getIntExtra("operateType",
                MyOperateType.MY_PUBLISH);
        mFrom = getIntent().getStringExtra("from");

        mContext = MyRelatedActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        mMyPublishLayout = (LinearLayout) this
                .findViewById(R.id.myPublishLayout);
        mTabBoardGroup = (RadioGroup) this.findViewById(R.id.tabRadioGroup);
        mTabBtn1 = (RadioButton) this.findViewById(R.id.tabBtn1);
        mTabBtn2 = (RadioButton) this.findViewById(R.id.tabBtn2);
        mTabBtn3 = (RadioButton) this.findViewById(R.id.tabBtn3);

        this.setHeaderLeftImage(R.drawable.icon_back);
        switch (mOperateType)
        {
            case MyOperateType.MY_PUBLISH:
                this.setHeaderLeftTitle(R.string.my_publish);
                mTabBtn1.setText(getString(R.string.topic));
                mTabBtn2.setText(getString(R.string.activity));
                mTabBtn3.setText(getString(R.string.category_info));
                break;
            case MyOperateType.MY_ACTIVITY:
                this.setHeaderLeftTitle(R.string.my_activity);
                break;
            case MyOperateType.MY_FAVORITE:
                this.setHeaderLeftTitle(R.string.my_favorite);
                break;
            case MyOperateType.MY_PRUCHASE:
                this.setHeaderLeftTitle(R.string.my_purchase);
                mTabBtn1.setText(getString(R.string.all));
                mTabBtn2.setText(getString(R.string.to_pay));
                mTabBtn3.setText(getString(R.string.to_appraise));
                break;
            default:
                break;
        }

        initViewPager();
    }

    private List<BaseFragment> getViewPagerSource()
    {
        mFragmentList = new ArrayList<BaseFragment>();

        switch (mOperateType)
        {
            case MyOperateType.MY_PUBLISH:
                mMyPublishLayout.setVisibility(View.VISIBLE);
                mFragmentList.add(new MyPublishFragment().setFragmentType(MyPublishType.TOPIC));
                mFragmentList
                        .add(new MyPublishFragment().setFragmentType(MyPublishType.ACTIVITY));
                mFragmentList.add(new MyPublishFragment().setFragmentType(MyPublishType.SERVICE));
                break;
            case MyOperateType.MY_ACTIVITY:
                mFragmentList.add(new MyActivityFragment());
                break;
            case MyOperateType.MY_FAVORITE:
                mFragmentList.add(new MyFavoriteFragment());
                break;
            case MyOperateType.MY_PRUCHASE:
                mFragmentList.add(new MyPurchaseFragment());
                break;
            default:
                break;
        }

        return mFragmentList;
    }

    private void initViewPager()
    {
        mAdapter = new MyPageAdapter(getSupportFragmentManager(),
                getViewPagerSource());
        mViewPager = (ViewPager) findViewById(R.id.myViewPager);
        mViewPager.setAdapter(mAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        if(MyOperateType.MY_PUBLISH != mOperateType)
        {
            mIndicator.setVisibility(View.GONE);
            return;
        }
        mIndicator.setViewPager(mViewPager);
        mIndicator.setLineWidth(ViewUtil.getWidth()/3);

        mTabBoardGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.tabBtn1:
                        mIndicator.setCurrentItem(0);
                        break;
                    case R.id.tabBtn2:
                        mIndicator.setCurrentItem(1);
                        break;
                    case R.id.tabBtn3:
                        mIndicator.setCurrentItem(2);
                        break;
                }
            }

        });

        mIndicator.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                // if (mTabBoardGroup != null
                // && mTabBoardGroup.getChildCount() > position)
                // {
                // ((RadioButton) mTabBoardGroup.getChildAt(position))
                // .setChecked(true);
                // }

                switch (position)
                {
                    case 0:
                        mTabBtn1.setChecked(true);
                        break;
                    case 1:
                        mTabBtn2.setChecked(true);
                        break;
                    case 2:
                        mTabBtn3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });

        // init
        ((RadioButton) mTabBoardGroup.getChildAt(0)).setChecked(true);
    }

    private void closeActivity()
    {
        if (TextUtils.isEmpty(mFrom)
                || !mFrom.equalsIgnoreCase(TradingPayResultActivity.TAG))
        {
            finish();
        }
        else
        {
            UIHelper.showHomePageActivity(mContext);
        }
    }

    @Override
    protected void onHeaderLeftClick()
    {
        closeActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            closeActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
