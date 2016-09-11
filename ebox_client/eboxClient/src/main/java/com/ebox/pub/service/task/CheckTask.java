package com.ebox.pub.service.task;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.config.Temp;
import com.ebox.AppApplication;
import com.ebox.ex.adv.AdvDownload;
import com.ebox.ex.adv.DownloadManager;
import com.ebox.ex.database.adv.AdvOp;
import com.ebox.ex.database.adv.AdvTable;
import com.ebox.ex.database.adv.AdvertiseData;
import com.ebox.ex.network.model.RspGetConfig;
import com.ebox.ex.network.model.RspGetShowConfig;
import com.ebox.ex.network.model.base.type.AppVersion;
import com.ebox.ex.network.model.req.ReqGetConfig;
import com.ebox.ex.network.request.RequestGetConfig;
import com.ebox.ex.network.request.RequestGetShowConfig;
import com.ebox.ex.ui.TopActivity;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.file.TempFile;
import com.ebox.pub.ledctrl.LedCtrl;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class CheckTask extends TimerTask {
	private static final String TAG = "CheckTask";
	private static Long checkTaskTimes = 719L;
	private TaskData data;
	private static boolean appReport = false;
	private static boolean appShowConfig = false;
	public static boolean hasAdvDownload = false;
	private static ArrayList<DownloadManager> updateList = new ArrayList<DownloadManager>();
	
	public CheckTask(TaskData data)
	{
		this.data = data;
	}
	
	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
//		AppService.getIntance().checkTask();
		//Log.e("Timer", "set mCheckLastRunning:"+AppService.getIntance().mCheckLastRunning);
		
		checkTaskTimes++;
		if(checkTaskTimes >= 65535)
		{
			checkTaskTimes = 1L;
		}

		// 每分钟
		if(checkTaskTimes%12 == 0)
		{
			// 四点重启
			rebootOs();
			//
			BoxInfoHelper.pullServiceOrder();
			//获取服务端配置文件
			getConfig();
			//背光设置
			setBacklight();
			
			// 检测是否有apk需要升级
			if(AppService.getIntance().isSysIdle())
			{
				checkUpdate();
			}
			
			if(updateTime())
			{
				new AdvDownload().advDownload();
			}
			
			// 下载广告图片
			ArrayList<AdvertiseData> dlList = AdvOp.getAdvertise(AdvTable.STATE_0);
			
			if(dlList != null && dlList.size() > 0)
			{
				for(int i = 0; i < dlList.size(); i++)
				{
					if(!getUpdateManager(dlList.get(i)))
					{
						addUpdate(dlList.get(i));
					}
				}
			}
			
			// led开关控制
			if(GlobalField.ledOpen == null || !GlobalField.ledOpen)
			{
				if(isLedOpen())
				{
					Log.i(TAG, "open led");
					LedCtrl led = new LedCtrl();
					
					if(led.init())
					{
						if(led.openCloseLed(0))
						{
							GlobalField.ledOpen = true;
						}
						led.close();
					}
				}
			}
			
			if(GlobalField.ledOpen == null || GlobalField.ledOpen)
			{
				if(!isLedOpen())
				{
					Log.i(TAG, "close led");
					LedCtrl led = new LedCtrl();
					
					if(led.init())
					{
						if(led.openCloseLed(1))
						{
							GlobalField.ledOpen = false;
						}
						led.close();
					}
				}
			}
		}
	}


	private boolean getUpdateManager(AdvertiseData download)
	{
		for(int i = 0; i < updateList.size(); i++)
		{
			if(download.getContent().equals(updateList.get(i).UPDATE_DOWNURL))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void addUpdate(AdvertiseData download)
	{
		DownloadManager udpate =  new DownloadManager(download);
		updateList.add(udpate);
		udpate.start();
	}

	public static void  checkDownState() {
		if (updateList.size() <= 1)
		{//全部下载完成通知更新
			LogUtil.d("通知广告更新");
			AppApplication.getInstance().sendBroadcast(new Intent(AppApplication.BROADCAST_UPDATE_ADVSOURCE));
		}
	}
	
	public static void removeUpdate(DownloadManager udpate)
	{
		for(int i = 0; i < updateList.size(); i++)
		{
			if(updateList.get(i).equals(udpate))
			{
				updateList.remove(i);
				break;
			}
		}
	}
	
	private boolean updateTime()
	{
		Calendar c = Calendar.getInstance();
		// 凌晨一点初始化变量
		if(c.get(Calendar.HOUR_OF_DAY) == 1)
		{
			hasAdvDownload = false;
		}
		
		// 凌晨亮点点发起广告更新请求
		if(c.get(Calendar.HOUR_OF_DAY) == 2 && !hasAdvDownload)
		{
			return true;
		}
		return false;
	}
	
	private boolean isLedOpen()
	{
		int open = Integer.valueOf(GlobalField.config.getLed_open_time());
		int close = Integer.valueOf(GlobalField.config.getLed_close_time());
		Calendar c = Calendar.getInstance();
		int now = c.get(Calendar.HOUR_OF_DAY)*100+c.get(Calendar.MINUTE);
		
		// open: 0700  close: 0100
		if(open >= close)
		{
			if(now >= open || now <= close)
			{
				return true;
			}
		}
		// open:0700 close:2300
		else
		{
			if(now >= open && now <= close)
			{
				return true;
			}
		}
		return false;
	}
	
	private void rebootOs()
	{
		// 上次重启时间
		Long timeMillis = Long.valueOf(GlobalField.temp.getLastReboot());
		
		// 今天凌晨毫秒数
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long nowMillis = cal.getTimeInMillis();
		if(nowMillis >= timeMillis+GlobalField.serverConfig.getRebootDay()*86400000)
		{
			Calendar c = Calendar.getInstance();
			if(c.get(Calendar.HOUR_OF_DAY) == 4 && AppService.getIntance().isSysIdle())
			{
				Temp temp = new Temp();
				temp.setLastReboot(nowMillis);
				temp.setIs_account(null);
				TempFile.saveTemp(temp);
				AppApplication.getInstance().restartOs();
			}
		}
	}
	
	private void setBacklight()
	{
		int time1 = Integer.valueOf(GlobalField.config.getTime1());
		int time2 = Integer.valueOf(GlobalField.config.getTime2());
		int backlight1 = Integer.valueOf(GlobalField.config.getBacklight1());
		int backlight2 = Integer.valueOf(GlobalField.config.getBacklight2());
		Calendar c = Calendar.getInstance();
		String nowBacklight=getBackLight();
		if(c.get(Calendar.HOUR_OF_DAY) >= time1 && c.get(Calendar.HOUR_OF_DAY)< time2 
				&& nowBacklight!=null && backlight1!=Integer.parseInt(nowBacklight) )
		{
			setBackLight(backlight1);
		} else if(c.get(Calendar.HOUR_OF_DAY) >= time2 
				&& nowBacklight!=null && backlight2!=Integer.parseInt(nowBacklight))
		{
			setBackLight(backlight2);
		}
		
	}
	
	/*
	 * 
	 */
	private void setBackLight(int value){
		File f = new File("/sys/class/backlight/pwm-backlight.0/brightness");
		if(f.exists()){
			try {
				FileOutputStream fout = new FileOutputStream("/sys/class/backlight/pwm-backlight.0/brightness");
				
				if(value >= 0 && value<=255)
				{
	        		fout.write((value+"").getBytes());
				}
				fout.flush();
		        fout.close();  
		        //Thread.sleep(0);
			} catch (FileNotFoundException e1) {
				Log.e(TAG, "backlight not found"+e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "backlight IOException"+e.getMessage());
				e.printStackTrace();
			} /*catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		
	}
	
	private String getBackLight(){
		String result=null;
		File f = new File("/sys/class/backlight/pwm-backlight.0/brightness");
		if(f.exists()){
			try {
				FileInputStream fis=new FileInputStream("/sys/class/backlight/pwm-backlight.0/brightness");
				
				 byte[] buf = new byte[1];
		         StringBuffer sb=new StringBuffer();
		         while((fis.read(buf))!=-1){
		             sb.append(new String(buf));    
		         }
		         result = sb.toString();
		         result=result.trim();
		         fis.close();  
		        //Thread.sleep(0);
			} catch (FileNotFoundException e1) {
				Log.e(TAG, "backlight not found"+e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "backlight IOException"+e.getMessage());
				e.printStackTrace();
			} /*catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		return result;
	}
	
	/**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }
    
    public void update(String apk) 
    {
    	Log.i(TAG, "update apk:"+apk);
    	String savefolder = Environment.getExternalStorageDirectory().getPath()+"/apk/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        intent.setDataAndType(
                Uri.fromFile(new File(savefolder, apk)),
                "application/vnd.android.package-archive");
        AppApplication.globalContext.startActivity(intent);
    }
	
	private void checkUpdate()
	{
		File mfile = new File(Environment.getExternalStorageDirectory(), "apk");
		
		if(mfile != null)
		{
			File[] files = mfile.listFiles();
			
			if(files != null && files.length > 0)
			{
				for(int j = 0; j < files.length; j++)
				{
					File file = files[j];
					
					if(file.getName().endsWith("apk"))
					{
						PackageManager packageManager = AppApplication.globalContext.getPackageManager();
						PackageInfo packageInfo = null;
						try{
							packageInfo = packageManager.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
						}
						catch (Exception e){
							file.delete();
							continue;
						}

						if(packageInfo == null)
						{
							file.delete();
							continue;
						}
						
						
						List<PackageInfo> mApps = getAllApps(AppApplication.globalContext);
						boolean needUpdate = false;
						int i ;
				        for(i = 0; i < mApps.size(); i++)
				        {
				        	if(mApps.get(i).packageName.equals(packageInfo.packageName))
				        	{
				        		if(packageInfo.versionName.compareTo(mApps.get(i).versionName) > 0)
				        		{
				        			needUpdate = true;
				        		}
				        		else
				        		{
				        			file.delete();
				        		}
				        		break;
				        	}
				        }
				        
				        if(i == mApps.size())
				        {
				        	needUpdate = true;
				        }
				        
				        if(needUpdate)
				        {
				        	update(file.getName());
				        }
					}
				}
			}
		}
	}

	private void getConfig()
	{
		if(!appReport)
    	{
			updateConfig();
    	}
		if (!appShowConfig) {
			getShowConfig();
		}
	}

	private void getShowConfig(){

		RequestGetShowConfig request=new RequestGetShowConfig(new ResponseEventHandler<RspGetShowConfig>() {
			@Override
			public void onResponseSuccess(RspGetShowConfig result) {
				if (result.isSuccess()) {
					appShowConfig=true;
					GlobalField.showConfig = result.getData();
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				LogUtil.e(TAG, "get showConfig  error  " + error.getMessage());
			}
		});

		RequestManager.addRequest(request, null);

	}

	private void updateConfig() {
		// 获取配置信息
		ReqGetConfig req = new ReqGetConfig();

		AppVersion app = new AppVersion();
		app.setPackageName("com.ebox");
		app.setClientVersion(FunctionUtil.getCurVersion());
		req.setVersion(app);

		RequestGetConfig requestGetConfig=new RequestGetConfig(req, new ResponseEventHandler<RspGetConfig>()
		{

			@Override
			public void onResponseSuccess(RspGetConfig result) {
				if (result.isSuccess())
				{
					GlobalField.serverConfig = result.getData();

					//记录管理员密码
					SharePreferenceHelper.setSupperAdmin(AppApplication.globalContext,
							GlobalField.serverConfig.getUserList());

					//发送显示格格货栈的广播
					Intent intent = new Intent(TopActivity.BROADCAST_GEGE);
					AppApplication.getInstance().sendBroadcast(intent);

					//保存计费字段
					if (GlobalField.serverConfig.getIsAccount() == null)
					{
						GlobalField.serverConfig.setIsAccount(TempFile.getTemp().getIs_account());
					} else {
						Temp tmp = new Temp();
						tmp.setIs_account(GlobalField.serverConfig.getIsAccount());
						tmp.setLastReboot(null);
						TempFile.saveTemp(tmp);
					}

					appReport = true;
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				LogUtil.e(TAG, "get serverConfig  error  " + error.getMessage());
			}
		});

		RequestManager.addRequest(requestGetConfig, null);
	}
}
