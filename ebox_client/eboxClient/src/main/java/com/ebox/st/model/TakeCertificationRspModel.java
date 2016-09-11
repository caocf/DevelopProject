package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/25.
 */
public class TakeCertificationRspModel  {
    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBox_id() {
        return box_id;
    }

    public void setBox_id(int box_id) {
        this.box_id = box_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String idcard;
    private int identification;
    private String terminal_code;
    private int order_state;
    private int status;
    private int box_id;
    private String name ;
    private String box_name;
    private String box_code;

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }
}
