package com.ebox.st.model;

import com.ebox.pub.model.IdcardModel;

import java.util.ArrayList;

public class JhsyCommit {
	private IdcardModel idcard;
	private String telephone;
	private String marryState;
	private IdcardModel OIdcard;
	private String oTelephone;
	private String oMarryState;
	private ArrayList<ChildrenModel> pList;
	private String marriage_attachmentid;
	private String house_index_attachmentid;
	private String house_user_attachmentid;
	private String house2_index_attachmentid;
	private String house2_user_attachmentid;
	private String divorce_attachmentid;
	private String divorce2_attachmentid;
	
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
	public String getMarryState() {
		return marryState;
	}
	public void setMarryState(String marryState) {
		this.marryState = marryState;
	}
	public String getoTelephone() {
		return oTelephone;
	}
	public void setoTelephone(String oTelephone) {
		this.oTelephone = oTelephone;
	}
	public String getoMarryState() {
		return oMarryState;
	}
	public void setoMarryState(String oMarryState) {
		this.oMarryState = oMarryState;
	}
	public ArrayList<ChildrenModel> getpList() {
		return pList;
	}
	public void setpList(ArrayList<ChildrenModel> pList) {
		this.pList = pList;
	}
	public IdcardModel getOIdcard() {
		return OIdcard;
	}
	public void setOIdcard(IdcardModel oIdcard) {
		OIdcard = oIdcard;
	}
	public String getMarriage_attachmentid() {
		return marriage_attachmentid;
	}
	public void setMarriage_attachmentid(String marriage_attachmentid) {
		this.marriage_attachmentid = marriage_attachmentid;
	}
	public String getHouse_index_attachmentid() {
		return house_index_attachmentid;
	}
	public void setHouse_index_attachmentid(String house_index_attachmentid) {
		this.house_index_attachmentid = house_index_attachmentid;
	}
	public String getHouse_user_attachmentid() {
		return house_user_attachmentid;
	}
	public void setHouse_user_attachmentid(String house_user_attachmentid) {
		this.house_user_attachmentid = house_user_attachmentid;
	}
	public String getHouse2_index_attachmentid() {
		return house2_index_attachmentid;
	}
	public void setHouse2_index_attachmentid(String house2_index_attachmentid) {
		this.house2_index_attachmentid = house2_index_attachmentid;
	}
	public String getHouse2_user_attachmentid() {
		return house2_user_attachmentid;
	}
	public void setHouse2_user_attachmentid(String house2_user_attachmentid) {
		this.house2_user_attachmentid = house2_user_attachmentid;
	}
	public String getDivorce_attachmentid() {
		return divorce_attachmentid;
	}
	public void setDivorce_attachmentid(String divorce_attachmentid) {
		this.divorce_attachmentid = divorce_attachmentid;
	}
	public String getDivorce2_attachmentid() {
		return divorce2_attachmentid;
	}
	public void setDivorce2_attachmentid(String divorce2_attachmentid) {
		this.divorce2_attachmentid = divorce2_attachmentid;
	}
}
