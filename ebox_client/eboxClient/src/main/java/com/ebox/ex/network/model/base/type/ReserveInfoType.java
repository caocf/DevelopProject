package com.ebox.ex.network.model.base.type;

import java.util.List;

public class ReserveInfoType {

    private String terminal_code;
    private List<ReserveBoxInfo> reserve_type;

    public String getTerminalCode() {
        return terminal_code;
    }

    public void setTerminalCode(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public List<ReserveBoxInfo> getReserveType() {
        return reserve_type;
    }

    public void setReserveType(List<ReserveBoxInfo> reserve_type) {
        this.reserve_type = reserve_type;
    }


}
