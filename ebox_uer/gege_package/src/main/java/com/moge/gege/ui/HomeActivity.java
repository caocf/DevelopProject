package com.moge.gege.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.OptionBuilder;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.fragment.DeliveryFragment;
import com.moge.gege.ui.fragment.MeFragment;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.MyFragmentTabHost;
import com.moge.gege.util.widget.chat.EmoticonUtil;

import de.greenrobot.event.EventBus;

public class HomeActivity extends BaseActivity implements
        FragmentTabHost.OnTabChangeListener
{
    public static final int INDEX_FEED = 0;
    public static final int INDEX_LIFEZONE = INDEX_FEED + 5;
    public static final int INDEX_WAREHOUSE = INDEX_FEED + 1;
    public static final int INDEX_MSG = INDEX_FEED + 5;
    public static final int INDEX_ME = INDEX_FEED + 1;
    public static final int INDEX_DELIVERY = INDEX_FEED;

    private Activity mContext;
    private MyFragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    private static int mCurIndexPage = INDEX_FEED;
    private int mTryToJumpPage = -1;
    public static HomeActivity instance = null;

    private static final Class mFragmentArray[] = {DeliveryFragment.class, MeFragment.class};

    private static final int mImageViewArray[] = {R.drawable.tab_localservice, R.drawable.tab_me};

    private static final int mTextResIdArray[] = {R.string.delivery, R.string.persional_center};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;
        instance = HomeActivity.this;
        initView();
        initData();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        instance = null;

        // reset syn shoppingcart
        AppApplication.setSynShoppingCartTime(0);

        // to do list!!!
        // ImageLoaderUtil.instance().clear();
        // System.gc();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句

       // 从通知消息过来的
       if(intent.getBooleanExtra("open_msg", false))
       {
           mCurIndexPage = INDEX_MSG;
       }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        switchTabPage(mCurIndexPage);
    }

    @Override
    protected void onPause() {
        super.onResume();
    }

    @Override
    protected void initView()
    {
        mLayoutInflater = LayoutInflater.from(this);

        mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);
        mTabHost.setOnTabChangedListener(this);

        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++)
        {
            TabSpec tabSpec = mTabHost
                    .newTabSpec(getString(mTextResIdArray[i])).setIndicator(
                            getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
//            mTabHost.getTabWidget().getChildAt(i)
//                    .setBackgroundResource(R.drawable.tab_bg);

            mTabHost.getTabWidget().getChildAt(i)
                    .setOnClickListener(new MyClickListener(i));
        }
        mTabHost.setClickable(true);

        switchTabPage(INDEX_FEED);
    }

    private void initData()
    {
        OptionBuilder.instance().init(getApplicationContext());

        new Thread()
        {
            public void run()
            {
                EmoticonUtil.instance().init(
                        AppApplication.instance().getApplicationContext());
            }
        }.start();
    }

    /**
     * set tab image and text
     */
    private View getTabItemView(int index)
    {
        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(getString(mTextResIdArray[index]));

        return view;
    }

    private long firstClickBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            long secondClickBackTime = System.currentTimeMillis();
            if (secondClickBackTime - firstClickBackTime > 2000)
            {
                ToastUtil
                        .showToastShort(
                                getString(R.string.repeat_quit_confirm));
                firstClickBackTime = secondClickBackTime;
            }
            else
            {
                AppApplication.instance().exit();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void switchTabPage(int index)
    {
        mTabHost.setCurrentTab(index);
    }

    public static void setCurTabPage(int index)
    {
        mCurIndexPage = index;
    }

    public class MyClickListener implements OnClickListener
    {
        int mIndex = 0;

        MyClickListener(int index)
        {
            this.mIndex = index;
        }

        @Override
        public void onClick(View v)
        {
//            if (mIndex == INDEX_ME)
            {
                if (!AppApplication.checkLoginState(
                        mContext))
                {
                    mTryToJumpPage = mIndex;
                    return;
                }
            }

            if(mCurIndexPage == mIndex)
            {
                EventBus.getDefault().post(new Event.RefreshEvent(mIndex));
                return;
            }

            switchTabPage(mIndex);
        }
    }

    @Override
    public void onTabChanged(String tabId)
    {
        mCurIndexPage = mTabHost.getCurrentTab();
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        if (result != ErrorCode.SUCCESS)
        {
            return;
        }

        if (mTryToJumpPage != -1)
        {
            mCurIndexPage = mTryToJumpPage;
            switchTabPage(mCurIndexPage);

            mTryToJumpPage = -1;
        }
    }
}
