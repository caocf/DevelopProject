package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class OrderInfo  implements Serializable{
    private BoxType box;
    private TerminalType terminal;
    private Long id;
    private Integer operator_id;
    private Integer box_id;
    private Integer operator_station_id;
    private Integer dot_id;
    private String order_id;
    private String images;
    private String pickup_id;
    private String box_type;
    private String fetch_at;
    private String terminal_code;
    private Integer charge;
    private Integer state;
    private String delivery_at;
    private String item_id;
    private String state_at;
    private String msisdn;
    private String password;
    private Integer timeout;
    private String operator_username;


    public Integer getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Integer operator_id) {
        this.operator_id = operator_id;
    }

    public Integer getBox_id() {
        return box_id;
    }

    public void setBox_id(Integer box_id) {
        this.box_id = box_id;
    }

    public Integer getOperator_station_id() {
        return operator_station_id;
    }

    public void setOperator_station_id(Integer operator_station_id) {
        this.operator_station_id = operator_station_id;
    }

    public Integer getDot_id() {
        return dot_id;
    }

    public void setDot_id(Integer dot_id) {
        this.dot_id = dot_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPickup_id() {
        return pickup_id;
    }

    public void setPickup_id(String pickup_id) {
        this.pickup_id = pickup_id;
    }

    public String getBox_type() {
        return box_type;
    }

    public void setBox_type(String box_type) {
        this.box_type = box_type;
    }

    public String getFetch_at() {
        return fetch_at;
    }

    public void setFetch_at(String fetch_at) {
        this.fetch_at = fetch_at;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDelivery_at() {
        return delivery_at;
    }

    public void setDelivery_at(String delivery_at) {
        this.delivery_at = delivery_at;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState_at() {
        return state_at;
    }

    public void setState_at(String state_at) {
        this.state_at = state_at;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BoxType getBox() {
        return box;
    }

    public void setBox(BoxType box) {
        this.box = box;
    }

    public TerminalType getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalType terminal) {
        this.terminal = terminal;
    }

    public String getOperator_username() {
        return operator_username;
    }

    public void setOperator_username(String operator_username) {
        this.operator_username = operator_username;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
