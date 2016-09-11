package com.ebox.pub.idcard.serial;

import com.ebox.pub.model.IdcardModel;
import com.ebox.pub.utils.FunctionUtil;
import com.jni.serialport.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;



public class LogicIdCardSerialTask{
	private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    private boolean portOk = false;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}
	

	public boolean isPortOk() {
		return portOk;
	}

	public void setPortOk(boolean portOk) {
		this.portOk = portOk;
	}

	public boolean portValid()
	{
		if(portOk && getOutputStream() != null && getInputStream() != null)
		{
			return true;
		}
		
		return false;
	}
	
	/*
	 * 寻找卡片
	 * true 找到卡片
	 * false 没有找到
	 */
	
	public boolean FindIdCard() {
		boolean rst = false;
		
		try{
	    	
	    	if(!portValid())
	    	{
				return rst;
	    	}
	    	
	    	byte[] cmd = new byte[]{(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20,  0x01, 0x22,};
	        
	        if(new WriteIdcardSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	return rst;
	        }
	        
	        byte[] readRsp = new ReadIdCardSerial().read(getInputStream());
	        
	        
	        if(readRsp != null )
	        {
	        	if(readRsp[0] != (byte)0xAA || readRsp[1] != (byte)0xAA ||
	        			readRsp[2] != (byte)0xAA || readRsp[3] != (byte)0x96 ||
	        			readRsp[4] != (byte)0x69 
	        			)
	    		{
	    			return rst;
	    		}
	        	if(readRsp[7] == 0x00 && readRsp[8]== 0x00 && readRsp[9]== (byte)0x9F){
	        		return true;
	        	} 
	        }
		}
		finally{
		}
        
        return rst;
	}
	
	/*
	 * 选取卡片
	 * true 选取成功
	 * false 选取失败
	 */
	
	public boolean SelectIdCard() {
		boolean rst = false;
		
		try{
	    	
	    	if(!portValid())
	    	{
				return rst;
	    	}
	    	
	    	byte[] cmd = new byte[]{(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20,  0x02,  0x21,};
	        
	        if(new WriteIdcardSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	return rst;
	        }
	        
	        byte[] readRsp = new ReadIdCardSerial().read(getInputStream());
	        
	        if(readRsp != null )
	        {
	        	if(readRsp[0] != (byte)0xAA || readRsp[1] != (byte)0xAA ||
	        			readRsp[2] != (byte)0xAA || readRsp[3] != (byte)0x96 ||
	        			readRsp[4] != (byte)0x69 
	        			)
	    		{
	    			return rst;
	    		}
	        	if(readRsp[7] == 0x00 && readRsp[8]== 0x00 && readRsp[9]== (byte)0x90){
	        		return true;
	        	}
	        }
		}
		finally{
		}
        
        return rst;
	}
	

	/*
	 * 读取卡片
	 * @return IdcardModel
	 */
	
	public IdcardModel ReadIdCard() {
		 IdcardModel rst = null;
		
		try{
	    	
	    	if(!portValid())
	    	{
				return rst;
	    	}
	    	
	    	byte[] cmd = new byte[]{(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x01, 0x32};
	        
	        if(new WriteIdcardSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	return rst;
	        }
	        
	        byte[] readRsp = new ReadIdCardSerial().read(getInputStream());
	        
	        if(readRsp != null )
	        {
	        	if(readRsp[0] != (byte)0xAA || readRsp[1] != (byte)0xAA ||
	        			readRsp[2] != (byte)0xAA || readRsp[3] != (byte)0x96 ||
	        			readRsp[4] != (byte)0x69 
	        			)
	    		{
	    			return rst;
	    		}
	        	if(readRsp[7] == 0x00 && readRsp[8]== 0x00 && readRsp[9]== (byte)0x90){
	        		rst = new IdcardModel();
	        		rst.setName(FunctionUtil.hexByes2String(
                            FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 14, 44)), "").trim());
	        		rst.setAddress(FunctionUtil.hexByes2String(
	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 66, 136)), "").trim());
	        		rst.setSex(FunctionUtil.getSexString(FunctionUtil.hexByes2String(
	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 44, 46)), "")).trim());
	        		rst.setNation(FunctionUtil.getNationalString(FunctionUtil.hexByes2String(
	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 46, 50)), "")).trim());
//	        		rst.setBirthday(FunctionUtil.hexByes2String(
//	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 50, 66)), "").trim());
	        		rst.setIdcard(FunctionUtil.hexByes2String(
	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 136, 172)), "").trim());
//	        		rst.setProvideoffice(FunctionUtil.hexByes2String(
//	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 172, 202)), "").trim());
	        		//有效期起始时间
//	        		rst.setBirthday(FunctionUtil.hexByes2String(
//	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 202, 218)), ""));
//	        		rst.setEnddate(FunctionUtil.hexByes2String(
//	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 218, 234)), "").trim());
	        		rst.setValid_date(FunctionUtil.hexByes2String(
	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 202, 218)), "") +"-"
	        				+ FunctionUtil.hexByes2String(
    	        				FunctionUtil.hexBytesH2L(Arrays.copyOfRange(readRsp, 218, 234)), "").trim());
	        		// 身份证照片解码
	        	}
	        }
		}
		finally{
		}
        
        return rst;
	}
	
	
	/*private int ReadCard(byte[] readRsp)
    {    	
		int Readflage = -1;
		//照片解码									
		try
		{	
			int ret = IDCReaderSDK.Init();
			if (ret == 0)
			{	
				byte[] datawlt = new byte[1384];
				byte[] byLicData = {(byte)0x05,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x5B,(byte)0x03,(byte)0x33,(byte)0x01,(byte)0x5A,(byte)0xB3,(byte)0x1E,(byte)0x00};
				for(int i = 0; i < 1295; i++)
				{
				 	datawlt[i] = readRsp[i];
				}
				int t = IDCReaderSDK.unpack(datawlt,byLicData);
				if(t == 1)
				{
					Readflage = 1;//读卡成功
				}
				else
				{
					Readflage = -1;//照片解码异常
				}											
			}
			else
			{
				Readflage = -1;//照片解码异常
			}										
		}
		catch(Exception e)
		{								
			Readflage = -1;//照片解码异常
		}	
		
		return Readflage;
    }*/
	
}
