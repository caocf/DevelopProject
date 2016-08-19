package com.xhl.bqlh.business.view.event;

import com.xhl.bqlh.business.Model.ProductModel;

import java.util.List;

/**
 * Created by Sum on 16/4/19.
 */
public class SelectOrderEvent {

    public List<ProductModel> orderProducts;
    public String storeCode;

}
