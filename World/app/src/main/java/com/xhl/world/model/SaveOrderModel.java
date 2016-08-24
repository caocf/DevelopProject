package com.xhl.world.model;

import com.xhl.world.model.serviceOrder.BlanketOrder;

/**
 * Created by Sum on 16/1/18.
 */
public class SaveOrderModel {
    private String result;
    private String orderCode;
    private BlanketOrder blanketOrder;

    public String getResult() {
        return result;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public BlanketOrder getBlanketOrder() {
        return blanketOrder;
    }

    public boolean createOrderSuccess() {
        if (result.equals("1")) {
            return true;
        }
        return false;
    }
}
