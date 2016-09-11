package com.moge.gege.model;

import java.io.Serializable;


public class DeliveryBoxModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String box_name;
    private int box_type;
    private String deliver_time;
    private String delivery_logo;
    private String delivery_name;
    private String delivery_phone;
    private String delivery_type;
    private String fetch_time;
    private String mobile;
    private String number;
    private String operator_mobile;
    private String operator_name;
    private String password;
    private int state;
    private String terminal_address;
    private String terminal_code;
    private String terminal_name;
    private String verbose_state;
    private String terminal_village_address;
    private String rack_code;
    private String rack_name;

    private boolean showHeader;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }

    public int getBox_type() {
        return box_type;
    }

    public void setBox_type(int box_type) {
        this.box_type = box_type;
    }

    public String getDeliver_time() {
        return deliver_time;
    }

    public void setDeliver_time(String deliver_time) {
        this.deliver_time = deliver_time;
    }

    public String getDelivery_logo() {
        return delivery_logo;
    }

    public void setDelivery_logo(String delivery_logo) {
        this.delivery_logo = delivery_logo;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_phone() {
        return delivery_phone;
    }

    public void setDelivery_phone(String delivery_phone) {
        this.delivery_phone = delivery_phone;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getFetch_time() {
        return fetch_time;
    }

    public void setFetch_time(String fetch_time) {
        this.fetch_time = fetch_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOperator_mobile() {
        return operator_mobile;
    }

    public void setOperator_mobile(String operator_mobile) {
        this.operator_mobile = operator_mobile;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTerminal_address() {
        return terminal_address;
    }

    public void setTerminal_address(String terminal_address) {
        this.terminal_address = terminal_address;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public String getTerminal_name() {
        return terminal_name;
    }

    public void setTerminal_name(String terminal_name) {
        this.terminal_name = terminal_name;
    }

    public String getVerbose_state() {
        return verbose_state;
    }

    public void setVerbose_state(String verbose_state) {
        this.verbose_state = verbose_state;
    }

    public String getTerminal_village_address() {
        return terminal_village_address;
    }

    public void setTerminal_village_address(String terminal_village_address) {
        this.terminal_village_address = terminal_village_address;
    }

    public String getRack_code() {
        return rack_code;
    }

    public void setRack_code(String rack_code) {
        this.rack_code = rack_code;
    }

    public String getRack_name() {
        return rack_name;
    }

    public void setRack_name(String rack_name) {
        this.rack_name = rack_name;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }
}
