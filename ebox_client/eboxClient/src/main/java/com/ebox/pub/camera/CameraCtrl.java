package com.ebox.pub.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;

import com.ebox.pub.service.global.GlobalField;
import com.jni.camera.CameraPreview;

import java.io.IOException;

public class CameraCtrl {
	private Camera camera;
	private CameraPreview cameraJni;
	private Context context;
	// 摄像头状态： 0：初始化 1：打开 2：关闭
	private int state;
	// 摄像头模式 0:系统API 1：JNI
	private int mode = 1;
	
	public CameraCtrl(Context context)
	{
		this.context = context;
		state = 0;
		mode = GlobalField.config.getCameraCtrl();
	}
	
	public void start()
	{
		Log.i("Pic", "CameraCtrl start "+state);
		if(state == 0)
		{
			if(mode == 0)
			{
				camera = openFacingBackCamera();
				if(camera != null)
				{
					state = 1;
					//Toast.makeText(AppApplication.globalContext, "摄像头初始化成功！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Log.e("Pic", "CameraCtrl start failed "+state);
				}
			}
			else
			{
				cameraJni = new CameraPreview(context);
				if(cameraJni.start())
				{
					state = 1;
					//Toast.makeText(AppApplication.globalContext, "摄像头初始化成功！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Log.e("Pic", "CameraJNI start failed "+state);
				}
			}
		}
	}
	
	public void stop()
	{
		Log.i("Pic", "CameraCtrl stop "+state);
		if(mode == 0)
		{
			if(camera != null)
			{
				camera.setPreviewCallback(null);
		        camera.stopPreview();
		        camera.release();
		        camera = null;
			}
		}
		else
		{
			if(cameraJni != null)
			{
				cameraJni.stop();
			}
		}
        state = 0;
	}
	
	public void release()
	{
		/*if(camera != null)
		{
			camera.setPreviewCallback(null);
	        camera.stopPreview();
	        camera.release();
	        camera = null;
		}
        state = 0;*/
	}

	private Camera openFacingBackCamera() {
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		;
		for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			//if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				try {
					cam = Camera.open(camIdx);
					Log.d("Pic", "CameraCtrl openFacingBackCamera "+cam);
					break;
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			//}
		}

		return cam;
	}
	
	public void takeOtherPicture(int type)
	{
		if(cameraJni != null && state == 1)
		{
			cameraJni.takeOtherPicture(type);
		}
	}
	
	public void takePicture(CameraData data, boolean needClose)
	{
		Log.d("Pic", "takeOrderPicture "+data.getItemId()+" "+data.getImageType());
		
		if(mode == 0)
		{
			if (camera != null) {
				SurfaceView dummy = new SurfaceView(context);
				try {
					camera.setPreviewDisplay(dummy.getHolder());
				} catch (IOException e) {
					e.printStackTrace();
				}
				camera.startPreview();
	
				camera.takePicture(null, null, new PhotoHandler(context, data, needClose));
			}
			else{
				Log.d("Pic", "camera null "+data.getItemId()+" "+data.getImageType());
			}
		}
		else
		{
			if(cameraJni != null && state == 1)
			{
				cameraJni.takeOrderPicture(data);
			}
		}
	}
}
