package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.LedConfigType;

import java.util.List;

public class RspLedConfig extends BaseRsp {

    private List<LedConfigType> data;

    public List<LedConfigType> getResult() {
        return data;
    }

    public void setResult(List<LedConfigType> result) {
        this.data = result;
    }


}
