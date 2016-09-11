package com.ebox.mgt.ui.utils;

import android.os.Environment;

import java.io.File;

public class UmscfgTools {
	/**
	 * 获取SD卡的目录路径
	 * 
	 * @return
	 */
	public static String getSdcardDir() {
		try {
			// 判断是否存在SD卡
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File file = Environment.getExternalStorageDirectory();
				return file.getCanonicalPath();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
