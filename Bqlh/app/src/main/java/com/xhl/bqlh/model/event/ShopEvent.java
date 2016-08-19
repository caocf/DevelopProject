package com.xhl.bqlh.model.event;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopEvent {

    public static final int TAG_PRODUCT = 1;//商品

    public static final int TAG_BRAND = 2;//品牌

    public static final int TAG_CATEGORY = 3;//分类

    public static final int TAG_SERVICE = 4;//服务

    public static final int TAG_SEARCH_BRAND = 5;//搜索品牌

    public static final int TAG_SEARCH_CATEGORY = 6;//搜索分类

    public String filterId;//过滤的id

    private int tag;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
