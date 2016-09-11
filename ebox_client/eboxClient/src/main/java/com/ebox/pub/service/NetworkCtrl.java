package com.ebox.pub.service;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ebox.AppApplication;

public class NetworkCtrl {
	public static boolean IsConnectedToNetwork(Context context) 
	{
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] allNetworkInfo = conManager.getAllNetworkInfo();
		NetworkInfo currNetworkInfo;
		boolean anythingConnected = false;
		for (int i = 0; i < allNetworkInfo.length; i++) 
		{
			currNetworkInfo = allNetworkInfo[i];
			if (currNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
				anythingConnected = true;
		}

		return anythingConnected;
	}
    
    public static boolean connectMobile() 
    {
		ConnectivityManager manager = (ConnectivityManager) AppApplication.globalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info != null && info.getState() == NetworkInfo.State.CONNECTED) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
    
    public static boolean connectWifi() 
    {
		ConnectivityManager manager = (ConnectivityManager) AppApplication.globalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info != null && info.getState() == NetworkInfo.State.CONNECTED) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
    
    public static boolean isNeedProxy()
	{
		/*ConnectivityManager connectivityManager = (ConnectivityManager) AppApplication.globalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null)
		{
			String type = activeNetInfo.getTypeName();
			// WIFI 返回不使用
			if (type.equalsIgnoreCase("WIFI"))
			{
				return false;
			}
			// 移动网络 判断当前链接类型
			else if (type.equalsIgnoreCase("MOBILE"))
			{
				String mobileProxyIp = "";
				String mobileProxyPort = "";
				String mobileAPN = "";
				Cursor c = AppApplication.globalContext.getContentResolver().query(
						Uri.parse("content://telephony/carriers/preferapn"),
						null, null, null, null);
				if (c != null)
				{
					if (!c.moveToFirst()) 
					{
						return false;
					}
					mobileProxyIp = c.getString(c.getColumnIndex("proxy"));
					mobileProxyPort = c.getString(c.getColumnIndex("port"));
					mobileAPN = c.getString(c.getColumnIndex("apn"));
					c.close();
					//  IP跟端口不为空
					if (mobileProxyIp != null && !"".equals(mobileProxyIp)
							&& !"".equals(mobileProxyPort))
					{
						GlobalField.hostUrl = mobileProxyIp;
						try
						{
							GlobalField.hostPort = Integer.parseInt(mobileProxyPort);

						}
						catch (Exception e)
						{
							return false;
						}
						return true;
					}
					else
					{
						if(mobileAPN != null && mobileAPN.endsWith("ctwap"))
						{
							GlobalField.hostUrl = GlobalField.ctwaphost;
							GlobalField.hostPort = 80;
							return true;
						}
						return false;
					}
				}
			}
		}*/
		return false;
	}
    
    public String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException ex)
		{
		}
		return null;
	}
  //打开或关闭GPRS
    public static  boolean gprsEnable(boolean bEnable)
    {
        Object[] argObjects = null;
                
        boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
        if(isOpen == !bEnable)
        {
            setGprsEnable("setMobileDataEnabled", bEnable);
        }
        
        return isOpen;    
    }
    
    //检测GPRS是否打开
    public static boolean gprsIsOpenMethod(String methodName)
    {
    	ConnectivityManager mCM = (ConnectivityManager) AppApplication.globalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass         = mCM.getClass();
        Class[] argClasses     = null;
        Object[] argObject     = null;
        
        Boolean isOpen = false;
        try
        {
            Method method = cmClass.getMethod(methodName, argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return isOpen;
    }
    
    //开启/关闭GPRS
    public static void setGprsEnable(String methodName, boolean isEnable)
    {
    	ConnectivityManager mCM = (ConnectivityManager) AppApplication.globalContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass         = mCM.getClass();
        Class[] argClasses     = new Class[1];
        argClasses[0]         = boolean.class;
        
        try
        {
            Method method = cmClass.getMethod(methodName, argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
