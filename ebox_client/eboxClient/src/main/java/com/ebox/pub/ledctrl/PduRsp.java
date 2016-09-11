package com.ebox.pub.ledctrl;

public class PduRsp {
	private byte[] head;	// 4字节
	private byte dst;		// 1字节
	private byte src;		// 1字节
	private byte[] cmd;		// 2字节
	private byte[] seq;		// 2字节
	private byte[] len;		// 4字节
	private byte[] data; 	// *字节
	private byte[] check; 	// 2字节
	private byte[] tail; 	// 2字节
	public byte[] getHead() {
		return head;
	}
	public void setHead(byte[] head) {
		this.head = head;
	}
	public byte getDst() {
		return dst;
	}
	public void setDst(byte dst) {
		this.dst = dst;
	}
	public byte getSrc() {
		return src;
	}
	public void setSrc(byte src) {
		this.src = src;
	}
	public byte[] getCmd() {
		return cmd;
	}
	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}
	public byte[] getSeq() {
		return seq;
	}
	public void setSeq(byte[] seq) {
		this.seq = seq;
	}
	public byte[] getLen() {
		return len;
	}
	public void setLen(byte[] len) {
		this.len = len;
	}public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public byte[] getCheck() {
		return check;
	}
	public void setCheck(byte[] check) {
		this.check = check;
	}
	public byte[] getTail() {
		return tail;
	}
	public void setTail(byte[] tail) {
		this.tail = tail;
	}
	
	
}
