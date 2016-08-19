package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.xhl_library.utils.TimeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sum on 16/6/3.
 */
public class ShopDisplayModel implements Serializable {

    public String shopId;

    private String time;

    private String remark;

    private String img;

    private double coordinateX;//经度

    private double coordinateY;//维度

    private String address;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShowTime() {
        return TimeUtil.timeToNow(time);
    }

    public String getShowRemark() {
        return remark;
    }

    public String getImageUrl() {
        List<String> list = getImageUrlList();
        if (list.size() > 0) {
            return NetWorkConfig.imageHost + list.get(0);
        }
        return null;
    }

    public List<String> getImageUrlList() {
        List<String> urls = new ArrayList<>();
        if (!TextUtils.isEmpty(img)) {
            //是否存在多张照片
            boolean contains = img.contains(",");
            if (contains) {
                String[] split = img.split(",");
                urls.addAll(Arrays.asList(split));
            } else {
                urls.add(img);
            }
        }
        return urls;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
