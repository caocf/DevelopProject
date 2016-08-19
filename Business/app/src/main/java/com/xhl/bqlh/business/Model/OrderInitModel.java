package com.xhl.bqlh.business.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 16/1/16.
 */
public class OrderInitModel implements Serializable {

    private List<OrderModel> orderList;
    private String allProductNum;
    private String allMoney;
    private String couponMoney;

    public List<OrderModel> getOrderList() {
        return orderList;
    }

    public String getAllProductNum() {
        return allProductNum;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public String getCouponMoney() {
        return couponMoney;
    }
}
