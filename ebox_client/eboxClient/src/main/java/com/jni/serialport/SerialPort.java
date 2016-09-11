/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.jni.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	
	static public Process getSu() throws Exception
	{
		return Runtime.getRuntime().exec("/system/xbin/su");
	}

	public void OpenPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			Process su = null;
			try {
				su = getSu();
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}finally {
				if(su != null)
				{
					//su.destroy();
				}
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate, flags,0,8,1,'N');
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
    private native static FileDescriptor open(String path, int baudrate, int flags, int mode, int databits,int stopbits,char parity);
	public native void close();
	static {
		System.loadLibrary("serial_port");
	}
	
	//a31s
	
	public void setGpio_A31(int value)
	{
		try {
			FileOutputStream fout = new FileOutputStream("/sys/devices/platform/hub_power/ctrl_485");
			
			if(value == 0)
			{
        		fout.write("0".getBytes());
			}
			else
			{
				fout.write("1".getBytes());
			}
			fout.flush();
	        fout.close();  
	        //Thread.sleep(0);
		} catch (FileNotFoundException e1) {
			Log.e(TAG, "gpio38 not found"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "gpio38 IOException"+e.getMessage());
			e.printStackTrace();
		} 
	}
	
	//Ti platform
	public void setGpio(int value)
	{
		try {
			FileOutputStream fout = new FileOutputStream("/sys/class/gpio/gpio38/value");
			
			if(value == 0)
			{
        		fout.write("0".getBytes());
			}
			else
			{
				fout.write("1".getBytes());
			}
			fout.flush();
	        fout.close();  
	        //Thread.sleep(0);
		} catch (FileNotFoundException e1) {
			Log.e(TAG, "gpio38 not found"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "gpio38 IOException"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*public void setGpio(int value)
	{
		Process su = null;
		try {
			su = getSu();
			String cmd;
			if(value == 0)
			{
				cmd = "echo 0 > /sys/class/gpio/gpio38/value\n";
			}
			else
			{
				cmd = "echo 1 > /sys/class/gpio/gpio38/value\n";
			}
			//String[] cmdStrings = new String[] {"sh", "-c", cmd};
			//Runtime.getRuntime().exec(cmdStrings);
			su.getOutputStream().write(cmd.getBytes());
			Thread.sleep(40);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(su != null)
			{
				su.destroy();
			}
		}
	}*/

    /*
    ** Ti Gpio 设置
     */
	public void initGpio()
	{
		Process su = null;
		try {
			su = getSu();
			String cmd;
			cmd = "echo 38 > /sys/class/gpio/export\n";
			su.getOutputStream().write(cmd.getBytes());
			cmd = "echo out > /sys/class/gpio/gpio38/direction\n";
			su.getOutputStream().write(cmd.getBytes());
			cmd = "chmod 666 /sys/class/gpio/gpio38/value\n";
			su.getOutputStream().write(cmd.getBytes());
			
			//背光初始化
			File f = new File("/sys/class/backlight/pwm-backlight.0/brightness");
			if(f.exists()){
				cmd = "chmod 666 /sys/class/backlight/pwm-backlight.0/brightness\n";
				su.getOutputStream().write(cmd.getBytes());
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(su != null)
			{
			//	su.destroy();
			}
		}

        init3G();
	}

    /*
    ** a31s 背光，3G初始化
     */
    public void initA31s()
    {
        Process su = null;
        try {
            su = getSu();
            String cmd;

            //背光初始化
            File f = new File("/sys/class/disp/disp/attr/lcd_bl");
            if(f.exists()){
                cmd = "chmod 666 /sys/class/disp/disp/attr/lcd_bl\n";
                su.getOutputStream().write(cmd.getBytes());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(su != null)
            {
              //  su.destroy();
            }
        }

//        init3G();
    }

    public void init3G()
    {
        Process su = null;
        try {
            su = getSu();
            String cmd;
            // 3G LED??
            File f1 = new File("/dev/ttyUSB0");
            if (f1.exists()) {
                cmd = "ifconfig eth0 192.168.1.22 netmask 255.255.255.0\n";
                su.getOutputStream().write(cmd.getBytes());
                cmd = "ip route add 192.168.1.0 dev eth0\n";
                su.getOutputStream().write(cmd.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(su != null)
            {
                //su.destroy();
            }
        }
    }
}
