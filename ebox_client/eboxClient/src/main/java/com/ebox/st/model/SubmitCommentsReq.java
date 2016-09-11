package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

/**
 * Created by mafeng on 2015/6/27.
 */
public class SubmitCommentsReq {

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String  telephone; //
    private IdcardModel idcard;
    private String  terminal_code;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;


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

}
