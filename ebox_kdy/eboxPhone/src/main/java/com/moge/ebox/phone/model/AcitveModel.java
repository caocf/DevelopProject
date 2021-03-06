package com.moge.ebox.phone.model;

import com.moge.ebox.phone.bettle.model.BaseEntity;

public class AcitveModel extends BaseEntity<AcitveModel> {
    private String end_date;
    private String start_date;
    private String act_detail;
    private Integer act_state;
    private Integer act_type;
    private Integer act_amount;
    private Integer act_value;
    private Integer act_id;
    private String act_image_path;
    private String act_script;
    private String act_timeout;

    public String getAct_image_path() {
        return act_image_path;
    }

    public void setAct_image_path(String act_image_path) {
        this.act_image_path = act_image_path;
    }

    public void setAct_script(String act_script) {
        this.act_script = act_script;
    }

    public String getAct_script() {
        return act_script;
    }

    public void setAct_timeout(String act_timeout) {
        this.act_timeout = act_timeout;
    }
    public String getAct_timeout() {
        return act_timeout;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getAct_detail() {
        return act_detail;
    }

    public void setAct_detail(String act_detail) {
        this.act_detail = act_detail;
    }

    public Integer getAct_state() {
        return act_state;
    }

    public void setAct_state(Integer act_state) {
        this.act_state = act_state;
    }

    public Integer getAct_type() {
        return act_type;
    }

    public void setAct_type(Integer act_type) {
        this.act_type = act_type;
    }

    public Integer getAct_amount() {
        return act_amount;
    }

    public void setAct_amount(Integer act_amount) {
        this.act_amount = act_amount;
    }

    public Integer getAct_value() {
        return act_value;
    }

    public void setAct_value(Integer act_value) {
        this.act_value = act_value;
    }

    public Integer getAct_id() {
        return act_id;
    }

    public void setAct_id(Integer act_id) {
        this.act_id = act_id;
    }


}
