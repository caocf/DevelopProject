package com.xhl.bqlh.model;

import java.util.List;

/**
 * Created by Summer on 2016/7/29.
 */
public class AreaModel {

    /**
     * areaCode : 000001
     * areaName : 宿州市
     * children : []
     * cost : 0
     * createTime : null
     * delFlag : 0
     * id : 0480c282554444a2b8bc781957b9134b
     * level : 0
     * page : null
     * parentId : 8a0555186dbf46958ae0764f08654fd5
     * updateTime : null
     */

    private String areaCode;
    private String areaName;
    private Object createTime;
    private String id;
    private Object page;
    private String parentId;
    private Object updateTime;
    private List<AreaModel> children;

    public String getId() {
        return id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public List<AreaModel> getChildren() {
        return children;
    }
}
