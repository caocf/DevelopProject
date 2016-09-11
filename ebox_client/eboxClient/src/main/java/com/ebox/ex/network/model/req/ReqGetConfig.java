package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.type.AppVersion;

public class ReqGetConfig extends BaseReq {
    private AppVersion app_version;

    public AppVersion getVersion() {
        return app_version;
    }

    public void setVersion(AppVersion version) {
        this.app_version = version;
    }

}
