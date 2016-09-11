package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.ReportTerminalStatus;

public class RspReportTerminalStatus extends BaseRsp {

    private ReportTerminalStatus data;

    public ReportTerminalStatus getData() {
        return data;
    }

    public void setData(ReportTerminalStatus data) {
        this.data = data;
    }
}
