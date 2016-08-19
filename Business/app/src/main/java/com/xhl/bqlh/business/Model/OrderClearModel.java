package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

/**
 * Created by Sum on 16/5/22.
 */
public class OrderClearModel {
    private String orderCode;
    private String storeOrderCode;
    private String creationTime;
    private String updateTime;
    private String repayment;
    private String arrears;
    private String remark;
    private int state;
    private int checkeStatus;//1:待审核，2：已经审核

    public String getOrderCode() {
        return orderCode;
    }

    public String getStoreOrderCode() {
        return storeOrderCode;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getRepayment() {
        if (TextUtils.isEmpty(repayment)) {
            return "";
        }
        return repayment;
    }

    public String getArrears() {
        return arrears;
    }

    public String getRemark() {
        if (TextUtils.isEmpty(remark)) {
            return "";
        }
        return remark;
    }

    public int getState() {
        return state;
    }

    public int getCheckStatus() {
        return checkeStatus;
    }
}
