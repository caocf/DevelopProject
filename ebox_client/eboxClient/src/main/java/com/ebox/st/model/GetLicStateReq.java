package com.ebox.st.model;


import com.ebox.pub.model.IdcardModel;

public class GetLicStateReq {
	
	private String terminal_code;
	private IdcardModel idcard;
	public String getTerminal_code() {
		return terminal_code;
	}
	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}
	public IdcardModel getIdcard() {
		return idcard;
	}
	public void setIdcard(IdcardModel idcard) {
		this.idcard = idcard;
	}


}
