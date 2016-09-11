package com.ebox.ex.network.model.enums;

public interface ErrorCode {
	public static final int EXCEPTION_CODE_F0001 = 10001;				//系统内部错误
	public static final int EXCEPTION_CODE_F0002 = 10002;				//报文格式错误
	public static final int EXCEPTION_CODE_F0003 = 10003;				//报文内容错误
	public static final int EXCEPTION_CODE_F0004 = 10004;				//Session超时
	public static final int EXCEPTION_CODE_F0005 = 10005;				//不能连接到服务器
	
	public static final int EXCEPTION_CODE_E0001 = 20001;				//未识别的操作员（快递业务员或者管理维护人员账号不存在）
	public static final int EXCEPTION_CODE_E0002 = 20002;				//密码无效（操作员或者用户的密码错误）
	public static final int EXCEPTION_CODE_E0003 = 20003;				//未识别的用户（用户输入的快件编号或者手机号不存在）
	public static final int EXCEPTION_CODE_E0004 = 20004;				//快件编号无效（快递业务员投递快件时输入的快件编号无效）
	public static final int EXCEPTION_CODE_E0005 = 20005;				//请求处理超时
	public static final int EXCEPTION_CODE_E0006 = 20006;				//没有可用的格口（没有符合条件的空格口，不能继续投递）
	public static final int EXCEPTION_CODE_E0007 = 20007;				//没有可用的快件箱
	public static final int EXCEPTION_CODE_E0008 = 20008;				//快件箱暂停服务
	public static final int EXCEPTION_CODE_E0009 = 20009;				//未识别的快件箱代码
	public static final int EXCEPTION_CODE_E0010 = 20010;				//未识别的格口代码
}
