package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.LedInfo;

import java.util.List;

public class ReqLedRefresh extends BaseReq {
    private List<LedInfo> content;

    public List<LedInfo> getContent() {
        return content;
    }

    public void setContent(List<LedInfo> content) {
        this.content = content;
    }

}
