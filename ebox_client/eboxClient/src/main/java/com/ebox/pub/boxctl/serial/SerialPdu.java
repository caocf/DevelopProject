package com.ebox.pub.boxctl.serial;

public class SerialPdu {
	private byte[] head;		// 0XAA	0X55 2字节
	private byte destAddr;	// 1字节
	private byte srcAddr;		// 1字节
	private byte len;			// 1字节
	private byte msgType;		// 1字节
	private Object body; 
	private byte check;			//１字节
	public byte[] getHead() {
		return head;
	}
	public void setHead(byte[] head) {
		this.head = head;
	}
	public byte getDestAddr() {
		return destAddr;
	}
	public void setDestAddr(byte destAddr) {
		this.destAddr = destAddr;
	}
	public byte getSrcAddr() {
		return srcAddr;
	}
	public void setSrcAddr(byte srcAddr) {
		this.srcAddr = srcAddr;
	}
	public byte getLen() {
		return len;
	}
	public void setLen(byte len) {
		this.len = len;
	}
	public byte getMsgType() {
		return msgType;
	}
	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public byte getCheck() {
		return check;
	}
	public void setCheck(byte check) {
		this.check = check;
	}
	
}
