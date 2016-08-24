package com.xhl.world.model;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyItemModel implements Serializable {

    private String id; // 第一级id：0;依次取子类id
    private String imgUrl;
    private String categoryName;

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
