package com.ebox.ex.database.LocalOrder;

import com.ebox.ex.network.model.enums.PickupType;

import java.io.Serializable;

/**
 * Created by Android on 2015/9/2.
 */
public class OrderLocalInfo extends PickupType implements Serializable{

    private String operator_telephone;//派件人
    private String customer_telephone;//收件人
    private String box_code;//箱门
    private String order_id;//订单id
    private String item_id;//运单号
    private String password;//取件码
    private String pick_id;//授权码
    private String user_type;//取件人 customer|operator|manager|qrcode
    private Integer time_out;//超期状态
    private String delivery_at;//超期时间
    private Integer order_state;//订单状态
    private String  pick_time;//取件时间


    public String getOperator_telephone() {
        return operator_telephone;
    }

    public void setOperator_telephone(String operator_telephone) {
        this.operator_telephone = operator_telephone;
    }

    public String getCustomer_telephone() {
        return customer_telephone;
    }

    public void setCustomer_telephone(String customer_telephone) {
        this.customer_telephone = customer_telephone;
    }

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPick_id() {
        return pick_id;
    }

    public void setPick_id(String pick_id) {
        this.pick_id = pick_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
    public Integer getTime_out() {
        return time_out;
    }

    public void setTime_out(Integer time_out) {
        this.time_out = time_out;
    }

    public String getDelivery_at() {
        return delivery_at;
    }

    public void setDelivery_at(String delivery_at) {
        this.delivery_at = delivery_at;
    }

    public Integer getOrder_state() {
        return order_state;
    }

    public void setOrder_state(Integer order_state) {
        this.order_state = order_state;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPick_time() {
        return pick_time;
    }

    public void setPick_time(String pick_time) {
        this.pick_time = pick_time;
    }


    @Override
    public String toString() {
        return "{" +
                " box_code='" + box_code + '\'' +
                ", order_id='" + order_id + '\'' +
                ", item_id='" + item_id + '\'' +", password='" + password + '\'' +
                '}';
    }
}
