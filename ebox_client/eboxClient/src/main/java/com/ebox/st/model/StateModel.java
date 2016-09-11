package com.ebox.st.model;

public class StateModel {
	
	private String creat_at;
	private String state;
	private String desc;
    private String current_state;

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }


	public String getCreat_at() {
		return creat_at;
	}
	public void setCreat_at(String creat_at) {
		this.creat_at = creat_at;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
