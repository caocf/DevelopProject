package com.xhl.world.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sum on 15/12/1.
 */
public class TimeCountHelper {
    /**
     * 计算当前时间到目标时间剩余到时间
     *
     * @param time 目标时间 ex:2015-12-1 16:00:00
     * @return 计算当前时间到目标时间到剩余时间
     */

    public static long countLeftTime(String time) {
        if (time.length() < 10) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long liefTime = 0;

        try {
            Date date = format.parse(time);
            long target = date.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            //Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GTM+8"), Locale.CHINA);
            //  currentTimeMillis = instance.getTimeInMillis();
            liefTime = target - currentTimeMillis;


            if (liefTime <= 0) {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return liefTime;
    }


    /**
     * 获取当天剩余时间
     *
     * @return
     */
    public static long countTodayLeftTime() {

        long liefTime;

        Calendar instance = Calendar.getInstance();

        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);

        long timeInMillis = instance.getTimeInMillis();

        long currentTimeMillis = System.currentTimeMillis();

        liefTime = timeInMillis - currentTimeMillis;

        return liefTime;
    }

    private String getDataSt(long time) {
        return "";
    }

    public static void getLeftTime(long leftTime) {

        int second = (int) (leftTime / 1000);

        int day = second / 86400;

        int hour = second / 3600;

        int leftSecond = second % 3600;

        int min = leftSecond / 60;

        int sec = leftSecond % 60;


        // System.out.print("day:" + day + " hour:" + hour + " min:" + min + " sec:" + leftS +"\n");
    }

    public static String checkTime(int time) {

        if (time == 0) {
            return "00";
        }
        if (time < 10) {
            return "0" + time;
        }
        return "" + time;
    }

   /* public static void main(String[] args) {
       *//* long leftTime = countLeftTime("2015-12-2 23:00:00");
//        System.out.print("lefttime:" + leftTime);
        while (true) {

            try {
                leftTime -=1000;
                getLeftTime(leftTime);
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*//*

       // countLeftTime();
    }*/

}
