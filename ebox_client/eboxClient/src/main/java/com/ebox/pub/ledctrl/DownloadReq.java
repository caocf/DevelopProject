package com.ebox.pub.ledctrl;

public class DownloadReq {
	private byte programCnt;	//1字节
	private byte[] program;		//*字节
	public byte getProgramCnt() {
		return programCnt;
	}
	public void setProgramCnt(byte programCnt) {
		this.programCnt = programCnt;
	}
	public byte[] getProgram() {
		return program;
	}
	public void setProgram(byte[] program) {
		this.program = program;
	}
	
}
