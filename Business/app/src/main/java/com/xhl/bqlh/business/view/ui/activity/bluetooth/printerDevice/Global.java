package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerDevice;

/**
 * 打印的TAG
 */
public class Global {

    public static final int START = 10000;
    //链接结果
    public static final int MSG_PRINTERTHREAD_SEND_CONNECTTING = START + 1;
    public static final int MSG_PRINTERTHREAD_SEND_CONNECT_FAILED = START + 2;
    public static final int MSG_PRINTERTHREAD_SEND_CONNECT_SUCCESS = START + 3;
    public static final int MSG_PRINTERTHREAD_SEND_PRINT_SUCCESS = START + 4;
    public static final int MSG_PRINTERTHREAD_SEND_PRINT_FAILED = START + 5;
    public static final int MSG_PRINTERTHREAD_SEND_BT_COLSE = START + 6;

    //链接蓝牙
    public static final int MSG_PRINTERTHREAD_HANDLER_CONNECT = START + 10;
    //打印
    public static final int MSG_PRINTERTHREAD_HANDLER_PRINT = START + 11;

    //后台服务准备完成
    public static final int MSG_SERVICE_READ = START + 12;
    //后台服务关闭
    public static final int MSG_SERVICE_STOP = START + 13;
    //重启打印服务服务
    public static final int MSG_SERVICE_START = START + 14;


    // Bundle data使用
    public static final String BYTESPARA1 = "bytespara1";
    public static final String INTPARA1 = "intpara1";
    public static final String INTPARA2 = "intpara2";

}
