package com.moge.ebox.phone.utils;

import android.util.Log;

public final class LogUtil
{
    private static final String DEBUG_TAG = "debug";
    private static final boolean DEBUG = true;

    private static String format(String args)
    {
        StackTraceElement tr = new Throwable().getStackTrace()[2];
        return ("[" + tr.getClassName() + "." + tr.getMethodName() + ":"
                + tr.getLineNumber() + "] " + args + "\n");
    }

    public static void i(String tag, String info)
    {
        if (DEBUG)
        {
            Log.i(tag, format(info));
        }
    }

    public static void d(String tag, String info)
    {
        if (DEBUG)
        {
            Log.d(tag, format(info));
        }
    }

    public static void w(String tag, String warn)
    {
        if (DEBUG)
        {
            Log.w(tag, format(warn));
        }
    }

    public static void e(String tag, String error)
    {
        if (DEBUG)
        {
            Log.e(tag, format(error));
        }
    }

    public static void i(String info)
    {
        i(DEBUG_TAG, format(info));
    }

    public static void d(String debug)
    {
        d(DEBUG_TAG, format(debug));
    }

    public static void w(String warn)
    {
        w(DEBUG_TAG, format(warn));
    }

    public static void e(String error)
    {
        e(DEBUG_TAG, format(error));
    }

    public static void logException(Throwable e)
    {
        if (DEBUG)
        {
            e.printStackTrace();
        }
    }

    public static void jw(Object object, Throwable tr)
    {
        if (DEBUG)
        {
            Log.w(getPureClassName(object), "", filterThrowable(tr));
        }
    }

    private static Throwable filterThrowable(Throwable tr)
    {
        StackTraceElement[] ste = tr.getStackTrace();
        tr.setStackTrace(new StackTraceElement[] { ste[0] });
        return tr;
    }

    private static String getPureClassName(Object object)
    {
        if (object == null)
        {
            Log.e(DEBUG_TAG, "getPureClassName() : object is null.");
        }
        String name = object.getClass().getName();
        if ("java.lang.String".equals(name))
        {
            return object.toString();
        }
        int idx = name.lastIndexOf('.');
        if (idx > 0)
        {
            return name.substring(idx + 1);
        }
        return name;
    }
}
