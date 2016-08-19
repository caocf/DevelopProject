package com.xhl.bqlh.business.view.ui.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/6/20.
 * 蓝牙设备列表
 */
@ContentView(R.layout.activity_bluetooth_device_list)
public class BTDeviceListActivity extends BaseAppActivity {

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;

    @ViewInject(R.id.btn_last)
    private Button btn_last;

    @ViewInject(R.id.btn_scan)
    private Button btn_scan;

    @Event(R.id.btn_scan)
    private void onScanClick(View view) {
        if (scanEnable) {

            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (null == adapter) {
                finish();
            }
            if (!adapter.isEnabled()) {
                if (adapter.enable()) {
                    while (!adapter.isEnabled()) ;
                } else {
                    ToastUtil.showToastLong(R.string.bluetooth_error);
                    finish();
                }
            }
            ll_content.removeAllViews();
            //搜索蓝牙
            adapter.cancelDiscovery();
            adapter.startDiscovery();
        }
    }

    private BroadcastReceiver broadcastReceiver = null;

    private boolean scanEnable = true;//是否可以扫描

    private int mScanSize;//扫描到的设备数

    @Override
    protected void initParams() {
        initToolbar(TYPE_child_other_clear);
        setTitle(R.string.bluetooth_device_connect);
        initBroadcast();
        lastBlue();
    }

    private void lastBlue() {
        final String nameGet = PreferenceData.getInstance().bluetoothNameGet();
        final String addressGet = PreferenceData.getInstance().bluetoothAddressGet();
        if (!TextUtils.isEmpty(addressGet)) {
            btn_last.setText("上次链接:" + nameGet);
            btn_last.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectAddress(addressGet, nameGet);
                }
            });
        } else {
            btn_last.setVisibility(View.GONE);
        }
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    final String address = device.getAddress();
                    String name = device.getName();
                    if (name == null || name.equals(address)) {
                        name = "蓝牙";
                    }
                    Button button = new Button(context);
                    button.setText(name + ": " + address);
                    button.setGravity(android.view.Gravity.CENTER_VERTICAL | Gravity.START);
                    final String finalName = name;

                    button.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View arg0) {
                            connectAddress(address, finalName);
                        }
                    });
                    button.getBackground().setAlpha(100);
                    ll_content.addView(button);
                    mScanSize++;
                    Logger.v("find:" + name);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                    scanEnable = false;
                    btn_scan.setText("扫描设备中");
                    btn_scan.setEnabled(false);
                    mScanSize = 0;

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                    scanEnable = true;
                    btn_scan.setEnabled(true);
                    btn_scan.setText(R.string.bluetooth_device_scan);
                    if (mScanSize == 0) {
                        SnackUtil.shortShow(mToolbar, "未检测到蓝牙设备");
                    }
                }
            }

        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void connectAddress(final String address, final String finalName) {
        //save data
        PreferenceData.getInstance().bluetoothAddressSave(address);
        PreferenceData.getInstance().bluetoothNameSave(finalName);
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                //返回数据
                Intent data = new Intent();
                data.putExtra("address", address);
                data.putExtra("name", finalName);
                setResult(RESULT_OK, data);
                finish();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
}
