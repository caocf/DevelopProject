package com.ebox.yc.model;


public class ReqAnnounce {
	Long  ccId;
	String nodeId;
	int channel;
	int pageIndex;
	int pageSize;
	public Long getCcId() {
		return ccId;
	}
	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
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
	
	
}
