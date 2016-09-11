package com.ebox.yc.model;

import java.util.List;


public class RspWorkItems {
	private int  result;
	private String resultMsg;
	private String windowId;
	private String windowName;
	private List<WorkItem> workItems;
	
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
	public String getWindowId() {
		return windowId;
	}
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	public String getWindowName() {
		return windowName;
	}
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
	public List<WorkItem> getWorkItems() {
		return workItems;
	}
	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}
	
	
}
