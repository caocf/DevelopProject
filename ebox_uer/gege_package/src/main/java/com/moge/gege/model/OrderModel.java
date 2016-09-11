package com.moge.gege.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int status;
    private String address_id;
    private String uid;
    private int total_num;
    String order_id;
    int delivery_fee;
    String order_at;
    String mobile;
    int total_fee;
    String merchant_id;
    String status_verbose_name;
    AddressModel address;

    ArrayList<GoodItemModel> goods;
    CouponModel coupon;
    int pay_fee;
    int pay_type;
    String pay_id;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(int delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getOrder_at() {
        return order_at;
    }

    public void setOrder_at(String order_at) {
        this.order_at = order_at;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStatus_verbose_name() {
        return status_verbose_name;
    }

    public void setStatus_verbose_name(String status_verbose_name) {
        this.status_verbose_name = status_verbose_name;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public ArrayList<GoodItemModel> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<GoodItemModel> goods) {
        this.goods = goods;
    }

    public CouponModel getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponModel coupon) {
        this.coupon = coupon;
    }

    public int getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(int pay_fee) {
        this.pay_fee = pay_fee;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }
}
