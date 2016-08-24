package com.xhl.world.model;

import com.xhl.world.config.NetWorkConfig;

/**
 * Created by Sum on 16/1/15.
 */
public class EvaluateModel {
    //评价参数
    private String storeOrderCode;
    private String companyId;
    private String goodsId;
    private String rate;
    private String rateContenet;
    private String rateImg;

    //extend 商品评价
    private String userName;
    private String vipPic;
    private String createTime;
    private String returnEvaluate;
    private String returnType;

    public String getRateContenet() {
        return rateContenet;
    }

    public String getUserName() {
        return userName;
    }

    public String getVipPic() {
        return NetWorkConfig.imageHost + vipPic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getReturnEvaluate() {
        return returnEvaluate;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getStoreOrderCode() {
        return storeOrderCode;
    }

    public void setStoreOrderCode(String storeOrderCode) {
        this.storeOrderCode = storeOrderCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateContent() {
        return rateContenet;
    }

    public void setRateContent(String rateContent) {
        this.rateContenet = rateContent;
    }

    public String getRateImg() {
        return rateImg;
    }

    public void setRateImg(String rateImg) {
        this.rateImg = rateImg;
    }

}
