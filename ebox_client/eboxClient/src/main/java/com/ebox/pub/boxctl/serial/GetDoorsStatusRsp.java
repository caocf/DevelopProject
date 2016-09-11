package com.ebox.pub.boxctl.serial;

public class GetDoorsStatusRsp {
	private int rst;
	private int[] boxState;
	public int getRst() {
		return rst;
	}
	public void setRst(int rst) {
		this.rst = rst;
	}
	public int[] getBoxState() {
		return boxState;
	}
	public void setBoxState(int[] boxState) {
		this.boxState = boxState;
	}
}
