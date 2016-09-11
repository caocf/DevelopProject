package com.moge.gege.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.moge.gege.config.GlobalConfig;

public class LocationUtil
{
    private LocationClient mLocationClient = null;
    private BDLocationListener mListener = new MyLocationListener();

    private double mLatitude = 0.0;
    private double mLongitude = 0.0;

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

    public void getSelfLocation(Context ctx)
    {
        LocationManager locationManager = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);

        // if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        // {
        // Location location = locationManager
        // .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // if (location != null)
        // {
        // mLatitude = location.getLatitude();
        // mLongitude = location.getLongitude();
        // }
        // }
        // else
        {
            LocationListener locationListener = new LocationListener()
            {

                @Override
                public void onStatusChanged(String provider, int status,
                        Bundle extras)
                {

                }

                @Override
                public void onProviderEnabled(String provider)
                {

                }

                @Override
                public void onProviderDisabled(String provider)
                {

                }

                @Override
                public void onLocationChanged(Location location)
                {
                    if (location != null)
                    {
                        LogUtil.e(
                                "Map",
                                "Location changed : Lat: "
                                        + location.getLatitude() + " Lng: "
                                        + location.getLongitude());
                    }
                }
            };
            locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000, 0, locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null)
            {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
            }
        }
    }

    private void openGPSSettings(Context ctx)
    {
        LocationManager locationManager = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
        {
            return;
        }

        // Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        // ctx.startActivityForResult(intent, 0);
    }

    public void init(Context ctx)
    {
        mLocationClient = new LocationClient(ctx);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.setIsNeedAddress(false);
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
            if (mLatitude > 0 || mLongitude > 0)
            {
                GlobalConfig.setLatitude((float) location.getLatitude());
                GlobalConfig.setLongitude((float) location.getLongitude());
                LocationUtil.instance.stop();
            }
        }
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
