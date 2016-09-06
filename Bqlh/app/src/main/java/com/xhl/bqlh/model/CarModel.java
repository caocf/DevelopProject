package com.xhl.bqlh.model;

import android.text.TextUtils;

/**
 * Created by Sum on 16/7/8.
 */
public class CarModel {

    public boolean isChecked;//购物车是否选择
    public int mCurNum;//当前数量

    private String id;
    private String buyerId;
    private String sellerId;
    private String sellerName;
    private String shopName;
    private String productId;
    private float minOrderPrice;//起订金额
    private String promoteRemark;//活动提示
    private ProductModel product;
    private float memberPrice;
    private float productPrice;//商品价格
    private int orderMinNum;//商品最小起定量
    private int purchaseQuantity;//商品最数量

    public String getId() {
        return id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getShopName() {
        return shopName;
    }

    public String getProductId() {
        return productId;
    }

    public float getMinOrderPrice() {
        return minOrderPrice;
    }

    public String getPromoteRemark() {
        if (TextUtils.isEmpty(promoteRemark)) {
            return "";
        }
        return promoteRemark;
    }

    public ProductModel getProduct() {
        return product;
    }

    public float getProductPrice() {
        if (memberPrice != 0) {
            return memberPrice;
        }
        return productPrice;
    }

    public int getOrderMinNum() {
        return orderMinNum;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }
}
