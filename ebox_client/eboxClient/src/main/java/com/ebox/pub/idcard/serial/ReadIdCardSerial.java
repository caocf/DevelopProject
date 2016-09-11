package com.ebox.pub.idcard.serial;

import android.util.Log;

import com.ebox.pub.utils.FunctionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class ReadIdCardSerial {
	private final int wait = 100;
	private int waitCnt = 0;
	private int maxCnt = 10;
	private static final String TAG = "ReadIdCardSerial";
	
	private boolean waitMs()
	{
		try {
			this.wait(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waitCnt++;
		
		if(waitCnt >= maxCnt)
		{
			Log.e(TAG, "ReadSerial: waitCnt:" + waitCnt);
			return false;
		}
		
		return true;
	}
	
	public ReadIdCardSerial()
	{
		
	}
	
	public ReadIdCardSerial(int maxCnt)
	{
		this.maxCnt = maxCnt;
	}
	
	public byte[] read(InputStream inputStream)
	{
		byte[] buffer = new byte[2048];
		int readLen = 0;
		int byteOffset = 0;
		boolean lenRead = false;
		int bodyLen = 0;
		int byteRead = 0;
		
		byte[] rsp = null;
		synchronized (this)
		{
			try {
				if (inputStream != null) 
				{
					//waitMs();
					
					// 读取到长度字段为止
					while(!lenRead)
					{
						readLen = inputStream.read(buffer, byteOffset, 7-byteOffset);
						
						byteOffset += readLen;
						if(byteOffset < 7)
						{
							if(!waitMs())
							{
								return rsp;
							}
							continue;
						}
						lenRead = true;
						bodyLen = FunctionUtil.byteToInt(Arrays.copyOfRange(buffer, 5, 7));
						// 异常保护
						if(bodyLen > 1288 || bodyLen < 0)
						{
							Log.e(TAG, "ReadSerial: bodyLen:" + bodyLen);
							return rsp;
						}
						break;
					}
					
					// 读取消息体
					while(true)
					{
						readLen = inputStream.read(buffer, byteOffset, bodyLen-byteRead);
						byteRead += readLen;
						byteOffset += readLen;
						if(byteRead < bodyLen)
						{
							if(!waitMs())
							{
								return rsp;
							}
							continue;
						}
						break;
					}
					
					byte[] rspBuffer = new byte[byteOffset];
					System.arraycopy(buffer, 0, rspBuffer, 0, byteOffset);
					rsp = rspBuffer;
				}
			} catch (IOException e) {
				Log.e(TAG, "ReadSerial: IOException:" + e);
				e.printStackTrace();
			} 
		}
		return rsp;
	}
}
