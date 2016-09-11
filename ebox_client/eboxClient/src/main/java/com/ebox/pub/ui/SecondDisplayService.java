package com.ebox.pub.ui;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.ebox.R;

import java.io.File;
import java.util.ArrayList;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

public class SecondDisplayService extends Service implements
		PresentationHelper.Listener {

	private final IBinder mBinder = new LocalBinder();
	private VideoView mVideoView;
	private Context mContext;
	private ArrayList<String> mVideoPathList;
    private int mPlayIndex = 0;
	private final static String TAG = "SecondDisplayService";

	private WindowManager wm = null;
	private View presoView = null;
	private PresentationHelper helper = null;

	public class LocalBinder extends Binder {
		public SecondDisplayService getService() {
			return SecondDisplayService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		Bundle extras = intent.getExtras();
		if (extras != null) {
            mVideoPathList = extras.getStringArrayList("VideoPathList");
		}

		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		helper = new PresentationHelper(this, this);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Bundle extras = intent.getExtras();
		if (extras != null) {
            mVideoPathList = extras.getStringArrayList("VideoPathList");
		}

        helper.onResume();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		helper.onPause();

		super.onDestroy();
	}

	@Override
	public void showPreso(Display display) {
		Context presoContext = createPresoContext(display);
		LayoutInflater inflater = LayoutInflater.from(presoContext);

		wm = (WindowManager) presoContext
				.getSystemService(Context.WINDOW_SERVICE);

		presoView = buildPresoView(presoContext, inflater);
		wm.addView(presoView, buildLayoutParams());
	}

	@Override
	public void clearPreso(boolean switchToInline) {
		if (presoView != null) {
			try {
				wm.removeView(presoView);
			} catch (Exception e) {
				// probably the window is gone, don't worry, be
				// happy
			}
		}

		presoView = null;
	}

	protected WindowManager.LayoutParams buildLayoutParams() {
		return (new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, 0, 0, 0, 0,
				PixelFormat.OPAQUE));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Context createPresoContext(Display display) {
		Context displayContext = createDisplayContext(display);
		final WindowManager wm = (WindowManager) displayContext
				.getSystemService(WINDOW_SERVICE);

		return (new ContextThemeWrapper(displayContext, getThemeId()) {
			@Override
			public Object getSystemService(String name) {
				if (Context.WINDOW_SERVICE.equals(name)) {
					return (wm);
				}

				return (super.getSystemService(name));
			}
		});
	}

	protected int getThemeId() {
		return (R.style.AppTheme);
	}

	protected View buildPresoView(Context ctxt, LayoutInflater inflater) {
		View contentView = inflater
				.inflate(R.layout.pub_second_display, null);

		// Get the Views
		mVideoView = (VideoView) contentView.findViewById(R.id.surface_view);

        playVideo();

		return contentView;
	}

    private void playVideo(){

		/*
		 * Alternatively,for streaming media you can use
		 * mVideoView.setVideoURI(Uri.parse(URLstring));
		 */
        File f1 = new File(mVideoPathList.get(mPlayIndex).substring(7));

        if(f1.exists())
        {
            mVideoView.setVideoPath(mVideoPathList.get(mPlayIndex));
        }
        else
        {
            mVideoPathList.remove(mPlayIndex);
        }

        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                mVideoView.stopPlayback();
                playVideo();

            }
        });

        if(mPlayIndex < mVideoPathList.size()){
            mPlayIndex++;
        }
        if(mPlayIndex == (mVideoPathList.size())){
            mPlayIndex = 0;
        }
    }

}