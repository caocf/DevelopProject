package com.xhl.world.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 15/12/14.
 */
public class ShoppingItemDetailsModel implements Serializable {

    private String shop_id;
    private String shop_title;
    private String shop_icon;//店铺图片
    private String shop_url;//店铺连接
    private List<ShoppingItemChildDetailsModel> shop_details;
    private String shop_total_free;
    private String shop_total_postal;
    private String shop_total_revenue;
    private int all_goods_in_car; //0 不全在购物车结算订单中，1 全在购物车结算订单中

    public String getShop_title() {
        return shop_title;
    }

    public void setShop_title(String shop_title) {
        this.shop_title = shop_title;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public String getShop_total_postal() {
        return shop_total_postal;
    }

    public void setShop_total_postal(String shop_total_postal) {
        this.shop_total_postal = shop_total_postal;
    }

    public String getShop_total_revenue() {
        return shop_total_revenue;
    }

    public void setShop_total_revenue(String shop_total_revenue) {
        this.shop_total_revenue = shop_total_revenue;
    }

    public List<ShoppingItemChildDetailsModel> getShop_details() {
        return shop_details;
    }

    public void setShop_details(List<ShoppingItemChildDetailsModel> shop_details) {
        this.shop_details = shop_details;
    }

    public String getShop_total_free() {
        return shop_total_free;
    }

    public void setShop_total_free(String shop_total_free) {
        this.shop_total_free = shop_total_free;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public int getAll_goods_in_car() {
        return all_goods_in_car;
    }

    public void setAll_goods_in_car(int all_goods_in_car) {
        this.all_goods_in_car = all_goods_in_car;
    }

    public boolean goodsAllInCar() {
        if (all_goods_in_car == 0) {
            return false;
        }
        return true;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }
}
