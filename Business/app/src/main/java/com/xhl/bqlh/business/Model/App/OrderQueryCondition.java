package com.xhl.bqlh.business.Model.App;

import com.xhl.bqlh.business.Model.Type.OrderType;

import java.io.Serializable;

/**
 * Created by Sum on 16/4/20.
 * 订单查询条件
 */
public class OrderQueryCondition implements Serializable {

    public String hint;
    //注意开始日期要小于结束日期
    public String startTime;
    public String endTime;
    public String creditStatus = "";//2：赊账 1:正常
    public String shopId = "";//零售商id
    public int details = 0;//1:查询详情，0：默认不查

    public int orderType = 0;//订单类型（默认全部）
    public boolean isNeedOrderTag = true;//订单类型

    public int selectWhich = 0;//记录状态选择项（默认全部）
    public int orderState = 0;//订单状态（默认全部）

    private String[] orderTypeArray;//订单类型
    private String[] stateCar;//车销订单状态
    private String[] stateNor;//普通订单状态

    public void setStateCar(String[] stateCar) {
        this.stateCar = stateCar;
    }

    public void setStateNor(String[] stateNor) {
        this.stateNor = stateNor;
    }

    //获取不同订单类型的订单状态
    public String[] getStateByType(int type) {
        if (type == OrderType.order_type_all) {
            return null;
        }
        if (type == OrderType.order_type_car) {
            return stateCar;
        } else {
            return stateNor;
        }
    }

    public String[] getOrderTypeArray() {
        return orderTypeArray;
    }

    public void setOrderTypeArray(String[] orderTypeArray) {
        this.orderTypeArray = orderTypeArray;
    }

    public void updateOrderState() {
        //根据不同订单状态和选择项判断订单类型
        int orderType = this.orderType;
        if (orderType == OrderType.order_type_all) {
            orderState = 0;
        } else if (orderType == OrderType.order_type_car) {
            //选中项
            if (selectWhich == 0) {
                orderState = OrderType.order_state_all;
            } else if (selectWhich == 1) {
                orderState = OrderType.order_state_have_send;
            } else {
                orderState = OrderType.order_state_finish;
            }
        } else {
            //选中项
            if (selectWhich == 0) {
                orderState = OrderType.order_state_all;
            } else if (selectWhich == 1) {
                orderState = OrderType.order_state_not_send;
            } else if (selectWhich == 2) {
                orderState = OrderType.order_state_have_send;
            } else {
                orderState = OrderType.order_state_finish;
            }
        }
    }

}
