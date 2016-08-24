package com.xhl.world.model;

import com.xhl.world.config.NetWorkConfig;
import com.xhl.xhl_library.ui.viewPager.ImageModel;

/**
 * Created by Sum on 16/1/11.
 */
public class AdvModel extends ImageModel {

    private String id;//广告id
    private String shopId;//店铺id
    private String templateId;//模板id
    private String adName;//广告名称
    private String adImage;//图片
    private String orders;//排序
    private String url;//ad地址
    private String describtion;//描述
    private String isShelves;
    private String startTime;
    private String endTime;
    private String createTime;
    private String updateTime;
    private String type;
    private String adLocation;
    private String productId;
    private String moldType;//广告类型 1：广告 2：商品

    private String threeName;//楼层名称
    private String fourName;
    private String sixName;

    public String getId() {
        return id;
    }

    public String getShopId() {
        return shopId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getAdName() {
        return adName;
    }

    public String getAdImage() {
        return adImage;
    }

    public String getOrders() {
        return orders;
    }

    public String getUrl() {
        return url;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getIsShelves() {
        return isShelves;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getType() {
        return type;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public String getProductId() {
        return productId;
    }

    public String getMoldType() {
        return moldType;
    }

    @Override
    public String getImageUrl() {
        return NetWorkConfig.imageHost + adImage;
    }

    public String getThreeName() {
        return threeName;
    }

    public String getFourName() {
        return fourName;
    }

    public String getSixName() {
        return sixName;
    }
}
