package com.ebox.ex.ui.fragment;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ebox.R;
import com.ebox.ex.adv.DownloadManager;
import com.ebox.ex.adv.utils.AdvertImagePagerAdapter;
import com.ebox.ex.adv.utils.AutoScrollViewPager;
import com.ebox.ex.adv.utils.FlowIndicator;
import com.ebox.ex.database.adv.AdvOp;
import com.ebox.ex.database.adv.AdvertiseData;
import com.ebox.ex.network.model.enums.AdvContentType;
import com.ebox.ex.network.model.enums.AdvType;
import com.ebox.pub.utils.FunctionUtils;

public class FragmentAdvPickUp extends Fragment implements OnClickListener {

	private AutoScrollViewPager mViewPager;
	private FlowIndicator mIndicator;
	private List<String> mAdv;
	private ArrayList<AdvertiseData> dlList;
	private int videoIndex = 0;
	private List<String> Videos;
	private RelativeLayout rl_adv1;
	private RelativeLayout rl_adv2;
	private VideoView mVideoView;
	
	private Boolean isVideo = false;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this.getActivity()))
			return;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.ex_fragment_adv, null);

		mViewPager = (AutoScrollViewPager) view
				.findViewById(R.id.auto_scroll_viewpager);
		mIndicator = (FlowIndicator) view.findViewById(R.id.indicator);
		mViewPager.setmIndicator(mIndicator);
		mViewPager.setSlideBorderMode(0);
		mViewPager.setInterval(10000);
		
		mAdv = new ArrayList<String>();
		
		rl_adv1 = (RelativeLayout) view.findViewById(R.id.rl_adv1);
		rl_adv2 = (RelativeLayout) view.findViewById(R.id.rl_adv2);
		mVideoView = (VideoView) view.findViewById(R.id.videoview);
		mPreview = (SurfaceView) view.findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.setFormat(PixelFormat.RGBA_8888); 

		return view;
	}

	private void initData() {
		if(isVideo){
        	rl_adv1.setVisibility(View.GONE);
        	rl_adv2.setVisibility(View.VISIBLE);
        	
    		
    		playVideo();
        } else {
        	rl_adv1.setVisibility(View.VISIBLE);
        	rl_adv2.setVisibility(View.GONE);
        	if (mAdv != null) {
    			mViewPager.setAdapter(new AdvertImagePagerAdapter(getActivity(),mAdv,mViewPager));
    		}
    		mViewPager.startAutoScroll();
    		stopAudioByMedia();
    		
    		if(dlList.size()>0 && dlList.get(0).getAudio_content() != null &&
					!dlList.get(0).getAudio_content().equals("")){
    			String file = dlList.get(0).getAudio_content().substring(dlList.get(0).getAudio_content().lastIndexOf("/") + 1);
    			String fileName = DownloadManager.savefolder + "/" + file;
    			if(FunctionUtils.isExists(fileName)){
    				playAudioByMedia(dlList.get(0).getAudio_content());
    			}
    			
    		}
    		
    		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int arg0) {
					stopAudioByMedia();
					if(dlList.get(arg0).getAudio_content() != null &&
							!dlList.get(arg0).getAudio_content().equals("")){
						String file = dlList.get(arg0).getAudio_content().substring(dlList.get(arg0).getAudio_content().lastIndexOf("/") + 1);
		    			String fileName = DownloadManager.savefolder + "/" + file;
		    			if(FunctionUtils.isExists(fileName)){
		    				playAudioByMedia(dlList.get(arg0).getAudio_content());
		    			}
		    		}
				}
    			
    		});
        }
		
	}

	@Override
	public void onResume() {
		super.onResume();
		dlList = AdvOp.getAdvertiseByType(AdvType.pick_up);
		readAdv();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(isVideo){
			mVideoView.stopPlayback();
		}
		
		stopAudioByMedia();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mViewPager.stopAutoScroll();
		mVideoView.stopPlayback();
		stopAudioByMedia();
	}
	
	

	private void readAdv() {
		// 读取缓存
		isVideo = false;
		videoIndex = 0;
		mAdv = new ArrayList<String>();
		Videos = new ArrayList<String>();
		if (dlList != null && dlList.size() > 0) {
			for (int i = 0; i < dlList.size(); i++) {
				String file = dlList.get(i).getContent().substring(dlList.get(i).getContent().lastIndexOf("/") + 1);
				String fileName = "file://"+DownloadManager.savefolder + "/" + file;
				
				if(dlList.get(i).getContent_type() == AdvContentType.pic){
					mAdv.add(fileName);
				}
				if(dlList.get(i).getContent_type() == AdvContentType.video){
					Videos.add(fileName);
					isVideo = true;
				}
			}
		} else {
			isVideo = false;
			mAdv.add("drawable://"+R.drawable.ex_screen_s);
		}
		initData();
	}
	
	private void playVideo(){
	 	
		/*
		 * Alternatively,for streaming media you can use
		 * mVideoView.setVideoURI(Uri.parse(URLstring));
		 */
		File f1 = new File(Videos.get(videoIndex).substring(7));
		
		if(f1.exists()){
			mVideoView.setVideoPath(Videos.get(videoIndex));
		}
		mVideoView.requestFocus();

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// optional need Vitamio 4.0
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				
				mVideoView.stopPlayback();
//				mVideoView.setVideoPath(Videos.get(videoIndex));
				playVideo();
			}
		});
		
		if(videoIndex < Videos.size()){
			videoIndex++;
		}
		if(videoIndex == (Videos.size())){
			videoIndex = 0;
		}
	}
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.videoview:
			{
				mVideoView.stopPlayback();
				break;
			}
			}
		}
		
		

	public Bitmap getBitmapFromResources(int resId) {
		return BitmapFactory.decodeResource(getResources(), resId);
	}
	
	private void playAudioByMedia(String path){
		String file = path.substring(path.lastIndexOf("/") + 1);
		String fileName = DownloadManager.savefolder + "/"+ file;
		
		try {
			mMediaPlayer = new MediaPlayer(this.getActivity());
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mMediaPlayer.start();
				}
			});
			
			mMediaPlayer.setDataSource("file://" +fileName);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	private void stopAudioByMedia(){
		if(mMediaPlayer!=null){
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
