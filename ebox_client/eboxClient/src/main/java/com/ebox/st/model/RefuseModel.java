package com.ebox.st.model;

import com.ebox.ex.network.model.base.type.BoxInfoType;



public class RefuseModel {
	private int stepNo;
	private Workflow data;
	private String order_id;
	
	private BoxInfoType checkBox ;
	
	public String getSn() {
		return order_id;
	}
	public void setSn(String sn) {
		this.order_id = sn;
	}
	public Workflow getWorkFlow() {
		return data;
	}
	public void setWorkFlow(Workflow workFlow) {
		this.data = workFlow;
	}
	
	public int getStepNo() {
		return stepNo;
	}
	public void setStepNo(int stepNo) {
		this.stepNo = stepNo;
	}
	public BoxInfoType getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(BoxInfoType checkBox) {
		this.checkBox = checkBox;
	}
	
	
}
