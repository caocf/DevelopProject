package com.xhl.world.model;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;

import java.io.Serializable;

/**
 * Created by Sum on 16/1/9.
 */
public class CollectionModel implements Serializable {
    private String id;//收藏表id
    private String userId;
    private String shopId;
    private String shopName;
    private String shopLogo;
    private String shopUrl;
    private String shopInfo;

    private String productId;
    private String productName;
    private String productPic;
    private String productPrice;//会员价
    private String productDesc;
    private String productNum;//商品数量

    public int mFragmentTag;//viewpager中标示当前是哪一个fragment

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductId() {
        return productId;
    }


    public String getProductName() {
        return productName;
    }

    //统一商品图片URL返回
    public String getProductPicUrl() {
        return NetWorkConfig.imageHost + productPic;
    }


    public String getShopLogo() {
        if (TextUtils.isEmpty(shopLogo)) {
            return null;
        }
        return NetWorkConfig.imageHost + shopLogo;
    }

    public String getShopName() {
        if (TextUtils.isEmpty(shopName))
            return "";
        return shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public String getId() {
        return id;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getShopUrl() {
        return shopUrl;
    }
}
