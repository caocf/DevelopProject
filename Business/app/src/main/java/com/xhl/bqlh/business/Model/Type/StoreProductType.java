package com.xhl.bqlh.business.Model.Type;

/**
 * Created by Sum on 16/5/31.
 */
public interface StoreProductType {

    //1.订单商品，2.装车单创建车销商品 3.装车单新增车销商品

    int TYPE_ORDER_PRODUCT = 1;

    //车销创建的时候商品
    int TYPE_CREATE_PRODUCT = 2;

    //新增商品
    int TYPE_UPDATE_PRODUCT = 3;
    //新增赠品
    int TYPE_UPDATE_PRODUCT_GIFT = 3 + 4;
}
