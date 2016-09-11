package com.ebox.ex.network.model.base.type;

public class TimeOutOrderType {

    private Integer operator_status;
    private String order_id;
    private String box_code;
    private Integer operator_reserver_status;
    private String operator_telephone;
    private String custom_telephone;
    private String delivery_at;
    private String item_id;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDelivery_at() {
        return delivery_at;
    }

    public void setDelivery_at(String delivery_at) {
        this.delivery_at = delivery_at;
    }

    public String getCustom_telephone() {
        return custom_telephone;
    }

    public void setCustom_telephone(String custom_telephone) {
        this.custom_telephone = custom_telephone;
    }

    public Integer getOperator_reserver_status() {
        return operator_reserver_status;
    }

    public void setOperator_reserver_status(Integer operator_reserver_status) {
        this.operator_reserver_status = operator_reserver_status;
    }

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOperator_telephone() {
        return operator_telephone;
    }

    public void setOperator_telephone(String operator_telephone) {
        this.operator_telephone = operator_telephone;
    }

    public Integer getOperator_status() {
        return operator_status;
    }

    public void setOperator_status(Integer operator_status) {
        this.operator_status = operator_status;
    }
}
