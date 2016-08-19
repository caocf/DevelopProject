package com.xhl.bqlh.view.helper;

import android.text.TextUtils;

import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.view.custom.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/7/7.
 */
public class GlobalCarInfo {

    private volatile static GlobalCarInfo globalCarInfo;

    private HashMap<Object, ArrayList<CarModel>> loadCarInfo;//首次加载的订货车数据

    private static List<BadgeView> showView = new ArrayList<>();

    //全局的购物车信息
    private HashMap<String, String> mMap;

    private GlobalCarInfo() {
        mMap = new HashMap<>();
    }

    public static GlobalCarInfo instance() {
        if (globalCarInfo == null) {
            synchronized (GlobalCarInfo.class) {
                if (globalCarInfo == null) {
                    globalCarInfo = new GlobalCarInfo();
                }
            }
        }
        return globalCarInfo;
    }

    public void addBadgeView(BadgeView view) {
        showView.add(view);
        view.setBadgeCount(mMap.size());
    }

    public void removeBadgeView(BadgeView view) {
        showView.remove(view);
    }

    private void notifyUpdateNum() {
        int size = mMap.size();
        for (BadgeView badge : showView) {
            badge.setBadgeCount(size);
        }
    }

    //获取购物车中数量
    public String getCarNum(String productId) {
        return mMap.get(productId);
    }

    //是否包含该商品
    public boolean containsId(String productId) {
        return mMap.containsKey(productId);
    }

    //添加数据
    public void putCarInfo(ProductModel product) {
        String productId = product.getId();
        mMap.put(productId, String.valueOf(product.mCurNum));
        //刷新数据
        notifyUpdateNum();
    }

    //添加数据
    public void putCarInfo(CarModel product) {
        String productId = product.getProductId();
        mMap.put(productId, String.valueOf(product.mCurNum));
        //刷新数据
        notifyUpdateNum();
    }

    public void clear() {
        mMap.clear();
        //刷新数据
        notifyUpdateNum();
    }


    public static void setProductNum(ProductModel product) {
        String carNum = GlobalCarInfo.instance().getCarNum(product.getId());
        if (!TextUtils.isEmpty(carNum)) {
            product.mCurNum = Integer.valueOf(carNum);
        } else {
            product.mCurNum = product.getOrderMinNum();
        }
    }

    public HashMap<Object, ArrayList<CarModel>> getLoadCarInfo() {
        //读取一次失效
        HashMap<Object, ArrayList<CarModel>> carInfo = this.loadCarInfo;
        this.loadCarInfo = null;
        return carInfo;
    }

    public void setLoadCarInfo(HashMap<Object, ArrayList<CarModel>> loadCarInfo) {
        this.loadCarInfo = loadCarInfo;
    }
}
