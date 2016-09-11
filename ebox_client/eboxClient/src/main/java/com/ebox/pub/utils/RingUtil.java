package com.ebox.pub.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;
import android.util.SparseIntArray;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.pub.service.global.GlobalField;

public class RingUtil {
	private static SoundPool soundPool;  
	private static int maxStreams = 70;
	public static int input_id = 0;// 请输入您收到的提取码，然后点击取件 TODO
	public static int pickup_id = 0; // 请您关好箱门，欢迎再次光临，祝您天天好心情
	public static int deal_timeout_id = 0;// 请您先处理超时件
	public static int scan_id = 0;// 请将快递条形码对准扫描口扫描
	public static int scan_success_id = 0;// 扫描成功
	public static int choose_box_id = 0;// 请核对快件大小，选择相应绿色箱门 
	public static int deliver_id = 0; // 箱门已打开，请您存好快递，及时关闭箱门，以免丢失您的物品
	public static int confirm_id = 0;// 箱门已关闭，请确认是否投递
	public static int withdraw_id = 0;// 请取出您的快递，并关好箱门，谢谢您的合作
	public static int failed_id = 0;// 操作失败，请取出您已存放的快递再试一次，非常抱歉给您带来不便
	public static int cancel_id = 0;// 操作取消，请取出您已存放的快递，谢谢合作
	public static int calladmin_id = 0;// 开门失败，请联系管理员取件
	public static int choose_id = 0;// 开门失败，请选择其他箱门
	public static int id_cancel = 0;// 操作取消取走卡片
	public static int id_failed = 0; // 操作失败取走卡片
	public static int id_failed_wait = 0;// 操作失败稍后再试
	public static int id_input_passwd = 0;// 输入密码
	public static int id_insert_card = 0;// 插入卡片
	public static int id_sucess = 0;// 操作成功取走卡片 
	public static int id_process_wait = 0; // 正在处理请稍后
	public static int id_0 = 0;
	public static int id_1 = 0;
	public static int id_2 = 0;
	public static int id_3 = 0;
	public static int id_4 = 0;
	public static int id_5 = 0;
	public static int id_6 = 0;
	public static int id_7 = 0;
	public static int id_8 = 0;
	public static int id_9 = 0;
	private static int now_id = 0;
	
	
	private static SparseIntArray numSound;//箱门数字
	private static int your_choose = 0;//您选择了
	private static int your_orderAt = 0;//您的快递在
	private static int pick_hint = 0;//请您取走快递，关好箱门，谢谢使用
	private static int choose_opened = 0;//已打开，请您存好快递及时关闭箱门
	private static int group = 0;//组
	private static int door = 0;//门


    public static int st001 = 0;
    public static int st002 = 0;
    public static int st003 = 0;
    public static int st004 = 0;
    public static int st005 = 0;
    public static int st006 = 0;
    public static int st007 = 0;
    public static int st008 = 0;
    public static int st009 = 0;
    public static int st010 = 0;
    public static int st011 = 0;
    public static int st012 = 0;
    public static int st013 = 0;
    public static int st014 = 0;
    public static int st015 = 0;
    public static int st016 = 0;
    public static int st017 = 0;
    public static int st018 = 0;
    public static int st019 = 0;
    public static int st020 = 0;
    public static int st021 = 0;
    public static int st022 = 0;
    public static int st023 = 0;
    public static int st024 = 0;
    public static int st025 = 0;
    public static int st026 = 0;
    public static int st027 = 0;
    public static int st028 = 0;
    public static int st029 = 0;
    public static int st030 = 0;
    public static int st031 = 0;
    public static int st032 = 0;
    public static int st033 = 0;
    public static int st034 = 0;
    public static int st035 = 0;

	public static void initSoundPool(){  
		if(soundPool == null)
		{
			numSound=new SparseIntArray();
			soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 100);
			input_id = soundPool.load(AppApplication.globalContext, R.raw.ex_input, 1);
			pickup_id = soundPool.load(AppApplication.globalContext, R.raw.ex_pickup, 1);
			deal_timeout_id = soundPool.load(AppApplication.globalContext, R.raw.ex_deal_timeout, 1);
			scan_id = soundPool.load(AppApplication.globalContext, R.raw.ex_scan, 1);
			scan_success_id = soundPool.load(AppApplication.globalContext, R.raw.ex_scan_success, 1);
			choose_box_id = soundPool.load(AppApplication.globalContext, R.raw.ex_choose_box, 1);
			deliver_id = soundPool.load(AppApplication.globalContext, R.raw.ex_deliver, 1);
			confirm_id = soundPool.load(AppApplication.globalContext, R.raw.ex_confirm, 1);
			withdraw_id = soundPool.load(AppApplication.globalContext, R.raw.ex_withdraw, 1);
			failed_id = soundPool.load(AppApplication.globalContext, R.raw.ex_failed, 1);
			cancel_id = soundPool.load(AppApplication.globalContext, R.raw.ex_cancel, 1);
			calladmin_id = soundPool.load(AppApplication.globalContext, R.raw.ex_calladmin, 1);
			choose_id = soundPool.load(AppApplication.globalContext, R.raw.ex_choose, 1);
			id_cancel = soundPool.load(AppApplication.globalContext, R.raw.ex_id_cancel, 1);
			id_failed = soundPool.load(AppApplication.globalContext, R.raw.ums_id_failed, 1);
			id_failed_wait = soundPool.load(AppApplication.globalContext, R.raw.ums_id_failed_wait, 1);
			id_input_passwd = soundPool.load(AppApplication.globalContext, R.raw.ums_id_input_passwd, 1);
			id_insert_card = soundPool.load(AppApplication.globalContext, R.raw.ums_id_insert_card, 1);
			id_sucess = soundPool.load(AppApplication.globalContext, R.raw.ums_id_sucess, 1);
			id_process_wait = soundPool.load(AppApplication.globalContext, R.raw.ums_id_process_wait, 1);
			id_0 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_0, 1);
			id_1 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_1, 1);
			id_2 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_2, 1);
			id_3 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_3, 1);
			id_4 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_4, 1);
			id_5 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_5, 1);
			id_6 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_6, 1);
			id_7 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_7, 1);
			id_8 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_8, 1);
			id_9 = soundPool.load(AppApplication.globalContext, R.raw.pub_id_9, 1);

			if (GlobalField.config.getDot()!=0)
			{
				st001 = soundPool.load(AppApplication.globalContext, R.raw.st_001, 1);
				st002 = soundPool.load(AppApplication.globalContext, R.raw.st_002, 1);
				st003 = soundPool.load(AppApplication.globalContext, R.raw.st_003, 1);
				st004 = soundPool.load(AppApplication.globalContext, R.raw.st_004, 1);
				st005 = soundPool.load(AppApplication.globalContext, R.raw.st_005, 1);
				st006 = soundPool.load(AppApplication.globalContext, R.raw.st_006, 1);
				st007 = soundPool.load(AppApplication.globalContext, R.raw.st_007, 1);
				st008 = soundPool.load(AppApplication.globalContext, R.raw.st_008, 1);
				st009 = soundPool.load(AppApplication.globalContext, R.raw.st_009, 1);
				st010 = soundPool.load(AppApplication.globalContext, R.raw.st_010, 1);
				st011 = soundPool.load(AppApplication.globalContext, R.raw.st_011, 1);
				st012 = soundPool.load(AppApplication.globalContext, R.raw.st_012, 1);
				st013 = soundPool.load(AppApplication.globalContext, R.raw.st_013, 1);
				st014 = soundPool.load(AppApplication.globalContext, R.raw.st_014, 1);
				st015 = soundPool.load(AppApplication.globalContext, R.raw.st_015, 1);
				st016 = soundPool.load(AppApplication.globalContext, R.raw.st_016, 1);
				st017 = soundPool.load(AppApplication.globalContext, R.raw.st_017, 1);
				st018 = soundPool.load(AppApplication.globalContext, R.raw.st_018, 1);
				st019 = soundPool.load(AppApplication.globalContext, R.raw.st_019, 1);
				st020 = soundPool.load(AppApplication.globalContext, R.raw.st_020, 1);
				st021 = soundPool.load(AppApplication.globalContext, R.raw.st_021, 1);
				st022 = soundPool.load(AppApplication.globalContext, R.raw.st_022, 1);
				st023 = soundPool.load(AppApplication.globalContext, R.raw.st_023, 1);
				st024 = soundPool.load(AppApplication.globalContext, R.raw.st_024, 1);
				st025 = soundPool.load(AppApplication.globalContext, R.raw.st_025, 1);
				st026 = soundPool.load(AppApplication.globalContext, R.raw.st_026, 1);
				st027 = soundPool.load(AppApplication.globalContext, R.raw.st_027, 1);
				st028 = soundPool.load(AppApplication.globalContext, R.raw.st_028, 1);
				st029 = soundPool.load(AppApplication.globalContext, R.raw.st_029, 1);
				st030 = soundPool.load(AppApplication.globalContext, R.raw.st_030, 1);
				st031 = soundPool.load(AppApplication.globalContext, R.raw.st_031, 1);
				st032 = soundPool.load(AppApplication.globalContext, R.raw.st_032, 1);
				st033 = soundPool.load(AppApplication.globalContext, R.raw.st_033, 1);
				st034 = soundPool.load(AppApplication.globalContext, R.raw.st_034, 1);
				st035 = soundPool.load(AppApplication.globalContext, R.raw.st_035, 1);
			}
			for (int i = 0; i < number.length; i++)
			{
				int load = soundPool.load(AppApplication.globalContext, number[i], 1);
				numSound.put(i+1, load);
			}
			
			your_choose=soundPool.load(AppApplication.globalContext, R.raw.ex_your_chooose, 1);
			your_orderAt=soundPool.load(AppApplication.globalContext,  R.raw.ex_your_oder_at, 1);
			pick_hint=soundPool.load(AppApplication.globalContext, R.raw.ex_pickup_new, 1);
			choose_opened=soundPool.load(AppApplication.globalContext, R.raw.ex_door_have_opened, 1);
		
			group=soundPool.load(AppApplication.globalContext, R.raw.ex_num_group, 1);
			door=soundPool.load(AppApplication.globalContext, R.raw.ex_num_door, 1);
		} 
    }  
	
	public  static void playRingtone(int res)
	{
		initSoundPool();
		if (AppApplication.globalContext != null)
		{
			/*final MediaPlayer player = MediaPlayer.create(
					AppApplication.globalContext, res);
			player.start();
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
			{
				public void onCompletion(MediaPlayer mp)
				{
					player.release();
				}
			});*/
			
			AudioManager audioManager = (AudioManager)AppApplication.globalContext.getSystemService(Context.AUDIO_SERVICE);  
	        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
	        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
	        float volume = streamVolumeCurrent / streamVolumeMax;  
	        if(now_id != 0)
	        {
	        	soundPool.stop(now_id);
	        }
	        now_id = soundPool.play(res, volume, volume, 1, 0, (float) 1.0);
		}
	}
	
	public static final int number[]=
		{
			R.raw.ex_num_1,R.raw.ex_num_2,R.raw.ex_num_3,R.raw.ex_num_4,R.raw.ex_num_5,
			R.raw.ex_num_6,R.raw.ex_num_7,R.raw.ex_num_8,R.raw.ex_num_9,R.raw.ex_num_10,
			R.raw.ex_num_11,R.raw.ex_num_12,R.raw.ex_num_13,R.raw.ex_num_14,R.raw.ex_num_15,
			R.raw.ex_num_16,R.raw.ex_num_17,R.raw.ex_num_18,R.raw.ex_num_19,R.raw.ex_num_20,
			R.raw.ex_num_21,R.raw.ex_num_22,R.raw.ex_num_23,R.raw.ex_num_24,R.raw.ex_num_25,
			R.raw.ex_num_26,R.raw.ex_num_27,R.raw.ex_num_28,R.raw.ex_num_29,R.raw.ex_num_30,
			R.raw.ex_num_31,R.raw.ex_num_31
		};
	
	public  static void playChooseDoor(int boardNum, int boxNum)
	{
		AudioManager audioManager = (AudioManager)AppApplication.globalContext.getSystemService(Context.AUDIO_SERVICE);  
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
        final float volume = streamVolumeCurrent / streamVolumeMax;  
        if(now_id != 0)
        {
        	soundPool.stop(now_id);
        }
        int board = numSound.get(boardNum);
        int box = numSound.get(boxNum);
        final int sound[]={your_choose,board,group,box,door};
        new Thread(new Runnable() {
			
			@Override
			public void run() {
			 for (int i = 0; i < sound.length; i++)
			 {
			    now_id = soundPool.play(sound[i], volume, volume, 1, 0, (float) 1.0);
			    if (i==0) {
					SystemClock.sleep(800);
				}else if(i==1){//04
					SystemClock.sleep(840);
				}else if(i==2){//组
					SystemClock.sleep(350);
				}else if(i==3){//23
					SystemClock.sleep(820);
				}
			 }
		}
		}).start();
	}

    public  static void playChooseDoorHaveOpened(int boardNum, int boxNum, boolean isKuaidi)
    {
        AudioManager audioManager = (AudioManager)AppApplication.globalContext.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final float volume = streamVolumeCurrent / streamVolumeMax;
        if(now_id != 0)
        {
            soundPool.stop(now_id);
        }
        int board = numSound.get(boardNum);
        int box = numSound.get(boxNum);
        final int sound[] = new int[5];
        sound[0] = board;
        sound[1] = group;
        sound[2] = box;
        sound[3] = door;
        if(isKuaidi)
        {
            sound[4] = choose_opened;
        }
        else
        {
            sound[4] = st032;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < sound.length; i++)
                {
                    now_id = soundPool.play(sound[i], volume, volume, 1, 0, (float) 1.0);
                    if (i==0) {//02
                        SystemClock.sleep(820);
                    }else if(i==1){//组
                        SystemClock.sleep(350);
                    }else if(i==2){//25
                        SystemClock.sleep(820);
                    }else if(i==3) {//号门
                        SystemClock.sleep(850);
                    }
                }
            }
        }).start();
    }
	
	public  static void playChooseDoorHaveOpened(int boardNum, int boxNum)
	{
		AudioManager audioManager = (AudioManager)AppApplication.globalContext.getSystemService(Context.AUDIO_SERVICE);  
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
        final float volume = streamVolumeCurrent / streamVolumeMax;  
        if(now_id != 0)
        {
        	soundPool.stop(now_id);
        }
        int board = numSound.get(boardNum);
        int box = numSound.get(boxNum);
        final int sound[]={board,group,box,door,choose_opened};
        new Thread(new Runnable() {
			@Override
			public void run() {
			 for (int i = 0; i < sound.length; i++)
			 {
			    now_id = soundPool.play(sound[i], volume, volume, 1, 0, (float) 1.0);
			    if (i==0) {//02
					SystemClock.sleep(820);
				}else if(i==1){//组
					SystemClock.sleep(350);
				}else if(i==2){//25
					SystemClock.sleep(820);
				}else if(i==3) {//号门
					SystemClock.sleep(850);
				}
			 }
			}
		}).start();
	}
	
	public  static void playPickUpDoorOpened(int boardNum, int boxNum)
	{
		AudioManager audioManager = (AudioManager)AppApplication.globalContext.getSystemService(Context.AUDIO_SERVICE);  
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
        final float volume = streamVolumeCurrent / streamVolumeMax;  
        if(now_id != 0)
        {
        	soundPool.stop(now_id);
        }
        int board = numSound.get(boardNum);
        int box = numSound.get(boxNum);
        final int sound[]={your_orderAt,board,group,box,door,pick_hint};
        new Thread(new Runnable() {
			@Override
			public void run() {
			 for (int i = 0; i < sound.length; i++)
			 {
			    now_id = soundPool.play(sound[i], volume, volume, 1, 0, (float) 1.0);
			    if(i==0){//您的快递在
					SystemClock.sleep(1000);
				}else if(i==1){//05
					SystemClock.sleep(820);
				}else if(i==2) {//组
					SystemClock.sleep(350);
				}else if(i==3){//15
					SystemClock.sleep(820);
				}else if(i==4){//号门
					SystemClock.sleep(850);
				}
			 }
			}
		}).start();
	}


	public static void clear()
	{
		soundPool.release();
	}

}
