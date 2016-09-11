package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqResetPassword extends BaseReq {
    private String username;
    private String password;
    private String verify;

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

}
