package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.utils.TimeUtil;

import java.util.Date;

/**
 * Created by Sum on 16/4/28.
 */
public class SignRecordModel {

    public int curDay;
    public boolean enterPoint;//是否进入签到点

    private String salesId;    //业务员id

    private String workTime;   //上班时间

    private String dutyTime;   //下班时间

    private int day;//一个月第几天

    private int workStatus; //上班状态：1正常，2迟到，3未打卡

    private int dutyStatus; //下班状态：1正常，2早退，3未打卡

    private String workPlace; //上班地点

    private String dutyPlace; //下班地点

    private int type;         //打卡类型		1、上班时间 2、下班时间

    private String workOnTime;        //经销商设置上班时间

    private String dutyUpTime;        //经销商设置下班时间

    private String workCoordinateX;
    private String workCoordinateY;
    private String dutyCoordinateX;
    private String dutyCoordinateY;

    public int getWorkStatus() {
        return workStatus;
    }

    public int getDutyStatus() {
        return dutyStatus;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getDutyPlace() {
        return dutyPlace;
    }

    public void setDutyPlace(String dutyPlace) {
        this.dutyPlace = dutyPlace;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWorkOnTime() {
        return workOnTime;
    }

    public void setWorkOnTime(String workOnTime) {
        this.workOnTime = workOnTime;
    }

    public String getDutyUpTime() {
        return dutyUpTime;
    }

    public void setDutyUpTime(String dutyUpTime) {
        this.dutyUpTime = dutyUpTime;
    }

    public String getWorkCoordinateX() {
        return workCoordinateX;
    }

    public void setWorkCoordinateX(String workCoordinateX) {
        this.workCoordinateX = workCoordinateX;
    }

    public String getWorkCoordinateY() {
        return workCoordinateY;
    }

    public void setWorkCoordinateY(String workCoordinateY) {
        this.workCoordinateY = workCoordinateY;
    }

    public String getDutyCoordinateX() {
        return dutyCoordinateX;
    }

    public void setDutyCoordinateX(String dutyCoordinateX) {
        this.dutyCoordinateX = dutyCoordinateX;
    }

    public String getDutyCoordinateY() {
        return dutyCoordinateY;
    }

    public void setDutyCoordinateY(String dutyCoordinateY) {
        this.dutyCoordinateY = dutyCoordinateY;
    }

    public Date signDate() {
//        if (date != 0) {
//            return new Date(date);
//        }
        String time;
        if (!TextUtils.isEmpty(workTime)) {
            time = workTime;
        } else {
            time = dutyTime;
        }
        return TimeUtil.getDateFromString(time);
    }

    public String getLastSignLocation() {
        if (!TextUtils.isEmpty(workPlace)) {
            return workPlace;
        } else {
            return dutyPlace;
        }
    }

    //1正常，2迟到，3 正常+地点异常 4 迟到+地点异常
    public String signWorkState() {
        if (workStatus == 1) {
            return "正常";
        } else if (workStatus == 2) {
            return "迟到";
        } else if (workStatus == 3) {
            return "正常(地点异常)";
        } else if (workStatus == 4) {
            return "迟到(地点异常)";
        } else {
            return "未打卡";
        }
    }

    public int signWorkStateBg() {
        if (workStatus == 1 || workStatus == 3) {
            return R.color.base_light_text_color;
        } else {
            return R.color.app_red;
        }
    }

    //下班状态：1正常，2早退
    public String signDutyState() {
        if (dutyStatus == 1) {
            return "正常";
        } else if (dutyStatus == 2) {
            return "早退";
        } else if (dutyStatus == 3) {
            return "正常(地点异常)";
        } else if (dutyStatus == 4) {
            return "早退(地点异常)";
        } else {
            return "未打卡";
        }
    }

    public int signDutyStateBg() {
        if (dutyStatus == 1 || dutyStatus == 3) {
            return R.color.base_light_text_color;
        } else {
            return R.color.app_red;
        }
    }

    public int getDay() {
        return day;
    }
}
