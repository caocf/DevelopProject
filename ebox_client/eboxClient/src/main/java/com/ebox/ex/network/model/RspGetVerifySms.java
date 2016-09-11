package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseReq;

public class RspGetVerifySms extends BaseReq{
	private String username ;
	private Integer type;
	
	public String getTelephone() {
		return username;
	}
	public void setTelephone(String username) {
		this.username = username;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}
