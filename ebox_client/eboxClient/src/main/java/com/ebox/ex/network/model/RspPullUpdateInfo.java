package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;

import java.util.List;

/**
 * Created by Android on 2015/10/10.
 */
public class RspPullUpdateInfo extends BaseRsp {

    private List<Integer> data;


    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
