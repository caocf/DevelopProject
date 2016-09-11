package com.ebox.ex.network.model.enums;

public interface BoxUserState {
	
	public static final int normal = 0;//正常
	public static final int alarmLock = 1;//告警锁定
	public static final int hardFault = 2;//硬件故障
	public static final int reserve = 3;//预留锁定
}
