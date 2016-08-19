package com.xhl.bqlh.model;

import java.io.Serializable;

/**
 * Created by Sum on 16/7/5.
 */
public class ProductSkuInfo implements Serializable{

    private String update_user;
    private String SKU;
    private String product_name;
    private String brand_id;
    private String category_id;
    private String expire_time;
    private String brandName;
    private String id;
    private String unit;
    private String floor;
    private String  update_time;
    private String package_metiral;
    private String create_time;
    private String SKU_desc;
    private String create_user;
    private String product_volume;

    public String getUpdate_user() {
        return update_user;
    }

    public String getSKU() {
        return SKU;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public String getFloor() {
        return floor;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getPackage_metiral() {
        return package_metiral;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getSKU_desc() {
        return SKU_desc;
    }

    public String getCreate_user() {
        return create_user;
    }

    public String getProduct_volume() {
        return product_volume;
    }
}
