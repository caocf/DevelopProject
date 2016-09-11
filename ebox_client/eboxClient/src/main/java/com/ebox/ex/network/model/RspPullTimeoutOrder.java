package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;

import java.util.List;

/**
 * Created by Android on 2015/10/10.
 */
public class RspPullTimeoutOrder extends BaseRsp {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
