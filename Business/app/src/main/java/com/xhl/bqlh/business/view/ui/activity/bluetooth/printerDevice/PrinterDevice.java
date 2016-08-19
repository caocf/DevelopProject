package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice;

import android.os.Bundle;

/**
 * Created by Sum on 16/6/21.
 */
public interface PrinterDevice {

    /**
     * 链接
     *
     * @param address 链接地址
     */
    void connect(String address);

    /**
     * 断开链接
     */
    void disConnect();

    /**
     * @return 是否连接中
     */
    boolean isConnect();

    /**
     * 打印
     *
     * @param bundle 打印参数
     */
    void print(Bundle bundle);
}
