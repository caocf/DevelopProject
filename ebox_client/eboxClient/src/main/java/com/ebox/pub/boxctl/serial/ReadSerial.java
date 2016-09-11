package com.ebox.pub.boxctl.serial;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.ebox.pub.service.global.GlobalField;


public class ReadSerial {
	private final int wait = 100;
	private int waitCnt = 0;
	private int maxCnt = 10;
	private static final String TAG = "ReadSerial";
	
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
	
	public ReadSerial()
	{
		
	}
	
	public ReadSerial(int maxCnt)
	{
		this.maxCnt = maxCnt;
	}
	
	public Object read(InputStream inputStream)
	{
		byte[] buffer = new byte[64];
		int readLen = 0;
		int byteOffset = 0;
		boolean lenRead = false;
		int bodyLen = 0;
		int byteRead = 0;
		
		Object rsp = null;
		synchronized (this)
		{
			try {
				if (inputStream != null) 
				{
					//waitMs();
					
					// 读取到长度字段为止
					while(!lenRead)
					{
						readLen = inputStream.read(buffer, byteOffset, 5-byteOffset);
						
						byteOffset += readLen;
						if(byteOffset < 5)
						{
							if(!waitMs())
							{
								return rsp;
							}
							continue;
						}
						lenRead = true;
						bodyLen = (int)buffer[4];
						
						// 异常保护
						if(bodyLen > 50 || bodyLen < 0)
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
					Log.i(TAG, "OpCnt["+GlobalField.portOpCnt+"] Read:" + LogicSerialTask.bytesToHexString(rspBuffer));
					
					rsp = new PduResponseDecoder().decode(rspBuffer);
					GlobalField.portOpCnt++;
				}
			} catch (IOException e) {
				Log.e(TAG, "ReadSerial: IOException:" + e);
				e.printStackTrace();
			} 
		}
		return rsp;
	}
}
