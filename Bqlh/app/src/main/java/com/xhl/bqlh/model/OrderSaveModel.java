package com.xhl.bqlh.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 16/1/16.
 */
public class OrderSaveModel implements Serializable {

    private List<OrderModel> orderList;

    private int result;

    private BlanketOrder blanketOrder;

    public List<OrderModel> getOrderList() {
        return orderList;
    }

    public int getResult() {
        return result;
    }

    public BlanketOrder getBlanketOrder() {
        return blanketOrder;
    }
}
