package com.xhl.bqlh.model;

import android.text.TextUtils;

import com.xhl.bqlh.AppConfig.NetWorkConfig;

/**
 * Created by Summer on 2016/7/19.
 */
public class OperatorMenu {

    private String id;

    private String position;

    private String menuPicture;

    private String menuUrl;

    private String menuName;

    private String type;

    public String getMenuPicture() {
        return NetWorkConfig.imageHost + menuPicture;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public String getMenuName() {
        if (TextUtils.isEmpty(menuName)) {
            return "";
        }
        return menuName;
    }
}
