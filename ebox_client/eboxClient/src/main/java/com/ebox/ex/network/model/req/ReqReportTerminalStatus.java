package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.BoxInfoType;

import java.util.List;

public class ReqReportTerminalStatus extends BaseReq {


    private List<BoxInfoType> boxs;

    public List<BoxInfoType> getBoxes() {
        return boxs;
    }

    public void setBoxes(List<BoxInfoType> boxes) {
        this.boxs = boxes;
    }


}
