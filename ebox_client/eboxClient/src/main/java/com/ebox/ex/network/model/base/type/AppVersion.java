package com.ebox.ex.network.model.base.type;

import com.ebox.AppApplication;
import com.ebox.pub.utils.PackageUtil;

public class AppVersion {
    private String package_name;
    private String client_version;
    private String download_url;


    public AppVersion() {
        this.package_name = "com.ebox";
        this.client_version= PackageUtil.getVersionName(AppApplication.globalContext);
    }

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String package_name) {
        this.package_name = package_name;
    }

    public String getClientVersion() {
        return client_version;
    }

    public void setClientVersion(String client_version) {
        this.client_version = client_version;
    }

    public String getDownloadUrl() {
        return download_url;
    }

    public void setDownloadUrl(String download_url) {
        this.download_url = download_url;
    }

}
