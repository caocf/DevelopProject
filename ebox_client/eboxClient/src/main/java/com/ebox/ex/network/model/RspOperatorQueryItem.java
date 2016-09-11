package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.QueryItemInfoType;
import com.ebox.ex.network.model.base.type.TimeOutOrderType;

import java.util.List;

public class RspOperatorQueryItem extends BaseRsp {

    private QueryItemInfoType data;

    public QueryItemInfoType getData() {
        return data;
    }

    public void setData(QueryItemInfoType data) {
        this.data = data;
    }
}