package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.RespUserStatModel;
import com.moge.gege.model.UserStatModel;
import com.moge.gege.model.enums.MyOperateType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.UserStatRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.fragment.UserFragment;
import com.moge.gege.ui.fragment.UserFragment.UserType;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.MyPageAdapter;
import com.moge.gege.util.widget.viewpagerindicator.LinePageIndicator;

public class ContactsActivity extends BaseActivity
{
    private Context mContext;

    private MyPageAdapter mPageAdapter;
    private ViewPager mViewPager;
    private LinePageIndicator mIndicator;
    private List<UserFragment> mFragmentList;
    private RadioGroup mContactTabGroup;
    private RadioButton mFriendBtn;
    private RadioButton mNeighborBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mContext = ContactsActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.contacts);

        mFriendBtn = (RadioButton) this.findViewById(R.id.friendBtn);
        mFriendBtn.setText(getString(R.string.my_friend, 0));
        mNeighborBtn = (RadioButton) this.findViewById(R.id.neighborBtn);
        mNeighborBtn.setText(getString(R.string.my_neighbor, 0));

        initViewPager();
    }

    private void initData()
    {
        doUserStat();
    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private void initViewPager()
    {
        mPageAdapter = new MyPageAdapter(getSupportFragmentManager(),
                getViewPagerSource());
        mViewPager = (ViewPager) findViewById(R.id.userViewPager);
        mViewPager.setAdapter(mPageAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setLineWidth(ViewUtil.getWidth()/2);

        mContactTabGroup = (RadioGroup) this.findViewById(R.id.contactTabGroup);
        mContactTabGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                        switch (checkedId)
                        {
                            case R.id.friendBtn:
                                mIndicator.setCurrentItem(0);
                                break;
                            case R.id.neighborBtn:
                                mIndicator.setCurrentItem(1);
                                break;
                        }
                    }

                });

        mIndicator.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                // if (mContactTabGroup != null
                // && mContactTabGroup.getChildCount() > position)
                // {
                // ((RadioButton) mContactTabGroup.getChildAt(position))
                // .setChecked(true);
                // }

                if (position == 0)
                {
                    mFriendBtn.setChecked(true);
                }
                else
                {
                    mNeighborBtn.setChecked(true);
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
        ((RadioButton) mContactTabGroup.getChildAt(0)).setChecked(true);
    }

    private List<UserFragment> getViewPagerSource()
    {
        mFragmentList = new ArrayList<UserFragment>();

        UserFragment friendFragment = new UserFragment();
        friendFragment.setFragmentType(UserType.FRIEND);
        mFragmentList.add(friendFragment);

        UserFragment neighborFragment = new UserFragment();
        neighborFragment.setFragmentType(UserType.NEIGHBOR);
        mFragmentList.add(neighborFragment);

        return mFragmentList;
    }

    private void doUserStat()
    {
        UserStatRequest request = new UserStatRequest("",
                new ResponseEventHandler<RespUserStatModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserStatModel> request,
                            RespUserStatModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            UserStatModel model = result.getData();
                            mFriendBtn.setText(getString(R.string.my_friend,
                                    model.getFriend_count()));
                            mNeighborBtn.setText(getString(
                                    R.string.my_neighbor,
                                    model.getNeighbor_count()));
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }
                });
        executeRequest(request);
    }
}
