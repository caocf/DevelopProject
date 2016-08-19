package com.xhl.bqlh.model.event;

/**
 * Created by Summer on 2016/7/18.
 */
public class AdEvent {

    public static final int type_scan = 1;
    public static final int type_shop = 2;
    public static final int type_product = 3;
    public static final int type_web = 4;

    public int adType;//类型

    public String title;//标题
    public String data;//数据

}
