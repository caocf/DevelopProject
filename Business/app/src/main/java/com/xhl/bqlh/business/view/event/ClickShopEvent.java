package com.xhl.bqlh.business.view.event;

/**
 * Created by Sum on 16/3/23.
 */
public class ClickShopEvent {

    //店铺id
    public String shopId;

    //店铺类型(拜访的 搜索的)
    public int shopType;

    //和当天的日期差
    public int dayInterval;

    //店铺状态(未拜访和已拜访)
    public int shopTaskState;
    //拜访状态
    public int shopTaskType;

    public String shopName;
    //精确定位
    public double longitude;
    public double latitude;

    //地址定位
    public String city;
    public String address;

}
