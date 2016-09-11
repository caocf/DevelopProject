package com.moge.gege.model;

import java.util.List;

public class ScanEboxDataModel{
    private List<ScanEboxOrderModel> orders;

    private String access_token;

    public List<ScanEboxOrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<ScanEboxOrderModel> orders) {
        this.orders = orders;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
