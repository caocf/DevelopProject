package com.ebox.ex.network.model.enums;

public interface NotificationState {
	public static final int sending = 0;			// 发送中
	public static final int send_failed = 1;		// 发送失败
	public static final int send_success = 2;		// 发送成功
}
