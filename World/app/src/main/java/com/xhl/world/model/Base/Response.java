package com.xhl.world.model.Base;

import java.util.List;

/**
 * Created by Sum on 16/1/6.
 */
public class Response<T> {

    private int total;
    private List<T> rows;

    private T myCollect;

    private T myFoot;

    public int getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    public T getMyCollect() {
        return myCollect;
    }

    public T getMyFoot() {
        return myFoot;
    }
}
