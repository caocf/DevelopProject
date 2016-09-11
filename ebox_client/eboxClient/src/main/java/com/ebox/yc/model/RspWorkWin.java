package com.ebox.yc.model;

import java.util.List;


public class RspWorkWin {
	int  result;
	String resultMsg;
	List<WorkWin> workWins;
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
	public List<WorkWin> getWorkWins() {
		return workWins;
	}
	public void setWorkWins(List<WorkWin> workWins) {
		this.workWins = workWins;
	}
	
	
}
