package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignConfigModel;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.custom.ShopInfoDialog;
import com.xhl.bqlh.business.view.helper.LocationService;
import com.xhl.bqlh.business.view.ui.recyclerHolder.BottomSheetItem;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailsActivity extends BaseAppActivity implements BDLocationListener,
        View.OnClickListener, OnGetGeoCoderResultListener {

    public static final int Location_type_1 = 1;//定位经纬度
    public static final int Location_type_2 = 2;//定位地理位置
    public static final String Location_type = "Location_type";//定位类型

    //View
    private MapView mMapView;
    private FrameLayout mFlContent;
    private TextView mTvTarget, mTvTargetDistance;
    private View mOperatorParent;//操作面板
    private RecyclerView mOperatorSheet;
    private RecyclerAdapter mOperatorAdapter;
    private BottomSheetItem.SheetItem mAccountItem;//赊账的item数据

    private FloatingActionButton mFabSign;
    private View mToggleSheet;//操作开关
    private ImageView mToggleView;
    private BaiduMap mBaiduMap;

    private boolean mShowOperator = true;

    private LocationService mService;
    //当前位置坐标
    private LatLng mCurLocation;
    private String mCurAddress = "";
    //目标位置坐标
    private LatLng mTargetLocation;
    //地理位置解析
    private GeoCoder mGeoSearch;

    //签到距离
    private int mSignDistance = 500;
    //是否强制在范围内签到
    private boolean mForceInDistance = true;

    //签到状态：1:拜访中 2：已拜访 @TaskType
    private int mSignState;
    //拜访类型
    private int mSignType;
    //拜访日期差
    private int mSignTime;
    //观察类型
    private int mVisitType;
    //是否已经进入签到范围
    private boolean mHasEnter = false;
    private String mDistance = "0";

    private BitmapDescriptor mTarget;

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        //设置标题
        setTitle(getIntent().getStringExtra(GlobalParams.Intent_shop_name));
        //签到距离
        SignConfigModel signConfigModel = PreferenceData.getInstance().signConfig();
        if (signConfigModel != null) {
            mSignDistance = signConfigModel.getShopSignDistance();
            mForceInDistance = signConfigModel.isForceShopSign();
        }
        //初始化数据
        initView();
        //拜访类型
        confirmType();
        //初始化数据
        initParams();
        //标记目标位置
        markerLocation();
        //获取赊账数量
        loadData();
    }

    private void loadData() {
        String id = getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        if (!TextUtils.isEmpty(id)) {
            ApiControl.getApi().shopAccountOrder(id, new Callback.CommonCallback<ResponseModel<Integer>>() {
                @Override
                public void onSuccess(ResponseModel<Integer> result) {
                    if (result.isSuccess()) {
                        if (result.getObj() > 0) {
                            mAccountItem.count = result.getObj();
                            mOperatorAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }

    }

    private void confirmType() {
        //设置签到按钮
        int type = getIntent().getIntExtra(GlobalParams.INTENT_VISIT_SHOP_TYPE, GlobalParams.VISIT_SHOP_SEARCH);
        mVisitType = type;
        //拜访的门店
        if (type == GlobalParams.VISIT_SHOP_TASK) {

            //获取任务状态
            mSignState = getIntent().getIntExtra(GlobalParams.Intent_visit_client_state, TaskType.STATE_UN_START);
            //任务类型
            mSignType = getIntent().getIntExtra(GlobalParams.Intent_visit_client_type, TaskType.TYPE_Normal);
            //默认当天拜访
            mSignTime = getIntent().getIntExtra(GlobalParams.Intent_visit_client_time, 0);
            if (mSignTime != 0) {
                //当天分正常和临时任务
                mFabSign.setVisibility(View.GONE);
            }

        } else if (type == GlobalParams.VISIT_SHOP_SEARCH) {//搜索的店铺

            mOperatorParent.setVisibility(View.GONE);
            //非当天任务都隐藏签到按钮
            mFabSign.setVisibility(View.GONE);
        } else if (type == GlobalParams.VISIT_SHOP_SIGN) {//考勤的地点

            mOperatorParent.setVisibility(View.GONE);
            mFabSign.setVisibility(View.GONE);
            //考勤签到距离
            int distance = getIntent().getIntExtra("distance", 0);
            if (distance != 0) {
                mSignDistance = distance;
            }
            mTvTarget.setCompoundDrawables(null, null, null, null);
            setTitle("考勤地点");
        }
        if (type == GlobalParams.VISIT_SHOP_SIGN) {
            mTarget = BitmapDescriptorFactory.fromResource(R.drawable.ic_target);
        } else {
            mTarget = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_target);
        }
    }

    private void initView() {
        //创建地图
        MapStatus ms = new MapStatus.Builder().zoom(15).overlook(-20).build();
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms).compassEnabled(false).zoomControlsEnabled(false);
        mMapView = new MapView(this, bo);
        mFlContent = _findViewById(R.id.map_content);
        mFlContent.addView(mMapView);

        //初始化View
        mTvTarget = _findViewById(R.id.tv_target_location);
        mTvTargetDistance = _findViewById(R.id.tv_target_location_distance);
        mOperatorSheet = _findViewById(R.id.recycler_view);
        mToggleSheet = _findViewById(R.id.fl_toggle_operator);
        mToggleView = _findViewById(R.id.iv_toggle);
        mFabSign = _findViewById(R.id.fab_sign);
        mOperatorParent = _findViewById(R.id.ll_operator);
    }

    private void markerLocation() {

        int type = getIntent().getIntExtra(Location_type, 0);
        //初始化搜索
        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
        mTvTarget.setText(R.string.locating);

        //精确定位
        if (type == Location_type_1) {
            double latitude = getIntent().getDoubleExtra(GlobalParams.Intent_shop_latitude, 32);
            double longitude = getIntent().getDoubleExtra(GlobalParams.Intent_shop_longitude, 120);
            LatLng target = new LatLng(latitude, longitude);
            mTargetLocation = target;
            addLayer(target);
            //获取位置
            mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(target));
            AVAnalytics.onEvent(this, "accurate location");
        }
        //地理位置解析
        else if (type == Location_type_2) {
            String city = getIntent().getStringExtra(GlobalParams.Intent_shop_city);
            String address = getIntent().getStringExtra(GlobalParams.Intent_shop_street);
            //搜索位置
            mGeoSearch.geocode(new GeoCodeOption().city(city).address(address));
            AVAnalytics.onEvent(this, "geocode location");
        }

    }

    //添加圆形范围图层
    private void addLayer(LatLng target) {

        MarkerOptions marker = new MarkerOptions().position(target).icon(mTarget).zIndex(10).
                animateType(MarkerOptions.MarkerAnimateType.none).draggable(false);
        //范围圆圈
        if (mVisitType != GlobalParams.VISIT_SHOP_SEARCH) {
            OverlayOptions ooCircle = new CircleOptions().fillColor(0x26009688)
                    .center(target).stroke(new Stroke(2, 0x405677fc))
                    .radius(mSignDistance);
            mBaiduMap.addOverlay(ooCircle);
        }
        mBaiduMap.addOverlay(marker);
        //地图中心位置
        toTarget(target);
    }

    private void operatorSheet(boolean show) {
        int height = mOperatorSheet.getMeasuredHeight();
        if (show) {
            mToggleView.animate().rotationX(0).start();
            mOperatorParent.animate().translationY(0).start();
        } else {
            mToggleView.animate().rotationX(180).start();
            mOperatorParent.animate().translationY(height).start();
        }
    }


    protected void initParams() {
        super.initToolbar();

        mBaiduMap = mMapView.getMap();
        //关闭旋转和俯视
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        //开启定位突图层
        mBaiduMap.setMyLocationEnabled(true);

        //签到
        mFabSign.setOnClickListener(this);
        //操作开关
        mToggleSheet.setOnClickListener(this);
        //目标位置
        findViewById(R.id.fl_target_location).setOnClickListener(this);

        //设置操作参数
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mOperatorSheet.setLayoutManager(manager);
        mOperatorSheet.setHasFixedSize(false);

        List<RecyclerDataHolder> data = getOperatorData();

        mOperatorAdapter = new RecyclerAdapter(this, data);
        mOperatorSheet.setAdapter(mOperatorAdapter);

        //启动定位监听
        mService = getAppApplication().mLocation;
        mService.registerListener(this);
    }

    //用户操作按钮
    private List<RecyclerDataHolder> getOperatorData() {
        List<RecyclerDataHolder> holders = new ArrayList<>();

        BottomSheetItem.SheetItem item = new BottomSheetItem.SheetItem();
        //拜访下单
        item.id = R.drawable.ic_shop_task;
        item.name = getString(R.string.shop_detail_order_self);
        Intent intent = new Intent(this, SelectProductActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_ORDER_SELF);
        item.intent = intent;
        holders.add(new BottomSheetItem(item));

        //车销下单
        item = new BottomSheetItem.SheetItem();
        intent = new Intent(this, SelectCarProductActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_ORDER_STORE);
        item.intent = intent;
        item.id = R.drawable.ic_shop_order_car;
        item.name = getString(R.string.shop_detail_order_store);
        holders.add(new BottomSheetItem(item));

        //门店订单
        item = new BottomSheetItem.SheetItem();
        item.id = R.drawable.ic_shop_order_all;
        item.name = getString(R.string.shop_detail_order);
        intent = new Intent(this, ShopOrderActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        item.intent = intent;
        holders.add(new BottomSheetItem(item));

        //账单查看
        item = new BottomSheetItem.SheetItem();
        item.id = R.drawable.ic_shop_account;
        item.name = getString(R.string.shop_detail_money);
        intent = new Intent(this, StatisticsShopDetailsActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        intent.putExtra(GlobalParams.Intent_shop_name, getIntent().getStringExtra(GlobalParams.Intent_shop_name));
        item.intent = intent;
        mAccountItem = item;//记录赊账item
        holders.add(new BottomSheetItem(item));

        //陈列照片
        item = new BottomSheetItem.SheetItem();
        item.id = R.drawable.ic_shop_display;
        item.name = getString(R.string.shop_detail_display);
        intent = new Intent(this, ShopDisplayActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        intent.putExtra(GlobalParams.Intent_shop_name, getIntent().getStringExtra(GlobalParams.Intent_shop_name));
        item.intent = intent;
        holders.add(new BottomSheetItem(item));

        //申请退货
        item = new BottomSheetItem.SheetItem();
        item.id = R.drawable.ic_shop_return;
        item.name = getString(R.string.shop_detail_order_return);
        intent = new Intent(this, SelectProductActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_ORDER_RETURN);
      /*  item.callback = new BottomSheetItem.SheetItemClickCallback() {
            @Override
            public void onClick(int tag) {
                ToastUtil.showToastShort("功能建设中");
            }
        };*/
        item.intent = intent;
        holders.add(new BottomSheetItem(item));


        return holders;
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        mService.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mService.stop();
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mGeoSearch.setOnGetGeoCodeResultListener(null);
        mGeoSearch = null;
        mService.unregisterListener(this);
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mTarget.recycle();
        mTarget = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 10) {
            boolean booleanExtra = data.getBooleanExtra("data", false);
            if (booleanExtra) {
                mSignState = TaskType.STATE_FINISH;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int order_type = intent.getIntExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, 0);
        //车销下单跳出支付界面
        if (order_type == ConfirmProductActivity.TYPE_ORDER_STORE) {
            startActivity(new Intent(this, PayActivity.class));
            ToastUtil.showToastLong(R.string.order_hint);
        } else if (order_type == ConfirmProductActivity.TYPE_ORDER_SELF) {
            //拜访下单
            ToastUtil.showToastLong(R.string.order_hint);
        } else if (order_type == ConfirmProductActivity.TYPE_ORDER_RETURN) {
//            ToastUtil.showToastShort("申请退货成功");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mVisitType == GlobalParams.VISIT_SHOP_TASK) {
            getMenuInflater().inflate(R.menu.user_menu_shop_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //门店纠错
        if (id == R.id.menu_shop_update) {
            new ShopInfoDialog(ShopDetailsActivity.this, getIntent().getStringExtra(GlobalParams.Intent_shop_id)).show();
            return true;
        }

        //当前位置
        if (id == R.id.menu_self_location) {
            if (mCurLocation != null) {
                toTarget(mCurLocation);
            } else {
                mService.stop();
                mService.start();
                SnackUtil.shortShow(mFlContent, R.string.locating);
            }
            return true;
        }
        //目标位置
        if (id == R.id.menu_target_location) {
            if (mTargetLocation != null) {
                toTarget(mTargetLocation);
            }
            return true;
        }
        //导航
        if (id == R.id.menu_navigation) {
            if (mCurLocation == null || mTargetLocation == null) {
                SnackUtil.shortShow(mFlContent, R.string.locating);
                return true;
            }
            NaviParaOption para = new NaviParaOption()
                    .startPoint(mCurLocation).endPoint(mTargetLocation)
                    .startName("当前位置").endName(getIntent().getStringExtra(GlobalParams.Intent_shop_name));
            try {
                BaiduMapNavigation.openBaiduMapNavi(para, ShopDetailsActivity.this);
            } catch (BaiduMapAppNotSupportNaviException e) {
                e.printStackTrace();
                OpenClientUtil.getLatestBaiduMapApp(ShopDetailsActivity.this);
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
        mCurAddress = location.getAddrStr();
        //添加自己的当前位置
        mBaiduMap.setMyLocationData(locData);
        //自己的位置
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        if (mCurLocation == null && mVisitType == GlobalParams.VISIT_SHOP_SIGN) {
            toTarget(ll);
        }
        mCurLocation = ll;
        //位置
        showDistance(ll, mTargetLocation);
    }

    //设置距离
    private void showDistance(LatLng start, LatLng end) {
        if (start == null || end == null) {
            return;
        }
        double distance = DistanceUtil.getDistance(start, end);
        String noDecimal = NumberUtil.getDoubleNoDecimal(distance);
        mTvTargetDistance.setText(getString(R.string.shop_sign_distance, noDecimal));
        //当前点是否以及进入圆圈之内
        mHasEnter = SpatialRelationUtil.isCircleContainsPoint(mTargetLocation, mSignDistance, mCurLocation);
        mDistance = noDecimal;
    }

    //移动到目标点
    private void toTarget(LatLng latLng) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_toggle_operator:
                mShowOperator = !mShowOperator;
                operatorSheet(mShowOperator);
                break;
            case R.id.fl_target_location:
                if (mTargetLocation != null) {
                    toTarget(mTargetLocation);
                }
                break;
            case R.id.fab_sign:
                //拜访完成
                if (mSignState == TaskType.STATE_FINISH) {
                    SnackUtil.longShow(mToolbar, R.string.shop_sign_finish);
                } else if (mSignState == TaskType.STATE_START) {
                    if (mForceInDistance && !mHasEnter) {
                        SnackUtil.shortShow(v, getString(R.string.shop_sign_limit, mSignDistance));
                    } else {
                        if (mCurLocation == null) {
                            SnackUtil.shortShow(v, R.string.locating);
                        } else {
                            Intent intent = new Intent(this, ShopSignActivity.class);
                            intent.putExtra(ShopSignActivity.INTENT_Sign_Address, mCurAddress);
                            intent.putExtra(ShopSignActivity.INTENT_Sign_Distance, mDistance);
                            intent.putExtra(ShopSignActivity.INTENT_Sign_LatLng, mCurLocation);
                            intent.putExtra(ShopSignActivity.INTENT_Sign_Task_Type, mSignType);
                            intent.putExtra(ShopSignActivity.INTENT_Sign_Task_Enter, mHasEnter);
                            intent.putExtra(GlobalParams.Intent_shop_id, getIntent().getStringExtra(GlobalParams.Intent_shop_id));
                            startActivityForResult(intent, 10);
                        }
                    }
                }

                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtil.showToastLong("店铺定位失败");
            mTvTarget.setText("店铺定位失败");
            return;
        }
        LatLng location = geoCodeResult.getLocation();
        mTargetLocation = location;

        String address = geoCodeResult.getAddress();

        mTvTarget.setText(address);
        showDistance(mCurLocation, location);

        addLayer(location);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtil.showToastLong("店铺定位失败");
            mTvTarget.setText("店铺定位失败");
            return;
        }
        String district = reverseGeoCodeResult.getAddress();
        if (!TextUtils.isEmpty(district)) {
            mTvTarget.setText(district);
        }
    }
}
