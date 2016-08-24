package com.xhl.world.model;

import com.xhl.world.model.serviceOrder.OrderDetail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sum on 16/1/19.
 */
public class InitReturnOrderModel implements Serializable {

    private int size1;
    private int size2;
    private int size3;
    private int size4;

    private ArrayList<ArrayList<OrderDetail>> returnList;

    public int getSize1() {
        return size1;
    }

    public int getSize2() {
        return size2;
    }

    public int getSize3() {
        return size3;
    }

    public int getSize4() {
        return size4;
    }

    public ArrayList<ArrayList<OrderDetail>> getReturnList() {
        return returnList;
    }
}
