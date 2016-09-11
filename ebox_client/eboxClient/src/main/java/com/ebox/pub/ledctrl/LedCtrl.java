package com.ebox.pub.ledctrl;

import android.util.Log;

import com.ebox.ex.network.model.base.type.LedInfo;
import com.ebox.ex.network.model.req.ReqLedRefresh;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.LogUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class LedCtrl {
	private final String IP = GlobalField.config.getLed_ip();
	private final int PORT = GlobalField.config.getLed_port();
	private Socket client = null; 
	private DataOutputStream dout;  
	private DataInputStream din; 
	
	private final byte[] HEAD = new byte[]{0x55,(byte)0xAA,0x00,0x00};
	private final byte[] DOWNLOAD_CMD = new byte[]{0x00,(byte)0xDA};
	private final byte[] OPEN_CLOSE_CMD = new byte[]{(byte)0x00,(byte)0xC6};
	private final byte[] CHECK = new byte[]{0x00,0x00};
	private final byte[] TAIL = new byte[]{0x0d,0x0a};
	private short seq = 0;
	
	public boolean init()
	{
		try {  
            client = new Socket(IP, PORT);  
            dout = new DataOutputStream(client.getOutputStream());  
            din = new DataInputStream(client.getInputStream());  
        } catch (UnknownHostException e) {  
          //  e.printStackTrace();
            return false;
        } catch (IOException e) {  
          //  e.printStackTrace();
            return false;
        }  
		catch (Exception e) {  
          //  e.printStackTrace();
            return false;
        }  
		
		return true;
	}
	
	
	public void close()
	{
		if(client != null)
		{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			client = null;
		}
	}
	
	// op:0:开机 1:关机
	public boolean openCloseLed(int op)
	{
		PduReq req = new PduReq();
		int pduLen = 0;
		
		req.setHead(HEAD);
		req.setDst((byte)1);// 屏号
		req.setSrc((byte)0);
		req.setCmd(OPEN_CLOSE_CMD);
		req.setSeq(DataTypeChangeHelper.shortToByteArray((short)seq++));
		req.setFrameCnt((byte)1);
		req.setFrameNo((byte)1);
		req.setCheck(CHECK);
		req.setTail(TAIL);
		
		// 数据区
		byte[] data = new byte[]{(byte)op,0x00,0x00};
		LogUtil.i("led","openCloseLed data ["+DataTypeChangeHelper.byteToHex(data)+"]");
		req.setData(data);
		req.setDataLen(DataTypeChangeHelper.intToByteArray(data.length));
		
		// 数据总长度,所有帧长度的总和
		pduLen = data.length;
		req.setLen(DataTypeChangeHelper.intToByteArray(pduLen));
		
		if(!sendReq(encodePduReq(req)))
			return false;
		
		byte[] rsp = getRsp();
		LogUtil.i("led","openCloseLed rsp ["+DataTypeChangeHelper.byteToHex(rsp)+"]");
		PduRsp pduRsp = null;
		if(rsp != null)
		{
			pduRsp = decodePduRsp(rsp);
			
			if(!Arrays.equals(pduRsp.getHead(),req.getHead()))
			{
				LogUtil.i("led","openCloseLed [ Different Head ]");
				return false;
			}
			
			if(!Arrays.equals(pduRsp.getTail(),req.getTail()))
			{
				LogUtil.i("led","openCloseLed [ Different Tail ]");
				return false;
			}
			
			if(!Arrays.equals(pduRsp.getCheck(),req.getCheck()))
			{
				LogUtil.i("led","openCloseLed [ Different Check ]");
				return false;
			}
			
			return true;
		}
		LogUtil.i("led","openCloseLed [ getRsp() Return Null ]");
		return false;
	}
	
	public boolean downloadText(ReqLedRefresh refreshReq)
	{
		PduReq req = new PduReq();
		int pduLen = 0;
		
		req.setHead(HEAD);
		req.setDst((byte)1);// 屏号
		req.setSrc((byte)0);
		req.setCmd(DOWNLOAD_CMD);
		req.setSeq(DataTypeChangeHelper.shortToByteArray((short)seq++));
		req.setFrameCnt((byte)1);
		req.setFrameNo((byte)1);
		req.setCheck(CHECK);
		req.setTail(TAIL);
		
		// 文字区
		byte[] textArea = encodeTextArea(refreshReq);
		LogUtil.i("led","downloadText textArea["+DataTypeChangeHelper.byteToHex(textArea)+"]");
		
		// 节目
		byte[] program = encodeProgram(textArea, refreshReq.getContent().size());
		LogUtil.i("led","downloadText program["+DataTypeChangeHelper.byteToHex(program)+"]");
		
		// 暂时固定一个节目
		byte[] downloadReq = encodeDownloadReq(program);
		LogUtil.i("led","downloadText downloadReq["+DataTypeChangeHelper.byteToHex(downloadReq)+"]");
		
		req.setData(downloadReq);
		req.setDataLen(DataTypeChangeHelper.intToByteArray(downloadReq.length));
		
		// 数据总长度,所有帧长度的总和
		pduLen = downloadReq.length;
		req.setLen(DataTypeChangeHelper.intToByteArray(pduLen));
		
		if(!sendReq(encodePduReq(req)))
			return false;
		//获取返回字节数组
		byte[] rsp = getRsp();
		LogUtil.i("led","downloadText getRsp["+DataTypeChangeHelper.byteToHex(rsp)+"]");
		
		PduRsp pduRsp = null;
		if(rsp != null)
		{
			pduRsp = decodePduRsp(rsp);
			
			if(!Arrays.equals(pduRsp.getHead(),req.getHead()))
			{
				LogUtil.i("led","different Head [ rsp="+DataTypeChangeHelper.byteToHex(pduRsp.getHead())+",req="+DataTypeChangeHelper.byteToHex(req.getHead())+"]");
				return false;
			}
			
			if(!Arrays.equals(pduRsp.getTail(),req.getTail()))
			{
				LogUtil.i("led","different Tail [ rsp="+DataTypeChangeHelper.byteToHex(pduRsp.getTail())+",req="+DataTypeChangeHelper.byteToHex(req.getTail())+"]");
				return false;
			}
			
			if(!Arrays.equals(pduRsp.getCheck(),req.getCheck()))
			{
				LogUtil.i("led","different Check [ rsp="+DataTypeChangeHelper.byteToHex(pduRsp.getCheck())+",req="+DataTypeChangeHelper.byteToHex(req.getCheck())+"]");
				return false;
			}
			LogUtil.i("led","getRsp() Return Null");
			return true;
		}
		
		return false;
	}
	
	private byte[] encodeTextArea(ReqLedRefresh refreshReq)
	{
		ArrayList<Byte> area = new ArrayList<Byte>();
		for(int i = 0; i < refreshReq.getContent().size(); i++)
		{
			LedInfo led = refreshReq.getContent().get(i);
			TextArea textArea = new TextArea();
			int dataLen = 0;
			int areaLen = 0;
			textArea.setNo((byte)1);
			textArea.setType((byte)0x0E);
			textArea.setStartx(DataTypeChangeHelper.shortToByteArray(led.getStartx().shortValue()));
			textArea.setStarty(DataTypeChangeHelper.shortToByteArray(led.getStarty().shortValue()));
			textArea.setEndx(DataTypeChangeHelper.shortToByteArray(led.getEndx().shortValue()));
			textArea.setEndy(DataTypeChangeHelper.shortToByteArray(led.getEndy().shortValue()));
			textArea.setColor((byte)1);
			textArea.setName(DataTypeChangeHelper.shortToByteArray((short)0));
			textArea.setSkill(new byte[]{led.getIn().byteValue(),
					led.getOut().byteValue(),
					led.getSpeed().byteValue(),
					led.getShowTime().byteValue()});
			textArea.setSize((byte)0x10);
			try {
				textArea.setData(led.getText().getBytes("gbk"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			dataLen= textArea.getData().length;
			textArea.setCnt(DataTypeChangeHelper.intToByteArray(dataLen));
			
			areaLen = dataLen +26;
			textArea.setLen(DataTypeChangeHelper.intToByteArray(areaLen));
			
			byte[] buffer = new byte[areaLen];
			int offset = 0;
			
			buffer[offset] = textArea.getNo();
			offset += 1;
			
			System.arraycopy(textArea.getLen(), 0, buffer, offset, textArea.getLen().length);
			offset += textArea.getLen().length;
			
			buffer[offset] = textArea.getType();
			offset += 1;
			
			System.arraycopy(textArea.getStartx(), 0, buffer, offset, textArea.getStartx().length);
			offset += textArea.getStartx().length;
			
			System.arraycopy(textArea.getStarty(), 0, buffer, offset, textArea.getStarty().length);
			offset += textArea.getStarty().length;
			
			System.arraycopy(textArea.getEndx(), 0, buffer, offset, textArea.getEndx().length);
			offset += textArea.getEndx().length;
			
			System.arraycopy(textArea.getEndy(), 0, buffer, offset, textArea.getEndy().length);
			offset += textArea.getEndy().length;
			
			buffer[offset] = textArea.getColor();
			offset += 1;

			System.arraycopy(textArea.getName(), 0, buffer, offset, textArea.getName().length);
			offset += textArea.getName().length;
			
			System.arraycopy(textArea.getSkill(), 0, buffer, offset, textArea.getSkill().length);
			offset += textArea.getSkill().length;
			
			buffer[offset] = textArea.getSize();
			offset += 1;
			
			System.arraycopy(textArea.getCnt(), 0, buffer, offset, textArea.getCnt().length);
			offset += textArea.getCnt().length;
			
			System.arraycopy(textArea.getData(), 0, buffer, offset, textArea.getData().length);
			offset += textArea.getData().length;
			
			for(int j = 0; j < buffer.length; j++)
			{
				area.add(buffer[j]);
			}
		}
		byte[] areaStr = new byte[area.size()];
		for(int j = 0; j < area.size(); j++)
		{
			areaStr[j] = area.get(j);
		}
		
		return areaStr;
	}
	
	private byte[] getTimeBytes(byte year, byte week, byte month, byte day,
			byte hour, byte minute, byte second)
	{
		byte[] time = new byte[7];
		
		time[0] = year;
		time[1] = week;
		time[2] = month;
		time[3] = day;
		time[4] = hour;
		time[5] = minute;
		time[6] = second;
		
		return time;
	}
	
	private byte[] encodeProgram(byte[] textArea, int areaCnt)
	{
		Program program = new Program();
		int programLen = 0;
		program.setNo((byte)1);
		programLen = textArea.length+24;
		program.setLen(DataTypeChangeHelper.intToByteArray(programLen));
		program.setCnt((byte)areaCnt);
		program.setTime(DataTypeChangeHelper.shortToByteArray((short)0));
		program.setTimeMode((byte)0);
		program.setWeekMode((byte)0);
		program.setStart(getTimeBytes((byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0));
		program.setEnd(getTimeBytes((byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0));
		program.setData(textArea);
		
		byte[] buffer = new byte[programLen];
		int offset = 0;
		
		buffer[offset] = program.getNo();
		offset += 1;
		
		System.arraycopy(program.getLen(), 0, buffer, offset, program.getLen().length);
		offset += program.getLen().length;
		
		buffer[offset] = program.getCnt();
		offset += 1;
		
		System.arraycopy(program.getTime(), 0, buffer, offset, program.getTime().length);
		offset += program.getTime().length;
		
		buffer[offset] = program.getTimeMode();
		offset += 1;
		
		buffer[offset] = program.getWeekMode();
		offset += 1;
		
		System.arraycopy(program.getStart(), 0, buffer, offset, program.getStart().length);
		offset += program.getStart().length;
		
		System.arraycopy(program.getEnd(), 0, buffer, offset, program.getEnd().length);
		offset += program.getEnd().length;
		
		System.arraycopy(program.getData(), 0, buffer, offset, program.getData().length);
		offset += program.getData().length;
		
		return buffer;
	}
	
	private byte[] encodeDownloadReq(byte[] program)
	{
		DownloadReq req = new DownloadReq();
		int frameLen= 1+program.length;
		req.setProgramCnt((byte)1);
		req.setProgram(program);
		
		byte[] buffer = new byte[frameLen];
		int offset = 0;
		
		buffer[offset] = req.getProgramCnt();
		offset += 1;
		
		System.arraycopy(req.getProgram(), 0, buffer, offset, req.getProgram().length);
		offset += req.getProgram().length;
		
		return buffer;
	}
	
	private byte[] encodePduReq(PduReq req)
	{
		//int pduLen = DataTypeChangeHelper.unsigned4BytesToInt(req.getLen(), 0)+4+1+1+2+2+4+1+1+4+2+2;
		int pduLen = 536;
		byte[] buffer = new byte[pduLen];
		int offset = 0;
		Arrays.fill(buffer, (byte)0x00);
		
		System.arraycopy(req.getHead(), 0, buffer, offset, req.getHead().length);
		offset += req.getHead().length;
		
		buffer[offset] = req.getDst();
		offset += 1;
		
		buffer[offset] = req.getSrc();
		offset += 1;
		
		System.arraycopy(req.getCmd(), 0, buffer, offset, req.getCmd().length);
		offset += req.getCmd().length;
		
		System.arraycopy(req.getSeq(), 0, buffer, offset, req.getSeq().length);
		offset += req.getSeq().length;
		
		System.arraycopy(req.getLen(), 0, buffer, offset, req.getLen().length);
		offset += req.getLen().length;
		
		buffer[offset] = req.getFrameCnt();
		offset += 1;
		
		buffer[offset] = req.getFrameNo();
		offset += 1;
		
		System.arraycopy(req.getDataLen(), 0, buffer, offset, req.getDataLen().length);
		offset += req.getDataLen().length;
		
		System.arraycopy(req.getData(), 0, buffer, offset, req.getData().length);
		offset += req.getData().length;
		
		System.arraycopy(req.getCheck(), 0, buffer, offset, req.getCheck().length);
		offset += req.getCheck().length;
		
		System.arraycopy(req.getTail(), 0, buffer, offset, req.getTail().length);
		offset += req.getTail().length;
		
		return buffer;
	}
	
	private PduRsp decodePduRsp(byte[] buffer)
	{
		PduRsp rsp = new PduRsp();
		int offset = 0;
		
		rsp.setHead(Arrays.copyOfRange(buffer, 0, 4));
		offset += 4;
		
		rsp.setDst(buffer[offset]);
		offset += 1;
		
		rsp.setSrc(buffer[offset]);
		offset += 1;
		
		rsp.setCmd(Arrays.copyOfRange(buffer, offset, offset+2));
		offset += 2;
		
		rsp.setSeq(Arrays.copyOfRange(buffer, offset, offset+2));
		offset += 2;
		
		rsp.setLen(Arrays.copyOfRange(buffer, offset, offset+4));
		offset += 4;
		
		rsp.setData(Arrays.copyOfRange(buffer, offset, offset+DataTypeChangeHelper.unsigned4BytesToInt(rsp.getLen(),0)));
		offset += DataTypeChangeHelper.unsigned4BytesToInt(rsp.getLen(),0);
		
		rsp.setCheck(Arrays.copyOfRange(buffer, offset, offset+2));
		offset += 2;
		
		rsp.setTail(Arrays.copyOfRange(buffer, offset, offset+2));
		offset += 2;
		
		return rsp;
	}
	
	private boolean sendReq(byte[] buffer)
	{
		try {
			if (client.isConnected()) {
                if (!client.isOutputShutdown()) {
        			dout.write(buffer);
        			dout.flush();
        			String log = DataTypeChangeHelper.byteToHex(buffer);
        			Log.d("led","sendReq ["+ log+"]");
        			return true;
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private byte[] getRsp()
	{
		try{
			while (true) {
	            if (!client.isClosed()) {
	                if (client.isConnected()) {
	                    if (!client.isInputShutdown()) {
	                    	byte[] buffer = new byte[64];
							int readLen = 0;
							int byteOffset = 0;
							
							// 读取到长度字段为止
							while(true)
							{
								readLen = din.read(buffer, byteOffset, 14-byteOffset);
								String log = DataTypeChangeHelper.byteToHex(buffer);
			        			Log.d("led", "getRsp["+log+"]");
			        			
								byteOffset += readLen;
								if(byteOffset < 14)
								{
									continue;
								}
								break;
							}
							
							// 读取消息体
							int bodyLen = DataTypeChangeHelper.unsigned4BytesToInt(buffer, 10)+4;
							int byteRead = 0;
							while(true)
							{
								readLen = din.read(buffer, byteOffset, bodyLen-byteRead);
								
								byteRead += readLen;
								byteOffset += readLen;
								if(byteRead < bodyLen)
								{
									continue;
								}
								break;
							}
							
							byte[] rspBuffer = new byte[byteOffset];
							System.arraycopy(buffer, 0, rspBuffer, 0, byteOffset);
							
							return rspBuffer;
	                    }
	                }
	            }
	            
	            break;
	        }
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
