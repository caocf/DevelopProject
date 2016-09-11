package com.ebox.ex.network.model;

import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.UploadPicToken;

public class RspUploadPicToken extends BaseRsp{

	private UploadPicToken data;


	public UploadPicToken getData() {
		return data;
	}

	public void setData(UploadPicToken data) {
		this.data = data;
	}
}
