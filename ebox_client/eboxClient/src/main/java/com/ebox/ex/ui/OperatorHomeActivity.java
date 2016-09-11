package com.ebox.ex.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.RspUpdateOperatorBalance;
import com.ebox.ex.network.request.RequestUpdateOperatorInfo;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.ex.ui.fragment.OpChangePwdFragment;
import com.ebox.ex.ui.fragment.OpDeliveryFragment;
import com.ebox.ex.ui.fragment.OpDownloadFragment;
import com.ebox.ex.ui.fragment.OpHelpFragment;
import com.ebox.ex.ui.fragment.OpHistoryOrderFragment;
import com.ebox.ex.ui.fragment.OpRechargeFragment;
import com.ebox.ex.ui.fragment.OpRecycleFragment;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.HashMap;

public class OperatorHomeActivity extends CommonActivity implements CompoundButton.OnCheckedChangeListener {

    private RadioButton rb_deliver, rb_time_out, rb_history_order, rb_change_pwd, rb_recharge, rb_download, rb_help;
    private TextView tv_title;
    private ImageView iv_title;
    private TextView tv_balance,tv_state;
    private Tip tip;

    //缓存的Fragment
    private HashMap<Integer, BaseOpFragment> mCacheFragment;

    //传递数据
    private int loginType;//登录状态  0：离线登录，1：联网登录
    public String operatorId;

    private int mCurPage;
    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.ex_oper_page);
        data.tvVisibility = true;
        title.setData(data, this);
        tv_title = title.tv;
        iv_title=title.iv_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_operator);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        mCacheFragment = new HashMap<Integer, BaseOpFragment>();
        initTitle();

        loginType = getIntent().getIntExtra("loginType", 1);
        operatorId = getIntent().getStringExtra("operatorId");

        rb_deliver = (RadioButton) findViewById(R.id.rb_deliver);
        rb_deliver.setOnCheckedChangeListener(this);
        rb_time_out = (RadioButton) findViewById(R.id.rb_time_out);
        rb_time_out.setOnCheckedChangeListener(this);
        rb_history_order = (RadioButton) findViewById(R.id.rb_history_order);
        rb_history_order.setOnCheckedChangeListener(this);
        rb_recharge = (RadioButton) findViewById(R.id.rb_recharge);
        rb_recharge.setOnCheckedChangeListener(this);
        rb_change_pwd = (RadioButton) findViewById(R.id.rb_change_pwd);
        rb_change_pwd.setOnCheckedChangeListener(this);
        rb_download = (RadioButton) findViewById(R.id.rb_download);
        rb_download.setOnCheckedChangeListener(this);
        rb_help = (RadioButton) findViewById(R.id.rb_help);
        rb_help.setOnCheckedChangeListener(this);

        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_state = (TextView) findViewById(R.id.tv_state);

        rb_deliver.setChecked(true);

        if (!OperatorHelper.isAccount())
        {
            rb_recharge.setVisibility(View.GONE);
        }

        registerBroadcast();

        if (OperatorHelper.checkTimeOut(operatorId))
        {
            changePage(1);
        }else {
            changePage(0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();

        updateBalance();
    }

    private void  updateBalance(){
        String balance = OperatorHelper.getOperatorBalance2St(operatorId);

        tv_balance.setText(getResources().getString(R.string.ex_op_balance,balance));
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseOpFragment cacheFragment = getCacheFragment(mCurPage);
        if (cacheFragment!=null)
        {
            cacheFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(updateReceiver);
        if (tip != null) {
            tip.closeTip();
        }
        //clear cache
        OperatorHelper.clear();
        super.onDestroy();
    }

    private void updateTitle(String title) {
        if (title != null && tv_title != null) {
            tv_title.setText(title);
        }
    }

    /**
     * 获取缓存Fragment
     *
     * @param page
     * @return
     */
    public BaseOpFragment getCacheFragment(Integer page) {

        if (mCacheFragment != null) {

            BaseOpFragment fragment = mCacheFragment.get(page);

            return fragment;
        }
        return null;
    }

    /**
     * 添加Fragment到缓存队列中
     *
     * @param page
     * @param fragment
     * @return
     */
    public void addFragment(Integer page, BaseOpFragment fragment) {

        if (mCacheFragment != null)
        {
            mCacheFragment.put(page, fragment);
        }
    }

    // 0:派件 1：超时件 2：投递记录 3：充值 4.修改密码 5.下载 6.帮助
    private void changePage(int page) {

        if (OperatorHelper.mHasTimeoutOrder)
        {
            tip = new Tip(this, "请先处理超时件", null);
            tip.show(0);
            return;
        }

        mCurPage = page;

        BaseOpFragment cacheFragment = getCacheFragment(page);
        if (cacheFragment == null)
        {
            switch (page) {
                case 0:
                    cacheFragment = OpDeliveryFragment.newInstance();
                    break;
                case 1:
                    cacheFragment = OpRecycleFragment.newInstance();
                    break;
                case 2:
                    cacheFragment = OpHistoryOrderFragment.newInstance();
                    break;
                case 3:
                    cacheFragment = OpRechargeFragment.newInstance();
                    break;
                case 4:
                    cacheFragment = OpChangePwdFragment.newInstance();
                    break;
                case 5:
                    cacheFragment = OpDownloadFragment.newInstance();
                    break;
                case 6:
                    cacheFragment = OpHelpFragment.newInstance();
                    break;
            }
            addFragment(page, cacheFragment);
        }
        replace(cacheFragment);
    }

    private void replace(BaseOpFragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fl_content, fragment).commit();

    }


    /**
     * 更新用戶信息
     */
    public static final String BROADCAST_UPDATE_OPERATOR = "com.ebox.ui.OperatorMainActivity.updateOperator";
    /**
     * 切換頁面
     */
    public static final String BROADCAST_CHANGE_TO_FIRST = "com.ebox.ui.OperatorMainActivity.changePage";

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_UPDATE_OPERATOR);
        filter.addAction(BROADCAST_CHANGE_TO_FIRST);
        registerReceiver(updateReceiver, filter);
    }

    //更新信息
    BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(BROADCAST_UPDATE_OPERATOR))
            {
                updateOperatorInfo();
            }
            else if (action.equals(BROADCAST_CHANGE_TO_FIRST))
            {


            }
        }
    };

    private void updateOperatorInfo() {

        RequestUpdateOperatorInfo request = new RequestUpdateOperatorInfo(operatorId, new ResponseEventHandler<RspUpdateOperatorBalance>() {

            @Override
            public void onResponseSuccess(RspUpdateOperatorBalance result) {
                if (result.isSuccess())
                {
                    Long balance = result.getData().getBalance();
                    if (balance != null && balance > 0)
                    {
                        OperatorOp.updateOperatorBalance(operatorId, balance, 0);
                        updateBalance();
                    }
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                LogUtil.e(error.getMessage());
            }
        });

        RequestManager.addRequest(request, null);
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String title = (String) buttonView.getText();
        switch (buttonView.getId()) {
            case R.id.rb_deliver:
                if (isChecked) {
                    updateTitle(title);
                    changePage(0);
                    iv_title.setImageResource(R.drawable.ex_pub_delivery);
                }
                break;
            case R.id.rb_time_out:
                if (isChecked) {
                    updateTitle(title);
                    changePage(1);
                    iv_title.setImageResource(R.drawable.ex_pub_recycle);
                }
                break;
            case R.id.rb_history_order:
                if (isChecked) {
                    updateTitle(title);
                    changePage(2);
                    iv_title.setImageResource(R.drawable.ex_input_item_id);
                }
                break;
            case R.id.rb_recharge:
                if (isChecked) {
                    updateTitle(title);
                    changePage(3);
                    iv_title.setImageResource(R.drawable.ex_pub_pay);
                }
                break;
            case R.id.rb_change_pwd:
                if (isChecked) {
                    updateTitle(title);
                    changePage(4);
                    iv_title.setImageResource(R.drawable.ex_pub_changep);
                }
                break;
            case R.id.rb_download:
                if (isChecked) {
                    updateTitle(title);
                    changePage(5);
                    iv_title.setImageResource(R.drawable.ex_pub_download);
                }
                break;
            case R.id.rb_help:
                if (isChecked) {
                    updateTitle(title);
                    changePage(6);
                    iv_title.setImageResource(R.drawable.ex_pub_help);
                }
                break;

        }


    }
}
