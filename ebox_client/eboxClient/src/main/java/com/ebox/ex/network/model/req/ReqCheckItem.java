package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqCheckItem extends BaseReq {
    private String itemId;
    private String operatorId;
    private Long localTime;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Long localTime) {
        this.localTime = localTime;
    }


}
