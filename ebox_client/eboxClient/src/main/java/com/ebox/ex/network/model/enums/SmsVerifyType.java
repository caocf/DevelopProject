package com.ebox.ex.network.model.enums;

public interface SmsVerifyType {
	public static final int regedit = 1;		//1：注册
	public static final int forget_pwd = 2;		//2：忘记密码
	public static final int change_phone = 3;	//3：修改手机号
	public static final int op_regedit = 4;		//4：快递员注册
	public static final int op_forget_pwd = 5;	//5：快递员忘记密码
	
	public static final int min_type = regedit;
	public static final int max_type = op_forget_pwd;
}
