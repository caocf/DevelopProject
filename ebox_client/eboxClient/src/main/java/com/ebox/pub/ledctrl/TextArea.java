package com.ebox.pub.ledctrl;

public class TextArea {
	private byte no;		//1字节
	private byte[] len;		//4字节
	private byte type;		//1字节
	private byte[] startx;	//2字节
	private byte[] starty;	//2字节
	private byte[] endx;	//2字节
	private byte[] endy;	//2字节
	private byte color;		//1字节
	private byte[] name;	//2字节
	private byte[] skill;	//4字节
	private byte size;		//1字节
	private byte[] cnt;		//4字节
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
	public byte getColor() {
		return color;
	}
	public void setColor(byte color) {
		this.color = color;
	}
	public byte[] getName() {
		return name;
	}
	public void setName(byte[] name) {
		this.name = name;
	}
	public byte[] getSkill() {
		return skill;
	}
	public void setSkill(byte[] skill) {
		this.skill = skill;
	}
	public byte getSize() {
		return size;
	}
	public void setSize(byte size) {
		this.size = size;
	}
	public byte[] getCnt() {
		return cnt;
	}
	public void setCnt(byte[] cnt) {
		this.cnt = cnt;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
