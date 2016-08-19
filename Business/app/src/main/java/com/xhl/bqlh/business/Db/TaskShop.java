package com.xhl.bqlh.business.Db;

import com.xhl.bqlh.business.Model.Type.TaskType;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Sum on 16/4/18.
 * <p/>
 * 业务员的任务表
 */
@Table(name = "task_shop")
public class TaskShop {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "week")
    private int week;//星期几

    @Column(name = "index")
    private int index;//每天的任务索引

    @Column(name = "shopId")
    private String shopId;

    @Column(name = "liableName")
    private String liableName;

    @Column(name = "liablePhone")
    private String liablePhone;

    @Column(name = "shopName")
    private String shopName;

    @Column(name = "shopCity")
    private String shopCity;

    @Column(name = "shopAddress")
    private String shopAddress;

    @Column(name = "shopLocation")
    private String shopLocation;

    @Column(name = "shopLatitude")
    private double shopLatitude;

    @Column(name = "shopLongitude")
    private double shopLongitude;

    @Column(name = "data1")
    private String data1 = "";//店铺状态 @ShopStateType

    @Column(name = "data2")
    private String data2 = "";

    @Column(name = "data3")
    private String data3 = "";

    private int taskType = TaskType.TYPE_Normal;//默认正常任务

    private int taskState = TaskType.STATE_START;//默认添加后开始任务

    private int taskId = -1;//每天添加执行任务的id

    private int dayInterval = -1;//和今天日期区间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public double getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public double getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(double shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDayInterval() {
        return dayInterval;
    }

    public void setDayInterval(int dayInterval) {
        this.dayInterval = dayInterval;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getLiableName() {
        return liableName;
    }

    public void setLiableName(String liableName) {
        this.liableName = liableName;
    }

    public String getLiablePhone() {
        return liablePhone;
    }

    public void setLiablePhone(String liablePhone) {
        this.liablePhone = liablePhone;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    @Override
    public String toString() {
        return "TaskShop{" +
                "id=" + id +
                ", week=" + week +
                ", index=" + index +
                ", shopId='" + shopId + '\'' +
                ", liableName='" + liableName + '\'' +
                ", liablePhone='" + liablePhone + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopCity='" + shopCity + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopLocation='" + shopLocation + '\'' +
                ", shopLongitude=" + shopLongitude +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                ", data3='" + data3 + '\'' +
                ", shopLatitude=" + shopLatitude +
                '}';
    }
}
