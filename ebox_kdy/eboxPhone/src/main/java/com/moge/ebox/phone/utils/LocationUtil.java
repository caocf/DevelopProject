package com.moge.ebox.phone.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.moge.ebox.phone.R;

public class LocationUtil 
{
    private LocationClient mLocationClient = null;
    private BDLocationListener mListener = new MyLocationListener();
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private FinishLocate mLocation;
    private static LocationUtil instance = null;

    public static LocationUtil instance()
    {
        if (instance == null)
        {
            instance = new LocationUtil();
        }
        return instance;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }


    public void init(Context ctx,boolean gps)
    {
        mLocationClient = new LocationClient(ctx);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setOpenGps(gps);//是否启用gps定位
        option.setScanSpan(5000);
        option.setProdName(ctx.getResources().getString(R.string.app_name));//可选
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(mListener);
    }

    public void start()
    {
        if (mLocationClient != null)
        {
            mLocationClient.start();
        }
    }

    public void stop()
    {
        if (mLocationClient != null)
        {
            mLocationClient.stop();
        }
    }
    
    /**
     * 定位完成返回接口
     * @param l
     */
    public void setCompLocation(FinishLocate l){
    	this.mLocation=l;
    }

    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null)
                return;
            
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
            if (location.getLocType() == BDLocation.TypeGpsLocation)
            {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
            {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
            }
            LogUtil.i(sb.toString());
            // stop locate service after get result
            if (mLatitude > 0 && mLongitude > 0)
            {
                LocationUtil.instance.stop();
            }
            if (mLocation!=null) {
				mLocation.finishLocate(location);
			}
        }
    }
    
    public abstract interface FinishLocate{
    	abstract void finishLocate(BDLocation location);
    }
    
    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double distanceOfTwoPoints(double lat1, double lng1,
            double lat2, double lng2)
    {
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

    public static double distanceOfTwoPoints(double geo1[], double geo2[])
    {
        return distanceOfTwoPoints(geo1[1], geo1[0], geo2[1], geo2[0]);
    }

}
