package com.xhl.bqlh.business.view.ui.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.Global;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent.PrinterHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/6/20.
 * 蓝牙打印界面
 */
@ContentView(R.layout.activity_bluetooth_printer)
public class BTPrinterActivity extends BaseAppActivity {

    //打印的内容
    public static final String TAG_PRINT_CONTENT = "print_content";

    private boolean mConnected;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case Global.MSG_SERVICE_START:
                    //启动服务
                    Intent intent = new Intent(BTPrinterActivity.this, PrinterService.class);
                    startService(intent);
                    break;

                case Global.MSG_SERVICE_STOP:
                    tv_state.setText("已断开连接: " + mBTInfo);
                    btn_print.setEnabled(false);
                    btn_un_connect.setEnabled(true);
                    //再次启动服务，在断开后
                    btn_un_connect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BTPrinterActivity.this, PrinterService.class);
                            startService(intent);
                        }
                    });
                    break;
                case Global.MSG_SERVICE_READ:
                    if (PrinterService.isConnect()) {
                        connectShow(true);
                    } else {
                        //连接设备
                        PrinterService.connect(mBTAddress);
                    }
                    break;
                case Global.MSG_PRINTERTHREAD_SEND_CONNECTTING:
                    tv_state.setText("正在连接: " + mBTInfo);
                    btn_print.setEnabled(false);
                    btn_un_connect.setEnabled(false);
                    break;
                case Global.MSG_PRINTERTHREAD_SEND_CONNECT_SUCCESS:
                    ToastUtil.showToastShort("连接成功");
                    connectShow(true);
                    break;
                case Global.MSG_PRINTERTHREAD_SEND_CONNECT_FAILED:
                    ToastUtil.showToastShort("连接失败,请检测蓝牙是否打开");
                    connectShow(false);
                    break;
                case Global.MSG_PRINTERTHREAD_SEND_PRINT_SUCCESS:
                    ToastUtil.showToastLong("打印完成");
                    btn_print.setText("再次打印");
                    break;
                case Global.MSG_PRINTERTHREAD_SEND_PRINT_FAILED:
                    ToastUtil.showToastLong("打印异常,请重试");
                    break;
            }
        }
    };

    private void connectShow(boolean connect) {
        btn_print.setEnabled(connect);
        btn_un_connect.setEnabled(true);
        if (connect) {
            tv_state.setText("已连接: " + mBTInfo);
            btn_un_connect.setText(R.string.bluetooth_device_disconnect);
            btn_un_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //停止服务
                    stopService(new Intent(BTPrinterActivity.this, PrinterService.class));
                }
            });
        } else {
            tv_state.setText("连接失败: " + mBTInfo);
            btn_un_connect.setText(R.string.bluetooth_device_reconnect);
            btn_un_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrinterService.connect(mBTAddress);
                }
            });
        }
    }

    @ViewInject(R.id.tv_state)
    private TextView tv_state;

    @ViewInject(R.id.tv_preview)
    private TextView tv_preview;

    @ViewInject(R.id.btn_print)
    private Button btn_print;

    @ViewInject(R.id.btn_un_connect)
    private Button btn_un_connect;//断开连接，重新连接

    private String mBTInfo;
    private String mBTAddress;

    @Event(R.id.btn_connect)
    private void onConnectClick(View view) {
        startActivityForResult(new Intent(this, BTDeviceListActivity.class), 1);
    }

    @Event(R.id.btn_print)
    private void onPrintClick(View view) {
        Bundle bundle = PrinterHelper.createPrinter(mPrint);
        //打印
        PrinterService.print(bundle);

    }

    private String mPrint;

    @Override
    protected void initParams() {
        initToolbar();
        setTitle(R.string.order_details_print);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            ToastUtil.showToastLong(R.string.bluetooth_error);
            finish();
            return;
        }
        //蓝牙打印
        showAddress();

        //启动服务
        Intent intent = new Intent(this, PrinterService.class);
        startService(intent);

        //注册监听
        PrinterService.addHandler(mHandler);

        //打印内容
        String print = getIntent().getStringExtra(TAG_PRINT_CONTENT);
//        if (!TextUtils.isEmpty(print)) {
//            tv_preview.setText(print);
//        }
        mPrint = print;
    }

    private void showAddress() {
        String address = PreferenceData.getInstance().bluetoothAddressGet();
        mBTAddress = address;
        String name = PreferenceData.getInstance().bluetoothNameGet();
        if (TextUtils.isEmpty(address)) {
            tv_state.setText("未连接设备");
        } else {
            mBTInfo = name;
            tv_state.setText(name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        PrinterService.removeHandler(mHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //链接设备
        if (requestCode == 1) {
            mBTAddress = data.getStringExtra("address");
            mBTInfo = data.getStringExtra("name");
            //连接设备
            PrinterService.connect(mBTAddress);
        }
    }
}
