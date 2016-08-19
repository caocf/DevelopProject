package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import java.io.Serializable;

public class ProductReturnDetail implements Serializable {

    private String id;//主键
    private String returnId;//退货表主键
    private String productId;//商品id
    private String productName;//商品名称
    private String applyReturnPrice;//申请退货价格
    private String verifyReturnPrice;//核定退货价格
    private String num;//数量

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getApplyReturnPrice() {
        return applyReturnPrice;
    }

    public void setApplyReturnPrice(String applyReturnPrice) {
        this.applyReturnPrice = applyReturnPrice;
    }

    public String getVerifyReturnPrice() {
        if (TextUtils.isEmpty(verifyReturnPrice)) {
            return "待确认";
        }
        return verifyReturnPrice;
    }

    public void setVerifyReturnPrice(String verifyReturnPrice) {
        this.verifyReturnPrice = verifyReturnPrice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
