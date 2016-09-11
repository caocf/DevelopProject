package com.moge.ebox.phone.bettle.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.Window;
import java.io.File;

public class MethodsCompat
{
  @TargetApi(5)
  public static void overridePendingTransition(Activity activity, int enter_anim, int exit_anim)
  {
    activity.overridePendingTransition(enter_anim, exit_anim);
  }

  @TargetApi(7)
  public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
    return MediaStore.Images.Thumbnails.getThumbnail(cr, origId, kind, options);
  }

  @TargetApi(8)
  public static File getExternalCacheDir(Context context)
  {
    File extCacheDir = new File(Environment.getExternalStorageDirectory(), 
      "/Android/data/" + context.getApplicationInfo().packageName + "/cache/");
    extCacheDir.mkdirs();
    return extCacheDir;
  }

  @TargetApi(11)
  public static void recreate(Activity activity) {
    if (Build.VERSION.SDK_INT >= 11)
      activity.recreate();
  }

  @TargetApi(11)
  public static void setLayerType(View view, int layerType, Paint paint)
  {
    if (Build.VERSION.SDK_INT >= 11)
      view.setLayerType(layerType, paint);
  }

  @TargetApi(14)
  public static void setUiOptions(Window window, int uiOptions)
  {
    if (Build.VERSION.SDK_INT >= 14)
      window.setUiOptions(uiOptions);
  }
}
