package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.data.dao.PushMsgDAO;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.fragment.BaseFragment;
import com.moge.gege.ui.fragment.FriendMsgFragment;
import com.moge.gege.ui.fragment.NoticeFragment;
import com.moge.gege.ui.fragment.NotifyFragment;
import com.moge.gege.util.widget.MyPageAdapter;

public class MessageNotifyActivity extends BaseActivity
{
    private Context mContext;
    private RadioGroup mTabBoardGroup;
    private MyPageAdapter mAdapter;
    private ViewPager mViewPager;
    private RadioButton mTabBtn1;
    private RadioButton mTabBtn2;
    private RadioButton mTabBtn3;
    private List<BaseFragment> mFragmentList;

    private int mMsgType;
    private String mMsgParams;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelated);

        mMsgType = getIntent().getIntExtra("msg_type", MessageType.MSG_NOTICE);
        mMsgParams = getIntent().getStringExtra("msg_params");
        mTitle = getIntent().getStringExtra("title");

        mContext = MessageNotifyActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderRightImage(R.drawable.icon_setting);

        mTabBoardGroup = (RadioGroup) this.findViewById(R.id.tabRadioGroup);
        mTabBoardGroup.setVisibility(View.GONE);

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(mTitle);
        initViewPager();
    }

    private void initData()
    {
        // read local unread message
        PushMsgDAO.instance().readPushMsg(AppApplication.getLoginId(),
                mMsgParams);
        if (AppService.instance() != null)
        {
            AppService.instance().removeNotify(mMsgParams, 0);
        }
    }

    @Override
    protected void onHeaderRightClick()
    {
        Intent intent = new Intent(mContext, ChatSettingActivity.class);
        intent.putExtra("tag", mMsgParams);
        startActivity(intent);
    }

    private List<BaseFragment> getViewPagerSource()
    {
        mFragmentList = new ArrayList<BaseFragment>();

        switch (mMsgType)
        {
            case MessageType.MSG_NOTICE:
                mFragmentList.add(new NoticeFragment().setFragmentType(mMsgParams));
                break;
            case MessageType.MSG_NOTIFY:
                mFragmentList.add(new NotifyFragment().setFragmentType(mMsgParams));
                break;
            case MessageType.MSG_FRIEND:
                mFragmentList.add(new FriendMsgFragment());
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

        mTabBoardGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.tabBtn1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tabBtn2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.tabBtn3:
                        mViewPager.setCurrentItem(2);
                        break;
                }
            }

        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                if (mTabBoardGroup != null
                        && mTabBoardGroup.getChildCount() > position)
                {
                    ((RadioButton) mTabBoardGroup.getChildAt(position))
                            .setChecked(true);
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

}
