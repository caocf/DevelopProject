package com.xhl.world.model;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;

import java.io.Serializable;

/**
 * Created by Sum on 16/1/6.
 */
public class ProductModel implements Serializable {

    private String productId;
    private String productName;
    private String productPic;
    private String originalPrice;//会员价
    private String productDesc;

    public String getProductId() {
        return productId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getProductName() {
        if (TextUtils.isEmpty(productName)) {
            return "";
        }
        return productName;
    }

    //统一商品图片URL返回
    public String getProductPicUrl() {
        return NetWorkConfig.imageHost + productPic;
    }

}
