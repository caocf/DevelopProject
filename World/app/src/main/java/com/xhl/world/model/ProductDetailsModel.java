package com.xhl.world.model;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;

import java.util.List;

/**
 * Created by Sum on 16/1/22.
 */
public class ProductDetailsModel {

    public ShopModel getShop() {
        return shop;
    }

    public String getProductId() {
        return productId;
    }

    public String getStock() {
        if (TextUtils.isEmpty(stock)) {
            return "0";
        }
        return stock;
    }

    public String getProductType() {
        return productType;
    }

    public boolean isCollection() {
        if (TextUtils.isEmpty(resultCollect)) {
            return false;
        }
        return resultCollect.equals("1");
    }

    public String getProductDesc() {
        return productDesc;
    }

    static class attrMap {
        String name;
        String value;
    }

    private String evaluateTotalNum;//评价数
    private String retailPrice;//现价
    private String originalPrice;//原价
    private String carriage;//邮费
    private String productId;
    private String productPic;
    private String productName;
    private String productDesc;//商品富媒体描述
    private String promiseTime;//保障时间
    private String productProperty;//商品类型
    private String productType;//商品类型
    private String stock;//库存
    private String resultCollect;//是否收藏 1：已经收藏 0:未收藏

    private ShopModel shop;//店铺

    private List<ProductAttachModel> attachment;//关联的商品详情图片

    private List<attrMap> resultMap;

    public String getEvaluateTotalNum() {
        return evaluateTotalNum;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getCarriage() {
        return carriage;
    }

    public String getProductPic() {
        return NetWorkConfig.imageHost + productPic;
    }

    public String getProductName() {
        return productName;
    }

    public String getPromiseTime() {
        return promiseTime;
    }

    public String getProductProperty() {
        return productProperty;
    }

    public List<ProductAttachModel> getAttachment() {
        return attachment;
    }

    public List<attrMap> getResultMap() {
        return resultMap;
    }
}
