package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqUpdateInfo extends BaseReq {

    private Integer update_type;


    public Integer getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(Integer update_type) {
        this.update_type = update_type;
    }
}
