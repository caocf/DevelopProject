package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.xhl.bqlh.business.view.ui.activity.bluetooth.PrinterThread;

/**
 * Created by Sum on 16/6/21.
 */
public class BluetoothPrinterImpl implements PrinterDevice {

    private PrinterThread printerThread;

    private String mCurAddress;

    public BluetoothPrinterImpl(Handler handler) {
        printerThread = new PrinterThread("printer", handler);
        printerThread.start();
    }

    @Override
    public void connect(String address) {
        if (TextUtils.isEmpty(address)) {
            return;
        }
        if (!printerThread.isOpen()) {
            printerThread.connectBt(address);
            mCurAddress = address;
        } else {
            //同一个地址返回
            if (mCurAddress.equals(address)) {
                return;
            }
            //关闭重新连接
            printerThread.disconnectBt();
            printerThread.connectBt(address);
        }
    }

    @Override
    public void disConnect() {
        printerThread.disconnectBt();
        printerThread.quit();
        printerThread = null;
    }

    @Override
    public boolean isConnect() {
        return printerThread.isOpen();
    }

    @Override
    public void print(Bundle bundle) {
        printerThread.printCmd(bundle);
    }
}
