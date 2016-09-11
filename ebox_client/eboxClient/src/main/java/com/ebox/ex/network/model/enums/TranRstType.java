package com.ebox.ex.network.model.enums;

public class TranRstType {
	public static final String Logcat ="unionpay";
	public static final String RESULTCODE00 = "交易成功";
	public static final String RESULTCODE96 = "系统繁忙";
	public static final String RESULTCODEH1 = "缴费金额不正确";
	public static final String RESULTCODEH2 = "请求渠道流水号错误";
	public static final String RESULTCODEH3 = "出账机构无效";
	public static final String RESULTCODEH4 = "查询号错误";
	public static final String RESULTCODEH5 = "查询渠道号错误";
	public static final String RESULTCODEH6 = "查询号类型错误";
	public static final String RESULTCODEH7 = "账单查询处理错误";
	public static final String RESULTCODEH8 = "密码加密失败";
	public static final String RESULTCODEH9 = "获取出账机构ID[MER_ID]出错";
	public static final String RESULTCODEL0 = "账单查询失败";
	public static final String RESULTCODEL1 = "账单私有域填写错误";
	public static final String RESULTCODEL2 = "下单失败";
	public static final String RESULTCODEL3 = "订单查询用户号为空";
	public static final String RESULTCODEL4 = "订单查询机构代码为空";
	public static final String RESULTCODEL5 = "订单开始日期格式有误";
	public static final String RESULTCODEL7 = "未出账或已经缴纳,暂时无需缴费";
	public static final String RESULTCODEL8 = "超过受理期,银行不予受理,请至缴费单位缴费";
	public static final String RESULTCODEL9 = "超过缴费时间";
	public static final String RESULTCODEM0 = "暂时无法缴费,请联系CHINAPAY或自来水公司";
	public static final String RESULTCODEM1 = "预付费交易金额不足";
	public static final String RESULTCODEM2 = "该户号不存在";
	public static final String RESULTCODEM3 = "超过限定金额";
	public static final String RESULTCODE01 = "请持卡人与发卡银行联系";
	public static final String RESULTCODE02 = "查发卡方的特殊条件";
	public static final String RESULTCODE03 = "无效商户";
	public static final String RESULTCODE04 = "此卡被没收";
	public static final String RESULTCODE05 = "持卡人认证失败";
	public static final String RESULTCODE06 = "出错";
	public static final String RESULTCODE07 = "特殊条件下没收卡";
	public static final String RESULTCODE09 = "请求正在处理中";
	public static final String RESULTCODE10 = "显示部分批准金额，提示操作员";
	public static final String RESULTCODE11 = "此为VIP客户";
	public static final String RESULTCODE12 = "无效交易";
	public static final String RESULTCODE13 = "无效金额";
	public static final String RESULTCODE14 = "无效卡号";
	public static final String RESULTCODE15 = "此卡无对应发卡方";
	public static final String RESULTCODE19 = "请重做交易";
	public static final String RESULTCODE20 = "无效应答";
	public static final String RESULTCODE21 = "该卡未初始化";
	public static final String RESULTCODE22 = "操作有误，或超出交易允许天数";
	public static final String RESULTCODE23 = "不可接受的交易费";
	public static final String RESULTCODE25 = "没有原始交易，请联系发卡方";
	public static final String RESULTCODE30 = "请重试";
	public static final String RESULTCODE31 = "交换中心不支持的银行";
	public static final String RESULTCODE33 = "过期的卡（没收卡）";
	public static final String RESULTCODE34 = "作弊卡,呑卡";
	public static final String RESULTCODE35 = "与安全保密部门联系（没收卡）";
	public static final String RESULTCODE36 = "受限制的卡（没收卡）";
	public static final String RESULTCODE37 = "呼受理方安全保密部门（没收卡）";
	public static final String RESULTCODE38 = "密码错误次数超限，请与发卡方联系";
	public static final String RESULTCODE39 = "无此信用卡帐户";
	public static final String RESULTCODE40 = "发卡方不支持的交易";
	public static final String RESULTCODE41 = "此卡已挂失,呑卡";
	public static final String RESULTCODE42 = "无此帐户";
	public static final String RESULTCODE43 = "此卡被没收，请与发卡方联系";
	public static final String RESULTCODE44 = "无此投资帐户";
	public static final String RESULTCODE45 = "ISO保留使用";
	public static final String RESULTCODE46 = "ISO保留使用";
	public static final String RESULTCODE47 = "ISO保留使用";
	public static final String RESULTCODE48 = "ISO保留使用";
	public static final String RESULTCODE49 = "ISO保留使用";
	public static final String RESULTCODE50 = "ISO保留使用";
	public static final String RESULTCODE51 = "可用余额不足";
	public static final String RESULTCODE52 = "无此支票帐户";
	public static final String RESULTCODE53 = "无此储蓄卡帐户";
	public static final String RESULTCODE54 = "该卡已过期";
	public static final String RESULTCODE55 = "密码错误";
	public static final String RESULTCODE56 = "无此卡记录";
	public static final String RESULTCODE57 = "发卡方不允许进行此交易";
	public static final String RESULTCODE58 = "发卡方不允许该卡在本终端进行此交易";
	public static final String RESULTCODE59 = "卡片校验错";
	
	public static final String RESULTCODE60 = "受卡方与安全保密部门联系";
	public static final String RESULTCODE61 = "交易金额超限";
	public static final String RESULTCODE62 = "受限制的卡";
	public static final String RESULTCODE63 = "违反安全保密规定";
	public static final String RESULTCODE64 = "交易金额与原交易不匹配";
	public static final String RESULTCODE65 = "超出取款次数限制";
	public static final String RESULTCODE66 = "受卡方呼受理方安全保密部门";
	public static final String RESULTCODE67 = "捕捉（没收卡）";
	public static final String RESULTCODE68 = "交易超时，请重试";
	public static final String RESULTCODE69 = "ISO保留使用";
	
	public static final String RESULTCODE70 = "ISO保留使用";
	public static final String RESULTCODE71 = "ISO保留使用";
	public static final String RESULTCODE72 = "ISO保留使用";
	public static final String RESULTCODE73 = "ISO保留使用";
	public static final String RESULTCODE74 = "ISO保留使用";
	public static final String RESULTCODE75 = "密码错误次数超限";
	public static final String RESULTCODE77 = "POS批次与网络中心不一致";
	public static final String RESULTCODE78 = "网络中心需要向POS终端下载数据";
	public static final String RESULTCODE79 = "POS终端上传的脱机数据对帐不平";
	
	public static final String RESULTCODE89 = "私有保留使用";
	
	public static final String RESULTCODE90 = "请稍后重试";
	public static final String RESULTCODE91 = "发卡方状态不正常，请稍后重试";
	public static final String RESULTCODE92 = "请稍后重试";
	public static final String RESULTCODE93 = "交易违法、不能完成";
	public static final String RESULTCODE94 = "拒绝，重复交易，请稍后重试";
	public static final String RESULTCODE95 = "调节控制错";
	public static final String RESULTCODE97 = "终端号未登记";
	public static final String RESULTCODE98 = "发卡方超时";
	public static final String RESULTCODE99 = "PIN格式错请重新签到";
	
	public static final String RESULTCODEA0 = "MAC校验错，请重新签到";
	public static final String RESULTCODEA1 = "转账货币不一致";
	public static final String RESULTCODEA2 = "交易成功，请向资金转入行确认";
	public static final String RESULTCODEA3 = "校验密钥错";
	public static final String RESULTCODEA4 = "交易成功，请向资金到账行确认";
	public static final String RESULTCODEA5 = "交易成功，请向资金到账行确认";
	public static final String RESULTCODEA6 = "交易成功，请向资金到账行确认";
	
	
	public static final String RESULTCODEB1 = "此业务无欠费";
	public static final String RESULTCODEB2 = "打包8583报文失败";
	public static final String RESULTCODEB3 = "计算MAC失败";
	public static final String RESULTCODEB4 = "不支持的交易类型";
	public static final String RESULTCODEB5 = "冲正失败";
	
	public static final String RESULTCODEE1 = "用户号码无效";
	public static final String RESULTCODEE2 = "支付号码无效";
	public static final String RESULTCODEE3 = "费用已缴";
	public static final String RESULTCODEE4 = "单笔交易金额超限";
	public static final String RESULTCODEE5 = "日累计交易金额超限";
	public static final String RESULTCODEE6 = "风险卡账户，请联系发卡行";
	public static final String RESULTCODEE7 = "支付密码错误";
	public static final String RESULTCODEE8 = "无效的终端交易时间";
	public static final String RESULTCODEE9 = "委托关系用户号码数量超限";
	
	public static final String RESULTCODEF1 = "退货交易无效";
	public static final String RESULTCODEF2 = "交易超时，请重试";
	public static final String RESULTCODEF3 = "无效的行业商户";
	public static final String RESULTCODEF4 = "行业商户状态异常";
	public static final String RESULTCODEF5 = "行业商户超时";
	public static final String RESULTCODEF6 = "无效的交易币种";
	public static final String RESULTCODEF7 = "无效的交易发起方";
	public static final String RESULTCODEF8 = "交易发起方状态异常";
	public static final String RESULTCODEF9 = "转出卡委托不存在";
	
	public static final String RESULTCODEX1 = "交易类型错误";
	public static final String RESULTCODEX2 = "接口文件格式错";
	public static final String RESULTCODEX3 = "初始化输出接口文件错";
	public static final String RESULTCODEX4 = "已结算";
	public static final String RESULTCODEX5 = "该交易已经撤消";
	public static final String RESULTCODEX6 = "原交易不是消费交易";
	
	public static final String RESULTCODEYY = "商户号校验错";
	public static final String RESULTCODEY1 = "二磁道信息错误";
	public static final String RESULTCODEY2 = "冲正或接口转换交易类型错";
	public static final String RESULTCODEY3 = "init配置文件失败";
	public static final String RESULTCODEY4 = "未找到原交易";
	public static final String RESULTCODEY5 = "与密码键盘通讯失败";
	public static final String RESULTCODEY6 = "报文错误";
	public static final String RESULTCODEY7 = "冲正文件错误，操作系统异常";
	public static final String RESULTCODEY8 = "通讯链路异常，请检查连接";
	public static final String RESULTCODEY9 = "结算文件生成错误";
	
	public static final String RESULTCODEZA = "校验错，请重新签到";
	public static final String RESULTCODEZ0 = "未收到应答";
	public static final String RESULTCODEZ1 = "交易超时，请重试";
	public static final String RESULTCODEZ2 = "因数据包错误引发冲正";
	public static final String RESULTCODEZ3 = "因交易类型错误引发冲正";
	public static final String RESULTCODEZ4 = "因原交易不匹配引发冲正";
	public static final String RESULTCODEZ5 = "因签购单生成失败冲正,请重试";
	
	public static final String RESULTCODEG1 = "高风险用户号码";
	public static final String RESULTCODEG2 = "无效委托关系 ";
	public static final String RESULTCODEG3 = "银行卡注册信息无效";
	public static final String RESULTCODEG4 = "用户号关联信息无效";
	
	public static final String RESULTCODEK1 = "系统未开通该交易";
	public static final String RESULTCODET2 = "信用卡受限";
	
	
	
	public static String getRstCodeStr(String s) {
		
		if(s.equals("00")){
			return TranRstType.RESULTCODE00;
		} else if(s.equals("01")){
			return TranRstType.RESULTCODE01;
		}else if(s.equals("02")){
			return TranRstType.RESULTCODE02;
		}else if(s.equals("03")){
			return TranRstType.RESULTCODE03;
		}else if(s.equals("04")){
			return TranRstType.RESULTCODE04;
		}else if(s.equals("05")){
			return TranRstType.RESULTCODE05;
		}else if(s.equals("06")){
			return TranRstType.RESULTCODE06;
		}else if(s.equals("07")){
			return TranRstType.RESULTCODE07;
		}else if(s.equals("09")){
			return TranRstType.RESULTCODE09;
		}else if(s.equals("11")){
			return TranRstType.RESULTCODE11;
		}else if(s.equals("12")){
			return TranRstType.RESULTCODE12;
		}else if(s.equals("13")){
			return TranRstType.RESULTCODE13;
		}else if(s.equals("14")){
			return TranRstType.RESULTCODE14;
		}else if(s.equals("15")){
			return TranRstType.RESULTCODE15;
		}else if(s.equals("10")){
			return TranRstType.RESULTCODE10;
		}else if(s.equals("20")){
			return TranRstType.RESULTCODE20;
		}else if(s.equals("21")){
			return TranRstType.RESULTCODE21;
		}else if(s.equals("22")){
			return TranRstType.RESULTCODE22;
		}else if(s.equals("23")){
			return TranRstType.RESULTCODE23;
		}else if(s.equals("25")){
			return TranRstType.RESULTCODE25;
		}else if(s.equals("30")){
			return TranRstType.RESULTCODE30;
		}else if(s.equals("31")){
			return TranRstType.RESULTCODE31;
		}else if(s.equals("33")){
			return TranRstType.RESULTCODE33;
		}else if(s.equals("34")){
			return TranRstType.RESULTCODE34;
		}else if(s.equals("35")){
			return TranRstType.RESULTCODE35;
		}else if(s.equals("36")){
			return TranRstType.RESULTCODE36;
		}else if(s.equals("37")){
			return TranRstType.RESULTCODE37;
		}else if(s.equals("38")){
			return TranRstType.RESULTCODE38;
		}else if(s.equals("39")){
			return TranRstType.RESULTCODE39;
		}else if(s.equals("40")){
			return TranRstType.RESULTCODE40;
		}else if(s.equals("41")){
			return TranRstType.RESULTCODE41;
		}else if(s.equals("42")){
			return TranRstType.RESULTCODE42;
		}else if(s.equals("43")){
			return TranRstType.RESULTCODE43;
		}else if(s.equals("44")){
			return TranRstType.RESULTCODE44;
		}else if(s.equals("45")){
			return TranRstType.RESULTCODE45;
		}else if(s.equals("46")){
			return TranRstType.RESULTCODE46;
		}else if(s.equals("47")){
			return TranRstType.RESULTCODE47;
		}else if(s.equals("48")){
			return TranRstType.RESULTCODE42;
		}else if(s.equals("42")){
			return TranRstType.RESULTCODE48;
		}else if(s.equals("49")){
			return TranRstType.RESULTCODE49;
		}else if(s.equals("50")){
			return TranRstType.RESULTCODE50;
		}else if(s.equals("51")){
			return TranRstType.RESULTCODE51;
		}else if(s.equals("52")){
			return TranRstType.RESULTCODE52;
		}else if(s.equals("52")){
			return TranRstType.RESULTCODE52;
		}else if(s.equals("53")){
			return TranRstType.RESULTCODE53;
		}else if(s.equals("54")){
			return TranRstType.RESULTCODE54;
		}else if(s.equals("55")){
			return TranRstType.RESULTCODE55;
		}else if(s.equals("56")){
			return TranRstType.RESULTCODE56;
		}else if(s.equals("57")){
			return TranRstType.RESULTCODE57;
		}else if(s.equals("58")){
			return TranRstType.RESULTCODE58;
		}else if(s.equals("59")){
			return TranRstType.RESULTCODE59;
		}else if(s.equals("60")){
			return TranRstType.RESULTCODE60;
		}else if(s.equals("61")){
			return TranRstType.RESULTCODE61;
		}else if(s.equals("62")){
			return TranRstType.RESULTCODE62;
		}else if(s.equals("63")){
			return TranRstType.RESULTCODE63;
		}else if(s.equals("64")){
			return TranRstType.RESULTCODE64;
		}else if(s.equals("65")){
			return TranRstType.RESULTCODE65;
		}else if(s.equals("66")){
			return TranRstType.RESULTCODE66;
		}else if(s.equals("67")){
			return TranRstType.RESULTCODE67;
		}else if(s.equals("68")){
			return TranRstType.RESULTCODE68;
		}else if(s.equals("69")){
			return TranRstType.RESULTCODE69;
		}else if(s.equals("70")){
			return TranRstType.RESULTCODE70;
		}else if(s.equals("71")){
			return TranRstType.RESULTCODE71;
		}else if(s.equals("72")){
			return TranRstType.RESULTCODE72;
		}else if(s.equals("73")){
			return TranRstType.RESULTCODE73;
		}else if(s.equals("74")){
			return TranRstType.RESULTCODE74;
		}else if(s.equals("75")){
			return TranRstType.RESULTCODE75;
		}else if(s.equals("77")){
			return TranRstType.RESULTCODE77;
		}else if(s.equals("78")){
			return TranRstType.RESULTCODE78;
		}else if(s.equals("79")){
			return TranRstType.RESULTCODE79;
		}else if(s.equals("89")){
			return TranRstType.RESULTCODE89;
		}else if(s.equals("90")){
			return TranRstType.RESULTCODE90;
		}else if(s.equals("91")){
			return TranRstType.RESULTCODE91;
		}else if(s.equals("92")){
			return TranRstType.RESULTCODE92;
		}else if(s.equals("93")){
			return TranRstType.RESULTCODE93;
		}else if(s.equals("94")){
			return TranRstType.RESULTCODE94;
		}else if(s.equals("95")){
			return TranRstType.RESULTCODE95;
		}else if(s.equals("96")){
			return TranRstType.RESULTCODE96;
		}else if(s.equals("97")){
			return TranRstType.RESULTCODE97;
		}else if(s.equals("98")){
			return TranRstType.RESULTCODE98;
		}else if(s.equals("99")){
			return TranRstType.RESULTCODE99;
		}else if(s.equals("A0")){
			return TranRstType.RESULTCODEA0;
		}else if(s.equals("A1")){
			return TranRstType.RESULTCODEA1;
		}else if(s.equals("A2")){
			return TranRstType.RESULTCODEA2;
		}else if(s.equals("A3")){
			return TranRstType.RESULTCODEA3;
		}else if(s.equals("A4")){
			return TranRstType.RESULTCODEA4;
		}else if(s.equals("A5")){
			return TranRstType.RESULTCODEA5;
		}else if(s.equals("A6")){
			return TranRstType.RESULTCODEA6;
		}else if(s.equals("B1")){
			return TranRstType.RESULTCODEB1;
		}else if(s.equals("B2")){
			return TranRstType.RESULTCODEB2;
		}else if(s.equals("B3")){
			return TranRstType.RESULTCODEB3;
		}else if(s.equals("B4")){
			return TranRstType.RESULTCODEB4;
		}else if(s.equals("B5")){
			return TranRstType.RESULTCODEB5;
		}else if(s.equals("E1")){
			return TranRstType.RESULTCODEE1;
		}else if(s.equals("E2")){
			return TranRstType.RESULTCODEE2;
		}else if(s.equals("E3")){
			return TranRstType.RESULTCODEE3;
		}else if(s.equals("E4")){
			return TranRstType.RESULTCODEE4;
		}else if(s.equals("E5")){
			return TranRstType.RESULTCODEE5;
		}else if(s.equals("E6")){
			return TranRstType.RESULTCODEE6;
		}else if(s.equals("E7")){
			return TranRstType.RESULTCODEE7;
		}else if(s.equals("E8")){
			return TranRstType.RESULTCODEE8;
		}else if(s.equals("E9")){
			return TranRstType.RESULTCODEE9;
		}else if(s.equals("F1")){
			return TranRstType.RESULTCODEF1;
		}else if(s.equals("F2")){
			return TranRstType.RESULTCODEF2;
		}else if(s.equals("F3")){
			return TranRstType.RESULTCODEF3;
		}else if(s.equals("F4")){
			return TranRstType.RESULTCODEF4;
		}else if(s.equals("F5")){
			return TranRstType.RESULTCODEF5;
		}else if(s.equals("F6")){
			return TranRstType.RESULTCODEF6;
		}else if(s.equals("F7")){
			return TranRstType.RESULTCODEF7;
		}else if(s.equals("F8")){
			return TranRstType.RESULTCODEF8;
		}else if(s.equals("F9")){
			return TranRstType.RESULTCODEF9;
		}else if(s.equals("X1")){
			return TranRstType.RESULTCODEX1;
		}else if(s.equals("X2")){
			return TranRstType.RESULTCODEX2;
		}else if(s.equals("X3")){
			return TranRstType.RESULTCODEX3;
		}else if(s.equals("X4")){
			return TranRstType.RESULTCODEX4;
		}else if(s.equals("X5")){
			return TranRstType.RESULTCODEX5;
		}else if(s.equals("X6")){
			return TranRstType.RESULTCODEX6;
		}else if(s.equals("YY")){
			return TranRstType.RESULTCODEYY;
		}else if(s.equals("Y1")){
			return TranRstType.RESULTCODEY1;
		}else if(s.equals("Y2")){
			return TranRstType.RESULTCODEY2;
		}else if(s.equals("Y3")){
			return TranRstType.RESULTCODEY3;
		}else if(s.equals("Y4")){
			return TranRstType.RESULTCODEY4;
		}else if(s.equals("Y5")){
			return TranRstType.RESULTCODEY5;
		}else if(s.equals("Y6")){
			return TranRstType.RESULTCODEY6;
		}else if(s.equals("Y7")){
			return TranRstType.RESULTCODE97;
		}else if(s.equals("Y8")){
			return TranRstType.RESULTCODEY8;
		}else if(s.equals("Y9")){
			return TranRstType.RESULTCODEY9;
		}else if(s.equals("ZA")){
			return TranRstType.RESULTCODEZA;
		}else if(s.equals("Z0")){
			return TranRstType.RESULTCODEZ0;
		}else if(s.equals("Z1")){
			return TranRstType.RESULTCODEZ1;
		}else if(s.equals("Z2")){
			return TranRstType.RESULTCODEZ2;
		}else if(s.equals("Z3")){
			return TranRstType.RESULTCODEZ3;
		}else if(s.equals("Z4")){
			return TranRstType.RESULTCODEZ4;
		}else if(s.equals("Z5")){
			return TranRstType.RESULTCODEZ5;
		}else if(s.equals("H1")){
			return TranRstType.RESULTCODEH1;
		}else if(s.equals("H2")){
			return TranRstType.RESULTCODEH2;
		}else if(s.equals("H3")){
			return TranRstType.RESULTCODEH3;
		}else if(s.equals("H4")){
			return TranRstType.RESULTCODEH4;
		}else if(s.equals("H5")){
			return TranRstType.RESULTCODEH5;
		}else if(s.equals("H6")){
			return TranRstType.RESULTCODEH6;
		}else if(s.equals("H7")){
			return TranRstType.RESULTCODEH7;
		}else if(s.equals("H8")){
			return TranRstType.RESULTCODEH8;
		}else if(s.equals("H9")){
			return TranRstType.RESULTCODEH9;
		}else if(s.equals("L0")){
			return TranRstType.RESULTCODEL0;
		}else if(s.equals("L1")){
			return TranRstType.RESULTCODEL1;
		}else if(s.equals("L2")){
			return TranRstType.RESULTCODEL2;
		}else if(s.equals("L3")){
			return TranRstType.RESULTCODEL3;
		}else if(s.equals("L4")){
			return TranRstType.RESULTCODEL4;
		}else if(s.equals("L5")){
			return TranRstType.RESULTCODEL5;
		}else if(s.equals("L7")){
			return TranRstType.RESULTCODEL7;
		}else if(s.equals("L8")){
			return TranRstType.RESULTCODEL8;
		}else if(s.equals("L9")){
			return TranRstType.RESULTCODEL9;
		}else if(s.equals("M0")){
			return TranRstType.RESULTCODEM0;
		}else if(s.equals("M1")){
			return TranRstType.RESULTCODEM1;
		}else if(s.equals("M2")){
			return TranRstType.RESULTCODEM2;
		}else if(s.equals("M3")){
			return TranRstType.RESULTCODEM3;
		}else if(s.equals("G1")){
			return TranRstType.RESULTCODEG1;
		}else if(s.equals("G2")){
			return TranRstType.RESULTCODEG2;
		}else if(s.equals("G3")){
			return TranRstType.RESULTCODEG3;
		}else if(s.equals("G4")){
			return TranRstType.RESULTCODEG4;
		}else if(s.equals("K1")){
			return TranRstType.RESULTCODEK1;
		}else if(s.equals("T2")){
			return TranRstType.RESULTCODET2;
		} else {
			return "交易失败";
		}
		
		
	}
}
