package com.ebox.pub.camera;


import java.io.File;
import java.io.FileOutputStream;

import com.ebox.AppApplication;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;

public class PhotoHandler implements PictureCallback {
	private final String TAG = "Pic";
    private CameraData cameraData;
    private boolean needClose;

    public PhotoHandler(Context context , CameraData data ,boolean needClose) {
        this.cameraData= data;
        this.needClose = needClose;
    }
    
    private void finishTake()
    {
    	if(needClose)
    	{
    		AppApplication.getInstance().getCc().stop();
    	}
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = OrderPicture.getDir(cameraData);
        
        Log.d("Pic", "onPictureTaken ");

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

        	Log.e(TAG, "Can't create directory to save image.");
            finishTake();
            return;
        }

        String filename = pictureFileDir.getPath() + File.separator + OrderPicture.getOrderPictureName(cameraData);

        File pictureFile = new File(filename);
        Log.d(TAG, "filename is "+ filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d(TAG, "New Image saved:" + OrderPicture.getOrderPictureName(cameraData));

        } catch (Exception error) {
        	Log.d(TAG, "Image could not be saved.");
        } finally{
        	finishTake();
        }
    }
}
