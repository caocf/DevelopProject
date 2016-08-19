package com.xhl.bqlh.business.Model;

import java.io.Serializable;

/**
 * Created by Summer on 2016/7/27.
 */
public class GiftModel implements Serializable {

    private int giftNum;

    private String id;

    private String giftName;

    private String image;

    private String unit;

    private int stock;

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public SkuModel getSkuResult() {
        SkuModel skuModel = new SkuModel();
        skuModel.setCommisionProportion(0);
        skuModel.setUnit(unit);
        return skuModel;
    }

}
