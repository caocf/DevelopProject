package com.xhl.world.model;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.model.serviceOrder.Product;

import java.io.Serializable;

/**
 * Created by Sum on 16/1/8.
 */
public class ShoppingCarProductModel implements Serializable {

    private String id;

    private String buyerId; //买家ID

    private String sellerId; //卖家ID

    private String sellerName;//卖家名称

    private String shopId;

    private String shopName;//店铺名

    private String productId;//商品ID

    private String createTime; //创建时间

    private String updateTime; //更新时间

    private Integer purchaseQuantity; //购买数量

    private String productPrice;//商品价格

    private Integer operateStatus;//店铺状态

    private String shopLogo;

    private String shopUrl;

    // 购物车类型 1：大贸商品  2：行邮商品 3：直邮商品
    private String type;

    private Product product;


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

    public String getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getProductId() {
        return productId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public Integer getOperateStatus() {
        return operateStatus;
    }

    public String getType() {
        return type;
    }

    public Product getProduct() {
        return product;
    }


    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public String getShopLogo() {
        if (TextUtils.isEmpty(shopLogo)) {
            return null;
        }
        return NetWorkConfig.imageHost + shopLogo;
    }

    public String getShopUrl() {
        return shopUrl;
    }

}
