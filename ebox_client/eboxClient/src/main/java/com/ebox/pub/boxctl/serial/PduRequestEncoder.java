package com.ebox.pub.boxctl.serial;

public class PduRequestEncoder {
	public byte[] encode(int id, Object req)
	{
		int pduLen = 0;
		int bodyLen = 0;
		byte[] buffer = null;
		byte[] head = new byte[]{(byte) 0xAA, (byte)0x55};
		
		switch(id)
		{
		case SerialOpIds.GetSingleBoardInfo:
		{
			GetSingleBoardInfoReq reqInfo = (GetSingleBoardInfoReq)req;

			// msgType+body+check
			bodyLen = 1+0+1;
			// head+destAddr+srcAddr+len+消息体长度
			pduLen = head.length+1+1+1+bodyLen;
			buffer =  new byte[pduLen];
			System.arraycopy(head, 0, buffer, 0, head.length);
			buffer[2] = (byte)reqInfo.getBoardId();
			buffer[3] = (byte)0;
			buffer[4] = (byte)bodyLen;
			buffer[5] = (byte)id;
			buffer[6] = (byte)0;
			break;
		}
		case SerialOpIds.GetDoorsStatus:
		{
			GetDoorsStatusReq reqInfo = (GetDoorsStatusReq)req;

			// msgType+body+check
			bodyLen = 1+0+1;
			// head+destAddr+srcAddr+len+消息体长度
			pduLen = head.length+1+1+1+bodyLen;
			buffer =  new byte[pduLen];
			System.arraycopy(head, 0, buffer, 0, head.length);
			buffer[2] = (byte)reqInfo.getBoardId();
			buffer[3] = (byte)0;
			buffer[4] = (byte)bodyLen;
			buffer[5] = (byte)id;
			buffer[6] = (byte)0;
			break;
		}
		case SerialOpIds.OpenDoor:
		{
			OpenDoorReq reqInfo = (OpenDoorReq)req;

			// msgType+body+check
			bodyLen = 1+1+1;
			// head+destAddr+srcAddr+len+消息体长度
			pduLen = head.length+1+1+1+bodyLen;
			buffer =  new byte[pduLen];
			System.arraycopy(head, 0, buffer, 0, head.length);
			buffer[2] = (byte)reqInfo.getBoardId();
			buffer[3] = (byte)0;
			buffer[4] = (byte)bodyLen;
			buffer[5] = (byte)id;
			buffer[6] = (byte)reqInfo.getBoxId();
			buffer[7] = (byte)0;
			break;
		}
		}
        
		return buffer;
	}
}
