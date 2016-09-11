package com.moge.gege.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;

public class FunctionUtil
{
    public static final String HTTP_WAP = "wap";

    private static long lastClickTime;

    public static int dip2px(Context context, double d)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }

    public static void shakeAnimation(Context context, View v)
    {
        // Animation shake = AnimationUtils.loadAnimation(context,
        // R.anim.shake);
        // v.startAnimation(shake);
    }

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick(int millisecond)
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < millisecond)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static int parseIntByString(String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0;
        }
    }

    public static float parseFloatByString(String string)
    {
        try
        {
            return Float.parseFloat(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0.00f;
        }
    }

    public static double parseDoubleByString(String string)
    {
        try
        {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e)
        {
            LogUtil.logException(e);
            return 0.00;
        }
        catch (Exception e)
        {
            LogUtil.logException(e);
            return 0.00;
        }
    }

    public static String parseNull(String string)
    {
        return null == string ? "" : string;
    }

    public static String getDouble(double data)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(data);
    }

    public static String getDouble(String data)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(parseDoubleByString(data));
    }

    public static String fixNumber(int number)
    {
        if (number < 10)
        {
            return "0" + number;
        }
        else
        {
            return String.valueOf(number);
        }
    }

    // 12345 -> 12,345
    public static String formatPoolNum(String pools)
    {
        int length = pools.length();
        StringBuilder sb = new StringBuilder();
        int start;
        int num;

        if (length <= 3)
        {
            return pools;
        }

        start = length % 3;
        if (start == 0)
        {
            num = length / 3;
            start = 3;
        }
        else
        {
            num = length / 3 + 1;
        }

        sb.append(pools.substring(0, start));
        start = sb.length();

        for (int i = 1; i < num; i++)
        {
            sb.append(",");
            sb.append(pools.substring(start, start + 3));
            start = start + 3;
        }

        return sb.toString();
    }

    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static SpannableStringBuilder highLight(String text,
            String delimiters, int colorRed, int colorBlue)
    {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);// ���ڿɱ��ַ���
        CharacterStyle spanRed = null;
        CharacterStyle spanBlue = null;
        int end = text.indexOf(delimiters);
        if (end == -1)
        {
            spanRed = new ForegroundColorSpan(colorRed);
            spannable.setSpan(spanRed, 0, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else
        {
            spanRed = new ForegroundColorSpan(colorRed);
            spanBlue = new ForegroundColorSpan(colorBlue);
            spannable.setSpan(spanRed, 0, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(spanBlue, end + 1, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.replace(end, end + 1, " ");
        }
        return spannable;
    }

}
