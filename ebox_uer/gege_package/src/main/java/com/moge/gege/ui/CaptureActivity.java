package com.moge.gege.ui;

import java.io.IOException;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.moge.gege.R;
import com.moge.gege.model.ActivityListModel;
import com.moge.gege.model.RespActivityListModel;
import com.moge.gege.model.RespQRCodeModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ActivityListRequest;
import com.moge.gege.network.request.QRCodeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.barcode.CameraManager;
import com.moge.gege.util.barcode.CaptureActivityHandler;
import com.moge.gege.util.barcode.InactivityTimer;
import com.moge.gege.util.barcode.ViewfinderView;

public class CaptureActivity extends BaseActivity implements Callback,
        OnClickListener
{
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.90f;
    private boolean vibrate;
    private Context mContext;

    public static  final  String SCAN_TYPE="scan_type";
    public static  final  int SCAN_TYPE_DETAILS_ORDER=1;
    public static  final  int SCAN_TYPE_OPEN_EBOX=2;
    private int mScanType;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_barcode);
        mScanType=getIntent().getIntExtra(SCAN_TYPE,0);
        mContext = CaptureActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface)
        {
            initCamera(surfaceHolder);
        }
        else
        {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(
                AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy()
    {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode)
    {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        final String resultString = result.getText();

        if (resultString.equals(""))
        {
            ToastUtil.showToastShort("抱歉，没有识别出来~");
        }
        else {

            if (mScanType == SCAN_TYPE_OPEN_EBOX)
            {
                UIHelper.showScanOpenActivity(mContext,resultString);
                finish();
            } else {
                doQRCodeRequest(resultString);
            }


            // to do list!!!
//            if (UIHelper.showUrlRedirect(mContext, resultString))
//            {
//                finish();
//            }
//            else
//            {
//                ToastUtil.showToastShort(resultString);
//            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        }
        catch (IOException ioe)
        {
            return;
        }
        catch (RuntimeException e)
        {
            return;
        }
        if (handler == null)
        {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!hasSurface)
        {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView()
    {
        return viewfinderView;
    }

    public Handler getHandler()
    {
        return handler;
    }

    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound()
    {
        if (playBeep && mediaPlayer == null)
        {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.qrcode_found);
            try
            {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (IOException e)
            {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate()
    {
        if (playBeep && mediaPlayer != null)
        {
            mediaPlayer.start();
        }
        if (vibrate)
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    private void doQRCodeRequest(String content)
    {
        QRCodeRequest request = new QRCodeRequest(content,
                new ResponseEventHandler<RespQRCodeModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespQRCodeModel> request,
                            RespQRCodeModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            UIHelper.showUrlRedirect(mContext,
                                    result.getData().getAction());
                            finish();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

}