package com.moge.gege.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class EnviromentUtil
{
    private static final String APK = "/apk";
    private static final String APP = "/gege";
    private static final String CACHE = "/cache";
    private static final String IMAGE = "/image";
    private static final String LOG = "/log";

    public static boolean checkSDCard()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static File getAppDirectory(Context context)
    {
        return mkdir(getRootPath(context) + APK);
    }

    public static String getCacheDirectory(Context context)
    {
        mkdir(getRootPath(context) + CACHE);
        return getRootPath(context) + CACHE;
    }

    public static String getImageStorePath()
    {
        final String storePath = Environment.getExternalStorageDirectory()
                + "/gege/";
        FileUtil.isFolderExists(storePath);
        return storePath;
    }

    public static String getExternalPhotoSavePath(String path)
    {
        File localFile = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                path);
        if (!localFile.exists())
            localFile.mkdirs();
        return localFile.getAbsolutePath();
    }

    public static File getImageDirectory(Context context)
    {
        return mkdir(getRootPath(context) + IMAGE);
    }

    public static File getLogDirectory(Context context)
    {
        return mkdir(getRootPath(context) + LOG);
    }

    public static String getRootPath(Context context)
    {
        return getSDPath(context) + APP;
    }

    private static String getSDPath(Context context)
    {
        // if (checkSDCard())
        {
            return Environment.getExternalStorageDirectory().getPath();
        }
        // else
        // {
        // return context.getFilesDir().getAbsolutePath();
        // }
    }

    public static File mkdir(String paramString)
    {
        File localFile = new File(paramString);
        if (!localFile.exists())
            localFile.mkdirs();
        return localFile;
    }

}
