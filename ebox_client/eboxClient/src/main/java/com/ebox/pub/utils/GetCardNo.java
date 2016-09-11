package com.ebox.pub.utils;

import com.jni.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GetCardNo {
	private static SerialPort mSerialPort;
	private static InputStream mFileInputStream;
	private static boolean cancel = false;

	private static boolean openSerial()
	{
		final int O_NDELAY = 04000;
			
		mSerialPort = new SerialPort();
		try {
			mSerialPort.OpenPort(new File("/dev/ttyS4"),9600,O_NDELAY);
			mFileInputStream  = mSerialPort.getInputStream();
			return true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static void closeSerial()
	{
		if(mSerialPort != null)
		{
			mSerialPort.close();
			mSerialPort = null;
		}
		mFileInputStream = null;
	}
	
	public static void cancelRead()
	{
		cancel = true;
	}
	
	public static String getCardNo()
	{
		StringBuilder cardNo = new StringBuilder();
		cancel = false;

		if(!openSerial())
		{
			return "";
		}
		
		try {
			byte[] buffer = new byte[12];
			int offset = 0;
			int count = 0;
			
			while(true)
			{
				if(cancel)
					break;
				
				int readLen = mFileInputStream.read(buffer, offset, 12-offset);
				offset = offset + readLen;
				
				// 读取成功
				if(offset >= 12)
				{
					for(int i = 0; i < readLen; i ++)
					{
                        LogUtil.d("mf",bytesToHexString(buffer[i]));
						cardNo.append(bytesToHexString(buffer[i]));
					}
					return cardNo.toString();
				}
				else if(offset > 0)
				{
					// 读取超时，清数据，等待下一次读卡
					count++;
					if(count >= 100)
					{
						offset = 0;
						count = 0;
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally
		{
			closeSerial();
		}
		
		return "";
	}


    public static String bytesToHexString(byte src){
        StringBuilder stringBuilder = new StringBuilder("");
            int v = src & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        return stringBuilder.toString();
    }



}
