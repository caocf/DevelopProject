package com.xhl.world.model;

/**
 * Created by Sum on 16/3/4.
 * 优惠券
 */
public class Coupon {

    private String startTime;
    private int amount;
    private String id;//用户领取的优惠券id(创建订单使用该id)
    private String title;//使用条件说明
    private String couponName;//优惠券名称
    private String status;//A:未使用，U:已使用，O:已超期，X:已经下架
    private String minumum;
    private String endTime;
    private String couponId;//平台的优惠id
    private String provision;//使用范围说明

    public int getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCouponName() {
        return couponName;
    }

    public String getStatus() {
        return status;
    }

    public String getMinumum() {
        return minumum;
    }


    public String getEndTime() {
        return endTime;
    }


    public String getProvision() {
        return provision;
    }


    public String getStartTime() {
        return startTime;
    }

}
