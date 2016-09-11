package com.ebox.pub.ledctrl;

public class TimeArea {
	private byte no;		//1字节
	private byte[] len;		//4字节
	private byte type;		//1字节
	private byte[] startx;	//2字节
	private byte[] starty;	//2字节
	private byte[] endx;	//2字节
	private byte[] endy;	//2字节
	private byte mode;		//1字节
	private byte way;		//1字节
	private byte color;		//1字节
	private byte delay;		//2字节
	private byte time;		//7字节
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
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte[] getStartx() {
		return startx;
	}
	public void setStartx(byte[] startx) {
		this.startx = startx;
	}
	public byte[] getStarty() {
		return starty;
	}
	public void setStarty(byte[] starty) {
		this.starty = starty;
	}
	public byte[] getEndx() {
		return endx;
	}
	public void setEndx(byte[] endx) {
		this.endx = endx;
	}
	public byte[] getEndy() {
		return endy;
	}
	public void setEndy(byte[] endy) {
		this.endy = endy;
	}
	public byte getMode() {
		return mode;
	}
	public void setMode(byte mode) {
		this.mode = mode;
	}
	public byte getWay() {
		return way;
	}
	public void setWay(byte way) {
		this.way = way;
	}
	public byte getColor() {
		return color;
	}
	public void setColor(byte color) {
		this.color = color;
	}
	public byte getDelay() {
		return delay;
	}
	public void setDelay(byte delay) {
		this.delay = delay;
	}
	public byte getTime() {
		return time;
	}
	public void setTime(byte time) {
		this.time = time;
	}
}
