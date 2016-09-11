package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

import java.util.List;

public class LnydCommit {
	private List<AttaModel> atta;
	private String boxId;
	private IdcardModel idcard;
	private String telephone;
	
	public List<AttaModel> getAtta() {
		return atta;
	}
	public void setAtta(List<AttaModel> atta) {
		this.atta = atta;
	}
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	public IdcardModel getIdcard() {
		return idcard;
	}
	public void setIdcard(IdcardModel idcard) {
		this.idcard = idcard;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
