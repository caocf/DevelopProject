package com.xhl.world.model;

import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.user.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 16/1/16.
 */
public class InitOrderModel implements Serializable {

    //分单order集合
    private List<Order> orderList;
    private String allProductNum;
    private String allMoney;//分单价格
    private String couponMoney;

    //未分单的order集合
    private List<Order> orderList2;
    private String allProductNum2;
    private String allMoney2;///未分单价格
    private String couponMoney2;

    private String orderType;
    private AddressModel address;
    private UserInfo user;

    private String orderListType;//订单类型（1.大贸商品，2.行邮商品）

    private List<Coupon> couponList;//订单使用的优惠券

    private Integer integral;//账号积分

    private Float proportion;//积分兑换比

    private Integer upperLimit;//积分使用上限

    public List<Order> getOrderList() {
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

    public List<Order> getOrderList2() {
        return orderList2;
    }

    public String getAllProductNum2() {
        return allProductNum2;
    }

    public String getAllMoney2() {
        return allMoney2;
    }

    public String getCouponMoney2() {
        return couponMoney2;
    }

    public String getOrderType() {
        return orderType;
    }

    public AddressModel getAddressList() {
        return address;
    }

    public UserInfo getUser() {
        return user;
    }

    public Integer getIntegral() {
        return integral;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public Float getProportion() {
        return proportion;
    }

    public void setProportion(Float proportion) {
        this.proportion = proportion;
    }
}
