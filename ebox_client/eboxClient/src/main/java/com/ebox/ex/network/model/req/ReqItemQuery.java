package com.ebox.ex.network.model.req;

import com.ebox.ex.network.model.base.BaseReq;

public class ReqItemQuery extends BaseReq{
	private String itemQuery;
	
	public String getItemQuery() {
		return itemQuery;
	}
	public void setItemQuery(String itemQuery) {
		this.itemQuery = itemQuery;
	}

}