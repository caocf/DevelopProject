package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class QueryWorkInfoReq {
    private String barcode;

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    private String terminal_code;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
