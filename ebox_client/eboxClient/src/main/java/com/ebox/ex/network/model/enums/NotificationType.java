package com.ebox.ex.network.model.enums;

public interface NotificationType {
	public static final int get_noti = 0;			// 取件通知
	public static final int done_noti = 1;			// 取件完成通知
	public static final int down_noti = 2;			// 终端断线通知
	public static final int resume_noti = 3;		// 终端恢复通知
	public static final int urge_get_noti = 4;		// 催取短信
	public static final int timeout_get_noti = 5;	// 逾期件被快递员取走通知
}
