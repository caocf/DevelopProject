package com.xhl.bqlh.business.Model;

import java.util.List;

/**
 * Created by Sum on 16/5/14.
 */
public class StatisticsModel {

    private String totalNum;

    private String totalMoney;

    private String realOrderMoney;

    private String couponsMoney;

    private List<StatisticsProductModel> productList;

    private List<StatisticsShopModel> shopList;

    public String getTotalNum() {
        return totalNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public String getRealOrderMoney() {
        return realOrderMoney;
    }

    public List<StatisticsProductModel> getList() {
        return productList;
    }

    public List<StatisticsShopModel> getShopList() {
        return shopList;
    }

    public String getCouponsMoney() {
        return couponsMoney;
    }
}
