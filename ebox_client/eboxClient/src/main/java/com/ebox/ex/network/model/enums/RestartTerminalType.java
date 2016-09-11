package com.ebox.ex.network.model.enums;

public interface RestartTerminalType {
	public static final int restart_app = 1;
	public static final int restart_os = 2;
	public static final int min_type = restart_app;
	public static final int max_type = restart_os;
}
