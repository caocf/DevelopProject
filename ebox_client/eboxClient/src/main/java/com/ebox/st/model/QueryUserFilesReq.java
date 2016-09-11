package com.ebox.st.model;

/**
 * Created by mafeng on 2015/6/24.
 */
public class QueryUserFilesReq {
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

    private String source;
}
