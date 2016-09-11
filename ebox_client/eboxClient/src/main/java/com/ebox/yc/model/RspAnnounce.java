package com.ebox.yc.model;

import java.util.List;

public class RspAnnounce {
	private int  result;
	private String resultMsg;
	private int rowCount;
	private int pageIndex;
	private int pageSize;
	private List<Content> anounceList;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public List<Content> getAnounceList() {
		return anounceList;
	}
	public void setAnounceList(List<Content> anounceList) {
		this.anounceList = anounceList;
	}
	
}
