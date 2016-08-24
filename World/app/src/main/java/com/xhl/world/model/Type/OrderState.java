package com.xhl.world.model.Type;

/**
 * Created by Sum on 16/1/5.
 */
public interface OrderState {

    public static final int order_wait_pay = 0; //待支付
    public static final int order_wait_pick = 1; //待收货
    public static final int order_wait_pick_sender = 2; //待收货,未发货
    public static final int order_wait_judge = 3; //待评价
    public static final int order_cancel = 4; //取消
    public static final int order_finish = 5; //完成
}
