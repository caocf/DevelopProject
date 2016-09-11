package com.ebox.pub.ledctrl;

import com.ebox.pub.utils.TimeUtil;

public class DataTypeChangeHelper {
	/** 
     * 将Byte数组转换成十六进制字符串 
     *  
     * @param b 
     *            byte 
     * @return convert result 
     */  
    public static String byteToHex(byte[] b) {  
    	if (b==null)
    	{
    		return "null,"+TimeUtil.currentTime();
		}
    	String str = "";
    	for(int i = 0; i < b.length; i++)
    	{
    		str += ",0x"+Integer.toHexString(b[i] & 0xFF);
    	}
        return str;  
    }  
	/** 
     * 将一个单字节的Byte转换成十六进制的数 
     *  
     * @param b 
     *            byte 
     * @return convert result 
     */  
    public static String byteToHex(byte b) {  
        int i = b & 0xFF;  
        return Integer.toHexString(i);  
    }  
  
    /** 
     * 将一个4byte的数组转换成32位的int 
     *  
     * @param buf 
     *            bytes buffer 
     * @param byte[]中开始转换的位置 
     * @return convert result 
     */  
    public static int unsigned4BytesToInt(byte[] buf, int pos) {  
        int firstByte = 0;  
        int secondByte = 0;  
        int thirdByte = 0;  
        int fourthByte = 0;  
        int index = pos;  
        firstByte = (0x000000FF & ((int) buf[index]));  
        secondByte = (0x000000FF & ((int) buf[index + 1]));  
        thirdByte = (0x000000FF & ((int) buf[index + 2]));  
        fourthByte = (0x000000FF & ((int) buf[index + 3]));  
        index = index + 4;  
        return ((int) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFF;  
    }  
  
    /** 
     * 将16位的short转换成byte数组 
     *  
     * @param s 
     *            short 
     * @return byte[] 长度为2 
     * */  
    public static byte[] shortToByteArray(short s) {  
        byte[] targets = new byte[2];  
        for (int i = 0; i < 2; i++) {  
            int offset =  i * 8;  
            targets[i] = (byte) ((s >>> offset) & 0xff);  
        }  
        return targets;  
    }  
  
    /** 
     * 将32位整数转换成长度为4的byte数组 
     *  
     * @param s 
     *            int 
     * @return byte[] 
     * */  
    public static byte[] intToByteArray(int s) {  
        byte[] targets = new byte[4];  
        for (int i = 0; i < 4; i++) {  
            int offset = i * 8;  
            targets[i] = (byte) ((s >>> offset) & 0xff);  
        }  
        return targets;  
    }  
  
    /** 
     * long to byte[] 
     *  
     * @param s 
     *            long 
     * @return byte[] 
     * */  
    public static byte[] longToByteArray(long s) {  
        byte[] targets = new byte[2];  
        for (int i = 0; i < 8; i++) {  
            int offset = i * 8;  
            targets[i] = (byte) ((s >>> offset) & 0xff);  
        }  
        return targets;  
    }  
}
