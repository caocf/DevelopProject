package com.ebox.pub.file;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import android.os.Environment;

import com.ebox.Anetwork.config.Ctrl;

public class CtrlFile {
	private static final String ctrl_filename = Environment.getExternalStorageDirectory().getPath()+
			"/ctrl.properties";
	
	public static Ctrl getCtrl()
	{
		Ctrl ctrl = new Ctrl();
		try{
			File f=new File(ctrl_filename);
	  		
		   	 if(!f.exists())
		   	 {
				f.createNewFile();
		   	 }
		   	 
			Properties prop = PropertiesFile.loadConfig(ctrl_filename); 
			
			String need = prop.getProperty("needReboot");
		      if(need != null)
		      {
		    	  ctrl.setNeedUpdate(Integer.valueOf(need));
		      }
		}
		catch (IOException e) {
			e.printStackTrace();
		}finally{
        	 if(ctrl.getNeedUpdate() == null)
		      {
        		 ctrl.setNeedUpdate(0);
		      }
         }
		return ctrl;
	}
	
	public static void saveCtrl(Ctrl ctrl)
	{
		File f=new File(ctrl_filename);
  		
	   	 if(!f.exists())
	   	 {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	   	 }
	   	 
		if(ctrl.getNeedUpdate() != null)
		{
			PropertiesFile.saveConfig(ctrl_filename, "needReboot", ctrl.getNeedUpdate()+"");
		}
	}
	
	public static boolean setNeedUpdate(Integer needUpdate)
	{
		Ctrl temp = new Ctrl();
		temp.setNeedUpdate(needUpdate);
		saveCtrl(temp);
		
		return true;
	}
}
