package com.moge.ebox.phone.bettle.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;

import tools.ImageCacheUtil;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.baidu.frontia.FrontiaApplication;
import com.vikaa.baseapp.R;

public class AppContext extends FrontiaApplication
{
  private int requestCode = 0;

  public final double SCREENSIZE = 6.0D;
  public static final int NETTYPE_WIFI = 1;
  public static final int NETTYPE_CMWAP = 2;
  public static final int NETTYPE_CMNET = 3;
  public static final int PAGE_SIZE = 20;
  private static final int CACHE_TIME = 3600000;
  private Hashtable<String, Object> memCacheRegion = new Hashtable();

  private Handler unLoginHandler = new Handler() {
    public void handleMessage(Message msg) {
      if (msg.what == 1)
        UIHelper.ToastMessage(AppContext.this, AppContext.this.getString(R.string.msg_login_error), 0);
    }
  };

  public void onCreate()
  {
    super.onCreate();
    ImageCacheUtil.init(this);
    Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
  }

  public boolean isAudioNormal()
  {
    AudioManager mAudioManager = (AudioManager)getSystemService("audio");
    return mAudioManager.getRingerMode() == 2;
  }

  public boolean isAppSound()
  {
    return (isAudioNormal()) && (isVoice());
  }

  public boolean isNetworkConnected()
  {
    ConnectivityManager cm = (ConnectivityManager)getSystemService("connectivity");
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return (ni != null) && (ni.isConnectedOrConnecting());
  }

  public int getNetworkType()
  {
    int netType = 0;
    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService("connectivity");
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if (networkInfo == null) {
      return netType;
    }
    int nType = networkInfo.getType();
    if (nType == 0) {
      String extraInfo = networkInfo.getExtraInfo();
      if (!StringUtils.empty(extraInfo)) {
        if (extraInfo.toLowerCase().equals("cmnet"))
          netType = 3;
        else
          netType = 2;
      }
    }
    else if (nType == 1) {
      netType = 1;
    }
    return netType;
  }

  public static boolean isMethodsCompat(int VersionCode)
  {
    int currentVersion = Build.VERSION.SDK_INT;
    return currentVersion >= VersionCode;
  }

  public PackageInfo getPackageInfo()
  {
    PackageInfo info = null;
    try {
      info = getPackageManager().getPackageInfo(getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace(System.err);
    }
    if (info == null) info = new PackageInfo();
    return info;
  }

  public String getAppId()
  {
    String uniqueID = getProperty("APP_UNIQUEID");
    if (StringUtils.empty(uniqueID)) {
      uniqueID = UUID.randomUUID().toString();
      setProperty("APP_UNIQUEID", uniqueID);
    }
    return uniqueID;
  }

  public void saveScreenSize(double screenSize)
  {
    setProperties(new Properties()
    {
    });
  }

  public double getScreenSize()
  {
    return Double.valueOf(getProperty("screensize")).doubleValue();
  }

  public void saveRegId(String Regid) {
    setProperties(new Properties()
    {
    });
  }

  public String getRegId()
  {
    String RegId = "";
    RegId = getProperty("gcm.regid");
    Logger.i(RegId);
    return RegId;
  }

  public void setPHPSESSID(String sessid)
  {
    setProperties(new Properties()
    {
    });
  }

  public String getPHPSESSID()
  {
    String PHPSESSID = getProperty("user.phpsessid");
    return PHPSESSID;
  }

  public Handler getUnLoginHandler()
  {
    return this.unLoginHandler;
  }

  public int getRequestCode()
  {
    return this.requestCode;
  }

  public void setRequestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  public boolean isAudioWorked()
  {
    ActivityManager myManager = (ActivityManager)getApplicationContext().getSystemService("activity");
    ArrayList runningService = (ArrayList)myManager.getRunningServices(30);
    for (int i = 0; i < runningService.size(); i++) {
      if (((ActivityManager.RunningServiceInfo)runningService.get(i)).service.getClassName().toString().equals("com.momoka.app.audio.DisplayService")) {
        return true;
      }
    }
    return false;
  }

  public Bitmap getUserFace(String key)
    throws AppException
  {
    FileInputStream fis = null;
    try {
      fis = openFileInput(key);
      return BitmapFactory.decodeStream(fis);
    } catch (Exception e) {
      throw AppException.run(e);
    } finally {
      try {
        fis.close();
      }
      catch (Exception localException2)
      {
      }
    }
  }

  public boolean isLoadImage()
  {
    String perf_loadimage = getProperty("perf_loadimage");

    if (StringUtils.empty(perf_loadimage)) {
      return true;
    }
    return StringUtils.toBool(perf_loadimage);
  }

  public void setConfigLoadimage(boolean b)
  {
    setProperty("perf_loadimage", String.valueOf(b));
  }

  public boolean isVoice()
  {
    String perf_voice = getProperty("perf_voice");

    if (StringUtils.empty(perf_voice)) {
      return true;
    }
    return StringUtils.toBool(perf_voice);
  }

  public void setConfigVoice(boolean b)
  {
    setProperty("perf_voice", String.valueOf(b));
  }

  public boolean isCheckUp()
  {
    String perf_checkup = getProperty("perf_checkup");

    if (StringUtils.empty(perf_checkup)) {
      return true;
    }
    return StringUtils.toBool(perf_checkup);
  }

  public void setConfigCheckUp(boolean b)
  {
    setProperty("perf_checkup", String.valueOf(b));
  }

  public boolean isScroll()
  {
    String perf_scroll = getProperty("perf_scroll");

    if (StringUtils.empty(perf_scroll)) {
      return false;
    }
    return StringUtils.toBool(perf_scroll);
  }

  public void setConfigScroll(boolean b)
  {
    setProperty("perf_scroll", String.valueOf(b));
  }

  public boolean isHttpsLogin()
  {
    String perf_httpslogin = getProperty("perf_httpslogin");

    if (StringUtils.empty(perf_httpslogin)) {
      return false;
    }
    return StringUtils.toBool(perf_httpslogin);
  }

  public void setConfigHttpsLogin(boolean b)
  {
    setProperty("perf_httpslogin", String.valueOf(b));
  }

  public void cleanCookie()
  {
    removeProperty(new String[] { "cookie" });
  }

  private boolean isReadDataCache(String cachefile)
  {
    return readObject(cachefile) != null;
  }

  private boolean isExistDataCache(String cachefile)
  {
    boolean exist = false;
    File data = getFileStreamPath(cachefile);
    if (data.exists())
      exist = true;
    return exist;
  }

  public boolean isCacheDataFailure(String cachefile)
  {
    boolean failure = false;
    File data = getFileStreamPath(cachefile);
    if ((data.exists()) && (System.currentTimeMillis() - data.lastModified() > 3600000L))
      failure = true;
    else if (!data.exists())
      failure = true;
    return failure;
  }

  public void clearAppCache()
  {
    deleteDatabase("webview.db");
    deleteDatabase("webview.db-shm");
    deleteDatabase("webview.db-wal");
    deleteDatabase("webviewCache.db");
    deleteDatabase("webviewCache.db-shm");
    deleteDatabase("webviewCache.db-wal");

    clearCacheFolder(getFilesDir(), System.currentTimeMillis());
    clearCacheFolder(getCacheDir(), System.currentTimeMillis());

    if (isMethodsCompat(8)) {
      clearCacheFolder(MethodsCompat.getExternalCacheDir(this), System.currentTimeMillis());
    }

    Properties props = getProperties();
    for (Iterator localIterator = props.keySet().iterator(); localIterator.hasNext(); ) { Object key = localIterator.next();
      String _key = key.toString();
      if (_key.startsWith("temp"))
        removeProperty(new String[] { _key });
    }
  }

  private int clearCacheFolder(File dir, long curTime)
  {
    int deletedFiles = 0;
    if ((dir != null) && (dir.isDirectory())) {
      try {
        for (File child : dir.listFiles()) {
          if (child.isDirectory()) {
            deletedFiles += clearCacheFolder(child, curTime);
          }
          if ((child.lastModified() < curTime) && 
            (child.delete()))
            deletedFiles++;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return deletedFiles;
  }

  public void setMemCache(String key, Object value)
  {
    this.memCacheRegion.put(key, value);
  }

  public Object getMemCache(String key)
  {
    return this.memCacheRegion.get(key);
  }

  public void setDiskCache(String key, String value)
    throws IOException
  {
    FileOutputStream fos = null;
    try {
      fos = openFileOutput("cache_" + key + ".data", 0);
      fos.write(value.getBytes());
      fos.flush();
    } finally {
      try {
        fos.close();
      }
      catch (Exception localException)
      {
      }
    }
  }

  public String getDiskCache(String key)
    throws IOException
  {
    FileInputStream fis = null;
    try {
      fis = openFileInput("cache_" + key + ".data");
      byte[] datas = new byte[fis.available()];
      fis.read(datas);
      return new String(datas);
    } finally {
      try {
        fis.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public boolean saveObject(Serializable ser, String file)
  {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      fos = openFileOutput(file, 0);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(ser);
      oos.flush();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        oos.close(); } catch (Exception localException5) {
      }
      try {
        fos.close();
      }
      catch (Exception localException6)
      {
      }
    }
  }

  public Serializable readObject(String file)
  {
    if (!isExistDataCache(file))
      return null;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      fis = openFileInput(file);
      ois = new ObjectInputStream(fis);
      return (Serializable)ois.readObject();
    } catch (FileNotFoundException localFileNotFoundException) {
    }
    catch (OutOfMemoryError localOutOfMemoryError) {
    }
    catch (Exception e) {
      e.printStackTrace();

      if ((e instanceof InvalidClassException)) {
        File data = getFileStreamPath(file);
        data.delete();
      }
    } finally {
      try {
        ois.close(); } catch (Exception localException9) {
      }
      try {
        fis.close(); } catch (Exception localException10) {
      }
    }
    return null;
  }

  public boolean containsProperty(String key) {
    Properties props = getProperties();
    return props.containsKey(key);
  }

  public void setProperties(Properties ps) {
    AppConfig.getAppConfig(this).set(ps);
  }

  public Properties getProperties() {
    return AppConfig.getAppConfig(this).get();
  }

  public void setProperty(String key, String value) {
    AppConfig.getAppConfig(this).set(key, value);
  }

  public String getProperty(String key) {
    return AppConfig.getAppConfig(this).get(key);
  }
  public void removeProperty(String[] key) {
    AppConfig.getAppConfig(this).remove(key);
  }
}