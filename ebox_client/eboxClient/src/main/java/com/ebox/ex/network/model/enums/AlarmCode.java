package com.ebox.ex.network.model.enums;

public interface AlarmCode {
	/**
	 * 开锁失败告警
	 */
	public static final int code_3 = 3;		 
	/**
	 * 快递员未点击存件，自动重试失败告警
	 */
	public static final int code_8 = 8;	
	
	/*
	 * 吞卡告警
	 */
	
	public static final int code_9 = 9;	
	
	/**
	 * 断电告警
	 */
	public static final int code_10 = 10;	
	/**
	 * 故障保修
	 */
	public static final int code_11 = 11;
}
