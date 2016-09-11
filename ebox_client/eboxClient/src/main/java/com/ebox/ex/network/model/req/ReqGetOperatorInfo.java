package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

import java.util.List;

public class ReqGetOperatorInfo extends BaseReq {

	 private List<String> username;

	public List<String> getUsername() {
		return username;
	}

	public void setUsername(List<String> username) {
		this.username = username;
	}

	 

}
