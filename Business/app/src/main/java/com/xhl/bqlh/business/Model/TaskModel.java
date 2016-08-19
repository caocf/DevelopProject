package com.xhl.bqlh.business.Model;

/**
 * Created by Sum on 16/4/18.
 */
public class TaskModel {

    private String retailerId;
    private int week;
    private int sort;
    private String chinkintime;
    private int checktype;
    private String companyName;
    private String address;
    private String position;
    private String retailerArea;
    private double coordinateX;
    private double coordinateY;
    private String liableName;
    private String liablePhone;

    public String getRetailerId() {
        return retailerId;
    }

    public int getWeek() {
        return week;
    }

    public int getSort() {
        return sort;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getArea() {
        return retailerArea;
    }

    public String getLiableName() {
        return liableName;
    }

    public String getLiablePhone() {
        return liablePhone;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public String getChinkintime() {
        return chinkintime;
    }

    public void setChinkintime(String chinkintime) {
        this.chinkintime = chinkintime;
    }

    public int getChecktype() {
        return checktype;
    }
}
