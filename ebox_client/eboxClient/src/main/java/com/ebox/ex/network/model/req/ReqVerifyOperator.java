package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqVerifyOperator extends BaseReq {
    private String username;
    private String password;

    private String card;

    public String getOperatorId() {
        return username;
    }

    public void setOperatorId(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
