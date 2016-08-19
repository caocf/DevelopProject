package com.xhl.bqlh.business.view.ui.activity.bluetooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.USBPrinting;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.Global;

/**
 * Created by Sum on 16/6/21.
 */
public class PrinterThread extends HandlerThread {

    private BTPrinting bt;
    private Pos pos = new Pos();
    private Handler workHandler;
    private Handler targetHandler;

    public PrinterThread(String name, Handler handler) {
        super(name);
        targetHandler = handler;
        bt = new BTPrinting();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        workHandler = new WorkHandler();
    }

    public boolean isOpen() {
        return bt.IsOpened();
    }

    public void printCmd(Bundle bundle) {
        if ((null != workHandler)) {
            Message msg = workHandler
                    .obtainMessage(Global.MSG_PRINTERTHREAD_HANDLER_PRINT);
            msg.setData(bundle);
            workHandler.sendMessage(msg);
        }
    }

    public void connectBt(String address) {
        if ((null != workHandler)) {
            Message msg = workHandler
                    .obtainMessage(Global.MSG_PRINTERTHREAD_HANDLER_CONNECT);
            msg.obj = address;
            workHandler.sendMessage(msg);
        } else {
            // 回馈给UI
            targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_CONNECT_FAILED);
        }
    }

    public void disconnectBt() {
        try {
            bt.Close();
            targetHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class WorkHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            bt.PauseHeartBeat();

            switch (msg.what) {
                case Global.MSG_PRINTERTHREAD_HANDLER_CONNECT: {
                    //链接中
                    if (targetHandler != null)
                        targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_CONNECTTING);
                    pos.Set(bt);
                    String BTAddress = (String) msg.obj;
                    boolean result = bt.Open(BTAddress);
                    if (!result) {
                        if (targetHandler != null) {
                            targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_CONNECT_FAILED);
                        }
                    } else {
                        if (targetHandler != null)
                            targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_CONNECT_SUCCESS);
                    }
                    break;
                }
                case Global.MSG_PRINTERTHREAD_HANDLER_PRINT: {
                    Bundle data = msg.getData();
                    byte[] buffer = data.getByteArray(Global.BYTESPARA1);
                    int offset = data.getInt(Global.INTPARA1);
                    int count = data.getInt(Global.INTPARA2);

                    byte recbuf[] = new byte[100];
                    boolean result = false;
                    if (USBPrinting.class.getName().equals(
                            pos.IO.getClass().getName()))
                        result = PrinterService.isConnect();
                    else
                        result = pos.POS_QueryStatus(recbuf, 1000);

                    if (result) {
                        if (pos.IO.Write(buffer, offset, count) == count) {
                            if (targetHandler != null)
                                targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_PRINT_SUCCESS);
                        } else {
                            if (targetHandler != null)
                                targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_PRINT_FAILED);
                        }
                    } else {
                        if (targetHandler != null)
                            targetHandler.sendEmptyMessage(Global.MSG_PRINTERTHREAD_SEND_PRINT_FAILED);
                    }

                    break;
                }
            }

            bt.ResumeHeartBeat();
        }

    }

}
