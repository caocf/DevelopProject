package com.ebox.pub.websocket.helper;

public class ErrorDesc {
	// 返回码 0：成功 其他：异常
	public static final int CODE_SUCCESS = 0;

	public static final int EXCEPTION_CODE_F0001 = 10001; // 系统内部错误
	public static final int EXCEPTION_CODE_F0002 = 10002; // 报文格式错误
	public static final int EXCEPTION_CODE_F0003 = 10003; // 报文内容错误
	public static final int EXCEPTION_CODE_F0004 = 10004; // Session超时
	public static final int EXCEPTION_CODE_F0005 = 10005; // 不能连接到服务器
	public static final int EXCEPTION_CODE_F0006 = 10006; // 未知命令

	// TS错误码
	public static final int EXCEPTION_CODE_E0001 = 20001; // 未识别的操作员（快递业务员或者管理维护人员账号不存在）
	public static final int EXCEPTION_CODE_E0002 = 20002; // 密码无效（操作员或者用户的密码错误）
	public static final int EXCEPTION_CODE_E0003 = 20003; // 未识别的用户（用户输入的快件编号或者手机号不存在）
	public static final int EXCEPTION_CODE_E0004 = 20004; // 快件编号无效（快递业务员投递快件时输入的快件编号无效）
	public static final int EXCEPTION_CODE_E0005 = 20005; // 请求处理超时
	public static final int EXCEPTION_CODE_E0006 = 20006; // 没有可用的格口（没有符合条件的空格口，不能继续投递）
	public static final int EXCEPTION_CODE_E0007 = 20007; // 没有可用的快件箱
	public static final int EXCEPTION_CODE_E0008 = 20008; // 快件箱暂停服务
	public static final int EXCEPTION_CODE_E0009 = 20009; // 未识别的快件箱代码
	public static final int EXCEPTION_CODE_E0010 = 20010; // 未识别的格口代码
	public static final int EXCEPTION_CODE_E0011 = 20011; // 该单号已存在，同一快件不允许多次投递
	public static final int EXCEPTION_CODE_E0012 = 20012; // 手机号码已经注册
	public static final int EXCEPTION_CODE_E0013 = 20013; // 手机号码不存在
	public static final int EXCEPTION_CODE_E0014 = 20014; // 密码不正确
	public static final int EXCEPTION_CODE_E0015 = 20015; // 验证码发送次数过多
	public static final int EXCEPTION_CODE_E0016 = 20016; // 重试次数过多
	public static final int EXCEPTION_CODE_E0017 = 20017; // 验证码输入错误
	public static final int EXCEPTION_CODE_E0018 = 20018; // 已提交申请，请等待审核
	public static final int EXCEPTION_CODE_E0019 = 20019; // 此网点未开通权限，是否申请开通？
	public static final int EXCEPTION_CODE_E0020 = 20020; // 原手机号码不正确
	public static final int EXCEPTION_CODE_E0021 = 20021; // 余额不足
	public static final int EXCEPTION_CODE_E0022 = 20022; // 原密码不正确

	public static final int EXCEPTION_CODE_E0023 = 20023; // 开门失败，请重新操作
	public static final int EXCEPTION_CODE_E0024 = 20024; // 提交订单成功返回后存放数据库异常
	public static final int EXCEPTION_CODE_E0025 = 20025; // 箱门锁定失败
	public static final int EXCEPTION_CODE_E0026 = 20026; // 管理员取走带同步
	public static final int EXCEPTION_CODE_E0027 = 20027; // 本地不存在订单
	public static final int EXCEPTION_CODE_E0028 = 20028; //

	// AS错误码

	public static String getErrorDesc(int code) {
		if (code == EXCEPTION_CODE_F0001) {
			return "系统内部错误";
		} else if (code == EXCEPTION_CODE_F0002) {
			return "报文格式错误";
		} else if (code == EXCEPTION_CODE_F0003) {
			return "报文内容错误";
		} else if (code == EXCEPTION_CODE_F0004) {
			return "操作超时";
		} else if (code == EXCEPTION_CODE_F0005) {
			return "不能连接到服务器";
		} else if (code == EXCEPTION_CODE_F0006) {
			return "未知命令";
		} else if (code == EXCEPTION_CODE_E0001) {
			return "无此操作员";
		} else if (code == EXCEPTION_CODE_E0002) {
			return "密码输入错误";
		} else if (code == EXCEPTION_CODE_E0003) {
			return "无此用户";
		} else if (code == EXCEPTION_CODE_E0004) {
			return "快件单号无效";
		} else if (code == EXCEPTION_CODE_E0005) {
			return "请求处理超时";
		} else if (code == EXCEPTION_CODE_E0006) {
			return "没有可用的格口";
		} else if (code == EXCEPTION_CODE_E0007) {
			return "没有可用的快件箱";
		} else if (code == EXCEPTION_CODE_E0008) {
			return "快件箱暂停服务";
		} else if (code == EXCEPTION_CODE_E0009) {
			return "未识别的快件箱代码";
		} else if (code == EXCEPTION_CODE_E0010) {
			return "未识别的格口代码";
		} else if (code == EXCEPTION_CODE_E0011) {
			return "该单号已存在，同一快件不允许多次投递";
		} else if (code == EXCEPTION_CODE_E0012) {
			return "手机号码已经注册";
		} else if (code == EXCEPTION_CODE_E0013) {
			return "手机号码不存在";
		} else if (code == EXCEPTION_CODE_E0014) {
			return "密码不正确";
		} else if (code == EXCEPTION_CODE_E0015) {
			return "验证码发送次数过多";
		} else if (code == EXCEPTION_CODE_E0016) {
			return "重试次数过多";
		} else if (code == EXCEPTION_CODE_E0017) {
			return "验证码输入错误";
		} else if (code == EXCEPTION_CODE_E0018) {
			return "已提交申请，请等待审核";
		} else if (code == EXCEPTION_CODE_E0019) {
			return "此网点未开通权限，请联系客服开通";
		} else if (code == EXCEPTION_CODE_E0020) {
			return "原手机号码不正确";
		} else if (code == EXCEPTION_CODE_E0021) {
			return "余额不足";
		} else if (code == EXCEPTION_CODE_E0022) {
			return "原密码不正确";
		} else if (code == EXCEPTION_CODE_E0023) {
			return "开门失败，请重新操作";
		} else if (code == EXCEPTION_CODE_E0024) {
			return "提交订单成功返回后存放数据库异常";
		} else if (code == EXCEPTION_CODE_E0025) {
			return "箱门锁定失败";
		} else if (code == CODE_SUCCESS) {
			return "开门成功";
		}else if (code == EXCEPTION_CODE_E0026) {
			return "管理员取走待同步";
		}else if (code == EXCEPTION_CODE_E0027) {
			return "本地不存在该订单";
		}else if (code == EXCEPTION_CODE_E0028) {
			return "管理员取出";
		}

		return "未知错误";
	}
}
