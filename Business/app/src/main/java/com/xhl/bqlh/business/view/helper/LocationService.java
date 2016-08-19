package com.xhl.bqlh.business.view.helper;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.view.event.AutoLocationEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/3/18.
 */
public class LocationService {
    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private defaultLocation mListener;
    private Object objLock = new Object();
    private static LocationService mLocation;

    public static LocationService initLocation(Context appContext) {
        if (mLocation == null) {
            mLocation = new LocationService(appContext);
        }
        return mLocation;
    }

    private LocationService(Context appContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(appContext);
                mListener = new defaultLocation();
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /**
     * 获取当前位置
     */
    public void resolveLocation() {
        if (mLocation != null) {
            registerListener(mListener);
            start();
        }
    }

    class defaultLocation implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                saveLocation(location);

                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        List poiList = location.getPoiList();
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }
                Log.v("location", sb.toString());

                unregisterListener(mListener);
                stop();

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                AutoLocationEvent event = new AutoLocationEvent();
                event.location = location;
                event.address = location.getAddrStr();
                event.latLng = latLng;
                event.nearBy = location.getLocationDescribe();
                event.stLatLng = latLng.longitude + "," + latLng.latitude;
                EventBus.getDefault().post(event);
            }
        }
    }

    public static void saveLocation(BDLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        GlobalParams.mLocation = location;
        GlobalParams.mAddr = location.getAddrStr();
        GlobalParams.mNearBy = location.getLocationDescribe();
        GlobalParams.mSelfLatLng = latLng;
        GlobalParams.mSelfLatLngStr = latLng.longitude + "," + latLng.latitude;
    }

    /***
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    /***
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        }
        return mOption;
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double distanceOfTwoPoints(double lat1, double lng1,
                                             double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static double distanceOfTwoPoints(double geo1[], double geo2[]) {
        return distanceOfTwoPoints(geo1[1], geo1[0], geo2[1], geo2[0]);
    }

    public static double distanceOfTwoPoints(LatLng start, LatLng target) {
        return distanceOfTwoPoints(start.latitude, start.longitude, target.latitude, target.longitude);
    }

}
