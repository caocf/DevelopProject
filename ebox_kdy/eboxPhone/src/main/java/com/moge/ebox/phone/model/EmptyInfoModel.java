package com.moge.ebox.phone.model;

import com.moge.ebox.phone.bettle.model.BaseEntity;

import java.io.Serializable;

public class EmptyInfoModel extends BaseEntity<OrderDetailsModel> implements Serializable {

	private String terminal_name;
	private String terminal_addr;
	private String terminal_code;

	private BigEmptyInfoModel big;
	private MiddleEmptyInfoModel middle;
	private SmallEmptyInfoModel small;

	public String getTerminal_name() {
		return terminal_name;
	}

	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
	}

	public String getTerminal_addr() {
		return terminal_addr;
	}

	public void setTerminal_addr(String terminal_addr) {
		this.terminal_addr = terminal_addr;
	}

	public String getTerminal_code() {
		return terminal_code;
	}

	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}

	public BigEmptyInfoModel getBig() {
		return big;
	}

	public void setBig(BigEmptyInfoModel big) {
		this.big = big;
	}

	public MiddleEmptyInfoModel getMiddle() {
		return middle;
	}

	public void setMiddle(MiddleEmptyInfoModel middle) {
		this.middle = middle;
	}

	public SmallEmptyInfoModel getSmall() {
		return small;
	}

	public void setSmall(SmallEmptyInfoModel small) {
		this.small = small;
	}
}
