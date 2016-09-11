package com.ebox.pub.idcard.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class WriteIdcardSerial {
	private int rst = RstCode.Failed;
	
	public int write(OutputStream outputStream, byte[] mBuffer, InputStream inputStream)
	{
		try {
			// 清空读缓存
			byte[] buffer = new byte[64];
			int readLen = 0;
			while(true)
			{
				readLen = inputStream.read(buffer);
				if(readLen == 0 || readLen == -1)
				{
					break;
				}
			}
			outputStream.write(mBuffer);
			outputStream.flush();
			rst = RstCode.Success;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return rst;
	}
}
