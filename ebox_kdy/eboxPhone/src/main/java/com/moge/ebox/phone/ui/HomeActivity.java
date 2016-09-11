package com.moge.ebox.phone.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.model.UserInfo;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.utils.JsonUtils;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout rl_scand_send, rl_empty_query;
    private RelativeLayout rl_delivery_query, rl_order_query;
    private RelativeLayout rl_quick_recharge, rl_history_send;
    private RelativeLayout rl_ssqd, rl_setting;
    private TextView tv_account_money, tv_express_count, tv_time_out_count, tv_express_count_title;
    private Context mContext;
    private PullToRefreshScrollView mRefreshScrollView;
    private int mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = HomeActivity.this;
        setContentView(R.layout.activity_home);
        initView();
        initData();
        register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //需要重新获得快递员信息
        reFreshUserInfor();
        //显示余额
        showBalance("");
        loadbalance();
    }

    private void reFreshUserInfor() {
        Map params = new HashMap();
//        params.put("tel", mTel);
        /**
         * 获得快递员详细信息
         */
        ApiClient.getOperatorInfor(EboxApplication.getInstance(), params,
                new ClientCallback() {
                    @Override
                    public void onSuccess(JSONArray dataPre, int code) {

                        try {
                            JSONObject data = dataPre.getJSONObject(0).getJSONObject("user");
                            Logger.i("HomeActivity:refresh222+" + data.toString());
                            UserInfo userInfo = new UserInfo();
                            Logger.i("HomeActivity:userinfo+" + data.toString());
                            userInfo = userInfo.parse(data.toString());
                            if (userInfo != null) {
                                EboxApplication.getInstance().modifyLoginInfo(userInfo);
                                showBalance("");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                    }
                });
    }

    private void initData() {
        mRefreshScrollView.setMode(Mode.PULL_FROM_START);
        mRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                showBalance("");
                loadbalance();
            }
        });
        loadbalance();
//        loadTimeOutCount();
    }


    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalConfig.INTENT_ACTION_UPDATE);
        filter.addAction(GlobalConfig.INTENT_ACTION_TIMEOUT_COUNT);
        registerReceiver(receiver, filter);
    }


    private void initView() {
        mRefreshScrollView = findviewById_(R.id.scrollView);
        tv_time_out_count = findviewById_(R.id.tv_time_out_count);
        tv_time_out_count.setVisibility(View.INVISIBLE);
        rl_scand_send = findviewById_(R.id.rl_scand_send);
        rl_scand_send.setOnClickListener(this);
        rl_empty_query = findviewById_(R.id.rl_empty_query);
        rl_empty_query.setOnClickListener(this);
        rl_delivery_query = findviewById_(R.id.rl_delivery_query);
        rl_delivery_query.setOnClickListener(this);
        rl_order_query = findviewById_(R.id.rl_order_query);
        rl_order_query.setOnClickListener(this);
        rl_quick_recharge = findviewById_(R.id.rl_quick_recharge);
        rl_quick_recharge.setOnClickListener(this);
        rl_history_send = findviewById_(R.id.rl_history_send);
        rl_history_send.setOnClickListener(this);
        rl_ssqd = findviewById_(R.id.rl_ssqd);
        rl_ssqd.setOnClickListener(this);
        rl_setting = findviewById_(R.id.rl_setting);
        rl_setting.setOnClickListener(this);
        tv_account_money = findviewById_(R.id.tv_account_money);
        tv_express_count = findviewById_(R.id.tv_express_count);
        tv_express_count_title = findviewById_(R.id.tv_express_count_title);
//        //显示派件总数
//        showMsg(0);

    }

    private void showMsg(int count) {

        if (count != 0) {
            tv_express_count.setText("" + count);
            tv_express_count_title.setVisibility(View.VISIBLE);
            tv_express_count.setVisibility(View.VISIBLE);
            Logger.i("HomeActivity:显示");
        } else {
            //隐藏控件
            tv_express_count.setVisibility(View.GONE);
            tv_express_count_title.setVisibility(View.GONE);
        }
    }

    private String getMoney(String sMoney) {

        Integer money = Integer.valueOf(sMoney);

        return money / 100 + "." + String.format("%02d", money % 100) + "元";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_scand_send:

                break;
            case R.id.rl_empty_query:
                start(QueryEmptyActivity.class);
                break;
            case R.id.rl_delivery_query:
                start(QueryTimeOutActivity.class);
                hideCount();
                break;
            case R.id.rl_order_query:
                start(QuerykdActivity.class);
                break;
            //快速充值
            case R.id.rl_quick_recharge:
                start(FastRechargeChooseActivity.class);
                break;
            case R.id.rl_history_send:
                start(QueryHistoryActivity.class);
                break;
            case R.id.rl_ssqd:

                break;
            case R.id.rl_setting:
                start(SettingActivity.class);
                break;
        }
    }

    // 处理按钮点击事件派发
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();// 记录点击时间
                ToastUtil.showToastShort("再次点击退出程序");
            } else {
                EboxApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void start(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, cls);
        startActivity(intent);
        //overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(GlobalConfig.INTENT_ACTION_UPDATE)) {
                loadbalance();
            } else if (action.equals(GlobalConfig.INTENT_ACTION_TIMEOUT_COUNT)) {
                showCount();
            }
        }
    };

    private void showCount() {

        if (mTotal > 0) {
            tv_time_out_count.setText(mTotal + "");
            tv_time_out_count.setVisibility(View.VISIBLE);
            tv_time_out_count.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_show));
        } else {
            hideCount();
        }
    }

    private void hideCount() {
        mTotal = 0;
        if (tv_time_out_count.getVisibility() == View.VISIBLE) {
            tv_time_out_count.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_hide));
            tv_time_out_count.setVisibility(View.INVISIBLE);
        }
    }

    private void loadTimeOutCount() {
        Map params = new HashMap();
//		params.put("tel", EboxApplication.getInstance().getLoginPhone());
        params.put("page", 0);
        params.put("page_size", 10);

        /**
         *  查询超时快递信息
         */
        ApiClient.queryTimeOutExpress(EboxApplication.getInstance(), params,
                new ClientCallback() {
                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        JSONObject object;
                        try {
                            object = data.getJSONObject(0);
                            mTotal = object.getInt("total");
                            sendBroadcast(new Intent(GlobalConfig.INTENT_ACTION_TIMEOUT_COUNT));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                    }
                });
    }

    /**
     * 加载派件总数
     */
    private void loadbalance() {
        Map parmas = new HashMap();
//        String phone = EboxApplication.getInstance().getLoginPhone();
//        parmas.put(KeyValues.user_tel, phone);
        /**
         * 快递派件总数
         */
        ApiClient.queryExpressOrderCount(EboxApplication.getInstance(), parmas, new ClientCallback() {

            @Override
            public void onSuccess(JSONArray data, int code) {
                mRefreshScrollView.onRefreshComplete();
                //显示派件总数
                showData(data);
            }

            @Override
            public void onFailed(byte[] data, int code) {
                mRefreshScrollView.onRefreshComplete();
//                if (code==300){
//                    //未授权访问
//                    Intent loginIntent=new Intent(HomeActivity.this,LoginActivity.class);
//                    startActivity(loginIntent);
//                }
            }
        });

    }

    /**
     * 派件总数显示
     *
     * @param data
     */
    protected void showData(JSONArray data) {
        try {
            JSONObject object = JsonUtils.getJSONOFirstbject(data);
//            int total_count = Integer.valueOf(object.getJSONObject("count").getJSONObject("total_count").toString()).intValue();
            int total_count = object.getJSONObject("count").getInt("total_count");
            Logger.i("HomeActivity:" + total_count);
            showMsg(total_count);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * balance余额显示
     */

    private void showBalance(String balance) {
        UserInfo userInfo = EboxApplication.getInstance().getLoginUserInfo();
        balance = userInfo.balance;
        Logger.i("HomeActivity:balance" + balance);
        if (!TextUtils.isEmpty(balance)) {
            tv_account_money.setText(getMoney(balance));
        } else {
            tv_account_money.setText("--");
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
