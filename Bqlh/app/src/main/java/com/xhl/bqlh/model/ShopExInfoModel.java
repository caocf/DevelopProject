package com.xhl.bqlh.model;

import java.util.List;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopExInfoModel {
    private String category;
    private String liablePhone;
    private String deliveryRange;
    private String liableName;
    private String min_order_price;
    private List<BrandModel> brand;

    public String getCategory() {
        return category;
    }

    public String getLiablePhone() {
        return liablePhone;
    }

    public String getDeliveryRange() {
        return deliveryRange;
    }

    public String getMin_order_price() {
        return min_order_price;
    }

    public List<BrandModel> getBrand() {
        return brand;
    }

    public String getLiableName() {
        return liableName;
    }
}
