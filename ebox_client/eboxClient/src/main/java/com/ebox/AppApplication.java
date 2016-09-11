package com.ebox;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import com.ebox.Anetwork.RequestManager;
import com.ebox.ex.database.ExDataStorer;
import com.ebox.ex.network.model.enums.MainBoardType;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.TopActivity;
import com.ebox.ex.utils.BoxInfoHelper;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.boxctl.serial.LogicSerialTask;
import com.ebox.pub.camera.CameraCtrl;
import com.ebox.pub.database.PubDataStorer;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.file.TempFile;
import com.ebox.pub.idcard.serial.LogicIdCardSerialTask;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.ImageLoaderUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.ums.database.UmsDataStorer;
import com.jni.serialport.SerialPort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    private static AppApplication application;
    public static Context globalContext;
    public LogicSerialTask sb = null;
    private SerialPort mSerialPort = null;
    private static final int O_NDELAY = 04000;

    public CameraCtrl cc;
    public ArrayList<Activity> activityList = new ArrayList<Activity>();
    public ExDataStorer exDs;
    public PubDataStorer pubDs;
    public UmsDataStorer umsDs;

    public static LogicIdCardSerialTask si = null;
    private SerialPort mSerialPort1 = null; //身份证串口

    public static final String BROADCAST_UPDATE_ADVSOURCE="com.ebox.broadcast.update.advsource";
    private int appUid;

    public boolean is55Screen = false;
    public ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "AppApplication onCreate()");
        globalContext = this.getBaseContext();
        application = this;
        executorService= Executors.newScheduledThreadPool(3);
        exDs = new ExDataStorer();
        pubDs = new PubDataStorer();
        umsDs = new UmsDataStorer();

        // 读取配置文件
        readIni();
        RequestManager.init(this);
        //图片加载工具
        ImageLoaderUtil.instance().init(this);

        //检测是否更改终端
        SharePreferenceHelper.isChangeTerminal_code();
        //版本升级默认全部配置完成
        if (SharePreferenceHelper.isVersionUpdate())
        {
            LogUtil.d("版本升级 " + SharePreferenceHelper.curVersion());
            SharePreferenceHelper.saveInitBoxState(true);
            SharePreferenceHelper.saveCheckConifgState(true);
            //升级设置需要同步全部订单
            SharePreferenceHelper.saveSyncOrderState(false);
        }
        //程序启动锁定订单格口
        BoxInfoHelper.syncLocalOrderToBox();

        new Thread(new Runnable() {
            @Override
            public void run() {
                BackInit();
            }
        }).start();

    }

    private void BackInit() {

        // 摄像头初始化
        camearInit();
        // 串口初始化
        portInit();
        // 音频初始化
        RingUtil.initSoundPool();

        if (Constants.config == Constants.Config.RELEASE)
        {
            CrashHandler crashHandler = CrashHandler.getInstance();
            // 注册crashHandler
            crashHandler.init(getApplicationContext());
            // 发送以前没发送的报告
            crashHandler.sendPreviousReportsToServer();
        }
        startEboxLogService();
        //货栈相关
        FunctionUtils.init(this);

        //启动ebox.video
        if (GlobalField.config.getScreen() != 0)
        {
            startEboxVideo();
        }

        // 开后台服务
        if (AppService.getIntance() == null)
        {
            startService(new Intent(this, AppService.class));
        }
    }

    private boolean isSystemTimeOk(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        if(year<2015)
        {
            return false;
        }
        return true;
    }

    public String isSystemOk() {

        String msg=null;
        boolean isOk=true;
        if (sb == null || !sb.portValid()) {
            msg="串口初始化中";
            isOk=false;
        }

       else if (!GlobalField.boxLocalInit) {
            msg="串口初始化中";
            isOk=false;
        }

        else if (getTerminal_code() == null || getTerminal_code().equals("")) {
            msg="配置信息错误";
            isOk=false;
        }

        else if (!SharePreferenceHelper.getInitBoxState()) {
            msg="本地格口数据未初始化";
            isOk=false;
        }
        else if (!SharePreferenceHelper.getCheckConfigState()) {
            msg="本地配置未完成校验";
            isOk=false;
        }
        else if (!SharePreferenceHelper.getSyncOrderState()) {
            msg="系统数据更新中,请您稍后再试!!";
            isOk=false;
        }

//        else if (!isSystemTimeOk()) {
//            msg="系统错误,请联系管理员处理!";
//            isOk=false;
//        }


        if (isOk) {
            msg="ok";
        }
        return msg;
    }

    public static AppApplication getInstance() {
        if (application == null) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return application;
    }

    @Override
    public void onTerminate() {
        Log.e(TAG, "onTerminate()");
        super.onTerminate();
    }

    public LogicSerialTask getSb() {
        if (sb == null) {
            portInit();
        }
        return sb;
    }

    public ExDataStorer getExDs() {
        if (exDs == null) {
            exDs = new ExDataStorer();
        }
        return exDs;
    }

    public PubDataStorer getPubDs() {
        if (pubDs == null) {
            pubDs = new PubDataStorer();
        }
        return pubDs;
    }

    public UmsDataStorer getUmsDs() {
        if (umsDs == null) {
            umsDs = new UmsDataStorer();
        }
        return umsDs;
    }

    private void portInit() {
        if (sb == null) {
            sb = new LogicSerialTask();
        }
        try {
            if (mSerialPort == null) {
                mSerialPort = new SerialPort();

                File ctrl485 = null;

                switch (GlobalField.config.getMaim_board()) {
                    case 0: {
                        ctrl485 = new File("/dev/ttyO1");
                        break;
                    }
                    case 1: {
                        ctrl485 = new File("/dev/ttyO3");
                        break;
                    }

                    case 2: {
                        ctrl485 = new File("/dev/ttyS3");
                        break;
                    }
                    default: {
                        ctrl485 = new File("/dev/ttyO1");
                        break;
                    }
                }
                mSerialPort.OpenPort(ctrl485, 4800, O_NDELAY);
                //全志
                //mSerialPort.OpenPort(new File("/dev/ttyS3"),4800,O_NDELAY);
                sb.setInputStream(mSerialPort.getInputStream());
                sb.setOutputStream(mSerialPort.getOutputStream());
                sb.setSerialPort(mSerialPort);
                if (GlobalField.config.getMaim_board() != MainBoardType.A31S_VER1) {
                    mSerialPort.initGpio();
                } else {
                    mSerialPort.initA31s();
                }

                //mSerialPort.setGpio(0);
                sb.setPortOk(true);
                //Toast.makeText(AppApplication.globalContext, "485初始化成功！", Toast.LENGTH_LONG).show();
            }

        } catch (SecurityException e) {
            String err = "485初始化失败！You do not have read/write permission to the serial port.";
            Log.e(TAG, "You do not have read/write permission to the serial port.");
            //Toast.makeText(AppApplication.globalContext, err, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            String err = "485初始化失败！You do not have read/write permission to the serial port.";
            Log.e(TAG, "The serial port can not be opened for an unknown reason.");
            //Toast.makeText(AppApplication.globalContext, err, Toast.LENGTH_LONG).show();
        } catch (InvalidParameterException e) {
            String err = "485初始化失败！Please configure your serial port first.";
            Log.e(TAG, "Please configure your serial port first.");
            //Toast.makeText(AppApplication.globalContext, err, Toast.LENGTH_LONG).show();
        }
    }

    private void portClose() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }

        if (sb != null) {
            sb = null;
        }
    }

    public void systemErrRestart() {
        // 485异常侦测
        if (GlobalField.portFailCnt > 10 || GlobalField.systemErr > 0) {
            AppApplication.getInstance().AppRestart();
        } else if (GlobalField.portFailCnt > 1) {
            AppApplication.getInstance().portRestart();
        }
    }


    public void portRestart() {
        Log.e(TAG, "port restart!!!");
        portClose();
        portInit();
    }

    public void AppRestart() {
        Log.e(TAG, "app restart!!!");
        AppApplication.getInstance().exitApp();
    }

    public CameraCtrl getCc() {
        if (cc == null) {
            camearInit();
        }
        return cc;
    }

    private void camearInit() {
        cc = new CameraCtrl(this);
    }

    private void cameraStop() {
        if (cc != null) {
            cc.stop();
            cc = null;
        }
    }

    public String getTerminal_code() {
        if (GlobalField.config == null || GlobalField.config.getTerminal_code() == null
                || GlobalField.config.getTerminal_code().equals("")) {
            return "";
        }

        return GlobalField.config.getTerminal_code();
    }

    private void readIni() {
        GlobalField.config = FileOp.getConfig();

        if (GlobalField.config == null || GlobalField.config.getTerminal_code() == null
                || GlobalField.config.getTerminal_code().equals("")) {
            /*AlertDialog.Builder ab = new AlertDialog.Builder(AppApplication.this);
            ab.setMessage(R.string.check_config);
			ab.setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
			ab.setNegativeButton(R.string.cancel, null);
			ab.show();*/
           // Toast.makeText(AppApplication.globalContext, getResources().getString(R.string.pub_check_config), Toast.LENGTH_LONG).show();
            Log.e(TAG, getResources().getString(R.string.pub_check_config));
        }

        GlobalField.temp = TempFile.getTemp();
    }

    public void addToActivityList(Activity activity) {
        activityList.add(activity);
    }

    public void removeFromActivityList(Activity activity) {
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).equals(activity)) {
                activityList.remove(i);
            }
        }
    }

    private void updateSyncState(){

        SharePreferenceHelper.saveSyncBoxDoorState(false);
        SharePreferenceHelper.saveSyncOrderState(false);
    }

    // 退出应用程序
    public void exitApp() {
      //  Toast.makeText(this,"重启APK中，稍等片刻...",Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                exit();
            }
        }).start();

    }

    public void home(){
        Intent homeIntent=new Intent(this, TopActivity.class);
        startActivity(homeIntent);
    }

    private void exit() {
        updateSyncState();
        // 退出所有Activity
        for (int i = 0; i < activityList.size(); i++)
        {
            if (activityList.get(i) instanceof CommonActivity)
            {
                ((CommonActivity) activityList.get(i)).saveParams();
            }
            activityList.get(i).finish();
        }
        activityList.clear();

        portClose();
        cameraStop();

        // 关闭后台服务
        if (AppService.getIntance() == null)
        {
            AppService.getIntance().stopSelf();
        }
        //System.exit(0);

        RingUtil.clear();

        SystemClock.sleep(3000);

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public  void restartOs()
    {
        if(GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1)
        {
            restartOsByA31s();
        }
        else
        {
            restartTIOs();
        }
    }

    private  void restartOsByA31s()
    {
        Log.i(TAG, "a31s restartOs");
        try {
            FileOutputStream fout = new FileOutputStream("/sys/devices/platform/hub_power/ctrl_reboot");

            fout.write("1".getBytes());
            fout.flush();
            fout.close();
            updateSyncState();
        } catch (FileNotFoundException e1) {
            Log.e(TAG, "ctrl_reboot not found"+e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "ctrl_reboot IOException"+e.getMessage());
            e.printStackTrace();
        }
    }

    private  void restartTIOs()
    {
        Log.i(TAG, "restartOs");
        Process su = null;
        try {
            su = SerialPort.getSu();
            String cmd;
            cmd = "reboot\n";
            su.getOutputStream().write(cmd.getBytes());
            updateSyncState();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LogicIdCardSerialTask getSi() {
        if (si == null) {
            idPortInit();
        }
        return si;
    }

    private void idPortInit() {
        if (si == null) {
            si = new LogicIdCardSerialTask();
        }
        try {
            if (mSerialPort1 == null) {
                mSerialPort1 = new SerialPort();

                //a31s
                if (GlobalField.config.getMaim_board() == MainBoardType.A31S_VER1) {
                    mSerialPort1.OpenPort(new File("/dev/ttyS4"), 115200, O_NDELAY);
                } else {
                    mSerialPort1.OpenPort(new File("/dev/ttyO2"), 115200, O_NDELAY);
                }

                //Ti
//

                si.setInputStream(mSerialPort1.getInputStream());
                si.setOutputStream(mSerialPort1.getOutputStream());
                si.setSerialPort(mSerialPort1);
                si.setPortOk(true);
            }

        } catch (SecurityException e) {
            String err = "身份证串口初始化失败！You do not have read/write permission to the serial port.";
            Log.e(TAG, "You do not have read/write permission to the serial port.");
        } catch (IOException e) {
            String err = "身份证串口初始化失败！You do not have read/write permission to the serial port.";
            Log.e(TAG, "The serial port can not be opened for an unknown reason.");
        } catch (InvalidParameterException e) {
            String err = "身份证串口初始化失败！Please configure your serial port first.";
            Log.e(TAG, "Please configure your serial port first.");
        }
    }

    private void idPortClose() {
        if (mSerialPort1 != null) {
            mSerialPort1.close();
            mSerialPort1 = null;
        }

        if (si != null) {
            si = null;
        }

    }

    private boolean processExist(String pack_name)
    {
        boolean isExist= false;

       ActivityManager activityManager= (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo app : runningAppProcesses) {
            if (app.processName.equals(pack_name))
            {
                isExist=true;
                break;
            }
        }


        return  isExist;
    }

    // 如果已经安装com.ebox.service
    public void startEboxLogService() {

        if (processExist("com.ebox.service"))
        {
            Log.e(GlobalField.tag,"ebox log service has exist ");
            return;
        }

        PackageManager pManager = this.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            String pkg = "com.ebox.service";
            if (pak.packageName.equals(pkg)) {
                //应用的主activity类
                try {
                    String cls = "com.ebox.service.ui.TopActivity";
                    if(pak.versionCode <=10)
                    {
                        cls = "com.ebox.ui.TopActivity";
                    }

                    ComponentName componet = new ComponentName(pkg, cls);
                    Intent intent = new Intent();
                    intent.setComponent(componet);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e)
                {
                    LogUtil.e("ActivityNotFoundException");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // 如果已经安装com.ebox.video，启动之
    public void startEboxVideo() {
        if (processExist("com.ebox.video"))
        {
            Log.e(GlobalField.tag,"ebox video has exist ");
            return;
        }
        PackageManager pManager = this.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            String pkg = "com.ebox.video";
            if (pak.packageName.equals(pkg)) {
                //应用的主activity类
                try {
                    String cls = "com.ebox.video.MainActivity";
                    ComponentName componet = new ComponentName(pkg, cls);
                    Intent intent = new Intent();
                    intent.setComponent(componet);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }  catch (ActivityNotFoundException e)
                {
                    LogUtil.e("ActivityNotFoundException");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public int getAppUid() {

        if (appUid==0)
        {
            appUid=getUID(application);
        }

        return appUid;
    }

    private int getUID(Context context){

        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        String pack=context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo process : runningAppProcesses) {
            if (process.processName.equals(pack))
            {
                return process.uid;
            }
        }
        return 0;
    }
}
