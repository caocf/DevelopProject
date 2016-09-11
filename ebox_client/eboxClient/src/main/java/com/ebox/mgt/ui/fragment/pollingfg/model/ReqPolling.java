package com.ebox.mgt.ui.fragment.pollingfg.model;

import com.ebox.ex.network.model.base.BaseReq;

/**
 * Created by prin on 2015/9/29.
 */
public class ReqPolling extends BaseReq {

    private String super_name;  //管理员用户名
    private String pressureState;    //220V电压状态
    private String elecdegree;  //电表度数
    private String discVcrState;    //硬盘录像机状态
    private String monitorCamState; //监控摄像头状态
    private String netTest; //网络测试
    private String netRouteModuleState; //3G路由器模块状态
    private String netLineState;    //3G天线状态
    private String netRouteState;   //网络路由器状态
    private String startLockNum;    //柜门螺丝松动数量
    private String lockLockNum; //处理业务锁定的锁数量
    private String changeLock;  //更换硬件故障的锁数量
    private String audioTest;   //喇叭测试
    private String scanTest;    //扫描器测试
    private String cameraTest;  //摄像头测试
    private String overtimeNum; //超时件
    private String isClear;    //是否清洁柜体
    private String isTestScreen;    //是否进行屏幕测试
    private String isTestTouch; //是否进行触屏测试


    public String getCameraTest() {
        return cameraTest;
    }

    public void setCameraTest(String cameraTest) {
        this.cameraTest = cameraTest;
    }

    public String getIsClear() {
        return isClear;
    }

    public void setIsClear(String isClear) {
        this.isClear = isClear;
    }

    public String getIsTestScreen() {
        return isTestScreen;
    }

    public void setIsTestScreen(String isTestScreen) {
        this.isTestScreen = isTestScreen;
    }

    public String getIsTestTouch() {
        return isTestTouch;
    }

    public void setIsTestTouch(String isTestTouch) {
        this.isTestTouch = isTestTouch;
    }

    public String getSuper_name() {
        return super_name;
    }

    public void setSuper_name(String super_name) {
        this.super_name = super_name;
    }

    public String getPressureState() {
        return pressureState;
    }

    public void setPressureState(String pressureState) {
        this.pressureState = pressureState;
    }

    public String getElecdegree() {
        return elecdegree;
    }

    public void setElecdegree(String elecdegree) {
        this.elecdegree = elecdegree;
    }

    public String getDiscVcrState() {
        return discVcrState;
    }

    public void setDiscVcrState(String discVcrState) {
        this.discVcrState = discVcrState;
    }

    public String getMonitorCamState() {
        return monitorCamState;
    }

    public void setMonitorCamState(String monitorCamState) {
        this.monitorCamState = monitorCamState;
    }

    public String getNetTest() {
        return netTest;
    }

    public void setNetTest(String netTest) {
        this.netTest = netTest;
    }

    public String getNetRouteModuleState() {
        return netRouteModuleState;
    }

    public void setNetRouteModuleState(String netRouteModuleState) {
        this.netRouteModuleState = netRouteModuleState;
    }

    public String getNetLineState() {
        return netLineState;
    }

    public void setNetLineState(String netLineState) {
        this.netLineState = netLineState;
    }

    public String getNetRouteState() {
        return netRouteState;
    }

    public void setNetRouteState(String netRouteState) {
        this.netRouteState = netRouteState;
    }

    public String getStartLockNum() {
        return startLockNum;
    }

    public void setStartLockNum(String startLockNum) {
        this.startLockNum = startLockNum;
    }

    public String getLockLockNum() {
        return lockLockNum;
    }

    public void setLockLockNum(String lockLockNum) {
        this.lockLockNum = lockLockNum;
    }

    public String getChangeLock() {
        return changeLock;
    }

    public void setChangeLock(String changeLock) {
        this.changeLock = changeLock;
    }

    public String getAudioTest() {
        return audioTest;
    }

    public void setAudioTest(String audioTest) {
        this.audioTest = audioTest;
    }

    public String getScanTest() {
        return scanTest;
    }

    public void setScanTest(String scanTest) {
        this.scanTest = scanTest;
    }

    public String getOvertimeNum() {
        return overtimeNum;
    }

    public void setOvertimeNum(String overtimeNum) {
        this.overtimeNum = overtimeNum;
    }
}
