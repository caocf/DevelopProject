package com.xhl.bqlh.business.Model;

import java.util.List;

/**
 * Created by Sum on 16/4/23.
 */
public class BusinessTypeModel {

    private String id;
    private String code;
    private String name;
    private String parentId;
    private String parentName;
    private String sequence;
    private String level;
    private String delFlag;
    private String description;
    private String createTime;
    private String updateTime;
    private List<BusinessTypeModel> children;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public String getSequence() {
        return sequence;
    }

    public String getLevel() {
        return level;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<BusinessTypeModel> getChildren() {
        return children;
    }
}
