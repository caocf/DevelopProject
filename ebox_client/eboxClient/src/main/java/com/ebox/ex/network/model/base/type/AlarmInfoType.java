package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class AlarmInfoType implements Serializable{
	private static final long serialVersionUID = 1L;
	private String terminal_id;
	private String boxId;
	private Integer alarm_code;
	private String content;
	public String getTerminalId() {
		return terminal_id;
	}
	public void setTerminalId(String terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	public Integer getAlarmCode() {
		return alarm_code;
	}
	public void setAlarmCode(Integer alarm_code) {
		this.alarm_code = alarm_code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
