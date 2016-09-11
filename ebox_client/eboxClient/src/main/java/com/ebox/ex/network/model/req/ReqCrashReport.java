package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.AppVersion;

public class ReqCrashReport extends BaseReq {
    private String msg;

    private AppVersion app_version;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public AppVersion getAppVersion() {
        return app_version;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.app_version = appVersion;
    }
}
