package com.ebox.st.model;


import com.ebox.pub.model.IdcardModel;

import java.io.Serializable;

public class IdentiOrderModel implements Serializable{
	Long terminal_id;
	Long st_identi_order_id;
	String update_at;
	int state;
	String tel;
	IdcardModel idcard;
	String barcode;
	String box_id;
	String creat_at;
    private static final long serialVersionUID = 1L;
	
	public Long getSt_identi_order_id() {
		return st_identi_order_id;
	}
	public void setSt_identi_order_id(Long st_identi_order_id) {
		this.st_identi_order_id = st_identi_order_id;
	}
	
	public IdcardModel getIdcard() {
		return idcard;
	}
	public void setIdcard(IdcardModel idcard) {
		this.idcard = idcard;
	}
	
	public String getTelephone() {
		return tel;
	}
	public void setTelephone(String telephone) {
		this.tel = telephone;
	}
	public Long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getBox_id() {
		return box_id;
	}
	public void setBox_id(String box_id) {
		this.box_id = box_id;
	}
	public String getCreat_at() {
		return creat_at;
	}
	public void setCreat_at(String creat_at) {
		this.creat_at = creat_at;
	}
	public String getUpdate_at() {
		return update_at;
	}
	public void setUpdate_at(String update_at) {
		this.update_at = update_at;
	}
	
	
	
	
	
	
}
