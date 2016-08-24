package com.xhl.world.model.Entities;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/14.
 * 最小单位的一个广告实体
 */
public class BaseTargetEntities implements Serializable{

    private int target;

    private String image_url;

    private String target_data;

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTarget_data() {
        return target_data;
    }

    public void setTarget_data(String target_data) {
        this.target_data = target_data;
    }
}
