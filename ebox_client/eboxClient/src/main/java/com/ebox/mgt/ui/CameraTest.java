package com.ebox.mgt.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ebox.pub.service.global.Constants;
import com.jni.camera.CameraPreview;

import java.io.File;
import java.io.FileOutputStream;

public class CameraTest extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "WebCam";
    protected Context context;
    private SurfaceHolder holder;
    private CameraPreview camera;

    private boolean cameraExists = false;
    private boolean shouldStop = false;

    public CameraTest(Context context) {
        super(context);
        this.context = context;
        if (Constants.DEBUG) Log.d(TAG, "CameraPreview constructed");
        setFocusable(true);

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    public CameraTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (Constants.DEBUG) Log.d(TAG, "CameraPreview constructed");
        setFocusable(true);

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void run() {
        if (Constants.DEBUG)
            Log.d(TAG, "start draw image   id:" + Thread.currentThread().getId() + "," + cameraExists);
        while (true && cameraExists) {
            //obtaining display area to draw a large image
            camera.shot();
            if (shouldStop) {
                shouldStop = false;
                break;
            }
        }
        camera.stop();
        //SystemClock.sleep(10);
        if (Constants.DEBUG)
            Log.d(TAG, "run()--- stoped  id:" + Thread.currentThread().getId() + "exist:" + cameraExists + ",stop:" + shouldStop);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (Constants.DEBUG)
            Log.d(TAG, "surfaceCreated " + Thread.currentThread().getName() + ",id:" + Thread.currentThread().getId() + "," + cameraExists);
        //AppApplication.getInstance().getCc().stop();
        shouldStop = false;
        //camera = new CameraPreview(context, this);
        camera = CameraPreview.getInstance();
        if (camera.start())
            cameraExists = true;

        camera.startRun(this, this, context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (Constants.DEBUG) Log.d(TAG, "surfaceChanged");
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (Constants.DEBUG)
            Log.d(TAG, "surfaceDestroyed " + Thread.currentThread().getName() + ",id:" + Thread.currentThread().getId() + "," + cameraExists);
        if (cameraExists) {
            shouldStop = true;
        }
        //	AppApplication.getInstance().getCc().start();
    }

    public String takePicture() {
        if (camera.bmp == null) {
            Log.e(TAG, "bmp is null");
            Toast.makeText(context, "拍照失败：bmp is null", Toast.LENGTH_LONG).show();
            return null;
        }

        File pictureFileDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photo_test");

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.d(TAG, "Can't create directory to save image.");
            Toast.makeText(context, "拍照失败：photo_test路径打开错误", Toast.LENGTH_LONG).show();
            return null;
        }

        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Log.d(TAG, "filename is " + filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            camera.bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, "New Image saved:" + filename);
            Toast.makeText(context, "拍照成功：" + filename, Toast.LENGTH_LONG).show();

        } catch (Exception error) {
            Log.d(TAG, "Image could not be saved.");
            Toast.makeText(context, "拍照失败：写入文件失败", Toast.LENGTH_LONG).show();
        } finally {
        }

        return filename;
    }

    /**
     * 关闭办理证照摄像头
     */
    public void stopCamera() {
        if (cameraExists) {
            shouldStop = true;
            Log.d(TAG, "stopCamera()-----id:" + Thread.currentThread().getId());
        }
    }
}

