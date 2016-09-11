package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.ExpressCharge;
import com.ebox.ex.network.model.base.type.OperatorInfoType;

import java.util.List;


public class RspOperatorVerifyByCard extends BaseRsp {
    private OperatorInfoType operatorInfo;
    private Boolean hasTimeout;
    private Boolean nobalance;
    private List<ExpressCharge> expressCharge;

    public OperatorInfoType getOperatorInfo() {
        return operatorInfo;
    }

    public void setOperatorInfo(OperatorInfoType operatorInfo) {
        this.operatorInfo = operatorInfo;
    }

    public Boolean getHasTimeout() {
        return hasTimeout;
    }

    public void setHasTimeout(Boolean hasTimeout) {
        this.hasTimeout = hasTimeout;
    }

    public List<ExpressCharge> getExpressCharge() {
        return expressCharge;
    }

    public void setExpressCharge(List<ExpressCharge> expressCharge) {
        this.expressCharge = expressCharge;
    }

    public Boolean getNobalance() {
        return nobalance;
    }

    public void setNobalance(Boolean nobalance) {
        this.nobalance = nobalance;
    }
}
