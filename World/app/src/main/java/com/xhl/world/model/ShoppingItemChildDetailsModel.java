package com.xhl.world.model;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/14.
 */
public class ShoppingItemChildDetailsModel implements Serializable {

    private String goods_tag;
    private String car_id;//根据id删除购物车商品
    private String seller_id;//根据id删除购物车商品
    private String goods_typ;//商品类型
    private String goods_icon;
    private String goods_title;
    private String goods_price;
    private String goods_old_price;
    private int goods_count;
    private int goods_max_limit;
    private int goods_in_car;//0 不在购物车结算订单中，1 在购物车结算订单中

    public String getGoods_icon() {
        return goods_icon;
    }

    public void setGoods_icon(String goods_icon) {
        this.goods_icon = goods_icon;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_old_price() {
        return goods_old_price;
    }

    public void setGoods_old_price(String goods_old_price) {
        this.goods_old_price = goods_old_price;
    }

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public int getGoods_max_limit() {
        return goods_max_limit;
    }

    public void setGoods_max_limit(int goods_max_limit) {
        this.goods_max_limit = goods_max_limit;
    }

    public String getGoods_id() {
        return goods_tag;
    }

    public void setGoods_id(String goods_tag) {
        this.goods_tag = goods_tag;
    }


    public void setGoods_in_car(int goods_in_car) {
        this.goods_in_car = goods_in_car;
    }

    public boolean goodsInCar() {
        if (goods_in_car == 0) {
            return false;
        }
        return true;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getGoods_typ() {
        return goods_typ;
    }

    public void setGoods_typ(String goods_typ) {
        this.goods_typ = goods_typ;
    }
}
