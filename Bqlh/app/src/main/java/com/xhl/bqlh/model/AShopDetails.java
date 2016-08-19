package com.xhl.bqlh.model;

import java.util.List;

/**
 * Created by Sum on 16/7/11.
 */
public class AShopDetails {
    //店铺信息
    private ShopModel shops;

    private ShopExInfoModel allinfo;

    public ShopExInfoModel getAllinfo() {
        return allinfo;
    }

    public ShopModel getShops() {
        return shops;
    }

    //广告信息
    private List<AdInfoModel> Carousel;

    public List<AdInfoModel> getCarousel() {
        return Carousel;
    }
}
