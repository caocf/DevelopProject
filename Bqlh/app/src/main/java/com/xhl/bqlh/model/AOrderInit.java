package com.xhl.bqlh.model;

import java.util.List;

/**
 * Created by Sum on 16/7/12.
 */
public class AOrderInit {

    private String allProductNum;

    private String couponMoney;

    private String allMoney;

    private UserInfo user;

    private List<OrderModel> orderList;

    public String getAllProductNum() {
        return allProductNum;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public UserInfo getUser() {
        return user;
    }

    public List<OrderModel> getOrderList() {
        return orderList;
    }
}
