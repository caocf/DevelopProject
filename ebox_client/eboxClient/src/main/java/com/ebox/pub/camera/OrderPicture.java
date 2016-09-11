package com.ebox.pub.camera;

import android.os.Environment;
import android.util.Log;

import com.ebox.ex.network.model.enums.OrderImageType;

import java.io.File;

public class OrderPicture {
	private final static String TAG = "Pic";
	public final static String separator = "_";
    public static final String photePath = "photo";
    public static final String photeDeliver = "photoDeliver";
    public static final String photeBakPath = "photo_bak";


	public static String getOrderPictureName(CameraData data)
	{
		return data.getOrderId()+separator+data.getItemId()+separator+data.getImageType()+".jpg";
	}
	
	public static boolean validPic(String name)
	{
		int index = name.indexOf(separator);
		
		if(index == -1)
		{
			return false;
		}
		
		if(!name.substring(0, index).matches("[0-9]+"))
		{
			return false;
		}
		
		int lastIndex = name.lastIndexOf(separator);
		
		if(lastIndex == -1)
		{
			return false;
		}
		
		int lastDotIndex = name.lastIndexOf(".");
		if(lastDotIndex == -1)
		{
			return false;
		}
		
		if(!name.substring(lastIndex+1, lastDotIndex).matches("[0-9]+"))
		{
			return false;
		}
		
		return true;
	}
	
	public static Long getOrderId(String pic)
	{
		return Long.valueOf(pic.substring(0, pic.indexOf(separator)));
	}
	
	public static String getItemId(String pic)
	{
		int firstIndex = pic.indexOf(separator);
		int lastIndex = pic.lastIndexOf(separator);
		
		return pic.substring(firstIndex+1, lastIndex);
	}

	public static File getDir(CameraData data) {
		File file;
		if(data.getImageType().equals(OrderImageType.STORE))
		{
			file = getDeliverDir();
		}
		else
		{
			file = getUploadDir();
		}
		
		if (!file.exists() && !file.mkdirs()) {
        	Log.e(TAG, "Can't create directory to save image.");
            return null;
        }
		
		return file;
    }
	
	public static File getUploadDir() {
		File sdDir = Environment
		          .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		        return new File(sdDir, OrderPicture.photePath);
    }
	
	public static File getDeliverDir() {
		File sdDir = Environment
		          .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		        return new File(sdDir, OrderPicture.photeDeliver);
    }
	
	public static File getBakDir() {
        File sdDir = Environment
          .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File path = new File(sdDir, OrderPicture.photeBakPath);
        
        if (!path.exists() && !path.mkdirs()) {
        	Log.e(TAG, "Can't create directory to save image.");
            return null;
        }
        return path;
    }
}
