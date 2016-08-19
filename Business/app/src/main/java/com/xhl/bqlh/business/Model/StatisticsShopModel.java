package com.xhl.bqlh.business.Model;

/**
 * Created by Sum on 16/5/15.
 */
public class StatisticsShopModel {

    private String id;
    private String retailerId;
    private String orderCode;
    private String storeOrderCode;
    private String arrears;
    private String salseManId;
    private String companyName;

    public String getRetailerId() {
        return retailerId;
    }

    public String getArrears() {
        return arrears;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStoreOrderCode() {
        return storeOrderCode;
    }
}
