package com.ebox.pub.boxctl.serial;

import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.ex.network.model.enums.MainBoardType;
import com.ebox.pub.service.global.GlobalField;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WriteSerial {
	private int rst = RstCode.Failed;
	private static final String TAG = "WriteSerial";
	
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
			if(GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1) {
				AppApplication.getInstance().getSb().getSerialPort().setGpio_A31(1);
			} else {
				AppApplication.getInstance().getSb().getSerialPort().setGpio(1);
			}
			
			Thread.sleep(20);
			outputStream.write(mBuffer);
			outputStream.flush();
			rst = RstCode.Success;
			Log.i(TAG, "OpCnt["+GlobalField.portOpCnt+"] Write:" + LogicSerialTask.bytesToHexString(mBuffer));
			
			Thread.sleep(40);
			
			if(GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1) {
				AppApplication.getInstance().getSb().getSerialPort().setGpio_A31(0);
			} else {
				AppApplication.getInstance().getSb().getSerialPort().setGpio(0);
			}
			Thread.sleep(20);
			GlobalField.portOpCnt++;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rst;
	}
}
