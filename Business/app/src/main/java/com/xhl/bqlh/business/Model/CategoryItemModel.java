package com.xhl.bqlh.business.Model;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/2.
 */
public class CategoryItemModel implements Serializable {

    //设置对应Fragment的索引key
    public int index;

    private String id;
    private String categoryAppName;

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryAppName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryAppName = categoryName;
    }
}
