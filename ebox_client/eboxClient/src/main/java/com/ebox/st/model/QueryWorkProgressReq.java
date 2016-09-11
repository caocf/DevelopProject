package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

/**
 * Created by mafeng on 2015/6/25.
 */
public class QueryWorkProgressReq {

    private String  identification; //1：计划生育服证
    private IdcardModel idcard;
    private String  terminal_code;


    public IdcardModel getIdcard() {
        return idcard;
    }
    public void setIdcard(IdcardModel idcard) {
        this.idcard = idcard;
    }
    public String getTerminal_code() {
        return terminal_code;
    }
    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }
    public String getIdentification() {
        return identification;
    }
    public void setIdentification(String identification) {
        this.identification = identification;
    }

}
