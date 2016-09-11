package com.moge.ebox.phone.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogonUtils {

	// 位权值数组
	private static byte[] Wi = new byte[17];
	// 旧身份证长度
	private static final byte oldIDLen = 15;
	// 新身份证长度
	private static final byte newIDLen = 18;
	// 新身份证年份标志
	private static final String yearFlag = "19";
	// 身份证前部分字符数
	private static final byte fPart = 6;
	// 身份证算法求模关键值
	private static final byte fMod = 11;
	// 校验码串
	private static final String CheckCode = "10X98765432";

	// 最小的行政区划码
	// private static final int minCode = 150000;
	// 最大的行政区划码
	// private static final int maxCode = 700000;

	// public static boolean isIdCard(String idCard) {
	// if (idCard.length() != 15 && idCard.length() != 18) {
	// return false;
	// }
	// String regex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
	// Pattern p = Pattern.compile(regex);
	// Matcher m = p.matcher(idCard);
	// return m.matches();
	// }

	private static void setWiBuffer() {
		for (int i = 0; i < Wi.length; i++) {
			int k = (int) Math.pow(2, (Wi.length - i));
			Wi[i] = (byte) (k % fMod);
		}
	}

	// 判断身份证号码的合法性
	public static boolean checkIDCard(final String idCard) {
		// 初始化方法
		 LogonUtils.setWiBuffer();
		boolean isNew = false;
		// String message = "";
		if (!checkLength(idCard, isNew)) {
			// message = "ID长度异常";
			return false;
		}
		if (idCard.length() == newIDLen) {
			isNew = true;
		} else {
			isNew = false;
		}
		String idDate = getIDDate(idCard, isNew);
		if (!checkDate(idDate)) {
			// message = "ID时间异常";
			return false;
		}
//		if (isNew) {
//			String checkFlag = getCheckFlag(idCard);
//			String theFlag = idCard.substring(idCard.length() - 1,
//					idCard.length());
//			if (!checkFlag.equals(theFlag)) {
//				// message = "新身份证校验位异常";
//				return false;
//			}
//		}
		return true;
	}

	// 获取新身份证的最后一位:检验位
	private static String getCheckFlag(String idCard) {
		int sum = 0;
		// 进行加权求和
		for (int i = 0; i < 17; i++) {
			sum += Integer.parseInt(idCard.substring(i, i + 1)) * Wi[i];
		}
		// 取模运算，得到模值
		byte iCode = (byte) (sum % fMod);
		return CheckCode.substring(iCode, iCode + 1);
	}

	// 判断串长度的合法性
	private static boolean checkLength(final String idCard, boolean newIDFlag) {
		boolean right = (idCard.length() == oldIDLen)
				|| (idCard.length() == newIDLen);

		return right;
	}

	// 获取时间串
	private static String getIDDate(final String idCard, boolean newIDFlag) {
		String dateStr = "";
		if (newIDFlag)
			dateStr = idCard.substring(fPart, fPart + 8);
		else
			dateStr = yearFlag + idCard.substring(fPart, fPart + 6);
		return dateStr;
	}

	// 判断时间合法性
	private static boolean checkDate(final String dateSource) {
		String dateStr = dateSource.substring(0, 4) + "-"
				+ dateSource.substring(4, 6) + "-" + dateSource.substring(6, 8);
		DateFormat df = DateFormat.getDateInstance();
		df.setLenient(false);
		try {
			Date date = df.parse(dateStr);
			return (date != null);
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isMobileNO2(String mobiles) {
		String regex = "^(((86|\\+86|0|)1[34587][0-9]{9})|(\\d{3,4}-(\\d{7,8})))$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	
	public static boolean isMobileNO(String mobiles) {
		String regex = "^(((86|\\+86|0|)1[0-9][0-9]{9})|(\\d{3,4}-(\\d{7,8})))$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	public static boolean matcherCheckCode(String checkCode) {
		if (checkCode.length() < 4 || checkCode.length() > 10) {
			return false;
		}
		int num = 0;
		num = Pattern.compile("\\d").matcher(checkCode).find() ? num + 1 : num;
		num = Pattern.compile("[a-zA-Z]").matcher(checkCode).find() ? num + 1
				: num;
		if (num >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
