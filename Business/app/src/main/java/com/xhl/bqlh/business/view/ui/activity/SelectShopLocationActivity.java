package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.LocationService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/4/6.
 */
@ContentView(R.layout.activity_shop_select_location)
public class SelectShopLocationActivity extends BaseAppActivity implements BDLocationListener, OnGetGeoCoderResultListener {

    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";

    @ViewInject(R.id.map_view)
    private MapView mMapView;

    @ViewInject(R.id.iv_target_location)
    private ImageView mIvTargetLocation;

    @ViewInject(R.id.tv_target_location)
    private TextView mTvTargetLocation;

    @ViewInject(R.id.ll_location)
    private View mContent;

    private BaiduMap mBaiduMap;
    private LocationService mService;
    private boolean isFirstLoc = true;
    private LatLng mSelfLocation;
    //编译地理位置
    private GeoCoder mGeoSearch;
    private LatLng mSelectLatLng;
    private ReverseGeoCodeResult.AddressComponent mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //定位参数
        mService = getAppApplication().mLocation;
        mService.registerListener(this);
        mService.start();

        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle("标记店铺位置");

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setBuildingsEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);

        Drawable drawable = mIvTargetLocation.getDrawable();
        getTintDrawable(drawable, R.color.colorAccent);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                mTvTargetLocation.setText(R.string.locating);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng target = mapStatus.target;
                showSelectLocation(target);
            }
        });

    }

    private void showSelectLocation(LatLng target) {
        mSelectLatLng = target;
        mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(target));
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mGeoSearch.setOnGetGeoCodeResultListener(null);
        mService.stop();
        mService.unregisterListener(this);
        mService = null;
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_select_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_ok) {

            if (mAddress != null && mSelectLatLng != null) {
                Intent intent = new Intent();
                intent.putExtra(LOCATION, mSelectLatLng);
                intent.putExtra(ADDRESS, mAddress);
                setResult(RESULT_OK, intent);
                this.finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            mSelfLocation = ll;
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(20.0f);

            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            mContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            mTvTargetLocation.setText(R.string.locating);
        } else {
            String hint = reverseGeoCodeResult.getAddress();
            mAddress = reverseGeoCodeResult.getAddressDetail();
            if (!TextUtils.isEmpty(hint)) {
                mTvTargetLocation.setText(hint);
            }
        }

    }
}
