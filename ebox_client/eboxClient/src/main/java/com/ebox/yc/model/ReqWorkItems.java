package com.ebox.yc.model;


public class ReqWorkItems {
	private String  windowId;
	private String pageIndex;
	private String pageSize;
	private int channel;
	
	public String getWindowId() {
		return windowId;
	}
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	
}
