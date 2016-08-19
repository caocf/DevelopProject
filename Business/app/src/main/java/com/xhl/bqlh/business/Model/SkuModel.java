package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Sum on 16/6/15.
 */
public class SkuModel implements Serializable {

    private int unitConversion;//换算比

    private String unit;//最小单位

    private String largestUnit;//最大单位

    public int getCommisionProportion() {
        return unitConversion;
    }

    public void setCommisionProportion(int commisionProportion) {
        this.unitConversion = commisionProportion;
    }

    public String getUnit() {
        if (TextUtils.isEmpty(unit)) {
            return "个";
        }
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLargestUnit() {
        return largestUnit;
    }

    public void setLargestUnit(String largestUnit) {
        this.largestUnit = largestUnit;
    }
}
