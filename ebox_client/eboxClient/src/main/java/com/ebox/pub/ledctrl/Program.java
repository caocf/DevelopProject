package com.ebox.pub.ledctrl;

public class Program {
	private byte no;	//1字节
	private byte[] len;	//4字节
	private byte cnt;	//1字节
	private byte[] time;//2字节
	private byte timeMode;	//1字节
	private byte weekMode;	//1字节
	private byte[] start;	//7字节
	private byte[] end;		//7字节
	private byte[] data;	//*字节
	public byte getNo() {
		return no;
	}
	public void setNo(byte no) {
		this.no = no;
	}
	public byte[] getLen() {
		return len;
	}
	public void setLen(byte[] len) {
		this.len = len;
	}
	public byte getCnt() {
		return cnt;
	}
	public void setCnt(byte cnt) {
		this.cnt = cnt;
	}
	public byte[] getTime() {
		return time;
	}
	public void setTime(byte[] time) {
		this.time = time;
	}
	public byte getTimeMode() {
		return timeMode;
	}
	public void setTimeMode(byte timeMode) {
		this.timeMode = timeMode;
	}
	public byte getWeekMode() {
		return weekMode;
	}
	public void setWeekMode(byte weekMode) {
		this.weekMode = weekMode;
	}
	public byte[] getStart() {
		return start;
	}
	public void setStart(byte[] start) {
		this.start = start;
	}
	public byte[] getEnd() {
		return end;
	}
	public void setEnd(byte[] end) {
		this.end = end;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
}
