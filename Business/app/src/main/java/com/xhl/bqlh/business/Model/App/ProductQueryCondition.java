package com.xhl.bqlh.business.Model.App;

import java.io.Serializable;

/**
 * Created by Sum on 16/4/23.
 */
public class ProductQueryCondition implements Serializable {

    public static final int TYPE_CAR_PRODUCT = 0;//车销商品
    public static final int TYPE_ORDER_PRODUCT = 1;//订单商品

    public int queryType;//查询类型

    public String shopId;

    public String startTime;
    public String endTime;

}
