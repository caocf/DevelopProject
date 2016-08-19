package com.xhl.bqlh.business.view.ui.activity.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.BluetoothPrinterImpl;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.Global;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.PrinterDevice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/21.
 */
public class PrinterService extends Service {

    private static PrinterDevice mPrinter;
    //通过后台服务保持和打印机的链接
    private static List<Handler> mTargetHandler = new ArrayList<>(2);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //将所有结果返回给service里面Handler处理
        MHandler handler = new MHandler(this);
        mPrinter = new BluetoothPrinterImpl(handler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = Message.obtain();
        msg.what = Global.MSG_SERVICE_READ;
        notifyHandlers(msg);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPrinter.disConnect();
        Message msg = Message.obtain();
        msg.what = Global.MSG_SERVICE_STOP;
        notifyHandlers(msg);
    }

    public static void connect(String address) {
        if (mPrinter != null) {
            mPrinter.connect(address);
        }else {
            //发消息，重启服务
            Message msg = Message.obtain();
            msg.what = Global.MSG_SERVICE_START;
            notifyHandlers(msg);
        }
    }

    public static boolean isConnect() {
        return mPrinter.isConnect();
    }

    public static void print(Bundle bundle) {
        mPrinter.print(bundle);
    }

    static class MHandler extends Handler {

        WeakReference<PrinterService> mService;

        public MHandler(PrinterService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            notifyHandlers(msg);
        }
    }

    /**
     * @param handler
     */
    public static void addHandler(Handler handler) {
        if (!mTargetHandler.contains(handler)) {
            mTargetHandler.add(handler);
        }
    }

    /**
     * @param handler
     */
    public static void removeHandler(Handler handler) {
        if (mTargetHandler.contains(handler)) {
            mTargetHandler.remove(handler);
        }
    }

    /**
     * @param msg
     */
    public static void notifyHandlers(Message msg) {
        for (int i = 0; i < mTargetHandler.size(); i++) {
            Message message = Message.obtain(msg);
            mTargetHandler.get(i).sendMessage(message);
        }
    }

}
