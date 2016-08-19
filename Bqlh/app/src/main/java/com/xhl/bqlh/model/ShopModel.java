package com.xhl.bqlh.model;

import android.text.TextUtils;

import com.xhl.bqlh.AppConfig.NetWorkConfig;

import java.io.Serializable;


/**
 * Created by Sum on 16/1/9.
 */
public class ShopModel implements Serializable {

    private String sid;
    private String state;
    private String uid;
    private String shopName;
    private String shopLogo;
    private String shopStar;
    private String shopUrl;
    private String info;//店铺介绍
    private String applyTime;//店铺申请时间
    private String createTime;//店铺创建时间
    private String endTime;//店铺关闭时间
    private String operateStatus;//
    private String bail;//保证金 0:未交 1：已交
    private String collectNum;//收藏数量
    private String region;//配送范围
    private String liablePhone;
    private String liableName;
    private int productCollected;
    private int shopCollected;

    //收藏的属性
    private String id;
    private String shopId;

    public String getShopId() {
        return sid;
    }

    public String getShopLogo() {
        return NetWorkConfig.imageHost + shopLogo;
    }

    public String getShopName() {
        if (TextUtils.isEmpty(shopName))
            return "";
        return shopName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public String getCollectNum() {
        if (TextUtils.isEmpty(collectNum)) {
            return "0";
        }
        return collectNum;
    }

    public String getId() {
        return id;
    }

    public String getCollectionShopId(){
        return shopId;
    }

    public String getBail() {
        return bail;
    }

    public String getRegion() {
        return region;
    }

    public String getLiablePhone() {
        return liablePhone;
    }

    public String getLiableName() {
        return liableName;
    }

    public int getProductCollected() {
        return productCollected;
    }

    public int getShopCollected() {
        return shopCollected;
    }
}
