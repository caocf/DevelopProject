package com.xhl.bqlh.business.Model;

/**
 * Created by Sum on 16/4/23.
 */
public class VersionInfo {
    private String id;
    private String version;
    private boolean isupdate;
    private String versionupdateinfo;
    private String versionstate;
    private String versionupdatetime;
    private String clienttype;
    private String url;

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public boolean isupdate() {
        return isupdate;
    }

    public String getVersionupdateinfo() {
        return versionupdateinfo;
    }

    public String getVersionstate() {
        return versionstate;
    }

    public String getVersionupdatetime() {
        return versionupdatetime;
    }

    public String getClienttype() {
        return clienttype;
    }

    public String getUrl() {
        return url;
    }
}
