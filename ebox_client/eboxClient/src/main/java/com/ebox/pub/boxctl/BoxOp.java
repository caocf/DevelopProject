package com.ebox.pub.boxctl;

public class BoxOp {
	private Integer opId;	
	private BoxInfo opBox;
	private int report = 0;//1：存放数据库 2：不需要放数据库
	private int lock = 0;//1:释放箱门 2：锁定箱门
	private resultListener listener;

	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	public BoxInfo getOpBox() {
		return opBox;
	}
	public void setOpBox(BoxInfo opBox) {
		this.opBox = opBox;
	}
	
	public resultListener getListener() {
		return listener;
	}
	public void setListener(resultListener listener) {
		this.listener = listener;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public interface resultListener
	{
		void onResult(int result);
	}
}
