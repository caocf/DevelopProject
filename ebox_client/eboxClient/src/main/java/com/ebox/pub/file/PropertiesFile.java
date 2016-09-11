package com.ebox.pub.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {
	public static Properties loadConfig(String file) {
		Properties properties = new Properties();
		FileInputStream s = null;
		try {
			s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
       	 	if(s != null)
       	 	{
       	 		try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
       	 	}
        }
		return properties;
	}

	public static void saveConfig(String file,String parameterName,String parameterValue) {
		FileOutputStream s = null;
		InputStream fis = null;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream(file);
            prop.load(fis);
			s = new FileOutputStream(file, false);
			prop.setProperty(parameterName, parameterValue);
            prop.store(s, "Update '" + parameterName + "' value");
		} catch (Exception e){
			e.printStackTrace();
		}finally{
       	 	if(s != null)
       	 	{
       	 		try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
       	 	}
       	 	
       	 	if(fis != null)
    	 	{
    	 		try {
    	 			fis.close();
    	 			fis = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
    	 	}
        }
	}
}
