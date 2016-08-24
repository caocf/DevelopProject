package com.xhl.world.model.Entities;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/16.
 */
public class ShoppingOrderEntities implements Serializable, Cloneable {

    private String goods_id;//productId
    private String goods_type;//productType
    private String car_id;//shoppingCartId
    private String seller_id;//
    private int count;//quantity

    private boolean in_car;
    private float goods_price;
    private String goods_title;
    private String goods_url;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public float getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(float goods_price) {
        this.goods_price = goods_price;
    }

    public ShoppingOrderEntities clone() throws CloneNotSupportedException {
        return (ShoppingOrderEntities) super.clone();
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_sku) {
        this.goods_id = goods_sku;
    }

    public boolean isIn_car() {
        return in_car;
    }

    public void setIn_car(boolean in_car) {
        this.in_car = in_car;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
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

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }
}
