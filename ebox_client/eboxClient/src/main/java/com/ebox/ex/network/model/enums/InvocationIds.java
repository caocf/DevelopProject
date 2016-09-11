package com.ebox.ex.network.model.enums;

public interface InvocationIds {
	public static final int TSApplyForAvailableBox = 1;
	public static final int TSGetBoxes = 2;
	public static final int TSVerifyOperator = 3;
	public static final int TSGetDeliveryInfo = 4;
	public static final int TSConfirmDelivery = 5;
	public static final int TSCancelDelivery = 6;
	public static final int TSGetExpiredItems = 7;
	public static final int TSWithdrawExpiredItem = 8;
	public static final int TSVerifyUser = 9;
	public static final int TSPickupItem = 10;
	public static final int TSReportTerminalStatus = 11;
	public static final int TSGetConfig = 12;
	public static final int TSCheckItem = 13;
	public static final int TSRegister = 14;
	public static final int TSHeartBeat = 15;
	public static final int TSGetVerifySms = 16;
	public static final int TSResetPassword = 18;
	public static final int TSCrashReport = 20;
	public static final int TSAlarmReport = 21;
	public static final int TSItemQuery = 22;
	public static final int TSResendSms = 23;
	public static final int TSChangePwd = 24;
	public static final int TSOperatorQueryItem = 25;
	public static final int TSGetOrgnizationInfo = 26;
	public static final int TSGetAdvertise = 27;
    public static final int TSOperatorVerifyByCard = 28;

	
	public static final int STOpenBox = 3001;
	public static final int STLedRefresh = 3002;
	public static final int STRestartTerminal = 3003;
	public static final int STLedConfig = 3004;
	public static final int STUpdateVersion = 3005;
}
