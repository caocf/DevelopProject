package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class ExpressCharge implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer box_type;
	private Long fee;
	public Integer getBoxSize() {
		return box_type;
	}
	public void setBoxSize(Integer box_size) {
		this.box_type = box_size;
	}
	public Long getFee() {
		return fee;
	}
	public void setFee(Long fee) {
		this.fee = fee;
	}

}
