package com.ebox.pub.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;

import com.ebox.R;

public class TimeUtil {
	private static String getTimePartDisc(Context context, long howlong)
    {
        String s;
        if(howlong < 0)
            s = "";
        else
        if(howlong < 6*60*60*1000)
            s = context.getString(R.string.pub_fmt_dawn);
        else
        if(howlong < 12*60*60*1000)
            s = context.getString(R.string.pub_fmt_morning);
        else
        if(howlong < 13*60*60*1000)
            s = context.getString(R.string.pub_fmt_noon);
        else
        if(howlong < 18*60*60*1000)
            s = context.getString(R.string.pub_fmt_afternoon);
        else
            s = context.getString(R.string.pub_fmt_evening);
        return s;
    }
	
	public static String getTimeDisc(Context context, long time, boolean detail)
    {
        GregorianCalendar now = new GregorianCalendar();
        String obj = "";
        GregorianCalendar midnight = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        long howlong = time - midnight.getTimeInMillis();
        
        // 今天
        if(howlong > 0 && howlong <= 24*60*60*1000)
        {
        	obj = getTimePartDisc(context, howlong)+DateFormat.format("kk:mm", time);
        } 
        else
        {
            long howlongyes = 24*60*60*1000 + (time - midnight.getTimeInMillis());
            // 昨天
            if(howlongyes > 0 && howlongyes <= 24*60*60*1000)
            {
                if(detail)
                	obj = context.getString(R.string.pub_fmt_pre_yesterday)+" "+getTimePartDisc(context, howlongyes)+DateFormat.format("kk:mm", time);
                else
                	obj = context.getString(R.string.pub_fmt_pre_yesterday)+" "+getTimePartDisc(context, howlongyes);
            } 
            else
            {
                GregorianCalendar inputTime = new GregorianCalendar();
                inputTime.setTimeInMillis(time);
                // 这个星期
                if(now.get(Calendar.YEAR) == inputTime.get(Calendar.YEAR) && 
                		now.get(Calendar.WEEK_OF_YEAR) == inputTime.get(Calendar.WEEK_OF_YEAR))
                {
                    obj = DateFormat.format("E ", inputTime)+getTimePartDisc(context, 60*60*1000 * (long)inputTime.get(Calendar.HOUR_OF_DAY));
                    if(detail)
                        obj = obj+DateFormat.format("kk:mm", time);
                } 
                // 今年
                else if(now.get(Calendar.YEAR) == inputTime.get(Calendar.YEAR))
                {
                    if(detail)
                    {
                    	String partDisc = getTimePartDisc(context, 60*60*1000 * (long)inputTime.get(Calendar.HOUR_OF_DAY));
                        obj = DateFormat.format("M月d日 "+partDisc+"kk:mm".toString(), time)+"";
                    } 
                    else
                    {
                        obj = DateFormat.format("M月d日", time)+"";
                    }
                } 
                else if(!detail)
                {
                    obj = DateFormat.format("yyyy年M月d日", time)+"";
                } 
                else
                {
                	String partDisc = getTimePartDisc(context, 60*60*1000 * (long)inputTime.get(Calendar.HOUR_OF_DAY));
                    obj = DateFormat.format("yyyy年M月d日 "+partDisc+"kk:mm", time)+"";
                }
            }
        }
        return obj;
    }
	
	public static String currentTime()
	 {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
	                .format(new Date()).toString();
	 }


    /**
     * 本地订单生成时间
     * @return
     */
    public static String orderTime()
    {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()).toString();
    }


    public static  Long StringTimeToLong(String time){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
	 
}
