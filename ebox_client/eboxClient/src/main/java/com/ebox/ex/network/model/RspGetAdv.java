package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.AdvertiseClass;

import java.util.List;

public class RspGetAdv extends BaseRsp {

    private List<AdvertiseClass> data;


    public List<AdvertiseClass> getData() {
        return data;
    }

    public void setData(List<AdvertiseClass> data) {
        this.data = data;
    }
}
