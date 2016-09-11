package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.BoxInfoType;

import java.util.List;

public class RspPullBoxState extends BaseRsp {

    private List<BoxInfoType> data;

    public List<BoxInfoType> getData() {
        return data;
    }

    public void setData(List<BoxInfoType> data) {
        this.data = data;
    }
}
