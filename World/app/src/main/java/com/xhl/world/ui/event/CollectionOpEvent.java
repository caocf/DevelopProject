package com.xhl.world.ui.event;

import com.xhl.world.model.CollectionModel;

/**
 * Created by Sum on 16/1/9.
 */
public class CollectionOpEvent {

    public int actionType;//动作类型 0：点击 1：长按删除

    public int collectionType; //0：商品 1:店铺

    public int deletePosition;//点击是view位置

    public int deleteType;//0:删除单个 1：删除全部

    public CollectionModel data;

    public String productId;//商品id

}
