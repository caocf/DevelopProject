package com.ebox.yc.model;

import java.io.Serializable;

public class WorkWin implements Serializable{
	private String windowId;
	private String windowName;
	private String winPic;
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
	public String getWinPic() {
		return winPic;
	}
	public void setWinPic(String winPic) {
		this.winPic = winPic;
	}
	
}
