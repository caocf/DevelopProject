package com.xhl.bqlh.business.Model;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Sum on 16/4/29.
 */
public class SignConfigModel {

    public String workTime;//开始时间
    public String dutyTime;//结束时间
    public String signPlace;//签到地点
    private int attenceDistance;//签到距离
    private int determine;//是否强制在范围内签到

    private int shopDistance;//店铺距离
    private int shopDetermine;//是否强制在店铺签到范围内签到

    public double coordinateX;//经度
    public double coordinateY;//维度

    public boolean isForceSign() {
        return determine == 1;
    }

    public boolean isForceShopSign() {
        return shopDetermine == 1;
    }

    public int getSignDistance() {
        return attenceDistance;
    }

    public int getShopSignDistance() {
        return shopDistance;
    }

    public LatLng getSignLocation() {
        return new LatLng(coordinateY, coordinateX);
    }

}
