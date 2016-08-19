package com.xhl.bqlh.model;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.model.event.AdEvent;
import com.xhl.bqlh.view.custom.viewPager.ImageModel;
import com.xhl.bqlh.view.helper.EventHelper;

import java.io.Serializable;

/**
 * Created by Sum on 16/7/11.
 */
public class AdInfoModel extends ImageModel  implements Serializable{

    private String adName;
    private String adImage;
    private String url;
    private String adLocation;
    private String adLocationName;
    private String linkType;
    private String retailPrice;
    private String productName;

    public String getAdName() {
        return adName;
    }

    public String getAdImage() {
        return adImage;
    }

    public String getUrl() {
        return url;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public String getAdLocationName() {
        return adLocationName;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public String getImageUrl() {
        return NetWorkConfig.imageHost + adImage;
    }

    public static void postEvent(AdInfoModel info) {

        AdEvent event = new AdEvent();

        if (info.getLinkType().equals("1")) {
            event.adType = AdEvent.type_product;
        } else if (info.getLinkType().equals("2")) {
            event.adType = AdEvent.type_shop;
        } else if (info.getLinkType().equals("3")) {
            event.adType = AdEvent.type_web;
        }
        event.data = info.getUrl();

        EventHelper.postDefaultEvent(event);
    }
}
