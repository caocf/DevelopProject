package com.ebox.ex.network.model.base.type;

import java.util.List;

/**
 * Created by Android on 2015/9/2.
 */
public class ServiceConfig {

    private Integer heartbeat;
    private Integer rebootDay;
    private Integer is_account;// 0不计费，1计费
    private Integer pickup_type;// 0 不等待，1 等待
    private Integer is_open_gege;// 0,不显示，1 显示
    private List<ExpressCharge> express_charge;
    private List<AdminUserType> userList;
    private List<TerminalActivity> activity;
    private String system_time;

    private Integer is_scan_pickup; // 是否需要扫码开柜

    public Integer isScanPickup() {
        return is_scan_pickup;
    }

    public void setScanPickup(Integer isScanPickup) {
        this.is_scan_pickup = isScanPickup;
    }

    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Integer getRebootDay() {
        return rebootDay;
    }

    public void setRebootDay(Integer rebootDay) {
        this.rebootDay = rebootDay;
    }

    public List<ExpressCharge> getExpressCharge() {
        return express_charge;
    }

    public void setExpressCharge(List<ExpressCharge> expressCharge) {
        this.express_charge = expressCharge;
    }

    public Integer getIsAccount() {
        return is_account;
    }

    public void setIsAccount(Integer isAccount) {
        this.is_account = isAccount;
    }

    public Integer getpickupType() {
        return pickup_type;
    }

    public void setpickupType(Integer pickupType) {
        this.pickup_type = pickupType;
    }

    public Integer getIsOpenGeGe() {
        return is_open_gege;
    }

    public void setIsOpenGeGe(Integer isOpenGeGe) {
        this.is_open_gege = isOpenGeGe;
    }

    public List<AdminUserType> getUserList() {
        return userList;
    }

    public void setUserList(List<AdminUserType> userList) {
        this.userList = userList;
    }

    public List<TerminalActivity> getActivity() {
        return activity;
    }

    public void setActivity(List<TerminalActivity> activity) {
        this.activity = activity;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }
}
