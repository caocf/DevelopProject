package com.xhl.bqlh.model;

import com.xhl.bqlh.AppConfig.NetWorkConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sum on 16/7/4.
 */
public class ClassifyModel implements Serializable{

    public String shopId;

    private String id;

    private String parentId;

    private String areaId;

    private String categoryAppName;

    private String imgUrl;

    private int categoryLevel;

    private ArrayList<ClassifyModel> children;

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getCategoryAppName() {
        return categoryAppName;
    }

    public String getImgUrl() {
        return NetWorkConfig.imageHost + imgUrl;
    }

    public int getCategoryLevel() {
        return categoryLevel;
    }

    public ArrayList<ClassifyModel> getChildren() {
        return children;
    }
}
