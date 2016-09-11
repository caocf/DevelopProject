package com.ebox.ex.network.model.base.type;

public class LedConfigType {
	private String terminal_id; 
	private String open_time;
	private String close_time;
	private Long config_id;
	public String getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getOpen_time() {
		return open_time;
	}
	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public Long getConfig_id() {
		return config_id;
	}
	public void setConfig_id(Long config_id) {
		this.config_id = config_id;
	}
}
