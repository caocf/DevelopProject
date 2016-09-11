package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class GetUserFileReq {
    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    private String terminal_code;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSt_identi_order_id() {
        return st_identi_order_id;
    }

    public void setSt_identi_order_id(String st_identi_order_id) {
        this.st_identi_order_id = st_identi_order_id;
    }

    private String st_identi_order_id;
    private String source;

}
