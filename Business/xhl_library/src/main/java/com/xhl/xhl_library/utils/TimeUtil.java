package com.xhl.xhl_library.utils;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    public static final String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四",
            "星期五", "星期六"};

    public static String dateToWeek(int i) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        cal.setTime(nextDate(i));
        int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    public static String dateToWeek(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        cal.setTime(date);
        int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = dateFormat.parse(datetime);
            calendar.setTime(date);
        } catch (ParseException e) {
        }
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    public static Date nextDate(int i) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        date = cal.getTime();
        return date;
    }

    public static Date nextDate(Date baseDate, int dayInteval) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.add(Calendar.DAY_OF_YEAR, dayInteval);
        date = cal.getTime();
        return date;
    }

    public static Date nextDate(String baseDate, int dayInteval) {
        Date date = getDateFromString(baseDate);
        return nextDate(date, dayInteval);
    }

    public static String getNormalStringDate(Date date) {
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }

    public static String getNormalStringDate(String format, Date date) {
        return DateFormat.format(format, date).toString();
    }

    public static String getStringTime(Date date) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString();
    }

    public static Date getDateFromString(String date) {
        Date realDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            realDate = dateFormat.parse(date);
        } catch (ParseException e) {
        }
        return realDate;
    }

    public static Date getDateFromString(String date, String fromat) {
        Date realDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                fromat, Locale.CHINA);
        try {
            realDate = dateFormat.parse(date);
        } catch (ParseException e) {
        }
        return realDate;
    }

    public static String currentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                .format(new Date()).toString();
    }

    public static String currentTime(String dateFormate) {
        return new SimpleDateFormat(dateFormate, Locale.CHINA)
                .format(new Date()).toString();
    }

    public static String currentTimeDay() {
        return new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date()).toString();
    }

    public static Date currentDate() {
        Date nowDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            nowDate = dateFormat.parse(currentTime());
        } catch (ParseException e) {
        }
        return nowDate;
    }

    public static long getDataDuration(String beginDate, String endDate) {
        Date fromDate = null;
        Date toDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            fromDate = dateFormat.parse(beginDate);
            toDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
        }

        long milliSecond = toDate.getTime() - fromDate.getTime();

        return milliSecond / 1000;
    }

    public static Date getEndDate(String time) {
        Date endDate = null;
        if (!TextUtils.isEmpty(time)) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                endDate = dateFormat.parse(time);
            } catch (ParseException e) {
            }
        }
        return endDate;
    }


    public static String getDateTimeStr(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(
                new Date(time)).toString();
    }

    public static String getDataStr(long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(
                new Date(time)).toString();
    }

    public static String getTimeStr(long time) {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(
                new Date(time)).toString();
    }

    public static String timeToNow(String dateTime) {
        Date date = getEndDate(dateTime);
        return getTimeAgoString(date);
    }

    public static String timeToNow(long secondTime) {
        long time = secondTime * 1000;
        Date datetime = new Date(time);
        if (datetime == null) {
            return "未知";
        }

        return getTimeAgoString(datetime);
    }

    private static String getTimeAgoString(Date datetime) {
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        // String curDate = dateFormater2.get().format(cal.getTime());
        // String paramDate = dateFormater2.get().format(time);
        // if (curDate.equals(paramDate))
        // {
        // int hour = (int) ((cal.getTimeInMillis() - time.getTime()) /
        // 3600000);
        // if (hour == 0)
        // ftime = Math.max(
        // (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
        // + "分钟前";
        // else
        // ftime = hour + "小时前";
        // return ftime;
        // }

        long lt = datetime.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - datetime.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math
                        .max((cal.getTimeInMillis() - datetime.getTime()) / 60000,
                                1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 5) {
            ftime = days + "天前";
        } else if (days > 5) {
            ftime = getStringTime(datetime);
        }
        return ftime;
    }
}
