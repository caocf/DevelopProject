package com.ebox.pub.service;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SpaceCtrl {
	// 剩余磁盘空间 单位M
	public static long getAvailaleSize(){
		File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath()); 

		long blockSize = stat.getBlockSize(); 
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize)/1024 /1024; 
	}
	
	// 磁盘空间 单位M
	public static long getAllSize(){
		File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath()); 

		long blockSize = stat.getBlockSize(); 
		long availableBlocks = stat.getBlockCount();
		return (availableBlocks * blockSize)/1024 /1024; 
	}
	
	// 磁盘空间 单位M
	public static long getFreeSize(){
		File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath()); 

		long blockSize = stat.getBlockSize(); 
		long availableBlocks = stat.getFreeBlocks();
		return (availableBlocks * blockSize)/1024 /1024; 
	}
}
