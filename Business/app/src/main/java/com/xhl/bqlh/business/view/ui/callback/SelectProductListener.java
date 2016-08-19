package com.xhl.bqlh.business.view.ui.callback;

import android.view.View;

import com.xhl.bqlh.business.Model.ProductModel;

/**
 * Created by Sum on 16/4/14.
 */
public interface SelectProductListener {

    /**
     * 增加商品数量
     */
    void onAddCar(ProductModel product,View view);

    /**
     * 减少商品数量
     */
    void onReduceCar(ProductModel product);
}
