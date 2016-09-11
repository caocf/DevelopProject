package com.ebox.pub.boxctl.serial;

public class PduResponseDecoder {
	public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 0) & 0x1) + (byte) ((b >> 1) & 0x1)
                + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 3) & 0x1)
                + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 5) & 0x1)
                + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 7) & 0x1);
    }
	
	public Object decode(byte[] buffer)
	{
		Object rsp = null;
		
		
		// 消息头不对
		if(buffer[0] != (byte)0xAA || buffer[1] != (byte)0x55)
		{
			return rsp;
		}
		
		switch(buffer[5])
		{
		case SerialOpIds.GetSingleBoardInfoRsp:
		{
			GetSingleBoardInfoRsp rspInfo = new GetSingleBoardInfoRsp();
			
			rspInfo.setRst(buffer[6]);
			rspInfo.setBoxCount(buffer[7]);
			rsp = rspInfo;
			break;
		}
		case SerialOpIds.GetDoorsStatusRsp:
		{
			GetDoorsStatusRsp rspInfo = new GetDoorsStatusRsp();
			
			rspInfo.setRst(buffer[6]);
			
			String doorStatus="";
            doorStatus+=byteToBit(buffer[7]);
            doorStatus+=byteToBit(buffer[8]);
            doorStatus+=byteToBit(buffer[9]);
            doorStatus+=byteToBit(buffer[10]);
            
            int[] boxState = new int[32];
            for(int i = 0; i < doorStatus.length(); i++)
            {
            	boxState[i] = Integer.valueOf(doorStatus.substring(i, i+1));
            }
			rspInfo.setBoxState(boxState);
			
			rsp = rspInfo;
			break;
		}
		case SerialOpIds.OpenDoorRsp:
		{
			OpenDoorRsp rspInfo = new OpenDoorRsp();
			
			rspInfo.setRst(buffer[6]);
			rsp = rspInfo;
			break;
		}
		}
		
		return rsp;
	}
}
