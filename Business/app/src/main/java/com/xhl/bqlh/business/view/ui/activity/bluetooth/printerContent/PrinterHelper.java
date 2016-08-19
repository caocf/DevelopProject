package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent;

import android.os.Bundle;

import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice.Global;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sum on 16/6/21.
 */
public class PrinterHelper {


    private static byte[] byteArraysToBytes(byte[][] data) {
        int length = 0;
        for (int i = 0; i < data.length; i++)
            length += data[i].length;
        byte[] send = new byte[length];
        int k = 0;
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[i].length; j++)
                send[k++] = data[i][j];
        return send;
    }

    public static Bundle createPrinter(String content) {
        byte[] header = new byte[]{0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x39,0x01};
        byte[] buffer = null;
        try {
            byte strbuf[] = content.getBytes("UTF-8");
            buffer = byteArraysToBytes(new byte[][]{header, strbuf});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Bundle data = new Bundle();
        data.putByteArray(Global.BYTESPARA1, buffer);
        data.putInt(Global.INTPARA1, 0);
        data.putInt(Global.INTPARA2, buffer.length);
        return data;
    }

}
