package com.xhl.bqlh.model;

import java.util.List;

/**
 * Created by Summer on 2016/7/19.
 */
public class AdModel {

    private AdInfoModel advert;
    private List<AdInfoModel> advertList;
    private String location;
    private String name;

    public AdInfoModel getAdvert() {
        return advert;
    }

    public List<AdInfoModel> getAdvertList() {
        return advertList;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
