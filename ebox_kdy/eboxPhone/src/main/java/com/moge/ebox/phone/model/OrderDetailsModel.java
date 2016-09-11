package com.moge.ebox.phone.model;

import com.moge.ebox.phone.bettle.model.BaseEntity;

import java.io.Serializable;

public class OrderDetailsModel extends BaseEntity<OrderDetailsModel> implements Serializable {
    //{"orgnization_id":32,"operator_id":796,
    //"msisdn":"15656263709","fetch_time":"2014-12-18 09:53:09","state":5,
    //"package":"","order_id":360010,"password":"313278","isUrge":0,"charge":30,"dot_id":2,"item_id":"111000",
    //"state_date":"2014-12-18 09:53:09","name":"","box_id":9776,
    //"deliver_time":"2014-12-18 09:46:59",
    //"terminal_code":"0025050001","customer_id":39777}
    private String pickup_id;
    private Integer operator_id;
    private String msisdn;
    private String state;
    private String state_at;
    private Integer box_type;
    private String order_id;
    private Integer operator_station_id;
    private Integer id;
    private Integer charge;
    private String fetch_at;
    private TerminalModel terminal;
    private Integer dot_id;
    private String item_id;
    private Integer box_id;
    private String images;
    private BoxModel box;
    private String delivery_at;
    private String terminal_code;


    public String getPickup_id() {
        return pickup_id;
    }

    public void setPickup_id(String pickup_id) {
        this.pickup_id = pickup_id;
    }

    public Integer getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Integer operator_id) {
        this.operator_id = operator_id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_at() {
        return state_at;
    }

    public void setState_at(String state_at) {
        this.state_at = state_at;
    }

    public Integer getBox_type() {
        return box_type;
    }

    public void setBox_type(Integer box_type) {
        this.box_type = box_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getOperator_station_id() {
        return operator_station_id;
    }

    public void setOperator_station_id(Integer operator_station_id) {
        this.operator_station_id = operator_station_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public String getFetch_at() {
        return fetch_at;
    }

    public void setFetch_at(String fetch_at) {
        this.fetch_at = fetch_at;
    }


    public Integer getDot_id() {
        return dot_id;
    }

    public void setDot_id(Integer dot_id) {
        this.dot_id = dot_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public Integer getBox_id() {
        return box_id;
    }

    public void setBox_id(Integer box_id) {
        this.box_id = box_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    public String getDelivery_at() {
        return delivery_at;
    }

    public void setDelivery_at(String delivery_at) {
        this.delivery_at = delivery_at;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public TerminalModel getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalModel terminal) {
        this.terminal = terminal;
    }

    public BoxModel getBox() {
        return box;
    }

    public void setBox(BoxModel box) {
        this.box = box;
    }
}
