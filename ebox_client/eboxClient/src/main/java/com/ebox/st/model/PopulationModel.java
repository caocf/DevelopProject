package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

import java.io.Serializable;

public class PopulationModel implements Serializable{
	private String relation;
	private IdcardModel idcard;
	private String telephone;
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
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
