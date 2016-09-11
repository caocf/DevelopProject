package com.jni.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;

import com.ebox.pub.camera.CameraData;
import com.ebox.pub.camera.OrderPicture;
import com.ebox.pub.camera.PictureType;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class CameraPreview implements Runnable{
	private static final String TAG="WebCam";
	private Context context;
	// JNI functions
    public native int prepareCamera(int videoid);
    public native int prepareCameraWithBase(int videoid, int camerabase);
    public native void processCamera();
    public native void stopCamera();
    public native void pixeltobmp(Bitmap bitmap);
    static {
        System.loadLibrary("ImageProc");
    }
    
    // /dev/videox (x=cameraId+cameraBase) is used.
 	// In some omap devices, system uses /dev/video[0-3],
 	// so users must use /dev/video[4-].
 	// In such a case, try cameraId=0 and cameraBase=4
 	private int cameraId=0;
// 	private int cameraBase=0;
 	//a31s modify by mafeng 20150306
 	private int cameraBase=0;
 	
 	// This definition also exists in ImageProc.h.
 	// Webcam must support the resolution 640x480 with YUYV format. 
 	static final int IMG_WIDTH=640;
 	static final int IMG_HEIGHT=480;
 	
 	public Bitmap bmp=null;

	private boolean cameraExists=false;
	private boolean shouldStop=false;
	
	// The following variables are used to draw camera images.
    private int winWidth=0;
    private int winHeight=0;
    private Rect rect;
    private int dw, dh;
    private float rate;
    Thread mainLoop = null;
    SurfaceView dummy;
    
    private CameraData data;
    private Integer pictureType;
	public volatile static CameraPreview cameraPreview=null;
	public static ThreadPoolExecutor poolExecutor=null;
    
    public CameraPreview(Context context)
	{
		this.context = context;
		dummy = new SurfaceView(context);
	}
    
	private  CameraPreview(){
		poolExecutor=new ScheduledThreadPoolExecutor(1);
	}

	/**龙岗拍照上传多线程控制
	 *
	 * 单例控制多线程访问摄像头资源
	 * */
	public static CameraPreview getInstance() {
		if (cameraPreview==null) {
			synchronized (CameraPreview.class) {
				if (cameraPreview==null)
				{
					cameraPreview=new CameraPreview();
				}
			}
		}
		return cameraPreview;
	}

	public void startRun(Runnable run,SurfaceView dummy,Context context){
		this.context=context;
		this.dummy=dummy;

		if (poolExecutor!=null)
			poolExecutor.execute(run);
	}

	public synchronized boolean start()
	{
		Log.e("WebCam"," camera start thread:"+Thread.currentThread().getId()+" exist:"+cameraExists);
		if(bmp==null){
			bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.ARGB_8888);
		}
		int ret=-1;
		if (!cameraExists) {
			// /dev/videox (x=cameraId + cameraBase) is used
			 ret = prepareCameraWithBase(cameraId, cameraBase);
		}
		
		if(ret!=-1) 
		{
			cameraExists = true;
			//mainLoop = new Thread(this);
	        //mainLoop.start();	
		}
		else
		{
			cameraExists = false;
		}
		
		return cameraExists;
	}
    
    
    public synchronized void stop()
	{
		Log.e("WebCam"," camera stop thread:"+Thread.currentThread().getId());
    	/*if(cameraExists){
    		shouldStop = true;
			while(shouldStop){
				try{ 
					Thread.sleep(100); // wait for thread stopping
				}catch(Exception e){}
			}
		}*/
		cameraExists=false;
		stopCamera();
	}
    
    public  void shot()
    {
    	//obtaining display area to draw a large image
    	if(winWidth==0){
    		winWidth=dummy.getWidth();
    		winHeight=dummy.getHeight();

    		if(winWidth*3/4<=winHeight){
    			dw = 0;
    			dh = (winHeight-winWidth*3/4)/2;
    			rate = ((float)winWidth)/IMG_WIDTH;
    			rect = new Rect(dw,dh,dw+winWidth-1,dh+winWidth*3/4-1);
    		}else{
    			dw = (winWidth-winHeight*4/3)/2;
    			dh = 0;
    			rate = ((float)winHeight)/IMG_HEIGHT;
    			rect = new Rect(dw,dh,dw+winHeight*4/3 -1,dh+winHeight-1);
    		}
    	}
    	
    	// obtaining a camera image (pixel data are stored in an array in JNI).
    	processCamera();
    	// camera image to bmp
    	pixeltobmp(bmp);
    	
        Canvas canvas = dummy.getHolder().lockCanvas();
        if (canvas != null && bmp != null)
        {
        	// draw camera bmp on canvas
        	canvas.drawBitmap(bmp,null,rect,null);

        	dummy.getHolder().unlockCanvasAndPost(canvas);
        }
    }
    
    @Override
    public void run() {
    	if(bmp == null)
		{
			Log.e(TAG, "bmp is null");
			return;
		}
		
		int loop = 0;
		while(true)
		{
			loop ++;
			if(loop >= 10)
			{
				break;
			}
			shot();
		}
		
		if(pictureType.equals(PictureType.Order))
		{
			File pictureFileDir = OrderPicture.getDir(data);
			
	        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	        	Log.d(TAG, "Can't create directory to save image.");
	            return;
	        }
	        //  上传图片压缩为20%
	        String filename = pictureFileDir.getPath() + File.separator + OrderPicture.getOrderPictureName(data);
	        File pictureFile = new File(filename);
	        Log.d(TAG, "filename is: "+ filename);
	        
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            bmp.compress(Bitmap.CompressFormat.JPEG, 20, fos);
	            fos.flush();
	            fos.close();
	            Log.d(TAG, "New Image saved:" + filename);

	        } catch (Exception error) {
	        	Log.d(TAG, "Image could not be saved.");
	        } finally{
	        }
	        
	        //  本地存储原分辨率图片
	        String filenameBak = OrderPicture.getBakDir().getPath() + File.separator + OrderPicture.getOrderPictureName(data);
	        File pictureFileBak = new File(filenameBak);
	        Log.d(TAG, "filename bak is "+ filenameBak);
	        
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFileBak);
	            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	            fos.flush();
	            fos.close();
	            Log.d(TAG, "New Bak Image saved:" + filenameBak);

	        } catch (Exception error) {
	        	Log.d(TAG, "Bak Image could not be saved.");
	        } finally{
	        }
		}
		else
		{
			File pictureFileDir = new File(Environment
			          .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"photo_admin");

	        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	        	Log.d(TAG, "Can't create directory to save image.");
	            return;
	        }
	        
	        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis()+".jpg";
	        File pictureFile = new File(filename);
	        Log.d(TAG, "filename is " + filename);
	        
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	            fos.flush();
	            fos.close();
	            Log.d(TAG, "New Image saved:" + filename);

	        } catch (Exception error) {
	        	Log.d(TAG, "Image could not be saved.");
	        } finally{
	        }
		}
    }


	public synchronized void takeOrderPicture(CameraData data)
	{
    	this.data = data;
    	this.pictureType = PictureType.Order;
    	mainLoop = new Thread(this);
        mainLoop.start();	
	}
    
    public void takeOtherPicture(int type)
	{
    	pictureType = type;
    	mainLoop = new Thread(this);
        mainLoop.start();	
	}
}
