package com.ebox.ex.network.model.base;

import com.ebox.AppApplication;

/**
 * Created by Android on 2015/8/31.
 */
public class BaseReq {

    private String terminal_code;

    private String local_time;

    public BaseReq() {
        this.local_time = System.currentTimeMillis()+"";
        this.terminal_code = AppApplication.getInstance().getTerminal_code();
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }


    public String getLocal_time() {
        return local_time;
    }

    public void setLocal_time(String local_time) {
        this.local_time = local_time;
    }
}
