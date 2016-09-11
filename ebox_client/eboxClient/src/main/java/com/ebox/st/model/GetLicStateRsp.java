package com.ebox.st.model;

import java.util.ArrayList;

public class GetLicStateRsp {

	private Boolean success;
	private ArrayList<StateModel> result;
	private String parames;
	private String state;
	

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getParames() {
		return parames;
	}

	public void setParames(String parames) {
		this.parames = parames;
	}

	public ArrayList<StateModel> getResult() {
		return result;
	}

	public void setResult(ArrayList<StateModel> result) {
		this.result = result;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
