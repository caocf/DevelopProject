package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqBackFillUpdate extends BaseReq {

    private Integer update_type;
    private Long update_id;


    public Integer getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(Integer update_type) {
        this.update_type = update_type;
    }

    public Long getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(Long update_id) {
        this.update_id = update_id;
    }
}
