package com.ebox.pub.boxctl.serial;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.util.Log;

import com.ebox.pub.service.global.GlobalField;
import com.jni.serialport.SerialPort;

public class LogicSerialTask{
	private static final String TAG = "LogicSerialTask";
	private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    private boolean portOk = false;
	// DEBUG模式
    private static boolean DEBUG = true;
	
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
	
	public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
            	hv += "0";  
            }  
            stringBuilder.append(" 0x"+hv);  
        }  
        return stringBuilder.toString();  
    }  
	
	public GetSingleBoardInfoRsp GetSingleBoardInfo(GetSingleBoardInfoReq req) {
		int id = SerialOpIds.GetSingleBoardInfo;
		Object readRsp = null;
		
		GetSingleBoardInfoRsp rsp = new GetSingleBoardInfoRsp();
		rsp.setRst(RstCode.Failed);
		
		try{
			if(DEBUG && id == 0)
	    	{
				rsp.setRst(RstCode.Success);
				rsp.setBoxCount(18);
				return rsp;
	    	}
	    	
	    	if(!portValid())
	    	{
				return rsp;
	    	}
	    	
	    	byte[] cmd = new PduRequestEncoder().encode(id, req);
	        Log.i(TAG, "GetSingleBoardInfo:" + bytesToHexString(cmd));
	        
	        if(new WriteSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	return rsp;
	        }
	        
	        readRsp = new ReadSerial().read(getInputStream());
	        
	        if(readRsp != null && readRsp instanceof GetSingleBoardInfoRsp)
	        {
	        	rsp = (GetSingleBoardInfoRsp)readRsp;
	        	Log.i(TAG, "GetSingleBoardInfoRsp: Rst[" +rsp.getRst()+"] BoxCount["+rsp.getBoxCount()+"]");
	        	/*String info = String.format("%02d",req.getBoardId())+AppApplication.getInstance().getResources().getString(R.string.rack)
	        				+"["+rsp.getBoxCount()+"]门，初始化成功！";
	        	Toast.makeText(AppApplication.globalContext, info, Toast.LENGTH_LONG).show();*/
	        }
		}
		finally{
			if(rsp.getRst() != RstCode.Success)
			{
				GlobalField.portFailCnt++;
				GlobalField.portFailAllCnt++;
				Log.i(TAG, "GetSingleBoardInfo 485 failed :FailCnt["+GlobalField.portFailCnt
						+"] FailAllCnt["+GlobalField.portFailAllCnt+"]");
			}
		}
        
        return rsp;
	}
	
	public GetDoorsStatusRsp GetDoorsStatus(GetDoorsStatusReq req)
	{
		int id = SerialOpIds.GetDoorsStatus;
		Object readRsp = null;
		
		GetDoorsStatusRsp rsp = new GetDoorsStatusRsp();
		rsp.setRst(RstCode.Failed);
		
		try{
			if(DEBUG && id == 0)
	    	{
				rsp.setRst(RstCode.Success);
				rsp.setBoxState(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
				return rsp;
	    	}
	    	
	    	if(!portValid())
	    	{
				return rsp;
	    	}
	    	
	    	byte[] cmd = new PduRequestEncoder().encode(id, req);
	        Log.i(TAG, "GetDoorsStatus:" + bytesToHexString(cmd));
	        
	        if(new WriteSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	return rsp;
	        }
	        
	        readRsp = new ReadSerial().read(getInputStream());
	        
	        if(readRsp != null && readRsp instanceof GetDoorsStatusRsp)
	        {
	        	rsp = (GetDoorsStatusRsp)readRsp;
	        	Log.i(TAG, "GetDoorsStatusRsp: Rst[" +rsp.getRst()+"] BoxState["+Arrays.toString(rsp.getBoxState())+"]");
	        }
		}
		finally{
			if(rsp.getRst() != RstCode.Success)
			{
				GlobalField.portFailCnt++;
				GlobalField.portFailAllCnt++;
				Log.i(TAG, "GetDoorsStatus 485 failed :FailCnt["+GlobalField.portFailCnt
						+"] FailAllCnt["+GlobalField.portFailAllCnt+"]");
			}
		}
        
        return rsp;
	}
	
	public OpenDoorRsp OpenDoor(OpenDoorReq req)
	{
		int id = SerialOpIds.OpenDoor;
		Object readRsp = null;
		
		OpenDoorRsp rsp = new OpenDoorRsp();
		rsp.setRst(RstCode.Failed);
		
		try{
			if(DEBUG && id == 0)
	    	{
				rsp.setRst(RstCode.Success);
				return rsp;
	    	}
	    	
	    	if(!portValid())
	    	{
				return rsp;
	    	}
	    	
	    	byte[] cmd = new PduRequestEncoder().encode(id, req);
	        Log.i(TAG, "OpenDoor:" + bytesToHexString(cmd));
	        
	        if(new WriteSerial().write(getOutputStream(), cmd, getInputStream()) == RstCode.Failed)
	        {
	        	Log.i(TAG, "OpenDoorRsp:failed");
	        	return rsp;
	        }
	        
	        readRsp = new ReadSerial(25).read(getInputStream());
	        
	        if(readRsp != null && readRsp instanceof OpenDoorRsp)
	        {
	        	rsp = (OpenDoorRsp)readRsp;
	        	Log.i(TAG, "OpenDoorRsp: Rst[" +rsp.getRst()+"]");
	        }
	        else
	        {
	        	Log.i(TAG, "OpenDoorRsp:no rsp");
	        }
		}
	   finally
	   {
			if(rsp.getRst() == RstCode.Failed)
			{
				GlobalField.portFailCnt++;
				GlobalField.portFailAllCnt++;
				Log.i(TAG, "OpenDoor 485 failed :FailCnt["+GlobalField.portFailCnt
						+"] FailAllCnt["+GlobalField.portFailAllCnt+"]");
			}
		}
        
        return rsp;
	}
}
