package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

/**
 * Created by Android on 2015/9/2.
 */
public class TerminalType implements Serializable{

    private String code;
    private String addr;
    private String terminal_name;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTerminal_name() {
        return terminal_name;
    }

    public void setTerminal_name(String terminal_name) {
        this.terminal_name = terminal_name;
    }
}
