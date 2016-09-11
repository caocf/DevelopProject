package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.LedContentType;

import java.util.List;

public class RspLedContent extends BaseRsp {

    private List<LedContentType> data;

    public List<LedContentType> getResult() {
        return data;
    }

    public void setResult(List<LedContentType> data) {
        this.data = data;
    }


}
