package com.ebox.pub.file;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import android.os.Environment;

import com.ebox.Anetwork.config.Temp;

public class TempFile {
	private static final String temp_filename = Environment.getExternalStorageDirectory().getPath()+
			"/temp.properties";
	
	public static Temp getTemp()
	{
		Temp temp = new Temp();
		try{
			File f=new File(temp_filename);
	  		
		   	 if(!f.exists())
		   	 {
				f.createNewFile();
		   	 }
		   	 
			Properties prop = PropertiesFile.loadConfig(temp_filename);  
			String last = prop.getProperty("lastReboot");
		      if(last != null)
		      {
				temp.setLastReboot(Long.valueOf(last));
		      }
	      String is_account = prop.getProperty("is_account");
		      if(is_account != null)
		      {
				temp.setIs_account(Integer.valueOf(is_account));
		      }
		}
		catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(temp.getLastReboot() == null)
		      {
				temp.setLastReboot(0L);
		      }
			
			if(temp.getIs_account() == null)
		      {
				temp.setIs_account(0);
		      }
         }
		return temp;
	}
	
	public static void saveTemp(Temp temp)
	{
		if(temp.getLastReboot() != null)
		{
			PropertiesFile.saveConfig(temp_filename, "lastReboot", temp.getLastReboot()+"");
		}
		if(temp.getIs_account() != null)
		{
			PropertiesFile.saveConfig(temp_filename, "is_account", temp.getIs_account()+"");
		}
	}
}
