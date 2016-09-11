package com.ebox.mgt.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.TrafficStats;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mgt.ui.fragment.BoxRackNumSetFragment;
import com.ebox.mgt.ui.fragment.FragmentAlarm;
import com.ebox.mgt.ui.fragment.FragmentBoxInfo;
import com.ebox.mgt.ui.fragment.FragmentDeliver;
import com.ebox.mgt.ui.fragment.FragmentOrder;
import com.ebox.mgt.ui.fragment.FragmentUser;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

public class SqliteQueryActivity extends CommonActivity implements OnCheckedChangeListener {

    private RadioButton rb_userInfo, rb_deliver, rb_order, rb_alarm;
    private RadioButton rb_timeOut, rb_boxInfo, rb_rack_num;

    private TextView tv_traffic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgt_activity_sqlite_query);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initView();
    }

    private void initView() {
        rb_userInfo = (RadioButton) findViewById(R.id.rb_userInfo);
        rb_userInfo.setOnCheckedChangeListener(this);
        rb_userInfo.setChecked(true);
        rb_deliver = (RadioButton) findViewById(R.id.rb_deliver);
        rb_deliver.setOnCheckedChangeListener(this);
        rb_order = (RadioButton) findViewById(R.id.rb_order);
        rb_order.setOnCheckedChangeListener(this);
        rb_alarm = (RadioButton) findViewById(R.id.rb_alarm);
        rb_alarm.setOnCheckedChangeListener(this);
        rb_timeOut = (RadioButton) findViewById(R.id.rb_timeOut);
        rb_timeOut.setOnCheckedChangeListener(this);
        rb_boxInfo = (RadioButton) findViewById(R.id.rb_boxinfo);
        rb_boxInfo.setOnCheckedChangeListener(this);
        rb_rack_num = (RadioButton) findViewById(R.id.rb_rack_num);
        rb_rack_num.setOnCheckedChangeListener(this);
        tv_traffic = (TextView) findViewById(R.id.tv_traffic);

        rb_order.setChecked(true);
        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.mgt_query_sqlite);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
        tv_traffic.setText(getTraffic());
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_rack_num:
                if (isChecked) {
                    startSetRackNumFragment();
                }
                break;

            case R.id.rb_userInfo:
                if (isChecked) {
                    startUserInfoFragment();
                }
                break;

            case R.id.rb_deliver:
                if (isChecked) {
                    startDeliverFragment();
                }
                break;

            case R.id.rb_alarm:
                if (isChecked) {
                    startAlarmFragment();
                }
                break;

            case R.id.rb_order:
                if (isChecked) {
                    startOrderFragment();
                }
                break;
            case R.id.rb_timeOut:
                if (isChecked) {
                  //  startTimeOutFragment();
                }
                break;
            case R.id.rb_boxinfo:
                if (isChecked) {
                    startBoxInfoFragment();
                }
                break;
        }
    }

    private void startBoxInfoFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("BoxInfo");
        if (fragment == null) {
            fragment = new FragmentBoxInfo();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "BoxInfo").commit();


    }


    private void startAlarmFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("Alarm");
        if (fragment == null) {
            fragment = new FragmentAlarm();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "Alarm").commit();

    }

    private void startOrderFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("Order");
        if (fragment == null) {
            fragment = new FragmentOrder();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "Order").commit();

    }

    private void startDeliverFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("Deliver");
        if (fragment == null) {
            fragment = new FragmentDeliver();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "Deliver").commit();

    }

    private void startUserInfoFragment() {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("UserInfo");
        if (fragment == null) {
            fragment = new FragmentUser();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "UserInfo").commit();
    }

    private void startSetRackNumFragment() {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;
        fragment = manager.findFragmentByTag("rackNumb");
        if (fragment == null) {
            fragment = new BoxRackNumSetFragment();
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(R.id.fl_content, fragment, "rackNumb").commit();
    }


    private String getTraffic() {
        StringBuffer buffer = new StringBuffer();
        int uid= AppApplication.getInstance().getAppUid();

        long uidRxBytes = TrafficStats.getUidRxBytes(uid);//app接受的字节数
        long uidTxBytes = TrafficStats.getUidTxBytes(uid);//app发送的字节数

        long totalRxBytes = TrafficStats.getTotalRxBytes();//系统全部接受的字节数
        long totalTxBytes = TrafficStats.getTotalTxBytes();//系统全部发送的字节数

        long uidKb = (uidRxBytes + uidTxBytes) / 1024;
        buffer.append("EBox流量统计\n").append("用量:").append(uidKb).append(" KB\n");
        long totalKb = (totalRxBytes + totalTxBytes) / 1024;
        buffer.append("\n系统流量统计\n").append("用量:").append(totalKb).append(" KB\n");

        return buffer.toString();
    }

}
