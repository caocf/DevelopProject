package com.ebox.mgt.ui.fragment.pollingfg.model;

/**
 * Created by prin on 2015/9/30.
 */
public class ProductModel {
    private boolean isTestAll;
    private int subPlatTest; //0:未测试/测试不通过，1：测试通过；
    private int netTest;
    private int cameraTest;
    private int scanTest;
    private int audioTest;
    private int umsTest;
    private int idcardTest;
    private int touchTest;
    private int screenTest;

    public int getTouchTest() {
        return touchTest;
    }

    public void setTouchTest(int touchTest) {
        this.touchTest = touchTest;
    }

    public int getScreenTest() {
        return screenTest;
    }

    public void setScreenTest(int screenTest) {
        this.screenTest = screenTest;
    }

    public int getSubPlatTest() {
        return subPlatTest;
    }

    public void setSubPlatTest(int subPlatTest) {
        this.subPlatTest = subPlatTest;
    }

    public int getNetTest() {
        return netTest;
    }

    public void setNetTest(int netTest) {
        this.netTest = netTest;
    }

    public int getCameraTest() {
        return cameraTest;
    }

    public void setCameraTest(int cameraTest) {
        this.cameraTest = cameraTest;
    }

    public int getScanTest() {
        return scanTest;
    }

    public void setScanTest(int scanTest) {
        this.scanTest = scanTest;
    }

    public int getAudioTest() {
        return audioTest;
    }

    public void setAudioTest(int audioTest) {
        this.audioTest = audioTest;
    }

    public int getUmsTest() {
        return umsTest;
    }

    public void setUmsTest(int umsTest) {
        this.umsTest = umsTest;
    }

    public int getIdcardTest() {
        return idcardTest;
    }

    public void setIdcardTest(int idcardTest) {
        this.idcardTest = idcardTest;
    }

    public boolean isTestAll() {
        return isTestAll;
    }

    public void setIsTestAll(boolean isTestAll) {
        this.isTestAll = isTestAll;
    }
}
