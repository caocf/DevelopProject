package com.ebox.ex.adv;

import android.os.Environment;
import android.util.Log;

import com.ebox.ex.database.adv.AdvOp;
import com.ebox.ex.database.adv.AdvertiseData;
import com.ebox.pub.service.task.CheckTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadManager {
	private static final String TAG="DownloadManager";
	
	private Boolean canceled;
    private Thread downLoadThread = null;
    private Timer timer;
    private long CHECK_TIME = 5*60*1000;	// 五分钟超时
    private AdvertiseData data;
    
    //存放更新APK文件的路径
    public String UPDATE_DOWNURL = "";
    public String UPDATE_APKNAME = "";
    public String UPDATE_APKNAME_TMP = "";
    
	public static String savefolder = Environment.getExternalStorageDirectory().getPath()+"/adv/";
	private String tempFix = ".tmp";
    
	public static void deleteAdvFile(AdvertiseData data)
	{
		String file = data.getContent().substring(data.getContent().lastIndexOf("/")+1);
		File f=new File(savefolder+"/"+file);
		
		if(f.exists())
		{
			f.delete();
		}
		
		if(data.getAudio_content() != null && !data.getAudio_content().equals(""))
		{
			String audio = data.getAudio_content().substring(data.getAudio_content().lastIndexOf("/")+1);
			File faudio=new File(savefolder+"/"+audio);
			
			if(faudio.exists())
			{
				faudio.delete();
			}
			
		}
	}
	
	public DownloadManager(AdvertiseData data) {
		this.data = data;
        canceled = false;
        setContentInfo(data.getContent());
    }
    
    public void setContentInfo(String downloadUrl)
    {
    	int lastIndex = downloadUrl.lastIndexOf("/");
    	UPDATE_DOWNURL = downloadUrl;
    	UPDATE_APKNAME = downloadUrl.substring(lastIndex+1, downloadUrl.length());
    	UPDATE_APKNAME_TMP = UPDATE_APKNAME+tempFix;
    }
    
    public void setAudioContentInfo(String downloadUrl)
    {
    	int lastIndex = downloadUrl.lastIndexOf("/");
    	UPDATE_DOWNURL = downloadUrl;
    	UPDATE_APKNAME = downloadUrl.substring(lastIndex+1, downloadUrl.length());
    	UPDATE_APKNAME_TMP = UPDATE_APKNAME+tempFix;
    }
    
    public void start()
    {
    	Log.i(TAG, "start download:"+UPDATE_APKNAME+" url:"+UPDATE_DOWNURL);
    	downloadPackage();
    	timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i(TAG, "download timeout:"+UPDATE_APKNAME+" url:"+UPDATE_DOWNURL);
				// 超时，退出下载线程
				canceled = true;
				downLoadThread.interrupt();
			}
		}, CHECK_TIME);
    }
    
    public void downloadPackage() 
    {
    	downLoadThread = new Thread() {            
             @Override  
                public void run() { 
            	 
            	 	if(download(0))
            	 	{
            	 		if(data.getAudio_content() != null &&
                	 			!data.getAudio_content().equals(""))
                	 	{
            	 			setAudioContentInfo(data.getAudio_content());
            	 			
            	 			if(download(1))
            	 			{
            	 				AdvOp.updateAdvertise(data.getContent());
            	 			}
                	 	}
                	 	else
                	 	{
                	 		AdvOp.updateAdvertise(data.getContent());
                	 	}
            	 	}
            	 	
                	CheckTask.removeUpdate(DownloadManager.this);
                	if(timer != null)
                	{
                		timer.cancel();
                	}
                } 
        };
        downLoadThread.start();
    }
    
    private boolean download(int type)
    {
    	FileOutputStream fos = null;
    	InputStream is = null;
    	try {  
    		
            URL url = new URL(UPDATE_DOWNURL);  
//    		  URL url = new URL(URLEncoder.encode(UPDATE_DOWNURL));
          
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.connect();  
            is = conn.getInputStream();  
            
            File fileDir = new File(savefolder);
    		
            if (!fileDir.exists() && !fileDir.mkdirs()) {
            	Log.e(TAG, "Can't create directory:"+savefolder);
                return false;
            }
           
            File ApkFile = new File(savefolder,UPDATE_APKNAME_TMP);
            
            if(ApkFile.exists())
            {
                ApkFile.delete();
            }
            
            fos = new FileOutputStream(ApkFile);  
             
            byte buf[] = new byte[512];  
              
            do{  
                
                int numread = is.read(buf);  
                
                if(numread <= 0){      
                	Log.i(TAG, "download success:"+UPDATE_APKNAME+" url:"+UPDATE_DOWNURL);
                	ApkFile.renameTo(new File(savefolder, UPDATE_APKNAME));
                	checkDownState();
                	return true;
                }  
                fos.write(buf,0,numread);  
            }while(!canceled); 
        } catch (MalformedURLException e) {  
            e.printStackTrace(); 
        } catch(IOException e){  
            e.printStackTrace();  
        }  finally{ 
        	if(fos != null)
        	{
        		try {
					fos.close();
				} catch (IOException e) {
				}
        	}
        	if(is != null)
        	{
        		try {
					is.close();
				} catch (IOException e) {
				}
        	}
        }
    	
    	return false;
    }

	private void checkDownState()
	{
		CheckTask.checkDownState();
	}
}
