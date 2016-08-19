package com.xhl.bqlh.business.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 16/1/16.
 */
public class OrderSaveModel implements Serializable {

    private List<OrderModel> orderList;

    private int result;

    public List<OrderModel> getOrderList() {
        return orderList;
    }

    public int getResult() {
        return result;
    }
}
