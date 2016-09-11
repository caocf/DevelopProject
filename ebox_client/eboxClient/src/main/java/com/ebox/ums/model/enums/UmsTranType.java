package com.ebox.ums.model.enums;

public interface UmsTranType {
	public static final int cardInit   = 0x101; //卡初始化成功
	public static final int cardProcInitFailed   = 0x102; //卡初始化失败
	public static final int cardTimeOut = 0x103;//读卡超时
	public static final int cardReadState   = 0x104; //读卡状态成功
	public static final int cardProc   = 0x105; //读卡成功
	public static final int pinInit   = 0x106; //键盘初始化成功
	public static final int inputNum   = 0x107; //输入了数字
	public static final int inputDel   = 0x108; //输入了删除键
	public static final int inputEnt   = 0x109; //输入了回车键
	public static final int tranSuccess= 0x110; //交易成功
	public static final int tranfailed = 0x111; //交易失败
	public static final int transInit = 0x112; //交易初始化成功
	public static final int queryCredit = 0x113; //查询信用卡手续费成功
	public static final int queryElec = 0x114; //查询电费成功
	public static final int queryFuel = 0x115; //查询煤气费成功
	public static final int queryWater = 0x116; //查询水费成功
	public static final int queryTraffic = 0x117; //查询罚没成功
}
